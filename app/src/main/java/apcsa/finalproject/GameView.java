package apcsa.finalproject;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Canvas;

import java.io.IOException;
import static java.lang.Thread.sleep;

public class GameView extends SurfaceView implements Runnable {
    //is the game playing
    private volatile boolean playing;
    private Thread gameThread;
    //player
    private static Player p;
    //used to draw game
    private Paint paint, paint2;
    private Canvas canvas;
    //calculate updates
    private long fps, timeThisFrame;
    //screen size
    private int screenX, screenY;
    //main context
    private Context context;
    private SurfaceHolder surfaceHolder;
    private boolean paused = true;
    //sfx
    private SoundPool soundPool;
    //fonts
    private Typeface typeface;

    //invader bullets
    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;

    //invaders
    Invader[] invaders = new Invader[60];
    int numInvaders = 0;

    //array of sfx
    static int[] sfx;

    private DefenceBrick[] bricks = new DefenceBrick[400];
    private int numBricks;
    private Bullet bullet;

    int score = 0;
    private int lives = 3;

    public GameView(Context context, int x, int y)
    {
        //assign variable values
        super(context);
        screenX = x;
        screenY = y;
        paint = new Paint();
        paint2 = new Paint();
        paint.setColor(Color.WHITE);
        paint2.setColor(Color.WHITE);
        canvas = new Canvas();
        this.context = context;
        surfaceHolder = getHolder();
        typeface = Typeface.createFromAsset(context.getAssets(),"fonts/robotomono-bold.ttf");
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        //fill array with different sfx
        sfx = new int[4];
        sfx[0] = soundPool.load(context, R.raw.hit_enemy_sfx, 1);
        sfx[1] = soundPool.load(context, R.raw.hit_player_sfx, 1);
        sfx[2] = soundPool.load(context, R.raw.hit_shelter, 1);
        sfx[3] = soundPool.load(context, R.raw.player_shot, 1);
        //calls to make new level
        prepareLevel();
    }

