package com.example.moon.firebasetest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private ArrayList<ObjectForMessage> mArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, message;
        public ImageView iv_user;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.username);
            message = (TextView) view.findViewById(R.id.text);
            iv_user = (ImageView) view.findViewById(R.id.imageView2);
        }
    }


    public MessageAdapter(ArrayList<ObjectForMessage> mArrayList) {
        this.mArrayList = mArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customadapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ObjectForMessage objectForMessage = mArrayList.get(position);
        holder.name.setText(objectForMessage.getUserName());
        holder.message.setText(objectForMessage.getMessage());
        Picasso.get().load(objectForMessage.getUrl()).into(holder.iv_user);
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }
}
