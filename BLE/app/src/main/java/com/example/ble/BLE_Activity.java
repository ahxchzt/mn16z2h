package com.example.ble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class BLE_Activity extends AppCompatActivity {
//    allstudentList store the student information who selected the corresponding lecture
//    studentList store the student information who is scanned
//    absentstudentList store the student information who is not scanned
    private List<Student> allstudentList=new ArrayList<>();
    private List<Student> studentList=new ArrayList<>();
    private List<Student> absentstudentList=new ArrayList<>();
    private Lecture lecture;
    TextView lectureTitle;
    Button scan;
    BluetoothManager blemanager;
    BluetoothAdapter bleAdapter;
    TextView student_number;
    TextView absent_number;
    BluetoothReceive bleReceive;
    BLE_Adapter ble_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        lectureTitle=(TextView)findViewById(R.id.lecture_title);
//        Obtain lecture information
        lecture=(Lecture) getIntent().getSerializableExtra("Lecture");
        lectureTitle.setText(lecture.getCode());
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.student);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        initble();
        initstudent();
//        When the scan button is clicked, the system will call ScanBlue()
        scan=(Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(v.getContext(), "SCAN START!", Toast.LENGTH_SHORT).show();
                ScanBlue();
            }
        });
//        All unscanned students will be displayed on the screen
        ble_adapter=new BLE_Adapter(absentstudentList,lecture);
        recyclerView.setAdapter(ble_adapter);
    }
    private void initstudent(){
        Thread thread = null;
//        Send a request to the server to get the student information who selected the lecture
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    RequestBody student_request= new FormBody.Builder().add("module_code",lecture.getCode()).build();
                    Request request = new Request.Builder().url("http://192.168.5.9:8080/StudentServlet").post(student_request).build();
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
        student_number=(TextView) findViewById(R.id.student_number);
        student_number.setText("Student Number: "+allstudentList.size());
    }
    private void initble(){
        blemanager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        // Broadcast registration
        bleReceive = new BluetoothReceive();
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bleReceive, intentFilter);
        intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bleReceive, intentFilter);
        bleAdapter = BluetoothAdapter.getDefaultAdapter();
//Judge whether Bluetooth is on. If it is off, request to turn on Bluetooth
        if (bleAdapter == null || !bleAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
            Toast.makeText(this,"OPENING BlueTeeth",Toast.LENGTH_SHORT).show();
        }

    }
    public void ScanBlue() {

        if (bleAdapter.isDiscovering()) {
            bleAdapter.cancelDiscovery();
            bleAdapter.startDiscovery();
        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bleAdapter.startDiscovery();
        }
    }

    private void parseJSON(String Data){
        try{
            JSONObject jsonArray = new JSONObject(Data);
            JSONObject test = jsonArray.getJSONObject("params");
//          Parse the data obtained from the server to obtain all student information
            JSONArray ab=test.getJSONArray("Result");
            for (int b=0;b<ab.length();b++){
                String address= test.getJSONArray("Result").getJSONObject(b).getString("address");
                String student_id= test.getJSONArray("Result").getJSONObject(b).getString("student_id");
                String name= test.getJSONArray("Result").getJSONObject(b).getString("name");
                int absent_time= test.getJSONArray("Result").getJSONObject(b).getInt("absent_time");
                String email= test.getJSONArray("Result").getJSONObject(b).getString("email");
                Student student=new Student();
                student.setAddress(address);
                student.setAbsent_time(absent_time);
                student.setEmail(email);
                student.setName(name);
                student.setStudent_id(student_id);
                allstudentList.add(student);
            }



        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private class BluetoothReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Student student=new Student();
                student.setAddress(device.getAddress());
//                Whenever a new device is scanned, the corresponding student is stored in the studentlist
                if (!studentList.contains(student)){
                    studentList.add(student);
                    System.out.println(student.getAddress());
                }



            }else if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
//                Notify the user that the scan is complete
                Toast.makeText(context, "Finish！", Toast.LENGTH_SHORT).show();
//                allstudentList stores all student information, and studentList stores scanned student information.
//                Therefore, the absent students are the students who are present in allstudentList but not in studentList
                for(int i=allstudentList.size()-1;i>=0;i--){
                    for(int j=studentList.size()-1;j>=0;j--){
                        if (allstudentList.get(i).getAddress().equals(studentList.get(j).getAddress())){
                            allstudentList.remove(i);
                            break;
                        }
                    }
                }
                absentstudentList.addAll(allstudentList);
                absent_number=(TextView) findViewById(R.id.absent_number);
                absent_number.setText("Absent: "+absentstudentList.size());
//                Update page
                ble_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bleReceive);
        super.onDestroy();
    }
}
