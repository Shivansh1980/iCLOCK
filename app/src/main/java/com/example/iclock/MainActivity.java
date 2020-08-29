package com.example.iclock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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
    final String Tag = "SignInIntent";
    private static int RC_SIGN_IN = 120;    //varriable which google checks with requestCode if it matches or not, ignore this also
    private Button signup;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ignore all below things you can just press ctrl+f and type in the searchbox "Dheeraj" and then just below the comment which you searched you get
        //there signup onclicklistener you need to create a new activity or fragment which should be opened when user click that on signup button and
        //later on you can design that acitvity or fragmemnt by yourself.

        //if you want from me to setup things for you then tell me. I will setup and then you will just need to design page.

        progressDialog = new ProgressDialog(this);
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
        Button login_button = findViewById(R.id.login_button);
        SignInButton signin_google = findViewById(R.id.signInButton);
        TextView orLoginWith = findViewById(R.id.or_login_with);

        //Animating the layouts by using the setanimation method available in java
        username_pass_layout.setAnimation(bottom_animation);
        text.setAnimation(top_animation);
        login_button.setAnimation(scale_animation);
        signin_google.setAnimation(bottom_animation);
        orLoginWith.setAnimation(scale_animation);


        signin_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        //username and password edit text from user
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Logging In");
                progressDialog.setMessage("Please Wait Loading...");
                progressDialog.show();
                EditText user_input = (EditText) findViewById(R.id.username);
                EditText pass_input = (EditText) findViewById(R.id.password);
                String username = user_input.getText().toString();
                String password = pass_input.getText().toString();
                Log.d("SignInActivity","Username and Password = "+username+" "+password);
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    if(password.length()<6){
                        Toast.makeText(MainActivity.this, "Password Length Too Short", Toast.LENGTH_SHORT).show();
                    }
                    else if(!isValidEmail(username)) {
                        Toast.makeText(MainActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        AuthenticateUserAndOpenDashboard(username,password);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Username or Password Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Dheeraj right your signup Code  here, on clicking signup button activity or new fragment should open which take input of registration, leave the backend code on me.
        //and if you want to write backend code then do so.
        signup = findViewById(R.id.sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.Change this Activity To Your New Activity which contains the signup page.
                //2.And in you new Activity, There you will startActivity(DashboardActivity) after user done with signup
                //or you leave after creating page i will do so.

            }
        });

    }

    public void takeabreak(){
        try {
            sleep(1500);
        }catch (Exception e) {
            Toast.makeText(this, "Failed To Wait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            progressDialog.setTitle("Logging In");
            progressDialog.setMessage("Please Wait Loading...");
            progressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    openDashboard();
                }
            },2500);
        }
        else return;
    }

    private void signIn() {
        progressDialog.setTitle("Logging In");
        progressDialog.setMessage("Please Wait Loading...");
        progressDialog.show();
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
                            takeabreak();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    openDashboard();
                                }
                            },1500);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInActivity", "signInWithCredential:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

    //This function is not built-in and has been defined by developers so do not change it, this is taking username and password and authenticating user to dashboard
    private void AuthenticateUserAndOpenDashboard(String username,String password) {

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInIntent", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    openDashboard();
                                }
                            },2000);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInIntent", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    private void openDashboard(){
        Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(intent);
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}