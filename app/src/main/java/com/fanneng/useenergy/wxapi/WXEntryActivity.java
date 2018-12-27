package com.fanneng.useenergy.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanneng.useenergy.util.WxShare;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * @author ：mp5a5 on 2018/10/8 18：20
 * @describe
 * @email：wwb199055@126.com
 */

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
  private IWXAPI api;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    WxShare share = new WxShare(this);
    api = share.getApi();
    // .register() ;
    // 注意：
    // 第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
    try {
      if (!api.handleIntent(getIntent(), this)) {
        finish();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setIntent(intent);
    if (!api.handleIntent(intent, this)) {
      finish();
    }
  }

  @Override
  public void onReq(BaseReq baseReq) {
  }

  @Override
  public void onResp(BaseResp baseResp) {
    Intent intent = new Intent(WxShare.ACTION_SHARE_RESPONSE);
    intent.putExtra(WxShare.EXTRA_RESULT, new WxShare.Response(baseResp));
    sendBroadcast(intent);
    finish();
  }
}


