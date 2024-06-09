package com.thatgame.langcards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String> data, data2;
    public FlashCardsActivity flashCardsActivity;

    Adapter(Context context, List<String> data, List<String> data2, FlashCardsActivity flashCardsActivity) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.data2 = data2;
        this.flashCardsActivity = flashCardsActivity;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание нового элемента списка (вьюхолдера)
        View view = layoutInflater.inflate(R.layout.custom_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // Привязка данных к вьюхолдеру
        String word = data.get(position);
        String translation = data2.get(position);
        holder.text.setSingleLine(false);
        // Если этот элемент текущий - показываем слово и кнопку "показать перевод", иначе - показываем перевод
        if (position == flashCardsActivity.count-1) {
            holder.text.setText(word);
            holder.btn.setText("Показать перевод");
        } else {
            holder.text.setText(translation);
            holder.btn.setVisibility(View.INVISIBLE);
        }
        holder.btn.setOnClickListener(v -> {
            if (holder.text.getText().toString().equals(data.get(position))) {
                holder.text.setText(translation);
                holder.btn.setText("Дальше");
                flashCardsActivity.watched++;
            } else {
                if (position == flashCardsActivity.count-1) {
                    flashCardsActivity.count++;
                    flashCardsActivity.initAdapter();
                }
            }
        });
        holder.delBtn.setOnClickListener(v -> {
            // Удаляем слово из базы данных и обновляем адаптер
            String table = flashCardsActivity.lang + "_" + flashCardsActivity.login;
            Log.i("mine", table);
            flashCardsActivity.db.deleteWord(table, word);
            flashCardsActivity.count--;
            flashCardsActivity.initAdapter();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        Button btn, delBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.editTextText);
            btn = itemView.findViewById(R.id.button3);
            delBtn = itemView.findViewById(R.id.deleteWordBtn);
        }
    }
}
