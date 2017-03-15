package fr.unice.iutnice.sumble.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabriel on 14/03/2017.
 */

public class Score implements Parcelable {

    private float valeur;
    private TypeDifficulte typeDifficulte;

    public Score(float valeur, TypeDifficulte typeDifficulte){
        this.valeur = valeur;
        this.typeDifficulte = typeDifficulte;
    }

    protected Score(Parcel in) {
        valeur = in.readFloat();
        typeDifficulte = in.readParcelable(TypeDifficulte.class.getClassLoader());
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(valeur);
        dest.writeParcelable(typeDifficulte, flags);
    }

    public String toString(){
        return ""+valeur+", "+typeDifficulte.toString();
    }

    public float getValeur() {
        return valeur;
    }

    public TypeDifficulte getTypeDifficulte() {
        return typeDifficulte;
    }
}
