package fr.unice.iutnice.sumble.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

import fr.unice.iutnice.sumble.Controller.BulleFactory;
import fr.unice.iutnice.sumble.Controller.ConversionDpPixel;
import fr.unice.iutnice.sumble.Model.Bulle;
import fr.unice.iutnice.sumble.R;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by gonzo on 07/03/2017.
 */

public class SurfaceViewBulle extends SurfaceView implements SurfaceHolder.Callback {
    // Le holder
    SurfaceHolder mSurfaceHolder;
    // Le thread dans lequel le dessin se fera
    DrawingThread mThread;
    DisplayMetrics metrics;
    Context context;

    BulleFactory bulleFactory;
    Bitmap backgroundResize;

    public SurfaceViewBulle (Context context,  DisplayMetrics metrics) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mThread = new DrawingThread();
        this.metrics = metrics;
        this.context = context;

        bulleFactory = new BulleFactory(context, metrics);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(backgroundResize, 0, 0, null);

        Paint paint = new Paint();

        for(Bulle bulle : bulleFactory.getListeBulle()) {
            canvas.drawBitmap(bulle.getImg().getBitmap(), bulle.getX(), bulle.getY(), paint);
            paint.setTextSize(bulle.getLargeur()/2);
            canvas.drawText(""+bulle.getValeur(), bulle.getX() + bulle.getLargeur()/2-paint.getTextSize()/2, bulle.getY() + bulle.getLargeur()/2, paint);
        }
    }

    public void update() {
        for(Bulle bulle : bulleFactory.getListeBulle()) {
            bulle.deplacementY(ConversionDpPixel.dpToPx(5));
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Que faire quand le surface change ? (L'utilisateur tourne son téléphone par exemple)
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        float scale = (float)background.getHeight()/(float)getHeight();
        int newWidth = Math.round(background.getWidth()/scale);
        int newHeight = Math.round(background.getHeight()/scale);
        backgroundResize = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);

        //création du thread permettant l'actualisation de l'affichage
        mThread.keepDrawing = true;
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mThread.keepDrawing = false;

        boolean joined = false;
        while (!joined) {
            try {
                mThread.join();
                joined = true;
            } catch (InterruptedException e) {}
        }
    }

    private class DrawingThread extends Thread {
        // Utilisé pour arrêter le dessin quand il le faut
        boolean keepDrawing = true;

        @SuppressLint("WrongCall")
        @Override
        public void run() {

            while (keepDrawing) {
                Canvas canvas = null;
                try {
                    // On récupère le canvas pour dessiner dessus
                    canvas = mSurfaceHolder.lockCanvas();
                    // On s'assure qu'aucun autre thread n'accède au holder
                    synchronized (mSurfaceHolder) {
                        //on déplace la bulle
                        update();
                        // Et on dessine
                        onDraw(canvas);
                    }
                } finally {
                    // Notre dessin fini, on relâche le Canvas pour que le dessin s'affiche
                    if (canvas != null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }

                // Pour dessiner à 50 fps
               /* try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {}*/
            }
        }
    }
}

