package com.sengod.sengod.ui.customerview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sengod.sengod.R;

public class CustomerDialog extends Dialog {

    public CustomerDialog(Context context) {
        super(context);
    }

    public CustomerDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private OnDialogButtonClickListener buttonClickListner;

        public Builder(Context context) {
            this.context = context;
        }

        public CustomerDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomerDialog dialog = new CustomerDialog(context, R.style.Dialog);
            //设置透明度
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.4f;
            window.setAttributes(lp);

            View layout = inflater.inflate(R.layout.disconnected_dialog, null);
            Button btn_ok = (Button)layout.findViewById(R.id.btn_ok);
            Button btn_exit = (Button)layout.findViewById(R.id.btn_exit);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buttonClickListner.okButtonClick();

                }
            });
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    buttonClickListner.exitButtonClick();
                }
            });

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            return dialog;
        }
        public void setOnButtonClickListener(OnDialogButtonClickListener listener) {
            this.buttonClickListner = listener;
        }
    }

    //实现回调功能
    public interface OnDialogButtonClickListener {
        public void okButtonClick();
        public void exitButtonClick();

    }
}