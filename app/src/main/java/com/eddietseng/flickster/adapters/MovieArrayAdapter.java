package com.eddietseng.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eddietseng.flickster.DetailActivity;
import com.eddietseng.flickster.R;
import com.eddietseng.flickster.TrailerActivity;
import com.eddietseng.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by eddietseng on 7/19/16.
 */
public class MovieArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int REGULAR = 0, BACKDROP_IMG = 1;
    private static final String URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private Context context;
    private List<Movie> movies;

    public MovieArrayAdapter(Context context, List<Movie> movies ) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType)
        {
            case REGULAR:
                View regularView = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new ViewHolderRegular(regularView);
                break;

            case BACKDROP_IMG:
                View backdropView = inflater.inflate(R.layout.item_movie_backdrop, parent, false);
                viewHolder = new ViewHolderBackdrop(backdropView);
                break;

            default:
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case REGULAR:
                ViewHolderRegular vhr = (ViewHolderRegular)holder;
                configureViewHolderRegular(vhr,position);
                break;

            case BACKDROP_IMG:
                ViewHolderBackdrop vhb = (ViewHolderBackdrop) holder;
                configureViewHolderBackdrop(vhb,position);
                break;

            default:
                break;
        }
    }

    private void configureViewHolderRegular(ViewHolderRegular vhr, int position){
        Movie movie = movies.get(position);

        //Clear out image from convertView
        vhr.image.setImageResource(0);

        vhr.title.setText(movie.getOriginalTitle());
        vhr.overview.setText(movie.getOverview());

        int orientation = context.getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(context).load(movie.getPosterPath())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.movieplaceholder_re).into(vhr.image);
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(context).load(movie.getBackdropPath())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.filmplaceholder_re).into(vhr.image);
        }
    }

    private void configureViewHolderBackdrop(ViewHolderBackdrop vhb, int position){
        Movie movie = movies.get(position);

        //Clear out image from convertView
        vhb.backdrop.setImageResource(0);

        Picasso.with(context).load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(10, 10))
                .placeholder(R.drawable.filmplaceholder_1280).into(vhb.backdrop);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(movies.get(position).getVoteAverage() < 5) {
            return REGULAR;
        }
        else {
            return BACKDROP_IMG;
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        movies.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Movie> list) {
        movies.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolderRegular extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView image;
        public TextView title, overview;

        public ViewHolderRegular(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.ivMovieImage);
            title = (TextView) itemView.findViewById(R.id.tvDetailTitle);
            overview = (TextView) itemView.findViewById(R.id.tvOverview);

            itemView.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            Movie movie = movies.get(position);

            Log.d("ViewHolder", "CLICK on item called");
            Intent intent = new Intent (v.getContext(), DetailActivity.class);
            intent.putExtra("movie", movie );

            v.getContext().startActivity(intent);
        }
    }

    public class ViewHolderBackdrop extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView backdrop;

        public ViewHolderBackdrop(View itemView) {
            super(itemView);
            backdrop = (ImageView) itemView.findViewById(R.id.ivMovieBackdrop);

            itemView.setOnClickListener( this );
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition(); // gets item position
            int id = movies.get(position).getId();

            Log.d("ViewHolder", "CLICK on item called");

            AsyncHttpClient client = new AsyncHttpClient();

            client.get(URL + id + API_KEY, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray trailerResults = null;
                    String trailerId;

                    try {
                        trailerResults = response.getJSONArray("youtube");
                        trailerId = getTrailerId(trailerResults);
                        Log.d("DEBUG", trailerId );

                        Intent intent = new Intent (context, TrailerActivity.class);
                        intent.putExtra("trailer_id", trailerId );

                        context.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString,
                                      Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    private String getTrailerId( JSONArray array ) throws JSONException {
        for( int i = 0; i < array.length(); ++i) {
            JSONObject obj = array.getJSONObject(i);

            if(obj.getString("type").equals("Trailer")) {
                if(obj.getString("name").contains("Official Trailer"))
                    return obj.getString("source");
            }

        }
        return "";
    }
}
