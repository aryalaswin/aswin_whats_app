package com.aryal.aswinwhatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aryal.aswinwhatsapp.databinding.ActivitySettingsBinding;
import com.aryal.aswinwhatsapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;

    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivitySettingsBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status=binding.etStatus.getText().toString();
                String username=binding.etUsername.getText().toString();
                HashMap<String,Object> obj=new HashMap<>();
                obj.put("username",username);
                obj.put("status",status);
                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users=snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avator).into(binding.profileImage);
                        binding.etStatus.setText(users.getStatus());
                        binding.etUsername.setText(users.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri sFile =data.getData();
            binding.profileImage.setImageURI(sFile);
            final StorageReference reference=storage.getReference().child("profilePic").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                       database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                               .child("profilePic").setValue(uri.toString());
                            Toast.makeText(SettingsActivity.this, "Profile Picture uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

        }
    }
}