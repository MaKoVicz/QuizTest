package com.example.quiztest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ExamFragment extends Fragment {

    private Button examStartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.exam_layout, container, false);
        examStartButton = (Button) rootView.findViewById(R.id.examStartButton);

        examStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete UserChoice data
                QuizDatabase db = new QuizDatabase(getActivity());
                db.DeleteUserChoiceData();

                Intent examSettingIntent = new Intent(getActivity(), ExamSettingPreferences.class);
                startActivity(examSettingIntent);
            }
        });

        return rootView;
    }
}
