package com.example.iclock;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iclock.dummy.CreateBook;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class BooksFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private List<CreateBook> bookList;
    private Context context;
    private NavController navController;
    private static String TAG = "checkforupload";
    private DatabaseReference databaseReference;
    private BookRecyclerViewAdapter bookRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    public BooksFragment() {

    }

    public static BooksFragment newInstance(int columnCount) {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books_list, container, false);

        //getting below things to pass them in our recycler view
        context = container.getContext();
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        bookList = new ArrayList<>();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading Books");
        progressDialog.setMessage("Please Wait this may take few seconds....");
        progressDialog.setCancelable(false);

        recyclerView = view.findViewById(R.id.list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        CreateBook book = new CreateBook();
        book.setBookName("Introduction to Algorithm");
        book.setBookDescription("This is the best book to learn algorithm and data structure");
        book.setBookImageUrl("https://firebasestorage.googleapis.com/v0/b/iclock-690ad.appspot.com/o/Books_Details%2F1604475454718.webp?alt=media&token=c13847e9-9e38-4682-a2bc-fc98450f2406");
        bookList.add(book);

        databaseReference = FirebaseDatabase.getInstance().getReference("Books_Details");
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: Going To Retrive Data Of Book From Firebases");
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    final CreateBook createBook = postSnapshot.getValue(CreateBook.class);
                    Log.d(TAG, "onDataChange: adding book to book list");
                    bookList.add(createBook);
                    for(CreateBook book : bookList){
                        Log.d(TAG, "onCreateView: "+book.getBookName());
                    }
                    continue;
                }
                bookRecyclerViewAdapter = new BookRecyclerViewAdapter(bookList, context, navController);
                recyclerView.setAdapter(bookRecyclerViewAdapter);
                progressDialog.dismiss();
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "onCreateView: Going to call Recycler View of Book with the following booklist : ");
        for(CreateBook books : bookList){
            Log.d(TAG, "onCreateView: "+books.getBookName());
        }
        Log.d(TAG, "onCreateView: Size of Book List : "+bookList.size());
        return view;
    }
}