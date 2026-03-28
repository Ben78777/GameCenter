package com.example.gamecenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class GameDetailFragment extends Fragment {

    public GameDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_detail, container, false);

        ImageButton buttonBackToApplication = view.findViewById(R.id.btnBack);
        ImageView image = view.findViewById(R.id.detailImage);
        TextView title = view.findViewById(R.id.detailTitle);
        TextView genre = view.findViewById(R.id.detailGenre);
        TextView desc = view.findViewById(R.id.detailDescription);
        TextView platform = view.findViewById(R.id.detailPlatform);
        TextView publisher = view.findViewById(R.id.detailPublisher);
        TextView developer = view.findViewById(R.id.detailDeveloper);
        TextView releaseDate = view.findViewById(R.id.detailReleaseDate);
        Button btnPlayNow = view.findViewById(R.id.btnPlayNow);

        if (getArguments() != null) {
            int gameId = getArguments().getInt("game_id");

            MainActivity mainActivity = (MainActivity) getActivity();
            Game game = null;

            if (mainActivity != null && mainActivity.getGameList() != null) {
                for (Game g : mainActivity.getGameList()) {
                    if (g.getId() == gameId) {
                        game = g;
                        break;
                    }
                }
            }

            if (game != null) {
                title.setText(game.getTitle());
                genre.setText(game.getGenre());
                desc.setText(game.getShort_description());
                platform.setText(game.getPlatform());
                publisher.setText(game.getPublisher());
                developer.setText(game.getDeveloper());
                releaseDate.setText(game.getRelease_date());

                Glide.with(this)
                        .load(game.getThumbnail())
                        .into(image);

                String gameUrl = game.getGame_url();
                btnPlayNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gameUrl));
                        startActivity(intent);
                    }
                });
            }
        }

        buttonBackToApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.navhostfragment);
                navController.navigate(R.id.action_gameDetailFragment_to_applicationFragment);
            }
        });

        return view;
    }
}