package com.example.memes;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainTab extends Fragment{

    private TextView textViewRealAngle;
    private ImageButton infoImgBtn;
    private ImageButton settingsImgBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        textViewRealAngle = rootView.findViewById(R.id.textView_realAngle);

        infoImgBtn = rootView.findViewById(R.id.imageButton_info);
        infoImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InformationActivity.class);
                startActivity(intent);
            }
        });

        settingsImgBtn = rootView.findViewById(R.id.imageButton_settings);
        settingsImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
