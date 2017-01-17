package com.example.quiztest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class PracticeFragment extends Fragment {

    private Button practiceStartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.practice_layout, container, false);
        practiceStartButton = (Button) rootView.findViewById(R.id.practiceStartButton);

        practiceStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager practiceFragment = getFragmentManager();

                PracticeQuizFragment practiceQuizFragment = new PracticeQuizFragment();
                practiceFragment.beginTransaction().
                        replace(R.id.main_content, practiceQuizFragment, "practiceQuizFragment")
                        .addToBackStack("practiceQuizFragment").commit();
            }
        });

        return rootView;
    }
}
