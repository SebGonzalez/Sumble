package fr.unice.iutnice.sumble.Controller;

/**
 * Created by gonzo on 21/03/2017.
 */

public interface iNiveau {

    void genererBulle();
    int[] definirValeurBulle(boolean aleatoire);
    int definirValeurAAtteindre();
    void verifPoint();
    int sommeBulleTouche();
    int modifScore();
    void supprimerBulleOutDated();
    void update() throws Exception;
}
