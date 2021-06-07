package com.example.dhaneshchappidi.pc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterNotices extends RecyclerView.Adapter<AdapterNotices.MyHolder> {
    Context context;
    List<ModelNotice> noticeList;

    public AdapterNotices(Context context, List<ModelNotice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_notice,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String ntitle=noticeList.get(i).getNtitle();
        String ndesc=noticeList.get(i).getNdesc();
        String nyear=noticeList.get(i).getNyear();
        String nbrach=noticeList.get(i).getNbranch();
        final String nid=noticeList.get(i).getNid();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(nid));
        String ntime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        myHolder.notime.setText(ntime);
        myHolder.notitle.setText(ntitle);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Full_Notice.class);
                intent.putExtra("nid",nid);
                intent.putExtra("ntitle",ntitle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        TextView notitle,notime;
        CardView cardView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            notitle=itemView.findViewById(R.id.notitle);
            notime=itemView.findViewById(R.id.no_time);

        }
    }
}
