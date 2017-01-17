package com.example.quiztest;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExamSettingPreferences extends PreferenceActivity {

    private Button beginButton;
    private Intent mainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        addPreferencesFromResource(R.xml.exam_setting_preferences);
        mainActivityIntent = new Intent(ExamSettingPreferences.this, MainActivity.class);
        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        beginButton = (Button) findViewById(R.id.beginButton);

        HideStatusBar();
        ModifyActionBar();
        BeginButtonOnClick();
    }

    @Override
    public void onBackPressed() {
    }

    public void HideStatusBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void ModifyActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.quiz_actionbar);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorExam)));

        RelativeLayout actionBarLayout = (RelativeLayout) actionBar.getCustomView().findViewById(R.id.actionBarLayout);
        actionBarLayout.setBackgroundColor(getResources().getColor(R.color.colorExam));

        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.titleTextView);
        title.setText(getString(R.string.examTextView));

        Button backButton = (Button) actionBar.getCustomView().findViewById(R.id.backButton);
        backButton.setBackgroundColor(getResources().getColor(R.color.colorExamTransparent));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityIntent.putExtra("isGoToExamLayout", true);
                startActivity(mainActivityIntent);
            }
        });
    }

    public void BeginButtonOnClick() {
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityIntent.putExtra("isGoToExamQuiz", true);
                startActivity(mainActivityIntent);
            }
        });
    }
}
