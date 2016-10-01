package com.nocomment.supersaiyan;

import android.app.Application;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by milanvidojevic on 10/1/2016.
 */

public class SuperSaiyanApplication extends Application {

    public Firebase getMyFirebase() {
        return myFirebase;
    }

    public void setMyFirebase(Firebase myFirebase) {
        this.myFirebase = myFirebase;
    }

    Firebase myFirebase;

    @Override
    public void onCreate() {
        super.onCreate();

        // Firebase initialization
        Firebase.setAndroidContext(this);
        myFirebase = new Firebase("https://super-saiyan.firebaseio.com/");



    }
}
