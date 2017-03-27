package fr.unice.iutnice.sumble.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabriel on 14/03/2017.
 */

/**
 * Classe de gestion du Score (parcelable pour le faire passer entre les activité)
 */
public class Score implements Parcelable {

    //Un score contient la valeur correspondant au résultat de la partie, la difficulté et le mode dans lequel l'utilisateur a joué
    private float valeur;
    private TypeDifficulte typeDifficulte;
    private String mode;

    /**
     * Constructeur
     * @param valeur du score
     * @param typeDifficulte du jeu
     * @param mode du jeu
     */
    public Score(float valeur, TypeDifficulte typeDifficulte, String mode){
        this.valeur = valeur;
        this.typeDifficulte = typeDifficulte;
        this.mode = mode;
    }

    /**
     * constructeur pour reconstruir l'objet à partir d'un parcel
     */
    protected Score(Parcel in) {
        valeur = in.readFloat();
        typeDifficulte = in.readParcelable(TypeDifficulte.class.getClassLoader());
        mode = in.readString();
    }

    /**
     * Méthode permettant de construire une instance de Score à partir d’un Parcel
     */
    public static final Creator<Score> CREATOR = new Creator<Score>() {
        @Override
        public Score createFromParcel(Parcel in) {
            return new Score(in);
        }

        @Override
        public Score[] newArray(int size) {
            return new Score[size];
        }
    };

    /**
     * Sert à décrire le contenu de notre Parcel et plus précisément le nombre d’objet spéciaux contenus dans le Parcel
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Sert à écrire l’objet dans un Parcel
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(valeur);
        dest.writeParcelable(typeDifficulte, flags);
        dest.writeString(mode);
    }

    /**
     * Méthode standarde toString
     * @return
     */
    public String toString(){
        return ""+valeur+", "+typeDifficulte.toString()+", "+mode;
    }

    //getter
    public float getValeur() {
        return valeur;
    }

    public TypeDifficulte getTypeDifficulte() {
        return typeDifficulte;
    }

    public String getMode(){
        return mode;
    }
}
