package com.att.attcase;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

/**
 * Created by mac on 6/6/17.
 */

public class DialogXayDungCase extends Dialog implements View.OnClickListener {

    public Dialog d;
    public XayDungCase c;
    public Button btn1,btn2;

    public DialogXayDungCase(XayDungCase c) {
        super(c);
        this.c = c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_savedialog);
        btn1 = (Button) findViewById(R.id.btn_ok_dialog);
        btn2 = (Button) findViewById(R.id.btn_no_dialog);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok_dialog:
                c.xacnhan();
                break;

            case R.id.btn_no_dialog:
                dismiss();
                break;
        }
    }
}
