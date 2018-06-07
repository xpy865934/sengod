package com.sengod.sengod.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sengod.sengod.R;

public class LoadingDialog {
    private static  Dialog dialog=null;

    public static Dialog getDialog(Context context,String title){
        dialog=new Dialog(context, R.style.loadDialog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View layout = LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);
        TextView textView1 = layout.findViewById(R.id.textView1);
        textView1.setText(title);
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}
