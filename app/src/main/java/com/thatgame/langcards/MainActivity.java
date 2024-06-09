package com.thatgame.langcards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    // Создание экземпляра базы данных
    dbConnect db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Инициализация полей для ввода имени пользователя и пароля
        EditText usrname = (EditText) findViewById(R.id.username);
        EditText pssword = (EditText) findViewById(R.id.password);

        // Инициализация подключения к базе данных
        db = new dbConnect(this);

        // Инициализация кнопки входа
        MaterialButton loginBtn = (MaterialButton) findViewById(R.id.singInButton);

        // Добавление обработчика нажатий к кнопке входа
        loginBtn.setOnClickListener(view -> {
            // Получение имени пользователя и пароля из полей ввода
            String username = usrname.getText().toString();
            String password = pssword.getText().toString();

            // Проверка имени пользователя и пароля
            if (db.checkUser(username, password)) {
                // Если проверка прошла успешно, то переход к активности выбора языка
                Intent intent = new Intent(getApplicationContext(), LangChooserActivity.class);
                // Создание новой таблицы в базе данных
                db.createNewTableDict("Английский_" + username);

                // Добавление дополнительных данных к намерению
                intent.putExtra("watched", 0);
                intent.putExtra("flag", 1);
                intent.putExtra("login", username);

                // Запуск новой активности
                startActivity(intent);
            } else {
                // Если проверка не прошла успешно, то отображение уведомления
                Toast.makeText(MainActivity.this, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
            }
        });

        // Инициализация текстового поля для перехода к регистрации
        TextView noReg = (TextView) findViewById(R.id.toReg);
        noReg.setOnClickListener(view -> {
            // При нажатии на текстовое поле осуществляется переход к активности регистрации
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });

        // Инициализация текстового поля для смены пароля
        TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(v -> {
            // При нажатии на текстовое поле осуществляется переход к активности смены пароля
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        // Инициализация текстового поля для входа без регистрации
        TextView withoutReg = (TextView) findViewById(R.id.noReg);
        withoutReg.setOnClickListener(v -> {
            // При нажатии на текстовое поле происходит очистка незарегистрированного пользователя и переход
            // к активности выбора языка
            Intent intent = new Intent(getApplicationContext(), LangChooserActivity.class);
            db.cleanUnregDict();

            // Добавление дополнительных данных к намерению
            intent.putExtra("watched", 0);
            intent.putExtra("flag", 0);
            intent.putExtra("login", "unreg");

            // Запуск новой активности
            startActivity(intent);
        });
    }
}