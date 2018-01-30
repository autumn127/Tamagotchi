package eecs40.tamagotchi;

/**
 * Created by autumnlee1 on 5/24/16.
 */
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap;
import java.util.Random;

public class Player {
    Bitmap image;
    int x1, y1, x2, y2;// for dst
    int i1, j1, i2, j2; // for src
    int vx = 20;
    int vy = 20;
    int run = 30;
    int w = 64;
    int width, height;
    int hearts;
    int action = 0;
    int timer;
    int steps;
    public Player(int width, int height, Bitmap reference){
        this.width = width;
        this.height = height;
        image = reference;
    }

    public void randomAction(){
        if (hearts == 0 || steps == 0){
            action = 12;
        }
        else {
            Random number = new Random();
            if (action >= 7 && action <= 11) {
                number = new Random();
                action = 8 + number.nextInt(4);
                switch (action) {
                    case 8: // run right
                        i1 = 0;
                        j1 = w * 7;
                        i2 = w;
                        j2 = w * 8;
                        break;
                    case 9: // run back
                        i1 = 0;
                        j1 = w * 8;
                        i2 = w;
                        j2 = w * 9;
                        break;
                    case 10: // run left
                        i1 = 0;
                        j1 = w * 9;
                        i2 = w;
                        j2 = w * 10;
                        break;
                    case 11: // run forward
                        i1 = 0;
                        j1 = w * 6;
                        i2 = w;
                        j2 = w * 7;
                        break;
                }
            } else {
                action = number.nextInt(5);
            }
        }
    }

    public void incStep(){
        if (action > 7) {
            steps++;
        }
    }

    public void action(float x, float y){
        //eating
        if (x >= 0 && x <= width *3/10 && y >= height * 85/100 && y <= height){
            timer = 0;
            if (hearts < 10){
                hearts++;
                action = 5;
                i1 = 0;
                j1 = w*5;
                i2 = w;
                j2 = w*6;
            }
            else{ // full
                action = 13;
                i1 = 0;
                j1 = w*12;
                i2 = w;
                j2 = w*13;
            }
        }
        //laughing if user taps hamster
        if (x >= x1 && x <= x2 && y >= y1 && y <= y2){
            action = 6;
            i1 = 0;
            j1 = w*10;
            i2 = w;
            j2 = w*11;

        }
        // running
        if (x >= (width / 2 - width * 12 / 100) && x <= (width / 2 + width * 12 / 100) && y >= height * 83 / 100 && y <= height){
            if(action >= 7 && action <= 11){
                action = 0;
            }
            else{
                steps++;
                timer = 0;
                action = 7;
            }
        }
    }

    public void autoAction(){
        if (timer == 0)
        {
            switch(action) {
                case 0: // standing
                    i1 = 0;
                    j1 = 0;
                    i2 = w;
                    j2 = w;
                    break;
                case 1: //walking forward
                    i1 = 0;
                    j1 = w;
                    i2 = w;
                    j2 = w*2;
                    break;
                case 2: // walk right
                    i1 = 0;
                    j1 = w*2;
                    i2 = w;
                    j2 = w*3;
                    break;
                case 3: // walk back
                    i1 = 0;
                    j1 = w*3;
                    i2 = w;
                    j2 = w*4;
                    break;
                case 4: // walk left
                    i1 = 0;
                    j1 = w*4;
                    i2 = w;
                    j2 = w*5;
                    break;
                case 12:
                    i1 = 0;
                    j1 = w*11;
                    i2 = w;
                    j2 = w*12;
                    break;
            }
        }
    }
    public void update(){
        switch(action){
        // which part of bitmap
            case 0: //standing idle
                if(timer < 3) {
                    i1 += w;
                    i2 += w;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 1: //walking down
                if(timer < 7) {
                    i1 += w;
                    i2 += w;
                    y1 += vy;
                    y2 += vy;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 2: //walking right
                if(timer < 7) {
                    i1 += w;
                    i2 += w;
                    x1 += vx;
                    x2 += vx;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();

                }
                break;
            case 3: //walking up
                if(timer < 7) {
                    i1 += w;
                    i2 += w;
                    y1 -= vy;
                    y2 -= vy;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();

                }
                break;
            case 4: //walking left
                if(timer < 7) {
                    i1 += w;
                    i2 += w;
                    x1 -= vx;
                    x2 -= vx;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 5: //eating
                if(timer < 6) {
                    i1 += w;
                    i2 += w;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                 break;
            case 6: //laughing
                if(timer < 8){
                    i1 += w;
                    i2 += w;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 7: //choose direction to run in
                randomAction();
                break;
            case 8: //run down
                if(timer < 3){
                    i1 += w;
                    i2 += w;
                    x1 += run;
                    x2 += run;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 9: //run right
                if(timer < 3){
                    i1 += w;
                    i2 += w;
                    y1 -= run;
                    y2 -= run;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 10:    //run up
                if(timer < 3){
                    i1 += w;
                    i2 += w;
                    x1 -= run;
                    x2 -= run;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 11:    //run left
                if(timer < 3){
                    i1 += w;
                    i2 += w;
                    y1 += run;
                    y2 += run;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 12:    //crying
                if(timer < 3){
                    i1 += w;
                    i2 += w;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
            case 13:    //overfeeding
                if(timer < 7){
                    i1 += w;
                    i2 += w;
                    timer++;
                }
                else{
                    timer = 0;
                    randomAction();
                }
                break;
        }
        while(((x1 <= 0 && action == 4) || (x2 >= width - width/20 && action == 2) || (y1 <= height/10 && action == 3) || (y2 >= (height * 82/100 ) - height/30  && action == 1)) ||
                (x1 <= 0 && action == 10) || (x2 >= width - width/20 && action == 8) || (y1 <= height/10 && action == 9) || (y2 >= (height * 82/100 ) - height/30  && action == 11)){
            randomAction();
        }
    }

    public void draw(Canvas canvas){
        Rect dst = new Rect();
        dst.set(x1, y1, x2, y2);// where bitmap placed
        Rect src = new Rect();
        src.set(i1, j1, i2, j2);// what part of bitmap drawn
        canvas.drawBitmap(image, src, dst, null);
    }
}
