package com.example.iclock;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static android.app.Activity.RESULT_OK;

public class AddEventFragment extends Fragment {

    Calendar myCalendar;
    EditText event_name;
    EditText description;
    EditText start_date;
    EditText end_date;
    EditText contact_number;
    EditText certification;
    EditText optional_details;

    public static final String TAG="CheckForDatabase";
    private CreateUserEvent createUserEvent;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth mAuth;
    private Uri image_uri;
    private ImageView image_after_upload; //This is the image view when user choosed a image and the selected image will be  shown in this image view
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseStorage mFirebaseStorage;
    private Context context;
    private EditText file_name; //edit text for the user to enter file name
    private StorageTask uploadTask; //About this described below
    private ProgressDialog progressDialog;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        context = container.getContext();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait...");

        View root = inflater.inflate(R.layout.fragment_add_event, container, false);

        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);

        //Image Chooseing area
        Button submit = root.findViewById(R.id.submit_btn);
        file_name = root.findViewById(R.id.event_name);
        image_after_upload = root.findViewById(R.id.image_after_upload);
        image_uri = Uri.parse(getArguments().getString("Image-Uri"));

        //All text Fields of input
        event_name = root.findViewById(R.id.event_name);
        description = root.findViewById(R.id.description);
        start_date = root.findViewById(R.id.event_start_date);
        end_date = root.findViewById(R.id.event_end_date);
        contact_number = root.findViewById(R.id.contact_number);
        certification = root.findViewById(R.id.certification_available);
//        optional_details = root.findViewById(R.id.optional_detail);


        //storage references and creating the directories in firebase using below two lines
        storageReference = FirebaseStorage.getInstance().getReference("Events_Details");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(context, "Upload Already in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    createUserEvent = getUserInformationObject();
                    if (createUserEvent != null) {
                        uploadUserInformationToDatabase();
                        progressDialog.show();
                    }
                    else {
                        return;
                    }
                }
            }
        });
        return root;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(image_uri));
    }

    private void uploadUserInformationToDatabase() {
        if (image_uri != null) {
            //this will create a big_number.jpg and when we call .child this means we are
            //going to add something inside Events_Images Directory
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(image_uri));

            //now we will store our image file in firebase and check for success and failure events
            //And we store the refrence of current process in this uploadtask varriable which helps us
            //when user clicks on upload button multiple time, so when he clicks one time uploadTask will
            //take  the reference and when the upload runnig and the user clicks the upload button another
            //time then we put a check if uploadTask is null or not.if it is null then this means no task is
            //running else we don't upload. This check you put above in upload onlicklisterner.

            uploadTask = fileReference.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            lets create the object with name and url of the image and save this object into our database.
//                            String name_of_event = file_name.getText().toString().trim();
//                            String url_of_image = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                            Upload upload = new Upload(name_of_event, url_of_image);

//                            Now you just need to get the url of the image that you have uploaded.
                            Log.d("CheckImageUpload", "onSuccess: Upload Image Successfull");

                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            String url = uri.getResult().toString();
                            createUserEvent.setImageUrl(url);


                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child("Events_Details").child(uploadId).setValue(createUserEvent).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Event created successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Event Created SuccessFully", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.action_addEventFragment_to_eventFragment);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Failed to upload details", Toast.LENGTH_SHORT).show();
                                    StorageReference photoref = mFirebaseStorage.getReferenceFromUrl( createUserEvent.getImageUrl() );
                                    photoref.delete();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed To Upload Check your Connection", Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {
            progressDialog.dismiss();
            Toast.makeText(context, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private CreateUserEvent getUserInformationObject() {

        String eventName = event_name.getText().toString();
        String eventDescription = description.getText().toString();
        String eventStartDate = start_date.getText().toString();
        String eventEndDate = end_date.getText().toString();
        String isCertification = certification.getText().toString();
        String userId = mAuth.getCurrentUser().getUid();
        String contact = contact_number.getText().toString();
        String extraDetails = optional_details.getText().toString();

        int isAllRight = performInfoCheck(eventName, eventDescription, eventStartDate, eventEndDate, contact, isCertification);

        if (isAllRight == 1) {
            CreateUserEvent createUserEvent = new CreateUserEvent();
            createUserEvent.setEventName(eventName);
            createUserEvent.setDescription(eventDescription);
            createUserEvent.setEventStartdate(eventStartDate);
            createUserEvent.setEventEndDate(eventEndDate);
            createUserEvent.setContact(contact);
            createUserEvent.setIsCertificationAvailable(isCertification);
            createUserEvent.setUserId(userId);

            if (extraDetails != "" || extraDetails != null)
                createUserEvent.setOtherDetailOptional(extraDetails);
            return createUserEvent;
        } else
            return null;
    }


    private int performInfoCheck(String eventName, String eventDescription, String startDate, String endDate, String contact, String isCertification) {

        if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(eventDescription) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(contact)) {
            Toast.makeText(context, "You can't leave any field empty", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (eventDescription.length() <= 10) {
            Toast.makeText(context, "Event Description too short", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (contact.length() != 10) {
            Toast.makeText(context, "Please Enter Correct Contact Number", Toast.LENGTH_LONG).show();
            return 0;
        } else if (!validateJavaDate((startDate)) || !validateJavaDate(endDate) ) {
            Toast.makeText(context, "Please Enter The Date In Correct Formate e.g. dd/mm/yyyy", Toast.LENGTH_LONG).show();
            return 0;
        }
        return 1;
    }

    public static boolean validateJavaDate(String strDate) {
        if (strDate.trim().equals("")) {
            return true;
        } else {
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
            sdfrmt.setLenient(false);
            try {
                Date javaDate = sdfrmt.parse(strDate);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }
}