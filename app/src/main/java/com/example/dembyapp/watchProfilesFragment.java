package com.example.dembyapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import Data.DatabaseHandler;
import Model.Profile;


public class watchProfilesFragment extends Fragment {

    private DatabaseHandler databaseHandler;
    private static String userName;
    private static int numOfProf = 0;
    private TextView hi_text, your_name_field, your_age_field, your_city_field, profile_description_field;
    private ImageView profile_image, like_button, dislike_button;
    private List<Profile> profilesToShow;
    private static boolean isProfileExist = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_profiles, container, false);

        databaseHandler = new DatabaseHandler(requireContext());
        userName = getUN();

        your_name_field = view.findViewById(R.id.your_name_field);
        your_age_field = view.findViewById(R.id.your_age_field);
        your_city_field = view.findViewById(R.id.your_city_field);
        profile_description_field = view.findViewById(R.id.profile_description_field);
        profile_image = view.findViewById(R.id.profile_image);

        like_button = view.findViewById(R.id.like_button);
        dislike_button = view.findViewById(R.id.dislike_button);

        profilesToShow = databaseHandler.getProfiles(userName);

        if (profilesToShow == null) {
            isProfileExist = false;
            profile_description_field.setText("Для начала пользования приложением создайте анкету:)");
        }

        else if(numOfProf >= profilesToShow.size()) {
            isProfileExist = false;
            profile_description_field.setText("Анкет больше нет(");
        }
        else{
            isProfileExist = true;
            nextProfile();
        }


        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isProfileExist) {

                    String likesTmp = profilesToShow.get(numOfProf).getLikedBy();
                    likesTmp += userName + "$";
                    databaseHandler.getProfileByName(userName).setLikedBy(likesTmp);

                    numOfProf++;

                    changeColor(dislike_button);
                    nextProfile();
                }
            }
        });

        dislike_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isProfileExist) {

                    String seenTmp = profilesToShow.get(numOfProf).getSeenBy();
                    seenTmp += userName + "$";
                    databaseHandler.getProfileByName(userName).setLikedBy(seenTmp);

                    numOfProf++;

                    changeColor(dislike_button);
                    nextProfile();
                }
            }
        });

        return view;
    }

    private void changeColor(ImageView img) {
        AlphaAnimation animation = new AlphaAnimation(1f, 0.5f);
        animation.setDuration(200);
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);
        img.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                img.clearColorFilter();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    public void nextProfile(){
        if(numOfProf < profilesToShow.size()) {
            showWarn("К сожалению это все(");
            profile_description_field.setText("К сожалению пока что нет анкет(");
            return;
        }

        while(isBeen(profilesToShow.get(numOfProf))){

            numOfProf++;

            if(numOfProf < profilesToShow.size()){
                showWarn("К сожалению это все(");
                break;
            }
        }

        showProfile(profilesToShow.get(numOfProf));
    }

    public boolean isBeen(Profile profileToShow){
        String[] seenProfiles = profileToShow.getSeenBy().split("\\$");
        for(String byWhom : seenProfiles){
            if(byWhom.equals(userName)){
                return true;
            }
        }

        String[] likedProfiles = profileToShow.getLikedBy().split("\\$");
        for(String byWhom : likedProfiles){
            if(byWhom.equals(userName)){
                return true;
            }
        }

        return false;
    }

    public void showProfile(Profile profileToShow){

        your_name_field.setText(profileToShow.getRealName());
        your_age_field.setText(String.valueOf(profileToShow.getAge()));
        your_city_field.setText(profileToShow.getCity());
        profile_description_field.setText(profileToShow.getDescription());

        Bitmap bitmap = BitmapFactory.decodeByteArray(profileToShow.getImage(), 0, profileToShow.getImage().length);
        profile_image.setImageBitmap(bitmap);
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

            Log.i("Profile watching","Uploaded data for main: " + stringBuffer.toString());

            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showWarn(String text){
        Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show();
    }
}