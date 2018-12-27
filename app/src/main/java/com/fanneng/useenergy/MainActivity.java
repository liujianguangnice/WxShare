package com.fanneng.useenergy;

import android.os.Bundle;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }


    @OnClick(R.id.bt_share)
    public void onViewClicked() {
        shareOrCopy();
    }
}
