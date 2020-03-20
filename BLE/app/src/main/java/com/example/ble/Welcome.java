package com.example.ble;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            Intent intent=new Intent(Welcome.this,Login_Activity.class);
            startActivity(intent);
            finish();
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler.sendEmptyMessageDelayed(0,3000);
    }
}
