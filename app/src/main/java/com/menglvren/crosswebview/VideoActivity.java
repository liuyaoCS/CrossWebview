package com.menglvren.crosswebview;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView video;
    MediaController mediaco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent it=new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.parse("http://vod.chinaso.com/video/2016/3/14/20163141457920349857_21_1.mp4"),"video/mp4");
        startActivity(it);
    }
}
