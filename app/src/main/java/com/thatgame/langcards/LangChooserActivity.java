package com.thatgame.langcards;

import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Objects;

public class LangChooserActivity extends AppCompatActivity implements View.OnClickListener {
    // Список языков у пользователя
    ArrayList<String> langs;

    // Объект для подключений к базе данных
    dbConnect db;

    LinearLayout linear;

    // Переменные для хранения данных пользователя
    int watched;
    int flag;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lang_chooser);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Инициализация объекта для взаимодействия с базой данных
        db = new dbConnect(this);

        // Получение переданных данных из предыдущей активности
        watched = getIntent().getIntExtra("watched", watched);
        flag = getIntent().getIntExtra("flag", flag);
        login = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("login")).toString();

        // Инициализация контейнера
        linear = (LinearLayout) findViewById(R.id.linear);

        // Установка обработчика нажатия для кнопки добавления нового языка
        Button plus = (Button) findViewById(R.id.plusButton);
        plus.setOnClickListener(v -> {
            // Всплывающее окно для добавления нового языка
            LangDialog langDialog = new LangDialog();
            langDialog.show(getSupportFragmentManager(), "123");
        });

        // Инициализация списка языков
        init();
    }

    public void init() {
        // Очистка всех предыдущих дочерних вьюшек в контейнере
        linear.removeAllViews();

        // Получение списка языков из базы данных
        langs = db.getLangs(login);

        // Добавление кнопок с языками на экран
        for (int i = 0; i < langs.size(); i++) {
            Button b = new Button(this);
            b.setText(langs.get(i));
            b.setOnClickListener(this);
            linear.addView(b);
        }
    }

    @Override
    public void onClick(View view) {
        // Обработчик нажатия кнопки языка для перехода к активности изучения карточек
        Intent intent = new Intent(LangChooserActivity.this, FlashCardsActivity.class);
        intent.putExtra("lang", ((Button) view).getText().toString());
        intent.putExtra("watched", watched);
        intent.putExtra("flag", flag);
        intent.putExtra("login", login);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Инициализация меню активности
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Обработка выбора элементов меню
        int id = item.getItemId();
        if (id == R.id.stats) {
            // Если выбран пункт "Статистика", показываем диалог со статистикой
            StatsDialog stats = new StatsDialog();
            stats.show(getSupportFragmentManager(), "123");
        }
        if (id == R.id.out) {
            // Если выбран пункт "Выход", переходим на главную активность
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}