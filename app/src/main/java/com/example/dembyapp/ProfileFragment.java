package com.example.dembyapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    private TextView hi_text, your_name_field, your_age_field, your_city_field, profile_description_field;
    private ImageView profile_image;
    Button delete_profile_button;


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
        your_name_field = view.findViewById(R.id.your_name_field);
        your_age_field = view.findViewById(R.id.your_age_field);
        your_city_field = view.findViewById(R.id.your_city_field);
        profile_description_field = view.findViewById(R.id.profile_description_field);
        profile_image = view.findViewById(R.id.profile_image);
        delete_profile_button = view.findViewById(R.id.delete_profile_button);

        userName = getUN();

        databaseHandler = new DatabaseHandler(requireContext());

        Profile userProfile = new Profile();
        userProfile = databaseHandler.getProfileByName(userName);
        String profileName = userProfile.getRealName();

        if(profileName.length() > 7)
            hi_text.setTextSize(26);

        String hiText = "Привет " + profileName + "!";
        hi_text.setText(hiText);

        your_name_field.setText(userProfile.getRealName());
        your_age_field.setText(String.valueOf(userProfile.getAge()));
        your_city_field.setText(userProfile.getCity());
        profile_description_field.setText(userProfile.getDescription());

        Bitmap bitmap = BitmapFactory.decodeByteArray(userProfile.getImage(), 0, userProfile.getImage().length);
        profile_image.setImageBitmap(bitmap);

        delete_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });

        return view;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Подтверждение удаления");
        builder.setMessage("Вы действительно хотите удалить анкету?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProfile();
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });

        dialog.show();
    }

    private void deleteProfile(){
        databaseHandler.deleteProfile(userName);

        Fragment fr = new NewProfileFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_field, fr);
        transaction.addToBackStack(null);
        transaction.commit();
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