package com.thatgame.langcards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FlashCardsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    dbConnect db;
    String lang;
    List<String> words, translations;
    int count=1;
    int watched, flag;
    String login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        // Инициализация объекта для взаимодействия с базой данных
        db = new dbConnect(this);

        // Получение переданных данных из предыдущей активности
        lang = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("lang")).toString();
        watched = getIntent().getIntExtra("watched", watched);
        flag = getIntent().getIntExtra("flag", flag);
        login = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("login")).toString();

        // Инициализация кнопки добавления нового слова
        Button plus = (Button) findViewById(R.id.plusButton1);
        plus.setOnClickListener(v -> {
            // Всплывающее окно для добавления нового слова
            WordsDialog wordsDialog = new WordsDialog();
            wordsDialog.show(getSupportFragmentManager(), "123");

            // Обновление адаптера
            initAdapter();
        });

        // Инициализация кнопки возврата обратно
        ImageView goBack = (ImageView) findViewById(R.id.go_back_button);
        goBack.setOnClickListener(v -> {
            // Переход обратно к активности выбора языка
            Intent intent = new Intent(getApplicationContext(), LangChooserActivity.class);
            intent.putExtra("watched", watched);
            intent.putExtra("flag", flag);
            intent.putExtra("login", login);
            startActivity(intent);
        });

        // Инициализация адаптера
        initAdapter();
    }

    @Override
    public void onBackPressed() {
        // Определение поведения при нажатии на кнопку "Назад" устройства
        super.onBackPressed();

        // Переход обратно к активности выбора языка
        Intent intent = new Intent(getApplicationContext(), LangChooserActivity.class);
        intent.putExtra("watched", watched);
        intent.putExtra("flag", flag);
        intent.putExtra("login", login);
        startActivity(intent);
    }

    void initAdapter() {
        // Инициализация RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создание новой таблицы для слов в базе данных
        db.createNewTableDict(lang + "_" + login);

        // Получение слов и их переводов из базы данных
        words = db.getWords(lang + "_" + login).stream().limit(count).collect(Collectors.toList());
        translations = db.getTranslations(lang + "_" + login).stream().limit(count).collect(Collectors.toList());

        // Инициализация и установка адаптера для RecyclerView
        adapter = new Adapter(this, words, translations, this);
        recyclerView.setAdapter(adapter);
    }
}