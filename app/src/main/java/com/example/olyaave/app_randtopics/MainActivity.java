package com.example.olyaave.app_randtopics;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    private Button createBut;
    private TextView text;
    private DataBaseManager db;
    SQLiteDatabase SQL_db; // хз зачем

    private View.OnClickListener mOnActionClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(LOG_TAG, "--- onClIck call ---");
            String product = "";

            RandGen r = new RandGen(db.getCountLine(SQL_db));
            int id = r.RandLine();
            Log.d(LOG_TAG, "--- id = " + id);

            try {
                Cursor cursor = SQL_db.rawQuery("SELECT * FROM TOPICS WHERE _id='" + id + "'", null);
                Log.d(LOG_TAG, "---  rawQuery successful ---");
                cursor.moveToFirst();
                product = cursor.getString(1);
                Log.d(LOG_TAG, "--- action = " + product);
                text.setText(product);
                cursor.close();
            } catch (SQLException mSQLException) {
                Log.d(LOG_TAG, mSQLException.getMessage());
                throw mSQLException;
            }
        }
    };

    private View.OnClickListener onSettingsClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent startSettingsIntent = new Intent(MainActivity.this, SettingActivity.class);
//            startSettingsIntent.putExtra();
            startActivity(startSettingsIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBut = findViewById(R.id.button);
        text =  findViewById(R.id.textView);
        db = new DataBaseManager(this);

        try {
            db.updateDataBase();
            Log.d(LOG_TAG, "--- update database ---");
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            SQL_db = db.getWritableDatabase();
            Log.d(LOG_TAG, "--- SQL_db reading successful  ---");
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        createBut.setOnClickListener(mOnActionClickListner);
    }





}
