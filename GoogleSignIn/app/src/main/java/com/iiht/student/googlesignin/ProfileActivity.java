package com.iiht.student.googlesignin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {
TextView tvname,tvgivenname,tvfamily,tvemail, tvid;
String name, given_name, family_name,email, pid,photo=null;
ImageView propic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        given_name = i.getStringExtra("given_name");
        family_name = i.getStringExtra("family_name");
        email = i.getStringExtra("email");
        pid = i.getStringExtra("id");
        photo = i.getStringExtra("photo");
//        Toast.makeText(getApplicationContext(),photo,Toast.LENGTH_SHORT).show();

        tvname = (TextView) findViewById(R.id.textView_name);
        tvgivenname = (TextView) findViewById(R.id.textView_givenname);
        tvfamily = (TextView) findViewById(R.id.textView_familyname);
        tvemail = (TextView) findViewById(R.id.textView_email);
        tvid = (TextView) findViewById(R.id.textView_id);
        propic = (ImageView) findViewById(R.id.imageView1);

        tvname.setText(name);
        tvgivenname.setText(given_name);
        tvfamily.setText(family_name);
        tvemail.setText(email);
        tvid.setText(pid);

        if(  photo.equals("null") || photo.isEmpty() )
        {
//            Toast.makeText(getApplicationContext(),"photo is empty ",Toast.LENGTH_SHORT).show();
            Drawable placeholder = propic.getContext().getResources().getDrawable(R.drawable.no_user);
            propic.setImageDrawable(placeholder);
        }
        else {
//            Toast.makeText(getApplicationContext(),"photo has data",Toast.LENGTH_SHORT).show();
            new ImageDownloaderTask(propic).execute(photo);
        }


    }



    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        ProgressDialog statusDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusDialog = new ProgressDialog(ProfileActivity.this);
            statusDialog.setMessage("Loading...");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
//                if (statusCode != HttpStatus.SC_OK) {
//                    return null;
//                }

                if (statusCode != 200) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            statusDialog.dismiss();

            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.no_user);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }
}
