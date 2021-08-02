package com.triktron.ninegag.post;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    public  String ID, URL, Title, Description;
    public PostTypes Type;
    public int UpVotes, DownVotes, Creation;

    public int MediaWidth, MediaHeight;
    public String MediaUrl;

    public static Post FromJSON(JSONObject object) throws JSONException {
        Post post = new Post();

        post.ID = object.getString("id");
        post.URL = object.getString("url");
        post.Title = object.getString("title");
        post.Description = object.getString("description");

        post.UpVotes = object.getInt("upVoteCount");
        post.DownVotes = object.getInt("downVoteCount");
        post.Creation = object.getInt("creationTs");

        JSONObject media;

        switch (object.getString("type")) {
            default:
            case "Photo":
                post.Type = PostTypes.Image;
                media = object.getJSONObject("images").getJSONObject("image460");
                break;

            case  "Animated-":
                post.Type = PostTypes.Video;
                media = object.getJSONObject("images").getJSONObject("image460sv");
                break;
        }

        post.MediaWidth = media.getInt("width");
        post.MediaHeight = media.getInt("height");
        post.MediaUrl = media.getString("url");

        return  post;
    }
}
