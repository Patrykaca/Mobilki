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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.LoginActivity;
import com.example.mobilki.R;
import com.example.mobilki.StartActivity;
import com.example.mobilki.User;
import com.example.mobilki.adapters.ShoppingListAdapter;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private  ArrayList<ShoppingList> sh;
    private CircleImageView circleImageView;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);


        drawerLayout = findViewById(R.id.nav_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);
        circleImageView = headerView.findViewById(R.id.profile_image_nav);

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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User _user = snapshot.getValue(User.class);
                assert _user != null;
                if(Objects.equals(_user.getImageUrl(), "default")) {
                    circleImageView.setImageResource(R.drawable.profile_icon);
                }
                else {
                    Picasso.get().load(_user.getImageUrl()).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setInitData();
        sh = new ArrayList<>();
        recyclerView = findViewById(R.id.shoppingListsRecyclerView);
        adapter = new ShoppingListAdapter(this,shoppingLists);
        recyclerView.setAdapter(adapter);

        Button addSh = findViewById(R.id.addShLButton);
        addSh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddShoppingListActivity.class);
                startActivity(intent);
            }
        });
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

    private void setInitData(){
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Pierogi",  2,"opakowanie"));
        items.add(new Item("Pomidor", (float) 1.5,"kg"));
        items.add(new Item("Kapusta",2,"szt"));
        items.add(new Item("Wolowina", (float) 3.5,"kg"));
        items.add(new Item("Kurczak", (float) 0.5,"kg"));
        items.add(new Item("Buraczki",5,"szt"));
        shoppingLists.add(new ShoppingList("asdj", "Lidl",items,"Wolczanska 5","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Auchan",items,"Politechniki 53","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Zabka",items,"Piotrkowska 42","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Biedronka",items,"Pilsudskiego 6","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Stokrotka",items,"Sarnia 9","Lodz"));shoppingLists.add(new ShoppingList("asdj", "Lidl",items,"Wolczanska 5","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Auchan",items,"Politechniki 53","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Zabka",items,"Piotrkowska 42","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Biedronka",items,"Pilsudskiego 6","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Stokrotka",items,"Sarnia 9","Lodz"));shoppingLists.add(new ShoppingList("asdj", "Lidl",items,"Wolczanska 5","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Auchan",items,"Politechniki 53","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Zabka",items,"Piotrkowska 42","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Biedronka",items,"Pilsudskiego 6","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Stokrotka",items,"Sarnia 9","Lodz"));shoppingLists.add(new ShoppingList("asdj", "Lidl",items,"Wolczanska 5","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Auchan",items,"Politechniki 53","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Zabka",items,"Piotrkowska 42","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Biedronka",items,"Pilsudskiego 6","Lodz"));
        shoppingLists.add(new ShoppingList("asdj", "Stokrotka",items,"Sarnia 9","Lodz"));
    }
}
