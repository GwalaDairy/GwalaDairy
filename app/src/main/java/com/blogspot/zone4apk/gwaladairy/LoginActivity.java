package com.blogspot.zone4apk.gwaladairy;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // To check the authenticity of this appplication the default username is "abc@gmail.com" and
    // default password is "123456"
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText emailField;
    EditText passField;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.user_email);
        passField = findViewById(R.id.user_password);
        progressBar = findViewById(R.id.progress_bar);


        findViewById(R.id.sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onclick listener for signin buttton
                signIn(emailField.getText().toString(), passField.getText().toString());
            }
        });
        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onclick listener for signup hyperlink
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private boolean validateForm() {
        //This function validates the form credentials
        return true;
        //considering that form is already validated.
    }

    private void signIn(String email, String password) {
        //Function deals with the signin action initiated by signin button
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void showProgressDialog() {
        //shows circular progressbar
        progressBar.setVisibility(View.VISIBLE);
        findViewById(R.id.sign_in).setVisibility(View.GONE);
    }

    private void updateUI(FirebaseUser user) {
        //deals with the changes occuring due to valid user
        hideProgressDialog();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            this.finish();
        }
    }

    private void hideProgressDialog() {
        //hides circular progressbar
        progressBar.setVisibility(View.GONE);
        findViewById(R.id.sign_in).setVisibility(View.VISIBLE);

    }


}
