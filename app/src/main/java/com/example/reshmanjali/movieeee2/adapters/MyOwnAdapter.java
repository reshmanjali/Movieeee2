package com.example.reshmanjali.movieeee2.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.reshmanjali.movieeee2.InDetail;
import com.example.reshmanjali.movieeee2.MainActivity;
import com.example.reshmanjali.movieeee2.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import com.example.reshmanjali.movieeee2.model.MyPOJO;


public class MyOwnAdapter extends RecyclerView.Adapter<MyOwnAdapter.MyViewHOlder>  {
    MainActivity context;
    ArrayList<MyPOJO> resultsArraylist=new ArrayList<MyPOJO>();
    private int position;
    public MyOwnAdapter(MainActivity mainActivity, ArrayList<MyPOJO> resultsArray)
    {
        context=mainActivity;
        resultsArraylist=resultsArray;
    }
    @Override
    public MyViewHOlder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHOlder(LayoutInflater.from(context).inflate(R.layout.activity_with_image,parent,false));
    }
    @Override
    public void onBindViewHolder(MyViewHOlder holder, int position) {
        Picasso.with(context)
                .load(context.getString(R.string.image_url)+""+resultsArraylist.get(position).getPoster_path())
                .placeholder(R.mipmap.inplaceholder)
                .into(holder.movieimg);
        this.position=position;
    }
    @Override
    public int getItemCount() {
        return resultsArraylist.size();
    }

    public class MyViewHOlder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView movieimg;
        public MyViewHOlder(View itemView) {
            super(itemView);
            movieimg=itemView.findViewById(R.id.id_imgview_in_withimage);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int indx=getAdapterPosition();
            Toast.makeText(context,resultsArraylist.get(indx).getTitle(),Toast.LENGTH_SHORT).show();
            Intent i=new Intent(context,InDetail.class);
            i.putExtra("MyclassObject",resultsArraylist.get(indx));
            v.getContext().startActivity(i);
        }
    }
    public int getPosition(){
        return position;
    }
}