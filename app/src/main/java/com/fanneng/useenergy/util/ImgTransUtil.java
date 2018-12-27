package com.fanneng.useenergy.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ScrollView;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

/**
 * 作者： liujianguang on 2018/10/12 17:52
 * 邮箱： liujga@enn.cn
 */
public class ImgTransUtil {

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

    }

    /**
     * Bitmap转byte数组的两种方式
     */
    public static byte[] getBytesByBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bitmap.getByteCount());
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * 压缩图片（质量压缩）
     * Bitmap 转byte
     * 如果分享的图片大于35k，微信客户端是没有响应的，因此做了判断，这是官方demo没有的，官方文档有讲解
     */
    public static byte[] compressBitmapToByte(@NonNull Bitmap bitmap) {
        /*ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到outputStream中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        int options = 100;
        LogUtils.i("-----图片未压缩前的大小:" + bytesTrans((outputStream.toByteArray().length)));
        //循环判断如果压缩后图片是否大于500kb,大于继续压缩
        while (outputStream.toByteArray().length / 1024 >= 32) {
            //重置outputStream即清空outputStream
            outputStream.reset();
            //每次都减少10
            options -= 10;
            LogUtils.e("图片质量："+options+"");
            //这里压缩options%，把压缩后的数据存放到outputStream中
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
            long length = outputStream.toByteArray().length;
            LogUtils.i("-----------------图片压缩后的大小:" + bytesTrans(length));
        }
        return outputStream.toByteArray();*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//bitmap是一张图片

        LogUtils.i("-----图片未压缩前的大小:" + bytesTrans((baos.toByteArray().length)));

        bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
        /*该平方根表示大约缩进zoom倍，实际存储大小会接近32KB，可以自己算一下，就是长乘以宽*/
        float zoom = (float) Math.sqrt(32 * 1024 / (float) baos.toByteArray().length);
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        baos.reset();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        /*压缩到32KB为止*/
        while (baos.toByteArray().length > 32 * 1024) {
            matrix.setScale(0.8f, 0.8f);
            resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0, resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);
            baos.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        long length = baos.toByteArray().length;
        LogUtils.i("-----------------图片压缩后的大小:" + bytesTrans(length));
        return  baos.toByteArray();

    }

    /**
     * 计算图片大小
     */
    public static String bytesTrans(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);

        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }

    public static Bitmap getViewGroupBitmap(ViewGroup view) {
        int h = 0;
        // 获取listView实际高度
        for (int i = 0; i < view.getChildCount(); i++) {
            h += view.getChildAt(i).getHeight();
        }
        //Log.d(TAG, "实际高度:" + h);
        Log.i("TAG", "getListViewBitmap: ");
        Log.d("TAG", "list 高度:" + view.getHeight());
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * 截图RecycleView
     **/
    public static Bitmap getRecycleViewBitmap(RecyclerView recycleView) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < recycleView.getChildCount(); i++) {
            h += recycleView.getChildAt(i).getHeight();
        }
        //Log.d(TAG, "实际高度:" + h);
        Log.i("TAG", "getListViewBitmap: ");
        Log.d("TAG", "list 高度:" + recycleView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(recycleView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        recycleView.draw(canvas);
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getScrollViewBitmap(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 截取NestedScrollView的屏幕
     **/
    public static Bitmap getNestedScrollViewBitmap(NestedScrollView scrollView) {
        int h = 0;
        Bitmap bitmap;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 截取scrollview的屏幕
     **/
    public static Bitmap getViewBitmap(View view) {
        int h = 0;
        Bitmap bitmap;
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /*截图系统图，只显示第一次加载出来的view，只在Activity重建时显示view*/
    public static Bitmap capture(Activity activity) {
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bmp = activity.getWindow().getDecorView().getDrawingCache();
        return bmp;
    }

    /**
     * 截图listview
     **/
    public static Bitmap getListViewBitmap(ListView listView) {
        int h = 0;
        Bitmap bitmap;
        // 获取listView实际高度
        for (int i = 0; i < listView.getChildCount(); i++) {
            h += listView.getChildAt(i).getHeight();
        }
        //Log.d(TAG, "实际高度:" + h);
        Log.i("TAG", "getListViewBitmap: ");
        Log.d("TAG", "list 高度:" + listView.getHeight());
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(listView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        listView.draw(canvas);
        return bitmap;
    }

    //这是webview的，利用了webview的api
    private static Bitmap captureWebView(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
                snapShot.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }


    /**
     * 纵向拼接图片
     */
    public static Bitmap addBitmap(Bitmap first, Bitmap second) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, 0, first.getHeight(), null);
        return result;
    }


}
