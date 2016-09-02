package com.example.parkhanee.mytravelapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;

/**
 * Created by parkhanee on 2016. 8. 31..
 */
public class FbLoginFragment extends Fragment{
    public FbLoginFragment(){   }

    CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private AccessTokenTracker tokenTracker;
    LoginButton loginButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { //get arguments and set them as private variables
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create(); //create a callback manager to handle login responses
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                if (newProfile != null) {
                    //TODO the user has logged out
                } else {
                    //TODO the user may have logged in or changed some of his profile settings
                }
            }
        };
        profileTracker.startTracking();
        tokenTracker.startTracking();
    }

    public static AccessToken getCurrentAccessToken(){
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token;
    }

    public static Profile getCurrentProfile(){
        Profile profile = Profile.getCurrentProfile();
        return profile;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fb_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loginButton = (LoginButton) view.findViewById(R.id.login_button); //TODO : put it in onCREATE or OnCreateView?>??

        //TODO: get some more permissions from the user
        //loginButton.setReadPermissions("user_friends");
        //loginButton.setReadPermissions("email");
        //You can customize the properties of Login button
        //includes LoginBehavior, DefaultAudience, ToolTipPopup.Style and permissions on the LoginButton

        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) { //when the user newly logged in
                // App code
                String string = loginResult.getAccessToken().getUserId();
                //MainActivity.login(string);
                MainActivity.ifFbLogged=true;
                //send boolean to Main activity , to save user info into shared preference in Main Activity
                Intent a = new Intent(getContext(),MainActivity.class);
                startActivity(a);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            Log.v("profile track", (DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(loginResult.getAccessToken().getExpires())));
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                Log.v("profile track","onCompleted");

                                String name = object.getString("name");
                                String email = object.getString("email");
                                String id = object.getString("id");
                                Toast.makeText(getContext(), name + " " + " " + email + " " + id, Toast.LENGTH_SHORT).show();
                                Log.v("profile track","name + \" \" + \" \" + email + \" \" + id");

                    /*write  your code  that is to be executed after successful login*/


                            } catch (JSONException ex) {
                                ex.printStackTrace();
                                Log.v("profile track","json exception");
                            }
                        }
                    });
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException e) {
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //얘가 없으면 로그인 하고나서 로그인 버튼이 "로그아웃"으로 바뀌지 않으ㅁ //it passes the result to the CallbackManager
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
