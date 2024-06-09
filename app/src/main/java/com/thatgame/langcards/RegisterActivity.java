package com.thatgame.langcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegisterActivity extends AppCompatActivity {

    // Объявление объекта для подключения к базе данных
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Инициализация полей для ввода нового имени пользователя и пароля
        EditText newUsername = (EditText) findViewById(R.id.newUsername);
        EditText newPassword = (EditText) findViewById(R.id.newPassword);

        // Инициализация кнопки регистрации
        MaterialButton registerBtn = (MaterialButton) findViewById(R.id.singUpButton);

        // Инициализация объекта для подключения к базе данных
        db = new dbConnect(this);

        // Установка обработчика для нажатия на кнопку регистрации
        registerBtn.setOnClickListener(v -> {
            // Получение текста из полей ввода
            String username = newUsername.getText().toString();
            String password = newPassword.getText().toString();

            // Проверка, существует ли такое имя пользователя в базе данных
            if (db.checkUsername(username)) {
                // Добавление нового пользователя в базу данных, если данное имя пользователя еще не существует
                db.addUser(username, password);

                // Переход обратно на главное активность после успешной регистрации
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                // Отображение уведомления, если данное имя пользователя уже существует
                Toast.makeText(RegisterActivity.this, "Такой логин уже существует", Toast.LENGTH_LONG).show();
            }
        });
    }
}