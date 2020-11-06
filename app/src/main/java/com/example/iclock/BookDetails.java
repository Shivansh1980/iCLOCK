package com.example.iclock;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.iclock.dummy.CreateBook;

public class BookDetails extends Fragment {
    CreateBook createBook;
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

        //The below createBook object contains the detail of Books so make a layout and show all the details
        //Assume this createBook object contains all details of book that are available in CreateBook class
        //you can get details like this createBook.getBookName(); this will give you book name and now on the
        //layout you can setText it.
        Bundle bundle = getArguments();
        createBook = (CreateBook) bundle.getSerializable("BookDetails");

        return root;
    }
}