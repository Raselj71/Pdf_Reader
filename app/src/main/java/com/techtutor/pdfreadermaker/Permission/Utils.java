package com.techtutor.pdfreadermaker.Permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.core.content.ContextCompat;

public class Utils {

    public static boolean isPermissonGranted(Context context){
                 if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
                     return Environment.isExternalStorageManager();
                 }else{
                     int readExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
                     return readExternalStorage== PackageManager.PERMISSION_GRANTED;
                 }

    }
}
