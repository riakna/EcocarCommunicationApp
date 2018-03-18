package br.com.unicamp.ecocar.ecocarcommapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.unicamp.ecocar.ecocarcommapp.Models.User;

public class SignUpActivity extends AppCompatActivity {

    //views
    private EditText mNameSignUpEditText;
    private EditText mEmailSignUpEditText;
    private EditText mPasswordEditText;
    private Button mSignUpButton;
    private Button mGoToLoginButton;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDBref;

    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //assign the views
        mNameSignUpEditText = findViewById(R.id.nameSignUpEditText);
        mEmailSignUpEditText = findViewById(R.id.emailSignUpEditText);
        mPasswordEditText = findViewById(R.id.passwordSignUpEditText);
        mSignUpButton = findViewById(R.id.signUpButton);
        mGoToLoginButton = findViewById(R.id.goToLogIn);

        //firebase assign
        mAuth = FirebaseAuth.getInstance();

        //dialog
        mDialog = new ProgressDialog(this);

        /**listen to sign up button click**/
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameSignUpEditText.getText().toString();
                String email = mEmailSignUpEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();


                if(name.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Name cannot be empty!", Toast.LENGTH_SHORT).show();
                }else if(email.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    signUpUserWithFirebase(name, email, password);
                }
            }
        });

        /**listen to go to login button**/
        mGoToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

    }

    private void signUpUserWithFirebase(final String name, String email, String password){
        mDialog.setMessage("Please wait...");
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    //there was an error
                    Toast.makeText(SignUpActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    final FirebaseUser newUser = task.getResult().getUser();
                    //success creating user, now set display name as name
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    newUser.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Log.d(SignUpActivity.class.getName(), "User profile updated.");
                                        /***CREATE USER IN FIREBASE DB AND REDIRECT ON SUCCESS**/
                                        createUserInDb(newUser.getUid(), newUser.getDisplayName(), newUser.getEmail());

                                    }else{
                                        //error
                                        Toast.makeText(SignUpActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    private void createUserInDb(String userId, String displayName, String email){
        mUsersDBref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(userId, displayName, email);
        mUsersDBref.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    //error
                    Toast.makeText(SignUpActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    //success adding user to db as well
                    //go to users chat list
                    goToChartUsersActivity();
                }
            }
        });
    }

    private void goToLoginActivity(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void goToChartUsersActivity(){
        startActivity(new Intent(this, ChatUsersActivity.class));
        finish();
    }
}
