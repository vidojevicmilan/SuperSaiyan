package com.nocomment.supersaiyan;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreboardActivity extends AppCompatActivity {

    private ArrayList<Score> scores;
    private ListView listView;
    private ScoreListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scoreboard);

        ((SuperSaiyanApplication)getApplication()).getMyFirebase().child("Scoreboard").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                scores = new ArrayList<Score>();
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    final Score score = new Score();

                    score.setXP(itemSnapshot.getValue(String.class));
                    score.setId(itemSnapshot.getKey());

                    Firebase user = ((SuperSaiyanApplication)getApplication()).getMyFirebase().child("Users").child(itemSnapshot.getKey());
                    user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            score.setUsername(dataSnapshot.child("username").getValue(String.class));
                            score.setImage(ProfileActivity.decodeBase64(dataSnapshot.child("image").getValue(String.class)));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                    if(score.getUsername() == null)
                        score.setUsername("N/a");
                    if(score.getImage() == null)
                        score.setImage(ProfileActivity.decodeBase64(getString(R.string.goku_image_base64)));
                    scores.add(score);
                    Collections.sort(scores, new Comparator<Score>(){
                        public int compare(Score c1, Score c2){
                            int num1 = Integer.parseInt(c1.getXP());
                            int num2 = Integer.parseInt(c2.getXP());
                            if(num1 < num2)
                                return 1;
                            else if(num1 > num2)
                                return -1;
                            return 0;
                        }
                    });
                    setScoreImagesAndUsernames(scores);

                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setScoreImagesAndUsernames(ArrayList<Score> scores) {
        final int count = 0;
        for (final Score score: scores) {
            Firebase base = ((SuperSaiyanApplication)getApplication()).getMyFirebase().child("Users").child(score.getId());
            base.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    score.setUsername(dataSnapshot.child("username").getValue(String.class));
                    score.setImage(ProfileActivity.decodeBase64(dataSnapshot.child("image").getValue(String.class)));

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        listView = (ListView)findViewById(R.id.list);
        adapter = new ScoreListAdapter(ScoreboardActivity.this, scores, ScoreboardActivity.this);
        listView.setAdapter(adapter);
    }
}
