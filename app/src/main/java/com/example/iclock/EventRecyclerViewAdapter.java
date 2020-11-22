package com.example.iclock;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iclock.dummy.UserInformation;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//here myviewholder is the subclass of eventrecyclerviewholder which represent the single dataitem in the
//recycler view
//the next thing is you have implemented here filterable for making search work
public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<CreateUserEvent> userEvents;
    private List<CreateUserEvent> userEventsFullList;//to store all events for search
    private NavController navController;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public EventRecyclerViewAdapter(Context context, List<CreateUserEvent> userEvents, NavController navController) {
        this.userEvents = userEvents;
        this.context = context;
        this.navController = navController;
        userEventsFullList = new ArrayList<>();
        userEventsFullList.addAll(userEvents);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("User_Profile_Pictures");
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
        //userEvent is the array of CreateUserEvent Object

        final CreateUserEvent createUserEvent = userEvents.get(position);
        final UserInformation userInfo = userEvents.get(position).getUserInfo();

        if (userEvents.get(position) == null) {
            Toast.makeText(context, "Founds Null Object", Toast.LENGTH_SHORT).show();
            return;
        }
        holder.eventName.setText("Event Name:");
        holder.eventName.append(userEvents.get(position).getEventName());
        holder.eventOwnerName.setText("Owner Name : ");
        holder.eventOwnerName.append(userEvents.get(position).getEventOwner());

        if(userInfo != null ) {
            if (userInfo.getImageUrl() == null || userInfo.getImageUrl() == "None" || userInfo.getImageUrl() == "") {
                Toast.makeText(context, "Owner Image Not Available", Toast.LENGTH_SHORT).show();
            }
            else{
                Picasso.get().load(userInfo.getImageUrl())
                        .fit()
                        .centerCrop()
                        .into(holder.ownerImage);
            }
        }

        Picasso.get().load(userEvents.get(position).getImageUrl()) //userevents.get(position) will give the clicked object by user onclick the card
                    .fit()
                    .centerCrop()
                    .into(holder.eventImage);


        holder.mView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            //we are able serialize the below CreateUserEvent Class because the class CreateUserEvent implements serializable
            Log.d("Check Bundle", "onClick: ");
            bundle.putSerializable("event_details",createUserEvent);
            navController.navigate(R.id.action_eventFragment_to_eventDetails,bundle);
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
        ImageView eventImage;
        TextView eventOwnerName;
        ImageView ownerImage;
        View mView;

        //this View is coming from onCreatedViewHolder.
        public MyViewHolder(View itemView) {
            super(itemView);
            //this is the itemView inflated from our event_item_layout and that's why we can get
            // id's by using ths itemView as reference as shown
            eventName = itemView.findViewById(R.id.event_name);
            eventImage = itemView.findViewById(R.id.event_image);
            eventOwnerName = itemView.findViewById(R.id.event_owner_name);
            ownerImage = itemView.findViewById(R.id.event_owner_profile_pic);
            mView = itemView;
        }
    }

    @Override
    public Filter getFilter() {
        return userEventFilter;
    }

    private Filter userEventFilter = new Filter() {
        //the method performFiltering works in background thread and hence our ui will not block
        //during its run and publishResults method run on UI.
        //performFiltering return the results which it pass to publishResults and then we can show
        //these reults on UI
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //In the below list we will add filtered events on the basis of charsequence constraint every time when the user type some character
            //this function will be called with the constraint that user typed on the search box
            List<CreateUserEvent> filteredList = new ArrayList<>();

            if(constraint == null || constraint.toString().length() == 0)
                filteredList.addAll(userEventsFullList);
            else {
                String filter = constraint.toString().toLowerCase().trim();
                for(CreateUserEvent createUserEvent : userEventsFullList){
                    if(createUserEvent.getEventName().toLowerCase().contains(filter)){
                        filteredList.add(createUserEvent);
                    }
                }
            }
            //return the filtered list by converting its type to filtered list
            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.d("CHECK_FILTERING", "performFiltering: filteredList : "+results.values+"\n\n");
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userEvents.clear();
            userEvents.addAll((Collection<? extends CreateUserEvent>) results.values);
            if(userEvents.isEmpty())
                Toast.makeText(context, "No Event Found", Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }
    };
}
