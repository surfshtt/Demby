package com.example.dembyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Data.DatabaseHandler;
import Model.Profile;


public class ProfileFragment extends Fragment {

    private DatabaseHandler databaseHandler;
    private static String userName;
    private TextView hi_text, here_is_your_profile_text;



    public ProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        hi_text = view.findViewById(R.id.hi_text);
        here_is_your_profile_text = view.findViewById(R.id.here_is_your_profile_text);
        userName = getUN();

        databaseHandler = new DatabaseHandler(requireContext());

        Profile userProfile = new Profile();
        userProfile = databaseHandler.getProfileByName(userName);
        String profileName = userProfile.getRealName();

        if(profileName.length() > 7)
            hi_text.setTextSize(26);

        String hiText = "Привет " + profileName + "!";
        hi_text.setText(hiText);

        return view;
    }

    public String getUN(){
        try {
            FileInputStream fileInput = requireContext().openFileInput("userNameData.txt");
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