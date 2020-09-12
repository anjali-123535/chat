package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.adapter.AdminMessageAdapter;
import com.example.chat.model.Client;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMainActivity extends AppCompatActivity {
    private static final String TAG_NAME = AdminMainActivity.class.getName();
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseRecyclerOptions<Client> recyclerOptions;
    FirebaseRecyclerAdapter<Client, ClientViewHolder> adapter;
ArrayList<Client> clientlist;
DatabaseReference clientreference;
    //AdminMessageAdapter madapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        clientlist = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        clientreference=firebaseDatabase.getReference().child("spade").child("clients");
        clientreference.keepSynced(true);
        //  madapter=new AdminMessageAdapter(this);
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
          if(firebaseAuth.getCurrentUser()==null) {
AdminMainActivity.this.finish();
          }

            }
        };
        Query query = firebaseDatabase.getReference().child("spade").child("clients");//.orderByChild("name");

        recyclerOptions =
                new FirebaseRecyclerOptions.Builder<Client>()
                        .setQuery(query, Client.class)
                        .build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        // recyclerView.setAdapter(adapter);
        adapter = new FirebaseRecyclerAdapter<Client, ClientViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ClientViewHolder holder, int position, @NonNull final Client model) {
holder.number.setText(model.getNumber());
holder.name.setText(model.getName());
holder.view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("client_uid",model.getUid());
        intent.putExtra("client_mobile",mAuth.getCurrentUser().getPhoneNumber().substring(3,13));
startActivity(intent);
    }
});
            }

            @NonNull
            @Override
            public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview,parent,false);
                return new ClientViewHolder(view);
            }
        };
recyclerView.setAdapter(adapter);
        //reference=firebaseDatabase.getReference().child("clients");
    /*    reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clientlist.clear();
           if(snapshot!=null)
           {
              for(DataSnapshot dataSnapshot:snapshot.getChildren())
              {
                  Client client=dataSnapshot.getValue(Client.class);
                  clientlist.add(client);
              }
           }
           else{

           }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
         */
    }
            public class ClientViewHolder extends RecyclerView.ViewHolder {
            TextView name,number,uid;
            View view;
            public ClientViewHolder(@NonNull View itemView)
            {
                super(itemView);
                name=itemView.findViewById(R.id.txt_name);
                number=itemView.findViewById(R.id.txt_number);
                view=itemView.findViewById(R.id.card_item);
            }


        }
    private void onSignedOutCleanup() {
     // clientlist.clear();
    adapter.stopListening();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_logout:
                mAuth.signOut();
                finish();

        }
      return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG_NAME,"onStart is called");
        Toast.makeText(this, "noteAdapter is not null", Toast.LENGTH_SHORT).show();
        mAuth.addAuthStateListener(authStateListener);
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG_NAME,"onStop is called");
        mAuth.removeAuthStateListener(authStateListener);
        if(adapter!=null)
        {
            Toast.makeText(this, "noteAdapter is not null", Toast.LENGTH_SHORT).show();
            adapter.stopListening();
        }
    }
}
