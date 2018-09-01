package com.mr_abdali.childmonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddChildActivity extends AppCompatActivity {

    // TODO: 8/7/2018 Variables declaration section...
    private static final String TAG = "AddChildActivity";
    public static final String EXTRA_MESSAGE = "ParentId";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    public String parentId;
    String userID;

    @BindView(R.id.input_name) EditText mChildName;
    @BindView(R.id.input_age) EditText mChildAge;
    @BindView(R.id.input_email) EditText mChildEmail;
    @BindView(R.id.input_password) EditText mChildPassword;
    @BindView(R.id.input_reEnterPassword) EditText mChildReEnterPassword;
    @BindView(R.id.btn_add_child) Button btnAddChild;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        ButterKnife.bind(this);

        // TODO: 8/7/2018 ToolBar implementation...
        toolbar.setTitle("Registered New Child!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ChildList");

        // TODO: 8/7/2018 Clicked Button Add New Child .....
        btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewChild();
            }
        });
    }

    // TODO: 8/7/2018 Add New Child Method Implementation...
    private void addNewChild() {
        Log.e(TAG, "AddNewChild");

        if (!validate()){
            onAddChildFailed();
            return;
        }

        btnAddChild.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AddChildActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding New Child!...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onAddChildSuccess();
                        progressDialog.dismiss();
                    }
                },1000
        );
    }

    // TODO: 8/7/2018 on Add Child Success method implementation...
    private void onAddChildSuccess() {
        btnAddChild.setEnabled(true);

        parentId = getIntent().getStringExtra("ParentId");
        final String name = mChildName.getText().toString().trim();
        final String age  = mChildAge.getText().toString().trim();
        final String email = mChildEmail.getText().toString().trim();
        final String pass = mChildPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = mDatabase.child(parentId).child(user_id);

                        current_user.child("Name").setValue(name);
                        current_user.child("Age").setValue(age);
                        current_user.child("Email").setValue(email);
                        current_user.child("Password").setValue(pass);

                        Toast.makeText(AddChildActivity.this,"Child Added Successfully!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddChildActivity.this, ChildActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, parentId);
                        finish();
                        startActivity(intent);
                    } else if (!task.isSuccessful()) {
                        Toast.makeText(AddChildActivity.this, "Authentication Failed. try Again!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddChildActivity.this, ChildActivity.class));
                        finish();
                    }
                }
            });
        }
    }

    // TODO: 8/7/2018 onAddChildFailed method implementation....
    private void onAddChildFailed() {
        Toast.makeText(getBaseContext(), "New Child Registration Failed!", Toast.LENGTH_LONG).show();
        btnAddChild.setEnabled(true);
    }

    // TODO: 8/7/2018 Validate method implementation...
    private boolean validate() {
        boolean valid = true;

        String name = mChildName.getText().toString().trim();
        String age  = mChildAge.getText().toString().trim();
        String email = mChildEmail.getText().toString().trim();
        String pass = mChildPassword.getText().toString().trim();
        String rePass = mChildReEnterPassword.getText().toString().trim();

        if (name.isEmpty() || name.length() < 5){
            mChildName.setError("Name Must be at Least 5 Characters!");
            valid = false;
        }else {
            mChildName.setError(null);
        }

        if (age.isEmpty() ){
            mChildAge.setError("Child Age Under 18!");
            valid = false;
        }else {
            mChildAge.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mChildEmail.setError("Enter a Valid Email Address");
            valid = false;
        }else {
            mChildEmail.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 6 || pass.length() > 15 ){
            mChildPassword.setError("Password must be Between 6 and 15 Alphanumeric Characters");
            valid = false;
        }else {
            mChildPassword.setError(null);
        }

        if (rePass.isEmpty() || rePass.length() < 6 || rePass.length() > 15 || !(rePass.equals(pass))){
            mChildReEnterPassword.setError("Password Do not Match Try Again!");
            valid = false;
        }else {
            mChildReEnterPassword.setError(null);
        }
        return valid;
    }



}
