package fr.unice.iutnice.sumble.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import fr.unice.iutnice.sumble.Controller.BulleFactory;
import fr.unice.iutnice.sumble.Controller.ConversionDpPixel;
import fr.unice.iutnice.sumble.Model.Bulle;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.R;

import static android.R.attr.colorBackground;
import static android.R.attr.x;
import static android.R.attr.y;
import static fr.unice.iutnice.sumble.R.color.backgroundBleuFonce;
import static fr.unice.iutnice.sumble.R.drawable.bulle;

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

    String mode;
    TypeDifficulte difficulte;
    BulleFactory bulleFactory;
    Bitmap backgroundResize;

    private boolean fin = false;

    public SurfaceViewBulle (Context context, DisplayMetrics metrics, String mode, TypeDifficulte difficulte) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        mThread = new DrawingThread();
        this.metrics = metrics;
        this.context = context;

        bulleFactory = new BulleFactory(context, metrics);
        this.mode = mode;
        this.difficulte = difficulte;

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setTextAlign(Paint.Align.CENTER);

        //affichage du background
        canvas.drawBitmap(backgroundResize, 0, 0, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.backgroundBleuFonce));
        //fond vert
        canvas.drawRect(0,0,metrics.widthPixels, ConversionDpPixel.dpToPx(60), paint);
        //carré blanc
        paint.setColor(Color.WHITE);
        canvas.drawRect(metrics.widthPixels-ConversionDpPixel.dpToPx(80),0,metrics.widthPixels, ConversionDpPixel.dpToPx(30), paint);
        //carré jaune
        paint.setColor(Color.YELLOW);
        canvas.drawRect(metrics.widthPixels-ConversionDpPixel.dpToPx(80),ConversionDpPixel.dpToPx(30),metrics.widthPixels, ConversionDpPixel.dpToPx(60), paint);

        //texte
        paint.setColor(Color.BLACK);
        paint.setTextSize(ConversionDpPixel.dpToPx(25));
        canvas.drawText("Coups",  metrics.widthPixels-ConversionDpPixel.dpToPx(40) ,  ConversionDpPixel.dpToPx(15)- ((paint.descent() + paint.ascent()) / 2), paint);
        canvas.drawText("0",  metrics.widthPixels-ConversionDpPixel.dpToPx(40) ,  ConversionDpPixel.dpToPx(45)- ((paint.descent() + paint.ascent()) / 2), paint);
        canvas.drawText(mode,  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(20)- ((paint.descent() + paint.ascent()) / 2), paint);
        paint.setTextSize(ConversionDpPixel.dpToPx(15));
        paint.setColor(Color.WHITE);
        canvas.drawText(difficulte.toString(),  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(40)- ((paint.descent() + paint.ascent()) / 2), paint);

        Drawable dr = context.getResources().getDrawable(R.drawable.bulle);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        canvas.drawBitmap( new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, ConversionDpPixel.dpToPx(50),ConversionDpPixel.dpToPx(50), true)).getBitmap(), ConversionDpPixel.dpToPx(10), ConversionDpPixel.dpToPx(5), paint);



        for(Bulle bulle : bulleFactory.getListeBulle()) {
            canvas.drawBitmap(bulle.getImg().getBitmap(), bulle.getX(),  bulle.getY(), paint);
            paint.setTextSize( bulle.getLargeur()/2);
            canvas.drawText(""+ bulle.getValeur(),  bulle.getX()+(bulle.getLargeur()/2) ,  (bulle.getY()+bulle.getLargeur()/2)- (paint.descent() + paint.ascent()/2), paint);
        }
    }

    public void genererBulle() {

        Bulle bulle = new Bulle(this.getContext(), metrics);

        Random random = new Random();
        int y = random.nextInt(5000);
        if (y < 100) {
            Random rand = new Random();

            int x = rand.nextInt(metrics.widthPixels - bulle.getLargeur());
            bulle.setX(x);

            if (bulleFactory.getListeBulle().size() > 0) {
                if (x > (bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getX() + bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getLargeur()) || x < (bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getX() - bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getLargeur()))
                    bulleFactory.getListeBulle().add(bulle);
                else {
                    return;
                }
            } else {
                bulleFactory.getListeBulle().add(bulle);
            }
        }
        else {
            return;
        }

    }

    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        int currentX = (int)event.getX();
        int currentY = (int)event.getY();

        switch (event.getAction()) {

            // code exécuté lorsque le doigt touche l'écran.
            case MotionEvent.ACTION_DOWN:
                // si le doigt touche une bulle :
                for (int i = 0; i < bulleFactory.getListeBulle().size(); i++) {

                    //si les coordonnées du toucher correspondent à la position de la bulle
                    if (    currentX >= bulleFactory.getListeBulle().get(i).getX() &&
                            currentX <= bulleFactory.getListeBulle().get(i).getX() + bulleFactory.getListeBulle().get(i).getLargeur() &&
                            currentY >= bulleFactory.getListeBulle().get(i).getY() &&
                            currentY <= bulleFactory.getListeBulle().get(i).getY() + bulleFactory.getListeBulle().get(i).getLargeur()) {

                        try {
                            bulleFactory.resetBloque();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //on supprime la bulle
                        bulleFactory.getListeBulle().remove(i);

                        //score++;

                        break;
                    }
                }
        }

        return true;  // On retourne "true" pour indiquer qu'on a géré l'évènement
    }

    public synchronized void update() {
        genererBulle();

        if(bulleFactory.getListeBulle().size() > 3) {
            //Intent .....
        }

        for(Bulle bulle : bulleFactory.getListeBulle()) {
            try {
                bulle.deplacementY(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Que faire quand le surface change ? (L'utilisateur tourne son téléphone par exemple)
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundw);
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
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

