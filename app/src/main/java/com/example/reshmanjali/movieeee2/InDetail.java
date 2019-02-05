package com.example.reshmanjali.movieeee2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.reshmanjali.movieeee2.adapters.AdapterREVIEWS;
import com.example.reshmanjali.movieeee2.adapters.AdapterVID;
import com.example.reshmanjali.movieeee2.model.MyPOJO;
import com.example.reshmanjali.movieeee2.model.ReviewPOJO;
import com.example.reshmanjali.movieeee2.model.VideoPOJO;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void> {

    public ArrayList<VideoPOJO> vidPathsList = new ArrayList<VideoPOJO>();
    public ArrayList<ReviewPOJO> reviewResultsList = new ArrayList<ReviewPOJO>();
    @BindView(R.id.recc_view_trailors)
    RecyclerView rcv_for_trailers;
    @BindView(R.id.id_rec_view_reviews)
    RecyclerView rec_for_reviews;
    @BindView(R.id.id_poster_in_mdetail)
    ImageView poster_img_v;
    @BindView(R.id.id_backdrop_in_mdetail)
    ImageView backdrop_img_v;
    @BindView(R.id.id_rating_tv_in_mdetail)
    TextView ratings_tv;
    @BindView(R.id.id_date_tv_in_mdetail)
    TextView release_date_tv;
    @BindView(R.id.id_overview_tv_in_detail)
    TextView overview_tv;
    @BindView(R.id.id_title_tv_mdetail)
    TextView title_tv;
    @BindView(R.id.id_fav_btn_in_mdetail)
    Button favourite_btn;
    int video_trailer_id = 35;
    int review_id = 40;
    String myIdString = "";
    private static final String MOVIE_API_KEY=BuildConfig.MY_API_KEY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_detail);
        ButterKnife.bind(this);
        final MyPOJO p = (MyPOJO) getIntent().getSerializableExtra("MyclassObject");
        myIdString = p.getId();
        if (ElementPresence(p.getId())) favourite_btn.setText(R.string.remove_frm_fav);
        else favourite_btn.setText(R.string.add_as_fav);
        title_tv.setText(p.getOriginal_title());
        ratings_tv.setText(Double.toString(p.getVote_average()));
        release_date_tv.setText(p.getRelease_date());
        overview_tv.setText(p.getOverview());
        Picasso.with(this).load("https://image.tmdb.org/t/p/w300" + "" + p.getPoster_path()).placeholder(R.mipmap.inplaceholder).into(poster_img_v);
        Picasso.with(this).load("https://image.tmdb.org/t/p/w500" + "" + p.getBackdrop_path()).placeholder(R.mipmap.inplaceholder).into(backdrop_img_v);
        getSupportLoaderManager().restartLoader(video_trailer_id, null, this);
        getSupportLoaderManager().restartLoader(review_id, null, this);
        favourite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ElementPresence(p.getId())) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(FavContract.MOVIE_ID_COLUMN, p.getId());
                    contentValues.put(FavContract.MOVIE_TITLE_COLUMN, p.getTitle());
                    contentValues.put(FavContract.RELEASE_DATE_COLUMN, p.getRelease_date());
                    contentValues.put(FavContract.POSTER_PATH_COLUMN, p.getPoster_path());
                    contentValues.put(FavContract.ORIGINAL_TITLE_COLUMN, p.getOriginal_title());
                    contentValues.put(FavContract.BACKDROP_PATH_COLUMN, p.getBackdrop_path());
                    contentValues.put(FavContract.PLOT_SYNOPSIS, p.getOverview());
                    contentValues.put(FavContract.RATING, p.getVote_average());
                    getContentResolver().insert(FavContract.CONTENT_URI, contentValues);
                    Toast.makeText(InDetail.this, "Added to favourites", Toast.LENGTH_SHORT).show();
                    favourite_btn.setText(R.string.remove_frm_fav);
                } else {
                    Uri uri = Uri.parse(FavContract.CONTENT_URI + "/*");
                    getContentResolver().delete(uri, FavContract.MOVIE_ID_COLUMN + " = '" + p.getId() + "'", null);
                    Toast.makeText(InDetail.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
                    favourite_btn.setText(R.string.add_as_fav);
                }
            }}
        );
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Boolean ElementPresence(String id) {
        Cursor c = getContentResolver().query(Uri.parse(FavContract.CONTENT_URI + "/*"), null, id, null, null);
        if (c.getCount() > 0)
            return true;
        else return false;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        if (id == video_trailer_id)
            return new AsyncTaskLoader<Void>(this) {
                @Override
                public Void loadInBackground() {
                    loadBackgroundForVideos();
                    return null;
                }

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }
            };
        else if (id == review_id) {
            return new AsyncTaskLoader<Void>(this) {
                @Override
                public Void loadInBackground() {
                    loadBackgroundForReviews();
                    return null;
                }

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }
            };
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        if (loader.getId() == video_trailer_id) {
            if (vidPathsList.isEmpty())
                findViewById(R.id.id_trailors_no_data_inDetail).setVisibility(View.VISIBLE);
            else {
                rcv_for_trailers.setLayoutManager(new LinearLayoutManager(InDetail.this));
                AdapterVID adap = new AdapterVID(InDetail.this, vidPathsList);
                rcv_for_trailers.setAdapter(adap);
                adap.notifyDataSetChanged();
            }
        } else if (loader.getId() == review_id) {
            if (reviewResultsList.isEmpty())
                findViewById(R.id.id_reviews_no_data_inDetail).setVisibility(View.VISIBLE);
            else {
                rec_for_reviews.setLayoutManager(new LinearLayoutManager(InDetail.this));
                AdapterREVIEWS adapRev = new AdapterREVIEWS(InDetail.this, reviewResultsList);
                rec_for_reviews.setAdapter(adapRev);
                adapRev.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {
    }

    void loadBackgroundForReviews() {
        StringBuilder rev_sb = new StringBuilder();
        Uri review_uri = Uri.parse(getString(R.string.movie_url)).buildUpon().appendPath(myIdString)
                .appendPath("reviews")
                .appendQueryParameter("api_key", MOVIE_API_KEY).build();
        URL rev_url = null;
        try {
            rev_url = new URL(review_uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection connect = (HttpURLConnection) rev_url.openConnection();
            connect.setRequestMethod("GET");
            InputStream ipstream = new BufferedInputStream(connect.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(ipstream));
            String oneLine = "";
            while ((oneLine = reader.readLine()) != null)
                rev_sb.append(oneLine).append("\n");
            ipstream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rev_sb != null) {
            try {
                JSONObject jo_rev = new JSONObject(rev_sb.toString());
                JSONArray rev_results = jo_rev.optJSONArray("results");
                for (int i = 0; i < rev_results.length(); i++) {
                    JSONObject rev_results_each = rev_results.optJSONObject(i);
                    ReviewPOJO reviewPOJOobj = new ReviewPOJO();
                    reviewPOJOobj.setAuthor(rev_results_each.optString("author"));
                    reviewPOJOobj.setComment(rev_results_each.optString("content"));
                    reviewPOJOobj.setLink(rev_results_each.optString("url"));
                    reviewResultsList.add(reviewPOJOobj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void loadBackgroundForVideos() {
        StringBuilder sb = new StringBuilder();
        Uri uri = Uri.parse(getString(R.string.movie_url)).buildUpon().appendPath(myIdString)
                .appendPath("videos")
                .appendQueryParameter("api_key", MOVIE_API_KEY).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String singleLine = "";
            while ((singleLine = reader.readLine()) != null)
                sb.append(singleLine).append("\n");
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sb != null) {
            try {
                JSONObject jo = new JSONObject(sb.toString());
                JSONArray vidResults = jo.optJSONArray("results");
                for (int i = 0; i < vidResults.length(); i++) {
                    JSONObject vidResOb = vidResults.optJSONObject(i);
                    VideoPOJO videoPOJO = new VideoPOJO();
                    videoPOJO.setKey(vidResOb.optString("key"));
                    videoPOJO.setType(vidResOb.optString("type"));
                    videoPOJO.setName(vidResOb.optString("name"));
                    vidPathsList.add(videoPOJO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}