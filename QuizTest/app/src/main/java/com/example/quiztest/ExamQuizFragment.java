package com.example.quiztest;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
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
import java.util.concurrent.TimeUnit;

public class ExamQuizFragment extends Fragment {

    //region Initialize
    private LinearLayout answerALinearLayoutExam, answerBLinearLayoutExam,
            answerCLinearLayoutExam, answerDLinearLayoutExam, answerOptionContainerLayout;
    private TextView answerA, answerB, answerC, answerD, questionTextViewExam, timerTextView, title;
    private Animation answerTransitionAnimation, questionTextViewTransition;
    private CountDownTimer countDownTimer;
    private String difficulty, correctAnswer;
    private int timer, questionAmount, questionCounter = 0, correctChoice = 0, wrongChoice = 0;
    private ArrayList<Question> listQuestion;
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exam_quiz, container, false);
        timerTextView = (TextView) view.findViewById(R.id.timerTextView);
        questionTextViewExam = (TextView) view.findViewById(R.id.questionTextViewExam);
        answerALinearLayoutExam = (LinearLayout) view.findViewById(R.id.answerALayoutExam);
        answerCLinearLayoutExam = (LinearLayout) view.findViewById(R.id.answerCLayoutExam);
        answerBLinearLayoutExam = (LinearLayout) view.findViewById(R.id.answerBLayoutExam);
        answerDLinearLayoutExam = (LinearLayout) view.findViewById(R.id.answerDLayoutExam);
        answerOptionContainerLayout = (LinearLayout) view.findViewById(R.id.answerOptionContainerExam);
        answerTransitionAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.answer_choice_transition);
        questionTextViewTransition = AnimationUtils.loadAnimation(getActivity(), R.anim.question_textview_transition);

        ModifyActionBar();
        GetPreferenceData();
        InitTimer();
        InitAnswerClickEvent();
        GetQuestionData();
        SetAnswerText(questionCounter);
        PlayBackgroundMusic();
        return view;
    }

    public void ModifyActionBar() {
        final ActionBar actionBar = getActivity().getActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable
                (new ColorDrawable(getResources().getColor(R.color.colorExam)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.quiz_actionbar);

        RelativeLayout actionBarLayout = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.actionBarLayout);
        actionBarLayout.setBackgroundColor(getResources().getColor(R.color.colorExam));

        title = (TextView) actionBar.getCustomView().findViewById(R.id.titleTextView);
        title.setText("Question 1");

        Button backButton = (Button) actionBar.getCustomView().findViewById(R.id.backButton);
        backButton.setBackgroundColor(getResources().getColor(R.color.colorExamTransparent));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext()).setTitle("Message")
                        .setMessage("Do you want to stop doing the exam?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                StopBackgroundMusic();
                                actionBar.hide();
                                countDownTimer.cancel();
                                BackToMainActivity();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .remove(ExamQuizFragment.this).commit();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    public void InitTimer() {
        countDownTimer = new CountDownTimer(timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String timeFormat = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                timerTextView.setText(timeFormat);
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Done");
                FinishQuiz();
            }
        };

        countDownTimer.start();
    }

    public void InitAnswerClickEvent() {
        answerA = (TextView) answerALinearLayoutExam.getChildAt(1);
        answerB = (TextView) answerBLinearLayoutExam.getChildAt(1);
        answerC = (TextView) answerCLinearLayoutExam.getChildAt(1);
        answerD = (TextView) answerDLinearLayoutExam.getChildAt(1);

        answerALinearLayoutExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerA.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "A", "Correct");
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "A");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "A", "Incorrect");
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "A");
                }
            }
        });

        answerBLinearLayoutExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerB.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "B", "Correct");
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "B");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "B", "Incorrect");
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "B");
                }
            }
        });

        answerCLinearLayoutExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerC.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "C", "Correct");
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "C");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "C", "Incorrect");
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "C");
                }
            }
        });

        answerDLinearLayoutExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerD.getText().toString().equals(Html.fromHtml(correctAnswer).toString())) {
                    PlaySoundEffect();
                    correctChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "D", "Correct");
                    AnswerClick(R.color.colorTextView, R.color.colorInteractive, "D");
                } else {
                    VibrateWrongChoice();
                    wrongChoice++;
                    AddUserChoiceData(listQuestion.get(questionCounter), "D", "Incorrect");
                    AnswerClick(R.color.colorTextView, R.color.colorExam, "D");
                }
            }
        });
    }

    public void GetPreferenceData() {
        PreferenceManager.setDefaultValues(getActivity(), R.xml.exam_setting_preferences, false);
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //User time choice
        String userTimeChoice = myPreference.getString("pref_examTime_list", null);
        timer = Integer.parseInt(userTimeChoice) * 60 * 1000;

        //User difficulty choice
        difficulty = myPreference.getString("pref_difficulty_list", null);

        //User question amount choice
        String userQuestionAmountChoice = myPreference.getString("pref_questionAmount_list", null);
        questionAmount = Integer.parseInt(userQuestionAmountChoice);
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
                    questionTextViewExam.startAnimation(questionTextViewTransition);
                    answerOptionContainerLayout.startAnimation(answerTransitionAnimation);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnimationBackgroundColorChange(colorToID, colorFromID, answerChoice);
                            SetTextViewEnabledTrue();
                            questionCounter++;
                            SetAnswerText(questionCounter);
                            SetTitleActionBar(questionCounter + 1);
                        }
                    }, 850);
                }
            }, 1200);
        } else { //Don't have any questions anymore
            SetTextViewEnabledFalse();
            countDownTimer.cancel();
            AnimationBackgroundColorChange(colorFromID, colorToID, answerChoice);
            FinishQuiz();
        }
    }

    public void GetQuestionData() {
        QuizDatabase db = new QuizDatabase(getActivity());
        listQuestion = new ArrayList<>();
        listQuestion = db.GetQuestionData(difficulty);

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

    public void SetTitleActionBar(int counter) {
        String titleFormat = "Question " + counter;
        title.setText(titleFormat);
    }

    public void SetTextViewEnabledTrue() {
        answerALinearLayoutExam.setEnabled(true);
        answerBLinearLayoutExam.setEnabled(true);
        answerCLinearLayoutExam.setEnabled(true);
        answerDLinearLayoutExam.setEnabled(true);
    }

    public void SetTextViewEnabledFalse() {
        answerALinearLayoutExam.setEnabled(false);
        answerBLinearLayoutExam.setEnabled(false);
        answerCLinearLayoutExam.setEnabled(false);
        answerDLinearLayoutExam.setEnabled(false);
    }

    public void SetAnswerText(int i) {
        questionTextViewExam.setText(Html.fromHtml(listQuestion.get(i).getQuestion()));
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

    public boolean CheckIdenticalQuestion(Question question) {
        if (listQuestion.contains(question)) {
            return true;
        }

        return false;
    }

    public void FinishQuiz() {
        StopBackgroundMusic();
        float correctPercent = ((float) correctChoice / (float) questionAmount) * 100;
        float wrongPercent = ((float) wrongChoice / (float) questionAmount) * 100;
        int unanswered = questionAmount - correctChoice - wrongChoice;
        float unansweredPercent = ((float) unanswered / (float) questionAmount) * 100;

        final String correctDisplay = Math.round(correctPercent) + "";
        final String incorrectDisplay = Math.round(wrongPercent) + "";
        final String unansweredDisplay = Math.round(unansweredPercent) + "";

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent previewStatisticIntent = new Intent(getActivity(), PreviewStatistic.class);

                previewStatisticIntent.putExtra("correctPercent", correctDisplay);
                previewStatisticIntent.putExtra("incorrectPercent", incorrectDisplay);
                previewStatisticIntent.putExtra("unansweredPercent", unansweredDisplay);

                startActivity(previewStatisticIntent);
            }
        }, 1100);
    }

    public void AddUserChoiceData(Question currentQuestion, String userChoice, String result) {
        String id = currentQuestion.getId();
        String question = currentQuestion.getQuestion();
        String ans1 = currentQuestion.getAns1();
        String ans2 = currentQuestion.getAns2();
        String ans3 = currentQuestion.getAns3();
        String ans4 = currentQuestion.getAns4();

        QuizDatabase myDb = new QuizDatabase(getActivity());
        myDb.AddUserChoiceData(id, question, ans1, ans2, ans3, ans4, userChoice, result);
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