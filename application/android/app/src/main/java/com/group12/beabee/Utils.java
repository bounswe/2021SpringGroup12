package com.group12.beabee;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.group12.beabee.views.dialogs.LoadingDialog;

public  class Utils {

    private static LoadingDialog dialog;
    private static boolean isLoadingShowed = false;
    private static int loadingStack = 0;

    public static void ShowErrorToast(Context context, String errorMsg){
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }

    public static void showLoading(FragmentManager fragmentManager){
        if (dialog==null) {
            dialog = new LoadingDialog();
        }

        if (isLoadingShowed) {
            loadingStack++;
            return;
        }

        dialog.show(fragmentManager, "loading");
        isLoadingShowed = true;
    }

    public static void dismissLoading(){
        if (!isLoadingShowed) {
            return;
        }

        if (loadingStack>0){
            loadingStack--;
            return;
        }

        dialog.dismiss();
    }


    public static void OpenRateDialog(Context context, OnRateSelectedListener onRateSelectedListener)
    {
        final Dialog d = new Dialog(context);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog_rate);
        Button b1 = (Button) d.findViewById(R.id.btn_dismiss);
        Button b2 = (Button) d.findViewById(R.id.btn_approve);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberpicker);
        np.setMaxValue(5);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                onRateSelectedListener.OnRateSelected(np.getValue());
                d.dismiss();
            }
        });
        d.show();
    }
}
