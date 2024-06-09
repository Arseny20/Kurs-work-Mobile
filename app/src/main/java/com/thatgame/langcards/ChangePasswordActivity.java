package com.thatgame.langcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class ChangePasswordActivity extends AppCompatActivity {
    // Объявление объекта для подключения к базе данных
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Инициализация полей для ввода старого имени пользователя и нового пароля
        EditText oldUsername = (EditText) findViewById(R.id.oldUsername);
        EditText newestPassword = (EditText) findViewById(R.id.newestPassword);

        // Инициализация кнопки сброса пароля
        MaterialButton resetBtn = (MaterialButton) findViewById(R.id.resetButton);

        // Инициализация объекта для подключения к базе данных
        db = new dbConnect(this);

        // Установка обработчика для нажатия на кнопку сброса пароля
        resetBtn.setOnClickListener(v -> {
            // Получение текста из полей ввода
            String username = oldUsername.getText().toString();
            String password = newestPassword.getText().toString();

            // Проверка, существует ли имя пользователя в базе данных
            if (!db.checkUsername(username)) {
                // Обновление пароля пользователя в базе данных, если такое имя пользователя существует
                db.updatePassword(username, password);

                // Переход обратно на главную активность после успешного обновления пароля
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                // Отображение уведомления, если такого имени пользователя не существует
                Toast.makeText(ChangePasswordActivity.this, "Такого логина не существует", Toast.LENGTH_LONG).show();
            }
        });
    }
}