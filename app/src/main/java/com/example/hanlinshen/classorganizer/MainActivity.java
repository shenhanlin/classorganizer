package com.example.hanlinshen.classorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String schoolPicked;
    private ArrayList<String> allSchoolInDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get all the school in the sever database
        ClassDataBase classDatabase = new ClassDataBase(this);
        try{
            classDatabase.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        allSchoolInDataBase = classDatabase.getAllTheSchool();
        classDatabase.close();

        final Spinner spinner =(Spinner) findViewById(R.id.school_spinner);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.dropdownlist_item,allSchoolInDataBase);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageView schoolBage = (ImageView)findViewById(R.id.imageViewForSchool);
                int schoolImageId = getResources().getIdentifier((spinner.getSelectedItem().toString().toLowerCase() +"_logo"),"drawable",getPackageName());
               schoolBage.setImageResource(schoolImageId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
    }

    //This method start the MainMenu and send the school that user picked
    public void toMenu(View view){
        Spinner schoolSpinner = (Spinner) findViewById(R.id.school_spinner);
        schoolPicked = schoolSpinner.getSelectedItem().toString();
        Intent intent = new Intent(this, MainMenu.class);
        intent.putExtra("schoolName",schoolPicked);
        startActivityForResult(intent, 0);

    }


}
