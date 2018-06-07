package com.sengod.sengod.ui.dialog;

import android.content.Context;

import com.sengod.sengod.ui.customerview.CustomerDialog;

/**
 * Created by Administrator on 2018/5/29.
 */

public class MyDialog {
    private static CustomerDialog customerDialog=null;

    public static CustomerDialog getDisConnectedDialog(Context context, CustomerDialog.OnDialogButtonClickListener listener) {
        CustomerDialog.Builder builder = new CustomerDialog.Builder(context);
        builder.setOnButtonClickListener(listener);
        customerDialog = builder.create();
        return customerDialog;
    }
}
