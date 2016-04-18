package com.example.hanlinshen.classorganizer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

public class ClassList extends AppCompatActivity implements View.OnClickListener {
    private String goalName;
    private String goalNumber;
    private String schoolName;
    private boolean isAddingClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);


        //get the goal that user picked
        Intent intent = getIntent();
        goalName = intent.getExtras().getString("goalName");
        goalNumber = intent.getExtras().getString("goalNumber");
        schoolName = intent.getExtras().getString("schoolName");
        isAddingClass = intent.getExtras().getBoolean("isAddingClass");
        getSupportActionBar().setTitle(goalName);

        if (isAddingClass) {
            initWithSeverDataBase();
            TextView t = (TextView) findViewById(R.id.status);
            t.setText("School");
        } else {
            initWithUserDataBase();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_class_list, menu);
        if (isAddingClass) {
            menu.findItem(R.id.action_addClass).setVisible(false);
            menu.findItem(R.id.action_goBack).setVisible(true);
        } else {
            menu.findItem(R.id.action_addClass).setVisible(true);
            menu.findItem(R.id.action_goBack).setVisible(false);
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        TableRow rowClicked = (TableRow)v;
        TextView className = (TextView) rowClicked.getChildAt(1);

        Intent intent = new Intent(this, ClassDetails.class);
        intent.putExtra("goalNumber", goalNumber);
        intent.putExtra("goalName", goalName);
        intent.putExtra("className", className.getText().toString());
        intent.putExtra("schoolName", schoolName);
        intent.putExtra("isAddingClass", isAddingClass);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_addClass:
                intent = new Intent(ClassList.this, ClassList.class);
                intent.putExtra("goalNumber", goalNumber);
                intent.putExtra("goalName", goalName);
                intent.putExtra("schoolName", schoolName);
                intent.putExtra("isAddingClass", true);
                startActivity(intent);
                finish();
                return true;
            case R.id.action_goBack:
                intent = new Intent(ClassList.this, ClassList.class);
                intent.putExtra("goalNumber", goalNumber);
                intent.putExtra("goalName", goalName);
                intent.putExtra("schoolName", schoolName);
                intent.putExtra("isAddingClass", false);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ClassList.this, GeneraleEdcationList.class);
        intent.putExtra("schoolName", schoolName);
        startActivity(intent);
    }

    //init view with data from user data base
    private void initWithUserDataBase() {
        UserInformationDatabase userDatabase = new UserInformationDatabase(this);
        try {
            userDatabase.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String[]> useClass = userDatabase.getUserClass(goalNumber, schoolName);
        String[] classInformation;

        TableLayout tl = (TableLayout) findViewById(R.id.allClass);

        for (int i = 0; i < useClass.size(); i++) {
            TableRow Row = new TableRow(this);
            TextView rowId = new TextView(this);
            TextView className = new TextView(this);
            TextView classGroup = new TextView(this);
            TextView classCredit = new TextView(this);
            TextView classStatus = new TextView(this);
            Row.setOnClickListener(this);


            classInformation = useClass.get(i);

            rowId.setText(String.valueOf(i + 1));
            className.setText(classInformation[0]);
            classGroup.setText(classInformation[1]);
            classCredit.setText(classInformation[2]);
            classStatus.setText(classInformation[3]);
            Row.addView(rowId);
            Row.addView(className);
            Row.addView(classGroup);
            Row.addView(classCredit);
            Row.addView(classStatus);
            if (i % 2 == 1) {
                Row.setBackgroundColor(Color.parseColor("#1A000000"));
            }
            tl.addView(Row);
        }

        userDatabase.close();
    }


    //init view with data from sever data base
    private void initWithSeverDataBase() {
        ClassDataBase classDataBase = new ClassDataBase(this);
        try {
            classDataBase.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<String[]> allClass = classDataBase.getAllClass(goalNumber, schoolName);
        String[] classInformation;

        TableLayout tl = (TableLayout) findViewById(R.id.allClass);

        for (int i = 0; i < allClass.size(); i++) {
            TableRow Row = new TableRow(this);
            TextView rowId = new TextView(this);
            TextView className = new TextView(this);
            TextView classGroup = new TextView(this);
            TextView classCredit = new TextView(this);
            TextView schoolName = new TextView(this);
            Row.setOnClickListener(this);

            classInformation = allClass.get(i);

            rowId.setText(String.valueOf(i + 1));
            className.setText(classInformation[0]);
            classGroup.setText(classInformation[1]);
            classCredit.setText(classInformation[2]);
            schoolName.setText(classInformation[3]);
            Row.addView(rowId);
            Row.addView(className);
            Row.addView(classGroup);
            Row.addView(classCredit);
            Row.addView(schoolName);
            if (i % 2 == 1) {
                Row.setBackgroundColor(Color.parseColor("#1A000000"));
            }
            tl.addView(Row);
        }

        classDataBase.close();
    }


}
