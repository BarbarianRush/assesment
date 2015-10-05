package com.example.assesment.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.assesment.R;


/**
 * Created by dcastrillo on 03/10/2014.
 */
public class DialogDefault extends Dialog  {

    public static final Integer TAG_CANCEL = 0;
    public static final Integer TAG_ACCEPT = 1;

    public Activity activity;
    public View.OnClickListener onClickListener;
    public Button bt_dialog_default_accept, bt_dialog_default_cancel;
    public TextView tv_dialog_default_title, tv_dialog_default_text;


    public DialogDefault(Activity activity, View.OnClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_default);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        tv_dialog_default_title = (TextView) this.findViewById(R.id.tv_dialog_default_title);
        tv_dialog_default_text = (TextView) this.findViewById(R.id.tv_dialog_default_text);
        bt_dialog_default_accept = (Button) this.findViewById(R.id.bt_dialog_default_accept);
        bt_dialog_default_cancel = (Button) this.findViewById(R.id.bt_dialog_default_cancel);
        bt_dialog_default_accept.setTag(TAG_ACCEPT);
        bt_dialog_default_cancel.setTag(TAG_CANCEL);
        bt_dialog_default_accept.setOnClickListener(onClickListener);
        bt_dialog_default_cancel.setOnClickListener(onClickListener);

    }

    @Override
    public void onBackPressed() {

    }

    public void oneButtonMode(boolean isOneButtonMode){
        if(isOneButtonMode){
            bt_dialog_default_cancel.setVisibility(View.GONE);
        }else{
            bt_dialog_default_cancel.setVisibility(View.VISIBLE);
        }
    }

    public void setCustomTitle(String title){
        tv_dialog_default_title.setText(title);
    }

    public void setTextBody(String textBody){
        tv_dialog_default_text.setText(textBody);
    }

    public void setAcceptButtonText(String text){
        bt_dialog_default_accept.setText(text);
    }

    public void setCancelButtonText(String text){
        bt_dialog_default_cancel.setText(text);
    }

}