package com.mr_abdali.childmonitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    // TODO: 8/5/2018 Variables section....
    private static final String TAG = "LoginActivity";
    public static final String EXTRA_MESSAGE = "ParentId";
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // TODO: 8/5/2018 BindView....
    @BindView(R.id.input_email) EditText mEmailText;
    @BindView(R.id.input_password) EditText mPasswordText;
    @BindView(R.id.btn_login) Button mLoginButton;
    @BindView(R.id.link_forgotPassword) TextView mForgotPass;
    @BindView(R.id.link_signup) TextView mSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // TODO: 8/5/2018 Butterknife Bind
        ButterKnife.bind(this);

        // TODO: 8/5/2018 DATABASE REFERENCE...
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        // TODO: 8/5/2018 when pressed Login button
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        // TODO: 8/5/2018 When SignUp link Clicked ...
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        // TODO: 8/5/2018 WHEN Forgot link CLicked ...
        mForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    // TODO: 8/5/2018 Login() method start...
    private void login() {
        Log.d(TAG, "Login");

        if (!validate()){
            onLoginFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        onLoginSuccess();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        //onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 1000
        );
    }

    // TODO: 8/5/2018 onLoginSuccess method ....
    private void onLoginSuccess() {
        mLoginButton.setEnabled(true);

        String email = mEmailText.getText().toString().trim();
        String pass  = mPasswordText.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        Intent intent = new Intent(LoginActivity.this, ChildActivity.class);
                        intent.putExtra(EXTRA_MESSAGE,user_id);
                        Toast.makeText(LoginActivity.this,"LogIn Successfully...",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "User Not Exist!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    // TODO: 8/5/2018 onLoginFailed method ..
    private void onLoginFailed() {
        Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show();
        mLoginButton.setEnabled(true);
    }

    // TODO: 8/5/2018 Validation ....
    private boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString().trim();
        String pass =  mPasswordText.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailText.setError("Enter a Valid Email Address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 6 || pass.length() > 15){
            mPasswordText.setError("Between 6 and 15 Alphanumeric Characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }
        return valid;
    }
}
