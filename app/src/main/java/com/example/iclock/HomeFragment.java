package com.example.iclock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.content_dashboard, container, false);
        Animation scale_animation = AnimationUtils.loadAnimation(container.getContext(),R.anim.scale_animation);

        ImageButton event_img_button = root.findViewById(R.id.event_button);
        ImageButton share_img_button = root.findViewById(R.id.sharing_button);
        LinearLayout event_layout = root.findViewById(R.id.event_linear_layout);
        LinearLayout share_layout = root.findViewById(R.id.sahring_linear_layout);

        //setting the animation
        event_layout.setAnimation(scale_animation);
        share_layout.setAnimation(scale_animation);

        //Here i have defined all listener Events on the main dashboard page activity.
        event_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(), "You Clicked on Event Button", Toast.LENGTH_SHORT).show();
            }
        });
        share_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(container.getContext(), "You Clicked On Books", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(HomeFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }
}