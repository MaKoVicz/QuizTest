package com.example.quiztest;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PracticeQuizFragment extends Fragment {

    //region Initiate
    private TextView questionTextViewPractice, answerA, answerB, answerC, answerD;
    private LinearLayout answerALinearLayoutPractice, answerBLinearLayoutPractice,
            answerCLinearLayoutPractice, answerDLinearLayoutPractice, answerOptionContainerLayout;
    private Animation answerTransitionAnimation, questionTextViewTransition;
    private ArrayList<Question> listQuestion;
    private String correctAnswer;
    private int questionCounter = 0, correctChoice = 0, wrongChoice = 0, questionAmount = 45;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.practice_quiz, container, false);

        questionTextViewPractice = (TextView) rootView.findViewById(R.id.questionTextViewPractice);
        answerALinearLayoutPractice = (LinearLayout) rootView.findViewById(R.id.answerALayoutPractice);
        answerBLinearLayoutPractice = (LinearLayout) rootView.findViewById(R.id.answerBLayoutPractice);
        answerCLinearLayoutPractice = (LinearLayout) rootView.findViewById(R.id.answerCLayoutPractice);
        answerDLinearLayoutPractice = (LinearLayout) rootView.findViewById(R.id.answerDLayoutPractice);
        answerOptionContainerLayout = (LinearLayout) rootView.findViewById(R.id.answerOptionContainerPractice);
        answerTransitionAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.answer_choice_transition);
        questionTextViewTransition = AnimationUtils.loadAnimation(getActivity(), R.anim.question_textview_transition);

        ModifyActionBar();
        IniAnswerClickEvent();
        GetQuestionData();
        SetAnswerText(questionCounter);
        PlayBackgroundMusic();

        return rootView;
    }

    public void ModifyActionBar() {
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable
                (new ColorDrawable(getResources().getColor(R.color.colorPractice)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.quiz_actionbar);

        RelativeLayout actionBarLayout = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.actionBarLayout);
        actionBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPractice));

        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.titleTextView);
        title.setText(getString(R.string.practiceTextView));

        Button backButton = (Button) actionBar.getCustomView().findViewById(R.id.backButton);
        backButton.setBackgroundColor(getResources().getColor(R.color.colorPracticeTransparent));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Message")
                        .setMessage("Do you want to stop practicing?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                actionBar.hide();
                                StopBackgroundMusic();
                                BackToMainActivity();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .remove(PracticeQuizFragment.this).commit();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(false).setIcon(R.drawable.ic_vector_message).show();
            }
        });
    }

    public void BackToMainActivity() {
        getFragmentManager().popBackStack();
    }

    public void IniAnswerClickEvent() {
        answerA = (TextView) answerALinearLayoutPractice.getChildAt(1);
        answerB = (TextView) answerBLinearLayoutPractice.getChildAt(1);
        answerC = (TextView) answerCLinearLayoutPractice.getChildAt(1);
        answerD = (TextView) answerDLinearLayoutPractice.getChildAt(1);

        answerALinearLayoutPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerA.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "A");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "A");
                }
            }
        });

        answerBLinearLayoutPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerB.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "B");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "B");
                }
            }
        });

        answerCLinearLayoutPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerC.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "C");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "C");
                }
            }
        });

        answerDLinearLayoutPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerD.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "D");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "D");
                }
            }
        });
    }

    public void AnimationBackgroundColorChange(int colorFromID, int colorToID, String answerChoice) {
        final String answer = answerChoice;

        int colorFrom = getResources().getColor(colorFromID);
        int colorTo = getResources().getColor(colorToID);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(1000); // milliseconds

        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (answer.equals("A")) {
                    answerA.setBackgroundColor((int) animation.getAnimatedValue());
                } else if (answer.equals("B")) {
                    answerB.setBackgroundColor((int) animation.getAnimatedValue());
                } else if (answer.equals("C")) {
                    answerC.setBackgroundColor((int) animation.getAnimatedValue());
                } else {
                    answerD.setBackgroundColor((int) animation.getAnimatedValue());
                }
            }
        });

        colorAnimation.start();
    }

    public void AnswerClick(final int colorFromID, final int colorToID, final String answerChoice) {
        if (questionCounter < listQuestion.size() - 1) { //Still contains questions
            SetTextViewEnabledFalse();
            AnimationBackgroundColorChange(colorFromID, colorToID, answerChoice);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    questionTextViewPractice.startAnimation(questionTextViewTransition);
                    answerOptionContainerLayout.startAnimation(answerTransitionAnimation);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnimationBackgroundColorChange(colorToID, colorFromID, answerChoice);
                            SetTextViewEnabledTrue();
                            questionCounter++;
                            SetAnswerText(questionCounter);
                        }
                    }, 850);
                }
            }, 1200);
        } else { //Don't have any questions anymore
            SetTextViewEnabledFalse();
            AnimationBackgroundColorChange(colorFromID, colorToID, answerChoice);
            FinishQuiz();
        }
    }

    public void GetQuestionData() {
        QuizDatabase db = new QuizDatabase(getActivity());
        listQuestion = new ArrayList<>();
        listQuestion = db.GetQuestionData("");

        ArrayList<Question> listQuestionTemp = new ArrayList<>(listQuestion.size()); //Store the entire question data
        SecureRandom QuestionRandom = new SecureRandom();

        for (Question question : listQuestion) { //copy listQuestion element to listQuestionTemp
            listQuestionTemp.add(question);
        }

        listQuestion.clear(); //clear data to store the exact number of questions that users choose.

        while (listQuestion.size() < questionAmount) {
            int size = listQuestionTemp.size();
            int randomElementIndex = QuestionRandom.nextInt(size);
            Question question = listQuestionTemp.get(randomElementIndex);

            if (!CheckIdenticalQuestion(question)) {
                listQuestion.add(question);
                listQuestionTemp.remove(randomElementIndex);
            }
        }
    }

    public boolean CheckIdenticalQuestion(Question question) {
        if (listQuestion.contains(question)) {
            return true;
        }

        return false;
    }

    public void SetTextViewEnabledTrue() {
        answerALinearLayoutPractice.setEnabled(true);
        answerBLinearLayoutPractice.setEnabled(true);
        answerCLinearLayoutPractice.setEnabled(true);
        answerDLinearLayoutPractice.setEnabled(true);
    }

    public void SetTextViewEnabledFalse() {
        answerALinearLayoutPractice.setEnabled(false);
        answerBLinearLayoutPractice.setEnabled(false);
        answerCLinearLayoutPractice.setEnabled(false);
        answerDLinearLayoutPractice.setEnabled(false);
    }

    public void SetAnswerText(int i) {
        questionTextViewPractice.setText(Html.fromHtml(listQuestion.get(i).getQuestion()));
        correctAnswer = listQuestion.get(i).getCorrectAns();

        List<String> answers = new ArrayList<>();
        answers.add(listQuestion.get(i).getAns1());
        answers.add(listQuestion.get(i).getAns2());
        answers.add(listQuestion.get(i).getAns3());
        answers.add(listQuestion.get(i).getAns4());

        Collections.shuffle(answers);

        answerA.setText(Html.fromHtml(answers.get(0)));
        answerB.setText(Html.fromHtml(answers.get(1)));
        answerC.setText(Html.fromHtml(answers.get(2)));
        answerD.setText(Html.fromHtml(answers.get(3)));

        listQuestion.get(i).setAns1(answers.get(0));
        listQuestion.get(i).setAns2(answers.get(1));
        listQuestion.get(i).setAns3(answers.get(2));
        listQuestion.get(i).setAns4(answers.get(3));
    }

    public void FinishQuiz() {
        StopBackgroundMusic();
        float correctPercent = ((float) correctChoice / (float) questionAmount) * 100;
        float wrongPercent = ((float) wrongChoice / (float) questionAmount) * 100;

        final String correctDisplay = Math.round(correctPercent) + " %";
        final String incorrectDisplay = Math.round(wrongPercent) + " %";

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(getContext()).
                        setMessage("Correct: " + correctDisplay + "\n" + "Incorrect: " + incorrectDisplay)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActionBar actionBar = getActivity().getActionBar();
                                actionBar.hide();
                                BackToMainActivity();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .remove(PracticeQuizFragment.this).commit();
                            }
                        }).setCancelable(false).setIcon(R.drawable.ic_vector_message).show();
            }
        }, 1100);
    }

    public void PlayBackgroundMusic() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity.getBackgroundMusicSwitch().isChecked()) {
            mainActivity.PlayBackgroundMusic();
        }
    }

    public void StopBackgroundMusic() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.StopBackgroundMusic();
    }

    public void PlaySoundEffect() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity.getSoundEffectSwitch().isChecked()) {
            mainActivity.PlaySoundEffect();
        }
    }

    public void VibrateWrongChoice() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
}