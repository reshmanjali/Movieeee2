package com.example.reshmanjali.movieeee2;

import android.net.Uri;
import android.util.Log;

import com.example.reshmanjali.movieeee2.model.MyPOJO;

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


public class Network {
    static String myapikey = BuildConfig.MY_API_KEY;

    public Network() {
    }

    public static URL buildingUrl(String withSortInfo) {

        Uri builtUri = Uri.parse(withSortInfo).buildUpon().appendQueryParameter("api_key", myapikey).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static ArrayList<MyPOJO> gettingResponse(URL url) throws IOException, MalformedURLException {
        ArrayList<MyPOJO> resultsList = new ArrayList<>();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sbuilderlines = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null)
            sbuilderlines.append(line).append('\n');
        in.close();
        if (sbuilderlines != null) {
            try {
                JSONObject jsonObject = new JSONObject(sbuilderlines.toString());
                JSONArray results = jsonObject.optJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject res = results.optJSONObject(i);
                    MyPOJO myPOJOObj = new MyPOJO();
                    myPOJOObj.setId(res.optString("id"));
                    myPOJOObj.setVote_count(res.optString("vote_count"));
                    myPOJOObj.setVideo(res.optBoolean("video"));
                    myPOJOObj.setVote_average(res.optDouble("vote_average"));
                    myPOJOObj.setTitle(res.optString("title"));
                    myPOJOObj.setPopularity(res.optDouble("popularity"));
                    myPOJOObj.setPoster_path(res.optString("poster_path"));
                    myPOJOObj.setOriginal_languauge(res.optString("original_language"));
                    myPOJOObj.setOriginal_title(res.optString("original_title"));
                    myPOJOObj.setAdult(res.optBoolean("adult"));
                    myPOJOObj.setBackdrop_path(res.optString("backdrop_path"));
                    myPOJOObj.setOverview(res.optString("overview"));
                    myPOJOObj.setRelease_date(res.optString("release_date"));
                    resultsList.add(myPOJOObj);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return resultsList;
    }
}