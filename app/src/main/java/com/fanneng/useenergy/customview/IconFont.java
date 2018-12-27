package com.fanneng.useenergy.customview;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;

import com.fanneng.useenergy.config.FNengConfig;

import java.util.HashMap;

@SuppressLint("AppCompatCustomView")
public class IconFont extends TextView {

    private static final String TTF_NAME_USER = "fanneng_iconfont.ttf";
    private static float DEFAULT_SCALEX = 1.0f;
    private static HashMap<String, Typeface> sFontMap = new HashMap<String, Typeface>();

    public IconFont(Context context) {
        super(context);
        init(null);
        setTextScaleX(DEFAULT_SCALEX);
    }

    /*public IconFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IconFont);
        int version = array.getInt(R.styleable.IconFont_version, 2);
        String ttfName = array.getString(R.styleable.IconFont_ttfName);
        array.recycle();
        init(ttfName);
        setTextScaleX(DEFAULT_SCALEX);
    }

    public IconFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.IconFont);
        int version = array.getInt(R.styleable.IconFont_version, 2);
        String ttfName = array.getString(R.styleable.IconFont_ttfName);
        array.recycle();
        init(ttfName);
        setTextScaleX(DEFAULT_SCALEX);
    }*/

    /**
     * @param ttfName 指定iconfont库名称。默认没有指定使用哪一个iconfont库。若是指定了使用哪个iconfont库，则直接根据ttfName使用此库
     */
    private void init(String ttfName) {
        if (!isInEditMode()) {
            if (!TextUtils.isEmpty(ttfName)) {
                setIconFontTypeface(ttfName);
                return;
            }

            initUserTtf();
        }
    }

    private void initUserTtf() {

        setIconFontTypeface(TTF_NAME_USER);
    }

    private void setIconFontTypeface(String ttfName) {
        Typeface typeface = sFontMap.get(ttfName);
        if (typeface == null) {
            Application context = FNengConfig.getApplication();
            typeface = Typeface.createFromAsset(context.getAssets(), "font/" + ttfName);
            sFontMap.put(ttfName, typeface);
        }
        setTypeface(typeface);
    }

    public static Typeface getUserIconFont() {
        Typeface typeface = sFontMap.get(TTF_NAME_USER);
        if (typeface == null) {
            Application context = FNengConfig.getApplication();
            typeface = Typeface.createFromAsset(context.getAssets(), "font/" + TTF_NAME_USER);
            sFontMap.put(TTF_NAME_USER, typeface);
        }
        return typeface;
    }
}


