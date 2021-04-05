package com.aryal.aswinwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.aryal.aswinwhatsapp.adaptor.ChatAdaptor;
import com.aryal.aswinwhatsapp.databinding.ActivityChatDetailBinding;
import com.aryal.aswinwhatsapp.models.Messages;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatDetailBinding.inflate(getLayoutInflater());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        final String senderId=auth.getUid();
        String receiverId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("username");
        String profilePic=getIntent().getStringExtra("profilePic");
        binding.username.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avator).into(binding.profileImage);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<Messages>messages=new ArrayList<>();
        final ChatAdaptor chatAdaptor=new ChatAdaptor(messages,this);
        final ChatAdaptor ca=new ChatAdaptor(messages,this,receiverId);
        binding.chatRecyclerView.setAdapter(chatAdaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom=senderId+receiverId;
        final String receiverRoom=receiverId+senderId;
        database.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Messages messages1=snapshot1.getValue(Messages.class);
                    messages1.setMessageId(snapshot1.getKey());
                   messages.add(messages1);
                }
                chatAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String message=  binding.etMessage.getText().toString();
              final Messages messages=new Messages(senderId,message);
              messages.setTimeStramp(new Date().getTime());
              binding.etMessage.setText("");
              database.getReference().child("chats").child(senderRoom).push()
              .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void aVoid) {
                      database.getReference().child("chats").child(receiverRoom).push().setValue(messages)
                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void aVoid) {

                                  }
                              });
                  }
              });

            }
        });

    }
}