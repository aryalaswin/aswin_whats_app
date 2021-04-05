package com.aryal.aswinwhatsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aryal.aswinwhatsapp.R;
import com.aryal.aswinwhatsapp.adaptor.UserAdaptor;
import com.aryal.aswinwhatsapp.databinding.FragmentChatBinding;
import com.aryal.aswinwhatsapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatFragment extends Fragment {

    public ChatFragment(){
}
FragmentChatBinding binding;
    ArrayList<Users>list=new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(inflater,container,false);
        final UserAdaptor adaptor=new UserAdaptor(list, getContext());
        binding.chatRecyclerView.setAdapter(adaptor);
        database=FirebaseDatabase.getInstance();
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                    list.add(users);}
                }
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}