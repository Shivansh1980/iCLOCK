package com.example.iclock;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventFragment extends Fragment {
    private NavController navController;
    private RecyclerView recyclerView;
    private Context context;
    private static final String TAG = "Checking_Dates";
    private EventRecyclerViewAdapter eventAdapter;
    List<CreateUserEvent> userEvents;
    private DatabaseReference databaseReference;
    String currentDate;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyy");
        Date date = new Date();
        currentDate = ss.format(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

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
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    final CreateUserEvent createUserEvent = postSnapshot.getValue(CreateUserEvent.class);

                    if (isPassedDate(createUserEvent.getEventEndDate())) {

                        String image_url = createUserEvent.getImageUrl();
                        Query q = databaseReference.orderByChild("imageUrl").equalTo(image_url);

                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot fakeSnapshots : snapshot.getChildren()) {
                                    fakeSnapshots.getRef().removeValue();
                                    StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(createUserEvent.getImageUrl());
                                    Log.d(TAG, "onDataChange: "+imageRef);
                                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "onSuccess: Removed Event Image From Storage");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "onFailure: Failed to delete image from database");
                                        }
                                    });
                                    Toast.makeText(context, createUserEvent.getEventName()+" Event Date Expired", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        continue;
                    }
                    userEvents.add(createUserEvent);
                }

                //when we have all userEvents ready with all objects of database we will set them into our adapter for
                //showing on to our activity view
                EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(context, userEvents, navController);
                recyclerView.setAdapter(adapter);

            }

            private boolean isPassedDate(String eventEndDate) {
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date d1 = simpleDateFormat.parse(currentDate);
                    Date d2 = simpleDateFormat.parse(eventEndDate);
                    Log.d(TAG, "isPassedDate: current date : " + d1.getDate() + "/" + d1.getMonth() + "/" + d1.getYear() + " And event end date : " + d2.getDate() + "/" + d2.getMonth() + "/" + d2.getYear());
                    if (d1.compareTo(d2) < 0) {
                        Log.d(TAG, "isPassedDate: current date < end date so returning false i.e. not removing");
                        return false;
                    } else {
                        Log.d(TAG, "isPassedDate: current date > end date so returning true i.e. removing from event list");
                        return true;
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid Date Formate", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}