package com.example.zhaungjie.news.conctroller;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaungjie.news.model.MydatabaseHelper;
import com.example.zhaungjie.news.R;

public class LoginActivity extends AppCompatActivity {

    private MydatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private TextView username;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.btn_login);
        Button signup = (Button) findViewById(R.id.btn_signup);
        username = (TextView) findViewById(R.id.Username);
        password = (TextView) findViewById(R.id.input_password);
        dbHelper= new MydatabaseHelper(this, "NewsStore.db", null,1);
        db=dbHelper.getWritableDatabase();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("")){
                    Cursor cursor = db.rawQuery("select * from info where username=?",
                            new String[]{username.getText().toString()});
                    if(cursor.moveToFirst()){
                        String p = cursor.getString(cursor.getColumnIndex("password"));
                        if(p.equals(password.getText().toString())) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "no this User", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "input username", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
