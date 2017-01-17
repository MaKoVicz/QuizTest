package com.example.quiztest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    //region Initialize
    private QuizDatabase myDB;
    private ViewPager viewPager;
    private PagerSlidingTabStrip tabs;
    private Switch backgroundMusicSwitch;
    private Switch soundEffectSwitch;
    private MediaPlayer backgroundMusic;
    private MediaPlayer soundEffect;
    //endregion

    //region get, set
    public Switch getBackgroundMusicSwitch() {
        return backgroundMusicSwitch;
    }

    public void setBackgroundMusicSwitch(Switch backgroundMusicSwitch) {
        this.backgroundMusicSwitch = backgroundMusicSwitch;
    }

    public MediaPlayer getBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(MediaPlayer backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    public Switch getSoundEffectSwitch() {
        return soundEffectSwitch;
    }

    public void setSoundEffectSwitch(Switch soundEffectSwitch) {
        this.soundEffectSwitch = soundEffectSwitch;
    }

    public MediaPlayer getSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(MediaPlayer soundEffect) {
        this.soundEffect = soundEffect;
    }
    //endregion

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideBar();

        //region Go to exam
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getBoolean("isGoToExamQuiz")) {
                bundle.clear();
                goToExamQuiz();
            }
        }
        //endregion

        myDB = new QuizDatabase(this);
        initViewPager();
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        InitSwitch();
        addQuestionData();
        ExitClick();
        AboutClick();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        StopBackgroundMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkFragmentVisible() && backgroundMusicSwitch.isChecked()) {
            PlayBackgroundMusic();
        }
    }

    @Override
    public void onBackPressed() {
    }

    //region Personal Method
    public void hideBar() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPractice)));
        actionBar.hide();
    }

    public void InitSwitch() {
        setBackgroundMusicSwitch((Switch) findViewById(R.id.backgroundMusicSwitch));
        setSoundEffectSwitch((Switch) findViewById(R.id.soundEffectSwitch));

        getBackgroundMusicSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (getBackgroundMusicSwitch().isChecked()) {
                    if (checkFragmentVisible()) {
                        PlayBackgroundMusic();
                    }
                } else {
                    StopBackgroundMusic();
                }
            }
        });
    }

    public void PlaySoundEffect() {
        soundEffect = MediaPlayer.create(this, R.raw.correcteff);
        soundEffect.start();
        soundEffect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundEffect.release();
                soundEffect = null;
            }
        });
    }

    public void PlayBackgroundMusic() {
        setBackgroundMusic(MediaPlayer.create(this, R.raw.practicebg));
        getBackgroundMusic().setLooping(true);
        getBackgroundMusic().start();
    }

    public void StopBackgroundMusic() {
        if (getBackgroundMusic() != null) {
            if (getBackgroundMusic().isPlaying()) {
                getBackgroundMusic().stop();
            }
            getBackgroundMusic().release();
            backgroundMusic = null;
        }
    }

    public boolean checkFragmentVisible() {
        PracticeQuizFragment practiceQuizFragment = (PracticeQuizFragment) getSupportFragmentManager().
                findFragmentByTag("practiceQuizFragment");

        ExamQuizFragment examQuizFragment = (ExamQuizFragment) getSupportFragmentManager().
                findFragmentByTag("examQuizFragment");

        if (practiceQuizFragment != null && practiceQuizFragment.isVisible()) {
            return true;
        } else if (examQuizFragment != null && examQuizFragment.isVisible()) {
            return true;
        }

        return false;
    }

    public void ExitClick() {
        LinearLayout exitContainer = (LinearLayout) findViewById(R.id.exitViewContainer);
        exitContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void AboutClick() {
        LinearLayout aboutContainer = (LinearLayout) findViewById(R.id.aboutViewContainer);
        final Activity activity = this;
        aboutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity).
                        setTitle("Message").
                        setMessage("Project 15.2A").
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setCancelable(false).setIcon(R.drawable.ic_vector_message).show();
            }
        });
    }

    public void goToExamQuiz() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, new ExamQuizFragment(), "examQuizFragment")
                .addToBackStack("examQuizFragment").commit();
    }

    public void addQuestionData() {
        boolean isContainData = myDB.CheckNullQuestionTable();

        if (!isContainData) {
            addEasyQuestion();
            addMediumQuestion();
            addHardQuestion();
        }
    }

    public void addHardQuestion() {
        String id, question, ans1, ans2, ans3, ans4, correctAns, difficulty;
        id = "H1";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> ints[] = { 0, 1, 2, 3 }; <br>\n" +
                "\t\t\t<font color=blue>int</font> *i1 = ints + 1; <br>\n" +
                "\t\t\t<font color=blue>int</font> a = ++*i1; <br>\n" +
                "\t\t\t<font color=blue>int</font> b = a + *i1; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\\n\"</font>, b); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "4";
        ans2 = "3";
        ans3 = "6";
        ans4 = "Another";
        correctAns = "4";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H2";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> ints[] = { 0, 5, 10, 15 }; <br>\n" +
                "\t\t\t<font color=blue>int</font>* i2 = ints + 2; <br>\n" +
                "\t\t\t<font color=blue>int</font> a = *i2++; // a = *(i2++); <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d#%d\\n\"</font>, a, *i2); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "10#15";
        ans2 = "10#10";
        ans3 = "15#15";
        ans4 = "11#15";
        correctAns = "10#15";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H3";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> ints[] = { 0, 1, 2, 3 }; <br>\n" +
                "\t\t\t<font color=blue>int</font>* i1 = ints + 1; <br>\n" +
                "\t\t\t<font color=blue>int</font>* i2 = ints + 2; <br>\n" +
                "\t\t\t<font color=blue>int</font> a = ++*i1 + *i2++; <br>\n" +
                "\t\t\t<font color=blue>int</font> b = *++i1 + *i2--; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d#%d\"</font>, a, b); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "4#4";
        ans2 = "4#5";
        ans3 = "5#6";
        ans4 = "4#6";
        correctAns = "4#5";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H4";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> i = 400; <br>\n" +
                "\t\t\t<font color=blue>int</font> *ptr = &i; <br>\n" +
                "\t\t\t*++ptr = 2; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d %d\"</font>, i, *ptr); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "400 2";
        ans2 = "400 400";
        ans3 = "400 401";
        ans4 = "Compiler error";
        correctAns = "400 2";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H5";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>char</font> str[] = {<font color=#FF5722>\"pvpit\"</font>}; <br>\n" +
                "\t\t\t<font color=blue>char</font> *s1 = str; <br>\n" +
                "\t\t\ts1++; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%c\"</font>, *s1); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "pvpit";
        ans2 = "vpit";
        ans3 = "v";
        ans4 = "Another";
        correctAns = "v";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H6";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>char</font> *s = <font color=#FF5722>\"\\12345s\\n\"</font>; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, strlen(s)); <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"\\n%s\"</font>, s); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "5";
        ans2 = "7";
        ans3 = "9";
        ans4 = "10";
        correctAns = "5";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H7";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> x = 5, y = 6; <br>\n" +
                "\t\t\t<font color=blue>int</font>* <font color=blue>const</font> p = &x; <br>\n" +
                "\t\t\tp = &y;  <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, (*p));  <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "Compiler error";
        ans2 = "5";
        ans3 = "6";
        ans4 = "Another";
        correctAns = "Compiler error";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H8";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>float</font> func() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> r = 0, d = 0, i = 0; <br>\n" +
                "\t\t\t<font color=blue>for</font> (i; i < 2; i++) <br>\n" +
                "\t\t\t{ <br>\n" +
                "\t\t\t\t\t\tr += 5 / d; <br>\n" +
                "\t\t\t} <br>\n" +
                "\t\t\t<font color=blue>return</font> r; <br>\n" +
                "}";
        ans1 = "5";
        ans2 = "0";
        ans3 = "Exception";
        ans4 = "Another";
        correctAns = "Exception";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H9";
        question = "Kết quả của đoạn code sau đây là?<br>\n" +
                "<font color=blue>void</font> main()<br>\n" +
                "{<br>\n" +
                "\t\t\t<font color=blue>int</font> a = 80;<br>\n" +
                "\t\t\t<font color=blue>if</font>(a++ > 80)<br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"C/C++ %d\"</font>, a);<br>\n" +
                "\t\t\t<font color=blue>else</font><br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"Java %d\"</font>, a);<br>\n" +
                " <br>\n" +
                "\t\t\tgetch();<br>\n" +
                "}";
        ans1 = "mmmm aaaa nnnn";
        ans2 = "mmm aaa nnn";
        ans3 = "mmmm aaa nnn";
        ans4 = "Another";
        correctAns = "mmmm aaaa nnnn";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H10";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main()<br>\n" +
                "{<br>\n" +
                "\t\t\t<font color=blue>int</font> i;<br>\n" +
                "\t\t\ti = 0;<br>\n" +
                "\t\t\t<font color=blue>if</font>(i = 15, 10, 5)<br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"C/C++ %d\"</font>, i);<br>\n" +
                "\t\t\t<font color=blue>else</font><br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"Java %d\"</font>, i);<br>\n" +
                " <br>\n" +
                "\t\t\tgetch();<br>\n" +
                "}";
        ans1 = "AAAAAABCDEF";
        ans2 = "AAAAAABCDE";
        ans3 = "ABCDEF";
        ans4 = "Another";
        correctAns = "AAAAAABCDEF";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H11";
        question = "Kết quả của đoạn code sau đây là? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{<br>\n" +
                "\t\t\t<font color=blue>int</font> k;<br>\n" +
                "\t\t\t<font color=blue>for</font> (k = -3; k < -5; k++)<br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"Hello\"</font>);<br>\n" +
                "\t\t\tgetch();<br>\n" +
                "}";
        ans1 = "Hello";
        ans2 = "Nothing";
        ans3 = "Compiler Error";
        ans4 = "Run time error";
        correctAns = "Nothing";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H12";
        question = "Kết quả của đoạn code sau đây là?<br>\n" +
                "<font color=blue>void</font> main()<br>\n" +
                "{<br>\n" +
                "\t\t\t<font color=blue>double</font> k = 0;<br>\n" +
                "\t\t\t<font color=blue>for</font> (k = 0.0; k < 3.0; k++);<br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%lf\"</font>, k);<br>\n" +
                "\t\t\tgetch();<br>\n" +
                "}";
        ans1 = "012";
        ans2 = "Run time error";
        ans3 = "3";
        ans4 = "2";
        correctAns = "3";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H13";
        question = "Kết quả của đoạn code sau đây là?<br>\n" +
                "<font color=blue>int</font> main()<br>\n" +
                "{<br>\n" +
                "\t\t\t<font color=blue>int</font> i = 0;<br>\n" +
                "\t\t\t<font color=blue>for</font> (; ; ; )<br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"In for loop\\n\"</font>);<br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"After loop\\n\"</font>);<br>\n" +
                "}";
        ans1 = "Compile time error";
        ans2 = "Infinite Loop";
        ans3 = "Nothing happen";
        ans4 = "Another";
        correctAns = "Compile time error";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H14";
        question = "Kết quả của đoạn code sau đây là?<br>\n" +
                "<font color=blue>int</font> main()<br>\n" +
                "{<br>\n" +
                "\t\t\t<font color=blue>int</font> i = 0;<br>\n" +
                "\t\t\t<font color=blue>while</font> (i = 0)<br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"True\\n\"</font>);<br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"False\\n\"</font>);<br>\n" +
                "\t\t\tgetch();<br>\n" +
                "}";
        ans1 = "True";
        ans2 = "False";
        ans3 = "Compile Error";
        ans4 = "Another";
        correctAns = "False";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //------------------------------------------------------------

        id = "H15";
        question = "Kết quả của đoạn code sau đây là?<br>\n" +
                "<font color=blue>int</font> main()<br>\n" +
                "{<br>\n" +
                "\t\t\t<font color=blue>int</font> a = 0, i = 0, b = 0 ;<br>\n" +
                "\t\t\t<font color=blue>for</font> (i = 0; i < 5; i++)<br>\n" +
                "\t\t\t{<br>\n" +
                "\t\t\t\t\t\ta++;<br>\n" +
                "\t\t\t\t\t\t<font color=blue>continue</font>;<br>\n" +
                "\t\t\t\t\t\tb++;<br>\n" +
                "\t\t\t}<br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"\\n a = %d ,b =%d\"<//font>, a, b);<br>\n" +
                "\t\t\tgetch();<br>\n" +
                "}";
        ans1 = "a = 5,b = 5";
        ans2 = "a = 4,b = 4";
        ans3 = "a = 5,b = 0";
        ans4 = "Another";
        correctAns = "a = 5,b = 0";
        difficulty = "Hard";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);
    }

    public void addMediumQuestion() {
        String id, question, ans1, ans2, ans3, ans4, correctAns, difficulty;
        id = "M1";
        question = "Kết quả trả về của đoạn code sau là? <br>\n" +
                "<font color=blue>typedef struct</font> <br>\n" +
                "{ <br>\n" +
                "\t\t<font color=blue>char</font> c; <font color=green>// 1 byte</font> <br>\n" +
                "\t\t<font color=blue>float</font> b; <font color=green>// 4 byte</font> <br>\n" +
                "\t\t<font color=blue>int</font> a;   <font color=green>// 4 byte</font> <br>\n" +
                "}A;  <br>\n" +
                "  <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\tprintf(<font color=#FF5722>\"\\n Size of struct: %d\"</font>, <font color=blue>sizeof</font>(A)); <br>\n" +
                "\t\tgetch(); <br>\n" +
                "}";
        ans1 = "9";
        ans2 = "12";
        ans3 = "16";
        ans4 = "24";
        correctAns = "12";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M2";
        question = "Kết quả trả về của đoạn code sau là? <br>\n" +
                "<font color=blue>int</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>double</font> num = 5.2; <br>\n" +
                "\t\t\t<font color=blue>int</font> var = 5; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\\t\"</font>, <font color=blue>sizeof</font>(!num)); <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\\t\"</font>, <font color=blue>sizeof</font>(var=15/2)); <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, var); <br>\n" +
                "\t\t\t<font color=blue>return</font> 0; <br>\n" +
                "}";
        ans1 = "1 4 5";
        ans2 = "1 4 7";
        ans3 = "8 4 7";
        ans4 = "Another";
        correctAns = "1 4 5";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M3";
        question = "Lệnh printf sau đây sẽ in ra giá trị nào? <br>\n" +
                "<font color=blue>int</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> w = 3; <br>\n" +
                "\t\t\t<font color=blue>int</font> x = 31; <br>\n" +
                "\t\t\t<font color=blue>int</font> y = 10; <br>\n" +
                "\t\t\t<font color=blue>double</font> z = x / y % w; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%f\\n\"</font>, z); <br>\n" +
                " <br>\n" +
                "\t\t\t<font color=blue>return</font> 0; <br>\n" +
                "}";
        ans1 = "1";
        ans2 = "0";
        ans3 = "0.1";
        ans4 = "Another";
        correctAns = "0";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M4";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>int</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>char</font> a = 250; <br>\n" +
                "\t\t\t<font color=blue>int</font> expr; <br>\n" +
                "\t\t\texpr= a + !a + ~a + ++a; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, expr); <br>\n" +
                "\t\t\t<font color=blue>return</font> 0; <br>\n" +
                "}";
        ans1 = "-6";
        ans2 = "4";
        ans3 = "5";
        ans4 = "Another";
        correctAns = "-6";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M5";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>int</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> a = -5; <br>\n" +
                "\t\t\t<font color=blue>unsigned int</font> b = -5u; <font color=green>// (*)</font> <br>\n" +
                "\t\t\t<font color=blue>if</font>(a == b) <br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"Avatar\"</font>); <br>\n" +
                "\t\t\t<font color=blue>else</font> <br>\n" +
                "\t\t\t\t\t\tprintf(<font color=#FF5722>\"Alien\"</font>); <br>\n" +
                "\t\t\t<font color=blue>return</font> 0; <br>\n" +
                "}";
        ans1 = "Avatar";
        ans2 = "Alien";
        ans3 = "Error at (*)";
        ans4 = "Another";
        correctAns = "Avatar";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M6";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> x = 3; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, x++ + ++x); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "7";
        ans2 = "8";
        ans3 = "9";
        ans4 = "Another";
        correctAns = "8";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M7";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> i = 5, j = 6, k; <br>\n" +
                "\t\t\tk = i & j; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, k); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "4";
        ans2 = "0";
        ans3 = "1";
        ans4 = "5";
        correctAns = "4";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M8";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> i = 5, j = 6; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, i | j);<br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "7";
        ans2 = "6";
        ans3 = "5";
        ans4 = "1";
        correctAns = "7";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M9";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>extern int</font> x = 0; <br>\n" +
                "<font color=blue>void</font> main()<br>\n" +
                "{<br>\n" +
                "\t\t\tx++; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, x); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "0";
        ans2 = "Error";
        ans3 = "1";
        ans4 = "x isn’t defined";
        correctAns = "1";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M10";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t{ <br>\n" +
                "\t\t\t\t\t\t<font color=blue>int</font> x = 1; <br>\n" +
                "\t\t\t} <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, x); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "0";
        ans2 = "1";
        ans3 = "Error Compiler";
        ans4 = "Another";
        correctAns = "0";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M11";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>int</font> y = 0; <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t{ <br>\n" +
                "\t\t\t\t\t\t<font color=blue>int</font> x = 0; <br>\n" +
                "\t\t\t\t\t\tx++; <br>\n" +
                "\t\t\t\t\t\t++y; <br>\n" +
                "\t\t\t} <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\\t%d\"</font>, x, y); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "1 1";
        ans2 = "1 0";
        ans3 = "x undeclared identifier";
        ans4 = "Another";
        correctAns = "x undeclared identifier";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M12";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> x; <br>\n" +
                "\t\t\t{ <br>\n" +
                "\t\t\t\t\t\tx++; <br>\n" +
                "\t\t\t} <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, x); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "1";
        ans2 = "0";
        ans3 = "Error";
        ans4 = "Another";
        correctAns = "Error";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M13";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> x=0; <br>\n" +
                "\t\t\t{ <br>\n" +
                "\t\t\t\t\t\t<font color=blue>int</font> x = 0, y = 0; <br>\n" +
                "\t\t\t\t\t\ty++; <br>\n" +
                "\t\t\t\t\t\tx++; <br>\n" +
                "\t\t\t} <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, x); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "1";
        ans2 = "Error";
        ans3 = "0";
        ans4 = "Another";
        correctAns = "0";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M14";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>const int</font> x = 5; <br>\n" +
                "<font color=blue>void</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> x[x]; <br>\n" +
                "\t\t\t<font color=blue>int</font> y = <font color=blue>sizeof</font>(x) / <font color=blue>sizeof</font>(<font color=blue>int</font>); <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d\"</font>, y); <br>\n" +
                "\t\t\tgetch(); <br>\n" +
                "}";
        ans1 = "1";
        ans2 = "5";
        ans3 = "20";
        ans4 = "x isn’t defined";
        correctAns = "5";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //---------------------------------------------------------------------------------

        id = "M15";
        question = "Kết quả của đoạn code sau đây là gì? <br>\n" +
                "<font color=blue>int</font> main() <br>\n" +
                "{ <br>\n" +
                "\t\t\t<font color=blue>int</font> x = 5, y = 10, z = 15; <br>\n" +
                "\t\t\tprintf(<font color=#FF5722>\"%d %d %d\"</font>); <br>\n" +
                "\t\t\t<font color=blue>return</font> 0; <br>\n" +
                "}";
        ans1 = "Garbage Garbage Garbage";
        ans2 = "5 10 15";
        ans3 = "15 10 5";
        ans4 = "Run time error";
        correctAns = "Garbage Garbage Garbage";
        difficulty = "Medium";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);
    }

    public void addEasyQuestion() {
        String id, question, ans1, ans2, ans3, ans4, correctAns, difficulty;
        id = "E1";
        question = "Từ khóa nào sau đây lập tức thoát khỏi hàm và trả về một giá trị nào đó?";
        ans1 = "<font color=blue>goto</font>";
        ans2 = "<font color=blue>break</font>";
        ans3 = "<font color=blue>continue</font>";
        ans4 = "<font color=blue>return</font>";
        correctAns = "<font color=blue>return</font>";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E2";
        question = "Để kết thúc một câu lệnh, chúng ta dùng dấu câu nào sau đây?";
        ans1 = ";";
        ans2 = ":";
        ans3 = ".";
        ans4 = ",";
        correctAns = ";";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E3";
        question = "Đáp án nào sau đây không phải là một kiểu dữ liệu?";
        ans1 = "<font color=blue>float</font>";
        ans2 = "<font color=blue>break</font>";
        ans3 = "<font color=blue>int</font>";
        ans4 = "<font color=blue>double</font>";
        correctAns = "<font color=blue>break</font>";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E4";
        question = "Dấu nào sau đây dùng để so sánh hai biến với nhau?";
        ans1 = ":=";
        ans2 = "=";
        ans3 = "==";
        ans4 = "!";
        correctAns = "==";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E5";
        question = "Trong câu lệnh switch case, từ khóa nào sau đây ngăn không cho chương trình thực thi câu lệnh của case kế tiếp?";
        ans1 = "<font color=blue>end</font>";
        ans2 = "<font color=blue>break</font>";
        ans3 = "<font color=blue>stop</font>";
        ans4 = "<font color=blue>continue</font>";
        correctAns = "<font color=blue>break</font>";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E6";
        question = "Hàm nào không trả về giá trị?";
        ans1 = "<font color=blue>int</font>";
        ans2 = "<font color=blue>float</font>";
        ans3 = "<font color=blue>void</font>";
        ans4 = "<font color=blue>double</font>";
        correctAns = "<font color=blue>void</font>";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E7";
        question = "Hàm nào sau đây không thể thiếu trong một chương trình C++?";
        ans1 = "start()";
        ans2 = "system()";
        ans3 = "main()";
        ans4 = "program()";
        correctAns = "main()";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);
        ;

        //-------------------------------------------------------------

        id = "E8";
        question = "Cách đặt tên biến nào sau đây được viết đúng theo quy tắc đặt tên của ngôn ngữ C++?";
        ans1 = "diem toan";
        ans2 = "3diemtoan";
        ans3 = "_diemtoan";
        ans4 = "-diemtoan";
        correctAns = "_diemtoan";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E9";
        question = "Đáp án nào sau đâu không phải là một cấu trúc lặp?";
        ans1 = "<font color=blue>for</font>";
        ans2 = "<font color=blue>do white</font>";
        ans3 = "<font color=blue>while</font>";
        ans4 = "<font color=blue>if else</font>";
        correctAns = "<font color=blue>if else</font>";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);
        ;

        //-------------------------------------------------------------

        id = "E10";
        question = "Kiểu dữ liệu nào sau đây được xem là kiểu dữ liệu cơ bản của ngôn ngữ C++?";
        ans1 = "Kiểu double";
        ans2 = "Kiểu con trỏ";
        ans3 = "Kiểu hợp";
        ans4 = "Kiểu mảng";
        correctAns = "Kiểu double";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E11";
        question = "Giả sử a và b là 2 số thực. Biểu thức nào sau đây không được phép?";
        ans1 = "a += b";
        ans2 = "a -= b";
        ans3 = "a >>= b";
        ans4 = "a *= b";
        correctAns = "a >>= b";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E12";
        question = "Xâu định dạng nào sau đây dùng để in ra một số nguyên?";
        ans1 = "<font color=#FF5722>%p</font>";
        ans2 = "<font color=#FF5722>%f</font>";
        ans3 = "<font color=#FF5722>%d</font>";
        ans4 = "<font color=#FF5722>%e</font>";
        correctAns = "<font color=#FF5722>%d</font>";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E13";
        question = "Xâu định dạng nào dưới đây dùng để in ra một xâu kí tự?";
        ans1 = "<font color=#FF5722>%f</font>";
        ans2 = "<font color=#FF5722>%x</font>";
        ans3 = "<font color=#FF5722>%s</font>";
        ans4 = "<font color=#FF5722>%c</font>";
        correctAns = "<font color=#FF5722>%s</font>";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E14";
        question = "Ngôn ngữ lập trình nào sau đây là ngôn ngữ lập trình có cấu trúc?";
        ans1 = "Assembler";
        ans2 = "C và Pascal";
        ans3 = "Cobol";
        ans4 = "Tất cả đều đúng";
        correctAns = "C và Pascal";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);

        //-------------------------------------------------------------

        id = "E15";
        question = "Ngôn ngữ C được Dennis Ritchie đưa ra vào năm nào?";
        ans1 = "1967";
        ans2 = "1972";
        ans3 = "1970";
        ans4 = "1976";
        correctAns = "1972";
        difficulty = "Easy";
        myDB.AddQuestionData(id, question, ans1, ans2, ans3, ans4, correctAns, difficulty);
    }
    //endregion

    //region ViewPager and SlideTag
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        List<Fragment> listFragment = new ArrayList<>();
        listFragment.add(new PracticeFragment());
        listFragment.add(new ExamFragment());
        listFragment.add(new InteractiveFragment());

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(), listFragment);

        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //endregion
}