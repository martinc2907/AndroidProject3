package com.example.user.androidlock;

/**
 * Created by user on 2018-01-09.
 */

public class Background {

    int image;
    int price;
    boolean owned;
    String tag;

    public int getImage(){
        return image;
    }

    public int getPrice(){
        return price;
    }

    public String getTag(){
        return tag;
    }

    public boolean getAvailable(){
        return owned;
    }

    public Background(int image, int price, boolean owned, String tag ){
        this.image = image;
        this.price = price;
        this.tag = tag;
        this.owned = owned;
    }
}
