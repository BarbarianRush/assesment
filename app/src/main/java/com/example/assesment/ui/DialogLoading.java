package com.example.assesment.ui;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.assesment.R;

/**
 * Created by dcastrillo on 06/10/2014.
 */
public class DialogLoading extends Dialog {

    public static final Integer TAG_ACCEPT = 1;

    public Activity activity;
    public View.OnClickListener onClickListener;
    public Button bt_dialog_loading_accept;
    public TextView tv_dialog_loading_title, tv_dialog_loading_text;
    public ProgressBar pb_dialog_loading;

    public DialogLoading(Activity activity, View.OnClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        tv_dialog_loading_title = (TextView) this.findViewById(R.id.tv_dialog_loading_title);
        tv_dialog_loading_text = (TextView) this.findViewById(R.id.tv_dialog_loading_text);
        bt_dialog_loading_accept = (Button) this.findViewById(R.id.bt_dialog_loading_accept);
        pb_dialog_loading = (ProgressBar) this.findViewById(R.id.pb_dialog_loading);
        bt_dialog_loading_accept.setTag(TAG_ACCEPT);
        bt_dialog_loading_accept.setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {

    }

    public void setCustomTitle(final String title){
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                tv_dialog_loading_title.setText(title);
            }
        });
    }

    public void setTextBody(final String textBody){
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                tv_dialog_loading_text.setText(textBody);
            }
        });
    }

    public void setAcceptButtonText(final String text){
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                bt_dialog_loading_accept.setText(text);
            }
        });
    }

    public void setPb_dialog_loadingVisibility(final int visibility){
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                pb_dialog_loading.setVisibility(visibility);
            }
        });
    }

    public void setTextBodyColor(final int color) {
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                tv_dialog_loading_text.setTextColor(color);
            }
        });
    }

    public void setBt_dialog_loading_acceptVisibility(final int visibility){
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                bt_dialog_loading_accept.setVisibility(visibility);
            }
        });
    }

    public ProgressBar getPb_dialog_loading() {
        return pb_dialog_loading;
    }

    public Button getBt_dialog_loading_accept() {
        return bt_dialog_loading_accept;
    }


}
