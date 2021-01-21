package com.example.mobilki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.LoginActivity;
import com.example.mobilki.R;
import com.example.mobilki.StartActivity;
import com.example.mobilki.User;
import com.example.mobilki.adapters.ShoppingListAdapter;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;
import com.example.mobilki.fragments.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    TextView textView;

    private List<ShoppingList> shoppingLists = new ArrayList<>();


    private CircleImageView circleImageView;

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
                if(!firebaseUser.getUid().equals(newShL.getUserID()) && newShL.getCourierID().isEmpty()){
                    shoppingLists.add(newShL);
                    if(shoppingLists.isEmpty())
                        textView.setVisibility(View.VISIBLE);
                    else
                        textView.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }

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

    //obsluga klikniecia opcji nawigacji
    private void setNavigationListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.my_profile:{
                        Intent intent = new Intent(getApplicationContext(), ChatsActivity.class);
                        intent.putExtra("key", 2);
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
                    case R.id.log_out:{
                        setUserStatus("offline");
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    }
                }
                return false;
            }
        });
    }

    private void setNavImage() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
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
    }


    private void initViews() {
        drawerLayout = findViewById(R.id.nav_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.inflateHeaderView(R.layout.navigation_header);
        circleImageView = headerView.findViewById(R.id.profile_image_nav);

        setNavImage();

        recyclerView = findViewById(R.id.shoppingListsRecyclerView);

        adapter = new ShoppingListAdapter(this,shoppingLists);
        recyclerView.setAdapter(adapter);
        textView = findViewById(R.id.nothing);
        if(shoppingLists.isEmpty())
            textView.setVisibility(View.VISIBLE);
        else
            textView.setVisibility(View.INVISIBLE);
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

    //wyszukiwanie wedlug imiona nazwiska oraz adresu
    private void filter(String newText) {
        sh.clear();
        for(ShoppingList i : shoppingLists){
            //String shop = i.getShop().toLowerCase();

            if( i.getNameSurname().toLowerCase().contains(newText.toLowerCase())
                    || i.getAddress().toLowerCase().contains(newText.toLowerCase()))
            {
                sh.add(i);
            }
        }
        adapter.setLists(sh);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }



    private void setUserStatus(String status) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        databaseReference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUserStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUserStatus("offline");
    }
}
