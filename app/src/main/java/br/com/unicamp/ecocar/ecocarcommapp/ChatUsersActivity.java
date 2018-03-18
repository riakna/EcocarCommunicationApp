package br.com.unicamp.ecocar.ecocarcommapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.unicamp.ecocar.ecocarcommapp.Models.User;
import br.com.unicamp.ecocar.ecocarcommapp.Util.UsersAdapter;

public class ChatUsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDBRef;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UsersAdapter adapter;
    private List<User> mUsersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_users);

        //assign firebase auth
        mAuth = FirebaseAuth.getInstance();
        mUsersDBRef = FirebaseDatabase.getInstance().getReference().child("Users");
        //initialize the recyclerview variables
        mRecyclerView = findViewById(R.id.usersRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void populaterecyclerView(){
        adapter = new UsersAdapter(mUsersList, this);
        mRecyclerView.setAdapter(adapter);

    }

    private void queryUsersAndAddthemToList(){
        mUsersDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 0){
                    for(DataSnapshot snap: dataSnapshot.getChildren()){
                        User user = snap.getValue(User.class);
                        //if not current user, as we do not want to show ourselves then chat with ourselves lol
                        try {
                            if(!user.getUserId().equals(mAuth.getCurrentUser().getUid())){
                                mUsersList.add(user);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                /**populate listview**/
                populaterecyclerView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkIfUserIsSignIn();

        /**query users and add them to a list**/
        queryUsersAndAddthemToList();
    }



    private void checkIfUserIsSignIn(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
            /**go to login user first**/
            goToSignIn();
        }
    }

    private void goToSignIn(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                logOutuser();
                return true;
            case R.id.userProfile:
                goToUpdateUserProfile();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOutuser(){
        FirebaseAuth.getInstance().signOut();
        //now send user back to login screen
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void goToUpdateUserProfile(){
        startActivity(new Intent(this, UpdateProfileActivity.class));
    }

}
