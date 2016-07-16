package com.zaksid.dev.android.training.bignerdranch.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    //TODO Add the ability to return by clicking arrow in AppBar

    public final static String EXTRA_ANSWER_IS_TRUE =
            "com.zaksid.dev.android.training.bignerdranch.geoquiz.answer_is_true";
    public final static String EXTRA_ANSWER_SHOWN =
            "com.zaksid.dev.android.training.bignerdranch.geoquiz.answer_shown";

    private final static String KEY_ANSWER_SHOWN = "isAnswerShown";
    private final static String KEY_SHOW_ANSWER_VISIBILITY = "showAnswer visibility";

    private boolean isAnswerTrue;
    private boolean isAnswerShown;

    private TextView answerTextView;
    private Button showAnswer;

    public static Intent newIntent(Context pageContext, boolean answerIsTrue) {
        Intent intent = new Intent(pageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);

        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        isAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        answerTextView = (TextView) findViewById(R.id.answer_text_view);
        showAnswer = (Button) findViewById(R.id.show_answer_button);

        if (savedInstanceState != null) {
            isAnswerShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            setAnswerShownResult(isAnswerShown);
            showAnswer.setVisibility(savedInstanceState.getInt(KEY_SHOW_ANSWER_VISIBILITY, View.VISIBLE));
        }

        if (answerTextView != null && isAnswerShown) {
            if (isAnswerTrue) {
                answerTextView.setText(R.string.true_button);
            } else {
                answerTextView.setText(R.string.false_button);
            }
        }

        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnswerTrue) {
                    answerTextView.setText(R.string.true_button);
                } else {
                    answerTextView.setText(R.string.false_button);
                }

                setAnswerShownResult(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = showAnswer.getWidth() / 2;
                    int cy = showAnswer.getHeight() / 2;
                    float radius = showAnswer.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(showAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            answerTextView.setVisibility(View.VISIBLE);
                            showAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    answerTextView.setVisibility(View.VISIBLE);
                    showAnswer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_ANSWER_SHOWN, isAnswerShown);
        outState.putInt(KEY_SHOW_ANSWER_VISIBILITY, showAnswer.getVisibility());
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
        this.isAnswerShown = isAnswerShown;
    }
}
