package com.example.quiztest;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PreviewStatistic extends FragmentActivity {
    private PieChart pieChart;
    private Button buttonPreview;
    private float[] yData;
    private int correctPercent = 0, incorrectPercent = 0, unansweredPercent = 0;
    private ArrayList<RecentRecord> recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_statistic);
        HideStatusBar();

        //region getExtras
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            correctPercent = Integer.parseInt(bundle.getString("correctPercent"));
            incorrectPercent = Integer.parseInt(bundle.getString("incorrectPercent"));
            unansweredPercent = Integer.parseInt(bundle.getString("unansweredPercent"));

            if (correctPercent + incorrectPercent + unansweredPercent < 100) {
                unansweredPercent = 100 - correctPercent - incorrectPercent;
            }
        }

        if (unansweredPercent != 0) {
            yData = new float[3];
            yData[0] = correctPercent;
            yData[1] = incorrectPercent;
            yData[2] = unansweredPercent;
        } else {
            yData = new float[2];
            yData[0] = correctPercent;
            yData[1] = incorrectPercent;
        }
        //endregion

        pieChart = (PieChart) findViewById(R.id.pieChart);
        buttonPreview = (Button) findViewById(R.id.previewButton);

        ModifyActionbar();
        ConfigurePieChartToLayout();
        GetRecentRecordData();
        SetRecentRecordTextView();
        ButtonPreviewClicked();
    }

    @Override
    public void onBackPressed() {
    }

    public void ModifyActionbar() {
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable
                (new ColorDrawable(getResources().getColor(R.color.colorPreview)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.quiz_actionbar);

        RelativeLayout actionBarLayout = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.actionBarLayout);
        actionBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPreview));

        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.titleTextView);
        title.setText("PREVIEW");

        Button backButton = (Button) actionBar.getCustomView().findViewById(R.id.backButton);
        backButton.setBackgroundColor(getResources().getColor(R.color.colorPreviewTransparent));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRecentRecordData();
                DeleteRecordData();
                Intent mainActivityIntent = new Intent(PreviewStatistic.this, MainActivity.class);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivityIntent);
            }
        });
    }

    public void ConfigurePieChartToLayout() {
        //configure pie chart
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("");

        //enable rotation of the chart
        pieChart.setRotationAngle(0f);
        pieChart.setRotationEnabled(true);

        //set Hole
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(7f);
        pieChart.setTransparentCircleRadius(10f);


        //Add data to pie chart
        AddChartData();
    }

    public void AddChartData() {
        ArrayList<PieEntry> yValue = new ArrayList<PieEntry>();

        for (int i = 0; i < yData.length; i++) {
            yValue.add(new PieEntry(yData[i], i));
        }

        //Pie chart dataset
        PieDataSet dataSet = new PieDataSet(yValue, "Quiz Statistic");

        //Add color;
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.GRAY);

        dataSet.setColors(colors);

        //Instantiate pie chart object
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.WHITE);
        pieChart.setData(data);

        //undo all highlight
        pieChart.highlightValue(null);

        //update pie chart
        pieChart.invalidate();
    }

    public void GoToPreview() {
        Intent previewMainIntent = new Intent(PreviewStatistic.this, PreviewMain.class);
        startActivity(previewMainIntent);
    }

    public void ButtonPreviewClicked() {
        buttonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToPreview();
            }
        });
    }

    public void HideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void AddRecentRecordData() {
        PreferenceManager.setDefaultValues(this, R.xml.exam_setting_preferences, false);
        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(this);

        String difficulty = myPreference.getString("pref_difficulty_list", null);
        String questionNumber = myPreference.getString("pref_questionAmount_list", null);
        String timeNumber = myPreference.getString("pref_examTime_list", null);
        String correctPercentString = correctPercent + "%";

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        Date date = new Date();
        String examDate = dateFormat.format(date);

        QuizDatabase db = new QuizDatabase(this);
        if (recordList.size() == 0) {
            db.AddRecentRecordData(recordList.size() + 1, difficulty, examDate, correctPercentString, questionNumber, timeNumber);
        } else {
            int recordListLastElement = recordList.size() - 1; //Last recordList element
            int nextID = recordList.get(recordListLastElement).getId() + 1; //Get the id of the last element and increase it by 1.
            db.AddRecentRecordData(nextID, difficulty, examDate, correctPercentString, questionNumber, timeNumber);
        }
    }

    public void GetRecentRecordData() {
        recordList = new ArrayList<>();
        QuizDatabase db = new QuizDatabase(this);

        recordList = db.GetRecentRecordData();
    }

    public void DeleteRecordData() {
        if (recordList.size() > 3) {
            int recordListLastElement = recordList.size() - 1; //Last recordList element
            int lastElementID = recordList.get(recordListLastElement).getId(); //get id of the last element

            QuizDatabase db = new QuizDatabase(this);
            db.DeleteRecentRecordData(lastElementID - 2);
        }
    }

    public void SetRecentRecordTextView() {
        if (recordList.size() == 0) {
            return;
        } else if (recordList.size() == 1) {
            SetRecentRecord1(0);
        } else if (recordList.size() == 2) {
            SetRecentRecord1(0);
            SetRecentRecord2(1);
        } else if (recordList.size() == 3) {
            SetRecentRecord1(0);
            SetRecentRecord2(1);
            SetRecentRecord3(2);
        } else if (recordList.size() > 3) {
            int i = recordList.size();
            SetRecentRecord1(i - 3);
            SetRecentRecord2(i - 2);
            SetRecentRecord3(i - 1);
        }
    }

    public void SetRecentRecord1(int i) {
        TextView difficultyRecordTextView1, datetimeRecordTextView1,
                correctRecordTextView1, questionAndTimeRecordTextView1;
        LinearLayout recordContainer1;

        difficultyRecordTextView1 = (TextView) findViewById(R.id.difficultyRecordTextView1);
        datetimeRecordTextView1 = (TextView) findViewById(R.id.datetimeRecordTextView1);
        correctRecordTextView1 = (TextView) findViewById(R.id.correctRecordTextView1);
        questionAndTimeRecordTextView1 = (TextView) findViewById(R.id.questionAndTimeRecordTextView1);
        recordContainer1 = (LinearLayout) findViewById(R.id.recordContainer1);

        String formatQuestionAndTimeText = recordList.get(i).getQuestionNumber() + " Questions" + " / "
                + recordList.get(i).getTimeNumber() + " Minutes";

        recordContainer1.setVisibility(View.VISIBLE);
        difficultyRecordTextView1.setText(recordList.get(i).getDifficulty());
        datetimeRecordTextView1.setText(recordList.get(i).getDate());
        correctRecordTextView1.setText(recordList.get(i).getCorrectPercent());
        questionAndTimeRecordTextView1.setText(formatQuestionAndTimeText);
    }

    public void SetRecentRecord2(int i) {
        TextView difficultyRecordTextView2, datetimeRecordTextView2,
                correctRecordTextView2, questionAndTimeRecordTextView2;
        LinearLayout recordContainer2;

        difficultyRecordTextView2 = (TextView) findViewById(R.id.difficultyRecordTextView2);
        datetimeRecordTextView2 = (TextView) findViewById(R.id.datetimeRecordTextView2);
        correctRecordTextView2 = (TextView) findViewById(R.id.correctRecordTextView2);
        questionAndTimeRecordTextView2 = (TextView) findViewById(R.id.questionAndTimeRecordTextView2);
        recordContainer2 = (LinearLayout) findViewById(R.id.recordContainer2);

        String formatQuestionAndTimeText = recordList.get(i).getQuestionNumber() + " Questions" + " / "
                + recordList.get(i).getTimeNumber() + " Minutes";

        recordContainer2.setVisibility(View.VISIBLE);
        difficultyRecordTextView2.setText(recordList.get(i).getDifficulty());
        datetimeRecordTextView2.setText(recordList.get(i).getDate());
        correctRecordTextView2.setText(recordList.get(i).getCorrectPercent());
        questionAndTimeRecordTextView2.setText(formatQuestionAndTimeText);
    }

    public void SetRecentRecord3(int i) {
        TextView difficultyRecordTextView3, datetimeRecordTextView3,
                correctRecordTextView3, questionAndTimeRecordTextView3;
        LinearLayout recordContainer3;

        difficultyRecordTextView3 = (TextView) findViewById(R.id.difficultyRecordTextView3);
        datetimeRecordTextView3 = (TextView) findViewById(R.id.datetimeRecordTextView3);
        correctRecordTextView3 = (TextView) findViewById(R.id.correctRecordTextView3);
        questionAndTimeRecordTextView3 = (TextView) findViewById(R.id.questionAndTimeRecordTextView3);
        recordContainer3 = (LinearLayout) findViewById(R.id.recordContainer3);

        String formatQuestionAndTimeText = recordList.get(i).getQuestionNumber() + " Questions" + " / "
                + recordList.get(i).getTimeNumber() + " Minutes";

        recordContainer3.setVisibility(View.VISIBLE);
        difficultyRecordTextView3.setText(recordList.get(i).getDifficulty());
        datetimeRecordTextView3.setText(recordList.get(i).getDate());
        correctRecordTextView3.setText(recordList.get(i).getCorrectPercent());
        questionAndTimeRecordTextView3.setText(formatQuestionAndTimeText);
    }
}
