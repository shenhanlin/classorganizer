package com.example.hanlinshen.classorganizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;

public class ClassDetails extends AppCompatActivity {
    private String className;
    private String schoolName;
    private boolean isAddingClass;
    private String classGroup;
    private String classCredit;
    private String classStatus;
    private String bookName;
    private String goalName;
    private String goalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_details);

        Intent intent = getIntent();
        className = intent.getExtras().getString("className");
        schoolName = intent.getExtras().getString("schoolName");
        isAddingClass = intent.getExtras().getBoolean("isAddingClass");
        goalName = intent.getExtras().getString("goalName");
        goalNumber = intent.getExtras().getString("goalNumber");


        if (isAddingClass) {
            findViewById(R.id.classStatusLabe).setVisibility(View.GONE);
            findViewById(R.id.classStatusText).setVisibility(View.GONE);
            getDataFromSeverDataBase();
        } else {
            getDataFromUserDataBase();
        }

        init();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_class_details, menu);
        if (isAddingClass) {
            menu.findItem(R.id.action_addThisClass).setVisible(true);
            menu.findItem(R.id.action_deleteThisClass).setVisible(false);
        } else {
            menu.findItem(R.id.action_addThisClass).setVisible(false);
            menu.findItem(R.id.action_deleteThisClass).setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        UserInformationDatabase userInformationDatabase =new UserInformationDatabase(this);
        switch (item.getItemId()) {
            case R.id.action_addThisClass:
                userInformationDatabase =new UserInformationDatabase(this);
                try {
                    userInformationDatabase.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                userInformationDatabase.addClass(className,classGroup,Integer.parseInt(classCredit),"Taking",schoolName,bookName);
                userInformationDatabase.close();

                intent = new Intent(this, ClassList.class);
                intent.putExtra("goalNumber", goalNumber);
                intent.putExtra("goalName", goalName);
                intent.putExtra("schoolName", schoolName);
                intent.putExtra("isAddingClass", true);
                startActivity(intent);
                finish();


                return true;
            case R.id.action_deleteThisClass:
                userInformationDatabase =new UserInformationDatabase(this);
                try {
                    userInformationDatabase.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                userInformationDatabase.removeThisClass(className,schoolName);
                userInformationDatabase.close();

                intent = new Intent(this, ClassList.class);
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

    private void getDataFromSeverDataBase() {
        ClassDataBase classDataBase = new ClassDataBase(this);
        try {
            classDataBase.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] classInfo;
        classInfo = classDataBase.findClass(className,schoolName);
        classGroup = classInfo[1];
        classCredit = classInfo[2];
        bookName = classInfo[4];
        classDataBase.close();
    }


    private void getDataFromUserDataBase() {
        UserInformationDatabase userInformationDatabase =new UserInformationDatabase(this);
        try {
            userInformationDatabase.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] classInfo;
        classInfo = userInformationDatabase.findClass(className,schoolName);
        classGroup = classInfo[1];
        classCredit = classInfo[2];
        classStatus = classInfo[3];
        bookName = classInfo[4];
        userInformationDatabase.close();
    }

    private void init(){
        TextView classNameTextView = (TextView) findViewById(R.id.classNameText);
        TextView classGroupTextView = (TextView) findViewById(R.id.classGroupText);
        TextView classCreditTextView = (TextView) findViewById(R.id.classCreditText);
        TextView classBookTextView = (TextView) findViewById(R.id.classBookText);
        TextView classStatusTextView = (TextView) findViewById(R.id.classStatusText);
        ImageView bookImage = (ImageView) findViewById(R.id.imageViewForBook);

        classNameTextView.setText(className);
        classGroupTextView.setText(classGroup);
        classCreditTextView.setText(classCredit);
        classBookTextView.setText(bookName);
        classStatusTextView.setText(classStatus);
        int bookImageId = getResources().getIdentifier(forTestMakeFakeNameForBookImage(bookName),"drawable",getPackageName());
        bookImage.setImageResource(bookImageId);

    }
    //Just for test make a fake book image name for the book
    private String forTestMakeFakeNameForBookImage(String bookName){
        int result = 0;
        int tempFirst =Character.getNumericValue(bookName.charAt(11));
        int tempSecond =Character.getNumericValue(bookName.charAt(12));
        int tempThree =Character.getNumericValue(bookName.charAt(13));

        result = tempFirst * tempSecond * tempThree;
        while (result > 10){
            result = result /2;
        }
        return "book_"+result;
    }

    //change class's status in the data base
    public void changeClassStatus(View v){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("You about to change the class status! " +
                "Are you sure you want to do this?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserInformationDatabase userInformationDatabase = new UserInformationDatabase(ClassDetails.this);
                try {
                    userInformationDatabase.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                userInformationDatabase.updataClassStatus(className, schoolName);
                userInformationDatabase.close();
                //back to classlist view
                Intent intent = new Intent(ClassDetails.this, ClassList.class);
                intent.putExtra("goalNumber", goalNumber);
                intent.putExtra("goalName", goalName);
                intent.putExtra("className", className);
                intent.putExtra("schoolName", schoolName);
                intent.putExtra("isAddingClass", isAddingClass);
                startActivity(intent);
                finish();


            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
