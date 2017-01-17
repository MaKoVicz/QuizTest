package com.example.quiztest;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PreviewMain extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;
    private ArrayList<UserChoice> userChoiceList;
    private ActionBar actionBar;
    private TextView title;
    private int questionAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_viewpager);
        HideStatusBar();
        viewPager = (ViewPager) findViewById(R.id.previewViewpager);

        GetUserChoiceData();
        ModifyActionBar();
        InitViewpager();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        title.setText("PREVIEW (" + (position + 1) + "/" + questionAmount + ")");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void ModifyActionBar() {
        actionBar = getActionBar();
        actionBar.show();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPreview)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.quiz_actionbar);

        RelativeLayout actionBarLayout = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.actionBarLayout);
        actionBarLayout.setBackgroundColor(getResources().getColor(R.color.colorPreview));

        title = (TextView) actionBar.getCustomView().findViewById(R.id.titleTextView);
        title.setText("PREVIEW (" + 1 + "/" + questionAmount + ")");

        Button backButton = (Button) actionBar.getCustomView().findViewById(R.id.backButton);
        backButton.setBackgroundColor(getResources().getColor(R.color.colorPreviewTransparent));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToPreviewStatistic();
            }
        });
    }

    public void InitViewpager() {
        int questionAmount = GetViewpagerFragmentAmount();
        List<Fragment> previewFragmentList = new ArrayList<>();

        for (int i = 0; i < questionAmount; i++) {
            String question = userChoiceList.get(i).getQuestion();
            String ans1 = userChoiceList.get(i).getAns1();
            String ans2 = userChoiceList.get(i).getAns2();
            String ans3 = userChoiceList.get(i).getAns3();
            String ans4 = userChoiceList.get(i).getAns4();
            String userChoice = userChoiceList.get(i).getUserChoice();
            String result = userChoiceList.get(i).getResult();

            PreviewFragment previewFragment = new PreviewFragment();
            previewFragment.TransferContent(question, ans1, ans2, ans3, ans4, userChoice, result);
            previewFragmentList.add(previewFragment);
        }

        viewPager.setAdapter(new PreviewPagerAdapter(this.getSupportFragmentManager(), previewFragmentList));
        viewPager.addOnPageChangeListener(this);
    }

    public int GetViewpagerFragmentAmount() {
        PreferenceManager.setDefaultValues(this, R.xml.exam_setting_preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        int questionAmount = Integer.parseInt(preferences.getString("pref_questionAmount_list", null));
        return questionAmount;
    }

    public void GetUserChoiceData() {
        QuizDatabase myDb = new QuizDatabase(this);
        userChoiceList = new ArrayList<>();
        userChoiceList = myDb.GetUserChoiceData();
        questionAmount = userChoiceList.size();
    }

    public void GoToPreviewStatistic() {
        Intent previewStatisticIntent = new Intent(PreviewMain.this, PreviewStatistic.class);
        previewStatisticIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(previewStatisticIntent);
    }

    public void HideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
