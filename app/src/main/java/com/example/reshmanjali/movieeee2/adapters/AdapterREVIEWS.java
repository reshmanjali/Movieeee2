package com.example.reshmanjali.movieeee2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.reshmanjali.movieeee2.R;
import com.example.reshmanjali.movieeee2.model.ReviewPOJO;

import java.util.ArrayList;


public class AdapterREVIEWS extends RecyclerView.Adapter<AdapterREVIEWS.ViewHolderMine> {

    Context c;
    ArrayList<ReviewPOJO> reviewResultsList = new ArrayList<>();

    public AdapterREVIEWS(Context c, ArrayList<ReviewPOJO> reviewResultsList) {
        super();
        this.c = c;
        this.reviewResultsList = reviewResultsList;
    }

    @Override
    public ViewHolderMine onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderMine(LayoutInflater.from(c).inflate(R.layout.each_review, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolderMine holder, int position) {
        holder.author_tv.setText(reviewResultsList.get(position).getAuthor());
        holder.comment_tv.setText(reviewResultsList.get(position).getComment());
        holder.urlLink_tv.setText(reviewResultsList.get(position).getLink());

    }

    @Override
    public int getItemCount() {
        return reviewResultsList.size();
    }

    public class ViewHolderMine extends RecyclerView.ViewHolder {
        TextView author_tv, comment_tv, urlLink_tv;

        public ViewHolderMine(View itemView) {
            super(itemView);
            author_tv = itemView.findViewById(R.id.id_author_tv_each_review);
            comment_tv = itemView.findViewById(R.id.id_comment_tv_each_review);
            urlLink_tv = itemView.findViewById(R.id.id_review_url_ech_reviewe);
        }
    }
}
