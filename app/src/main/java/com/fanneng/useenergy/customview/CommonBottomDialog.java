package com.fanneng.useenergy.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fanneng.useenergy.R;


/**
 * 作者：王文彬 on 2018/4/19 09：59
 * 邮箱：wwb199055@126.com
 */

public abstract class CommonBottomDialog extends Dialog {


  //控制点击dialog外部是否dismiss
  private boolean iscancelable;
  //控制返回键是否dismiss
  private boolean isBackCancelable;
  private Context context;

  public CommonBottomDialog(Context context, boolean isCancelable, boolean isBackCancelable) {
    super(context, R.style.MyDialog);
    this.context = context;
    this.iscancelable = isCancelable;

  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(setLayoutView());
    setCancelable(iscancelable);
    setCanceledOnTouchOutside(isBackCancelable);
    Window window = this.getWindow();
    window.setGravity(Gravity.BOTTOM);
    WindowManager.LayoutParams params = window.getAttributes();
    params.width = WindowManager.LayoutParams.MATCH_PARENT;
    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
    window.setAttributes(params);
  }

  public abstract View setLayoutView();
}
