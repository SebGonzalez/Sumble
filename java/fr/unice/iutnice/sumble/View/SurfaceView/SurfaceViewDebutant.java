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
import java.util.ListIterator;
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

public class SurfaceViewDebutant extends SurfaceView implements SurfaceHolder.Callback, iNiveau, iSon {

    /*************** PARAMETRE DU JEU **********************/
    private final int MAX_GENERATION_BULLE = 100;
    private final int GENERATION_BULLE = 20;
    private final int VITESSE_DEPLACEMENT = 5;
    private final int COUP_MIN = 2;
    private final int COUP_MAX = 5;
    private final int MAX_VALEUR = 20;
    /********************************************************/

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

    /**
     * Constructeur
     * @param context
     * @param mode
     */
    public SurfaceViewDebutant(GameActivity context, String mode) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        this.context = context;

        mThread = new DrawingThread();
        metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        this.context = context;

        bulleFactory = new BulleFactory(metrics);
        this.mode = mode;
        this.difficulte = TypeDifficulte.Facile;
        compteurValeurBulle = new ArrayList<>();
        valeurAAtteindre = new ArrayList<>();
        bulleTouche = new ArrayList<>();
        if(mode.equals("Challenge")) {
            nombreCoup = new ArrayList<>();
            nombreBulle = new ArrayList<>();
        }

