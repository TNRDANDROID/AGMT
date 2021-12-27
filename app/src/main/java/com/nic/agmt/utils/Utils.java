package com.nic.agmt.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nic.agmt.R;

/** Created By Dileep Kumar
 * **/
public class Utils {

    public static boolean isOnline(Context context) {
        boolean status = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = true;
            }
        } else {
            status = false;
        }

        return status;
    }

    public static void showAlert(Activity activity, String msg, String type, JsResult result){
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            ImageView close = (ImageView) dialog.findViewById(R.id.close);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button dialogButton1 = (Button) dialog.findViewById(R.id.btn_cancel);
            if(type.equals("alert")) {
                dialogButton1.setVisibility(View.GONE);
            }
            else {
                dialogButton1.setVisibility(View.VISIBLE);
            }
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result.cancel();
                    dialog.dismiss();
                }
            });
            dialogButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result.confirm();
                    dialog.dismiss();
                }
            });
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
