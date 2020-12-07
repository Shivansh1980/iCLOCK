package com.example.iclock;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iclock.dummy.CreateBook;
import com.example.iclock.dummy.DummyContent.DummyItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>{

    private List<CreateBook> bookList;
    private Context context;
    private NavController navController;
    private static String TAG = "CheckRecyclerView";

    public BookRecyclerViewAdapter(List<CreateBook> items, Context context, NavController navController) {
        bookList = items;
        this.context = context;
        this.navController = navController;
        Log.d(TAG, "BookRecyclerViewAdapter: Constructor Called with the Following Information");
        for(CreateBook book : bookList){
            Log.d(TAG, "BookRecyclerViewAdapter: Book Name : "+book.getBookName());
        }
        Log.d(TAG, "BookRecyclerViewAdapter: context : "+context);
        Log.d(TAG, "BookRecyclerViewAdapter: navconctroller : "+navController);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_books, parent, false);
        Log.d(TAG, "onCreateViewHolder: Inflated the fragment books layout and now returning the view");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookRecyclerViewAdapter.ViewHolder holder, int position) {
        final CreateBook createBook = bookList.get(position);
        if(createBook == null){
            Toast.makeText(context, "No Books Found", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "onBindViewHolder: Name of the Book : "+bookList.get(position).getBookName());

        holder.mItem = bookList.get(position);
        holder.book_name.setText("Name: "+bookList.get(position).getBookName());
        holder.book_description.setText("Description: "+bookList.get(position).getBookDescription());

        Log.d(TAG, "onBindViewHolder: Book List Image Url : "+bookList.get(position).getBookImageUrl());

        Picasso.get().load(
                bookList.get(position).getBookImageUrl()
                )
                .fit()
                .centerCrop()
                .into(holder.book_image);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle= new Bundle();
                bundle.putSerializable("BookDetails",createBook);
                navController.navigate(R.id.action_booksFragment_to_bookDetails,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: Book item count : "+ bookList.size());
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView book_name;
        TextView book_description;
        ImageView book_image;

        public CreateBook mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            book_name = (TextView) view.findViewById(R.id.book_name);
            book_description = view.findViewById(R.id.book_description);
            book_image = view.findViewById(R.id.book_image);
            Log.d(TAG, "ViewHolder: Constructor Of ViewHolder Class Has Been Called And References To the images of layout has been taken");
        }

        @Override
        public String toString() {
            return super.toString() + " '" + book_name.getText() + "'";
        }
    }
}