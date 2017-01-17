package com.example.quiztest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class QuizDatabase extends SQLiteOpenHelper {

    //region Initialize
    public static final String DATABASE_NAME = "Quiz.db";

    public static final String QUESTION_TABLE = "Question_table";
    public static final String USER_CHOICE_TABLE = "User_Choice_table";
    public static final String RECENT_RECORD_TABLE = "Recent_Record_table";

    public static final String QUESTION_TABLE_COL1 = "ID";
    public static final String QUESTION_TABLE_COL2 = "Question";
    public static final String QUESTION_TABLE_COL3 = "Ans1";
    public static final String QUESTION_TABLE_COL4 = "Ans2";
    public static final String QUESTION_TABLE_COL5 = "Ans3";
    public static final String QUESTION_TABLE_COL6 = "Ans4";
    public static final String QUESTION_TABLE_COL7 = "CorrectAns";
    public static final String QUESTION_TABLE_COL8 = "Difficulty";


    public static final String USER_CHOICE_TABLE_COL1 = "ID";
    public static final String USER_CHOICE_TABLE_COL2 = "Question";
    public static final String USER_CHOICE_TABLE_COL3 = "Ans1";
    public static final String USER_CHOICE_TABLE_COL4 = "Ans2";
    public static final String USER_CHOICE_TABLE_COL5 = "Ans3";
    public static final String USER_CHOICE_TABLE_COL6 = "Ans4";
    public static final String USER_CHOICE_TABLE_COL7 = "UserChoice";
    public static final String USER_CHOICE_TABLE_COL8 = "Result";

    public static final String RECENT_RECORD_TABLE_COL1 = "ID";
    public static final String RECENT_RECORD_TABLE_COL2 = "Difficulty";
    public static final String RECENT_RECORD_TABLE_COL3 = "QuizDate";
    public static final String RECENT_RECORD_TABLE_COL4 = "CorrectPercent";
    public static final String RECENT_RECORD_TABLE_COL5 = "QuestionNumber";
    public static final String RECENT_RECORD_TABLE_COL6 = "TimeNumber";
    //endregion

    public QuizDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase quizDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table TABLE_NAME (COL1 text primary key, COL2 text)
        db.execSQL("create table " + QUESTION_TABLE + " (" + QUESTION_TABLE_COL1 + " text primary key, " + QUESTION_TABLE_COL2 + " text, "
                + QUESTION_TABLE_COL3 + " text, " + QUESTION_TABLE_COL4 + " text, " + QUESTION_TABLE_COL5 + " text, "
                + QUESTION_TABLE_COL6 + " text, " + QUESTION_TABLE_COL7 + " text, " + QUESTION_TABLE_COL8 + " text)");

        db.execSQL("create table " + USER_CHOICE_TABLE + " (" + USER_CHOICE_TABLE_COL1 + " text primary key, "
                + USER_CHOICE_TABLE_COL2 + " text, " + USER_CHOICE_TABLE_COL3 + " text, " + USER_CHOICE_TABLE_COL4 + " text, "
                + USER_CHOICE_TABLE_COL5 + " text, " + USER_CHOICE_TABLE_COL6 + " text, " + USER_CHOICE_TABLE_COL7 + " text, "
                + USER_CHOICE_TABLE_COL8 + " text)");

        db.execSQL("create table " + RECENT_RECORD_TABLE + " (" + RECENT_RECORD_TABLE_COL1 + " integer primary key, "
                + RECENT_RECORD_TABLE_COL2 + " text, " + RECENT_RECORD_TABLE_COL3 + " text, "
                + RECENT_RECORD_TABLE_COL4 + " text, " + RECENT_RECORD_TABLE_COL5 + " text, " + RECENT_RECORD_TABLE_COL6 + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + QUESTION_TABLE);
        db.execSQL("drop table if exists " + USER_CHOICE_TABLE);
        db.execSQL("drop table if exists " + RECENT_RECORD_TABLE);

        onCreate(db);
    }

    public boolean AddQuestionData(String id, String question,
                                   String ans1, String ans2, String ans3, String ans4,
                                   String correctAns, String difficulty) {
        boolean isSuccess = true;
        SQLiteDatabase db = this.getWritableDatabase();

        id = "'" + id + "'";
        question = "'" + question + "'";
        ans1 = "'" + ans1 + "'";
        ans2 = "'" + ans2 + "'";
        ans3 = "'" + ans3 + "'";
        ans4 = "'" + ans4 + "'";
        correctAns = "'" + correctAns + "'";
        difficulty = "'" + difficulty + "'";

        String sqlCmd = "insert into " + QUESTION_TABLE + " values(" + id + "," + question + "," +
                ans1 + "," + ans2 + "," + ans3 + "," + ans4 + "," + correctAns + "," + difficulty + ")";

        try {
            db.execSQL(sqlCmd);
        } catch (Exception ex) {
            isSuccess = false;
        }

        return isSuccess;
    }

    public boolean AddUserChoiceData(String id, String question,
                                     String ans1, String ans2, String ans3, String ans4,
                                     String userChoice, String result) {
        boolean isSuccess = true;
        SQLiteDatabase db = this.getWritableDatabase();

        id = "'" + id + "'";
        question = "'" + question + "'";
        ans1 = "'" + ans1 + "'";
        ans2 = "'" + ans2 + "'";
        ans3 = "'" + ans3 + "'";
        ans4 = "'" + ans4 + "'";
        userChoice = "'" + userChoice + "'";
        result = "'" + result + "'";

        String sqlCmd = "insert into " + USER_CHOICE_TABLE + " values(" + id + "," + question + "," +
                ans1 + "," + ans2 + "," + ans3 + "," + ans4 + "," + userChoice + "," + result + ")";

        try {
            db.execSQL(sqlCmd);
        } catch (Exception ex) {
            isSuccess = false;
        }

        return isSuccess;
    }

    public boolean AddRecentRecordData(int id, String difficulty, String date, String correctPercent,
                                       String questionNumber, String timeNumber) {
        boolean isSuccess = true;
        SQLiteDatabase db = this.getWritableDatabase();

        difficulty = "'" + difficulty + "'";
        date = "'" + date + "'";
        correctPercent = "'" + correctPercent + "'";
        questionNumber = "'" + questionNumber + "'";
        timeNumber = "'" + timeNumber + "'";

        String sqlCmd = "insert into " + RECENT_RECORD_TABLE + " values(" + id + "," + difficulty + "," + date + "," +
                correctPercent + "," + questionNumber + "," + timeNumber + ")";

        try {
            db.execSQL(sqlCmd);
        } catch (Exception ex) {
            isSuccess = false;
        }

        return isSuccess;
    }

    public ArrayList<Question> GetQuestionData(String difficulty) {
        ArrayList<Question> listQuestion = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlCmd = "";

        if (difficulty.equals("") || difficulty.equals("Mix")) {
            sqlCmd = "select * from " + QUESTION_TABLE;
        } else {
            difficulty = "'" + difficulty + "'";
            sqlCmd = "select * from " + QUESTION_TABLE +
                    " where " + QUESTION_TABLE_COL8 + " = " + difficulty;
        }

        Cursor cursor = db.rawQuery(sqlCmd, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL1)));
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL2)));
                question.setAns1(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL3)));
                question.setAns2(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL4)));
                question.setAns3(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL5)));
                question.setAns4(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL6)));
                question.setCorrectAns(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL7)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndex(QUESTION_TABLE_COL8)));

                listQuestion.add(question);
            } while (cursor.moveToNext());
        }

        return listQuestion;
    }

    public ArrayList<UserChoice> GetUserChoiceData() {
        ArrayList<UserChoice> userChoicesList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlCmd = "select * from " + USER_CHOICE_TABLE;
        Cursor cursor = db.rawQuery(sqlCmd, null);
        if (cursor.moveToFirst()) {
            do {
                UserChoice choice = new UserChoice();
                choice.setId(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL1)));
                choice.setQuestion(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL2)));
                choice.setAns1(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL3)));
                choice.setAns2(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL4)));
                choice.setAns3(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL5)));
                choice.setAns4(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL6)));
                choice.setUserChoice(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL7)));
                choice.setResult(cursor.getString(cursor.getColumnIndex(USER_CHOICE_TABLE_COL8)));

                userChoicesList.add(choice);
            } while (cursor.moveToNext());
        }

        return userChoicesList;
    }

    public ArrayList<RecentRecord> GetRecentRecordData() {
        ArrayList<RecentRecord> recordList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String sqlCmd = "select * from " + RECENT_RECORD_TABLE;
        Cursor cursor = db.rawQuery(sqlCmd, null);
        if (cursor.moveToFirst()) {
            do {
                RecentRecord record = new RecentRecord();
                record.setId(cursor.getInt(cursor.getColumnIndex(RECENT_RECORD_TABLE_COL1)));
                record.setDifficulty(cursor.getString(cursor.getColumnIndex(RECENT_RECORD_TABLE_COL2)));
                record.setDate(cursor.getString(cursor.getColumnIndex(RECENT_RECORD_TABLE_COL3)));
                record.setCorrectPercent(cursor.getString(cursor.getColumnIndex(RECENT_RECORD_TABLE_COL4)));
                record.setQuestionNumber(cursor.getString(cursor.getColumnIndex(RECENT_RECORD_TABLE_COL5)));
                record.setTimeNumber(cursor.getString(cursor.getColumnIndex(RECENT_RECORD_TABLE_COL6)));

                recordList.add(record);
            } while (cursor.moveToNext());
        }


        return recordList;
    }

    public boolean CheckNullQuestionTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlCmd = "select * from " + QUESTION_TABLE;
        Cursor cursor = db.rawQuery(sqlCmd, null);

        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }

    public void DeleteUserChoiceData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlCmd = "delete from " + USER_CHOICE_TABLE;

        db.execSQL(sqlCmd);
    }

    public void DeleteRecentRecordData(int condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlCmd = "delete from " + RECENT_RECORD_TABLE +
                " where " + RECENT_RECORD_TABLE_COL1 + " < " + condition;

        db.execSQL(sqlCmd);
    }
}
