package com.kellyfj.android.geoquiz;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends Activity {
	private static final String TAG = "QuizActivity";
	private static final String KEY_INDEX = "index";
	private Button trueButton;
	private Button falseButton;
	private Button nextButton;
	private Button prevButton;
	private TextView questionTextView;
	
	private TrueFalse[] questionBank = new TrueFalse[] {
			new TrueFalse(R.string.question_oceans,true),
			new TrueFalse(R.string.question_mideast, false),
			new TrueFalse(R.string.question_africa,false),
			new TrueFalse(R.string.question_americas,true),
			new TrueFalse(R.string.question_asia, true)
	};
	
	private int currentIndex=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"onCreate(Bundle) called");
		setContentView(R.layout.activity_quiz);
		
		if(savedInstanceState != null) {
			currentIndex = savedInstanceState.getInt(KEY_INDEX,0);
		}
		
		questionTextView = (TextView) findViewById(R.id.question_text_view);
		questionTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentIndex = (currentIndex+1) % questionBank.length;
				updateQuestion();
				
			}
		});
		updateQuestion();
		
		trueButton = (Button) findViewById(R.id.true_button);
		trueButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(true);
			}
		});
		
		falseButton = (Button) findViewById(R.id.false_button);
		falseButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				checkAnswer(false);
			}
		});
		
		nextButton = (Button) findViewById(R.id.next_button);
		nextButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentIndex = (currentIndex+1) % questionBank.length;
				updateQuestion();
			}
		});
		
		prevButton = (Button) findViewById(R.id.prev_button);
		prevButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentIndex = (currentIndex - 1);
				if (currentIndex < 0)
					currentIndex = questionBank.length - 1;
				updateQuestion();
			}
		});
	}

	private void updateQuestion() {
		int question = questionBank[currentIndex].getQuestion();
		questionTextView.setText(question);
	}
	
	private void checkAnswer(boolean userPressedTrue) {
		boolean isAnswerTrue = questionBank[currentIndex].isTrueQuestion();
		int messageResId=0;
		
		if(userPressedTrue == isAnswerTrue) {
			messageResId= R.string.correct_toast;
		} else {
			messageResId = R.string.incorrect_toast;
		}
		
		Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG,"onStart() called");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG,"onPause() called");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG,"onResume() called");
	}
	
	
	@Override
	public void onStop() {
		super.onStop();
		Log.d(TAG,"onStop() called");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG,"onDestroy() called");
	}
	
	@Override 
	public void onSaveInstanceState(Bundle b) {
		super.onSaveInstanceState(b);
		Log.i(TAG,"onSaveInstanceState()");
		b.putInt(KEY_INDEX, currentIndex);
	}
}
