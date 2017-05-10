package com.casii.droid.musicalstructure;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends AppCompatActivity {

    /**
     * Handles playback of all the sound files
     */
    private MediaPlayer mMediaPlayer;

    /**
     * Handles audio focus when playing a sound file
     */
    private AudioManager mAudioManager;
    private int length;
    private ImageButton play;
    private String mp3File;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        play = (ImageButton) findViewById(R.id.play);
        ImageButton pause = (ImageButton) findViewById(R.id.pause);
        ImageButton stop = (ImageButton) findViewById(R.id.stop);
        // Create and setup the {@link AudioManager} to request audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
                Toast.makeText(getApplicationContext(), "Play", Toast.LENGTH_SHORT).show();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMediaPlayer();
                Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
                Toast.makeText(getApplicationContext(), "Stop", Toast.LENGTH_SHORT).show();
            }
        });
        String title = getIntent().getStringExtra("TITLE");
        String artist = getIntent().getStringExtra("ARTIST");
        mp3File = getIntent().getStringExtra("URL");
        getSupportActionBar().setTitle(title);
        TextView songTextView = (TextView) findViewById(R.id.song_text_view);
        songTextView.setText("Title   |   " + title + "   |   Artist   |  " + artist);
    }


    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    private void pause() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                length = mMediaPlayer.getCurrentPosition();
                play.setImageDrawable(getDrawable(R.drawable.ic_play_circle_outline_black_48dp));
            }
        }
    }

    private void play() {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(length);
            length = 0;
            mMediaPlayer.start();
        } else {
            // Release the media player if it currently exists because we are about to
            // play a different sound file
            releaseMediaPlayer();
            // Request audio focus so in order to play the audio file. The app needs to play a
            // short audio file, so we will request audio focus with a short amount of time
            // with AUDIOFOCUS_GAIN_TRANSIENT.
            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // We have audio focus now.

                // Create and setup the {@link MediaPlayer} for the audio resource associated
                // with the current song
                try {
                    final MediaPlayer mMediaPlayer = MediaPlayer.create(this,
                            Uri.parse("http://vprbbc.streamguys.net:80/vprbbc24.mp3"));
                    mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            // Start the audio file
                            mp.start();

                            // Setup a listener on the media player, so that we can stop and release the
                            // media player once the sound has finished playing.
                            mp.setOnCompletionListener(mCompletionListener);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    //  mMediaPlayer = MediaPlayer.create(this, R.raw.gameplay);
                }
            }
        }
        play.setImageDrawable(getDrawable(R.drawable.ic_replay_black_48dp));
    }

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };


    @Override
    public void onStop() {
        super.onStop();
        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            play.setImageDrawable(getDrawable(R.drawable.ic_play_circle_outline_black_48dp));
        }
    }
}
