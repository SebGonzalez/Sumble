package fr.unice.iutnice.sumble.Controller;

/**
 * Created by Sébastien Gonzalez on 21/03/2017.
 */

/**
 * Interface contenant toutes les méthodes pour gérer le déroulement du jeu
 */
public interface iNiveau {

    void genererBulle();
    int[] definirValeurBulle(boolean aleatoire);
    int definirValeurAAtteindre();
    void verifPoint();
    int sommeBulleTouche();
    void supprimerBulleOutDated();
    void update() throws Exception;
}
