package com.example.dembyapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Data.DatabaseHandler;
import Model.ArchiveMutual;
import Model.Profile;


public class MutualLikeFragment extends Fragment {

    private String userName;
    private DatabaseHandler databaseHandler;
    private static List<String> profilesToShow;
    private ImageView profile_image, inst_button, tg_button;
    private static int numOfProf = 0;
    private static boolean isExist = false;
    String urlTg,urlInst;
    private TextView your_name_field, your_age_field, your_city_field, profile_description_field;
    private Button next_profile_button;

    public MutualLikeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mutual_like, container, false);

        databaseHandler = new DatabaseHandler(requireContext());

        inst_button = view.findViewById(R.id.inst_button);
        tg_button = view.findViewById(R.id.tg_button);

        your_name_field = view.findViewById(R.id.your_name_field);
        your_age_field = view.findViewById(R.id.your_age_field);
        your_city_field = view.findViewById(R.id.your_city_field);
        profile_description_field = view.findViewById(R.id.profile_description_field);
        profile_image = view.findViewById(R.id.profile_image);
        next_profile_button = view.findViewById(R.id.next_profile_button);

        profilesToShow = new ArrayList<>();
        numOfProf = 0;

        userName = getUN();
        Profile userProfile = databaseHandler.getProfileByName(userName);

        if(userProfile == null){
            isExist = false;
            profile_description_field.setText("Сначала создайте анкету!");
        }else{
            isExist = true;

            if(databaseHandler.getArchiveMutual(userName) == null) {
                ArchiveMutual archiveMutual = new ArchiveMutual(userName, "");
                databaseHandler.newArchiveMutual(archiveMutual);
            }

            String[] likesBy = userProfile.getLikedBy().split("\\$");

            for (String user : likesBy) {
                Log.d("Mutual", user);
                if (isMutual(user)) {
                    if(!existInArchive(user)) {
                        profilesToShow.add(user);
                    }
                }
            }

            if(!profilesToShow.isEmpty()) {
                showProfile(databaseHandler.getProfileByName(profilesToShow.get(numOfProf)));
            }
        }

        inst_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlInst));
                startActivity(intent);
            }
        });

        tg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlTg));
                startActivity(intent);
            }
        });

        next_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isExist){return;}

                ArchiveMutual archiveMutual = databaseHandler.getArchiveMutual(userName);
                String tmpStrArchive = archiveMutual.getMutualNames() + profilesToShow.get(numOfProf) + "$";
                archiveMutual.setMutualNames(tmpStrArchive);

                databaseHandler.updateArchive(archiveMutual);

                numOfProf++;

                if(numOfProf < profilesToShow.size()){
                    showProfile(databaseHandler.getProfileByName(profilesToShow.get(numOfProf)));
                }
                else{
                    your_name_field.setText("Demby");
                    your_age_field.setText("");
                    your_city_field.setText("Минск");
                    profile_description_field.setText("Пока что это все");

                    profile_image.setImageResource(R.mipmap.demeby_icon_round);

                    urlInst = "https://instagram.com/";
                    urlTg = "https://t.me/";
                }
            }
        });

        return view;
    }

    public void showProfile(Profile profileToShow){
        your_name_field.setText(profileToShow.getRealName());
        your_age_field.setText(String.valueOf(profileToShow.getAge()));
        your_city_field.setText(profileToShow.getCity());
        profile_description_field.setText(profileToShow.getDescription());

        Bitmap bitmap = BitmapFactory.decodeByteArray(profileToShow.getImage(), 0, profileToShow.getImage().length);
        profile_image.setImageBitmap(bitmap);

        urlInst = "https://www.instagram.com/" + profileToShow.getInstagram();
        urlTg = "https://t.me/" + profileToShow.getTelegram();
    }

    private boolean isMutual(String user){
        if(user == null){
            return false;
        }

        String[] tmp = databaseHandler.getProfileByName(user).getLikedBy().split("\\$");

        for (String us : tmp) {
            if (us.equals(userName)) {
                return true;
            }
        }
        return false;
    }

    private boolean existInArchive(String user){
        String[] tmp = databaseHandler.getArchiveMutual(userName).getMutualNames().split("\\$");

        for (String us : tmp) {
            if (us.equals(user)) {
                return true;
            }
        }
        return false;
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

    public void showWarn(String text){
        Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show();
    }
}