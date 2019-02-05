package com.example.reshmanjali.movieeee2.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reshmanjali.movieeee2.R;
import com.example.reshmanjali.movieeee2.model.VideoPOJO;

import java.util.ArrayList;


public class AdapterVID extends RecyclerView.Adapter<AdapterVID.MyHolder> {
    ArrayList<VideoPOJO> vidList;
    Context context;

    public AdapterVID(Context context, ArrayList<VideoPOJO> vidList) {
        super();
        this.context = context;
        this.vidList = vidList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.each_trailer, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        String textForTrailer = vidList.get(position).getName() + " ( " + vidList.get(position).getType() + " )";
        holder.vidPath_tv.setText(textForTrailer);
    }

    @Override
    public int getItemCount() {
        return vidList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vidPath_tv;

        public MyHolder(View itemView) {
            super(itemView);
            vidPath_tv = itemView.findViewById(R.id.id_play_vid_each_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "" + vidList.get(getAdapterPosition()).getKey())));
            } catch (ActivityNotFoundException anf) {
                v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + "" + vidList.get(getAdapterPosition()).getKey())));
            }
        }
    }
}
