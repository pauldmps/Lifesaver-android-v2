package com.paulshantanu.lifesaver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.paulshantanu.lifesaver.util.APIResponseObject;
import com.paulshantanu.lifesaver.util.ConnectionUtil;
import com.paulshantanu.lifesaver.util.PermissionUtil;

import java.net.HttpURLConnection;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<APIResponseObject>{

    public static final String TAG = "RegisterActivity";

    private GoogleApiClient apiClient;
    private PermissionUtil permissionUtil;

    private static Location lastLocation;

    private static String name;
    private static String email;
    private static String password;
    private static String confirmPassword;

    private static Double latitude;
    private static Double longitude;

    public static final int PERMISSION_ACCESS_LOCATION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        permissionUtil = new PermissionUtil(this);

        if(apiClient == null) {
            apiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Button btnRegister = (Button) findViewById(R.id.btn_register_confirm);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name  = ((EditText) findViewById(R.id.register_name)).getText().toString();
                email = ((EditText) findViewById(R.id.register_email)).getText().toString();
                password = ((EditText) findViewById(R.id.register_password)).getText().toString();
                confirmPassword = ((EditText) findViewById(R.id.register_confirm_password)).getText().toString();

                if (!permissionUtil.
                        checkIfPermissionGranted(Manifest
                                .permission.ACCESS_FINE_LOCATION)) {
                                    permissionUtil.askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, 0,
                                            "This app requires your location");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        apiClient.disconnect();
        super.onStop();
    }

    @Override
    @SuppressWarnings({"ResourceType"})
    public void onConnected(@Nullable Bundle bundle){
        try {
            lastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(apiClient);

            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
        }
        catch (SecurityException ex){
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //No implementation required
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //No implementation required
    }

    @Override
    @SuppressWarnings({"ResourceType"})
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if(requestCode == PERMISSION_ACCESS_LOCATION
                    && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        apiClient.connect();

                getSupportLoaderManager()
                        .initLoader(0,null,RegisterActivity.this)
                        .forceLoad();
            }
    }

    @Override
    public Loader<APIResponseObject> onCreateLoader(int id, Bundle args) {
        return new RegisterLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<APIResponseObject> loader,
                               APIResponseObject data) {
        if(data.getResponseCode() == HttpURLConnection.HTTP_OK){
            Intent mainActivityIntent = new Intent(this,MainActivity.class);
            startActivity(mainActivityIntent);
        }

    }

    @Override
    public void onLoaderReset(Loader<APIResponseObject> loader) {
        //Implementation not required
    }


    public static class RegisterLoader extends AsyncTaskLoader<APIResponseObject> {

        public String url = "https://lifesaver-api.herokuapp.com/register";

        public RegisterLoader(Context context) {
            super(context);
        }

        @Override
        public APIResponseObject loadInBackground() {
            HashMap<String,String> payload = new HashMap<>();
            payload.put("name",name);
            payload.put("email",email);
            payload.put("password",password);
            payload.put("bloodGroup","A+"); //TODO remove hardcoded bloodgroup

            if(lastLocation != null) {
                payload.put("latitude", latitude.toString());
                payload.put("longitude", longitude.toString());
            }

            return new ConnectionUtil(url, ConnectionUtil.METHOD_POST, payload)
                    .getAPIResponse();
        }
    }
}
