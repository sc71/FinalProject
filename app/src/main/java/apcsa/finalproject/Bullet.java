package apcsa.finalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Bullet {
    //coordinates
    private float x;
    private float y;
    //collison rectF
    public RectF rect;
    //bullet direction (up = player, down = enemy)
    public final int UP = 0;
    public final int DOWN = 1;

    int heading = -1;
    float speed =  350;
    //bullet size
    private int width = 4;
    private int height;
    //has the bullet been fired
    private boolean isActive;

    //constructs a bullet with a height 1/20 of the screen height and declares it inactive
    public Bullet(int screenY) {
        height = screenY / 20;
        isActive = false;

        rect = new RectF();
    }
    //get collison rectF
    public RectF getRect(){
        return rect;
    }

    //is the bullet visible and moving
    public boolean getStatus(){
        return isActive;
    }

    //set bullet back to inactive
    public void setInactive(){
        isActive = false;
    }

    //what is the part of the bullet that will trigger the events
    public float getImpactPointY(){
        if (heading == DOWN){
            return y + height;
        }else{
            return  y;
        }

    }
    //makes the bullet object visible and sets coordinates
    public boolean shoot(float x, float y, int direction) {
        if (!isActive) {
            this.x = x;
            this.y = y;
            heading = direction;
            isActive = true;
            return true;
        }
        return false;
    }
    //move the bullet up or down and adjust the rectF accordingly
    public void update(long fps){
        if(heading == UP){
            y = y - speed / fps;
        }else{
            y = y + speed / fps;
        }
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }
}
