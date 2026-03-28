package com.example.gamecenter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteGamesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private User user;
    private TextView emptyText;
    private RecyclerView recyclerView;
    private GameAdapter gameAdapter;
    private ImageButton buttonBackToApplication;

    private String mParam1;
    private String mParam2;

    public FavoriteGamesFragment() {
    }
    public static FavoriteGamesFragment newInstance(String param1, String param2) {
        FavoriteGamesFragment fragment = new FavoriteGamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorite_games, container, false);

        buttonBackToApplication = view.findViewById(R.id.btnBack);
        emptyText = view.findViewById(R.id.emptyFavoritesText);
        recyclerView = view.findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        gameAdapter = new GameAdapter(new ArrayList<>());
        recyclerView.setAdapter(gameAdapter);

        loadFavoriteGames();

        buttonBackToApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(),R.id.navhostfragment);
                navController.navigate(R.id.action_favoriteGamesFragment_to_applicationFragment);
            }
        });
        return view;
    }

    private void loadFavoriteGames() {
        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null) {
            User user = mainActivity.getCurrentUser();

           ArrayList<Game> allGames = new ArrayList<>(mainActivity.getGameList());
           ArrayList<Game> favoriteGamesOfUser = new ArrayList<>();

            if (user != null && user.getFavoriteGames() != null && allGames != null) {
                ArrayList<String> favoriteGamesIds = user.getFavoriteGames();

                if (favoriteGamesIds.isEmpty())
                {
                    emptyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else
                {
                    for (Game game : allGames)
                    {
                        if (favoriteGamesIds.contains(game.getId()+""))
                        {
                            favoriteGamesOfUser.add(game);
                        }
                    }

                    if (favoriteGamesOfUser.isEmpty())
                    {
                        emptyText.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                    else
                    {
                        emptyText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
            else
            {
                emptyText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            gameAdapter.updateData(favoriteGamesOfUser);
        }
    }
}