package com.example.hanlinshen.classorganizer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class UserProfile extends AppCompatActivity {
    private ImageView myImageView;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri capturedImageUri;
    private  String viewId;
    private TextView userNameTextView;
    private TextView userSchoolTextView;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //get User Information
        UserInformationDatabase userInformationDatabase = new UserInformationDatabase(this);
        try {
            userInformationDatabase.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] userInformation = userInformationDatabase.getUserInformation();
        userNameTextView = (TextView) findViewById(R.id.userNameText);
        userSchoolTextView = (TextView) findViewById(R.id.userSchoolText);
        userNameTextView.setText(userInformation[0]);
        userSchoolTextView.setText(userInformation[1]);
        userInformationDatabase.close();


        // get User profile image from the local storage
        File file = new File(Environment.getExternalStorageDirectory(), "User_Profile_Image.jpg");
        if (file.exists()) {
            imageBitmap = null;
            capturedImageUri = Uri.fromFile(file);
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), capturedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myImageView = (ImageView) findViewById(R.id.imageViewForUserProfile);
            myImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), capturedImageUri);
                myImageView = (ImageView) findViewById(R.id.imageViewForUserProfile);
                myImageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //take a picture and save it in the local storage
    public void useCamera(View view) {
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "User_Profile_Image.jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        capturedImageUri = Uri.fromFile(file);

        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
        startActivityForResult(cameraintent, REQUEST_IMAGE_CAPTURE);

    }

    //update the use information in database
    public void changeNameOrSchool(final View view) {
        //get the view id in string value
        viewId = view.getResources().getResourceName(view.getId());
        viewId = viewId.substring((viewId.indexOf('/')+1));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if (viewId.equals("userNameText")) {
            alertDialogBuilder.setMessage("You about to change the User Name! " +
                    "Are you sure you want to do this?");
        }

        if (viewId.equals("userSchoolText")) {
            alertDialogBuilder.setMessage("You about to change the School! " +
                    "Are you sure you want to do this?");
        }

        //set an EditText view to get user input
        final EditText input = new EditText(this);
        alertDialogBuilder.setView(input);


        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                UserInformationDatabase userInformationDatabase = new UserInformationDatabase(UserProfile.this);
                try {
                    userInformationDatabase.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (viewId.equals("userNameText")) {
                    userInformationDatabase.addUser(input.getText().toString(),userSchoolTextView.getText().toString(), "");
                    userNameTextView.setText(input.getText().toString());
                }

                if (viewId.equals("userSchoolText")) {
                    userInformationDatabase.addUser(userNameTextView.getText().toString(), input.getText().toString(), "");
                    userSchoolTextView.setText(input.getText().toString());
                }
                userInformationDatabase.close();

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
