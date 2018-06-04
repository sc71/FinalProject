package apcsa.finalproject;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Player{

    //image bitmap
    private Bitmap bitmap;
    //coordinates
    private int xPos;
    private int yPos;
    private int speed;
    //collision rectF
    public RectF rect;
    private float shipSpeed;
    //is the ship moving, and in what direction
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    private int shipMoving = STOPPED;
    //image size
    private float length;
    private float height;

    public Player(Context context, int x, int y)
    {
        //make size relative to screen size
        length = x/10;
        height = y/10;
        //set bitmap to player drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_sprite);
        //make new rectF
        rect = new RectF();
        //set player to bottom center of the screen
        xPos = x/2;
        yPos = (y/7)*6;
        speed = 2;
        /*bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
*/
        shipSpeed = 350;
    }

    //update the position based on what direction of movement and update the rectF
    public void update(long fps) {
        if(shipMoving == LEFT){
            xPos = (int) (xPos - shipSpeed / fps);
        }

        if(shipMoving == RIGHT){
            xPos = (int) (xPos + shipSpeed / fps);
        }

        rect.top = yPos;
        rect.bottom = yPos + height;
        rect.left = xPos;
        rect.right = xPos + length;
    }

    //get values
    public RectF getRect(){
        return rect;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public int getxPos()
    {
        return xPos;
    }
    public float getLength(){
        return length;
    }
    public int getyPos()
    {
        return yPos;
    }
    public int getSpeed()
    {
        return speed;
    }
    public void setMovementState(int state){
        shipMoving = state;
    }
}