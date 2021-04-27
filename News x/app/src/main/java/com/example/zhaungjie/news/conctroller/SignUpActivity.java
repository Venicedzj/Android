package com.example.zhaungjie.news.conctroller;

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

public class SignUpActivity extends AppCompatActivity {

    private MydatabaseHelper dbHelper;
    private TextView username;
    private  TextView password;
    private TextView password_confirm;
    private Button ok;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        dbHelper = new MydatabaseHelper(this, "NewsStore.db", null,1);
        db = dbHelper.getWritableDatabase();
        username = (TextView) findViewById(R.id.Username);
        password =(TextView) findViewById(R.id.password);
        password_confirm = (TextView) findViewById(R.id.password_confirm);
        ok = (Button) findViewById(R.id.btn_OK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("")&&
                        !password.getText().toString().equals("")&&
                        !password_confirm.getText().toString().equals("")){
                    Cursor cursor =db.rawQuery("select * from info where username =?", new String[] {username.getText().toString()});
                    if(cursor.moveToFirst()){
                       Toast.makeText(SignUpActivity.this, "change a username", Toast.LENGTH_SHORT).show();
                        username.setText("");
                    }
                    else if(password.getText().toString().equals(password.getText().toString())){
                        db.execSQL("insert into info (username, password) values(?,?)",
                                new String[] {username.getText().toString(),password.getText().toString()});
                        Toast.makeText(SignUpActivity.this, "sign up successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    Toast.makeText(SignUpActivity.this, "fill in username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
