package com.example.student.socialintegration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;
   LoginButton btnLogin;
   TextView tvname,tvemail,tvgender,tvbday;
   ProfilePictureView profilePictureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());

        tvname = (TextView) findViewById(R.id.textView_name);
        tvemail=(TextView) findViewById(R.id.textView_email);
        tvgender = (TextView) findViewById(R.id.textView_gender);
        tvbday = (TextView)findViewById(R.id.textView_bday);
        profilePictureView = (ProfilePictureView)findViewById(R.id.propic);
        btnLogin = (LoginButton) findViewById(R.id.login_button);
        btnLogin.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));

      callbackManager = CallbackManager.Factory.create();

        btnLogin.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("Main", response.toString());
                                        setProfileToView(object);
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this,"Login Cancel",Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void setProfileToView(JSONObject jsonObject) {
        try {
            tvname.setText(jsonObject.getString("name"));
            tvemail.setText(jsonObject.getString("email"));
            tvgender.setText(jsonObject.getString("gender"));
            tvbday.setText(jsonObject.getString("birthday"));

            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
            profilePictureView.setProfileId(jsonObject.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
