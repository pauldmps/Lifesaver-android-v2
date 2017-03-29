package com.paulshantanu.lifesaver.util;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paulshantanu.lifesaver.R;
import com.paulshantanu.lifesaver.database.DBConstants;

import static com.paulshantanu.lifesaver.R.id.tv_address;
import static com.paulshantanu.lifesaver.R.id.tv_bloodgroup;
import static com.paulshantanu.lifesaver.R.id.tv_distance;
import static com.paulshantanu.lifesaver.R.id.tv_name;

/**
 * Created by Shantanu Paul on 3/26/2017.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder>{

    private Cursor userCursor;

    public UserListAdapter(Cursor userCursor) {
        this.userCursor = userCursor;
    }

    private static final String TAG = "UserListAdapter";

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvEmail;
        public TextView tvBloodGroup;
        public TextView tvDistance;

        UserViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(tv_name);
            tvEmail = (TextView) itemView.findViewById(tv_address);
            tvBloodGroup = (TextView) itemView.findViewById(tv_bloodgroup);
            tvDistance = (TextView) itemView.findViewById(tv_distance);
        }
    }

    public Cursor swapCursor(Cursor cursor){
        if(userCursor == cursor){
            return null;
        }

        Cursor oldCursor = userCursor;
        this.userCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent,false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        userCursor.moveToPosition(position);

        holder.tvName.setText(userCursor.getString(userCursor.getColumnIndex(DBConstants.COLUMN_NAME)));
        holder.tvEmail.setText(userCursor.getString(userCursor.getColumnIndex(DBConstants.COLUMN_EMAIL)));
        holder.tvDistance.setText(userCursor.getString(userCursor.getColumnIndex(DBConstants.COLUMN_LOCATION_LATITUDE)));
        holder.tvBloodGroup.setText(userCursor.getString(userCursor.getColumnIndex(DBConstants.COLUMN_BLOODGROUP)));
    }

    @Override
    public int getItemCount() {
        int d = (userCursor == null) ? 0 : userCursor.getCount();
        Log.d(TAG, String.valueOf(d));
        return d;

    }
}
