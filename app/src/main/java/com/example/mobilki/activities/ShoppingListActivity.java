package com.example.mobilki.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.R;
import com.example.mobilki.adapters.ShoppingListAdapter;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private List<ShoppingList> shoppingLists = new ArrayList<>();
    private RecyclerView recyclerView;
    private ShoppingListAdapter adapter;
    private  ArrayList<ShoppingList> sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

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
        return super.onOptionsItemSelected(item);
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
