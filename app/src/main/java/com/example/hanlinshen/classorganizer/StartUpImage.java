package com.example.hanlinshen.classorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.sql.SQLException;

public class StartUpImage extends AppCompatActivity {

    private static final long DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_image);

        //for Test add data to all database
        ClassDataBase classDatabase = new ClassDataBase(this);
        try{
            classDatabase.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        classDatabase.forTest();
        classDatabase.close();

        UserInformationDatabase userInformationDatabase = new UserInformationDatabase(this);
        try{
            userInformationDatabase.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        userInformationDatabase.forTest();
        userInformationDatabase.close();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartUpImage.this,MainActivity.class);
                startActivityForResult(intent, 0);
                StartUpImage.this.finish();
            }
        },DELAY);

    }

}
