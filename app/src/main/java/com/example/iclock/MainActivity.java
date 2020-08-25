package com.example.iclock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import static java.lang.Thread.sleep;
//ignore the above classes many classes has been added by the api of firebase to create sign in button.

//Remember this activity is for login and later on we will give correct name for every activity of iCLOCK
public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static int RC_SIGN_IN = 120;    //varriable which google checks with requestCode if it matches or not, ignore this also
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting Firebase Authentication Instance to create new users as well as for login , awesome right?
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        mAuth = FirebaseAuth.getInstance();

        //Load Animations from anim directory
        Animation top_animation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        Animation bottom_animation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        Animation scale_animation = AnimationUtils.loadAnimation(this,R.anim.scale_animation);
        Animation rotate_animation = AnimationUtils.loadAnimation(this,R.anim.rotate_animation);

        //finding layout to animate by using the animations available in java
        ConstraintLayout username_pass_layout = findViewById(R.id.constraint_layout);
        TextView text = findViewById(R.id.iclock_text);
        Button signin_button = findViewById(R.id.login_button);
        SignInButton signin_google = findViewById(R.id.signInButton);
        TextView orLoginWith = findViewById(R.id.or_login_with);

        //Animating the layouts by using the setanimation method available in java
        username_pass_layout.setAnimation(bottom_animation);
        text.setAnimation(top_animation);
        signin_button.setAnimation(scale_animation);
        signin_google.setAnimation(bottom_animation);
        orLoginWith.setAnimation(scale_animation);
        signin_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception exception = task.getException();
            Log.d("SignInActivity", "Exception From Task has been taken and now going to check if task is successfull");
            if(task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    Log.d("SignInActivity", "Task is successfull now we will will get the user id using GoogleSignInAccount account");
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d("SignInActivity", "firebaseAuthWithGoogle user ID : :" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("SignInActivity", "Google sign in failed", e);
                    // ...
                }
            }
            else{
                Log.w("SignInActivity", exception.toString());
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInActivity", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            openDashboard();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInActivity", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }
    private void openDashboard(){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}