package com.devsenheizer.myflickrgallery;


import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FlickrFetcher {
    private static final String TAG = "FlickrFetcher";
    private static final String API_KEY = "34cebf20da0afb0759adf694e2ebcb5e";


    public String getJSONString(String urlSpec) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(urlSpec)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;
    }

    public List<GalleryItem> fetchItems() {
        List<GalleryItem> items = new ArrayList<>();
        try {


            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", " 1 ")
                    .appendQueryParameter("extras", "url_s")
                    .build()
                    .toString();

            String jsonString = getJSONString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (JSONException e) {
            Log.e(TAG, "Loading Data Error", e);
        } catch (IOException e) {
            Log.e(TAG, "Parsing Json Error", e);

        }
        return items;
    }

    public void parseItems(List<GalleryItem> galleryItems, JSONObject jsonBody) throws JSONException {
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonObject = photosJsonObject.getJSONArray("photo");


        for (int i = 0; i < photoJsonObject.length(); i++) {
            JSONObject photoObject = photoJsonObject.getJSONObject(i);
            GalleryItem galleryItem = new GalleryItem();
            galleryItem.setId(photoObject.getString("id"));
            galleryItem.setCaption(photoObject.getString("title"));

            if (!photoObject.has("url_s")) continue;

            galleryItem.setUrl(photoObject.getString("url_s"));
            galleryItems.add(galleryItem);
        }
    }
}
