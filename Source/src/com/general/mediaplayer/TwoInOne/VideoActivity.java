package com.general.mediaplayer.TwoInOne;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import android.net.Uri;
import android.media.MediaPlayer;
import com.general.mediaplayer.Utils.CsrActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Donald Pae
 * Date: 1/17/14
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class VideoActivity extends CsrActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        final VideoView videoView = (VideoView)findViewById(R.id.videoView);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (CommonData.VIDEO_LOOPING == 0) {
                    Intent intent = new Intent(VideoActivity.this, ScanMediaActivity.class);
                    startActivity(intent);
                    overridePendingTransition(TransformManager.GetVideoInAnim(), TransformManager.GetVideoOutAnim());
                    finish();
                }
                else
                {
                    mp.reset();
                    videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/" + R.raw.twoinone));  //Don't put extension
                }
            }
        });

        /*videoView.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });*/

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer arg0) {
                videoView.start();

            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(VideoActivity.this, ScanMediaActivity.class);
                startActivity(intent);
                overridePendingTransition(TransformManager.GetVideoInAnim(), TransformManager.GetVideoOutAnim());
                finish();
                return true;
            }
        });


        ResolutionSet._instance.iterateChild(findViewById(R.id.layoutVideo));
    }

    @Override
    public void onStart()
    {
        super.onStart();

        VideoView videoView = (VideoView)findViewById(R.id.videoView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)videoView.getLayoutParams();
        int width  = layoutParams.width;
        int height = layoutParams.height;
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/" + R.raw.twoinone));  //Don't put extension
        //videoView.start();
    }
}