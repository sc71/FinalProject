package apcsa.finalproject;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;

public class GameActivity extends AppCompatActivity{
    //implements View.OnTouchListener

    private GameView gameView;
    public static int maxX, maxY;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get display size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        maxX = display.getWidth();
        maxY = display.getHeight();
        //make new gameview and run the game
        gameView = new GameView(this, size.x, size.y);
        gameView.run();
        setContentView(gameView);
    }
            protected void onPause() {
                super.onPause();
                gameView.pause();
            }

            protected void onResume() {
                super.onResume();
                gameView.resumeGame();
            }
}