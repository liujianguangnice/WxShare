package com.fanneng.useenergy.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanneng.useenergy.R;

public class  ShareDialog extends CommonBottomDialog {

    private Context mContext;
    private LinearLayout shareLl;
    private TextView quitTv;
    private ImageView iv_capture;
    private Bitmap bitmap;

    public ShareDialog(Context context) {
        super(context, true, true);
        this.mContext = context;
    }

    public ShareDialog(Context context,Bitmap bitmap) {
        super(context, true, true);
        this.mContext = context;
        this.bitmap = bitmap;
    }

    @Override
    public View setLayoutView() {
        View view = View.inflate(mContext, R.layout.share_dialog, null);
        shareLl = view.findViewById(R.id.ll_weixin_share);
        quitTv = view.findViewById(R.id.tv_quit_cancel);
        iv_capture = view.findViewById(R.id.iv_capture);
        if(bitmap!=null){
            iv_capture.setImageBitmap(bitmap);
        }
        shareLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogListener.onTrue();
            }
        });


        quitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnDialogListener.onCancel();
            }
        });
        return view;
    }

    private OnShareDialogListener mOnDialogListener;

    public void setOnDialogListener(OnShareDialogListener onDialogListener) {
        this.mOnDialogListener = onDialogListener;
    }

    public interface OnShareDialogListener {

        void onTrue();

        void onCopy();

        void onCancel();

    }
}

