package com.example.iclock.ui.home;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.iclock.DashboardActivity;
import com.example.iclock.MainActivity;
import com.example.iclock.R;
import com.example.iclock.ui.gallery.GalleryFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeFragment extends Fragment  {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        //inflating the home_fragement layout and storing it in root
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        //loading animation from anim resources
        Animation top_animation = AnimationUtils.loadAnimation(container.getContext(),R.anim.top_animation);
        Animation scale_animation = AnimationUtils.loadAnimation(container.getContext(),R.anim.scale_animation);


        //getting reference to button and all from fragement and since we are taking from fragement and these button are not the part of main_dashboard activity
        //thats why we are using root to get the reference of it ok guys :) iClock App will be Awesome.
        Button event = root.findViewById(R.id.event_button);
        Button sharing = root.findViewById(R.id.sharing_button);
        ImageButton event_image = root.findViewById(R.id.event_img_button);
        ImageButton sharing_image = root.findViewById(R.id.sharing_img_button);

        //Setting the animation on views
        event.setAnimation(top_animation);
        sharing.setAnimation(top_animation);
        event_image.setAnimation(scale_animation);
        sharing_image.setAnimation(scale_animation);

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GalleryFragment g = new GalleryFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment,g)
                        .commit();
            }
        });

        return root;
    }
}