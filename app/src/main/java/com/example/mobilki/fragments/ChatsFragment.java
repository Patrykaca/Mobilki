package com.example.mobilki.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobilki.R;
import com.example.mobilki.User;
import com.example.mobilki.adapters.UserAdapter;
import com.example.mobilki.classes.Chat;
import com.example.mobilki.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ChatsFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String title;
    private int page;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
    private List<String> usersId;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private List<String> recId = new ArrayList<>();



    public static ChatsFragment newInstance(String param1, int param2) {
        ChatsFragment fragment = new ChatsFragment();
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
            page = getArguments().getInt(ARG_PARAM2, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.chats_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        usersId = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersId.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Chat _chat = snapshot1.getValue(Chat.class);

                    assert _chat != null;
                    if(Objects.equals(firebaseUser.getUid(), _chat.getSender())) {
                        usersId.add(_chat.getReceiver());
                    }

                    if(Objects.equals(firebaseUser.getUid(), _chat.getReceiver())) {
                        usersId.add(_chat.getSender());
                        if (!_chat.isIsseen()) {
                            recId.add(_chat.getSender());
                        }
                    }
                }

                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        updateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void readChats() {

        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    for(String id : usersId) {
                        assert user != null;
                        if(Objects.equals(id, user.getId())) {
                            if(users.size() != 0) {
                                boolean flag = true;
                                for(int i=0; i<users.size(); i++) {
                                    if(Objects.equals(user.getId(), users.get(i).getId())) {
                                        flag = false;
                                        break;
                                    }
                                }
                                if(flag) {
                                    users.add(user);
                                }
                            }
                            else {
                                users.add(user);
                            }
                        }
                    }
                }
                if (!recId.isEmpty()) {
                    List<Object> newRec = recId.stream().distinct().collect(Collectors.toList());
                    List<User> newUsers = new ArrayList<>();
                    for (User user : users) {
                        for (int i = 0; i < newRec.size(); i++) {
                            if (user.getId().equals(newRec.get(i))) {
                                newUsers.add(user);
                            }
                        }
                    }

                    boolean flag = false;
                    for (int i = 0; i < users.size(); i++) {
                        for (int j = 0; j < newUsers.size(); j++) {
                            if (users.get(i).getId().equals(newUsers.get(j).getId())) {
                                flag = true;
                                break;
                            }
                            if (flag) {
                                newUsers.add(users.get(i));
                            }
                        }
                    }
                    userAdapter = new UserAdapter(getContext(), newUsers, true);
                } else {
                    userAdapter = new UserAdapter(getContext(), users, true);
                }

                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(firebaseUser.getUid()).setValue(token1);
    }

}