package apcsa.finalproject;

import android.graphics.RectF;

public class DefenceBrick {
    private RectF rect;

    private boolean isVisible;

    public DefenceBrick(int row, int column, int shelterNumber, int screenX, int screenY){

        //block dimensions
        int width = screenX / 90;
        int height = screenY / 40;
        isVisible = true;
        int shelterPadding = screenX / 8;
        int startHeight = screenY - (screenY /6 * 2);
        //make shelter block
        rect = new RectF(column * width +
                (shelterPadding * shelterNumber) +
                shelterPadding + shelterPadding * shelterNumber,
                row * height + startHeight,
                column * width + width +
                        (shelterPadding * shelterNumber) +
                        shelterPadding + shelterPadding * shelterNumber,
                row * height + height + startHeight);
    }
    //get shelter block
    public RectF getRect(){
        return this.rect;
    }
    //remove block
    public void setInvisible(){
        isVisible = false;
    }
    //is the block visible
    public boolean getVisibility(){
        return isVisible;
    }
}
