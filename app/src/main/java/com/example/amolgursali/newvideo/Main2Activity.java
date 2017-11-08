package com.example.amolgursali.newvideo;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Main2Activity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener
{


    SeekBar seekBar;
    ImageView img,maximize;
    SurfaceView surfaceView;
    MediaPlayer mediaPlayer;
    SurfaceHolder surfaceHolder;
    public static int pos;
    boolean stop = true;
    Thread thread;
    public static final String url = "http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4";
    int value;
    public static int pos1;
    public static boolean mode=true;
    TextView startTime, endTime;
    LinearLayout header;
    RelativeLayout relative;
    RelativeLayout frameLayout;
    public static boolean running;
    Timer timer;
    Handler handler;
    LinearLayout linear;
    public static int orgprogress;
    public static int latestProgress;
    public static boolean touch=true;
//    ImageView bgimg;
    public static int p;
    RelativeLayout seeklayout;
    RelativeLayout.LayoutParams layoutParams;
    RelativeLayout relativeparent;
    ProgressBar progressBar;
    public static int touchvisible;
    public static int maxtouchland;
    public static int currentpos;
    View v1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initialize();

    }

    private void initialize()
    {
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        img = (ImageView) findViewById(R.id.media);
        maximize = (ImageView) findViewById(R.id.maximizes);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        startTime = (TextView) findViewById(R.id.starttime);
        endTime = (TextView) findViewById(R.id.endtime);
//        bgimg=(ImageView)findViewById(R.id.bg);
        seeklayout=(RelativeLayout)findViewById(R.id.seeklayout);
        v1=(View)findViewById(R.id.headers);
        linear=(LinearLayout)findViewById(R.id.linear);
        relative=(RelativeLayout)findViewById(R.id.relative);
        frameLayout=(RelativeLayout)findViewById(R.id.frame);
        relativeparent=(RelativeLayout)findViewById(R.id.relativeparent);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(Main2Activity.this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        value=getApplicationContext().getResources().getConfiguration().orientation;


        maximize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mode==true)
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mode=false;
                    maxtouchland=1;
                }
                else
                {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mode=true;
                }

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stop == true)
                {
                    mediaPlayer.pause();
                    pos1=mediaPlayer.getCurrentPosition();
                    img.setImageResource(R.mipmap.pause_video_vert);
                    stop = false;

                }
                else
                {
                    mediaPlayer.start();
                    seekbarplayvideo();
                    updateTime();
                 //   mediaPlayer.seekTo(pos1);
                    running=true;
                    img.setImageResource(R.mipmap.play_video_vert);
                    stop = true;
                }

            }
        });



    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();

        // if we want to play video again then use below method

        // mp.setLooping(true);

        String secondsString = "";
        String finalTime = "";



        int hours;

        hours = (int) (mediaPlayer.getDuration() / (1000 * 60 * 60));

        if(hours>0)
        {
            finalTime =hours+":";
        }
        int minutes = (int) (mediaPlayer.getDuration() % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((mediaPlayer.getDuration() % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        endTime.setText(finalTime+minutes+":"+secondsString);




        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        progressBar.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });


        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                img.setImageResource(R.mipmap.pause_video_vert);
                Toast.makeText(Main2Activity.this, "Video Completed", Toast.LENGTH_SHORT).show();
            }
        });


        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent)
            {
                if(percent<mp.getDuration())
                {
                    seekBar.setSecondaryProgress(percent);
                    seekBar.setSecondaryProgress(percent/100);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser)
                {
                    if(mediaPlayer!=null)
                    {
                        if(seekBar.getProgress()<latestProgress)
                        {
                            mediaPlayer.seekTo(progress);
                        }
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

                orgprogress=seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {



                System.out.println("THE PROCESS TIMUING"+orgprogress+" "+seekBar.getProgress());



                 if(orgprogress>seekBar.getProgress())
                {
                    latestProgress=orgprogress;
                    currentpos=seekBar.getProgress();
                    mediaPlayer.seekTo(seekBar.getProgress());
                    System.out.println("THE Latest Process"+latestProgress+" "+seekBar.getProgress());

                }
                else
                {
                  seekBar.setProgress(orgprogress);
                }


            }
        });



        progressBar.setVisibility(View.GONE);

        updateTime();

        seekbarplayvideo();
    }



    private void updateTime()
    {

        final String[] finalTime = {""};
        final String[] secondsString = {""};
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            startTime.post(new Runnable() {
                                @Override
                                public void run()
                                {

                                    int hours;

                                    try {
                                        Thread.sleep(100);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    hours = (int) (mediaPlayer.getCurrentPosition() / (1000 * 60 * 60));

                                    if(hours>0)
                                    {
                                        finalTime[0] =hours+":";
                                    }
                                    int minutes = (int) (mediaPlayer.getCurrentPosition() % (1000 * 60 * 60)) / (1000 * 60);
                                    int seconds = (int) ((mediaPlayer.getCurrentPosition() % (1000 * 60 * 60)) % (1000 * 60) / 1000);

                                    if (seconds < 10) {
                                        secondsString[0] = "0" + seconds;
                                    } else {
                                        secondsString[0] = "" + seconds;
                                    }

                                    startTime.setText(finalTime[0]+minutes+":"+secondsString[0]+"");
                                }
                            });
                        } else {
                            timer.cancel();
                            timer.purge();
                        }
                    }
                });

            }
        },0,1000);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(surfaceHolder);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnPreparedListener(Main2Activity.this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (mediaPlayer != null) {
            pos = mediaPlayer.getCurrentPosition();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void seekbarplayvideo()
    {
        seekBar.setMax(mediaPlayer.getDuration());

        if(running==true)
        {
            mediaPlayer.seekTo(pos1);
        }

        if (pos != 0) {
            mediaPlayer.seekTo(pos);
            mediaPlayer.start();
        }


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!Thread.interrupted()) {
                    try {

                        while (mediaPlayer != null && mediaPlayer.isPlaying())
                        {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());

                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
/*
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            layoutParams=(RelativeLayout.LayoutParams)surfaceView.getLayoutParams();
            RelativeLayout.LayoutParams params;
            params=new RelativeLayout.LayoutParams(0, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            params.width=ViewGroup.LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            surfaceView.setLayoutParams(params);
            RelativeLayout.LayoutParams paramsBottom = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            paramsBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, frameLayout.getId());
            paramsBottom.setMargins(10,20,10,10);
            seeklayout.setLayoutParams(paramsBottom);
//            v1.setVisibility(View.GONE);
//            bgimg.setVisibility(View.GONE);
            seeklayout.setVisibility(View.VISIBLE);

        }
        else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {
                surfaceView.setLayoutParams(layoutParams);
//                v1.setVisibility(View.VISIBLE);
                relativeparent.setBackgroundResource(R.mipmap.bg_video);
                RelativeLayout.LayoutParams paramsBottom = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramsBottom.addRule(RelativeLayout.BELOW, linear.getId());
                paramsBottom.setMargins(10,20,10,0);
                seeklayout.setLayoutParams(paramsBottom);
                seeklayout.setVisibility(View.VISIBLE);
        }
    }*/
}

