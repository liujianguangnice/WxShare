package com.fanneng.useenergy.util;

/**
 * @author ：mp5a5 on 2018/10/9 10：41
 * @describe
 * @email：wwb199055@126.com
 */
public interface OnResponseListener {
  void onSuccess();

  void onCancel();

  void onFail(String message);
}
