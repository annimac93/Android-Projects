package com.appart.hp.nearme;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {

        String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    CircleImageView ivprofile;
    EditText edusername,edname,edemail,edmobile;
    Button cancel,signup;
    String username,name,email,mobile;
    byte[] imgbyte = null;
    String userid;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    DBO obj;
    String fromEmail, fromPassword, toEmail, emailSubject, emailBody;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //hiding titlebar
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.appart.hp.menuintegration.R.layout.activity_signup);

        obj=new DBO(this, null, null, 1);
        ivprofile = (CircleImageView)findViewById(com.appart.hp.menuintegration.R.id.img_propic);
        edusername = (EditText) findViewById(com.appart.hp.menuintegration.R.id.editText_username);
        edname = (EditText) findViewById(com.appart.hp.menuintegration.R.id.editText_name);
        edemail = (EditText) findViewById(com.appart.hp.menuintegration.R.id.editText_email);
        edmobile = (EditText) findViewById(com.appart.hp.menuintegration.R.id.editText_mob);

        cancel = (Button) findViewById(com.appart.hp.menuintegration.R.id.btn_cancel);
        signup = (Button) findViewById(com.appart.hp.menuintegration.R.id.btn_signup);

        ivprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(SignupActivity.this,FirstActivity.class);
//                startActivity(i);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=edusername.getText().toString();
                name=edname.getText().toString();
                email = edemail.getText().toString();
                mobile = edmobile.getText().toString();




                Cursor mCursor = obj.getunpw();


                    if(username.length()>0 && name.length()>0 && email.length()>0 && mobile.length() > 0 )
                    {


                        if(mobile.length() != 10)
                        {
                            Toast.makeText(getApplicationContext(),"Please enter valid phone number.",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            if(email.matches(emailPattern))
                            {
                                if(imgbyte != null)
                                {
                                    if (mCursor.moveToFirst()) {

                                        do {
                                            if(username.equalsIgnoreCase(mCursor.getString(1)))
                                            {
                                                flag = 1;
                                                break;
                                            }
                                            else
                                            {
                                                flag = 0;
                                                break;
                                            }

                                        } while (mCursor.moveToNext());


                                    }
                                    else
                                    {
//            Toast.makeText(getActivity(),"in else",Toast.LENGTH_SHORT).show();

                                    }

                                    if(flag == 0)
                                    {
//                                          Toast.makeText(getApplicationContext(),"in savedata : "+ String.valueOf(imgbyte),Toast.LENGTH_SHORT).show();
                                            saveData();
                                    }
                                    else if(flag == 1)
                                    {
                                        Toast.makeText(getApplicationContext(),"This user is already exist.",Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Please select your profile picture.",Toast.LENGTH_SHORT).show();

                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Please enter valid email.",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    else
                    {
                        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(SignupActivity.this);
                        alertBuilder.setTitle("Invalid Data");
                        alertBuilder.setMessage("Please, Enter valid data");
                        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });
                        alertBuilder.create().show();
                    }



            }
        });

    }


    private void saveData(){

    if(obj.addUser(username,name,email,mobile,"user@123",imgbyte))
    {
//        Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();

        fromEmail = "milymac93@gmail.com";
        fromPassword = "milymac11093@";
        toEmail = email;
        emailSubject = "Near Me Varification";
        emailBody = "Dear "+name +", Thank you for registering in Near Me Application. Your Near Me Password is : user@123. You can change your password from 'Profile'.";

        new SendMailTask(SignupActivity.this).execute(fromEmail, fromPassword, toEmail, emailSubject, emailBody);
        Toast.makeText(getApplicationContext(),"Please open your mail and get the password.",Toast.LENGTH_LONG).show();

//        Cursor mCursor = obj.getid(username,name,email,mobile);
//
//        if (mCursor.moveToFirst()) {
//            do {
//                userid = mCursor.getString(0);
//
//
//            } while (mCursor.moveToNext());

            Intent i =new Intent(SignupActivity.this,LoginActivity.class);
//            i.putExtra("userid",userid);
            startActivity(i);
            finish();
//        }
//
//        else
//        {
//            Toast.makeText(getApplicationContext(),"Record not found",Toast.LENGTH_SHORT).show();
//        }

//        Toast.makeText(getApplicationContext(),"userid = "+ userid,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),"Data inserted successfully ",Toast.LENGTH_SHORT).show();
    }
        else
    {
        Toast.makeText(getApplicationContext(),"Insertion Failed ",Toast.LENGTH_SHORT).show();
    }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    Toast.makeText(getApplicationContext(),"Please select your profile picture.",Toast.LENGTH_SHORT).show();
                    //code for deny
                }
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=checkPermission(SignupActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }


    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            imgbyte = bytes.toByteArray();
            fo.write(imgbyte);
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(),String.valueOf(imgbyte),Toast.LENGTH_SHORT).show();
        ivprofile.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                imgbyte = bytes.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Toast.makeText(getApplicationContext(),String.valueOf(imgbyte),Toast.LENGTH_SHORT).show();
        ivprofile.setImageBitmap(bm);
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public  boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }











}
