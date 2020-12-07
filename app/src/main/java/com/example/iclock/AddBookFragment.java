package com.example.iclock;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.iclock.dummy.CreateBook;
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


public class AddBookFragment extends Fragment {
    EditText book_name;
    EditText book_description;
    EditText publishing_year;
    EditText price;
    EditText contact_number;
    EditText owner_name;
    EditText optional_details;
    EditText bookForSemester;
    EditText bookForBranch;

    public static final String TAG="CheckForDatabase";
    private CreateBook createUserBook;
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

        View root = inflater.inflate(R.layout.fragment_add_book, container, false);

        navController = Navigation.findNavController(getActivity(),R.id.nav_host_fragment);

        //Image Chooseing area
        Button submit = root.findViewById(R.id.submit_btn);
        file_name = root.findViewById(R.id.book_name);
        image_after_upload = root.findViewById(R.id.image_after_upload);
        image_uri = Uri.parse(getArguments().getString("Image-Uri"));

        //All text Fields of input
        book_name = root.findViewById(R.id.book_name);
        book_description = root.findViewById(R.id.book_description);
        publishing_year = root.findViewById(R.id.book_publishing_year);
        price = root.findViewById(R.id.price);
        owner_name = root.findViewById(R.id.owner_name);
        contact_number = root.findViewById(R.id.contact_number);
        optional_details = root.findViewById(R.id.book_optional_details);
        bookForSemester = root.findViewById(R.id.book_for_semester);
        bookForBranch = root.findViewById(R.id.book_for_branch);


        //storage references and creating the directories in firebase using below two lines
        storageReference = FirebaseStorage.getInstance().getReference("Books_Details");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(context, "Upload Already in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    createUserBook = getUserInformationObject();
                    if (createUserBook != null) {
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
                            createUserBook.setBookImageUrl(url);


                            String uploadId = databaseReference.push().getKey();

                            databaseReference.child("Books_Details").child(uploadId).setValue(createUserBook).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Book added successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Book added SuccessFully", Toast.LENGTH_SHORT).show();
                                    navController.navigate(R.id.action_addBookFragment_to_booksFragment);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Failed to upload details", Toast.LENGTH_SHORT).show();
                                    StorageReference photoref = mFirebaseStorage.getReferenceFromUrl( createUserBook.getBookImageUrl() );
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

    private CreateBook getUserInformationObject() {
        String bookName = book_name.getText().toString();
        String bookDescription = book_description.getText().toString();
        String Price = price.getText().toString();
        String Owner_name = owner_name.getText().toString();
        String Publishing_year = publishing_year.getText().toString();
        String userId = mAuth.getCurrentUser().getUid();
        String contact = contact_number.getText().toString();
        String extraDetails = optional_details.getText().toString();
        String bookSemester = bookForSemester.getText().toString();
        String bookBranch = bookForBranch.getText().toString();
        if(extraDetails == null ){
            extraDetails = "";
        }

        int isAllRight = performInfoCheck(bookName, bookDescription, Price,  contact, Publishing_year);

        if (isAllRight == 1) {
            CreateBook createUserBook = new CreateBook();

            createUserBook.setBookName(bookName);
            createUserBook.setBookDescription(bookDescription);
            createUserBook.setBookPrice(Price);
            createUserBook.setBookOwner(Owner_name);
            createUserBook.setPublishingYear(Publishing_year);
            createUserBook.setContact(contact);
            createUserBook.setUserId(userId);
            Log.d(TAG, "getUserInformationObject: UseriD : "+userId);
            createUserBook.setBookForSemester(bookSemester);
            createUserBook.setBookForBranch(bookBranch);

            if (extraDetails != "" || extraDetails != null)
                createUserBook.setOtherDetailOptional(extraDetails);
            return createUserBook;
        } else
            return null;
    }


    private int performInfoCheck(String bookName, String bookDescription, String price, String contact, String publishingYear) {

        if (TextUtils.isEmpty(bookName) || TextUtils.isEmpty(bookDescription) || TextUtils.isEmpty(price) || TextUtils.isEmpty(publishingYear) ||  TextUtils.isEmpty(contact)) {
            Toast.makeText(context, "You can't leave any field empty", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (bookDescription.length() <= 10) {
            Toast.makeText(context, "Book Description too short", Toast.LENGTH_SHORT).show();
            return 0;
        } else if (contact.length() != 10) {
            Toast.makeText(context, "Please Enter Correct Contact Number", Toast.LENGTH_LONG).show();
            return 0;
        }

        return 1;
    }



}