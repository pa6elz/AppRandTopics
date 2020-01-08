package com.example.olyaave.app_randtopics;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DataBaseManager extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";

    private static final int DB_VERSION = 1;
    private static String DB_NAME = "TABLE_TOPIC.db";
    private static String DB_PATH = "";
    private static String DB_NAME_TABLE = "TOPICS";

    private SQLiteDatabase mDataBase;
    private Context mContext;
    private boolean mNeedUpdate = false;

    public DataBaseManager(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, 1);
        Log.d(LOG_TAG, "--- constructor database ---");
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        mContext = context;
        Log.d(LOG_TAG, "---  File path = " + DB_PATH + "\n" );
//        Log.d(LOG_TAG, "---  Check DB = " + checkDataBase() + "\n" );
        copyDataBase();
        this.getReadableDatabase();
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DB_PATH + DB_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();
            Log.d(LOG_TAG, "--- copy database ---");
            mNeedUpdate = false;
        }
    }

    public long getCountLine(SQLiteDatabase db){
        Log.d(LOG_TAG, "--- Count line ----");

        String sql = "SELECT COUNT(*) FROM " + DB_NAME_TABLE;

        try {
            SQLiteStatement statement = db.compileStatement(sql);
            long count = statement.simpleQueryForLong();
            Log.d(LOG_TAG, "--- Count line in table = " + count);
            return count;
        } catch (SQLException mSQLException) {
            Log.d(LOG_TAG, mSQLException.getMessage());
            throw mSQLException;
            }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();  // TRUE - base
    }

    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();

            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private void copyDBFile() throws IOException {

        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
        Log.d(LOG_TAG, "--- copyDBFile database ---");
    }

    public boolean openDataBase() throws SQLException {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");

    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion)
                mNeedUpdate = true;
        }
}
