package com.example.dhaneshchappidi.pc;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterBranch extends RecyclerView.Adapter<AdapterBranch.MyHolder> {
    Context context;
    List<ModelBranch> branchList;

    public AdapterBranch(Context context, List<ModelBranch> branchList) {
        this.context = context;
        this.branchList = branchList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_branch,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String b_title=branchList.get(i).btitle;
        String b_desc=branchList.get(i).bdesc;
        String b_year=branchList.get(i).byear;
        final String b_brach=branchList.get(i).branch;
        final String b_id=branchList.get(i).bid;

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(b_id));
        String ntime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        myHolder.notitle.setText(b_title);
        myHolder.notime.setText(ntime);
        myHolder.noyear.setText(b_year);
        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,Full_branch.class);
                intent.putExtra("nid",b_id);
                intent.putExtra("ntitle",b_title);
                intent.putExtra("branch",b_brach);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView notitle,notime,nobranch,noyear;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            notitle=itemView.findViewById(R.id.notitle);
            notime=itemView.findViewById(R.id.no_time);
            noyear=itemView.findViewById(R.id.no_year);
        }
    }
}
