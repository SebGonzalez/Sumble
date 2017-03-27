package fr.unice.iutnice.sumble.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabriel on 14/03/2017.
 * Enumération des types de difficulté
 */

public enum TypeDifficulte implements Parcelable{

    //on créé 3 modes de difficulté
    Facile("FACILE"),
    Moyen("INTERMEDIAIRE"),
    Difficile("DIFFICILE");


    private String choix;

    /**
     * Constructeur normal
     * @param choix
     */
    TypeDifficulte(String choix){
        this.choix = choix;
    }

    /**
     * Setteur
     * @param choix
     */
    private void setChoix(String choix)
    {
        this.choix = choix;
    }

    TypeDifficulte(Parcel in) {
        choix = in.readString();
    }

    public String getChoix(){
        return choix;
    }

    /**
     * écrit l'objet dans la parcel pour qu'il puisse passer d'activité en activité
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
        dest.writeString(choix);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Méthode qui va être appelée pour reconstruire l'objet parcelable
     */
    public static final Creator<TypeDifficulte> CREATOR = new Creator<TypeDifficulte>() {
        @Override
        public TypeDifficulte createFromParcel(Parcel in) {

            TypeDifficulte typeDifficulte = TypeDifficulte.values()[in.readInt()];
            typeDifficulte.setChoix(in.readString());
            return typeDifficulte;
        }

        @Override
        public TypeDifficulte[] newArray(int size) {
            return new TypeDifficulte[size];
        }
    };

    public String toString(){
        return choix;
    }
}
