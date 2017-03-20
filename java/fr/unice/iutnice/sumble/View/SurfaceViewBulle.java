package fr.unice.iutnice.sumble.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import fr.unice.iutnice.sumble.Controller.BulleFactory;
import fr.unice.iutnice.sumble.Controller.ConversionDpPixel;
import fr.unice.iutnice.sumble.Model.Bulle;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.R;

import static android.R.attr.max;
import static android.R.attr.y;
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

    private float score = 0F;

    private ArrayList<Integer> compteurValeurBulle;
    private ArrayList<Integer> valeurAAtteindre;
    private ArrayList<Integer[]> couleur = new ArrayList<>();
    private ArrayList<Bulle> bulleTouche;
    private int index = 0;

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
        compteurValeurBulle = new ArrayList<>();
        valeurAAtteindre = new ArrayList<>();
        bulleTouche = new ArrayList<>();

        for(int i=0; i<3; i++) {
            valeurAAtteindre.add(definirValeurAAtteindre());
            compteurValeurBulle.add(0);
            Random r = new Random();
            Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
            couleur.add(tabCouleur);
        }

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
        canvas.drawText(mode,  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(15)- ((paint.descent() + paint.ascent()) / 2), paint);
        paint.setTextSize(ConversionDpPixel.dpToPx(15));
        paint.setColor(Color.WHITE);
        canvas.drawText(""+difficulte,  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(35)- ((paint.descent() + paint.ascent()) / 2), paint);

        canvas.drawText("Score : " + score,  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(50)- ((paint.descent() + paint.ascent()) / 2), paint);



        //image de la bulle
        Drawable dr = ContextCompat.getDrawable(context, bulle);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
        canvas.drawBitmap( new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, ConversionDpPixel.dpToPx(50),ConversionDpPixel.dpToPx(50), true)).getBitmap(), ConversionDpPixel.dpToPx(10), ConversionDpPixel.dpToPx(5), paint);

        //valeur à atteindre
        paint.setColor(Color.BLACK);
        paint.setTextSize( ConversionDpPixel.dpToPx(50)/2);
        canvas.drawText(""+valeurAAtteindre.get(0),  ConversionDpPixel.dpToPx(30),  ConversionDpPixel.dpToPx(27)- ((paint.descent() + paint.ascent()) / 2), paint);


        for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
            canvas.drawBitmap(bulleFactory.getListeBulle().get(i).getImg().getBitmap(), bulleFactory.getListeBulle().get(i).getX(), bulleFactory.getListeBulle().get(i).getY(), paint);
            paint.setTextSize(bulleFactory.getListeBulle().get(i).getLargeur() / 2);
            //Log.v("size : " + bulleFactory.getListeBulle().size() + " index : " + i, "sizeCouleur : " + couleur.size() + " index : " + bulleFactory.getListeBulle().get(i).getCouleur()  + " sizeV : " + valeurAAtteindre.size() + " index : " + index);
            paint.setColor(Color.rgb(couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[0], couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[1], couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[2]));

            canvas.drawText("" + bulleFactory.getListeBulle().get(i).getValeur() + "/" + valeurAAtteindre.get(bulleFactory.getListeBulle().get(i).getCouleur()), bulleFactory.getListeBulle().get(i).getX() + (bulleFactory.getListeBulle().get(i).getLargeur() / 2), (bulleFactory.getListeBulle().get(i).getY() + bulleFactory.getListeBulle().get(i).getLargeur() / 2) - (paint.descent() + paint.ascent() / 2), paint);
        }
    }

    public synchronized void genererBulle() {

        Bulle bulle = new Bulle(this.getContext(), metrics);

        Random random = new Random();
        int y = random.nextInt(5000);
        if (y < 100) {
            Random rand = new Random();

            int x = rand.nextInt(metrics.widthPixels - bulle.getLargeur());
            bulle.setX(x);

            if (bulleFactory.getListeBulle().size() > 0) {
                if (x > (bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getX() + bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getLargeur()) || x < (bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getX() - bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getLargeur())) {
                    int test[] = definirValeurBulle(true);
                    bulle.setValeur(test[0]);
                    bulle.setCouleur(test[1]);
                    bulleFactory.getListeBulle().add(bulle);
                }
                else {
                    return;
                }
            } else {
                int test[] = definirValeurBulle(true);
                bulle.setValeur(test[0]);
                bulle.setCouleur(test[1]);
                bulleFactory.getListeBulle().add(bulle);
            }
        }
        else {
            return;
        }

    }

    public synchronized int[] definirValeurBulle(boolean aleatoire) {

        Random r = new Random();
        Integer randPos;

        if(aleatoire) {
            if (valeurAAtteindre.size() > 1)
                randPos = new Integer(r.nextInt(valeurAAtteindre.size() - 1));
            else
                randPos = 0;
        }
        else
            randPos = index;

       // Log.v("rand : "+randPos + " index : " + index, "atteindre : " + valeurAAtteindre.get(randPos) + " somme : " + compteurValeurBulle.get(randPos));
        if(compteurValeurBulle.get(randPos) != valeurAAtteindre.get(randPos)) {
            Integer max = valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos);
            Integer randValeur = r.nextInt(max) + 1;
            compteurValeurBulle.set(randPos, compteurValeurBulle.get(randPos) + randValeur);

            if (compteurValeurBulle.get(randPos) == valeurAAtteindre.get(randPos) && randPos == index) {
                compteurValeurBulle.add(0);
                valeurAAtteindre.add(definirValeurAAtteindre());
                Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
                couleur.add(tabCouleur);
                index++;
            }
            int retour[] = {randValeur, randPos};
            return retour;
        }
       else if(compteurValeurBulle.get(randPos) == valeurAAtteindre.get(randPos) && randPos == index) {
            index++;
            return definirValeurBulle(true);
        }
        else
            return definirValeurBulle(false);
    }

    public synchronized void etat() {
        ArrayList<Boolean> liste = new ArrayList<>();
        for(int i=0; i<valeurAAtteindre.size(); i++) {
            if(valeurAAtteindre.get(i) == compteurValeurBulle.get(i))
                liste.add(new Boolean(true));
            else
                liste.add(new Boolean(false));
        }
        Log.v("Liste",""+liste);
        Log.v("Index " , ""+index);
    }

    public synchronized int definirValeurAAtteindre() {
        Random r = new Random();
        int valeur = r.nextInt(10)+1;
        if(!valeurAAtteindre.isEmpty()) {
            if (valeur != valeurAAtteindre.get(valeurAAtteindre.size() - 1)) {
                Log.v("Nouvelle valeur : ", "" + valeurAAtteindre);
                return valeur;
            }
            else return definirValeurAAtteindre();
        }
        else
            return valeur;
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

                        Bulle b = bulleFactory.getListeBulle().get(i).clone();
                        bulleTouche.add(b);
                        //on supprime la bulle
                        bulleFactory.getListeBulle().remove(i);
                        verifPoint();

                        break;
                    }
                }
        }

        return true;  // On retourne "true" pour indiquer qu'on a géré l'évènement
    }

    public synchronized void verifPoint() {
        int sommeBulleTouche = sommeBulleTouche();
        Log.v(""+sommeBulleTouche, ""+valeurAAtteindre.get(0));
        if(sommeBulleTouche > valeurAAtteindre.get(0)) {
            score += modifScore();
            supprimerBulleOutDated();
            bulleTouche.clear();
            /*compteurValeurBulle.remove(0);
            valeurAAtteindre.remove(0);
            couleur.remove(0);*/
           /* for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
                if(bulleFactory.getListeBulle().get(i).getCouleur() == 0) {
                    bulleFactory.getListeBulle().remove(i);
                }
                else
                    bulleFactory.getListeBulle().get(i).setCouleur(bulleFactory.getListeBulle().get(i).getCouleur()-1);
            }
            if(index>0)
                index--;*/
            //definirValeurAAtteindre();
        }
        else if(sommeBulleTouche() == valeurAAtteindre.get(0)) {
            score += modifScore();
            supprimerBulleOutDated();
            bulleTouche.clear();
            /*compteurValeurBulle.remove(0);
            valeurAAtteindre.remove(0);
            couleur.remove(0);*/
            /*for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
                if(bulleFactory.getListeBulle().get(i).getCouleur() == 0) {
                    bulleFactory.getListeBulle().remove(i);
                }
                else
                    bulleFactory.getListeBulle().get(i).setCouleur(bulleFactory.getListeBulle().get(i).getCouleur()-1);
            }*/
            /*if(index>0)
                index--;*/
            //definirValeurAAtteindre();
        }
    }

    public synchronized int sommeBulleTouche() {
        int compteur = 0;

        for(int i=0; i<bulleTouche.size(); i++) {
            compteur+= bulleTouche.get(i).getValeur();
        }
        return compteur;
    }

    public int modifScore() {
        int compteur = bulleTouche.get(0).getValeur();
        boolean memeType = true;

        for(int i=1; i<bulleTouche.size(); i++) {
            compteur+= bulleTouche.get(i).getValeur();
            if( bulleTouche.get(i).getCouleur() != bulleTouche.get(i-1).getCouleur())
                memeType = false;
        }

        if(memeType)
            return compteur;
        else {
            return -compteur;
        }
    }

    public synchronized void supprimerBulleOutDated() {
        //On trie la liste d'index décroissante
        Collections.sort(bulleTouche, Collections.reverseOrder());
        Log.v("Liste trie", ""+bulleTouche);

        int e=0;
        //pour chaque bulle on supprime dans la liste à l'écran celle qui correspondent à la couleur de celle touché
        for(int i=0; i<bulleTouche.size(); i++) {
            Log.v("BIATCH", "BIATCH");
            while(e<bulleFactory.getListeBulle().size()) {
                if(bulleTouche.get(i).getCouleur() == bulleFactory.getListeBulle().get(e).getCouleur()) {
                    bulleFactory.getListeBulle().remove(e);
                    e=0;
                }
                else
                    e++;
            }
        }

        for(int z=0; z<bulleFactory.getListeBulle().size(); z++) {

            Log.v("OUIIIIIII : " + z, "" + bulleFactory.getListeBulle().get(z).getCouleur());
        }

        int z=0;
        while(z<bulleFactory.getListeBulle().size()) {
            if(bulleFactory.getListeBulle().get(z).getCouleur() == 0) {
                bulleFactory.getListeBulle().remove(z);
                z=0;
            }
            else
                z++;
        }
        for(int a=0; a<bulleFactory.getListeBulle().size(); a++) {

            Log.v("NOOOOOOOOON : " + a, "" + bulleFactory.getListeBulle().get(a).getCouleur());
        }

        if(bulleTouche.get(0).getCouleur() != 0) {
            if(valeurAAtteindre.get(bulleTouche.get(0).getCouleur()) != compteurValeurBulle.get(bulleTouche.get(0).getCouleur())) {
                compteurValeurBulle.add(0);
                valeurAAtteindre.add(definirValeurAAtteindre());
                Random r = new Random();
                Integer tabCouleur[] = {(r.nextInt(255) + 1), (r.nextInt(255) + 1), (r.nextInt(255) + 1)};
                couleur.add(tabCouleur);
            }

            valeurAAtteindre.remove(bulleTouche.get(0).getCouleur());
            compteurValeurBulle.remove(bulleTouche.get(0).getCouleur());
            couleur.remove(bulleTouche.get(0).getCouleur());

            for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
                Log.v("couleur Couleur : " + y, "" + bulleFactory.getListeBulle().get(y).getCouleur());
                if (bulleFactory.getListeBulle().get(y).getCouleur() > bulleTouche.get(0).getCouleur()) {
                    bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
                }
                Log.v("COULEUR COULEUR : " + y, "" + bulleFactory.getListeBulle().get(y).getCouleur());
                // Log.v("Couleur : " + y, "" + bulleFactory.getListeBulle().get(y).getCouleur());
            }

            if(bulleTouche.get(0).getCouleur() < index)
                index--;
        }
        if(bulleTouche.size()>1) {
            for (int i = 1; i < bulleTouche.size(); i++) {

                if (bulleTouche.get(i).getCouleur() != bulleTouche.get(i - 1).getCouleur() && bulleTouche.get(i).getCouleur() != 0) {
                    //Log.v("oui","curinga");
                    if(valeurAAtteindre.get(bulleTouche.get(i).getCouleur()) != compteurValeurBulle.get(bulleTouche.get(i).getCouleur())) {
                        compteurValeurBulle.add(0);
                        valeurAAtteindre.add(definirValeurAAtteindre());
                        Random r = new Random();
                        Integer tabCouleur[] = {(r.nextInt(255) + 1), (r.nextInt(255) + 1), (r.nextInt(255) + 1)};
                        couleur.add(tabCouleur);
                     }
                    if (bulleTouche.get(i).getCouleur() < index) {
                        index--;
                    }
                    valeurAAtteindre.remove(bulleTouche.get(i).getCouleur());
                    compteurValeurBulle.remove(bulleTouche.get(i).getCouleur());
                    couleur.remove(bulleTouche.get(i).getCouleur());

                    for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
                        Log.v("après Couleur : " + y, "" + bulleFactory.getListeBulle().get(y).getCouleur());
                        if (bulleFactory.getListeBulle().get(y).getCouleur() > bulleTouche.get(i).getCouleur()) {
                            bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
                        }
                        Log.v("avant Couleur : " + y, "" + bulleFactory.getListeBulle().get(y).getCouleur());
                    }
                }

            }
        }

        if(valeurAAtteindre.get(0) != compteurValeurBulle.get(0)) {
            compteurValeurBulle.add(0);
            valeurAAtteindre.add(definirValeurAAtteindre());
            Random r = new Random();
            Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
            couleur.add(tabCouleur);
        }
        valeurAAtteindre.remove(0);
        compteurValeurBulle.remove(0);
        couleur.remove(0);
        /*compteurValeurBulle.add(0);
        valeurAAtteindre.add(definirValeurAAtteindre());
        Random r = new Random();
        Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
        couleur.add(tabCouleur);*/
        if(index !=0)
            index--;

        for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
            Log.v("Couleur avant : " + y, "" + bulleFactory.getListeBulle().get(y).getCouleur());
            bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
            Log.v("Couleur après : " + y, "" + bulleFactory.getListeBulle().get(y).getCouleur());
        }

        etat();
    }

    public synchronized void update() {
        /*if(bulleFactory.getListeBulle().size() > 3) {

            Intent intent = new Intent(context, FinActivity.class);
            intent.putExtra("score", score(78f));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }*/
        genererBulle();

        for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
            try {
                bulleFactory.getListeBulle().get(i).deplacementY(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Que faire quand le surface change ? (L'utilisateur tourne son téléphone par exemple)
       // holder.setFixedSize(width, height);
       // holder.setFormat(format);


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
                    if (canvas != null) {
                        synchronized (mSurfaceHolder) {
                            //on déplace la bulle
                            update();
                            // Et on dessine
                            onDraw(canvas);
                        }
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

    public Score score(float value){
        return new Score(value, difficulte, mode);
    }
}

