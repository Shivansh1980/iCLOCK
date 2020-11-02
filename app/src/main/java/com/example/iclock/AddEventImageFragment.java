package com.example.iclock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
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

        //This will be used when user clicks on back button without filling the event form so the previous image which he selected will be displayed.
        if(image_uri != null)
            Picasso.get().load(image_uri).into(image_after_upload);;

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (image_uri == null) {
                    Toast.makeText(context, "Please Select Image to Proceed", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("Image-Uri",image_uri.toString());
                    navController.navigate(R.id.action_addEventImageFragment_to_addEventFragment,bundle);
                }
            }
        });
        return root;
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

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}