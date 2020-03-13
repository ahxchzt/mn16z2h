package com.example.ble;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;


public class Login_Activity extends AppCompatActivity {
    private EditText teacher_id;
    private EditText password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        teacher_id = (EditText) findViewById(R.id.teacher_id);
        password = (EditText) findViewById(R.id.password);
//      When the login button is clicked, the login function will be called
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });
    }
    private void login(){
//        Get the account number and password from the input field
        final String Teacher_id = teacher_id.getText().toString();
        final String pw = password.getText().toString();

    }
}
