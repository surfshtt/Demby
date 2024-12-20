package com.example.dembyapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Data.DatabaseHandler;
import Model.Profile;


public class EditProfileFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView image_from_user;
    private Button save_profile, upload_img_button;
    private EditText profile_name, profile_age, profile_tg, profile_inst, profile_description;
    private String userName;
    private Profile usersProfile;
    private DatabaseHandler databaseHandler;
    private AutoCompleteTextView town_field;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        profile_name = view.findViewById(R.id.profile_name);
        profile_age = view.findViewById(R.id.profile_age);
        town_field = view.findViewById(R.id.town_field);
        profile_description = view.findViewById(R.id.profile_description);
        image_from_user = view.findViewById(R.id.image_from_user);
        save_profile = view.findViewById(R.id.save_profile);
        upload_img_button = view.findViewById(R.id.upload_img_button);

        databaseHandler = new DatabaseHandler(requireContext());

        userName = getUN();
        usersProfile = databaseHandler.getProfileByName(userName);

        profile_name.setText(usersProfile.getRealName());
        profile_age.setText(String.valueOf(usersProfile.getAge()));
        town_field.setText(usersProfile.getCity());
        profile_description.setText(usersProfile.getDescription());

        Bitmap bitmap = BitmapFactory.decodeByteArray(usersProfile.getImage(), 0, usersProfile.getImage().length);
        image_from_user.setImageBitmap(bitmap);

        upload_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersProfile = collectData(usersProfile);
                if(!(usersProfile == null)){
                    Log.i("Profile","Access to creating a profile is apply");

                    databaseHandler.updateProfile(usersProfile);
                    Log.i("Profile","New profile for " + userName + " was created");
                    showWarn("Анкета сохранена!");

                    Fragment fr = new ProfileFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_field, fr);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else {
                    Log.i("Profile","Access to creating a profile is denied");
                    showWarn("Кажется вы что-то забыли");
                }
            }
        });


        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Разрешение получено, можно продолжить
            } else {
                requireActivity().finish();
            }
        }
    }

    // Метод для открытия выбора изображения
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            showWarn("Изображение загружено!");
            Log.i("Profile", "Изображение загружено успешно");

            image_from_user.setImageURI(imageUri);
        }
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

    private Profile collectData(Profile profile){

        if(!profile_name.getText().toString().contains(" ") && !profile_name.getText().toString().isEmpty() && profile_name.getText().toString().length() > 2)
            profile.setRealName(profile_name.getText().toString());
        else
            return null;

        if(!profile_age.getText().toString().contains(" ")
                && !profile_age.getText().toString().isEmpty()
                && Integer.parseInt(profile_age.getText().toString()) >= 18
                && Integer.parseInt(profile_age.getText().toString()) <= 99)
            profile.setAge(Integer.parseInt(profile_age.getText().toString()));
        else
            return null;

        if(!town_field.getText().toString().contains(" ") && !town_field.getText().toString().isEmpty() && town_field.getText().toString().length() > 2)
            profile.setCity(town_field.getText().toString());
        else
            return null;

        profile.setDescription(profile_description.getText().toString());

        profile.setSeenBy("");
        profile.setLikedBy("");

        Drawable drawable = image_from_user.getDrawable();
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        profile.setImage(stream.toByteArray());

        return profile;
    }

    public void showWarn(String text){
        Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show();
    }
}