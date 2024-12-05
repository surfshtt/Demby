package com.example.dembyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Data.DatabaseHandler;
import Model.Profile;


public class NewProfileFragment extends Fragment {

    AutoCompleteTextView town_field;
    String userName;
    DatabaseHandler databaseHandler;
    Profile profileForCreate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_profile, container, false);

        town_field = view.findViewById(R.id.town_field);
        userName = getUN();
        databaseHandler = new DatabaseHandler(requireContext());
        profileForCreate = new Profile();

        String[] items = {"Минск","Minsk", "Брест", "Могилев", "Витебск", "Гомель", "Гродно", "Варшава", "Москва", "Киев", "Таллин"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
        town_field.setAdapter(adapter);

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

            Log.i("Profile","Uploaded data for newAccount: " + stringBuffer.toString());

            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}