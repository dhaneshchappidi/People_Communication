package com.example.dhaneshchappidi.pc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder> {
    Context context;
    List<ModelUser> userList;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_users,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String UserImage=userList.get(i).getImage();
        final String UserName=userList.get(i).getName();
        String IdNo=userList.get(i).getIdno();
        final String Uid=userList.get(i).getUid();
        String type=userList.get(i).getType();

        myHolder.mnametv.setText(UserName);
        myHolder.midnotv.setText(IdNo);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Full_profile.class);
                intent.putExtra("uid",Uid);
                intent.putExtra("uname",UserName);
                context.startActivity(intent);
            }
        });

        try {
            Picasso.with(myHolder.mavatartv.getContext())
                    .load(UserImage)
                    .placeholder(R.drawable.pro_name)
                    .error(R.drawable.pro_name)
                    .into(myHolder.mavatartv);

        }
        catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView mavatartv;
        TextView mnametv,midnotv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mavatartv=itemView.findViewById(R.id.avatartv);
            mnametv=itemView.findViewById(R.id.nametv);
            midnotv=itemView.findViewById(R.id.idnotv);
        }
    }
}
