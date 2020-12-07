package com.example.iclock;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iclock.dummy.CreateBook;
import com.example.iclock.dummy.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class BookDetails extends Fragment {
    CreateBook createBook;
    TextView bookName;
    TextView bookDescription;
    TextView contact;
    TextView bookPrice;
    ImageView bookImage;
    ImageView ownerProfilePic;
    TextView bookOwnerName;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    UserInformation userDetails;
    private static String TAG = "CheckBooks";

    public BookDetails() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_book_details, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("User_Profile_Pictures");
        bookName = root.findViewById(R.id.book_details_book_name);
        bookDescription = root.findViewById(R.id.book_details_text_description);
        bookImage = root.findViewById(R.id.event_details_image);
        bookPrice = root.findViewById(R.id.book_details_price);
        contact = root.findViewById(R.id.book_details_contactno);
        bookOwnerName = root.findViewById(R.id.book_display_owner_name);
        ownerProfilePic = root.findViewById(R.id.owner_profile_pic);

        Bundle bundle = getArguments(); //Bundle coming from recyclerView
        createBook = (CreateBook) bundle.getSerializable("BookDetails");
        databaseReference.child(createBook.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetails = snapshot.getValue(UserInformation.class);
                Picasso.get().load(userDetails.getImageUrl())
                        .centerCrop()
                        .fit()
                        .into(ownerProfilePic);
                bookOwnerName.setText(userDetails.getUserName());
                Log.d(TAG, "onDataChange: "+userDetails.getUserName()+" belongs to college "+userDetails.getcollageName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bookDescription.append(createBook.getBookDescription());
        bookPrice.append(createBook.getBookPrice());
        contact.append(createBook.getContact());
        bookName.append(createBook.getBookName());

        Picasso.get().load(createBook.getBookImageUrl())
                .centerCrop()
                .fit()
                .into(bookImage);
        return root;
    }
}