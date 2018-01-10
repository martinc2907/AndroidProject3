package com.example.user.androidlock;

/**
 * Created by user on 2018-01-08.
 */

public class Background {
    int image;
    int price;
    boolean available;
    String tag;


    public int getImage(){
        return this.image;
    }

    public int getPrice(){
        return this.price;
    }

    public String getTag(){
        return this.tag;
    }
    public boolean getAvailable(){
        return this.available;
    }

    public void setAvailable(){
        this.available = true;
    }
    public Background(int image, int price, boolean available, String tag ){
        this.image = image;
        this.price = price;
        this.tag = tag;
        this.available = available;

    }

}
