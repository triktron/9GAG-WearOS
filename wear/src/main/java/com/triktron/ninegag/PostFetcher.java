package com.triktron.ninegag;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.triktron.ninegag.post.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostFetcher {
    private static final String JSON_BASE_URL = "https://9gag.com/v1/group-posts/group/default/type/";


    String Section = "hot";
    int PostsPerPage = 10;

    List<Post> Posts;
    RequestQueue RequestQueue;

    boolean IsLoading = false;


    private ProgressBar loadBar;

    public PostFetcher(Context context, ProgressBar _loadBar) {
        Posts = new ArrayList<>();
        RequestQueue = Volley.newRequestQueue(context);
        loadBar = _loadBar;
    }

    public void LoadNext(Runnable cb) {
        if (IsLoading) return;
        IsLoading = true;
        loadBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GetNextJsonUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            JSONArray tutorialsArray = obj.getJSONObject("data").getJSONArray("posts");

                            for (int i = 0; i < tutorialsArray.length(); i++) {
                                JSONObject tutorialsObject = tutorialsArray.getJSONObject(i);

                                Post post = Post.FromJSON(tutorialsObject);

                                Log.d("PostFetcher", "Adding post (" + post.Type.toString() + "): " + post.Title);

                                Posts.add(post);
                            }

                            IsLoading = false;
                            loadBar.setVisibility(View.GONE);
                            cb.run();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        //displaying the error in toast if occur
                        Log.e("PostFetcher", e.getMessage());
                    }
                });

        RequestQueue.add(stringRequest);
    }

    String GetNextJsonUrl() {
        android.net.Uri old = android.net.Uri.parse(JSON_BASE_URL + Section);
        Uri.Builder url = old.buildUpon();

        if (PostsPerPage > 0) url.appendQueryParameter("c", Integer.toString(PostsPerPage));

        if (Posts.size() != 0) {
            String lastPosts = Posts.stream().skip(Posts.size()-3).map(p -> p.ID).collect(Collectors.joining(","));
            url.appendQueryParameter("after", lastPosts);
        }

        return url.toString();
    }
}
