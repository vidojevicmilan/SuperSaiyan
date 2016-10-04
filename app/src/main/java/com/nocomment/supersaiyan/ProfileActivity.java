package com.nocomment.supersaiyan;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.utilities.Base64;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);

        Firebase base = ((SuperSaiyanApplication)getApplication()).getMyFirebase().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        base.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                ((EditText)findViewById(R.id.username_edit_text)).setText(dataSnapshot.child("username").getValue(String.class));
                ((EditText)findViewById(R.id.phone_edit_text)).setText(dataSnapshot.child("phone").getValue(String.class));
                try {
                    byte[] decodedString = Base64.decode(user.getImage(),Base64.NO_OPTIONS);
                    ((ImageView)findViewById(R.id.profile_picture_view)).setImageBitmap(BitmapFactory.decodeByteArray(decodedString,0,decodedString.length));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }


}
