package com.paulshantanu.lifesaver.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Shantanu Paul on 4/1/2017.
 */

public class PermissionUtil {
    private Context context;

    public PermissionUtil(Context context){
        this.context = context;
    }


    public boolean checkIfPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void askForPermission(final String permission, final int returnCode, String message){

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{permission},returnCode);
                        }
                    });

            builder.create().show();
        }
        else {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{permission},returnCode);
        }
    }

}
