package example.codeclan.com.youtubeapp;

import android.nfc.Tag;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.content.ContentValues.TAG;

public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    static final String GOOGLE_API_KEY = "AIzaSyAutXe5s3Tv6D9WiTt-9qgs2NFD4lYcbfI";
    static final String YOUTUBE_VIDEO_ID = "5MjBMhlMq3s";
    static final String YOUTUBE_PLAYLIST = "PL49AE65994CB8A791";
    private static final String TAG = "YouTubeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConstraintLayout layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_you_tube, null);
        setContentView(layout);

        YouTubePlayerView playerView = new YouTubePlayerView(this);
        playerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(playerView);
        playerView.initialize(GOOGLE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        Log.d(TAG, "onInitializationSuccess: provider is " + provider.getClass().toString());
        Toast.makeText(this, "Initialised YouTube Player successfully", Toast.LENGTH_LONG).show();
        youTubePlayer.setPlaybackEventListener(playBackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        if(!wasRestored){
            youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        final int REQUEST_CODE = 1;
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();
        } else {
            String errorMessage = String.format("There was an error initialising youTube player (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }
    private YouTubePlayer.PlaybackEventListener playBackEventListener = new YouTubePlayer.PlaybackEventListener(){
        @Override
        public void onPlaying() {
            Toast.makeText(YouTubeActivity.this, "Video is playing ok.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onPaused() {
            Toast.makeText(YouTubeActivity.this, "Video is paused.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onStopped() {
            Toast.makeText(YouTubeActivity.this, "Video has stopped.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onBuffering(boolean b) {
        }
        @Override
        public void onSeekTo(int i) {
        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
        }
        @Override
        public void onLoaded(String s) {
        }
        @Override
        public void onAdStarted() {
            Toast.makeText(YouTubeActivity.this, "Click the ad. The creator's gotta eat.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onVideoStarted() {
            Toast.makeText(YouTubeActivity.this, "Video started.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onVideoEnded() {
            Toast.makeText(YouTubeActivity.this, "Video completed.", Toast.LENGTH_LONG).show();
        }
        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
        }
    };
}
