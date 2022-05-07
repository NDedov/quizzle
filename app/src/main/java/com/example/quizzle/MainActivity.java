package com.example.quizzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class answer {
    String text;
    boolean correct;
    double percent;

    public answer(String text, boolean correct, double percent) {
        this.text = text;
        this.correct = correct;
        this.percent = percent;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return correct;
    }
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView questionView, descriptionView;
    String[] answers = {"Бен Стиллер","Джим Керри", "Адам Сэндлер", "Эдди Мерфи"};
    ArrayList<answer> answersList;
    String questionText = "Актёр, номинированный на антиприз «Золотая малина» в категории " +
            "«худший актёр» за все пять фильмов, выпущенные им в течение одного года.";
    String descriptionText = "В 2005 году Стиллер стал героем антипремии «Золотая малина», традиционно вручаемой за «сомнительные» заслуги в области кинематографа накануне оскаровской церемонии. Американца номинировали в категории «Худшая мужская роль» сразу за пять ролей в фильмах: «А вот и Полли», «Вышибалы», «Старски и Хатч», «Телеведущий» и «Черная зависть». Правда, в категории он занял лишь один слот из пяти — награда тогда досталась Джорджу Бушу за участие в документальном фильме «Фаренгейт 9/11».\n" +
            "\n" +
            "Уже тогда «Золотая малина» была премией, держащейся только на хайпе — какая-нибудь уникальная фишка была присуща церемонии каждый год. Пять номинаций Стиллера как раз и стали такой в 2005-м.\n" +
            "\n" +
            "Во второй раз Бен появился на радарах антипремии в 2017 году. «Образцовый самец 2» был представлен почти во всех категориях, три из которых напрямую касались Стиллера: «Худшая мужская роль», «Худший режиссер» и «Худший экранный ансамбль». Однако и тогда «победу» пополам разделили «Бэтмен против Супермена» и политическая документальная лента «Америка Хиллари: Тайная история Демократической партии».";

    Button button1, button2, button3, button4;

    CustomScrollView customScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.question4);
        questionView = findViewById(R.id.questionView);
        descriptionView = findViewById(R.id.textDescription);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        customScrollView = findViewById(R.id.nestedScrollView);
        customScrollView.setEnableScrolling(false);



        questionView.setText(questionText);
        descriptionView.setText(descriptionText);

        answersList = new ArrayList<>();

        for (int i = 0; i < answers.length; i++) {
            if (i==0){
                answersList.add(new answer(answers[i],true,0.25));
            }

            else{
                answersList.add(new answer(answers[i],false,0.25));
            }

        }
        Collections.shuffle(answersList);

        button1.setText(answersList.get(0).getText());
        button2.setText(answersList.get(1).getText());
        button3.setText(answersList.get(2).getText());
        button4.setText(answersList.get(3).getText());
        button1.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case (R.id.button1):
            descriptionView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Нажал 1", Toast.LENGTH_SHORT).show();
            customScrollView.smoothScrollTo(0,750, 700);
            customScrollView.setEnableScrolling(true);
            break;
    }
    }
}

