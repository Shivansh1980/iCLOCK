package com.example.iclock;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MoodleFragment extends Fragment {
    private ProgressDialog dialog;
    private WebView moodle;
    private Context context;
    public MoodleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog=new ProgressDialog(context);
        dialog.setTitle("Opening Moodle");
        dialog.setMessage("Please Wait....");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_moodle, container, false);
        moodle = root.findViewById(R.id.moodle_webview);
        moodle.setWebViewClient(new WebViewClient());

        WebSettings webSettings = moodle.getSettings();
        webSettings.setJavaScriptEnabled(true);
        moodle.loadUrl("http://moodle.mitsgwalior.in/");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                dialog.dismiss();
            }
        }, 1500);

        //This is to handle on back button pressed during browsing
        moodle.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && moodle.canGoBack()) {
                    moodle.goBack();
                    return true;
                }
                return false;
            }

        });

        return root;
    }
}