package com.devsenheizer.myflickrgallery;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    private final static String TAG = "GalleryActivity";

    private RecyclerView mRecycler;
    private List<GalleryItem> mGalleryItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

         mRecycler = findViewById(R.id.photo_gallery_recycler_view);
         mRecycler.setLayoutManager(new GridLayoutManager(this,3));
         new FetcherProcessor().execute();
         setupAdapter();
    }
    private void setupAdapter() {
        mRecycler.setAdapter(new PhotoAdapter(mGalleryItems));
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView itemImageView;
        public PhotoHolder(View itemView) {
            super(itemView);
            itemImageView  = itemView.findViewById(R.id.photo_gallery_image_view);
        }
    }
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> galleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            this.galleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(GalleryActivity.this);
            View v = inflater.inflate(R.layout.galary_item, parent , false);
            return new PhotoHolder(v);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
        GalleryItem galleryItem = galleryItems.get(position);
            Picasso.with(GalleryActivity.this).load(galleryItem.getUrl()).into(holder.itemImageView);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    class FetcherProcessor extends AsyncTask<Void,Void,List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new FlickrFetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
            setupAdapter();
        }
    }
}
