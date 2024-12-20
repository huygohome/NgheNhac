package com.example.nghenhac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText editTextUsername, editTextPassword;
    Button buttonLogin, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (dbHelper.checkUser (username, password)) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển đến ListActivity khi đăng nhập thành công
                    Intent intent = new Intent(LoginActivity.this, ChuyenManActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc LoginActivity để không quay lại được
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
