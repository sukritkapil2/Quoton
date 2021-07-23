package com.dream.quoton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView backBtn;
    private EditText quoteEdit;
    private Spinner spin;
    private Button publishButton;
    private String[] tags = {"confidence", "nature", "lifestyle", "spiritual", "positivity", "general"};
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference quotes = db.collection("quotes");
    private String tagSelected = "confidence";
    private FirebaseAuth mAuth;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        backBtn = findViewById(R.id.back_icon);
        spin = findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        publishButton = findViewById(R.id.publish_btn);
        quoteEdit = findViewById(R.id.quote_edit);
        mAuth = FirebaseAuth.getInstance();
        relativeLayout = findViewById(R.id.main_layout);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tags);
        aa.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spin.setAdapter(aa);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quoteString = quoteEdit.getText().toString();

                if (mAuth.getCurrentUser() != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("quote", quoteString);
                    data.put("tag", tagSelected);
                    data.put("userPic", mAuth.getCurrentUser().getPhotoUrl().toString());
                    data.put("userName", mAuth.getCurrentUser().getDisplayName());
                    data.put("userId", mAuth.getCurrentUser().getUid());
                    data.put("stars", 0);

                    quotes.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            String id = task.getResult().getId();

                            quotes.document("count").update("id_array", FieldValue.arrayUnion(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    quotes.document("count").update("count", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                if(tagSelected.equals("confidence")) {
                                                    quotes.document("count").update("confidence_array", FieldValue.arrayUnion(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            quotes.document("count").update("confidence_count", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Snackbar.make(relativeLayout, "Quote Published!", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else if(tagSelected.equals("lifestyle")) {
                                                    quotes.document("count").update("lifestyle_array", FieldValue.arrayUnion(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            quotes.document("count").update("lifestyle_count", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Snackbar.make(relativeLayout, "Quote Published!", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else if(tagSelected.equals("positivity")) {
                                                    quotes.document("count").update("positivity_array", FieldValue.arrayUnion(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            quotes.document("count").update("positivity_count", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Snackbar.make(relativeLayout, "Quote Published!", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else if(tagSelected.equals("general")) {
                                                    quotes.document("count").update("general_array", FieldValue.arrayUnion(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            quotes.document("count").update("general_count", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Snackbar.make(relativeLayout, "Quote Published!", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else if(tagSelected.equals("spiritual")) {
                                                    quotes.document("count").update("spiritual_array", FieldValue.arrayUnion(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            quotes.document("count").update("spiritual_count", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Snackbar.make(relativeLayout, "Quote Published!", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                } else if(tagSelected.equals("nature")) {
                                                    quotes.document("count").update("nature_array", FieldValue.arrayUnion(id)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            quotes.document("count").update("nature_count", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    Snackbar.make(relativeLayout, "Quote Published!", Snackbar.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tagSelected = tags[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}