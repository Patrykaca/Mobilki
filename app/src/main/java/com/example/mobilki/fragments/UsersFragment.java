package com.example.mobilki.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mobilki.R;
import com.example.mobilki.User;
import com.example.mobilki.adapters.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class UsersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String title;
    private int page;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private EditText search_users;


    public static UsersFragment newInstance(String param1, int param2) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            page = getArguments().getInt(ARG_PARAM2, 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        readUsers();
        search_users = view.findViewById(R.id.search_users);
        search_users.setText("Jan Pa");
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void filterUsers(final String s) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("firstname").startAt(s).endAt(s + "\uf8ff");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userList.clear();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    User _user = dataSnapshot.getValue(User.class);
//
//                    if(!Objects.equals(firebaseUser.getUid(), _user.getId())) {
//                        userList.add(_user);
//                    }
//                }
//                userAdapter = new UserAdapter(getContext(), userList, false);
//                recyclerView.setAdapter(userAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    User _user = snapshot1.getValue(User.class);
                    if(!Objects.equals(_user.getId(), firebaseUser.getUid())) {
                        if(!s.contains(" ")) {
                            if(_user.getFirstName().toLowerCase().contains(s) || _user.getLastName().toLowerCase().contains(s)) {
                                userList.add(_user);
                            }
                        }
                        else {
                            String [] parts = s.split(" ");
                            try {
                                if ((_user.getFirstName().toLowerCase().contains(parts[0]) && _user.getLastName().toLowerCase().contains(parts[1]))
                                || (_user.getFirstName().toLowerCase().contains(parts[1]) && _user.getLastName().toLowerCase().contains(parts[0]))) {
                                    userList.add(_user);
                                }
                            }catch (ArrayIndexOutOfBoundsException e) {
                                if(_user.getFirstName().toLowerCase().contains(parts[0])
                                        || _user.getLastName().toLowerCase().contains(parts[0])) {
                                    userList.add(_user);
                                }
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), userList, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(Objects.equals(search_users.getText().toString(), "")) {
                    userList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren()) {
                        User user = snapshot1.getValue(User.class);


                        assert firebaseUser != null;
                        assert user != null;
                        if(!Objects.equals(user.getId(),firebaseUser.getUid())) {
                            userList.add(user);
                        }
                    }

                    userAdapter = new UserAdapter(getContext(), userList, false);
                    recyclerView.setAdapter(userAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}