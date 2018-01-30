package eecs40.tamagotchi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by autumnlee1 on 5/31/16.
 */
public class Letter {
    boolean letter = false;
    final int ink = 1000;
    int[] pointsX = new int[ink];
    int[] pointsY = new int[ink];
    int pointCount = 0;
    int height, width;
    int size;
    Paint pen = new Paint();

    public Letter(int width, int height){
        size = 8;
        this.width = width;
        this.height = height;
        pen.setColor(Color.BLACK);
    }
    void mail(float x, float y){
        if(x >= width/2 + width*18/100 && x <= width * 97/100 && y >= height * 85/100 && y <= height * 99/100) {
            if (!letter) {
                letter = true;
            } else {
                letter = false;
                //clear ink
                for (int i = 0; i < pointCount; i++){
                    pointsX[i] = 0;
                    pointsY[i] = 0;
                }
                pointCount = 0;
            }
        }
      }
    public void write(float mx, float my){
        if (pointCount < ink){
            pointsX[pointCount] = (int)mx;
            pointsY[pointCount] = (int)my;
            pointCount++;
        }
    }

    public void draw(Canvas canvas){
        for (int i = 0; i < pointCount; i++){
            if (pointsX[i] >= width*3/100 + size && pointsY[i] >= height/25 + size && pointsX[i] <= width*97/100 - size && pointsY[i] <= height*4/5 - size){
                canvas.drawCircle(pointsX[i], pointsY[i], size, pen);
            }
        }
    }
}
