package apcsa.finalproject;

import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.widget.Button;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    //instance variables
    ImageView rmImgView;
    Button start;
    Button computer;
    Button ball;
    AnimationDrawable mainRoom;
    TextView title;
    Player p;
    MediaPlayer mediaPlayer;
    Typeface typeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //make new font because the default fonts were eh
        typeFace = Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/indieflower.ttf");
        //create and play main music
        mediaPlayer = MediaPlayer.create(this, R.raw.room_music);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        //set and play background animation
        rmImgView = (ImageView) findViewById(R.id.room_background);
        rmImgView.setBackgroundResource(R.drawable.room_anim);
        mainRoom = (AnimationDrawable)rmImgView.getBackground();
        mainRoom.start();
        //make title and set font to better font than sans serif
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(typeFace);
        //initialize variables to respective buttons
        ball = (Button) findViewById(R.id.activate_8ball);
        ball.setOnClickListener(this);
        computer = (Button) findViewById(R.id.activate_computer);
        computer.setOnClickListener(this);
        start = (Button) findViewById(R.id.startbutton);
        start.setOnClickListener(this);
        start.setTypeface(typeFace);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            //if button is clicked
            case R.id.activate_computer:
                //open spaceinvaders activity
                startActivity(new Intent(this, GameActivity.class));
                break;

            case R.id.startbutton:
                //start game
                start.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                computer.setVisibility(View.VISIBLE);
                ball.setVisibility(View.VISIBLE);
                break;

            case R.id.activate_8ball:
                //open 8ball activity
                startActivity(new Intent(this, Magic8.class));
                break;

            default:
                break;
        }
    }


}
