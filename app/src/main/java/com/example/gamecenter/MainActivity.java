package com.example.gamecenter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private User currentUser;
    private List<Game> gameList;
    private static final String BASE = "https://www.freetogame.com/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        fetchGames();
    }

    public void fetchGames()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GameApiService service = retrofit.create(GameApiService.class);

        Call<List<Game>> call = service.getGames();
        call.enqueue(new Callback<List<Game>>() {

            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gameList = response.body();
                }
            }
            @Override
            public void onFailure(Call<List<Game>> call, Throwable t)
            {

            }
        });
    }

    public void login() {
        String username = ((EditText) findViewById(R.id.usernameEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();

        if (!username.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = mAuth.getCurrentUser().getUid();
                                readUserFromDatabase(userId);

                                Toast.makeText(MainActivity.this, "Login Sucessful", Toast.LENGTH_LONG).show();
                                NavController navController = Navigation.findNavController(MainActivity.this, R.id.navhostfragment);
                                navController.navigate(R.id.action_loginFragment_to_applicationFragment);
                            } else {
                                Toast.makeText(MainActivity.this, "Login Failed, invalid credentials", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(MainActivity.this, "Login Failed, username or passsword is empty!", Toast.LENGTH_LONG).show();
        }
    }

    public void register() {
        String username = ((EditText) findViewById(R.id.usernameEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        String phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();

        if (!username.isEmpty() && !password.isEmpty() && !phone.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Register Sucessful,please sign in", Toast.LENGTH_LONG).show();
                                writeUserToDatabase();

                                NavController navController = Navigation.findNavController(MainActivity.this, R.id.navhostfragment);
                                navController.navigate(R.id.action_registerFragment_to_loginFragment);
                            } else {
                                Toast.makeText(MainActivity.this, "Register Failed, invalid credentials", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(MainActivity.this, "Register Failed, username or passsword or phone is empty!", Toast.LENGTH_LONG).show();
        }
    }

    public void writeUserToDatabase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = ((EditText)findViewById(R.id.usernameEditText)).getText().toString();
        String phone = ((EditText)findViewById(R.id.phoneEditText)).getText().toString();

        User user = new User(username, phone,new ArrayList<>());
        usersRef.child(userId).setValue(user);
    }
    public void readUserFromDatabase(String userId)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(userId);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setCurrentUser(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public User getCurrentUser()
    {
        return this.currentUser;
    }
    public void setCurrentUser(User user)
    {
        this.currentUser = user;
    }

    @Override
    protected void onStart() //used to avoid crashing and bugs if leaving and returning to app
    {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            readUserFromDatabase(mAuth.getCurrentUser().getUid());
        }
    }
    public void toggleFavorite(Game game) {
        if (currentUser == null || mAuth.getCurrentUser() == null) {
            return;
        }

        String gameId = game.getId() +"";
        ArrayList<String> favorites = currentUser.getFavoriteGames();

        if (favorites == null)
        {
            favorites = new ArrayList<>();
            currentUser.setFavoriteGames(favorites);
        }

        if (favorites.contains(gameId))
        {
            favorites.remove(gameId);
            currentUser.setFavoriteGames(favorites);
        }
        else
        {
            favorites.add(gameId);
            currentUser.setFavoriteGames(favorites);
        }

        String userId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("favoriteGames")
                .setValue(favorites)
                .addOnFailureListener(e -> {
                });
    }
}