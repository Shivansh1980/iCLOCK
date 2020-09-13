package com.example.iclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>{

    private List<CreateBook> bookList;
    private Context context;
    private CreateBook newBook;
    private NavController navController;

    public MyItemRecyclerViewAdapter(List<CreateBook> items, Context context, NavController navController) {
        bookList = items;
        this.context = context;
        this.navController = navController;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_books, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.mItem = bookList.get(position);
            holder.book_name.setText(bookList.get(position).getBookName());
            holder.book_description.setText(bookList.get(position).getBookDescription());
            Picasso.get().load(bookList.get(position).getBookImageUrl())
                    .fit()
                    .centerCrop()
                    .into(holder.book_image);
            newBook = bookList.get(position);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navController.navigate(R.id.action_booksFragment_to_bookDetails);
                    Toast.makeText(context, "Clicked on "+bookList.get(position).getBookName(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public int getItemCount() {
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
        }

        @Override
        public String toString() {
            return super.toString() + " '" + book_name.getText() + "'";
        }
    }
}