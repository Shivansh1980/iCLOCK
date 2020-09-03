package com.example.iclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

//here myview holder is the subclass of eventrecyclerviewholder which represent the single dataitem in tne
//recycler view

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<CreateUserEvent> userEvents;

    public EventRecyclerViewAdapter(Context context, List<CreateUserEvent> userEvents) {
        this.userEvents = userEvents;
        this.context = context;
    }

    //whenever RecyclerView wants the item it first call onCreateViewHolder and this onCreateViewHolder
    //inflates the view  and gives that view to our MyHolderClass(how we are giving ? Since we are calling
    //the constructor with view of our event_item_layout) where we get all reference of the Views such as
    //text,image and ViewGroup and after getting all references onCreateViewHolder return that holder
    //which contains refrences to the onBindViewHolder where we sets our data to those Views.
    @NonNull
    @Override
    public EventRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the layout which we have designed for single entity will gets inflated to view and stored as follow
        View view = LayoutInflater.from(context).inflate(R.layout.event_items_layout, parent, false);
        //why we passed view here, the reason is checkout your MyviewHolder Class Constructor created below. Since that class
        //is going to handle view so we have inflated the layout to view and giving it to the view holder
        return new MyViewHolder(view);
    }

    //here is the main thing taht we will do that is set the things to our layouts. This onViewBinder gives us the
    //position of the view
    @Override
    public void onBindViewHolder(@NonNull EventRecyclerViewAdapter.MyViewHolder holder, int position) {
        final CreateUserEvent createUserEvent = userEvents.get(position);
        if (userEvents.get(position) == null) {
            Toast.makeText(context, "Founds Null Object", Toast.LENGTH_SHORT).show();
            return;
        }
        holder.eventName.setText("Event Name: ");
        holder.eventName.append(userEvents.get(position).getEventName());
        holder.startDate.setText("start date: ");
        holder.startDate.append(userEvents.get(position).getEventStartdate());
        Picasso.get().load(userEvents.get(position)
                .getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.eventImage);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You have Clicked on " + createUserEvent.getEventName() + " event", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userEvents.size();
    }

    //The below class represent the one independent view which is shown in our RecyclerView
    //For example if you want only text View then we can set text View to the recycler view as a single entity

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName;
        TextView startDate;
        ImageView eventImage;
        View mView;

        //this View is coming from onCreatedViewHolder.
        public MyViewHolder(View itemView) {
            super(itemView);
            //this is the itemView inflated from our event_item_layout and that's why we can get
            // id's by using ths itemView as reference as shown
            eventName = itemView.findViewById(R.id.event_name_card);
            startDate = itemView.findViewById(R.id.start_date);
            eventImage = itemView.findViewById(R.id.event_image);
            mView = itemView;
        }
    }
}
