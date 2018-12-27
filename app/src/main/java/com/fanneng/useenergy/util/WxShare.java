package com.fanneng.useenergy.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * @author ：mp5a5 on 2018/10/9 10：43
 * @describe
 * @email：wwb199055@126.com
 */
public class WxShare {

  public static final String APP_ID = "wxf02352a2726bdb7e";
  public static final String ACTION_SHARE_RESPONSE = "action_wx_share_response";
  public static final String EXTRA_RESULT = "result";
  private final Context context;
  private final IWXAPI api;
  private OnResponseListener listener;
  private ResponseReceiver receiver;

  public WxShare(Context context) {
    api = WXAPIFactory.createWXAPI(context, APP_ID);
    this.context = context;
  }

  public WxShare register() {
    // 微信分享
//        api.registerApp(APP_ID);
    receiver = new ResponseReceiver();
    IntentFilter filter = new IntentFilter(ACTION_SHARE_RESPONSE);
    context.registerReceiver(receiver, filter);
    return this;
  }

  public void unregister() {
    try {
      api.unregisterApp();
      context.unregisterReceiver(receiver);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
  *   //wxShare.share("https://open.weixin.qq.com","I am so crazy");
  * */
  public WxShare share(String text) {
    WXTextObject textObj = new WXTextObject();
    textObj.text = text;
    WXMediaMessage msg = new WXMediaMessage();
    msg.mediaObject = textObj;
    // msg.title = "Will be ignored";
    msg.description = text;
    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction = buildTransaction("text");
    req.message = msg;
    req.scene = SendMessageToWX.Req.WXSceneSession;
    boolean result = api.sendReq(req);
//        Logger.i("text shared: " + result);
    return this;
  }

  public void sharePicture(Bitmap bitmap) {
//    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.demo_share);
    Bitmap bmp = bitmap;
    WXImageObject imgObj = new WXImageObject(bmp);
    WXMediaMessage msg = new WXMediaMessage();
    msg.mediaObject = imgObj;

    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);
    //设置缩略图
    msg.thumbData =ImgTransUtil.compressBitmapToByte(thumbBmp);
    //bmp.recycle();

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    req.transaction =String.valueOf(System.currentTimeMillis());
    req.message = msg;
    //WXSceneTimeline朋友圈    WXSceneSession聊天界面
    req.scene = SendMessageToWX.Req.WXSceneSession;
    api.sendReq(req);

  }

  public WxShare share(String url, String title) {

    WXWebpageObject webpage = new WXWebpageObject();
    webpage.webpageUrl = url;

    WXMediaMessage msg = new WXMediaMessage(webpage);
    msg.title = title;
    msg.description = title;
//        msg.thumbData = getWXThumb(bitmap).toByteArray();

    SendMessageToWX.Req req = new SendMessageToWX.Req();
    //WXSceneTimeline朋友圈    WXSceneSession聊天界面
    req.scene = SendMessageToWX.Req.WXSceneSession;
    req.message = msg;
    req.transaction = String.valueOf(System.currentTimeMillis());
    api.sendReq(req);


//        WXWebpageObject webPage = new WXWebpageObject();
//        webPage.webpageUrl = url;
//        WXMediaMessage msg = new WXMediaMessage();
////        msg.mediaObject = textObj;
//        msg.title = title;
//        msg.description = title;
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//        api.sendReq(req);
//        Logger.i("text shared: " + result);
    return this;
  }

  public IWXAPI getApi() {
    return api;
  }

  public void setListener(OnResponseListener listener) {
    this.listener = listener;
  }

  private String buildTransaction(final String type) {
    return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
  }

  private class ResponseReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Response response = intent.getParcelableExtra(EXTRA_RESULT);
//            Logger.d("type: " + response.getType());
//            Logger.d("errCode: " + response.errCode);
      String result;
      if (listener != null) {
        if (response.errCode == BaseResp.ErrCode.ERR_OK) {
          listener.onSuccess();
        } else if (response.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
          listener.onCancel();
        } else {
          switch (response.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
              result = "发送被拒绝";
              break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
              result = "不支持错误";
              break;
            default:
              result = "发送返回";
              break;
          }
          listener.onFail(result);
        }
      }
    }
  }

  public static class Response extends BaseResp implements Parcelable {
    public int errCode;
    public String errStr;
    public String transaction;
    public String openId;
    private int type;
    private boolean checkResult;

    public Response(BaseResp baseResp) {
      errCode = baseResp.errCode;
      errStr = baseResp.errStr;
      transaction = baseResp.transaction;
      openId = baseResp.openId;
      type = baseResp.getType();
      checkResult = baseResp.checkArgs();
    }

    @Override
    public int getType() {
      return type;
    }

    @Override
    public boolean checkArgs() {
      return checkResult;
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.errCode);
      dest.writeString(this.errStr);
      dest.writeString(this.transaction);
      dest.writeString(this.openId);
      dest.writeInt(this.type);
      dest.writeByte(this.checkResult ? (byte) 1 : (byte) 0);
    }

    protected Response(Parcel in) {
      this.errCode = in.readInt();
      this.errStr = in.readString();
      this.transaction = in.readString();
      this.openId = in.readString();
      this.type = in.readInt();
      this.checkResult = in.readByte() != 0;
    }

    public static final Creator<Response> CREATOR = new Creator<Response>() {
      @Override
      public Response createFromParcel(Parcel source) {
        return new Response(source);
      }

      @Override
      public Response[] newArray(int size) {
        return new Response[size];
      }
    };
  }
}
