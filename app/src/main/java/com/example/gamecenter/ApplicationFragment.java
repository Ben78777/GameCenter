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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class ApplicationFragment extends Fragment {

    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private ArrayList<Game> gameList = new ArrayList<>();
    private SearchView searchView;
    private LinearLayout layoutEmptyState;
    private ImageButton buttonGoToFavorites;

    public ApplicationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application, container, false);

        buttonGoToFavorites = view.findViewById(R.id.btnGoToFavorites);
        layoutEmptyState = view.findViewById(R.id.layoutEmptyState);
        recyclerView = view.findViewById(R.id.recyclerViewGames);
        searchView = view.findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GameAdapter(gameList);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filterGamesByText(newText);
                updateEmptySearch();
                return true;
            }
        });

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null && mainActivity.getGameList() != null) {
            List<Game> gamesFromActivity = mainActivity.getGameList();
            updateGames(gamesFromActivity);
            setChipCategoryValues(view, gamesFromActivity);
        }

        buttonGoToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.navhostfragment);
                navController.navigate(R.id.action_applicationFragment_to_favoriteGamesFragment);
            }
        });

        return view;
    }

    public void updateGames(List<Game> games) {
        gameList.clear();
        gameList.addAll(games);
        if (adapter != null) {
            adapter.updateData(new ArrayList<>(games));
        }
    }
    public void setChipCategoryValues(View fragmentView, List<Game> games) {
        ArrayList<String> categories = new ArrayList<>();

        ChipGroup chipGroup = fragmentView.findViewById(R.id.chipGroup);
        if (chipGroup == null) return;

        categories.add("All");
        for (Game game : games) {
            if (!categories.contains(game.getGenre())) {
                categories.add(game.getGenre());
            }
        }

        chipGroup.removeAllViews();
        for (String category : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(category);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setChipBackgroundColorResource(android.R.color.white);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    adapter.filterByCategory(category);
                }
            });

            chipGroup.addView(chip);
            if (category.equals("All"))
            {
                chip.setChecked(true);
            }
        }
    }

    private void updateEmptySearch() {
        if (adapter != null && layoutEmptyState != null)
        {
            if (adapter.getItemCount() == 0)
            {
                layoutEmptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else
            {
                layoutEmptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}