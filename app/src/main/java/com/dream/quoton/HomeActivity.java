package com.dream.quoton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class HomeActivity extends AppCompatActivity {

    private TextView scrollingTags, quote, tag, ups_downs, user_name;
    private Button upButton, downButton;
    private BottomNavigationView bottomNavigationView;
    private ImageView backButton;
    private LinearLayout quoteCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scrollingTags = findViewById(R.id.scrolling_tags);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        backButton = findViewById(R.id.back_icon);
        quoteCard = findViewById(R.id.quote_card);

        scrollingTags.setSelected(true);
        bottomNavigationView.setSelectedItemId(R.id.invisible);

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
                Toast.makeText(HomeActivity.this, "top", Toast.LENGTH_SHORT).show();
            }
        });
    }
}