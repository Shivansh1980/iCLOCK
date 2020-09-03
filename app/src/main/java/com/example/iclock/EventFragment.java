package com.example.iclock;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context context;
    private EventRecyclerViewAdapter eventAdapter;
    List<CreateUserEvent> userEvents;
    private DatabaseReference databaseReference;

    public EventFragment() {
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
        context = container.getContext();
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);

        Animation scale_animation = AnimationUtils.loadAnimation(context, R.anim.scale_animation);
        scale_animation.setDuration(1000);
        recyclerView.setAnimation(scale_animation);

        recyclerView.setHasFixedSize(true);

        //now lets set the layout in which way recycler view is going to put view like using grid or linealayout
        //in this case i choosed linear.

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //we are creating this userEvents array for getting the objects from database that we have uploaded during eventCreation
        //we get all objects in the below array
        userEvents = new ArrayList<>();

        //now get the  reference to the dataabse in which you have uploaded all of your objects
        databaseReference = FirebaseDatabase.getInstance().getReference("Events_Details");

        //listen for addValueListener and secondly in onDatachange you will get snapshop which contains all your objects
        //you can fetch them as i did below.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    CreateUserEvent createUserEvent = postSnapshot.getValue(CreateUserEvent.class);
                    userEvents.add(createUserEvent);
                }

                //when we have all userEvents ready with all objects of database we will set them into our adapter for
                //showing on to our activity view
                EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(context,userEvents);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}