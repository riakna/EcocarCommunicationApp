package br.com.unicamp.ecocar.ecocarcommapp.Util;

/**
 * Created by Nagaishi on 17/03/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.unicamp.ecocar.ecocarcommapp.ChatMessagesActivity;
import br.com.unicamp.ecocar.ecocarcommapp.Models.User;
import br.com.unicamp.ecocar.ecocarcommapp.R;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private List<User> mUsersList;
    private Context mContext;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView personNameTxtV;
        public ImageView personImageImgV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            personNameTxtV =  v.findViewById(R.id.userName);
            personImageImgV =  v.findViewById(R.id.userImage);




        }
    }

    public void add(int position, User person) {
        mUsersList.add(position, person);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mUsersList.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public UsersAdapter(List<User> myDataset, Context context) {
        mUsersList = myDataset;
        mContext = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.user_single_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final User user = mUsersList.get(position);
        holder.personNameTxtV.setText(user.getDisplayName());

        try {
            Picasso.with(mContext).load(user.getImage()).placeholder(R.mipmap.ic_launcher).into(holder.personImageImgV);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send this user id to chat messages activity
                goToUpdateActivity(user.getUserId());
            }
        });


    }

    private void goToUpdateActivity(String personId){
        Intent goToUpdate = new Intent(mContext, ChatMessagesActivity.class);
        goToUpdate.putExtra("USER_ID", personId);
        mContext.startActivity(goToUpdate);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mUsersList.size();
    }



}