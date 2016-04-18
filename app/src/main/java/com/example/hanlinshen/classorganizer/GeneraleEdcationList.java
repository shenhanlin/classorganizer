package com.example.hanlinshen.classorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.sql.SQLException;

public class GeneraleEdcationList extends AppCompatActivity implements View.OnClickListener {
    private String schoolName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generale_edcation_list);

        Intent intent =getIntent();
        schoolName = intent.getExtras().getString("schoolName");
        getSupportActionBar().setTitle(schoolName);


        ClassDataBase classDatabase = new ClassDataBase(this);
        try{
            classDatabase.open();
        } catch (SQLException e){
            e.printStackTrace();
        }
        initGoalTableView(classDatabase.getNumberOfGoal(schoolName));
        classDatabase.close();

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(GeneraleEdcationList.this,MainMenu.class);
        intent.putExtra("schoolName", schoolName);
        startActivity(intent);
    }


    //This method will init the goal table view with number of goal that school have.
    private void initGoalTableView(int numberOfGoal) {
        TableLayout tl = (TableLayout)findViewById(R.id.goalTableView);

        int numberOfRow =0;
        //get the number of row that needed
        if(numberOfGoal%2 == 1){
            numberOfRow =(numberOfGoal/2)+1;
        }else {
            numberOfRow=numberOfGoal/2;
        }


        for (int i =0; i<numberOfRow;i++){
            TableRow Row =new TableRow(this);
            if(numberOfGoal>1){
            for (int j = 1; j<3;j++){

                Button goalButton = new Button(this);
                goalButton.setText("Goal " + intToEnglishString(((i * 2) + j)));
                goalButton.setId(((i * 2) + j));
                goalButton.setOnClickListener(this);
                goalButton.setAllCaps(false);


                Row.addView(goalButton);

                numberOfGoal=numberOfGoal-1;
            }
            }else {
                Button goalButton = new Button(this);
                goalButton.setText("Goal "+intToEnglishString(((i * 2) + 1)));
                goalButton.setId(((i * 2) + 1));
                goalButton.setOnClickListener(this);
                goalButton.setAllCaps(false);


                Row.addView(goalButton);

                numberOfGoal=numberOfGoal-1;
            }

            tl.addView(Row);
        }
    }

    private String intToEnglishString(int number){
        String englishNumber="";
        switch (number) {
            case 1:
                englishNumber = "One";
                break;
            case 2:
                englishNumber = "Two";
                break;
            case 3:
                englishNumber = "Three";
                break;
            case 4:
                englishNumber = "Four";
                break;
            case 5:
                englishNumber = "Five";
                break;
            case 6:
                englishNumber = "Six";
                break;
            case 7:
                englishNumber = "Seven";
                break;
            case 8:
                englishNumber = "Eight";
                break;
            case 9:
                englishNumber = "Night";
                break;
            case 10:
                englishNumber = "Ten";
                break;
            default:
                englishNumber = Integer.toString(number);
                break;
        }
        return englishNumber;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ClassList.class);
        String tempGoalNumber="";
        tempGoalNumber = "Goal "+intToEnglishString(v.getId());
        intent.putExtra("goalNumber", Integer.toString(v.getId()));
        intent.putExtra("goalName",tempGoalNumber);
        intent.putExtra("schoolName",schoolName);
        intent.putExtra("isAddingClass",false);
        startActivityForResult(intent, 0);
    }
}
