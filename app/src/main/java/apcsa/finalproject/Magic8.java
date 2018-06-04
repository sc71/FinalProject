package apcsa.finalproject;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Magic8 extends AppCompatActivity implements View.OnClickListener{

    //initialize button and animations
    private ImageButton ball;
    private AnimationDrawable anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic8);
        //set variables to layout resources
        ball = findViewById(R.id.ball);
        ball.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //if ball is clicked
            case R.id.ball:
                //1 in 8 chance of randomly picking 1 of 8 animations
                if((int) (Math.random() * 8 + 1) == 1){
                    ball.setBackgroundResource(R.drawable.askyourself);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
                else if((int) (Math.random() * 8 + 1) == 2){
                    ball.setBackgroundResource(R.drawable.yes);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
                else if((int) (Math.random() * 8 + 1) == 3){
                    ball.setBackgroundResource(R.drawable.no);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
                else if((int) (Math.random() * 8 + 1) == 4){
                    ball.setBackgroundResource(R.drawable.probably);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
                else if((int) (Math.random() * 8 + 1) == 5){
                    ball.setBackgroundResource(R.drawable.probablynot);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
                else if((int) (Math.random() * 8 + 1) == 6){
                    ball.setBackgroundResource(R.drawable.unknown);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
                else if((int) (Math.random() * 8 + 1) == 7){
                    ball.setBackgroundResource(R.drawable.trylater);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
                else{
                    ball.setBackgroundResource(R.drawable.maybe);
                    anim = (AnimationDrawable)ball.getBackground();
                    anim.start();
                }
        }
    }
}
