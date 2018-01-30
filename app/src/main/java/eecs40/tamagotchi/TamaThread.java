package eecs40.tamagotchi;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class TamaThread extends Thread{
    TamaView tv;
    public TamaThread(TamaView sv) {
        this.tv=sv;
    }
    public void run() {
        SurfaceHolder sh = tv.getHolder();
        // Main game loop.
        while( !Thread.interrupted() ) {
            //You might want to do game specific processing in a method you call here
            Canvas c = sh.lockCanvas(null);
            try {
                synchronized(sh) {
                    tv.draw(c);
                }
            } catch (Exception e) {
            } finally {
                if ( c != null ) {
                    sh.unlockCanvasAndPost(c);
                } }
            // Set the frame rate by setting this delay
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // Thread was interrupted while sleeping.
                return; }
        } }
}
