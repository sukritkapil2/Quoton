package com.dream.quoton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.dream.quoton.Models.Quote;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private TextView scrollingTags, quote, tag, stars_count, user_name;
    private CircleImageView userPic;
    private ImageView starQuote;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fab;
    private ImageView backButton;
    private RelativeLayout relativeLayout;
    private LinearLayout quoteCard;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference quotes = db.collection("quotes");
    private CollectionReference users = db.collection("users");
    private List<String> idsList;
    private List<Integer> randomIndex;
    private List<Quote> quoteList = new ArrayList<>(0);
    private Boolean allVisited = false;
    private int counter = -1;
    private Random randNum;
    private FirebaseAuth mAuth;

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
        fab = findViewById(R.id.add_btn);
        stars_count = findViewById(R.id.stars_count);
        userPic = findViewById(R.id.user_pic);
        user_name = findViewById(R.id.name);
        mAuth = FirebaseAuth.getInstance();
        starQuote = findViewById(R.id.star_quote);


        scrollingTags.setSelected(true);
        bottomNavigationView.setSelectedItemId(R.id.invisible);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(HomeActivity.this, AddActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, StartActivity.class));
                }

            }
        });

        quotes.document("count").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int count = (int) task.getResult().getLong("count").intValue();
                    String headline = task.getResult().get("headline").toString();
                    scrollingTags.setText(headline);
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

                if(counter == -1) counter = 0;

                if (counter >= randomIndex.size()) {
                    counter = 0;
                    if(!allVisited) {
                        Toast.makeText(HomeActivity.this, "You have seen it all \uD83D\uDE0A!", Toast.LENGTH_SHORT).show();
                        allVisited = true;
                    }
                }

                String id = idsList.get(randomIndex.get(counter));

                if (quoteList.size() != randomIndex.size()) {
                    quotes.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Quote quoteObj = new Quote(task.getResult().get("quote").toString(), task.getResult().get("tag").toString(), task.getResult().get("userPic").toString(), task.getResult().get("userName").toString(), task.getResult().get("userId").toString(), task.getResult().getLong("stars").intValue(),false);
                                quoteList.add(quoteObj);
                                counter++;
                                quoteCard.startAnimation(animation);
                                v.vibrate(50);
                                quote.setText(quoteObj.getQuote());
                                tag.setText(quoteObj.getTag().toUpperCase());
                                stars_count.setText("∞ " + quoteObj.getStars() + " Stars ∞");
                                Glide.with(getApplicationContext()).load(quoteObj.getUserPic()).into(userPic);
                                user_name.setText(quoteObj.getUserName());

                                quotes.document(id).collection("starred").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if(documentSnapshot.exists() && documentSnapshot.getBoolean("saved")) {
                                                quoteList.get(counter-1).setSaved(true);
                                                starQuote.setBackgroundResource(R.drawable.ic_baseline_star_24);
                                            } else {
                                                starQuote.setBackgroundResource(R.drawable.ic_baseline_star_border_24);
                                            }
                                        }
                                    }
                                });

                                if (quoteObj.getTag().equals("nature")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nature));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_nature));
                                } else if (quoteObj.getTag().equals("lifestyle")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lifestyle));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_lifestyle));
                                } else if (quoteObj.getTag().equals("confidence")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.confidence));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_confidence));
                                } else if (quoteObj.getTag().equals("spiritual")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spiritual));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_spiritual));
                                } else if (quoteObj.getTag().equals("positivity")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.positivity));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_positivity));
                                } else if (quoteObj.getTag().equals("general")) {
                                    relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.general));
                                    quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_general));
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
                    stars_count.setText("∞ " + quoteObj.getStars() + " Stars ∞");
                    if(quoteObj.getSaved()) {
                        quoteList.get(counter-1).setSaved(true);
                        starQuote.setBackgroundResource(R.drawable.ic_baseline_star_24);
                    } else {
                        starQuote.setBackgroundResource(R.drawable.ic_baseline_star_border_24);
                    }
                    Glide.with(getApplicationContext()).load(quoteObj.getUserPic()).into(userPic);
                    user_name.setText(quoteObj.getUserName());
                    if (quoteObj.getTag().equals("nature")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nature));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_nature));
                    } else if (quoteObj.getTag().equals("lifestyle")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.lifestyle));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_lifestyle));
                    } else if (quoteObj.getTag().equals("confidence")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.confidence));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_confidence));
                    } else if (quoteObj.getTag().equals("spiritual")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.spiritual));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_spiritual));
                    } else if (quoteObj.getTag().equals("positivity")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.positivity));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_positivity));
                    } else if (quoteObj.getTag().equals("general")) {
                        relativeLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.general));
                        quoteCard.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.quote_card_general));
                    }
                }
            }
        });

        starQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tempCounter = counter-1;

                if(quoteList.get(tempCounter).getSaved()) {
                    stars_count.setText("∞ " + (quoteList.get(tempCounter).getStars()-1) + " Stars ∞");
                    starQuote.setBackgroundResource(R.drawable.ic_baseline_star_border_24);
                    quotes.document(idsList.get(tempCounter)).collection("starred").document(mAuth.getCurrentUser().getUid()).update("saved", false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            quoteList.get(tempCounter).setStars(quoteList.get(tempCounter).getStars()-1);
                            quoteList.get(tempCounter).setSaved(false);
                        }
                    });
                } else {
                    starQuote.setBackgroundResource(R.drawable.ic_baseline_star_24);
                    stars_count.setText("∞ " + (quoteList.get(tempCounter).getStars()+1) + " Stars ∞");
                    Map<String, Object> data = new HashMap<>();
                    data.put("saved", true);
                    quotes.document(idsList.get(tempCounter)).collection("starred").document(mAuth.getCurrentUser().getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            quotes.document(idsList.get(tempCounter)).update("stars", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    quoteList.get(tempCounter).setStars(quoteList.get(tempCounter).getStars()+1);
                                    quoteList.get(tempCounter).setSaved(true);
                                }
                            });
                        }
                    });
                }
            }
        });
    }


}