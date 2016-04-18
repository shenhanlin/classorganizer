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
public class UserInformationDatabase {
    private final static String DATABASE_NAME="UserInformation";
    //user Table
    private final static String DATABASE_USERTABLE="User";
    private final static String KEY_NAME ="Name";
    private final static String KEY_SCHOOL ="School";
    private final static String KEY_PROFILEPICTURE ="Profile_picture";
    //user class list table
    private final static String DATABASE_CLASSTABLE ="Class_List";
    private final static String KEY_ROWID ="Id";
    private final static String KEY_CLASSNAME = "Class_name";
    private final static String KEY_CLASSGROUP ="Class_group";
    private final static String KEY_CLASSCREDIT = "Class_credit";
    private final static String KEY_STATUS ="Class_status";
    private final static String KEY_SCHOOLNAME ="School_name";
    private final static String KEY_BOOK ="Book";



    private final static int DATABASE_VERSION=1;

    private final Context context;
    private  DbHelper databaseHelper;
    private SQLiteDatabase UserInformationDatabase;


    public UserInformationDatabase(Context context) {
        this.context =context;
    }

    //adding class to Class_List Table
    public long addClass(String className,String classGroup, int classCredit, String classStatus,String schoolName,String bookName){

        Cursor c = UserInformationDatabase.rawQuery("SELECT COUNT(*) FROM " + DATABASE_CLASSTABLE
                + " WHERE " + KEY_CLASSNAME + " = '" + className + "' AND " + KEY_SCHOOLNAME + " = '"
                + schoolName + "'", null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();
        if(count==0){
        ContentValues contentValues =new ContentValues();
        contentValues.put(KEY_CLASSNAME,className);
        contentValues.put(KEY_CLASSGROUP,classGroup);
        contentValues.put(KEY_CLASSCREDIT,classCredit);
        contentValues.put(KEY_STATUS, classStatus);
        contentValues.put(KEY_SCHOOLNAME,schoolName);
        contentValues.put(KEY_BOOK, bookName);
        return UserInformationDatabase.insert(DATABASE_CLASSTABLE, null,contentValues);
        }
        return -1;
    }

    //get class from Class_List Table
    public List<String[]> getUserClass(String goalNumber,String schoolName) {
        String[] args = new String[1];
        args[0] = "%"+goalNumber+"%";
        Cursor c = UserInformationDatabase.rawQuery("SELECT * FROM " + DATABASE_CLASSTABLE + " WHERE " + KEY_CLASSGROUP + " LIKE ?", args);
        List<String[]> databaseOutPut = new ArrayList<String[]>();

        int iClassName = c.getColumnIndex(KEY_CLASSNAME);
        int iClassGroup = c.getColumnIndex(KEY_CLASSGROUP);
        int iClassCredit = c.getColumnIndex(KEY_CLASSCREDIT);
        int iClassStatus = c.getColumnIndex(KEY_STATUS);
        int iSchoolName = c.getColumnIndex(KEY_SCHOOLNAME);
        int iBookName = c.getColumnIndex(KEY_BOOK);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            if(c.getString(iSchoolName).equals(schoolName)) {
                String[] classInformation = new String[5];
                classInformation[0] = c.getString(iClassName);
                classInformation[1] = c.getString(iClassGroup);
                classInformation[2] = c.getString(iClassCredit);
                classInformation[3] = c.getString(iClassStatus);
                classInformation[4] = c.getString(iBookName);
                databaseOutPut.add(classInformation);
            }
        }
        return databaseOutPut;
    }

