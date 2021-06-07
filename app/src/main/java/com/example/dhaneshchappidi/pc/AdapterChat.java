package com.example.dhaneshchappidi.pc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {

    private static final int MSG_TYPE_LEFT =0;
    private static final int MSG_TYPE_RIGHT =1;
    Context context;
    List<ModelMessage> modelMessageList;
    FirebaseUser firebaseUser;

    public AdapterChat(Context context, List<ModelMessage> modelMessageList) {
        this.context = context;
        this.modelMessageList = modelMessageList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_right,viewGroup,false);
            return new MyHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_left,viewGroup,false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChat.MyHolder myHolder, int i) {
        ModelMessage modelMessage=modelMessageList.get(i);
        String message=modelMessage.getMessage();
        myHolder.textView.setText(message);

    }

    @Override
    public int getItemCount() {
        return modelMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(modelMessageList.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.messageTv);
        }
    }
}
