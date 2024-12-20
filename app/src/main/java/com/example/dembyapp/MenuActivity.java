package com.example.dembyapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.dembyapp.databinding.ActivityMenuBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Data.DatabaseHandler;

public class MenuActivity extends AppCompatActivity {

   // ActivityMenuBinding binding;
    String userName;
    DatabaseHandler databaseHandler;
    BottomNavigationView menu_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menu_field = findViewById(R.id.menu_field);
        databaseHandler = new DatabaseHandler(this);

        userName = getUN();

        menu_field.setSelectedItemId(R.id.watchProfilesFragment);
        replaceFragment(new watchProfilesFragment());

        menu_field.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.mutualLikeFragment){
                replaceFragment(new MutualLikeFragment());
            }
            if(item.getItemId() == R.id.watchProfilesFragment){
                replaceFragment(new watchProfilesFragment());
            }
            if(item.getItemId() == R.id.profileFragment){
                if(databaseHandler.getProfileByName(userName)==null) {
                    replaceFragment(new NewProfileFragment());
                }
                else {
                    replaceFragment(new ProfileFragment());
                }
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fs = fragmentManager.beginTransaction();
        fs.replace(R.id.frame_field, fragment);
        fs.commit();
    }

    public String getUN(){
        try {
            FileInputStream fileInput = openFileInput("userNameData.txt");
            InputStreamReader reader = new InputStreamReader(fileInput);
            BufferedReader bR = new BufferedReader(reader);

            StringBuilder stringBuffer = new StringBuilder();
            String lines = "";
            while ((lines = bR.readLine()) != null) {
                stringBuffer.append(lines).append("\n");
            }

            Log.i("Profile","Uploaded data for main: " + stringBuffer.toString());

            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}