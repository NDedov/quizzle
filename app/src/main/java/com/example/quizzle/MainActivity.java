package com.example.quizzle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
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

    public double getPercent() {
        return percent;
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
    boolean answerDone; // ответ дан
    boolean answerCorrect; // ответ корректен или нет
    boolean hintDone; // воспользовался подсказкой
    Button button1, button2, button3, button4;
    LinearProgressIndicator progressBar1,progressBar2,progressBar3,progressBar4;
    LinearLayout linearLayoutDescription;
    float alphaButton = 0.3f;

    Button button50percent, button2chance, buttonPeople;
    View divider;

    boolean button1_visible = true, button2_visible = true, button3_visible = true, button4_visible = true; // для подсказок
    int hint50percent = 255;// количество подсказок
    int hint2chance = 267;
    int hintPeople = 56;

    CustomScrollView customScrollView; //скрол, который можно замораживать

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.question4);
        initViews();
        answerDone = false;
        hintDone = false;
        answerCorrect = false;
        answersList = new ArrayList<>();
        fillAnswers();
        initHints(true);
        setTexts();
        initClickers();
    }

    private void setTexts() { // заполнение вопросов и ответов
        questionView.setText(questionText);
        descriptionView.setText(descriptionText);
        button1.setText(answersList.get(0).getText());
        button2.setText(answersList.get(1).getText());
        button3.setText(answersList.get(2).getText());
        button4.setText(answersList.get(3).getText());

    }

    private void initViews() {
        questionView = findViewById(R.id.questionView);
        descriptionView = findViewById(R.id.textDescription);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar4 = findViewById(R.id.progressBar4);
        progressBar1.setVisibility(View.INVISIBLE);
        progressBar2.setVisibility(View.INVISIBLE);
        progressBar3.setVisibility(View.INVISIBLE);
        progressBar4.setVisibility(View.INVISIBLE);

        button50percent = findViewById(R.id.button50percent);
        button2chance = findViewById(R.id.button2chance);
        buttonPeople = findViewById(R.id.buttonPeople);
        customScrollView = findViewById(R.id.nestedScrollView);
        divider = findViewById(R.id.divider);
        linearLayoutDescription = findViewById(R.id.linearDescription);

        //customScrollView.setEnableScrolling(false);
        customScrollView.setEnableScrolling(true);

    }

    private void initClickers() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button50percent.setOnClickListener(this);
        button2chance.setOnClickListener(this);
        buttonPeople.setOnClickListener(this);
    }

    private void fillAnswers() {//заполнение коллекции ответов
        for (int i = 0; i < answers.length; i++) {
            if (i==0)
                answersList.add(new answer(answers[i],true,0.35));
            else
                answersList.add(new answer(answers[i],false,0.17));
        }
        Collections.shuffle(answersList);
    }

    private void initHints(boolean isActive) {//заполнение текста на подсказках
        String htmlTemp;
        if (!isActive){//красим иконки в серый
            button50percent.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_50percent_grey, 0, 0, 0);
            button2chance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dice_grey, 0, 0, 0);
            buttonPeople.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_peopletalk_v2_grey, 0, 0, 0);
        }

        if (hint50percent > 0) {
            htmlTemp = "<b>x" + hint50percent + "</b><br>" + "убрать" + "<br>" + "2 варианта";
            button50percent.setText(HtmlCompat.fromHtml(htmlTemp,0));
        }
        else
            button50percent.setText("Купить\nподсказку");

        if (hint2chance > 0) {
            htmlTemp = "<b>x" + hint2chance + "</b><br>" + "двойной" + "<br>" + "шанс";
            button2chance.setText(HtmlCompat.fromHtml(htmlTemp,0));
        }
        else
            button2chance.setText("Купить\nподсказку");

        if (hintPeople > 0) {
            htmlTemp = "<b>x" + hintPeople + "</b><br>" + "глас" + "<br>" + "народа";
            buttonPeople.setText(HtmlCompat.fromHtml(htmlTemp,0));
        }
        else
            buttonPeople.setText("Купить\nподсказку");
    }

    @Override
    public void onClick(View view) {
        if (!answerDone) {
            switch (view.getId()){
                case (R.id.button1)://обработка нажатий кнопок с ответами
                    paintButton(0,button1);
                    enableDescription();
                    answerDone = true;
                    break;
                case (R.id.button2):
                    paintButton(1,button2);
                    answerDone = true;
                    enableDescription();

                    break;
                case (R.id.button3):
                    paintButton(2,button3);
                    answerDone = true;
                    enableDescription();
                    break;
                case (R.id.button4):
                    paintButton(3,button4);
                    answerDone = true;
                    enableDescription();
                    break;

            }
            if (!hintDone){// обработка нажатий кнопок с подсказками
                switch (view.getId()){
                    case (R.id.button50percent):
                        if (hint50percent > 0){
                            percent50on50();
                            hintDone = true;
                            hint50percent--;
                            initHints(false);
                        }
                        else
                            buyHints();

                        break;
                    case (R.id.buttonPeople):
                        if (hintPeople > 0){
                            peopleTalk();
                            hintDone = true;
                            hintPeople--;
                            initHints(false);
                        }
                        else
                            buyHints();
                        break;
                    case (R.id.button2chance):
                        if (hint2chance > 0){
                            doubleChance();
                            hintDone = true;
                            hint2chance--;
                            initHints(false);
                        }
                        else
                            buyHints();
                        break;
                }

            }

        }
    }

    private void doubleChance() {
        //todo подсказку двойной шанс
    }

    private void buyHints() {
        //todo покупка подсказок/магазин
    }

    private void peopleTalk() {
        progressBar1.setMax(100);
        progressBar2.setMax(100);
        progressBar3.setMax(100);
        progressBar4.setMax(100);
        float al = 0.8f;
        button1.setAlpha(al);
        button2.setAlpha(al);
        button3.setAlpha(al);
        button4.setAlpha(al);


        for (int i = 0; i < answersList.size(); i++) {
            double percent = 100 * answersList.get(i).getPercent();
            switch (i){
                case (0):
                    progressBar1.setProgress((int) percent);
                    break;
                case (1):
                    progressBar2.setProgress((int) percent);
                    break;
                case (2):
                    progressBar3.setProgress((int) percent);
                    break;
                case (3):
                    progressBar4.setProgress((int) percent);
                    break;

            }
        }
        progressBar1.setVisibility(View.VISIBLE);
        progressBar2.setVisibility(View.VISIBLE);
        progressBar3.setVisibility(View.VISIBLE);
        progressBar4.setVisibility(View.VISIBLE);
    }

    void percent50on50(){// делает 2 неверных ответа невидимыми
        Random rnd = new Random();
        int answersAmount = 4;

        while (answersAmount > 2){
            int rnd_int = rnd.nextInt(4);
            if (!answersList.get(rnd_int).isCorrect()){
                switch (rnd_int){
                    case (0):
                        if (button1_visible){
                            button1.setAlpha(alphaButton);
                            //button1.setOnClickListener(null);
                            button1.setEnabled(false);
                            //button1.setVisibility(View.INVISIBLE);
                            button1_visible = false;
                            answersAmount--;
                        }
                        break;
                    case (1):
                        if (button2_visible){
                            button2.setAlpha(alphaButton);
                            button2.setEnabled(false);
                            //button2.setOnClickListener(null);
                            //button2.setVisibility(View.INVISIBLE);
                            button2_visible = false;
                            answersAmount--;
                        }
                        break;
                    case (2):
                        if (button3_visible){
                            button3.setAlpha(alphaButton);
                            //button3.setOnClickListener(null);
                            button3.setEnabled(false);
                            //button3.setVisibility(View.INVISIBLE);
                            button3_visible = false;
                            answersAmount--;
                        }
                        break;
                    case (3):
                        if (button4_visible){
                            button4.setAlpha(alphaButton);
                            //button4.setOnClickListener(null);
                            button4.setEnabled(false);
                            //button4.setVisibility(View.INVISIBLE);
                            button4_visible = false;
                            answersAmount--;
                        }
                        break;
                }
            }
        }
    }

    void paintButton(int num, Button button){//раскрашивает кнопки ответов в зависимости
        // от корректности ответа и проставлят флаг корректности ответа
        if (answersList.get(num).isCorrect()){
            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.correct_answer));
            answerCorrect = true;
        }
        else {
            button.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.wrong_answer));
            answerCorrect = false;
            for (int i = 0; i < answersList.size(); i++) {
                if (answersList.get(i).isCorrect()){
                    switch (i){
                        case (0):
                            button1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.correct_answer));
                            break;
                        case (1):
                            button2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.correct_answer));
                            break;
                        case (2):
                            button3.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.correct_answer));
                            break;
                        case (3):
                            button4.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.correct_answer));
                            break;
                    }
                }

            }


        }
    }

    void enableDescription()// делает описание видимым
    {
        linearLayoutDescription.setVisibility(View.VISIBLE);
        ViewTreeObserver vto = linearLayoutDescription.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {//выводим на
            //экран когда нарисовался Layout
            @Override
            public void onGlobalLayout() {
                linearLayoutDescription.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                customScrollView.smoothScrollTo(0,750, 700);
            }
        });
    }
}

