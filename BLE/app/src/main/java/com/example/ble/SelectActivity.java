package com.example.ble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelectActivity extends AppCompatActivity {
    private List<Lecture>lectureList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent =getIntent();
        final String teacher_id=intent.getStringExtra("id");
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.lecturex);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Thread thread=null;
//      get the teacher information passed from the login activity, and send a request to the server to obtain the course taught by the teacher
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody login_request= new FormBody.Builder().add("TeacherId",teacher_id).build();
                    Request request = new Request.Builder().url("http://192.168.5.9:8080/ModuleServlet").post(login_request).build();
                    System.out.println(login_request.toString());
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSON(responseData);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//       Update page to show course information
        LectureAdapter lectureAdapter=new LectureAdapter(lectureList);
        recyclerView.setAdapter(lectureAdapter);


    }
    private void parseJSON(String Data){
        try{
            JSONObject jsonArray = new JSONObject(Data);
            JSONObject test = jsonArray.getJSONObject("params");
//          Parse the results returned from the server to obtain course information
            JSONArray ab=test.getJSONArray("Result");
            for (int b=0;b<ab.length();b++){
                String a= test.getJSONArray("Result").getJSONObject(b).getString("module_code");
                String bb= test.getJSONArray("Result").getJSONObject(b).getString("module_name");
                String c= test.getJSONArray("Result").getJSONObject(b).getString("time");
                String d= test.getJSONArray("Result").getJSONObject(b).getString("data");
                Lecture lecture=new Lecture(a,bb,c,d);
                System.out.println(a);
                lectureList.add(lecture);
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
