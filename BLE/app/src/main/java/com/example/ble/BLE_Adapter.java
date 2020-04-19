package com.example.ble;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Handler;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BLE_Adapter extends RecyclerView.Adapter<BLE_Adapter.ViewHolder> {
    //      The adapter is used to define each item in the Listview
    private List<Student> mListStudent;
    private Lecture mlecture;
    static class ViewHolder extends RecyclerView.ViewHolder {
        View studentView;
        TextView studentName;
        Button supplementary;
        public ViewHolder(View view) {
            super(view);
            studentView=view;
            studentName= (TextView) view.findViewById(R.id.student_name);
            supplementary=(Button) view.findViewById(R.id.student_statue);
        }
    }

    public BLE_Adapter(List<Student> List,Lecture lecture) {
        mListStudent= List;
        mlecture=lecture;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplementary, parent, false);
        final ViewHolder holder = new ViewHolder(view);
//        When the ‘absent’ button is clicked,
//        the system will automatically determine the number of absenteeism of the student.
//        If the number of absenteeism is greater than three, it will automatically send a warning email to the student
        holder.supplementary.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
//                get the student information corresponding to the position
                final Student student= mListStudent.get(position);
                student.setStatus("absent");
                mListStudent.set(position,student);
                System.out.println(mListStudent.get(position).getName()+mListStudent.get(position).getStatus());
                holder.supplementary.setEnabled(false);
                holder.supplementary.setBackgroundColor(0xFFFF3333);
//                send email
                if(student.getAbsent_time()>=3){
                    Thread thread1=null;
                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                we use the smtp service provided by Netease
                                Properties properties = new Properties();
                                properties.setProperty("mail.smtp.host", "smtp.163.com");
                                properties.setProperty("mail.smtp.auth", "true");
                                properties.setProperty("username","ahxchzt@163.com");
                                properties.setProperty("To",student.getEmail());
                                Session session = Session.getInstance(properties, new Authenticator() {
                                    // 设置认证账户信息
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication("ahxchzt@163.com", "zp389289");
                                    }
                                });
                                session.setDebug(true);
                                MimeMessage message = new MimeMessage(session);
                                // Sender
                                message.setFrom(new InternetAddress("ahxchzt@163.com"));
                                // recipient
                                message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(properties.getProperty("username")));
                                message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty("To")));


                                // content
                                message.setSubject("Absent Warning");
                                message.setContent("You have been absent from work more than three times this semester. Please take classes seriously!", "text/html;charset=UTF-8");
                                message.setSentDate(new Date());
                                message.saveChanges();
                                Transport.send(message);
                            } catch (MessagingException e) {

                            }
                        }
                    });
                    thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Thread thread=null;
//                The number of absences is increased by 1 and stored in the server
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            OkHttpClient client = new OkHttpClient();
                            System.out.println(student.getStudent_id());
                            RequestBody login_request= new FormBody.Builder().add("StudentId",student.getStudent_id()).add("ModuleCode",mlecture.getCode()).build();
                            Request request = new Request.Builder().url("http://192.168.5.9:8080/AbsentServlet").post(login_request).build();

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
            }
        });

        return holder;
    }
    private void parseJSON(String Data){
        try{
            JSONObject jsonArray = new JSONObject(Data);
            JSONObject test = jsonArray.getJSONObject("params");
            String res = test.getString("Result");
            if(res.equals("success")){
               System.out.println("success");
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Student student = mListStudent.get(position);
        holder.studentName.setText(student.getName());
    }

    @Override
    public int getItemCount() {
        return mListStudent.size();
    }
}
