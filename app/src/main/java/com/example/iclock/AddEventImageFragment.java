package com.example.iclock;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddEventImageFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri image_uri;
    private ImageView image_after_upload;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private Context context;
    private Button next_button;
    private ProgressDialog progressDialog;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_event_image, container, false);
        Button choose_file = root.findViewById(R.id.choose_file_btn);
        image_after_upload = root.findViewById(R.id.image_after_upload);
        next_button = root.findViewById(R.id.next_btn);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait...");

        choose_file.setOnClickListener(new View.OnClickListener() {
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
        storageReference = FirebaseStorage.getInstance().getReference("Events_Details");

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(context, "Upload Already in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    uploadImage();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            image_uri = data.getData();
            //picaso is used to get images from devices
            Picasso.get().load(image_uri).into(image_after_upload);
        }
    }

    public void uploadImage() {
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
                            progressDialog.dismiss();
                            Toast.makeText(context, "Now Fill The Details", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_addEventImageFragment_to_addEventFragment);
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
}