package com.fanneng.useenergy.util;

import android.content.Context;

/**
 * 作者：ZhangChuan
 * 创建日期： 2016/9/29 9:56
 * 描述：像素转换
 */
public class DensityUtil {

    private DensityUtil(){}
    private static DensityUtil sDensityUtil;
    public synchronized static DensityUtil getInstance(){

        if(sDensityUtil==null){
            sDensityUtil =new DensityUtil();
        }
        return sDensityUtil;
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
