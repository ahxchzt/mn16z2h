package com.example.ble;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import org.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register_Activity extends AppCompatActivity {
    private EditText register_name;
    private EditText register_password1;
    private EditText register_password2;
    private EditText register_email;
    private EditText register_teacher_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_name = (EditText) findViewById(R.id.teacher_name);
        register_password1 = (EditText) findViewById(R.id.register_password1);
        register_password2 = (EditText) findViewById(R.id.register_password2);
        register_email = (EditText) findViewById(R.id.register_email);
        register_teacher_id = (EditText) findViewById(R.id.register_teacher_id);
        Button register_button = (Button) findViewById(R.id.register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    private void register() {
        final String name = register_name.getText().toString();
        final String pw1 = register_password1.getText().toString();
        final String pw2 = register_password2.getText().toString();
        final String email = register_email.getText().toString();
        final String teacher_Id = register_teacher_id.getText().toString();
//      Users need to fill in the information as required
        if(email.isEmpty()){
            register_email.setError("Please input your email！");
            register_email.setFocusable(true);;
        } else if (!isEmailValid(email)) {
            register_email.setError("Unavailable email address，please input again！");
            register_email.setText("");
            register_email.setFocusable(true);
        }else if (name.isEmpty()) {
            register_name.setError("Please input your name!");
            register_name.setFocusable(true);
        }else if(pw1.isEmpty()){
            register_password1.setError("Please input your password！");
            register_password1.setFocusable(true);
        }else if(!pw1.equals(pw2)){
            register_password2.setError("The two password are not the same");
            register_password2.setText("");
            register_password2.setFocusable(true);
        }else {
//          Send a request to the server to add teacher information
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        RequestBody register_request = new FormBody.Builder().add("Email", email).add("Password", pw1).add("TeacherId", teacher_Id).add("Name",name).build();
                        Request request = new Request.Builder().url("http://192.168.5.9:8080/RegisterServlet").post(register_request).build();
                        Response response = client.newCall(request).execute();
                        String responseData = response.body().string();
                        parseJSON(responseData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
    private void parseJSON(String Data) {
        try {
            JSONObject jsonArray = new JSONObject(Data);
                JSONObject test = jsonArray.getJSONObject("params");
                String res = test.getString("Result");
                if(res.equals("success")){
                    Intent intent = new Intent(Register_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    } else{
//                    Account has been registered
                    Looper.prepare();
                    Toast toast=Toast.makeText(this,"The Account has been used!",Toast.LENGTH_SHORT);
                    toast.show();
                    Looper.loop();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        private boolean isEmailValid(String email) {
            return email.contains("@");
        }
}

