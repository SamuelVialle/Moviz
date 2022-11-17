package com.samuelvialle.moviz;

import static com.samuelvialle.moviz.commons.Constants.COLLECTION_FILMS;
import static com.samuelvialle.moviz.commons.Constants.FILE_PREFS;
import static com.samuelvialle.moviz.commons.Constants.KEY_PREFS;
import static com.samuelvialle.moviz.commons.Constants.KEY_TITRE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.samuelvialle.moviz.commons.Utils;

public class HomeActivity extends AppCompatActivity {

    private View baseView;
    private RecyclerView rvFilms;
    private Context context;
    private AdapterFilms adapter;
    private FirebaseFirestore db;

    private void initUI() {
        baseView = findViewById(R.id.mainLayout);
        context = getApplicationContext();

        rvFilms = findViewById(R.id.rvFilms);
        rvFilms.setHasFixedSize(true);
        rvFilms.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));

        db = FirebaseFirestore.getInstance();
    }

    private void getDataFromFirestore(){
        Query query = db
                .collection(COLLECTION_FILMS)
                .orderBy(KEY_TITRE);

        FirestoreRecyclerOptions<ModelFilms> films =
                new FirestoreRecyclerOptions.Builder<ModelFilms>()
                        .setQuery(query, ModelFilms.class)
                        .build();

        adapter = new AdapterFilms(films);

        rvFilms.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        AddSampleData();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Utils.showSnackBar(baseView, "Hello " + currentUser.getDisplayName());
        getDataFromFirestore();
    }

    private void AddSampleData(){
        SharedPreferences sharedPreferences = getSharedPreferences(
                FILE_PREFS, Context.MODE_PRIVATE);

        if(!sharedPreferences.getBoolean(KEY_PREFS, false)){
            AddSampleDataToFireBase.addDataToFireBase(getApplicationContext());
        }
    }
}