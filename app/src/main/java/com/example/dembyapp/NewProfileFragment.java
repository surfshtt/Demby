package com.example.dembyapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import Data.DatabaseHandler;
import Model.Profile;

import android.Manifest;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


public class NewProfileFragment extends Fragment {

    private static final int REQUEST_CODE = 1;
    private static final int PICK_IMAGE_REQUEST = 1;
    private AutoCompleteTextView town_field;
    private String userName;
    private DatabaseHandler databaseHandler;

    private Button upload_img_button, save_profile;
    private ImageView image_from_user;
    private EditText profile_name, profile_age, profile_tg, profile_inst, profile_description;
    private ChipGroup gender1, gender2;

    final int[] genders = new int[] {R.id.gender_chip1,R.id.gender_chip2,R.id.gender_chip3};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_profile, container, false);

        image_from_user = view.findViewById(R.id.image_from_user);
        save_profile = view.findViewById(R.id.save_profile);

        town_field = view.findViewById(R.id.town_field);

        profile_name = view.findViewById(R.id.profile_name);
        profile_age = view.findViewById(R.id.profile_age);
        profile_tg = view.findViewById(R.id.profile_tg);
        profile_inst = view.findViewById(R.id.profile_inst);
        profile_description = view.findViewById(R.id.profile_description);
        gender1 = view.findViewById(R.id.chipGroup_genders1);
        gender2 = view.findViewById(R.id.chipGroup_genders2);

        upload_img_button = view.findViewById(R.id.upload_img_button);

        userName = getUN();
        databaseHandler = new DatabaseHandler(requireContext());

        String[] items = {"Минск", "Брест", "Могилев", "Витебск", "Гомель", "Гродно", "Варшава", "Москва", "Киев", "Таллин"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, items);
        town_field.setAdapter(adapter);

        upload_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profileForCreate = collectData();
                if(!(profileForCreate == null)){
                    Log.i("Profile","Access to creating a profile is apply");

                    databaseHandler.newProfile(profileForCreate);

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

            Log.i("Profile","Uploaded data for new profile owner: " + stringBuffer.toString());

            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    private Profile collectData(){

        Profile profile = new Profile();
        profile.setOwnerId(userName);

        if(!profile_name.getText().toString().contains(" ") && !profile_name.getText().toString().isEmpty() && profile_name.getText().toString().length() > 2)
            profile.setRealName(profile_name.getText().toString());
        else
            return null;

        if (gender1.getCheckedChipId() != -1 && gender2.getCheckedChipId() != -1) {
            int selectedGenderId = gender1.getCheckedChipId();
            int selectedLookingId = gender2.getCheckedChipId();

            if (selectedGenderId == R.id.gender_chip1) {
                profile.setGender("m");
            } else if (selectedGenderId == R.id.gender_chip2) {
                profile.setGender("f");
            } else if (selectedGenderId == R.id.gender_chip3) {
                profile.setGender("n");
            }

            if (selectedLookingId == R.id.gender_chip11) {
                profile.setGender_looking("m");
            } else if (selectedLookingId == R.id.gender_chip22) {
                profile.setGender_looking("f");
            } else if (selectedLookingId == R.id.gender_chip33) {
                profile.setGender_looking("n");
            }
        } else {
            return null;
        }

        if(!profile_age.getText().toString().contains(" ") && !profile_age.getText().toString().isEmpty())
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

        if(!profile_tg.getText().toString().isEmpty() && !profile_inst.getText().toString().isEmpty()){
            profile.setInstagram(profile_inst.getText().toString());
            profile.setTelegram(profile_tg.getText().toString());
        }
        else{
            return null;
        }

        return profile;
    }

    public void showWarn(String text){
        Toast.makeText(requireContext(),text,Toast.LENGTH_SHORT).show();
    }
}