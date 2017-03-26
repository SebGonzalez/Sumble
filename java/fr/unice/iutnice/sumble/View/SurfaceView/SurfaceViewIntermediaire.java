package fr.unice.iutnice.sumble.View.SurfaceView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fr.unice.iutnice.sumble.Controller.BulleFactory;
import fr.unice.iutnice.sumble.Controller.ConversionDpPixel;
import fr.unice.iutnice.sumble.Controller.iNiveau;
import fr.unice.iutnice.sumble.Controller.iSon;
import fr.unice.iutnice.sumble.Model.Bulle;
import fr.unice.iutnice.sumble.Model.Score;
import fr.unice.iutnice.sumble.Model.TypeDifficulte;
import fr.unice.iutnice.sumble.R;
import fr.unice.iutnice.sumble.View.FinActivity;
import fr.unice.iutnice.sumble.View.GameActivity;

/**
 * Created by gonzo on 07/03/2017.
 */

public class SurfaceViewIntermediaire extends SurfaceView implements SurfaceHolder.Callback, iNiveau, iSon {

    private final int GENERATION_BULLE = 350;
    private final int VITESSE_DEPLACEMENT = 1;

    // Le holder
    SurfaceHolder mSurfaceHolder;
    // Le thread dans lequel le dessin se fera
    DrawingThread mThread;
    DisplayMetrics metrics;
    private GameActivity context;

    String mode;
    TypeDifficulte difficulte;
    BulleFactory bulleFactory;
    Bitmap backgroundResize;
    Bitmap bulle;

    private String id;

    private float score = 0F;

    private ArrayList<Integer> compteurValeurBulle;
    private ArrayList<Integer> valeurAAtteindre;
    private ArrayList<Integer[]> couleur = new ArrayList<>();
    private ArrayList<Bulle> bulleTouche;
    private ArrayList<Integer> nombreCoup;
    private ArrayList<Integer> nombreBulle;
    private int index = 0;

    private MediaPlayer mPlayer = null;
    private MediaPlayer mPlayerFond = null;

    private boolean fin = false;

    public SurfaceViewIntermediaire(GameActivity context, String mode, String id) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        this.context = context;

        mThread = new DrawingThread();
        metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        bulleFactory = new BulleFactory(context, metrics);
        this.mode = mode;
        this.difficulte = TypeDifficulte.Moyen;
        compteurValeurBulle = new ArrayList<>();
        valeurAAtteindre = new ArrayList<>();
        bulleTouche = new ArrayList<>();
        if(mode.equals("Challenge")) {
            nombreCoup = new ArrayList<>();
            nombreBulle = new ArrayList<>();
        }