    private void prepareLevel(){

        //create player
        p = new Player(context, screenX, screenY);
        //create player bullet
        bullet = new Bullet(screenY);
        //create array of invader bullets
        for(int i = 0; i < invadersBullets.length; i++){
            invadersBullets[i] = new Bullet(screenY);
        }
        //number of invaders
        numInvaders = 0;
        //make invaders in rows and columns
        for(int column = 0; column < 6; column ++ ){
            for(int row = 0; row < 5; row ++ ){
                invaders[numInvaders] = new Invader(context, row, column, screenX, screenY);
                numInvaders ++;
            }
        }
        //number of bricks
        numBricks = 0;
        //creates 4 defence bricks
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new DefenceBrick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }
    }

    //update the gameview
    public void update() {
        //has the invaders bumped against a wall
        boolean bumped = false;
        //has the player run out of lives
        boolean lost = false;
        //update the player
        p.update(fps);

        //update the invaders and randomly shoot
        for(int i = 0; i < numInvaders; i++){
            //if the invader is visible
            if(invaders[i].getVisibility()) {
                //update the invader
                invaders[i].update(fps);
                //if takeaim is true
                if(invaders[i].takeAim(p.getxPos(),
                        p.getLength())){
                    //shoot a bullet
                    if(invadersBullets[nextBullet].shoot(invaders[i].getX()
                                    + invaders[i].getLength() / 2,
                            invaders[i].getY(), bullet.DOWN)) {
                        //get the next bullet ready for shooting
                        nextBullet++;
                        //if we are out of bullets, restart array
                        if (nextBullet == maxInvaderBullets) {
                            nextBullet = 0;
                        }
                    }
                }
                //the invader has hit the screen edge
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0){

                    bumped = true;
                }
            }

        }
        //if the bullet is active, update
        if(bullet.getStatus()){
            bullet.update(fps);
        }
        //if the invader is active, update
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                invadersBullets[i].update(fps);
            }
        }
        //if the invaders have hit the wall
        //drop down and change direction
        if(bumped){
            for(int i = 0; i < numInvaders; i++){
                invaders[i].drop();
                //if the invaders pass the lower tenth of the screen, the game is lost
                if(invaders[i].getY() > screenY - screenY / 10){
                    lost = true;
                }
            }
        }
        //if the game is lost, make new level
        if(lost){
            prepareLevel();
        }
        //if the bullet leaves the screen, set inactive so it may be fired again
        if(bullet.getImpactPointY() < 0){
            bullet.setInactive();
        }
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getImpactPointY() > screenY){
                invadersBullets[i].setInactive();
            }
        }
        //if the bullet hits an invader
        if(bullet.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), invaders[i].getRect())) {
                        //defeat invader
                        invaders[i].setInvisible();
                        //play sfx
                        soundPool.play(sfx[0], 1, 1, 0, 0, 1f);
                        //set bullet inactive
                        bullet.setInactive();
                        //add to score
                        score = score + 10;

                        //if score = all invaders defeated, start game over
                        if(score == numInvaders * 10){
                            paused = true;
                            score = 0;
                            lives = 3;
                            prepareLevel();
                        }
                    }
                }
            }
        }
        //if the invader bullet hits a defence brick
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                for(int j = 0; j < numBricks; j++){
                    if(bricks[j].getVisibility()){
                        if(RectF.intersects(invadersBullets[i].getRect(), bricks[j].getRect())){
                            //play sfx
                            soundPool.play(sfx[2], 1, 1, 0, 0, 1);
                            //set bullet inactive
                            invadersBullets[i].setInactive();
                            //destroy brick
                            bricks[j].setInvisible();

                        }
                    }
                }
            }

        }
        //if the bullet hits the player
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                if(RectF.intersects(p.getRect(), invadersBullets[i].getRect())){
                    invadersBullets[i].setInactive();
                    //lose a life
                    lives --;
                    //play sfx
                    soundPool.play(sfx[1], 1, 1, 0, 0, 1);
                    //if no lives are left, start game over
                    if(lives == 0){
                        paused = true;
                        lives = 3;
                        score = 0;
                        prepareLevel();

                    }
                }
            }
        }
    }


    public void run() {
        while (playing) {
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();
            control();
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    public void draw() {
        if(surfaceHolder.getSurface().isValid()) {
            //make canvas editable (dunno if that's a word)
            canvas = surfaceHolder.lockCanvas();
            //set background black
            canvas.drawColor(Color.BLACK);
            //draw player
            canvas.drawBitmap(p.getBitmap(), p.getxPos(), p.getyPos(), paint);
            //if bullet is active, draw it
            if(bullet.getStatus()) {
                canvas.drawRect(bullet.getRect(), paint);
            }
            for(int i = 0; i < invadersBullets.length; i++){
                if(invadersBullets[i].getStatus()) {
                    canvas.drawRect(invadersBullets[i].getRect(), paint);
                }
            }
            //if invader is active, draw it
            for(int i = 0; i < numInvaders; i++){
                if(invaders[i].getVisibility()) {
                        canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                    }
                }
                //if defence brick is active, draw it
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }
            //set score and lives text
            paint2.setTextSize(80);
            paint2.setTypeface(typeface);
            canvas.drawText("Score: " + score + "  Lives: " + lives, 50,70, paint2);
            //lock the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void control() {
        try {
            //maintains framerate
            sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        playing = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    //if screen is touched
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            //if screen is pressed
            case MotionEvent.ACTION_DOWN:
                paused = false;
                //if in lower eighth of screen, move left and right based on x coordinate of touch
                if(motionEvent.getY() > screenY - screenY / 8) {
                    if (motionEvent.getX() > screenX / 2) {
                        p.setMovementState(p.RIGHT);
                    } else {
                        p.setMovementState(p.LEFT);
                    }
                }
                //if in upper eighth, shoot bullet
                if(motionEvent.getY() < screenY - screenY / 8) {
                    if(bullet.shoot(p.getxPos()+p.getLength()/2 - 30,screenY - 300,bullet.UP)){
                        soundPool.play(sfx[3], 1, 1, 0, 0, 1);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                //if the screen is released, stop moving
                    p.setMovementState(p.STOPPED);
                break;
        }
        return true;
    }

}

