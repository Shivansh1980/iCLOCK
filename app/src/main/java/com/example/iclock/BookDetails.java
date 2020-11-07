package com.example.iclock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iclock.dummy.CreateBook;
import com.squareup.picasso.Picasso;

public class BookDetails extends Fragment {
    CreateBook createBook;
    TextView bookName;
    TextView bookDescription;
    TextView contact;
    TextView bookPrice;
    ImageView bookImage;
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
        bookName = root.findViewById(R.id.book_details_name);
        bookDescription = root.findViewById(R.id.book_details_text_description);
        bookImage = root.findViewById(R.id.book_details_image);
        bookPrice = root.findViewById(R.id.book_details_price);
        contact = root.findViewById(R.id.book_details_contactno);

        Bundle bundle = getArguments(); //Bundle coming from recyclerView
        createBook = (CreateBook) bundle.getSerializable("BookDetails");
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