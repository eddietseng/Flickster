package com.eddietseng.flickster;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.eddietseng.flickster.databinding.ActivityDetailBinding;
import com.eddietseng.flickster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


public class DetailActivity extends AppCompatActivity {
    private static final String URL = "https://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    Context context;
    ImageView imageView;
    TextView titleTextView;
    TextView releaseDateTextView;
    RatingBar voteRatingBar;
    TextView overviewTextView;
    Movie movie;

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        context = this;

        movie = (Movie)getIntent().getSerializableExtra("movie");

        imageView = binding.ivDetailBackdrop;
        titleTextView = binding.tvDetailTitle;
        releaseDateTextView = binding.tvDetailReleaseDate;
        voteRatingBar = binding.rbDetailVotes;
        overviewTextView = binding.tvDetailOverview;

        configureViews();
    }

    private void configureViews() {
        //Clear out image from convertView
        imageView.setImageResource(0);

        titleTextView.setText(movie.getOriginalTitle());
        releaseDateTextView.setText(movie.getReleaseDate());
        voteRatingBar.setRating((float)(movie.getVoteAverage()/2));
        overviewTextView.setText(movie.getOriginalOverview());

        Picasso.with(this).load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(10, 10))
                .placeholder(R.drawable.filmplaceholder_re).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ImageView", "CLICK on item called");

                    AsyncHttpClient client = new AsyncHttpClient();

                    client.get(URL + movie.getId() + API_KEY, new JsonHttpResponseHandler(){
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
            });
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
