package com.paulshantanu.lifesaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.paulshantanu.lifesaver.services.UserService;
import com.paulshantanu.lifesaver.util.UserListAdapter;
import com.paulshantanu.lifesaver.util.UserProvider;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private UserListAdapter userListAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getSupportLoaderManager().restartLoader(0,null,MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(UserService.BROADCAST_ACTION));

        if(checkConnectivity()){
            Intent updateDataIntent = new Intent(this, UserService.class);
            startService(updateDataIntent);
        }

        RecyclerView mainRecyclerView = (RecyclerView) findViewById(R.id.rv_main);
        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userListAdapter = new UserListAdapter(null);
        mainRecyclerView.setAdapter(userListAdapter);

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(mBroadcastReceiver);
    }

    public boolean checkConnectivity(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, UserProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        userListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        userListAdapter.swapCursor(null);
    }
}
