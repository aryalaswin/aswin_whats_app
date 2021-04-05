package com.aryal.aswinwhatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.aryal.aswinwhatsapp.adaptor.FragmentAdaptor;
import com.aryal.aswinwhatsapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        binding.viewPager.setAdapter(new FragmentAdaptor(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
               Intent intent2=new Intent(MainActivity.this, SettingsActivity.class);
               startActivity(intent2);
                break;
            case R.id.logout:
                Toast.makeText(this, "Logout CLicked", Toast.LENGTH_SHORT).show();
                auth.signOut();

                Intent intent=new Intent(MainActivity.this,SignInActivity.class);
                startActivity(intent);

                break;
            case R.id.groupchat:
                Toast.makeText(this, "You clicked on Group Chat", Toast.LENGTH_SHORT).show();

                Intent intent1=new Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}