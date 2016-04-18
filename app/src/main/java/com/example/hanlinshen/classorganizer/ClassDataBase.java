package com.example.hanlinshen.classorganizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hanlinshen on 15-12-2.
 */
public class ClassDataBase {
    private final static String DATABASE_NAME="Class";

    private final static String DATABASE_SCHOOLLIST ="School_list";
    private final static String KEY_SCHOOLNAME = "School_name";
    private final static String KEY_NUMBEROFGOAL = "Number_of_goal";


    private final static String DATABASE_ACLASSTABLE ="A";
    private final static String DATABASE_BCLASSTABLE ="B";
    private final static String DATABASE_CCLASSTABLE ="C";
    private final static String DATABASE_DCLASSTABLE ="D";


    private final static String KEY_ROWID ="Id";
    private final static String KEY_CLASSNAME = "Class_name";
    private final static String KEY_CLASSGROUP ="Class_group";
    private final static String KEY_CLASSCREDIT = "Class_credit";
    private final static String KEY_BOOK ="Book";



    private final static int DATABASE_VERSION=1;

    private final Context context;
    private  DbHelper databaseHelper;
    private SQLiteDatabase classDatabase;


    public ClassDataBase(Context context) {
        this.context =context;
    }

    //adding class to Class_List Table
    public long addClass(String className,String classGroup, int classCredit,String schoolName,String bookName){
        ContentValues contentValues =new ContentValues();
        contentValues.put(KEY_CLASSNAME,className);
        contentValues.put(KEY_CLASSGROUP,classGroup);
        contentValues.put(KEY_CLASSCREDIT,classCredit);
        contentValues.put(KEY_BOOK, bookName);
        switch (schoolName) {
            case "A":
                return classDatabase.insert(DATABASE_ACLASSTABLE, null,contentValues);
            case "B":
                return classDatabase.insert(DATABASE_BCLASSTABLE, null,contentValues);
            case "C":
                return classDatabase.insert(DATABASE_CCLASSTABLE, null,contentValues);
            case "D":
                return classDatabase.insert(DATABASE_DCLASSTABLE, null,contentValues);
            default:
                return classDatabase.insert(DATABASE_DCLASSTABLE, null,contentValues);
        }
    }

