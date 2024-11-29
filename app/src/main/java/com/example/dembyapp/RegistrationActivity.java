package com.example.dembyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private EditText user_name_field;
    private EditText email_field;
    private EditText first_password_field;
    private EditText second_password_field;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView go_log_button = findViewById(R.id.go_log_button);
        go_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogActivity(v);
            }
        });

        user_name_field = findViewById(R.id.user_name_field);
        email_field = findViewById(R.id.email_field);
        first_password_field = findViewById(R.id.first_password_field);
        second_password_field = findViewById(R.id.second_password_field);
    }

    public void goToLogActivity(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void RegButton_Click(View v){

    //Различные проверки корректности ввода

        //Проверка корректности юзернейма
        if(user_name_field.getText().toString().contains(" ") || user_name_field.getText().toString().isEmpty()){
            showWarn("Некорректный юзернейм!");
            user_name_field.setTextColor(getResources().getColor(R.color.warn_red));
            return;
        }else{
            user_name_field.setTextColor(getResources().getColor(R.color.white));
        }

        //Проверка корректности паролей
        if(first_password_field.getText().toString().contains(" ") || second_password_field.getText().toString().contains(" ") ||
                first_password_field.getText().toString().isEmpty() || second_password_field.getText().toString().isEmpty() ){
            showWarn("Неккоректный пароль!");
            first_password_field.setTextColor(getResources().getColor(R.color.warn_red));
            second_password_field.setTextColor(getResources().getColor(R.color.warn_red));
            return;
        }else{
            first_password_field.setTextColor(getResources().getColor(R.color.white));
            second_password_field.setTextColor(getResources().getColor(R.color.white));
        }

        //Проверка на существование юзернейма
        if(isUsernameExists(user_name_field.getText().toString())){
            showWarn("Юзернейм занят!");
            user_name_field.setTextColor(getResources().getColor(R.color.warn_red));
            return;
        }else{
            user_name_field.setTextColor(getResources().getColor(R.color.white));
        }

        //Проверка на корректный вид почты
        if(!isValidEmail(email_field.getText().toString())) {
            showWarn("Некорректная почта!");
            email_field.setTextColor(getResources().getColor(R.color.warn_red));
            return;
        }else{
            email_field.setTextColor(getResources().getColor(R.color.white));
        }

        //Пароли

        //Проверка на равенство введеных паролей
        if(!first_password_field.getText().toString().equals(second_password_field.getText().toString())){
            showWarn("Пароли не совпадают!");
            first_password_field.setTextColor(getResources().getColor(R.color.warn_red));
            second_password_field.setTextColor(getResources().getColor(R.color.warn_red));
            return;
        }else{
            first_password_field.setTextColor(getResources().getColor(R.color.white));
            second_password_field.setTextColor(getResources().getColor(R.color.white));
        }

        //Валидация пароля
        if(first_password_field.getText().toString().isEmpty() || first_password_field.getText().toString().contains(" ") ||
            first_password_field.getText().toString().length() < 8){
            showWarn("Некорректный пароль!");
            first_password_field.setTextColor(getResources().getColor(R.color.warn_red));
            second_password_field.setTextColor(getResources().getColor(R.color.warn_red));
            return;
        }else{
            first_password_field.setTextColor(getResources().getColor(R.color.white));
            second_password_field.setTextColor(getResources().getColor(R.color.white));
        }

        //TODO создание аккаунта в БД

        showWarn("Аккаунт создан!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static boolean isUsernameExists(String username) {
        //TODO поиск юзернейма в БД
        return false;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void showWarn(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}