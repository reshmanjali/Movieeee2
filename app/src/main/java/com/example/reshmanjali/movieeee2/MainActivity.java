package com.example.reshmanjali.movieeee2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.reshmanjali.movieeee2.adapters.MyOwnAdapter;
import com.example.reshmanjali.movieeee2.model.MyPOJO;
import java.io.IOException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MyPOJO>> {
    private static final int popular_id = 25;
    private static final int top_rated_id = 30;
    final static String key = "key";
    static String val = "popular";
    @BindView(R.id.id_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.id_prog_bar)
    ProgressBar progressBar_in_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (isNetworkAvailable()) {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(key)) {
                    String value = savedInstanceState.getString(key);
                    checkItem(value);
                } else checkItem(val);
            } else checkItem(val);
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(key, val);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void checkItem(String value) {
        if (value.equals("popular")) {
            Bundle bundle = new Bundle();
            val = "popular";
            bundle.putString(key, val);
            LoaderManager loaderManager = getLoaderManager();
            Loader<ArrayList<MyPOJO>> loader = loaderManager.getLoader(popular_id);
            if (loader == null)
                loaderManager.initLoader(popular_id, bundle, this);
            else
                loaderManager.restartLoader(popular_id, bundle, this);
        } else if (value.equals("top_rated")) {
            Bundle bundleTR = new Bundle();
            val = "top_rated";
            bundleTR.putString(key, val);
            LoaderManager loaderManager = getLoaderManager();
            Loader<ArrayList<MyPOJO>> loader = loaderManager.getLoader(top_rated_id);
            if (loader == null) loaderManager.initLoader(top_rated_id, bundleTR, this);
            else loaderManager.restartLoader(top_rated_id, bundleTR, this);
        } else if (value.equals("favourite")) {
            progressBar_in_main.setVisibility(View.INVISIBLE);
            val = "favourite";
            ArrayList<MyPOJO> favPosterPathsWithData = new ArrayList<>();
            Cursor cursor = getContentResolver().query(FavContract.CONTENT_URI, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    MyPOJO temp = new MyPOJO();
                    temp.setId(cursor.getString(1));
                    temp.setTitle(cursor.getString(2));
                    temp.setRelease_date(cursor.getString(3));
                    temp.setPoster_path(cursor.getString(4));
                    temp.setOriginal_title(cursor.getString(5));
                    temp.setBackdrop_path(cursor.getString(6));
                    temp.setOverview(cursor.getString(7));
                    temp.setVote_average(cursor.getDouble(8));
                    favPosterPathsWithData.add(temp);
                } while (cursor.moveToNext());
            }
            cursor.close();
            if (favPosterPathsWithData.isEmpty())
                Toast.makeText(this, "Your Favourites list is  Empty", Toast.LENGTH_LONG).show();
            else {
                recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                MyOwnAdapter adapterFav = new MyOwnAdapter(MainActivity.this, favPosterPathsWithData);
                recyclerView.setAdapter(adapterFav);
            }
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager cm
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = cm.getActiveNetworkInfo();
        Boolean b = (activeInfo != null && activeInfo.isConnected());
        if ((!b) && val != "favourite") {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.oops);
            alertDialog.setMessage(R.string.offline);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
        return b;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_for_sort, menu);
        return true;
    }

    public void clickedPopular(MenuItem item) {
        Toast.makeText(MainActivity.this, "Popular movies", Toast.LENGTH_SHORT).show();
        val = "popular";
        if (isNetworkAvailable())
            checkItem(val);
    }

    public void clickedHighestRated(MenuItem item) {
        Toast.makeText(MainActivity.this, "Top-rated movies", Toast.LENGTH_SHORT).show();
        val = "top_rated";
        if (isNetworkAvailable())
            checkItem(val);
    }

    public void clickedFavouritesInMenu(MenuItem item) {
        val = "favourite";
        checkItem(val);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<ArrayList<MyPOJO>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<MyPOJO>>(this) {
            ArrayList<MyPOJO> arrayList;

            @Override
            protected void onStartLoading() {
                if (args == null) return;
                if (arrayList == null) forceLoad();
                else deliverResult(arrayList);
                progressBar_in_main.setVisibility(View.VISIBLE);
            }

            @Override
            public ArrayList<MyPOJO> loadInBackground() {
                String value = args.getString(key);
                String myUrlString = getString(R.string.movie_url) + value + "?";
                try {
                    return Network.gettingResponse(Network.buildingUrl(myUrlString));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(ArrayList<MyPOJO> data) {
                arrayList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MyPOJO>> loader, ArrayList<MyPOJO> data) {
        progressBar_in_main.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        MyOwnAdapter adapter = new MyOwnAdapter(MainActivity.this, data);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MyPOJO>> loader) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (val == "favourite")
            checkItem(val);
     }

}