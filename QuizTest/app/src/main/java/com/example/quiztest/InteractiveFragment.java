package com.example.quiztest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InteractiveFragment extends Fragment {

    private Button interactiveStartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.interactive_layout, container, false);
        interactiveStartButton = (Button) rootView.findViewById(R.id.interactiveStartButton);

        interactiveStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).
                        setTitle(Html.fromHtml("<font color=Black>Message</font>")).
                        setMessage(Html.fromHtml("<font color=Black>Deploying</font>")).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(false).setIcon(R.drawable.ic_vector_message).show();
            }
        });

        return rootView;
    }
}
