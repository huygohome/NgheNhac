package com.example.nghenhac;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);
        databaseHelper = new DatabaseHelper(this);

        buttonRegister.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            // Kiểm tra các trường
            if (username.isEmpty()) {
                editTextUsername.setError("Tên đăng nhập không được để trống");
                editTextUsername.requestFocus();
                return;
            }

            if (username.length() < 3) {
                editTextUsername.setError("Tên đăng nhập phải có ít nhất 3 ký tự");
                editTextUsername.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                editTextPassword.setError("Mật khẩu không được để trống");
                editTextPassword.requestFocus();
                return;
            }

            if (password.length() < 8) {
                editTextPassword.setError("Mật khẩu phải có ít nhất 8 ký tự");
                editTextPassword.requestFocus();
                return;
            }

            if (!containsUpperCase(password)) {
                editTextPassword.setError("Mật khẩu phải có ít nhất 1 ký tự viết hoa");
                editTextPassword.requestFocus();
                return;
            }

            if (!containsSpecialCharacter(password)) {
                editTextPassword.setError("Mật khẩu phải có ít nhất 1 ký tự đặc biệt");
                editTextPassword.requestFocus();
                return;
            }

            if (!containsDigit(password)) {
                editTextPassword.setError("Mật khẩu phải có ít nhất 1 ký tự số");
                editTextPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmPassword)) {
                editTextConfirmPassword.setError("Mật khẩu không khớp");
                editTextConfirmPassword.requestFocus();
                return;
            }

            // Kiểm tra xem tên đăng nhập đã tồn tại chưa
            if (databaseHelper.checkUsername(username)) {
                editTextUsername.setError("Tên đăng nhập đã tồn tại");
                editTextUsername.requestFocus();
                return;
            }

            // Lưu thông tin người dùng mới vào cơ sở dữ liệu
            if (databaseHelper.insertData(username, password)) {
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng RegisterActivity
            } else {
                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean containsUpperCase(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsSpecialCharacter(String str) {
        String specialCharacters = "!@#$%^&*()-_=+[]{};:'\"\\|,.<>?/`~";
        for (char c : str.toCharArray()) {
            if (specialCharacters.indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    private boolean containsDigit(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}