package com.example.ble;

import android.content.Intent;
import android.view.View;
import android.os.Bundle;
<<<<<<< HEAD
<<<<<<< HEAD
import android.os.Looper;
import org.json.JSONObject;
import okhttp3.FormBody;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.OkHttpClient;
=======
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
=======
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;


public class Login_Activity extends AppCompatActivity {
    private EditText teacher_id;
    private EditText password;
    private Button login;
<<<<<<< HEAD
<<<<<<< HEAD
    private Button register;
=======
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
=======
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
<<<<<<< HEAD
<<<<<<< HEAD
        register = (Button) findViewById(R.id.register);
=======
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
=======
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
        teacher_id = (EditText) findViewById(R.id.teacher_id);
        password = (EditText) findViewById(R.id.password);
//      When the login button is clicked, the login function will be called
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });
<<<<<<< HEAD
<<<<<<< HEAD
//      When the registration button is clicked, it will enter the registration page
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
                startActivity(intent);
            }
        });
    }


=======
    }
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
=======
    }
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
    private void login(){
//        Get the account number and password from the input field
        final String Teacher_id = teacher_id.getText().toString();
        final String pw = password.getText().toString();
<<<<<<< HEAD
<<<<<<< HEAD
            new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
//                    Compare account password with database data
                    RequestBody login_request= new FormBody.Builder().add("AccountNumber",Teacher_id).add("Password",pw).build();
                    Request request = new Request.Builder().url("http://192.168.5.9:8080/LoginServlet").post(login_request).build();
                    System.out.println(login_request.toString());
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSON(responseData);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void parseJSON(String Data){
        try{
            JSONObject jsonArray = new JSONObject(Data);
            JSONObject test = jsonArray.getJSONObject("params");
            String res = test.getString("Result");
//            Get the result returned from the server
            if(res.equals("success")){

            }else if(res.equals("failed")){
                Looper.prepare();
                Toast toast=Toast.makeText(this,"Wrong Account or Password",Toast.LENGTH_SHORT);
                toast.show();
                Looper.loop();
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }



=======

    }
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
=======

    }
>>>>>>> 753b68a9555e6d4e83dd764f99226b8673d46416
}
