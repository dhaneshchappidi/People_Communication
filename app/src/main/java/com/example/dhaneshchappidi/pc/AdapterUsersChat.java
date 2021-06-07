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

public class AdapterUsersChat extends RecyclerView.Adapter<AdapterUsersChat.MyHolder> {
    Context context;
    List<Modelchat> modelchats;

    public AdapterUsersChat(Context context, List<Modelchat> modelchats) {
        this.context = context;
        this.modelchats = modelchats;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_chat,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String UserImage=modelchats.get(i).getImage();
        final String UserName=modelchats.get(i).getName();
        final String IdNo=modelchats.get(i).getIdno();
        final String Uid=modelchats.get(i).getUid();
        String type=modelchats.get(i).getType();

        myHolder.mnametv.setText(UserName);
        myHolder.midnotv.setText(IdNo);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Chat_page.class);
                intent.putExtra("Hisuid",Uid);
                intent.putExtra("uname",UserName);
                intent.putExtra("Idno",IdNo);
                intent.putExtra("image2",UserImage);
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
        return modelchats.size();
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
