package com.example.gamecenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GameApiService
{
    @GET("games")
    Call<List<Game>> getGames();
}
