package com.example.quiztest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreviewFragment extends Fragment {

    private TextView questionTextViewPreview, answerA, answerB, answerC, answerD;
    private LinearLayout answerALinearLayout, answerBLinearLayout, answerCLinearLayout, answerDLinearLayout;
    private String question, ans1, ans2, ans3, ans4, userChoice, result;

    public PreviewFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preview, container, false);
        questionTextViewPreview = (TextView) view.findViewById(R.id.questionTextViewPreview);
        answerALinearLayout = (LinearLayout) view.findViewById(R.id.answerALayoutPreview);
        answerBLinearLayout = (LinearLayout) view.findViewById(R.id.answerBLayoutPreview);
        answerCLinearLayout = (LinearLayout) view.findViewById(R.id.answerCLayoutPreview);
        answerDLinearLayout = (LinearLayout) view.findViewById(R.id.answerDLayoutPreview);

        InitAnswerTextView();
        SetTextViewContent();
        return view;
    }

    public void InitAnswerTextView() {
        answerA = (TextView) answerALinearLayout.getChildAt(1);
        answerB = (TextView) answerBLinearLayout.getChildAt(1);
        answerC = (TextView) answerCLinearLayout.getChildAt(1);
        answerD = (TextView) answerDLinearLayout.getChildAt(1);
    }

    public void TransferContent(String question, String ans1, String ans2, String ans3, String ans4,
                                String userChoice, String result) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.ans3 = ans3;
        this.ans4 = ans4;
        this.userChoice = userChoice;
        this.result = result;
    }

    public void SetTextViewContent() {
        questionTextViewPreview.setText(Html.fromHtml(question));
        answerA.setText(Html.fromHtml(ans1));
        answerB.setText(Html.fromHtml(ans2));
        answerC.setText(Html.fromHtml(ans3));
        answerD.setText(Html.fromHtml(ans4));

        if (userChoice.equals("A")) {
            if (result.equals("Correct")) {
                answerA.setBackgroundColor(getResources().getColor(R.color.colorInteractive));
            } else {
                answerA.setBackgroundColor(getResources().getColor(R.color.colorExam));
            }
        } else if (userChoice.equals("B")) {
            if (result.equals("Correct")) {
                answerB.setBackgroundColor(getResources().getColor(R.color.colorInteractive));
            } else {
                answerB.setBackgroundColor(getResources().getColor(R.color.colorExam));
            }
        } else if (userChoice.equals("C")) {
            if (result.equals("Correct")) {
                answerC.setBackgroundColor(getResources().getColor(R.color.colorInteractive));
            } else {
                answerC.setBackgroundColor(getResources().getColor(R.color.colorExam));
            }
        } else {
            if (result.equals("Correct")) {
                answerD.setBackgroundColor(getResources().getColor(R.color.colorInteractive));
            } else {
                answerD.setBackgroundColor(getResources().getColor(R.color.colorExam));
            }
        }
    }
}
