package com.example.mobilki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.LoginActivity;
import com.example.mobilki.R;
import com.example.mobilki.StartActivity;
import com.example.mobilki.adapters.ShoppingListAdapter;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private  ArrayList<ShoppingList> sh = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        initFirebaseConnection();

        //inicjalizacja komponentow widoku
        initViews();

        //obsluga menu dla navigacji aplikacja
        setNavigationListener();

        //nasluchiwanie przycisku dodaj liste zakupow
        initAddShoppingListButtonListener();


    }

    @Override
    protected void onStart() {
        super.onStart();
        shoppingLists.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ShoppingList newShL = snapshot.getValue(ShoppingList.class);
                shoppingLists.add(newShL);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initFirebaseConnection() {
       firebaseAuth = FirebaseAuth.getInstance();
       firebaseUser = firebaseAuth.getCurrentUser();

       firebaseDatabase = FirebaseDatabase.getInstance();
       databaseReference = firebaseDatabase.getReference().child("Advertisements");
       databaseReference.keepSynced(true);
    }



    private void initAddShoppingListButtonListener() {
        Button addSh = findViewById(R.id.addShLButton);
        addSh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddShoppingListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setNavigationListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.my_profile:{
                        Intent intent = new Intent(getApplicationContext(),MyProfileActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.chats:{
                        Intent intent = new Intent(getApplicationContext(),ChatsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.m_shopping_lists:{
                        Intent intent = new Intent(getApplicationContext(),MyShLActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.my_responses:{
                        Intent intent = new Intent(getApplicationContext(),MyResponsesActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.settings:{
                        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                        startActivity(intent);
                        break;

                    }
                    case R.id.log_out:{
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        recyclerView = findViewById(R.id.shoppingListsRecyclerView);

        adapter = new ShoppingListAdapter(this,shoppingLists);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serch_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.searchOption);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty())
                    setInitList();
                else{
                    filter(newText);
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setInitList() {
        adapter.setLists(shoppingLists);
        adapter.notifyDataSetChanged();
    }

    private void filter(String newText) {
        sh.clear();
        for(ShoppingList i : shoppingLists){
            String shop = i.getShop().toLowerCase();

            if(shop.contains(newText.toLowerCase()))
            {
                sh.add(i);
            }
        }
        adapter.setLists(sh);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //TODO settings selected
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

}
