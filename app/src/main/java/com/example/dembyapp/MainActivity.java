package com.example.dembyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Data.DatabaseHandler;
import Model.User;

public class MainActivity extends AppCompatActivity {

    TextView go_reg_button;
    EditText user_name_field, password_field;

    DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        go_reg_button = findViewById(R.id.go_reg_button);
        go_reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegActivity(v);
            }
        });

        user_name_field = findViewById(R.id.user_name_field);
        password_field = findViewById(R.id.password_field);

        databaseHandler = new DatabaseHandler(this);
    }

    public void goToRegActivity(View v){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void LogButton_Click(View v) {
        String userName = user_name_field.getText().toString();
        String password = password_field.getText().toString();

        if(fieldValidation(userName,password)){
            showWarn("Вход Успешен!");
            Log.i("Login", "Correct data. Username: " + userName + " Password: " + password);

            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        else{
            showWarn("Некорректные данные!");
            Log.i("Login", "Invalid data. Username: " + userName + " Password: " + password);
        }
    }

    private boolean fieldValidation(String userName, String password){

        if(userName.isEmpty() || userName.contains(" ") || userName.contains("SELECT") || userName.contains("WHERE"))
            return false;

        if(databaseHandler.getUser(userName) == null)
            return false;

        User userData = databaseHandler.getUser(userName);

        if(password.isEmpty() || password.contains(" ") || password.length() < 8)
            return false;

        return userData.getPassword().equals(password);
    }

    public void showWarn(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}