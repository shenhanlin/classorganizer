package com.example.hanlinshen.classorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainMenu extends AppCompatActivity {
private String schoolName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent =getIntent();
        schoolName = intent.getExtras().getString("schoolName");
        getSupportActionBar().setTitle(schoolName);

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(MainMenu.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_profile:
                intent = new Intent(this,UserProfile.class);
                intent.putExtra("schoolName",schoolName);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



//This method start the generalEducation list.
    public void toGeneralEducationList(View view){
        Intent intent = new Intent(this, GeneraleEdcationList.class);
        intent.putExtra("schoolName",schoolName);
        startActivityForResult(intent, 0);

    }



}
