package com.example.mobilki.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilki.R;
import com.example.mobilki.classes.Item;
import com.example.mobilki.classes.ShoppingList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShoppingListDetailedActivity extends AppCompatActivity {
    private TextView cityTextView;
    private TextView addressTextView;
    private TextView itemsTextView;
    private TextView shopTextView;
    private TextView statusTextView;
    private TextView nameSurnameTextView;

    private Button giveUpButton;
    private Button updateStatusButton;

    private ShoppingList sh;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private String newStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_detailed);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sh = null;

        initViews();

        //obsluga zaladowania widoku w przypadkach jesli nie zostaly do aktywnosci przeslane poprawnie parametry
        if(getIntent().getExtras() != null){
            try {
                sh = (ShoppingList) getIntent().getExtras().getSerializable("sh");

                //nawiazanie polaczenia z firebase
                initFirebaseConnection();

                    cityTextView.setText(sh.getCity().toString());
                    addressTextView.setText(sh.getAddress().toString());
                    shopTextView.setText(sh.getShop().toString());
                    statusTextView.setText(sh.getStatus());
                    nameSurnameTextView.setText(sh.getNameSurname());


                    String status="";
                    if(sh.getStatus().equals("accepted")){
                        status = "Update status to bought";
                    }else if(sh.getStatus().equals("bought")){
                        status = "Update status to delivered";
                    }
                    updateStatusButton.setText(status);
                    setStatusTextColor();

                    if(sh.getStatus().equals("delivered")){
                        updateStatusButton.setVisibility(View.INVISIBLE);

                    }

                    //obsluga oraz widocznosc przyciskow do zmiany statusu albo rezygnacji w zaleznosci czy to
                    // ogloszenie na ktore biezacy uzytkownik odpowiedzial czy to po prostu informacje o dowolnym
                    // innym ogloszeniu
                    try {
                        boolean active = getIntent().getExtras().getBoolean("active");
                        if(active){
                            if(sh.getStatus().equals("delivered")){
                                updateStatusButton.setVisibility(View.INVISIBLE);
                            }
                            giveUpButton.setVisibility(View.VISIBLE);

                            updateStatusButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!sh.getStatus().equals("delivered")){
                                        HashMap hashMap = new HashMap();
                                        if(sh.getStatus().equals("accepted")){
                                            hashMap.put("status","bought");
                                            newStatus = "bought";
                                            setStatusTextColor();
                                        }else if(sh.getStatus().equals("bought")){
                                            hashMap.put("status","delivered");
                                            newStatus = "delivered";
                                            setStatusTextColor();
                                        }
                                        AlertDialog alertDialog = new AlertDialog.Builder(ShoppingListDetailedActivity.this).create();
                                        alertDialog.setMessage("Are you sure you want to update status to " + hashMap.get("status") + "?");
                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                                            databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    sh.setStatus(newStatus);
                                                    statusTextView.setText(newStatus);
                                                    Toast.makeText(ShoppingListDetailedActivity.this, "Status updated to " + newStatus,Toast.LENGTH_SHORT).show();
                                                    if(!sh.getStatus().equals("delivered")){
                                                        if(sh.getStatus().equals("accepted")){
                                                            updateStatusButton.setText("Update status to bought");
                                                            setStatusTextColor();
                                                        }else if(sh.getStatus().equals("bought")){
                                                            updateStatusButton.setText("Update status to delivered");
                                                            setStatusTextColor();
                                                        }
                                                    }else{
                                                        updateStatusButton.setVisibility(View.INVISIBLE);
                                                        setStatusTextColor();
                                                    }

                                                }
                                            });
                                            dialog.dismiss();
                                        });
                                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                    else {
                                        updateStatusButton.setVisibility(View.INVISIBLE);
                                        Toast.makeText(ShoppingListDetailedActivity.this, "Nothing to update", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                            giveUpButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(ShoppingListDetailedActivity.this).create();
                                    alertDialog.setMessage("Are you sure you want to give up?");
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            HashMap hashMap = new HashMap();
                                            hashMap.put("courierID", "");
                                            hashMap.put("status","posted");
                                            databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                                @Override
                                                public void onSuccess(Object o) {
                                                    Toast.makeText(getApplicationContext(),"You have given up", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(),MyResponsesActivity.class));
                                                    finish();
                                                }
                                            });
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();

                                }
                            });
                        }else{
                            updateStatusButton.setVisibility(View.INVISIBLE);
                            giveUpButton.setVisibility(View.INVISIBLE);
                        }
                    }catch (Exception e){
                        Log.e("Error", "Error while getting active value from the bundle");
                        updateStatusButton.setVisibility(View.INVISIBLE);
                        giveUpButton.setVisibility(View.INVISIBLE);
                    }

                    List<Item> items = sh.getItems();
                    int counter = 1;
                    StringBuilder builder = new StringBuilder();
                    for(Item i : items){
                        builder.append(counter);
                        builder.append(".  ");
                        builder.append(i.getName());
                        builder.append(" ");
                        builder.append(i.getAmount());
                        builder.append(" ");
                        builder.append(i.getMeasurement());
                        builder.append("\n");
                        counter++;
                    }

                    itemsTextView.setText(builder.toString());

                    itemsTextView.setMovementMethod(new ScrollingMovementMethod());



            }catch (Exception e){
                Log.e("Error", "Unable to get shopping list from bundle or render it to the view");
                cityTextView.setText(R.string.none);
                addressTextView.setText(R.string.none);
                shopTextView.setText(R.string.none);
                itemsTextView.setText(R.string.none);
                updateStatusButton.setVisibility(View.INVISIBLE);
                giveUpButton.setVisibility(View.INVISIBLE);
            }
        }

    }

    public void setStatusTextColor(){
        if(sh.getStatus().equals("accepted")){
            statusTextView.setTextColor(Color.parseColor("#66cc92"));
        }else if(sh.getStatus().equals("bought")){
            statusTextView.setTextColor(Color.parseColor("#267347"));
        }else if(sh.getStatus().equals("delivered")){
            statusTextView.setTextColor(Color.parseColor("#133924"));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews() {
        cityTextView = findViewById(R.id.cityTextView);
        addressTextView = findViewById(R.id.addressTextView);
        itemsTextView = findViewById(R.id.itemsTextView);
        shopTextView = findViewById(R.id.shopTextView);
        statusTextView = findViewById(R.id.statusTextView);
        nameSurnameTextView = findViewById(R.id.nameSurnameView);

        updateStatusButton = findViewById(R.id.updateStatusButton);
        giveUpButton = findViewById(R.id.giveUpButton);
    }

    private void initFirebaseConnection() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Advertisements").child(sh.getId());
        databaseReference.keepSynced(true);
    }

    //pozycje menu sa widoczne lub nie w zaleznosci od tego czy to jest wlasne ogloszenie czy cudze
    //tak zeby mozna bylo edytowac lub usuwac tylko swoje ogloszenia albo odpowiedziec na obce
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailed_menu,menu);
        if(getIntent().getExtras()!=null){
            try {
                boolean edit = getIntent().getExtras().getBoolean("edit");
                if(edit){
                    menu.getItem(3).setVisible(false);
                }
                else{
                    menu.getItem(0).setVisible(false);
                    menu.getItem(1).setVisible(false);
                    menu.getItem(2).setVisible(false);
                }
            }catch (Exception e){
                Log.e("Error", "Error while getting edit boolean variable");
                menu.getItem(0).setVisible(false);
                menu.getItem(1).setVisible(false);
                menu.getItem(2).setVisible(false);
                menu.getItem(3).setVisible(false);
            }

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //usuwanie ogloszenia
            case R.id.deleteOption:{
                if(sh!=null && !sh.getStatus().equals("accepted") && !sh.getStatus().equals("bought")){
                    AlertDialog alertDialog = new AlertDialog.Builder(ShoppingListDetailedActivity.this).create();
                    alertDialog.setMessage("Are you sure you want to delete the post?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.removeValue();
                            startActivity(new Intent(getApplicationContext(),MyShLActivity.class));
                            alertDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Shopping list deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();

                }else{
                    Toast.makeText(ShoppingListDetailedActivity.this, "You can not delete this post as it is being proceeded", Toast.LENGTH_SHORT).show();
                }

                break;
            }

            //edytowanie ogloszenia
            case R.id.editOption:{
                Toast.makeText(getApplicationContext(), "Edit pressed", Toast.LENGTH_SHORT).show();
                if(sh!=null){
                    Intent intent = new Intent(this, AddShoppingListActivity.class);
                    intent.putExtra("sh",sh);
                    startActivity(intent);
                }
                break;
            }
            //uzytkownik odpowiada na cudze ogloszenia, w ogloszeniu zmienia sie pole courierID na wartosc id
            // biezaczego uzytkownika a status zmienia sie na accepted by osoba wstawiajaca cogloszenie
            // widziala to w statusach swoich ogloszen
            case R.id.respondOption:{
                if(sh!=null){
                    if (sh.getCourierID().isEmpty()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(ShoppingListDetailedActivity.this).create();
                        alertDialog.setMessage("Do you want to respond to the post?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap hashMap = new HashMap();
                                hashMap.put("courierID", firebaseUser.getUid());
                                hashMap.put("status", "accepted");
                                databaseReference.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(getApplicationContext(), "Challenge is taken!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("userid", sh.getUserID());
                                        intent.putExtras(bundle);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userid", sh.getUserID());
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                    }

                break;
            }
            //usuwanie realizowanego ogloszenia oraz ocenianie dostawcy
            case R.id.doneOption:{
                if(sh!=null){
                    AlertDialog alertDialog = new AlertDialog.Builder(ShoppingListDetailedActivity.this).create();
                    alertDialog.setMessage("Are you sure you want to mark the post as done? If yes the post will be removed.");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseReference.removeValue();
                            if(!Objects.equals(sh.getCourierID(), "")) {
                                Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                                intent.putExtra("courierId", sh.getCourierID());
                                startActivity(intent);
                            }
                            else {
                                startActivity(new Intent(getApplicationContext(),MyShLActivity.class));
                                finish();
                            }
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}