    //find one class by school name and class name
    public String[] findClass(String className,String schoolName){

        Cursor c = UserInformationDatabase.rawQuery("SELECT * FROM " + DATABASE_CLASSTABLE + " WHERE " + KEY_CLASSNAME + " = '" + className + "'", null);
        String[] databaseOutPut=new String[5];

        int iClassName = c.getColumnIndex(KEY_CLASSNAME);
        int iClassGroup = c.getColumnIndex(KEY_CLASSGROUP);
        int iClassCredit = c.getColumnIndex(KEY_CLASSCREDIT);
        int iClassStatus = c.getColumnIndex(KEY_STATUS);
        int iSchoolName = c.getColumnIndex(KEY_SCHOOLNAME);
        int iBookName = c.getColumnIndex(KEY_BOOK);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getString(iSchoolName).equals(schoolName)) {
                databaseOutPut[0] = c.getString(iClassName);
                databaseOutPut[1] = c.getString(iClassGroup);
                databaseOutPut[2] = c.getString(iClassCredit);
                databaseOutPut[3] = c.getString(iClassStatus);
                databaseOutPut[4] = c.getString(iBookName);
            }
        }
        return databaseOutPut;
    }


    //update class status
    public void updataClassStatus(String className,String schoolName){
        String[] classNeedToBeUpdata = findClass(className,schoolName);

        if(classNeedToBeUpdata[3].equals("Taking")){
            classNeedToBeUpdata[3] = "Done";
        }else {
            classNeedToBeUpdata[3] = "Taking";
        }

        ContentValues contentValues =new ContentValues();
        contentValues.put(KEY_CLASSNAME,classNeedToBeUpdata[0]);
        contentValues.put(KEY_CLASSGROUP,classNeedToBeUpdata[1]);
        contentValues.put(KEY_CLASSCREDIT,classNeedToBeUpdata[2]);
        contentValues.put(KEY_STATUS, classNeedToBeUpdata[3]);
        contentValues.put(KEY_SCHOOLNAME,schoolName);
        contentValues.put(KEY_BOOK, classNeedToBeUpdata[4]);

        UserInformationDatabase.update(DATABASE_CLASSTABLE,contentValues,KEY_CLASSNAME + " = '"
                + className + "' AND " + KEY_SCHOOLNAME + " = '" + schoolName + "'",null);

    }


    //Remove class from table
    public void removeThisClass(String className,String schoolName){

            UserInformationDatabase.delete(DATABASE_CLASSTABLE,KEY_SCHOOLNAME + " = '"
                    + schoolName +"' AND " + KEY_CLASSNAME + " = '" + className + "'",null);

    }

    public void clearClassTable(){
            UserInformationDatabase.execSQL("DELETE FROM " + DATABASE_CLASSTABLE);


    }


    ////////////////////////////////////////////////////////////////////////////

    //adding user to user Table
    public long addUser(String name,String school, String profilePicture){
        clearUserTable();
        ContentValues contentValues =new ContentValues();
        contentValues.put(KEY_NAME,name);
        contentValues.put(KEY_SCHOOL,school);
        contentValues.put(KEY_PROFILEPICTURE,profilePicture);
        return UserInformationDatabase.insert(DATABASE_USERTABLE, null,contentValues);
    }

    //get user information from User Table
    public String[] getUserInformation() {
        String [] columns = new String[] {KEY_NAME,KEY_SCHOOL,KEY_PROFILEPICTURE};
        Cursor c = UserInformationDatabase.query(DATABASE_USERTABLE,columns,null,null,null,null,null);

        String[] databaseOutPut = new String[3];

        int iUserName = c.getColumnIndex(KEY_NAME);
        int iSchool = c.getColumnIndex(KEY_SCHOOL);
        int iProfilePicture = c.getColumnIndex(KEY_PROFILEPICTURE);
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            databaseOutPut[0]=c.getString(iUserName);
            databaseOutPut[1]=c.getString(iSchool);
            databaseOutPut[2]=c.getString(iProfilePicture);
        }
        return databaseOutPut;
    }


    public void clearUserTable(){
        UserInformationDatabase.execSQL("DELETE FROM " + DATABASE_USERTABLE);


    }


    private class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DATABASE_USERTABLE
                    + " ("
                    + KEY_NAME +" TEXT PRIMARY KEY, "
                    + KEY_SCHOOL +" TEXT NOT NULL, "
                    + KEY_PROFILEPICTURE + " TEXT NOT NULL"
                    +")");

            db.execSQL("CREATE TABLE " + DATABASE_CLASSTABLE
                    + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_CLASSNAME + " TEXT NOT NULL, "
                    + KEY_CLASSGROUP + " TEXT NOT NULL, "
                    + KEY_CLASSCREDIT + " INTEGER NOT NULL, "
                    + KEY_STATUS + " TEXT NOT NULL, "
                    + KEY_SCHOOLNAME + " TEXT NOT NULL, "
                    + KEY_BOOK + " TEXT NOT NULL"
                    + ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public UserInformationDatabase open() throws SQLException{
        databaseHelper = new DbHelper(context);
        UserInformationDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        databaseHelper.close();
    }


    public void forTest(){
        clearClassTable();
        //add class for group
        for (int i=0; i<10;i++){
            for (int j=0; j<2; j++){
            String randomName = forTestRandomString();
                Random random = new Random();
                int randomCreidt =random.nextInt(3);
                if(randomCreidt==0){randomCreidt=3;}
                if(random.nextInt(10) > 4){
                    addClass(randomName,Integer.toString(i),randomCreidt,"Done","A",("Theorem Of "+randomName));
                }else{
                    addClass(randomName,Integer.toString(i),randomCreidt,"Taking","A",("Theorem Of "+randomName));
                }
            }
        }
        //add Double Count Class
        for (int i=0; i<10;i++){
                String randomName = forTestRandomString();
                Random random = new Random();
                int randomCreidt =random.nextInt(3);
                if(randomCreidt==0){randomCreidt=3;}
                if(random.nextInt(10) > 4){
                    addClass(randomName,Integer.toString(random.nextInt(10))+","+Integer.toString(random.nextInt(9)+1),randomCreidt,"Done","A",("Theorem Of "+randomName));
                }else{
                    addClass(randomName,Integer.toString(random.nextInt(10))+","+Integer.toString(random.nextInt(9)+1),randomCreidt,"Taking","A",("Theorem Of "+randomName));
                }

        }
        //add a user
        addUser("Hanlin Shen","A","");


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
