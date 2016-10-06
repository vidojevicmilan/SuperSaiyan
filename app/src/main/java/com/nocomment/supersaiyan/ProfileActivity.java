package com.nocomment.supersaiyan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
//import com.firebase.client.utilities.Base64;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import static com.nocomment.supersaiyan.R.id.imageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ProfileActivity extends AppCompatActivity {

    private Button submitButton;
    private EditText usernameEditText;
    private EditText phoneEditText;
    private ImageView profilePicture;
    private Bitmap bmp;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile);

        usernameEditText = (EditText)findViewById(R.id.username_edit_text);
        phoneEditText = (EditText)findViewById(R.id.phone_edit_text);
        profilePicture = (ImageView)findViewById(R.id.profile_picture_view);

        submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase user = ((SuperSaiyanApplication)getApplication()).getMyFirebase().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                usernameEditText = (EditText)findViewById(R.id.username_edit_text);
                phoneEditText = (EditText)findViewById(R.id.phone_edit_text);

                user.child("username").setValue(usernameEditText.getText().toString());
                user.child("phone").setValue(phoneEditText.getText().toString());
                if(encodedImage == null)
                    Toast.makeText(getApplicationContext(),"Select image first!",Toast.LENGTH_SHORT).show();
                else{
                    user.child("image").setValue(encodedImage);
                }
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }
        });

        Firebase base = ((SuperSaiyanApplication)getApplication()).getMyFirebase().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        base.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                usernameEditText.setText(dataSnapshot.child("username").getValue(String.class));
                phoneEditText.setText(dataSnapshot.child("phone").getValue(String.class));
                byte[] decodedString = Base64.decode(user.getImage(),Base64.DEFAULT);

                Bitmap bmp = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                encodedImage = encodeBitmapImage(bmp);
                profilePicture.setImageBitmap(bmp);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

//            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            cursor.close();

            try {
                this.bmp = getBitmapFromUri(selectedImage);
                bmp = ThumbnailUtils.extractThumbnail(this.bmp, 640, 640);
                profilePicture.setImageBitmap(bmp);
                encodedImage = encodeBitmapImage(this.bmp);
                Toast.makeText(this,"Image selected! "+ ((float)bmp.getByteCount())/1024 + " KBs", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeBitmapImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


}
