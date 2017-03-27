package fr.unice.iutnice.sumble.Controller;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import fr.unice.iutnice.sumble.Model.Bulle;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by Sébastien Gonzalez on 07/03/2017.
 */

/**
 * Classe de gestion des bulles (stockage action..)
 */
public class BulleFactory {

    private DisplayMetrics metrics;
    private static ArrayList<Bulle> listeBulle;

    /**
     * Constructeur
     * @param metrics
     */
    public BulleFactory(DisplayMetrics metrics) {
        this.metrics = metrics;
        listeBulle = new ArrayList<>();
    }

    /**
     * getter
     * @return la liste des bulles
     */
    public ArrayList<Bulle> getListeBulle() {
        return listeBulle;
    }

    /**
     * Methode permettant de vérifier si une bulle se trouve en dessous de celle passé en paramètre
     * @param b correspondant à la bulle à tester
     * @return -1 si il n'y a pas de bulles en dessous sinon l'écart en x entre la position de la bulle en dessous et de la bulle à tester
     */
    public static int verifBulleEnDessous(Bulle b) {

        //Pour chaque bulle
        for(int i=0; i<listeBulle.size(); i++) {
            //si il y a une bulle en dessous
            if(b.getY()+b.getLargeur() > listeBulle.get(i).getY() && b.getY()+b.getLargeur() < listeBulle.get(i).getY()+listeBulle.get(i).getLargeur() && !b.equals(listeBulle.get(i))) {
                //si la bulle en dessous est à sa gauche
                if(b.getX()>=listeBulle.get(i).getX() && b.getX() < listeBulle.get(i).getX() + listeBulle.get(i).getLargeur()) {
                    //on retourne l'écart entre les deux position des bulles
                    return ( (listeBulle.get(i).getX()+listeBulle.get(i).getLargeur()) - b.getX());
                }
                //sinon si elle est à sa droite
                else if(b.getX()<=listeBulle.get(i).getX() && b.getX()+b.getLargeur()>listeBulle.get(i).getX()) {
                    //on retourne l'écart entre les deux position des bulles
                    return -(((b.getX()+b.getLargeur())-listeBulle.get(i).getX()));
                }
            }
        }
        //sinon c'est qu'il n'y a pas de bulle en dessous on retourne -1
        return -1;
    }

    /**
     * Fonction permettant de réactiver le déplacement des bulles
     */
    public void resetBloque() {
        //pour chaque bulle, si elle est bloque on la débloque
        for(int i=0; i<listeBulle.size(); i++) {
            if(listeBulle.get(i).isBloque()) {
                listeBulle.get(i).setBloque(false);
            }
        }
    }

    /**
     * Fonction permettant de vérifier si l'on peut générer une bulle
     * @param largeur correspondant à la largeur de la bulle à générer
     * @return true si il y a la place sur le plateau de jeu pour générer la bulle, false sinon
     */
    public boolean verifPossibiliteGen(int largeur) {
        //pour différente position on vérifie si l'on peut placer la bulle
        for (int i = 0; i < metrics.widthPixels; i+=metrics.widthPixels/5) {
            if(verifPossibiliteGenPosition(i, largeur)) {
                //si à au moins une position on peut on retourne true
                return true;
            }
        }
        //sinon on retourne le resultat de la fonction, true si les bulles ne sont pas toutes bloqué (car si des bulles ne sont pas encore tombés et bloque la génération il ne faut pas arrêter le jeu
        return !verifBulleBloque();

    }

    /**
     * Fonction permettant de vérifier si l'ensemble des bulles sont dans un état "bloqué"
     * @return true si oui, false sinon
     */
    public boolean verifBulleBloque() {
        for(int i=0; i<listeBulle.size(); i++) {
            if(!listeBulle.get(i).isBloque())
                return false;
        }
        return true;
    }

    /**
     * Fonction permettant de vérifier si l'on peut générer une bulle à une position donné
     * @param largeur correspondant à la largeur de la bulle à générer
     * @return true si il y a la place sur le plateau de jeu pour générer la bulle, false sinon
     * @return
     */
    public boolean verifPossibiliteGenPosition(int x, int largeur) {
        //pour chaque bulle
        for(int y=0; y<listeBulle.size(); y++) {
            //on vérifie si il y a une bulle en dessous
            if (ConversionDpPixel.dpToPx(60) + largeur >= listeBulle.get(y).getY() || ConversionDpPixel.dpToPx(60) >= listeBulle.get(y).getY()) {
                //si la bulle est à gauche
                if(x<=listeBulle.get(y).getX() && x+largeur>= listeBulle.get(y).getX() + listeBulle.get(y).getLargeur())
                    return false;
                //sinon si elle est à gauche
                else if ((x >= listeBulle.get(y).getX() && x <= listeBulle.get(y).getX() + listeBulle.get(y).getLargeur()) || (x + largeur >= listeBulle.get(y).getX() && x + largeur <= listeBulle.get(y).getX() + listeBulle.get(y).getLargeur()))
                    return false;
                //sinon si la bulle en dessous se trouve à l'intérieur de la bulle
                else if( x>= listeBulle.get(y).getX() && x+largeur <= listeBulle.get(y).getLargeur())
                    return false;

            }
        }
        //on retourne true si il n'y a pas eu de bulle en dessous
        return true;
    }
}
