package com.example.mobilki.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilki.R;
import com.example.mobilki.User;
import com.example.mobilki.activities.MessageActivity;
import com.example.mobilki.classes.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private boolean ischat;

    String theLastMessage;

    public UserAdapter(Context context, List<User> users, boolean ischat) {
        this.users = users;
        this.context = context;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = users.get(position);
        holder.username.setText(user.getFirstName());
        holder.userLastname.setText(user.getLastName());
        if (user.getImageUrl().equals("default")) {
            holder.profile_image.setImageResource(R.drawable.profile_icon);
        } else {
            Picasso.get().load(user.getImageUrl()).into(holder.profile_image);
        }

        if (ischat) {
            lastMessage(user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }

        if(ischat) {
            if(Objects.equals(user.getStatus(), "online")) {
                holder.img_online.setVisibility(View.VISIBLE);
                holder.img_offline.setVisibility(View.GONE);
            }
            else {
                holder.img_online.setVisibility(View.GONE);
                holder.img_offline.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.img_online.setVisibility(View.GONE);
            holder.img_offline.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public TextView userLastname;
        public ImageView profile_image;
        public ImageView img_online;
        public ImageView img_offline;
        public TextView last_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            userLastname = itemView.findViewById(R.id.userLastname);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_online = itemView.findViewById(R.id.img_status_online);
            img_offline = itemView.findViewById(R.id.img_status_offline);
            last_msg = itemView.findViewById(R.id.last_msg);

        }
    }

    private void lastMessage(final String userid, final TextView last_msg) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);

                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                        chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid()) ) {
                        theLastMessage = chat.getMessage();
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        last_msg.setText("No Message");
                        break;
                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
