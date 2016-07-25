package com.eddietseng.flickster;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class TrailerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private static final String YOUTUBE_API_KEY = "AIzaSyCtHQy_L4K5kAqW0TBlY9lQyYFKpjyAPTE";
    private String trailerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        trailerId = getIntent().getStringExtra("trailer_id");

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize(YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {
        // play video
        if (trailerId != null) {
            youTubePlayer.loadVideo(trailerId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        } else {
            String errorMessage = "There was an error initializing the YouTubePlayer";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

}
