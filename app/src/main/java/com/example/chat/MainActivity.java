package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chat.adapter.ClientMessageAdapter;
import com.example.chat.model.FriendlyMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtmessage;
    private ImageView btnsend;
    private String mobile;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, userReference;
    private ListView mMessageListView;
    private ClientMessageAdapter mClientMessageAdapter;
    private FirebaseAuth mAuth;
    private ChildEventListener childEventListener;
    private FirebaseAuth.AuthStateListener authStateListener;
    String uid;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtmessage = findViewById(R.id.messageEditText);
        if(getIntent().hasExtra("client_uid"))
           uid=getIntent().getStringExtra("client_uid");
        else
            uid=mAuth.getCurrentUser().getUid();

        if(getIntent().hasExtra("mobile"))
           mobile = getIntent().getStringExtra("mobile");
        else if(getIntent().hasExtra("client_mobile"))
            mobile = getIntent().getStringExtra("client_mobile");


        btnsend = findViewById(R.id.sendButton);
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageListView = findViewById(R.id.messageListView);
        mClientMessageAdapter = new ClientMessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mClientMessageAdapter);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("spade").child("clientfrontchat").child(uid);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("spade").child("clientfrontmedia").child(uid);
        userReference = firebaseDatabase.getReference().child(mAuth.getUid());


        edtmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0)
                    btnsend.setEnabled(true);
                else
                    btnsend.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //this firebaseAuth is guaranteed to contain whether at that moment the user is authenticated or not
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize();
                    Toast.makeText(MainActivity.this, "You are succesfully logged in, Welcome to the Chat app!!", Toast.LENGTH_SHORT).show();
                    //user is signed in
                } else {
                    onSignedOutCleanup();
                    //user is not signed in
                    //smartlock allows the phone to automatically save the users credentials and try to log them in so we are setting it ar false
                    //create and launch sign in intent
                    //RC_SIGN_IN is requset code , its a flag for when we return from starting the activity for result

                }
            }

        };
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                Date date = new Date();
                //String sdate = dateFormat.format(date);
             //   Date date = new SimpleDateFormat("hh:mm:ss");
                String date1=new SimpleDateFormat("E, dd MMM yyyy").format(date);
                String time = new SimpleDateFormat("h:mm a").format(date);
                FriendlyMessage friendlyMessage = new FriendlyMessage(edtmessage.getText().toString(), "anjali", mobile, null,date1,time);
                databaseReference.push().setValue(friendlyMessage);
                // Clear input box
                edtmessage.setText("");
            }
        });

    }

    private void onSignedOutCleanup() {

        mClientMessageAdapter.clear();
    }

    private void onSignedInInitialize() {
        //craete and attatch the listener
        //mUsername=username;
        getSupportActionBar().setTitle(mAuth.getCurrentUser().getPhoneNumber());
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    mClientMessageAdapter.add(friendlyMessage);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }

    private void detachDatabseReadListener() {
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(authStateListener);
        detachDatabseReadListener();
        mClientMessageAdapter.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //attatching the listener
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();

        }
        return true;
    }
}