        //on génère 4 valeur au début
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

    }

    /**
     * Méthode qui dessine dans la surfaceView
     * @param canvas
     */
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

        //nombre coup
        if(mode.equals("Challenge"))
            canvas.drawText(""+(nombreCoup.get(0)-bulleTouche.size()),  metrics.widthPixels-ConversionDpPixel.dpToPx(40) ,  ConversionDpPixel.dpToPx(45)- ((paint.descent() + paint.ascent()) / 2), paint);
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

        //affichage des bulles et des chiffres dedans
        for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
            canvas.drawBitmap(bulleFactory.getListeBulle().get(i).getImg(), bulleFactory.getListeBulle().get(i).getX(), bulleFactory.getListeBulle().get(i).getY(), paint);
            paint.setTextSize(bulleFactory.getListeBulle().get(i).getLargeur() / 3);
            paint.setColor(Color.rgb(couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[0], couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[1], couleur.get(bulleFactory.getListeBulle().get(i).getCouleur())[2]));

            canvas.drawText("" + bulleFactory.getListeBulle().get(i).getValeur() + "/" + valeurAAtteindre.get(bulleFactory.getListeBulle().get(i).getCouleur()), bulleFactory.getListeBulle().get(i).getX() + (bulleFactory.getListeBulle().get(i).getLargeur() / 2), (bulleFactory.getListeBulle().get(i).getY() + bulleFactory.getListeBulle().get(i).getLargeur() / 2) - (paint.descent() + paint.ascent() / 2), paint);
        }
    }

    /**
     * Méthode qui génère les bulles
     */
    public void genererBulle() {

            Random random = new Random();
            int y = random.nextInt(MAX_GENERATION_BULLE);
            //si la valeur random est inférieur à la constante
            if (y < GENERATION_BULLE) {
                //on définit une valeur random de la largeur de la bulle et de la position en x
                int largeur = random.nextInt(ConversionDpPixel.dpToPx(40)) + ConversionDpPixel.dpToPx(40);
                int x = random.nextInt(metrics.widthPixels - largeur);

                //si l'on peut générer les bulles (s'il reste de la place sur l'écran)
                if (bulleFactory.verifPossibiliteGen(largeur)) {
                    //si il y a de la place à l'endroit donné
                    if(bulleFactory.verifPossibiliteGenPosition(x, largeur)) {
                        //on créé la bulle et on lui assigne sa valeur
                        Bulle bulle = new Bulle(this.getContext(), metrics, largeur);
                        bulle.setX(x);
                        //on définit une valeur à la bulle et on lui assigne
                        int recupValeur[] = definirValeurBulle(true);
                        bulle.setValeur(recupValeur[0]);
                        bulle.setCouleur(recupValeur[1]);
                        //on ajoute la bulle à la liste
                        bulleFactory.getListeBulle().add(bulle);
                    }
                } else {
                    //sinon si on peut générer nulle part c'est que la partie est finis
                    fin = true;
                }
            }
            else
                return;
    }

    /**
     * Méthode permettant de donner une valeur lors de la création d'une bulle
     * @param aleatoire spécifie si la valeur se génère aléatoirement (au niveau de la position) ou si on génère une valeur pour la 1ère valeur à atteindre
     * @return un tableau, indice 0 la valeur de la bulle, indice 1 la position de la valeur à atteindre
     */
    public int[] definirValeurBulle(boolean aleatoire) {

        Random r = new Random();
        Integer randPos;

        //si on génère n'importe quelle bulle on définit une valeur aléatoire entre l'indice min et la taille des bulles
        if(aleatoire) {
            randPos = new Integer(r.nextInt(valeurAAtteindre.size() - 1-index)+index);
        }
        //sinon on génère sur la première valeur
        else
            randPos = index;

        //si la somme des bulles déjà apparu pour cette bulle est différente de leur valeur à atteindre
        if(compteurValeurBulle.get(randPos) != valeurAAtteindre.get(randPos)) {
            Integer max = 0;
            //si on est dans le mode challenge
            if(mode.equals("Challenge")) {
                //le max vaut la différence entre les bulles déjà apparu et la valeur à atteindre - le nombre de coup restant (pour laisser de la place aux prochaine bulles)
                max = (valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos)) - (nombreCoup.get(randPos) - nombreBulle.get(randPos)-1);
            }
            else
                //sinon le max vaut la différence entre les bulles déjà apparu et la valeur à atteindre (pas de nombre de bulles imposé dans ce mode
                max = (valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos));

            //on génère une valeur grâce à ce max
            Integer randValeur = r.nextInt(max) + 1;

            //si il manque une seule bulle on définit la valeur manuellement (valeur restante pour faire la somme)
            if(mode.equals("Challenge") && nombreBulle.get(randPos) == nombreCoup.get(randPos)-1) {
                randValeur = valeurAAtteindre.get(randPos) - compteurValeurBulle.get(randPos);
                compteurValeurBulle.set(randPos, valeurAAtteindre.get(randPos));
                nombreBulle.set(randPos, nombreCoup.get(randPos));
            }
            else
                //sinon on attribut la valeur qu'on a généré aléatoirement
                compteurValeurBulle.set(randPos, compteurValeurBulle.get(randPos) + randValeur);

            //si on est dans le mode challenge on incrémente le compteur de bulle généré
            if(mode.equals("Challenge"))
                nombreBulle.set(randPos, nombreBulle.get(randPos)+1);

            //si on arrive au bon nombre de bulle bloque la génération pour cette valeur et on définit une nouvelle valeur à atteindre
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
        //sinon si c'est égale et que c'est la première valeur où il faut générer des bulles on bloque la génération sur cette valeur et on relance la fonction en aléatoire
       else if(compteurValeurBulle.get(randPos) == valeurAAtteindre.get(randPos) && randPos == index) {
            index++;
            return definirValeurBulle(true);
        }
        else
            //on relance la fonction sur la première valeur
            return definirValeurBulle(false);
    }

    /**
     * Méthode définissant le nombre de coup à effectuer pour réaliser la valeur à atteindre (dans le cas du mode challenge)
     * @return
     */
    public int definirNombreCoup() {
        Random r = new Random();
        //valeur random entre 2 et 5
        int nbCoup = r.nextInt(COUP_MAX - COUP_MIN) + COUP_MIN;

        //on vérifie que le nombre de coup n'est pas supérieur à la valeur à atteindre (sinon forcément y'a un soucis quoi)
        if (nbCoup < valeurAAtteindre.get(valeurAAtteindre.size() - 1)) {
            //s'il y a plus d'une valeur dans la liste
            if (nombreCoup.size() > 1) {
                //on vérifie que le nombre de coup n'est pas le même que celui d'avant
                if (nbCoup != nombreCoup.get(nombreCoup.size() - 1))
                    return nbCoup;
                //sinon on redéfinit une nouvelle valeur
                else
                    return definirNombreCoup();
            } else
                return nbCoup;
        }
        //sinon le nombre de est aléatoire en 1 et la valeur à atteindre
        else
            return  r.nextInt(valeurAAtteindre.get(valeurAAtteindre.size() - 1))+1;
    }

    //fonction pour debug je vais pas commenter hey hey
    public void etat() {
        ArrayList<Boolean> liste = new ArrayList<>();
        for(int i=0; i<valeurAAtteindre.size(); i++) {
            if(valeurAAtteindre.get(i) == compteurValeurBulle.get(i))
                liste.add(new Boolean(true));
            else
                liste.add(new Boolean(false));
        }
        Log.v("liste", ""+liste);
    }

    /**
     * Méthode définissant la valeur à atteindre en faisant la somme des bulles
     * @return
     */
    public int definirValeurAAtteindre() {
        //on définit une valeur entre 1 et 20
        Random r = new Random();
        int valeur = r.nextInt(MAX_VALEUR)+1;
        //si la liste n'est pas vide
        if(!valeurAAtteindre.isEmpty()) {
            //on vérifie que la valeur généré n'est pas la même que celle généré précédemment
            if (valeur != valeurAAtteindre.get(valeurAAtteindre.size() - 1)) {
                return valeur;
            }
            //sinon on génère une nouvelle valeur
            else return definirValeurAAtteindre();
        }
        else
            return valeur;
    }

    /**
     * Fonction appelé lorsque que l'on touche l'écran
     * @param event
     * @return
     */
    @Override
    public synchronized boolean onTouchEvent(MotionEvent event) {
        //on récupère les positions de l'endroit touché
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
                            //les autres bulles ne doivent plus être bloqué
                            bulleFactory.resetBloque();

                        //on clone la bulle qu'on vient de toucher et on l'ajoute à une liste temporaire
                        Bulle b = bulleFactory.getListeBulle().get(i).clone();
                        bulleTouche.add(b);
                        //on supprime la bulle
                        bulleFactory.getListeBulle().remove(i);
                        //on joue le son qu'il faut
                        playSound(R.raw.eclatement);
                        //on appel la fonction qui gère les points
                        verifPoint();

                        //on sort de la boucle (on peut pas toucher deux bulles en même temps ça serait fou)
                        break;
                    }
                }
        }

        return true;  // On retourne "true" pour indiquer qu'on a géré l'évènement
    }

    /**
     * Fonction qui gère les points
     */
    public void verifPoint() {
        //on récupère la somme des bulles qu'on a touché
        int sommeBulleTouche = sommeBulleTouche();
        //si cette somme est supérieur à la valeur que l'on devait atteindre c'est qu'on a fait une erreur
        if(sommeBulleTouche > valeurAAtteindre.get(0)) {
            //on joue le son d'erreur
            playSound(R.raw.erreur);
            //on enlève les points
            score -= sommeBulleTouche;
            //on supprime les bulles qui ne doivent plus être là
            supprimerBulleOutDated();
            //on reset la liste des bulles touchés
            bulleTouche.clear();
            //si le score est inférieur à 50 on lance la fin de la partie (en gros c'est que t'es nul)
            if(score < -50)
                fin = true;
        }
        //sinon si la somme vaut pile poile la valeur à atteindre (tout n'est pas encore gagné)
        else if(mode.equals("Challenge")) {
            //si la somme vaut pile poile la valeur à atteindre et qu'on a touché le bon nombre de boule on a gagné
            if (sommeBulleTouche == valeurAAtteindre.get(0) && bulleTouche.size() == nombreCoup.get(0) && verifBulleToucheMemeCouleur()) {
                playSound(R.raw.oui);
                score += sommeBulleTouche;
                supprimerBulleOutDated();
                bulleTouche.clear();
                //si la somme vaut pile poile la valeur à atteindre et qu'on a pas touché le bon nombre de boule on a perdu
            } else if (sommeBulleTouche() == valeurAAtteindre.get(0) && bulleTouche.size() != nombreCoup.get(0)) {
                playSound(R.raw.erreur);
                score -= sommeBulleTouche;
                supprimerBulleOutDated();
                bulleTouche.clear();
                if (score < -50)
                    fin = true;
                //si la somme est inférieur la valeur à atteindre et qu'on a touché trop de bulle c'est perdu aussi
            } else if (sommeBulleTouche() <= valeurAAtteindre.get(0) && bulleTouche.size() >= nombreCoup.get(0)) {
                playSound(R.raw.erreur);
                score -= sommeBulleTouche;
                supprimerBulleOutDated();
                bulleTouche.clear();
                if (score < -50)
                    fin = true;
            }
        }
        else {
            if (sommeBulleTouche() == valeurAAtteindre.get(0)) {
                playSound(R.raw.oui);
                score += sommeBulleTouche;
                supprimerBulleOutDated();
                bulleTouche.clear();
            }
        }
    }

    /**
     * Fonction qui calcul la somme des bulles que l'on a touché
     * @return
     */
    public int sommeBulleTouche() {
        int compteur = 0;
        //jpense que c'est facile à comprendre
        for(int i=0; i<bulleTouche.size(); i++) {
            compteur+= bulleTouche.get(i).getValeur();
        }
        return compteur;
    }

    /**
     * Fonction qui vérifie que les bulles touchés ont la même couleur
     * @return
     */
    public boolean verifBulleToucheMemeCouleur() {
        for(int i=1; i<bulleTouche.size(); i++) {
            if( bulleTouche.get(i).getCouleur() != bulleTouche.get(i-1).getCouleur())
                return false;
        }
        return true;
    }

    /**
     * Fonction qui supprime les bulles qui ne doivent plus être là (ne pas lire si vous ne voulez pas avoir une attaque cardiaque, âme sensible s'abstenir)
     */
    public void supprimerBulleOutDated() {
        //On trie la liste d'index décroissante
        Collections.sort(bulleTouche, Collections.reverseOrder());
        String s = "";
        for(Bulle b : bulleTouche) {
            s += " " + b.getCouleur() + " ";
        }
        Log.v("Bulle touche ", s);

        //pour chaque bulle on supprime dans la liste à l'écran celle qui correspondent à la couleur de celle touché
        ListIterator<Bulle> iterator = bulleFactory.getListeBulle().listIterator();
        for(int i=0; i<bulleTouche.size(); i++) {
            while(iterator.hasNext()) {
                if(bulleTouche.get(i).getCouleur() == iterator.next().getCouleur()) {
                    iterator.remove();
                }
            }
            iterator = bulleFactory.getListeBulle().listIterator();
        }

        String s2 = "";
        for(Bulle b2 : bulleFactory.getListeBulle()) {
            s2 += " " + b2.getCouleur() + " ";
        }
        Log.v("Reste après suppr ", s2);

        //si les bulles sont de couleur 0 (celle que l'utilisateur aurait du toucher) on les vire sans pression
        ListIterator<Bulle> iterator2 = bulleFactory.getListeBulle().listIterator();
        while(iterator2.hasNext()) {
            if(iterator2.next().getCouleur() == 0) {
                iterator2.remove();
            }
        }

        //si la première bulle n'est pas une de celle qu'on aurait dû toucher (on les a déjà viré au dessus)
        if(bulleTouche.get(0).getCouleur() != 0) {
            //si on avait pas généré toutes les bulles de cette valeur on créé un nouvelle valeur
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
            //on supprime tout
            valeurAAtteindre.remove(bulleTouche.get(0).getCouleur());
            compteurValeurBulle.remove(bulleTouche.get(0).getCouleur());
            couleur.remove(bulleTouche.get(0).getCouleur());
            if(mode.equals("Challenge")) {
                nombreCoup.remove(bulleTouche.get(0).getCouleur());
                nombreBulle.remove(bulleTouche.get(0).getCouleur());
            }

            //on réduit l'incide ce chaque bulle
            for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
                if (bulleFactory.getListeBulle().get(y).getCouleur() > bulleTouche.get(0).getCouleur()) {
                    bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
                }
            }
            //ainsi que l'index
            if(bulleTouche.get(0).getCouleur() < index)
                index--;
        }

        //si il y a plus d'une bulle touché
        if(bulleTouche.size()>1) {
            //pour chaque bulle
            for (int i = 1; i < bulleTouche.size(); i++) {
                //si la couleur est différente de la précédente
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

        //si la valeur qu'on devait atteindre avait pas fini on en recréé une
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
        //et on la supprime
        valeurAAtteindre.remove(0);
        compteurValeurBulle.remove(0);
        couleur.remove(0);
        if(mode.equals("Challenge")) {
            nombreCoup.remove(0);
            nombreBulle.remove(0);
        }

        if(index !=0)
            index--;

        //on réduit l'index de chacune
        for (int y = 0; y < bulleFactory.getListeBulle().size(); y++) {
            bulleFactory.getListeBulle().get(y).setCouleur(bulleFactory.getListeBulle().get(y).getCouleur() - 1);
        }

    }

    /**
     * Fonction qui gère le déroulement du jeu
     */
    public void update() {
        //on génère les bulles
        genererBulle();
        //on déplace les bulles
        for(int i=0; i<bulleFactory.getListeBulle().size(); i++) {
            bulleFactory.getListeBulle().get(i).deplacementY(VITESSE_DEPLACEMENT);
        }
    }

    /**
     * Certains seront décus la surface ne change jamais (on retourne pas l'écran
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * Méthode appelé lors de la création de la view
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //on définit l'image du background
        final Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.backgroundw);
        backgroundResize = Bitmap.createScaledBitmap(background, metrics.widthPixels, metrics.heightPixels, true);
        background.recycle();

        //et de la bulle
        final Bitmap bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.bulle);
        bulle = Bitmap.createScaledBitmap(bitmap, ConversionDpPixel.dpToPx(60),ConversionDpPixel.dpToPx(60), false);
        bitmap.recycle();

        //création du thread permettant l'actualisation de l'affichage
        mThread.keepDrawing = true;
        mThread.start();

        //on active le son de fond en boucle
        playSoundLoop(R.raw.ambiance);
    }

    /**
     * Méthode appelé lorsque la surface est détruite (quand on passe à l'activité suivant par exemple)
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //on arrête le thread
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

    /**
     * Fonction permettant de jouer un son défini une fois
     * @param resId
     */
    public void playSound(int resId) {
        if(mPlayer != null) {
            mPlayer.stop();
            mPlayer.reset();
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(this.getContext(), resId);
        mPlayer.start();
    }

    /**
     * Fonction permettant de jouer un son défini en boucle
     * @param resId
     */
    public void playSoundLoop(int resId) {
        if(mPlayerFond != null) {
            mPlayerFond.stop();
            mPlayerFond.reset();
            mPlayerFond.release();
        }
        mPlayerFond = MediaPlayer.create(this.getContext(), resId);
        mPlayerFond.setLooping(true);
        mPlayerFond.start();
    }

    /**
     * Fonction permettant d'arrêter le son
     */
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

    /**
     * Fonction vérifiant si la partie est fini ou pas
     */
    public void verifFin() {
        //si oui
        if(fin) {
            //on arrête le thread
            mThread.keepDrawing = false;

            //on lance la popup d'alerte
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
                
            }
        }
    }

    public Score score(float value){
        return new Score(value, difficulte, mode);
    }
}

