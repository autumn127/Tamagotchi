package eecs40.tamagotchi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics. Color;
import android.graphics. Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
*Created by autumnlee1 on 5/24/16.
 */
public class TamaView extends SurfaceView
implements SurfaceHolder.Callback {
    public TamaView(Context context) {
        super(context);
        // Notify the SurfaceHolder that you'd like to receive
        // SurfaceHolder callbacks.
        getHolder().addCallback(this);
        setFocusable(true);
        //Initialize game state variables. DON'T RENDER THE GAME YET.
    }
    TamaThread tt;
    static Player p;
    Letter l;
    Rect bmp = new Rect();
    Bitmap EmptyHeart = BitmapFactory.decodeResource(getResources(), R.drawable.emptyheart);
    Bitmap HeartIcon = BitmapFactory.decodeResource(getResources(),R.drawable.heart);
    Bitmap WalkIcon = BitmapFactory.decodeResource(getResources(),R.drawable.footprint);
    Bitmap Grass = BitmapFactory.decodeResource(getResources(), R.mipmap.background);
    Bitmap FoodIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.sunflowerseed);
    Bitmap Hamster = BitmapFactory.decodeResource(getResources(), R.drawable.hamtaro_sprites);
    Bitmap StampIcon = BitmapFactory.decodeResource(getResources(), R.drawable.hamstamp);
    Bitmap Paper = BitmapFactory.decodeResource(getResources(), R.drawable.hamletter);
    Bitmap HealthBar = BitmapFactory.decodeResource(getResources(), R.drawable.healthbar);
    Paint health = new Paint();
    long prev, prev2, now, delta;
    static long offTime;
    static final int fullFitness = 10000;     //steps to fill fitness bar completely
    final int oneHr = 3600000;        //milliseconds in an hour


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        // Construct game initial state
        // Launch animator thread
        tt = new TamaThread(this);
        p = new Player(getWidth(), getHeight(), Hamster); // move later...
        l = new Letter(getWidth(), getHeight());
        MainActivity.loadData(MainActivity.context);
        now = System.currentTimeMillis();
        delta = now - offTime;
        while (delta >= oneHr){
            p.hearts--;
            p.steps-=fullFitness/10;
            delta -= oneHr;
        }
        if (p.hearts < 0){
            p.hearts = 0;
        }
        if (p.steps < 0){
            p.steps = 0;
        }
        prev2 = System.currentTimeMillis();
        if(delta > 0){
            prev2 -= delta;
        }
        tt.start();
    }

    public void draw(Canvas c){
        bmp.set(0, 0, getWidth(), getHeight());
        c.drawBitmap(Grass, null, bmp, null);
        Paint w = new Paint();
        w.setColor(Color.WHITE);
        w.setAlpha(180);
        c.drawRect(0, getHeight()*82/100, getWidth(), getHeight(), w);
        for (int i = 0; i < 10; i++){
            bmp.set(getWidth()/12*i, 0, getWidth()/12*(i+1), getHeight()/20);
            c.drawBitmap(EmptyHeart, null, bmp, null);
        }
        if (p.hearts < 0){
            p.hearts = 0;
        }
        for (int i = 0; i < p.hearts; i++){
            bmp.set(getWidth()/12*i, 0, getWidth()/12*(i+1), getHeight()/20);
            c.drawBitmap(HeartIcon, null, bmp, null);
        }
        //drawing fitness bar
        if (p.steps < 0){
            p.steps = 0;
        }
        else if (p.steps > fullFitness){
            p.steps = fullFitness;
        }
        health.setColor(Color.BLACK);
        c.drawRect(getWidth()/30, getHeight()/20, getWidth()*5/6, getHeight()/10, health);
        if (p.steps < fullFitness/4){
            health.setColor(Color.RED);
        }
        else if (p.steps < fullFitness/2){
            health.setColor(Color.YELLOW);
        }
        else if (p.steps <= fullFitness) {
            health.setColor(Color.GREEN);
        }
        c.drawRect(0, getHeight()/20, (p.steps*(getWidth()*5/6))/fullFitness, getHeight()/10, health);
        bmp.set(0, getHeight()/20, getWidth()*5/6, getHeight()/10);
        c.drawBitmap(HealthBar, null, bmp, null);
        bmp.set(0, getHeight()*85/ 100, getWidth()*3/10, getHeight());
        c.drawBitmap(FoodIcon, null, bmp, null);
        bmp.set(getWidth()/2 - getWidth()*12/100, getHeight()*83/100, getWidth()/2 + getWidth()*12/100, getHeight()*99/100);
        c.drawBitmap(WalkIcon, null, bmp, null);
        bmp.set(getWidth()/2 + getWidth()*18/100, getHeight()*85/100, getWidth()*97/100, getHeight()*99/100);
        c.drawBitmap(StampIcon, null, bmp, null);
        now = System.currentTimeMillis();
        p.autoAction();
        p.draw(c);
        if(l.letter) {
            bmp.set(getWidth() * 3 / 100, getHeight() / 25, getWidth() * 97 / 100, getHeight() * 80 / 100);
            c.drawBitmap(Paper, null, bmp, null);
            l.draw(c);
        }
        if(now - prev >= 200) {
            p.update();
            prev = now;
        }
        if(now - prev2 >= oneHr) {
            p.hearts--;
            p.steps -= fullFitness/10;
            prev2 = now;
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
        // Respond to surface changes, e.g., aspect ratio changes.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        // The cleanest way to stop a thread is by interrupting it.
        // TamaThread regularly checks its interrupt flag.
        offTime = prev2;
        MainActivity.saveData(MainActivity.context);
        tt.interrupt();
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = 0;
        float y = 0;
        float mx = 0;
        float my = 0;
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = e.getX();
                y = e.getY();
                p.action(x, y);
                l.mail(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                mx = e.getX();
                my = e.getY();
                break;
        }
        if(l.letter) {
            l.write(mx, my);
        }
        return true;
    }

}
