package com.mr_abdali.childmonitor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mr_abdali.childmonitor.Fragments.TabActivity;
import com.mr_abdali.childmonitor.MyChat.ChatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildActivity extends AppCompatActivity {

    // TODO: 8/7/2018 Variables Section declaration....
    public static final String message1 = "ParentId";
    public static final String message2 = "childId";
    String ParentId, user_id;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //private RecyclerView mChildList;

    @BindView(R.id.childList) RecyclerView mChildList;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab_btn) FloatingActionButton btn_fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        ButterKnife.bind(this);

        // TODO: 8/7/2018 ToolBar implementation...
        toolbar.setTitle("Child List");
        toolbar.setSubtitle("Your Added Child List...");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: 8/7/2018 RecyclerView Implementation ...
        //mChildList = (RecyclerView) findViewById(R.id.childList);
        mChildList.setHasFixedSize(true);
        mChildList.setLayoutManager(new LinearLayoutManager(this));

        ParentId = getIntent().getStringExtra("ParentId");

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ChildList").child(ParentId);
        mAuth = FirebaseAuth.getInstance();
       // user_id = mAuth.getCurrentUser().getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(ChildActivity.this,"No Child Founded!",Toast.LENGTH_LONG).show();
                }
            }
        };

        // TODO: 8/7/2018 Floating Action Button Implementation...
        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ParentId = getIntent().getStringExtra("ParentId");
                Intent intent = new Intent(ChildActivity.this, AddChildActivity.class);
                intent.putExtra(message1, ParentId);
                //finish();
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        try{
            mAuth.addAuthStateListener(mAuthListener);
            FirebaseRecyclerAdapter<ChildList,ChildViewHolder> FBRA = new FirebaseRecyclerAdapter<ChildList, ChildViewHolder>(
                    ChildList.class,
                    R.layout.single_name,
                    ChildViewHolder.class,
                    mDatabase
            ) {
                @Override
                protected void populateViewHolder(ChildViewHolder viewHolder, ChildList model, int position) {
                    final String child_key = getRef(position).getKey().toString();

                    viewHolder.setName(model.getName());
                    viewHolder.child_name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ChildActivity.this, TabActivity.class);
                            intent.putExtra(message1,ParentId);
                            intent.putExtra(message2,child_key);
                            startActivity(intent);
                        }
                    });

                    viewHolder.mChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ChildActivity.this, ChatActivity.class);
                            intent.putExtra(message1,ParentId);
                            intent.putExtra(message2,child_key);
                            startActivity(intent);
                        }
                    });

                    viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            AlertDialog.Builder alert = new AlertDialog.Builder(ChildActivity.this);
                            alert.setTitle("Delete entry");
                            alert.setMessage("Are you sure you want to delete?");
                            alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    deleteChildName(child_key);
                                    Toast.makeText(getApplicationContext(),"Delete sucessfully",Toast.LENGTH_SHORT).show();
                                }
                            });
                            alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // close dialog
                                    dialog.cancel();
                                }
                            });
                            alert.show();

                        }
                    });
                }
            };

            mChildList.setAdapter(FBRA);
        }
        catch (Exception e){
            Log.e("error" , "erer" ,e);
        }
    }

    // TODO: 8/7/2018 Delete Child Name Method...
    private void deleteChildName(String child_key) {
        DatabaseReference delete=mDatabase.child(child_key);
        delete.removeValue();
    }


    // TODO: 8/7/2018 Child View Holder Class
    public static class ChildViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView child_name;
        Button mChat, mDelete;

        // TODO: 8/7/2018 ChildViewHolder Constructor...
        public ChildViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            mChat = (Button) mView.findViewById(R.id.chat_icon);
            mDelete = (Button) mView.findViewById(R.id.delete_icon);
        }

        public void setName(String child) {
            child_name = (TextView) mView.findViewById(R.id.child_name);
            child_name.setText(child);
        }
    }
}
