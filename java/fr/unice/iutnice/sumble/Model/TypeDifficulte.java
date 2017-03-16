package fr.unice.iutnice.sumble.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gabriel on 14/03/2017.
 */

public enum TypeDifficulte implements Parcelable{

    Facile("FACILE"),
    Moyen("INTERMEDIAIRE"),
    Difficile("DIFFICILE");


    private String choix;

    TypeDifficulte(String choix){
        this.choix = choix;
    }

    private void setChoix(String choix){
        this.choix = choix;
    }

    TypeDifficulte(Parcel in) {
        choix = in.readString();
    }

    public String getChoix(){
        return choix;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
        dest.writeString(choix);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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