    //get class from Class List Table by name and goal number
    public List<String[]> getAllClass(String goalNumber,String schoolName) {
        String DATABASE_CLASSTABLE;
        switch (schoolName) {
            case "A":
                DATABASE_CLASSTABLE=DATABASE_ACLASSTABLE;
                break;
            case "B":
                DATABASE_CLASSTABLE=DATABASE_BCLASSTABLE;
                break;
            case "C":
                DATABASE_CLASSTABLE=DATABASE_CCLASSTABLE;
                break;
            case "D":
                DATABASE_CLASSTABLE=DATABASE_DCLASSTABLE;
                break;
            default:
                DATABASE_CLASSTABLE=DATABASE_DCLASSTABLE;
        }


        String [] columns = new String[] {KEY_ROWID,KEY_CLASSNAME,KEY_CLASSGROUP,KEY_CLASSCREDIT,KEY_BOOK};
        Cursor c = classDatabase.query(DATABASE_CLASSTABLE,columns,null,null,null,null,null);

        List<String[]> databaseOutPut = new ArrayList<String[]>();

        int iClassName = c.getColumnIndex(KEY_CLASSNAME);
        int iClassGroup = c.getColumnIndex(KEY_CLASSGROUP);
        int iClassCredit = c.getColumnIndex(KEY_CLASSCREDIT);
        int iBookName = c.getColumnIndex(KEY_BOOK);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            if(c.getString(iClassGroup).contains(goalNumber)) {
                String[] classInformation = new String[5];
                classInformation[0] = c.getString(iClassName);
                classInformation[1] = c.getString(iClassGroup);
                classInformation[2] = c.getString(iClassCredit);
                classInformation[3] = schoolName;
                classInformation[4] = c.getString(iBookName);
                databaseOutPut.add(classInformation);
            }
        }
        return databaseOutPut;
    }

    //find one class by school name and class name
    public String[] findClass(String className,String schoolName){
        String DATABASE_CLASSTABLE;
        switch (schoolName) {
            case "A":
                DATABASE_CLASSTABLE=DATABASE_ACLASSTABLE;
                break;
            case "B":
                DATABASE_CLASSTABLE=DATABASE_BCLASSTABLE;
                break;
            case "C":
                DATABASE_CLASSTABLE=DATABASE_CCLASSTABLE;
                break;
            case "D":
                DATABASE_CLASSTABLE=DATABASE_DCLASSTABLE;
                break;
            default:
                DATABASE_CLASSTABLE=DATABASE_DCLASSTABLE;
        }

        Cursor c = classDatabase.rawQuery("SELECT * FROM " + DATABASE_CLASSTABLE + " WHERE " + KEY_CLASSNAME + " = '" +className+"'", null);
        String[] databaseOutPut=new String[5];


        int iClassName = c.getColumnIndex(KEY_CLASSNAME);
        int iClassGroup = c.getColumnIndex(KEY_CLASSGROUP);
        int iClassCredit = c.getColumnIndex(KEY_CLASSCREDIT);
        int iBookName = c.getColumnIndex(KEY_BOOK);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            databaseOutPut[0] = c.getString(iClassName);
            databaseOutPut[1] = c.getString(iClassGroup);
            databaseOutPut[2] = c.getString(iClassCredit);
            databaseOutPut[3] = schoolName;
            databaseOutPut[4] = c.getString(iBookName);
        }
        return databaseOutPut;
    }

    //adding school to school List Table
    public long addSchool(String schoolName,int numberOfGoal){
        ContentValues contentValues =new ContentValues();
        contentValues.put(KEY_SCHOOLNAME,schoolName);
        contentValues.put(KEY_NUMBEROFGOAL,numberOfGoal);
        return classDatabase.insert(DATABASE_SCHOOLLIST, null,contentValues);
    }

    //get all the school in school list table
    public ArrayList<String> getAllTheSchool(){
        String [] columns = new String[] {KEY_ROWID,KEY_SCHOOLNAME,KEY_NUMBEROFGOAL};
        Cursor c = classDatabase.query(DATABASE_SCHOOLLIST,columns,null,null,null,null,null);
        ArrayList<String> databaseOutPut = new ArrayList<String>();

        int iSchoolName = c.getColumnIndex(KEY_SCHOOLNAME);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
                databaseOutPut.add(c.getString(iSchoolName));
        }
        return databaseOutPut;
    }
    //get Number of goal from School List Table
    public int getNumberOfGoal(String schoolName) {

        Cursor c = classDatabase.rawQuery("SELECT * FROM " + DATABASE_SCHOOLLIST + " WHERE " + KEY_SCHOOLNAME + " = '" +schoolName+"'", null);

        int databaseOutPut=0;

        int iNumberOfGoal = c.getColumnIndex(KEY_NUMBEROFGOAL);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            databaseOutPut = c.getInt(iNumberOfGoal);
        }
        return databaseOutPut;
    }

    public void clearSchoolListTable(){
        classDatabase.execSQL("DELETE FROM " + DATABASE_SCHOOLLIST);

    }

    public void clearClassListTable(){
        classDatabase.execSQL("DELETE FROM " + DATABASE_ACLASSTABLE);
        classDatabase.execSQL("DELETE FROM " + DATABASE_BCLASSTABLE);
        classDatabase.execSQL("DELETE FROM " + DATABASE_CCLASSTABLE);
        classDatabase.execSQL("DELETE FROM " + DATABASE_DCLASSTABLE);

    }

    private class DbHelper extends SQLiteOpenHelper{

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_SCHOOLLIST
                    + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_SCHOOLNAME + " TEXT NOT NULL, "
                    + KEY_NUMBEROFGOAL + " INTEGER NOT NULL "
                    + ")");

            db.execSQL("CREATE TABLE " + DATABASE_ACLASSTABLE
                    + " ("
                    + KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CLASSNAME +" TEXT NOT NULL, "
                    + KEY_CLASSGROUP + " TEXT NOT NULL, "
                    + KEY_CLASSCREDIT + " INTEGER NOT NULL, "
                    + KEY_BOOK + " TEXT NOT NULL"
                    +")");
            db.execSQL("CREATE TABLE " + DATABASE_BCLASSTABLE
                    + " ("
                    + KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CLASSNAME +" TEXT NOT NULL, "
                    + KEY_CLASSGROUP + " TEXT NOT NULL, "
                    + KEY_CLASSCREDIT + " INTEGER NOT NULL, "
                    + KEY_BOOK + " TEXT NOT NULL"
                    +")");
            db.execSQL("CREATE TABLE " + DATABASE_CCLASSTABLE
                    + " ("
                    + KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CLASSNAME +" TEXT NOT NULL, "
                    + KEY_CLASSGROUP + " TEXT NOT NULL, "
                    + KEY_CLASSCREDIT + " INTEGER NOT NULL, "
                    + KEY_BOOK + " TEXT NOT NULL"
                    +")");
            db.execSQL("CREATE TABLE " + DATABASE_DCLASSTABLE
                    + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CLASSNAME + " TEXT NOT NULL, "
                    + KEY_CLASSGROUP + " TEXT NOT NULL, "
                    + KEY_CLASSCREDIT + " INTEGER NOT NULL, "
                    + KEY_BOOK + " TEXT NOT NULL"
                    + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public ClassDataBase open() throws SQLException{
        databaseHelper = new DbHelper(context);
        classDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }

    //only use for test
    public void forTest(){
        clearSchoolListTable();
        addSchool("B", 9);
        addSchool("A", 10);
        addSchool("C", 5);
        addSchool("D",4);

        clearClassListTable();
        //add class for group
        for (int i=0; i<10;i++){
            for (int j=0; j<5; j++){
                String randomName = forTestRandomString();
                Random random = new Random();
                int randomCreidt =random.nextInt(3);
                if(randomCreidt==0){randomCreidt=3;}
                addClass(randomName,Integer.toString(i),randomCreidt,"A",("Theorem Of "+randomName));
            }
        }
        //add Double Count Class
        for (int i=0; i<20;i++){
            String randomName = forTestRandomString();
            Random random = new Random();
            int randomCreidt =random.nextInt(3);
            if(randomCreidt==0){randomCreidt=3;}
            addClass(randomName,Integer.toString(random.nextInt(10))+","+Integer.toString(random.nextInt(9)+1),randomCreidt,"A",("Theorem Of "+randomName));

        }

        //add class for group
        for (int i=0; i<10;i++){
            for (int j=0; j<50; j++){
                String randomName = forTestRandomString();
                Random random = new Random();
                int randomCreidt =random.nextInt(3);
                if(randomCreidt==0){randomCreidt=3;}
                addClass(randomName,Integer.toString(i),randomCreidt,"B",("Theorem Of "+randomName));
            }
        }
        //add Double Count Class
        for (int i=0; i<20;i++){
            String randomName = forTestRandomString();
            Random random = new Random();
            int randomCreidt =random.nextInt(3);
            if(randomCreidt==0){randomCreidt=3;}
            addClass(randomName,Integer.toString(random.nextInt(10))+","+Integer.toString(random.nextInt(9)+1),randomCreidt,"B",("Theorem Of "+randomName));

        }

    }

    //cerat a random string of size 3
    private String forTestRandomString(){
        char[] chars ="ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i<3; i++){
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

}
