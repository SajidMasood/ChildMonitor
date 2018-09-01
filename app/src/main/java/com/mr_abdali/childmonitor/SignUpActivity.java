package com.mr_abdali.childmonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

    // TODO: 8/5/2018 Variables Section...
    private static final String TAG = "SignUpActivity";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // TODO: 8/5/2018 BindView ...
    @BindView(R.id.input_name) EditText mNameText;
    @BindView(R.id.input_email) EditText mEmailText;
    @BindView(R.id.input_password) EditText mPasswordText;
    @BindView(R.id.input_reEnterPassword) EditText mReEnterPasswordText;
    @BindView(R.id.btn_signup) Button mSignUpButton;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        // TODO: 8/7/2018 ToolBar implementation...
        toolbar.setTitle("Parental Registration");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        // TODO: 8/6/2018 When SignUpButton Clicked...
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    // TODO: 8/6/2018 SignUp method ...
    private void signUp() {
        Log.d(TAG , "Sign Up");

        if (!validate()){
            onSignUpFailed();
            return;
        }

        mSignUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onSignUpSuccess();
                        progressDialog.dismiss();
                    }
                }, 1000
        );
    }

    // TODO: 8/6/2018 Singn Up Method... 
    private void onSignUpSuccess() {
        mSignUpButton.setEnabled(true);

        final String userName = mNameText.getText().toString().trim();
        final String userEmail = mEmailText.getText().toString().trim();
        final String userPass = mPasswordText.getText().toString().trim();

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass)){
            mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = mDatabase.child(user_id);

                        current_user.child("Name").setValue(userName);
                        current_user.child("Email").setValue(userEmail);
                        current_user.child("Password").setValue(userPass);

                        Toast.makeText(SignUpActivity.this, "Account Create Successfully!",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        Log.e("ERROR", task.getException().toString());
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }
    }

    // TODO: 8/6/2018 Sign Up Failed Method ...
    private void onSignUpFailed() {
        Toast.makeText(this,"Registration Failed!",Toast.LENGTH_LONG).show();
        mSignUpButton.setEnabled(true);
    }

    // TODO: 8/6/2018 Validation of Edittext ....
    private boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        String reEnterPassword = mReEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 5){
            mNameText.setError("Name Must be at least 5 Characters");
            valid = false;
        }else {
            mNameText.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailText.setError("Enter a Valid Email Address");
            valid = false;
        }else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15){
            mPasswordText.setError("Password Should be Between 6 and 15 Alphanumeric Characters");
            valid = false;
        }else {
            mPasswordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 15 || !(reEnterPassword.equals(password))) {
            mReEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            mReEnterPasswordText.setError(null);
        }
        return valid;
    }


    // new code for back tool button..
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home)
        {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
