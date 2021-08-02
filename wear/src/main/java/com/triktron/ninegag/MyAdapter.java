package com.triktron.ninegag;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.triktron.ninegag.post.Post;
import com.triktron.ninegag.post.PostTypes;

import java.util.List;


public class MyAdapter extends ArrayAdapter<Post> {
    private List<Post> posts;
    private Bitmap bitmap;
    private Context mCtx;
    public MyAdapter(List<Post> posts, Context mCtx) {
        super(mCtx, R.layout.list_item, posts);
        this.posts = posts;
        this.mCtx = mCtx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        convertView = inflater.inflate(R.layout.list_item, null, true);
        holder = new ViewHolder();
        //getting text views
        holder.textViewName = convertView.findViewById(R.id.title);
        //holder.imageView = convertView.findViewById(R.id.imageView);

        convertView.setTag(holder);
        //Getting the tutorial for the specified position
        Post post = posts.get(position);

        holder.textViewName.setText(post.Title);

        if (post.Type == PostTypes.Image) {
            if (holder.imageView != null) {
                /*-------------fatching image------------*/;
                new ImageDownloaderTask(holder.imageView).execute(post.MediaUrl);
            }
            holder.imageView.setImageBitmap(bitmap);
        }

        return convertView;
    }
    static class ViewHolder {
        TextView textViewName;
        ImageView imageView;
    }
}