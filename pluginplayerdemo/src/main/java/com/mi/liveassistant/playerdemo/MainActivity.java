package com.mi.liveassistant.playerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.mi.liveassistant.player.VideoPlayerWrapperView;

public class MainActivity extends AppCompatActivity {
    private VideoPlayerWrapperView mPlayerWrapperView;
    private TextView mPlayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayerWrapperView = (VideoPlayerWrapperView) findViewById(R.id.player_view);
        mPlayerWrapperView.setVideoTransMode(VideoPlayerWrapperView.TRANS_MODE_CENTER_INSIDE);

        mPlayBtn = (TextView) findViewById(R.id.play_btn);
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayerWrapperView.play("http://v2.zb.mi.com/live/20215862_1489559808.flv?playui=0");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayerWrapperView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlayerWrapperView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayerWrapperView.release();
    }
}
