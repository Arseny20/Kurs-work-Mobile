package com.thatgame.langcards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class StatsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Создаем строителя диалога
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Получаем объект службы раздувания для преобразования XML-разметки во вьюшку
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        // Создаем вьюшку для диалога
        View dialogView = layoutInflater.inflate(R.layout.stats, null);

        // Находим в вьюшке виджеты - текстовое поле и кнопку
        final TextView textView = (TextView) dialogView.findViewById(R.id.textView);
        Button btnOk = dialogView.findViewById(R.id.button);

        // Получаем доступ к активности-владельцу этого диалога
        LangChooserActivity langChooserActivity = (LangChooserActivity) getActivity();

        // Проверяем, зарегистрирован ли пользователь
        if (langChooserActivity.flag == 1) {
            // Если зарегистрирован, то показываем статистику по количеству просмотренных карточек
            textView.setSingleLine(false);
            textView.setText("Вы просмотрели " + langChooserActivity.watched + "\nкарточек за эту ходку");
        } else {
            // Если не зарегистрирован, то показываем сообщение о том, что статистика доступна только для зарегистрированных
            textView.setSingleLine(false);
            textView.setText("Статистика доступна только\nзарегестрированным пользователям");
        }

        // При нажатии на кнопку - закрываем диалог
        btnOk.setOnClickListener(v -> dismiss());

        // Добавляем вьюшку к диалогу и устанавливаем заголовок
        builder.setView(dialogView).setMessage("Статистика");

        // Создаем диалог и возвращаем его
        return builder.create();
    }
}
