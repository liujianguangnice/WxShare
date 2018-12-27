package com.fanneng.useenergy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanneng.useenergy.customview.ShareDialog;
import com.fanneng.useenergy.util.DensityUtil;
import com.fanneng.useenergy.util.ImgTransUtil;
import com.fanneng.useenergy.util.OnResponseListener;
import com.fanneng.useenergy.util.StatusBarUtils;
import com.fanneng.useenergy.util.WxShare;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    private BaseActivity thisActivity = null;

    /*分享*/
    protected ShareDialog shareDialog;
    protected WxShare wxShare;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, initLayout(), null);
        setContentView(needHeader() ? getMergerView(view) : view);
        //沉浸式状态栏
        StatusBarUtils.translucent(this);
        ButterKnife.bind(this);
        thisActivity = this;

        initShare();
    }

    public BaseActivity getThisActivity() {
        return thisActivity;
    }


    /**
     * @return 初始化布局
     */
    protected abstract @LayoutRes
    int initLayout();

    protected boolean needHeader() {
        return false;
    }

    private View getMergerView(@NonNull View v) {

        View rootView;
        LinearLayout baseContainerRl;

        rootView = View.inflate(this, R.layout.title_bar_frag, null);


        return rootView;
    }





    public String getThisActivityName() {
        return thisActivity.getClass().getName();
    }

    public void gotoActivity(Class<?> clazz) {
        thisActivity.startActivity(new Intent(thisActivity, clazz));
    }

    public void gotoActivity(Class<?> clazz, boolean isFinish) {
        thisActivity.startActivity(new Intent(thisActivity, clazz));
        if (isFinish) {
            thisActivity.finish();
        }
    }

    public void gotoActivity(
            @NonNull Class<?> clazz, @NonNull Bundle bundle, @NonNull boolean isFinish) {
        Intent intent = new Intent(thisActivity, clazz);
        intent.putExtras(bundle);
        thisActivity.startActivity(intent);
        if (isFinish) {
            thisActivity.finish();
        }
    }


    protected void initShare() {
        wxShare = new WxShare(getThisActivity());
        wxShare.setListener(new OnResponseListener() {
            @Override
            public void onSuccess() { // 分享成功
                Log.i("TAG", "onSuccess: ");
            }

            @Override
            public void onCancel() { // 分享取消
                Log.i("TAG", "onCancel: ");
            }

            @Override
            public void onFail(String message) { // 分享失败
                Log.i("TAG", "onFail: ");
            }
        });

        wxShare.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
        wxShare.unregister();
    }

    public View getFakeTitle(String title,Boolean isHaveBack,Boolean isHaveFilter,Boolean isHaveShare){
        DisplayMetrics metric = new DisplayMetrics();
        getThisActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = DensityUtil.dip2px(this,44);
        View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.head_base_dialog, null, false);
        //去到指定view大小的函数
        layoutView(view, width, height);
        TextView tv_left_btn = view.findViewById(R.id.tv_left_btn);
        if(isHaveBack){
            tv_left_btn.setVisibility(View.VISIBLE);
        }else{
            tv_left_btn.setVisibility(View.INVISIBLE);
        }
        ImageView iv_right_img_filter = view.findViewById(R.id.iv_right_img_filter);
        if(isHaveFilter){
            iv_right_img_filter.setVisibility(View.VISIBLE);
        }else{
            iv_right_img_filter.setVisibility(View.INVISIBLE);
        }
        ImageView iv_right_img = view.findViewById(R.id.iv_right_img);
        if(isHaveShare){
            iv_right_img.setVisibility(View.VISIBLE);
        }else{
            iv_right_img.setVisibility(View.INVISIBLE);
        }

        TextView titleTv = view.findViewById(R.id.tv_middle_title);
        titleTv.setText(title);
        return view;
    }

    public void openShareDialog(Bitmap bitmap) {
        DisplayMetrics metric = new DisplayMetrics();
        getThisActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        View view = LayoutInflater.from(getThisActivity()).inflate(R.layout.share_dialog_bottom, null, false);
        //去到指定view大小的函数
        layoutView(view, width, height);
        final Bitmap boss = ImgTransUtil.addBitmap(bitmap, ImgTransUtil.getViewBitmap(view));


        shareDialog = new ShareDialog(getThisActivity(), boss);
        shareDialog.show();
        shareDialog.setOnDialogListener(new ShareDialog.OnShareDialogListener() {
            @Override
            public void onTrue() {
                Log.i("TAG", "onTrue: ");
                wxShare.sharePicture(boss);
            }

            @Override
            public void onCopy() {

            }

            @Override
            public void onCancel() {
                Log.i("TAG", "onCancel: ");
                shareDialog.dismiss();
            }
        });
    }
    /**
     * View和其内部的子View都具有了实际大小，也就是完成了布局，
     * 相当与添加到了界面上。接着就可以创建位图并在上面绘制了：
     */
    public void layoutView(View v, int width, int height) {
        // 指定整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }
    public void shareOrCopy() {
        Bitmap bitmap = ImgTransUtil.getViewBitmap(getWindow().getDecorView());
        openShareDialog(bitmap);
    }
    public void shareOrCopy(View contentView) {
        Bitmap bitmap = ImgTransUtil.getViewBitmap(contentView);
        openShareDialog(bitmap);
    }

    public void shareOrCopy(View headView ,ViewGroup middleView ) {
        Bitmap bitmap = ImgTransUtil.addBitmap(ImgTransUtil.getViewBitmap(headView),  ImgTransUtil.getViewGroupBitmap(middleView));
        openShareDialog(bitmap);
    }
    public void shareOrCopy(View headView ,View titleView ,ViewGroup middleView ) {
        Bitmap headViewAll = ImgTransUtil.addBitmap(ImgTransUtil.getViewBitmap(headView),  ImgTransUtil.getViewBitmap(titleView));
        Bitmap bitmap = ImgTransUtil.addBitmap(headViewAll,  ImgTransUtil.getViewGroupBitmap(middleView));
        openShareDialog(bitmap);
    }

}
