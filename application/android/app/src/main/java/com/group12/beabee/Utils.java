package com.group12.beabee;

import android.content.Context;
import android.widget.Toast;

public  class Utils {
    public static void ShowErrorToast(Context context, String errorMsg){
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
