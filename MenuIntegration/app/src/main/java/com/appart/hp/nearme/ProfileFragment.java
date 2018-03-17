package com.appart.hp.nearme;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link ProfileFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link ProfileFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    CircleImageView ivprofile;
    EditText edusername,edname,edemail,edmobile,edpassword;
    Button save;
    String username,name,email,mobile,password,userid;
    byte[] imgbyte = null;
    DBO obj;

//    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(com.appart.hp.menuintegration.R.layout.fragment_profile, container, false);

        Bundle args = getArguments();

        if (args != null) {
            userid = args.getString("userid");
        }
        obj=new DBO(getActivity(), null, null, 1);
        ivprofile = (CircleImageView)v.findViewById(com.appart.hp.menuintegration.R.id.img_changepropic);
        edusername = (EditText)v.findViewById(com.appart.hp.menuintegration.R.id.editText_changeuname);
        edname = (EditText) v.findViewById(com.appart.hp.menuintegration.R.id.editText_changename);
        edemail = (EditText) v.findViewById(com.appart.hp.menuintegration.R.id.editText_changeemail);
        edmobile = (EditText) v.findViewById(com.appart.hp.menuintegration.R.id.editText_changemob);
        edpassword = (EditText)v.findViewById(com.appart.hp.menuintegration.R.id.editText_changepwd);


        save = (Button) v.findViewById(com.appart.hp.menuintegration.R.id.btn_update);

        displayData();

        ivprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=edusername.getText().toString();
                name=edname.getText().toString();
                email = edemail.getText().toString();
                mobile = edmobile.getText().toString();
                password = edpassword.getText().toString();
                if(username.length()>0 && name.length()>0 && email.length()>0 && mobile.length() > 0 && password.length()>0 && imgbyte.length > 0)
                {
                    saveData();
                }
                else
                {
                    AlertDialog.Builder alertBuilder=new AlertDialog.Builder(getActivity());
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
        return v;
    }

    private void displayData() {

        Cursor mCursor = obj.getuserbyid(Integer.valueOf(userid));
//        Cursor mcur = obj.getbyuserid(Integer.valueOf(userid));

        if (mCursor.moveToFirst()) {
            do {
                username = mCursor.getString(1);
                name = mCursor.getString(2);
                email = mCursor.getString(3);
                mobile = mCursor.getString(4);
                imgbyte = mCursor.getBlob(6);


                edusername.setText(username);
                edname.setText(name);
                edemail.setText(email);
                edmobile.setText(mobile);
                Bitmap bm;
//                Toast.makeText(getActivity(),imgbyte.toString(),Toast.LENGTH_SHORT).show();
                bm = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
                ivprofile.setImageBitmap(bm);


            } while (mCursor.moveToNext());
        }

//        if (mcur.moveToFirst()) {
//            do {
//
//                imgbyte = mcur.getBlob(1);
//                Bitmap bm;
//                bm = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
//                ivprofile.setImageBitmap(bm);
//            } while (mcur.moveToNext());
//        }




        mCursor.close();
    }

    private void saveData(){


        if(obj.updateuser(Integer.valueOf(userid),username,name,email,mobile,password,imgbyte))
        {
            Toast.makeText(getContext(),"Profile Updated",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(),"Profile Not Updated",Toast.LENGTH_SHORT).show();
        }

//        Intent i =new Intent(SignupActivity.this,MenuActivity.class);
//        startActivity(i);


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
                    //code for deny
                }
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=checkPermission(getActivity());
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
//        Toast.makeText(getContext(),String.valueOf(imgbyte),Toast.LENGTH_SHORT).show();
        ivprofile.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                imgbyte = bytes.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Toast.makeText(getContext(),String.valueOf(imgbyte),Toast.LENGTH_SHORT).show();
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

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
