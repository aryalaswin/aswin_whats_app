package com.aryal.aswinwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aryal.aswinwhatsapp.adaptor.ChatAdaptor;
import com.aryal.aswinwhatsapp.databinding.ActivityGroupChatBinding;
import com.aryal.aswinwhatsapp.models.Messages;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {
    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<Messages> messages = new ArrayList<>();
        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.username.setText("Firends Group");
        final ChatAdaptor adaptor = new ChatAdaptor(messages, this);
        binding.chatRecyclerView.setAdapter(adaptor);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   Messages model=dataSnapshot.getValue(Messages.class);
                   messages.add(model);

                }
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=binding.etMessage.getText().toString();
                final  Messages model=new Messages(senderId,message);
                model.setTimeStramp(new Date().getTime());
                binding.etMessage.setText("");
                database.getReference().child("Group Chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
    }
}