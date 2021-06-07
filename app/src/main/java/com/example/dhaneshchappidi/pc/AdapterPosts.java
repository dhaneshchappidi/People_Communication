package com.example.dhaneshchappidi.pc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder> {
    Context context;
    List<ModelPost> postList;

    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_posts,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        String uid=postList.get(i).getUid();
        String uEmail=postList.get(i).getuEmail();
        String uName=postList.get(i).getuName();
        String uDp=postList.get(i).getuDp();
        String pid=postList.get(i).getpId();
        String pTitle=postList.get(i).getpTitle();
        String pDescription=postList.get(i).getpDescr();
        String pImage=postList.get(i).getpImage();
        String pTimeStamp=postList.get(i).getpTime();
        myHolder.uNameTv.setText(uName);
        myHolder.pTitleTv.setText(pTitle);
        myHolder.pDescriptionTv.setText(pDescription);

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();
        myHolder.pTimeTv.setText(pTime);

        try {
            Picasso
                    .with(myHolder.uImageIv.getContext())
                    .load(uDp)
                    .placeholder(R.drawable.person2)
                    .fit()
                    .error(R.drawable.person2)
                    .into(myHolder.uImageIv);
        }
        catch (Exception e){

        }
        if(pImage.equals("noImage")){
            myHolder.pImageIv.setVisibility(View.GONE);
        }
        else{
            try {
                Picasso.with(myHolder.pImageIv.getContext()).load(pImage).into(myHolder.pImageIv);

            }
            catch (Exception e){

            }
        }


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView pImageIv,uImageIv;
        TextView uNameTv,pTimeTv,pTitleTv,pDescriptionTv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            pImageIv=itemView.findViewById(R.id.pImageTv);
            uImageIv=itemView.findViewById(R.id.uPictureIv);
            uNameTv=itemView.findViewById(R.id.uName);
            pTimeTv=itemView.findViewById(R.id.time);
            pTitleTv=itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv=itemView.findViewById(R.id.pDescriptionTv);
        }
    }
}
