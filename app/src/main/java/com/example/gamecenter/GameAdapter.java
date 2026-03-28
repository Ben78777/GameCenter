package com.example.gamecenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder>
{
    private ArrayList<Game> dataset;
    private ArrayList<Game> datasetFullCopy;
    private String currentTextFilter;
    private String currentCategoryFilter;
    private User user;
    public GameAdapter(ArrayList<Game> dataset)
    {
        this.dataset = dataset;
        this.datasetFullCopy = new ArrayList<>(dataset);
        this.currentTextFilter="";
        this.currentCategoryFilter="All";
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageThumbnail;
        TextView textTitle;
        TextView textGenre;
        TextView textDescription;
        TextView textDeveloper;
        TextView textPlatform;
        TextView textReleaseDate;
        ImageButton buttonFavorite;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageThumbnail = itemView.findViewById(R.id.imageThumbnail);
            textTitle = itemView.findViewById(R.id.textTitle);
            textGenre = itemView.findViewById(R.id.textGenre);
            textDescription = itemView.findViewById(R.id.textDescription);
            textDeveloper = itemView.findViewById(R.id.textDeveloper);
            textPlatform = itemView.findViewById(R.id.textPlatform);
            textReleaseDate = itemView.findViewById(R.id.textReleaseDate);
            buttonFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
    @NonNull
    @Override
    public GameAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull GameAdapter.MyViewHolder holder, int position) {
        Game item = dataset.get(position);
        MainActivity mainActivity = (MainActivity) holder.itemView.getContext();
        String gameId = item.getId() + "";

        Glide.with(holder.itemView.getContext())
                .load(item.getThumbnail())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageThumbnail);

        holder.textTitle.setText(item.getTitle());
        holder.textGenre.setText(item.getGenre());
        holder.textDescription.setText(item.getShort_description());
        holder.textDeveloper.setText(item.getDeveloper());
        holder.textPlatform.setText(item.getPlatform());
        holder.textReleaseDate.setText(item.getRelease_date());

        user = mainActivity.getCurrentUser();
        boolean isCurrentlyFav = (user != null && user.getFavoriteGames() != null && user.getFavoriteGames().contains(gameId));

        holder.buttonFavorite.setImageResource(isCurrentlyFav ?
                android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                int destinationId = navController.getCurrentDestination().getId();
                Bundle bundle = new Bundle();
                bundle.putInt("game_id", item.getId());

                if (destinationId == R.id.applicationFragment)
                {
                    navController.navigate(R.id.action_applicationFragment_to_gameDetailFragment, bundle);
                }
                else if (destinationId == R.id.favoriteGamesFragment)
                {
                    navController.navigate(R.id.action_favoriteGamesFragment_to_gameDetailFragment, bundle);
                }
            }
        });

        holder.buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.toggleFavorite(item);

                user = mainActivity.getCurrentUser();
                boolean isGameFavorite = (user != null && mainActivity.getCurrentUser().getFavoriteGames().contains(gameId));


                NavController navController = Navigation.findNavController(v);
                int destinationId = navController.getCurrentDestination().getId();

                if (destinationId == R.id.applicationFragment && !isGameFavorite)
                {
                    holder.buttonFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                }

                if (destinationId == R.id.favoriteGamesFragment && !isGameFavorite)
                {
                    dataset.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
                else if ((destinationId == R.id.favoriteGamesFragment || destinationId == R.id.applicationFragment) && isGameFavorite )
                {
                    holder.buttonFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void filterGamesByText(String textFilter) {
        if (!textFilter.isEmpty())
        {
            this.currentTextFilter = textFilter;
        }
        else
        {
            this.currentTextFilter="";
        }
        applyTextAndCategorySearchFilters();
    }
    public void filterByCategory(String category) {
        this.currentCategoryFilter = category;
        applyTextAndCategorySearchFilters();
    }

    public void applyTextAndCategorySearchFilters()
    {
        if (datasetFullCopy.isEmpty() && !dataset.isEmpty())
        {
            datasetFullCopy.addAll(dataset);
        }

        ArrayList<Game> filteredList = new ArrayList<>();
        String textFilterNormalized = currentTextFilter.toLowerCase().trim();
        for (Game game : datasetFullCopy)
        {
            if ((game.getGenre().equals(this.currentCategoryFilter) || this.currentCategoryFilter.equals("All")) && game.getTitle().toLowerCase().contains(textFilterNormalized))
            {
                filteredList.add(game);
            }
        }
        dataset.clear();
        dataset.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<Game> newList)
    {
        this.datasetFullCopy = new ArrayList<>(newList);
        applyTextAndCategorySearchFilters();
    }
    public ArrayList<Game> getDatasetFullCopy()
    {
        return datasetFullCopy;
    }

}