        for(int i=0; i<4; i++) {
            valeurAAtteindre.add(definirValeurAAtteindre());
            compteurValeurBulle.add(0);
            Random r = new Random();
            Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
            couleur.add(tabCouleur);
            if(mode.equals("Challenge")) {
                nombreCoup.add(definirNombreCoup());
                nombreBulle.add(0);
            }
        }
        this.id = id;

    }

    @Override
    protected void onDraw(Canvas canvas) {

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
        if(mode.equals("Challenge"))
            canvas.drawText(""+nombreCoup.get(0),  metrics.widthPixels-ConversionDpPixel.dpToPx(40) ,  ConversionDpPixel.dpToPx(45)- ((paint.descent() + paint.ascent()) / 2), paint);
        else
            canvas.drawText("∞",  metrics.widthPixels-ConversionDpPixel.dpToPx(40) ,  ConversionDpPixel.dpToPx(45)- ((paint.descent() + paint.ascent()) / 2), paint);
        canvas.drawText(mode,  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(15)- ((paint.descent() + paint.ascent()) / 2), paint);
        paint.setTextSize(ConversionDpPixel.dpToPx(15));
        paint.setColor(Color.WHITE);
        canvas.drawText(""+difficulte,  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(35)- ((paint.descent() + paint.ascent()) / 2), paint);

        canvas.drawText("Score : " + score,  metrics.widthPixels/2 ,  ConversionDpPixel.dpToPx(50)- ((paint.descent() + paint.ascent()) / 2), paint);



        //image de la bulle

        canvas.drawBitmap( bulle, ConversionDpPixel.dpToPx(10), ConversionDpPixel.dpToPx(5), paint);

        //valeur à atteindre
        paint.setColor(Color.BLACK);
        paint.setTextSize( ConversionDpPixel.dpToPx(50)/2);
        canvas.drawText(""+valeurAAtteindre.get(0),  ConversionDpPixel.dpToPx(30),  ConversionDpPixel.dpToPx(27)- ((paint.descent() + paint.ascent()) / 2), paint);


        for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
            canvas.drawBitmap(bulleFactory.getListeBulle().get(i).getImg(), bulleFactory.getListeBulle().get(i).getX(), bulleFactory.getListeBulle().get(i).getY(), paint);
            paint.setTextSize(bulleFactory.getListeBulle().get(i).getLargeur() / 2);
            paint.setColor(Color.rgb(couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[0], couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[1], couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[2]));

            canvas.drawText("" + bulleFactory.getListeBulle().get(i).getValeur(), bulleFactory.getListeBulle().get(i).getX() + (bulleFactory.getListeBulle().get(i).getLargeur() / 2), (bulleFactory.getListeBulle().get(i).getY() + bulleFactory.getListeBulle().get(i).getLargeur() / 2) - (paint.descent() + paint.ascent() / 2), paint);
        }
    }

    public void genererBulle() {

        Random random = new Random();
        int y = random.nextInt(5000);
        if (y < GENERATION_BULLE) {
            Random rand = new Random();
            int largeur = rand.nextInt(ConversionDpPixel.dpToPx(40)) + ConversionDpPixel.dpToPx(40);
            int x = rand.nextInt(metrics.widthPixels - largeur);
            //Log.v("gen ", ""+bulleFactory.verifPossibiliteGen(x, metrics));
            if (bulleFactory.verifPossibiliteGen(largeur, metrics)) {
                if(bulleFactory.verifLigne(x, largeur)) {
                    Bulle bulle = new Bulle(this.getContext(), metrics, largeur);
                    bulle.setX(x);

                    if (bulleFactory.getListeBulle().size() > 0) {
                        if (x > (bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getX() + bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getLargeur()) || x < (bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getX() - bulleFactory.getListeBulle().get(bulleFactory.getListeBulle().size() - 1).getLargeur())) {
                            int test[] = definirValeurBulle(true);
                            bulle.setValeur(test[0]);
                            bulle.setCouleur(test[1]);
                            bulleFactory.getListeBulle().add(bulle);
                        } else {
                            return;
                        }
                    } else {
                        int test[] = definirValeurBulle(true);
                        bulle.setValeur(test[0]);
                        bulle.setCouleur(test[1]);
                        bulleFactory.getListeBulle().add(bulle);
                    }
                }
            } else {
                fin = true;
            }
        }
        else
            return;
    }

    public int[] definirValeurBulle(boolean aleatoire) {

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

        if(compteurValeurBulle.get(randPos) != valeurAAtteindre.get(randPos)) {
            Integer max = 0;

            if(mode.equals("Challenge")) {
                max = (valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos)) - (nombreCoup.get(randPos) - nombreBulle.get(randPos)-1);
                Log.v("max", ""+max);
                Log.v("diff", ""+(nombreCoup.get(randPos) - nombreBulle.get(randPos)));
                Log.v("diff2", ""+ (valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos)));
            }
            else
                max = (valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos));

            Integer randValeur = r.nextInt(max) + 1;

            if(mode.equals("Challenge") && nombreBulle.get(randPos) == nombreCoup.get(randPos)-1) {
                randValeur = valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos);
                compteurValeurBulle.set(randPos, valeurAAtteindre.get(randPos));
                nombreBulle.set(randPos, nombreCoup.get(randPos));
            }
            else
                compteurValeurBulle.set(randPos, compteurValeurBulle.get(randPos) + randValeur);

            if(mode.equals("Challenge"))
                nombreBulle.set(randPos, nombreBulle.get(randPos)+1);

            if (compteurValeurBulle.get(randPos) == valeurAAtteindre.get(randPos)) {
                compteurValeurBulle.add(0);
                valeurAAtteindre.add(definirValeurAAtteindre());
                Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
                couleur.add(tabCouleur);
                if(mode.equals("Challenge")) {
                    nombreCoup.add(definirNombreCoup());
                    nombreBulle.add(0);
                }

                if(randPos == index)
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

    public int definirNombreCoup() {
        Random r = new Random();
        int nbCoup = r.nextInt(5 - 2) + 2;

        if (nbCoup < valeurAAtteindre.get(valeurAAtteindre.size() - 1)) {
            if (nombreCoup.size() > 1) {
                if (nbCoup != nombreCoup.get(nombreCoup.size() - 1))
                    return nbCoup;
                else
                    return definirNombreCoup();
            } else
                return nbCoup;
        }
        else
            return  r.nextInt(valeurAAtteindre.get(valeurAAtteindre.size() - 1))+1;
    }

    public void etat() {
        ArrayList<Boolean> liste = new ArrayList<>();
        for(int i=0; i<valeurAAtteindre.size(); i++) {
            if(valeurAAtteindre.get(i) == compteurValeurBulle.get(i))
                liste.add(new Boolean(true));
            else
                liste.add(new Boolean(false));
        }
    }

    public int definirValeurAAtteindre() {
        Random r = new Random();
        int valeur = r.nextInt(20)+1;
        if(!valeurAAtteindre.isEmpty()) {
            if (valeur != valeurAAtteindre.get(valeurAAtteindre.size() - 1)) {
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

                            bulleFactory.resetBloque();

                        Bulle b = bulleFactory.getListeBulle().get(i).clone();
                        bulleTouche.add(b);
                        //on supprime la bulle
                        bulleFactory.getListeBulle().remove(i);
                        playSound(R.raw.eclatement);
                        verifPoint();

                        break;
                    }
                }
        }

        return true;  // On retourne "true" pour indiquer qu'on a géré l'évènement
    }

    public void verifPoint() {
        int sommeBulleTouche = sommeBulleTouche();
        if(sommeBulleTouche > valeurAAtteindre.get(0)) {
            playSound(R.raw.erreur);
            score -= modifScore();
            supprimerBulleOutDated();
            bulleTouche.clear();
            if(score < -50)
                fin = true;
        }
        else if(sommeBulleTouche() == valeurAAtteindre.get(0)) {
            if(mode.equals("Challenge")) {
                if(bulleTouche.size() == nombreBulle.get(0) && verifBulleToucheMemeCouleur()) {
                    playSound(R.raw.oui);
                    score += modifScore();
                    supprimerBulleOutDated();
                    bulleTouche.clear();
                }
                else {
                    playSound(R.raw.erreur);
                    score -= modifScore();
                    supprimerBulleOutDated();
                    bulleTouche.clear();
                    if(score < -50)
                        fin = true;
                }
            }
            else if (verifBulleToucheMemeCouleur()){
                playSound(R.raw.oui);
                score += modifScore();
                supprimerBulleOutDated();
                bulleTouche.clear();
            }
            else {
                playSound(R.raw.erreur);
                score -= modifScore();
                supprimerBulleOutDated();
                bulleTouche.clear();
                if(score < -50)
                    fin = true;
            }
        }
    }

    public int sommeBulleTouche() {
        int compteur = 0;

        for(int i=0; i<bulleTouche.size(); i++) {
            compteur+= bulleTouche.get(i).getValeur();
        }
        return compteur;
    }

    public int modifScore() {
        int compteur = bulleTouche.get(0).getValeur();

        for(int i=1; i<bulleTouche.size(); i++) {
            compteur+= bulleTouche.get(i).getValeur();
        }

        return compteur;
    }

    public boolean verifBulleToucheMemeCouleur() {
        for(int i=1; i<bulleTouche.size(); i++) {
            if( bulleTouche.get(i).getCouleur() != bulleTouche.get(i-1).getCouleur())
                return false;
        }
        return true;
    }

    public void supprimerBulleOutDated() {
        //On trie la liste d'index décroissante
        Collections.sort(bulleTouche, Collections.reverseOrder());

        int e=0;
        //pour chaque bulle on supprime dans la liste à l'écran celle qui correspondent à la couleur de celle touché
        for(int i=0; i<bulleTouche.size(); i++) {
            while(e<bulleFactory.getListeBulle().size()) {
                if(bulleTouche.get(i).getCouleur() == bulleFactory.getListeBulle().get(e).getCouleur()) {
                    bulleFactory.getListeBulle().remove(e);
                    e=0;
                }
                else
                    e++;
            }
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

        if(bulleTouche.get(0).getCouleur() != 0) {
            if(valeurAAtteindre.get(bulleTouche.get(0).getCouleur()) != compteurValeurBulle.get(bulleTouche.get(0).getCouleur())) {
                compteurValeurBulle.add(0);
                valeurAAtteindre.add(definirValeurAAtteindre());
                if(mode.equals("Challenge")) {
                    nombreCoup.add(definirNombreCoup());
                    nombreBulle.add(0);
                }
                Random r = new Random();
                Integer tabCouleur[] = {(r.nextInt(255) + 1), (r.nextInt(255) + 1), (r.nextInt(255) + 1)};
                couleur.add(tabCouleur);
            }

            valeurAAtteindre.remove(bulleTouche.get(0).getCouleur());
            compteurValeurBulle.remove(bulleTouche.get(0).getCouleur());
            couleur.remove(bulleTouche.get(0).getCouleur());
            if(mode.equals("Challenge")) {
                nombreCoup.remove(bulleTouche.get(0).getCouleur());
                nombreBulle.remove(bulleTouche.get(0).getCouleur());
            }

            for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
                if (bulleFactory.getListeBulle().get(y).getCouleur() > bulleTouche.get(0).getCouleur()) {
                    bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
                }
            }

            if(bulleTouche.get(0).getCouleur() < index)
                index--;
        }
        if(bulleTouche.size()>1) {
            for (int i = 1; i < bulleTouche.size(); i++) {

                if (bulleTouche.get(i).getCouleur() != bulleTouche.get(i - 1).getCouleur() && bulleTouche.get(i).getCouleur() != 0) {
                    if(valeurAAtteindre.get(bulleTouche.get(i).getCouleur()) != compteurValeurBulle.get(bulleTouche.get(i).getCouleur())) {
                        compteurValeurBulle.add(0);
                        valeurAAtteindre.add(definirValeurAAtteindre());
                        if(mode.equals("Challenge")) {
                            nombreCoup.add(definirNombreCoup());
                            nombreBulle.add(0);
                        }
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
                    if(mode.equals("Challenge")) {
                        nombreCoup.remove(bulleTouche.get(i).getCouleur());
                        nombreBulle.remove(bulleTouche.get(i).getCouleur());
                    }

                    for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
                        if (bulleFactory.getListeBulle().get(y).getCouleur() > bulleTouche.get(i).getCouleur()) {
                            bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
                        }
                    }
                }

            }
        }

        if(valeurAAtteindre.get(0) != compteurValeurBulle.get(0)) {
            compteurValeurBulle.add(0);
            valeurAAtteindre.add(definirValeurAAtteindre());
            if(mode.equals("Challenge")) {
                nombreCoup.add(definirNombreCoup());
                nombreBulle.add(0);
            }
            Random r = new Random();
            Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
            couleur.add(tabCouleur);
        }
        valeurAAtteindre.remove(0);
        compteurValeurBulle.remove(0);
        couleur.remove(0);
        if(mode.equals("Challenge")) {
            nombreCoup.remove(bulleTouche.get(0).getCouleur());
            nombreBulle.remove(bulleTouche.get(0).getCouleur());
        }
        /*compteurValeurBulle.add(0);
        valeurAAtteindre.add(definirValeurAAtteindre());
        Random r = new Random();
        Integer tabCouleur[] = {(r.nextInt(255)+1), (r.nextInt(255)+1), (r.nextInt(255)+1)};
        couleur.add(tabCouleur);*/
        if(index !=0)
            index--;

        for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
            bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
        }

    }

    public void update() throws Exception {
        genererBulle();

        for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
            bulleFactory.getListeBulle().get(i).deplacementY(VITESSE_DEPLACEMENT);
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
        final Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundw);
        backgroundResize = Bitmap.createScaledBitmap(background, metrics.widthPixels, metrics.heightPixels, true);
        background.recycle();

        final Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.bulle);
        bulle = Bitmap.createScaledBitmap(bitmap, ConversionDpPixel.dpToPx(60),ConversionDpPixel.dpToPx(60), false);
        bitmap.recycle();

        //création du thread permettant l'actualisation de l'affichage
        mThread.keepDrawing = true;
        mThread.start();

        playSoundLoop(R.raw.ambiance);
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

    public void playSound(int resId) {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(this.getContext(), resId);
        mPlayer.start();
    }

    public void playSoundLoop(int resId) {
        if(mPlayerFond != null) {
            mPlayerFond.stop();
            mPlayerFond.release();
        }
        mPlayerFond = MediaPlayer.create(this.getContext(), resId);
        mPlayerFond.setLooping(true);
        mPlayerFond.start();
    }

    public void stopSound() {
        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        if(mPlayerFond != null && mPlayerFond.isPlaying()) {
            mPlayerFond.stop();
            mPlayerFond.release();
        }
    }

    public void verifFin() {
        if(fin) {
            mThread.keepDrawing = false;

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(R.string.perdu);
                    builder.setMessage(R.string.continuer);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, FinActivity.class);
                            intent.putExtra("score", score(score));
                            intent.putExtra("id", id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            context.finish();
                        }
                    });
                    builder.show();
                }
            });
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
                            mSurfaceHolder.notify();
                        }
                        synchronized (mSurfaceHolder) {
                            // Et on dessine
                            onDraw(canvas);
                            mSurfaceHolder.notify();
                        }
                        verifFin();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

