package com.example.mysqlitememo01;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Date;

import android.view.View;
import android.widget.EditText;
import android.database.*;
import android.database.sqlite.*;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    EditText et;
    EditText ekey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText)findViewById(R.id.EditText01);
        ekey = (EditText)findViewById(R.id.EditKey);
        //データベース作成
        String dbStr= "data/data/" + getPackageName() + "/nhs00518.db";
        db = SQLiteDatabase.openOrCreateDatabase(dbStr,null);

        //データベース作成クエリ文
        String query_table1 = "DROP TABLE IF EXISTS memopad";
        String query_table2 = "CREATE TABLE memopad" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," + "key1 TEXT,memo TEXT,write_date TEXT)";

        //テーブル作成
        db.execSQL(query_table1);
        db.execSQL(query_table2);

        String write_dateStr = now_date();

        String query_record_set[] = {
                "INSERT INTO memopad(key1,memo,write_date)VALUES('test01','テスト01','"+ write_dateStr +"')",
                "INSERT INTO memopad(key1,memo,write_date)VALUES('test02','テスト02','"+ write_dateStr +"')",
                "INSERT INTO memopad(key1,memo,write_date)VALUES('test03','テスト03','"+ write_dateStr +"')"
        };

        for(int i=0; i<query_record_set.length; i++){
            db.execSQL(query_record_set[i]);
        }

    }

    //本文クリア
    public void functionKeyClear(View v){
        et.setText("");
    }

    public void functionKeyRead(View v){
        String keyStr = ekey.getText().toString();

        //レコード検索
        String query_select = "SELECT * FROM memopad WHERE key1='" + keyStr + "'";

        //DB検索実行
        Cursor db_row = db.rawQuery(query_select, null);

        String result_str = "";
        String keyStr1 = "";
        String memo = "";
        String write_date = "";

        while (db_row.moveToNext()){
            int index_key = db_row.getColumnIndex("key1");
            int index_memo = db_row.getColumnIndex("memo");
            int index_write_date = db_row.getColumnIndex("write_date");
            keyStr1 = db_row.getString(index_key);
            memo = db_row.getString(index_memo);
            write_date = db_row.getString(index_write_date);
            result_str += "KEY:" + keyStr1 + "\n 本文:" + memo + "\n 作成日付:" + write_date + "\n\n";
        }
        Toast myToast = Toast.makeText(this, keyStr1 + "の内容を読み込みました。",Toast.LENGTH_LONG);
        myToast.show();
        et.setText(result_str);
        if(!db_row.moveToFirst()){
//            int index_memo = db_row.getColumnIndex("memo");
//            memo = db_row.getString(index_memo);

            Toast.makeText(this, keyStr1 + "指定されたキーはありません。",Toast.LENGTH_LONG).show();
        }
    }

    public void functionKeyList(View v){
        String query_select = "SELECT * FROM memopad ORDER BY id";

        Cursor db_row = db.rawQuery(query_select,null);

        String result_str = "";
        String key = "";
        String memo = "";
        String write_date = "";
        while (db_row.moveToNext()){
            int index_key = db_row.getColumnIndex("key1");
            int index_memo = db_row.getColumnIndex("memo");
            int index_write_date = db_row.getColumnIndex("write_date");
            key = db_row.getString(index_key);
            memo = db_row.getString(index_memo);
            write_date = db_row.getString(index_write_date);
            result_str += "KEY:" + key + "\n 本文:" + memo + "\n 作成日付:" + write_date + "\n\n";
        }
        et.setText(result_str);

        Toast myToast = Toast.makeText(this, "全レコードを読み込みました。",Toast.LENGTH_LONG);
        myToast.show();
    }

    public void functionKeyWrite(View v){
        String key = ekey.getText().toString();
        String memo = et.getText().toString();
        String write_dateStr = now_date();
        String query_record_write = "INSERT INTO memopad(key1,memo,write_date)" +
                "VALUES('" + key +"', + '" + memo +"','" + write_dateStr +"')";
        db.execSQL(query_record_write);
        Toast myToast = Toast.makeText(this, "をキーにして書き込みを完了しました。",Toast.LENGTH_LONG);
        myToast.show();
    }

    public void  functionKeyDelete(View v){
        String key = ekey.getText().toString();
        String query_record_delete = "DELETE FROM memopad WHERE key1 = '"+key+"'";

        db.execSQL(query_record_delete);

        Toast myToast = Toast.makeText(this, "キーを削除しました。",Toast.LENGTH_LONG);
        myToast.show();
    }

    private String now_date() {
        Date today = new Date();

        Calendar cal = Calendar.getInstance();

        cal.setTime(today);

        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH)+1;
        int dd = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);

        String now_dateStr = yy + "/" + mm + "/" + dd + "" + hour + ":" + minute + ":" + second;
        return now_dateStr;
    }
}