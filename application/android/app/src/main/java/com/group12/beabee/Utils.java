package com.group12.beabee;

import android.content.Context;
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
}
