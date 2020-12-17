package com.example.instaclone.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.instaclone.MainActivity;
import com.example.instaclone.R;
import com.example.instaclone.models.User;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    public List<User> userList;

    public SearchAdapter(List<User> userList)
    {
        this.userList=userList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView name,username;
        public CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.singleitemfullname);
            username=itemView.findViewById(R.id.singleitemusername);
            circleImageView=itemView.findViewById(R.id.singleitemimageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            User userGlob=userList.get(getAdapterPosition());
            MainActivity.mainActivity.loadProfileFragment(userGlob.getUsername());

        }
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.singlesearchitem,viewGroup,false);



        /*
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder myViewHolder, int i) {
        com.example.instaclone.models.User user=userList.get(i);
        myViewHolder.username.setText(user.getUsername());
        myViewHolder.name.setText(user.getName());
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.usericon);
        Glide.with(MainActivity.mainContext).load(user.getPhotourl()).apply(options).into(myViewHolder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
