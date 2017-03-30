package com.paulshantanu.lifesaver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.paulshantanu.lifesaver.util.APIResponseObject;
import com.paulshantanu.lifesaver.util.ConnectionUtil;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shantanu Paul on 3/10/2017.
 */

public class LoginActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<APIResponseObject>{
    private static final String TAG = "LoginActivity";

    private static String email;
    private static String password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etEmail = (EditText) findViewById(R.id.et_email);
        final EditText etPassword = (EditText) findViewById(R.id.et_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Inside onClick");
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                getSupportLoaderManager()
                        .restartLoader(1,null,LoginActivity.this).forceLoad();
            }
        });
    }

    @Override
    public Loader<APIResponseObject> onCreateLoader(int id, Bundle args) {
        Log.d(TAG,"Inside onCreateLoader");
        return new LoginLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<APIResponseObject> loader, APIResponseObject data) {
        Log.d(TAG,String.valueOf(data.getResponseCode()));
        Log.d(TAG,data.getResponse());
        if(data.getResponseCode() == HttpURLConnection.HTTP_OK){
            Intent mainActivityIntent = new Intent(this,MainActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    @Override
    public void onLoaderReset(Loader<APIResponseObject> loader) {
        //Implementation not required
    }

    public static class LoginLoader extends AsyncTaskLoader<APIResponseObject>{

        private String url = "https://lifesaver-api.herokuapp.com/login";

        LoginLoader(Context context) {
            super(context);
            Log.d(TAG,"Inside loader constructor");
        }

        @Override
        public APIResponseObject loadInBackground() {
            Log.d(TAG,"Inside loadInBackground");

            Map<String,String> payload = new HashMap<>();
            payload.put("email",email);
            payload.put("password",password);

            return new ConnectionUtil(url,ConnectionUtil.METHOD_POST,payload)
                    .getAPIResponse();
        }
    }
}
