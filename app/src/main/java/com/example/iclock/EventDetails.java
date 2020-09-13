package com.example.iclock;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class EventDetails extends Fragment {
    private Context context;
    private CreateUserEvent createUserEvent;
    private TextView event_name;
    private ImageView event_image;
    private TextView event_description;
    private TextView event_start_date;
    private  TextView event_end_date;
    private TextView contact;
    private TextView certification;
    private TextView optional_description;


    public EventDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //So that's the important thing you are doing in this function and the thing is you are receiving the createuserevent object from the recyclerView of
        //event section. when user clicks on any item of recyclerview onclicklistener runs in recyclerviewadapter inside bindfunction, go and check it out
        //and there you have created a bundle object by which you are passing the clicked event object(CreateUserEvent) using bundle.putSerializable()
        //remember to implement serializable in you CreateUserEvent Class only then you can serialize the object of this class and then pass it to bundle

        Bundle bundle = getArguments();
        createUserEvent = (CreateUserEvent) bundle.getSerializable("event_details");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_event_details, container, false);
        context = container.getContext();

        event_name = root.findViewById(R.id.event_details_text_event_name);
        event_description = root.findViewById(R.id.event_details_text_description);
        event_image = root.findViewById(R.id.event_details_image);
        event_start_date = root.findViewById(R.id.event_details_registration_start_date);
        event_end_date = root.findViewById(R.id.event_details_registration_end_date);
        certification = root.findViewById(R.id.event_details_certification);
        contact = root.findViewById(R.id.event_details_contactno);


        Picasso.get().load(createUserEvent.getImageUrl())
                .fit()
                .centerCrop()
                .into(event_image);
        event_name.setText(createUserEvent.getEventName());
        event_description.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        event_description.append(createUserEvent.getDescription());
        event_end_date.append(createUserEvent.getEventEndDate());
        event_start_date.append(createUserEvent.getEventStartdate());
        certification.append(createUserEvent.getIsCertificationAvailable());
        contact.append(createUserEvent.getContact());

        return root;

    }
}