package br.com.unicamp.ecocar.ecocarcommapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class  LoginActivity extends AppCompatActivity {

    //viewa
    private EditText mEmailLoginEditText;
    private EditText mPasswordLoginEditText;
    private Button mLoginButton;
    private Button mGoToCreateNewAccount;

    //Firebase
    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //assign views
        mEmailLoginEditText = findViewById(R.id.emailLogInEditText);
        mPasswordLoginEditText = findViewById(R.id.passwordLogInEditText);
        mLoginButton = findViewById(R.id.logInButton);
        mGoToCreateNewAccount = findViewById(R.id.goToSignUpButton);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //dialog
        mDialog = new ProgressDialog(this);

        /**listen to login button click and perform Firebase login**/
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailLoginEditText.getText().toString().trim();
                String password = mPasswordLoginEditText.getText().toString().trim();

                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "You must provide email", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "You must provide password", Toast.LENGTH_SHORT).show();
                }else{
                    logInUsers(email, password);
                }
            }
        });

        /**go to create a new account on sign up activity**/
        mGoToCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateNewAccount();
            }
        });
    }

    private void logInUsers(String email, String password){
        mDialog.setMessage("Please wait...");
        mDialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if(!task.isSuccessful()){
                    //error loging
                    Toast.makeText(LoginActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    goToChatUsersActivity();
                }
            }
        });
    }

    private void goToChatUsersActivity(){
        startActivity(new Intent(this, ChatUsersActivity.class));
        finish();
    }

    private void goToCreateNewAccount(){
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
