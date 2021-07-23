package com.dream.quoton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.quoton.Models.Quote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private TextView scrollingTags, quote, tag, ups_downs, user_name;
    private Button upButton, downButton;
    private BottomNavigationView bottomNavigationView;
    private ImageView backButton;
    private RelativeLayout relativeLayout;
    private LinearLayout quoteCard;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference quotes = db.collection("quotes");
    private List<String> idsList;
    private List<Integer> randomIndex;
    private List<Quote> quoteList = new ArrayList<>(0);
    private Boolean allVisited = false;
    private int counter = -1;
    private Random randNum;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scrollingTags = findViewById(R.id.scrolling_tags);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        backButton = findViewById(R.id.back_icon);
        quoteCard = findViewById(R.id.quote_card);
        quote = findViewById(R.id.quote);
        tag = findViewById(R.id.tag);
        relativeLayout = findViewById(R.id.main_layout);

        scrollingTags.setSelected(true);
        bottomNavigationView.setSelectedItemId(R.id.invisible);

        quotes.document("count").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int count = (int) task.getResult().getLong("count").intValue();
                    idsList = (List<String>) task.getResult().get("id_array");

                    randNum = new Random();
                    Set<Integer> set = new LinkedHashSet<Integer>();
                    set.clear();
                    while (set.size() < count) {
                        set.add(randNum.nextInt(count));
                    }
                    randomIndex = new ArrayList<>(set);
                    Log.i("s", "TEST TEST TEST" + set);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

        quoteCard.setOnTouchListener(new OnSwipeTouchListener(HomeActivity.this) {
            public void onSwipeTop() {
                Animation animation;
                AnimationSet animationSet = new AnimationSet(true);

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -quoteCard.getHeight());
                animate.setDuration(300);
                animate.setFillAfter(false);
                animationSet.addAnimation(animate);
                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(100);
                animationSet.addAnimation(anim);
                animation = animationSet;
                animation.setDuration(300);

                if (counter >= randomIndex.size() || counter == -1) counter = 0;

                String id = idsList.get(randomIndex.get(counter));

                if (quoteList.size() != randomIndex.size()) {
                    quotes.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Quote quoteObj = new Quote(task.getResult().get("quote").toString(), task.getResult().get("tag").toString(), 0, 0, "", "", "");
                                quoteList.add(quoteObj);
                                counter++;
                                quoteCard.startAnimation(animation);
                                v.vibrate(50);
                                quote.setText(quoteObj.getQuote());
                                tag.setText(quoteObj.getTag().toUpperCase());

                                if (quoteObj.getTag().equals("nature")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nature));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_nature));
                                }
                                else if (quoteObj.getTag().equals("lifestyle")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lifestyle));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_lifestyle));
                                }
                                else if (quoteObj.getTag().equals("confidence")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.confidence));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_confidence));
                                }
                                else if (quoteObj.getTag().equals("spiritual")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spiritual));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_spiritual));
                                }
                            }
                        }
                    });
                } else {
                    Quote quoteObj = quoteList.get(counter);
                    counter++;
                    quoteCard.startAnimation(animation);
                    v.vibrate(50);
                    quote.setText(quoteObj.getQuote());
                    tag.setText(quoteObj.getTag().toUpperCase());
                    if (quoteObj.getTag().equals("nature")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nature));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_nature));
                    }
                    else if (quoteObj.getTag().equals("lifestyle")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lifestyle));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_lifestyle));
                    }
                    else if (quoteObj.getTag().equals("confidence")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.confidence));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_confidence));
                    }
                    else if (quoteObj.getTag().equals("spiritual")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spiritual));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_spiritual));
                    }
                }
            }
        });
    }
}