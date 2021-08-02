package com.triktron.ninegag;

import android.app.Activity;
import android.os.Bundle;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.triktron.ninegag.databinding.ActivityMainBinding;
import com.triktron.ninegag.post.Post;

public class MainActivity extends Activity {

    WearableRecyclerView  listView;
    ProgressBar loadBar;

    PostFetcher Fetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (WearableRecyclerView) findViewById(R.id.recycler_launcher_view);
        loadBar = (ProgressBar) findViewById(R.id.progressBar);

        PostAdaptor adapter = new PostAdaptor();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);

        Fetcher = new PostFetcher(this, loadBar);
        Fetcher.LoadNext(() -> {
            adapter.SetData(Fetcher.Posts);
        });

        listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    Fetcher.LoadNext(() -> {
                        adapter.SetData(Fetcher.Posts);
                    });
                }
            }
        });
    }
}