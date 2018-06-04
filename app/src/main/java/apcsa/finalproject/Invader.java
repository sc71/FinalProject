package apcsa.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Invader {
    Random generator = new Random();
    //bitmap with enemy image
    private Bitmap bitmap1;

    //size
    private float length;
    private float height;

    //top left coordinates
    private float x;
    private float y;

    private float enemySpeed;

    //left and right values
    public final int LEFT = 1;
    public final int RIGHT = 2;
    //collision rectF
    public RectF rect;

    //which direction is the invader moving in
    private int enemyMoving = RIGHT;

    //is invader active
    boolean isVisible;
    public Invader(Context context, int row, int column, int screenX, int screenY) {
        //make new rectF
        rect = new RectF();
        //set size based on screen size
        length = screenX / 20;
        height = screenY / 20;
        //makes invader active
        isVisible = true;
        //spacing
        int padding = screenX / 25;
        //set coordinates
        x = column * (length + padding);
        y = row * (length + padding/4);

        // Initialize the bitmap
        bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.sienemy1);

        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap1 = Bitmap.createScaledBitmap(bitmap1,
                (int) (length),
                (int) (height),
                false);
        // How fast is the invader in pixels per second
        enemySpeed = 40;
    }

    //set visibility
    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap1;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    //update position and rectF
    public void update(long fps){
        if(enemyMoving == LEFT){
            x = x - enemySpeed / fps;
        }

        if(enemyMoving == RIGHT){
            x = x + enemySpeed / fps;
        }
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;
    }
    //drop down a row and change direction
    public void drop(){
        if(enemyMoving == LEFT){
            enemyMoving = RIGHT;
        }else{
            enemyMoving = LEFT;
        }

        y = y + height;

        enemySpeed = enemySpeed * 1.18f;
    }
    //should the invader shoot
    public boolean takeAim(float enemyShipX, float playerShipLength){

        int randomNumber = -1;
        if((enemyShipX + playerShipLength > x &&
                enemyShipX + playerShipLength < x + length) || (enemyShipX > x && enemyShipX < x + length)) {
            randomNumber = generator.nextInt(150);
            if(randomNumber == 0) {
                return true;
            }
        }
        randomNumber = generator.nextInt(2000);
        if(randomNumber == 0){
            return true;
        }

        return false;
    }

}
