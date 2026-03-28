package com.example.gamecenter;

import java.util.ArrayList;

public class User
{
    private String name;
    private String phone;
    private ArrayList<String> favoriteGames;

    public User()
    {

    }
    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.favoriteGames = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getFavoriteGames()
    {
        if (favoriteGames == null) //this row is to avoid the firebase from deleting entirely the favoriteGames and creating a bug
        {                          // if no favoriteGames then an empty list to fill the void in firebase realtime db..
            return new ArrayList<>();
        }
        return favoriteGames;
    }
    public void setFavoriteGames(ArrayList<String> favoriteGames) {
        this.favoriteGames = favoriteGames;
    }

    public User(String name, String phone, ArrayList<String> favoriteGames) {
        this.name = name;
        this.phone = phone;
        this.favoriteGames = favoriteGames;
    }
}
