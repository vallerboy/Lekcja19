package pl.oskarpolak.lekcja13;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {


    MediaRecorder mediaRecorder;

    MediaPlayer mediaPlayer;

    File ourAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ourAudio = new File(getFilesDir().getAbsolutePath() + "/ourAudio.3gp");
    }

    @OnClick(R.id.record)
    public void buttonStartRecording(){
        startRecording();
    }
    @OnClick(R.id.stopRecord)
    public void buttonStopRecording(){
        stopRecording();
    }
    @OnClick(R.id.play)
    public void buttonPlayRecord(){
        playRecord();
    }
    @OnClick(R.id.stopPlay)
    public void buttonStopPlay(){
        stopPlaying();
    }

    private  void startRecording(){
          mediaRecorder = new MediaRecorder();
          mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
          mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
          mediaRecorder.setOutputFile(ourAudio.getAbsolutePath());
          mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);


        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();



    }

    private void stopRecording(){
        mediaRecorder.release();
        mediaRecorder.stop();
        mediaRecorder = null;
    }


    private void playRecord(){
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(ourAudio.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void stopPlaying(){
        mediaPlayer.release();
        mediaPlayer.stop();
        mediaPlayer = null;
    }


    // Tym sie nie przejmujecie
    private class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}




