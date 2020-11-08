package com.example.iclock;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.iclock.dummy.CreateBook;
import com.example.iclock.dummy.UserInformation;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private List<UserInformation> userList;
    private ImageView profile_picture;
    private UploadTask uploadTask;
    private Uri profilPictureUri;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private View headerView;
    private TextView userEmail;
    private DrawerLayout drawerLayout;
    private NavController navController;
    private NavigationView navigationView;
    private ImageView userImage;
    private static final String TAG = "checkfixes";
    private Toolbar toolbar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        navigationView = findViewById(R.id.nav_menu);

        //setting user information to navheader
        View headerView = navigationView.getHeaderView(0);
        profile_picture = headerView.findViewById(R.id.user_profile_picture);
        userEmail = headerView.findViewById(R.id.user_email);


        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("User_Profile_Pictures");


        String email = user.getEmail();
        userEmail.setText(email);
        if (!setUserProfilePicture()) {
            Toast.makeText(this, "Please Upload you Profile Picture", Toast.LENGTH_SHORT).show();
        }

        profile_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //this will take care that we will see only images when user clicks on choose file
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //now start activity for result and that will return uri of picked image, which we can get using onActiviityResult
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        //setting toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupWithNavController(navigationView, navController);
//        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);

        Log.d(TAG, "onCreate: drawerlayout created");
        //This will create the message box which you are seeing on the dashboard page of our activity we call it FloatingActionButton.

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    private boolean setUserProfilePicture() {
        final boolean[] isChanged = {false};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    final UserInformation userInformation = postSnapshot.getValue(UserInformation.class);
                    String userId = userInformation.getUserId();
                    if (userId == user.getUid() && userInformation.getImageUrl() != "None") {
                        Picasso.get().load(userInformation.getImageUrl())
                                .centerCrop()
                                .fit()
                                .into(profile_picture);
                        isChanged[0] = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return isChanged[0];
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(profilPictureUri));
    }

    private void uploadUserProfilePictureToDatabase(Uri profilPictureUri) {
        if (profilPictureUri != null) {

            final String url = checkIfUserExists();
            Log.d(TAG, "uploadUserProfilePictureToDatabase: "+url);
            if(url == "NotExists"){
                Toast.makeText(this, "Can't Proceed : Your Information is not in database please provide it", Toast.LENGTH_SHORT).show();
                return;
            }
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(profilPictureUri));
            fileReference.putFile(profilPictureUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            final String uploadedImageUrl = uri.getResult().toString();

                            if(url.length() > 10){
                                //this means there is an image already in database
                                storageReference = firebaseStorage.getReferenceFromUrl(url);
                                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(DashboardActivity.this, "Your Image has been removed from database", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            databaseReference.child(user.getUid()).child("imageUrl").setValue(uploadedImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(DashboardActivity.this, "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DashboardActivity.this, "Failed To uplaod", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed To Upload Check your Connection", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteOldImage(String url) {
        if (url != "None" && url != null && url != "") {
            Log.d(TAG, "deleteOldImage: Inside Delete Image : " + url);
            StorageReference photoRef = firebaseStorage.getReferenceFromUrl(url);

            if (photoRef != null) {
                photoRef.delete();
                return true;
            } else return false;
        } else return false;
    }

    String checkIfUserExists() {
        userList = new ArrayList<UserInformation>();
        final String[] url = {"NotExist"};
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Getting all Objects");

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    final UserInformation userInformation = postSnapshot.getValue(UserInformation.class);
                    userList.add(userInformation);
                    if (userInformation.getUserId() == user.getUid()) {
                        Log.d(TAG, "onDataChange: Adding User : "+userInformation.getUserId());
                        url[0] = userInformation.getImageUrl();
                        break;
                    }
                    Log.d(TAG, "onDataChange: Cheking user : "+userInformation.getImageUrl());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return url[0];
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            profilPictureUri = data.getData();
            //picaso is used to get images from devices
            Picasso.get().load(profilPictureUri).into(profile_picture);
            if (profilPictureUri != null) {
                uploadUserProfilePictureToDatabase(profilPictureUri);
            } else
                Toast.makeText(this, "Please Upload Profile Picture, It is not saved try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}