package com.epc.uscis.ui;

import java.util.Locale;

import com.epc.uscis.R;
import com.epc.uscis.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class AnswerActivity extends Activity implements OnClickListener, OnTouchListener, OnInitListener
{
    public static final String PREVIOUS_QUESTION = "Previous Question";
    
    private ScrollView scrollView;
    private TextView answer;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    
    private int currentIndex;
    private int currentQuestion;
    private int[] intArray;
    private int numberOfFlashCards;
   
    // Text to Speech objects
    private boolean isTextToSpeechEnabled;
    private TextToSpeech textToSpeech;
    private String answerTextString;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flash_card_answer_layout);
        
        answer = (TextView)findViewById(R.id.answer);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        
        scrollView.setOnTouchListener(this);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        
        Intent intent = getIntent();
        if(intent != null)
        {
            intArray = intent.getIntArrayExtra(QuestionActivity.FLASH_CARD_ARRAY);
            
            // This is the object that will store the text that will be
            // read out loud by the text to speech engine on the device
            answerTextString = intent.getStringExtra(QuestionActivity.ANSWER);
            
            answer.setText(Html.fromHtml(answerTextString));
            currentIndex = intent.getIntExtra(QuestionActivity.CURRENT_INDEX, -1);
            currentQuestion = intent.getIntExtra(QuestionActivity.CURRENT_QUESTION, -1);
            isTextToSpeechEnabled = intent.getBooleanExtra(QuestionActivity.TEXT_TO_SPEECH_ENABLED, false);
            numberOfFlashCards = intArray.length - 1;
            setTitle("US Civics Flash Cards - Answer to Question #" + currentQuestion);
        }
        
        // We now check to see if text to speech has been enabled 
        // by the user, if so then we will instantiate the appropriate
        // object to accomplish this.
        if(isTextToSpeechEnabled)
        {
            Intent checkTextToSpeechIntent = new Intent();
            checkTextToSpeechIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTextToSpeechIntent, QuestionActivity.TTS_CHECK_CODE);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == QuestionActivity.TTS_CHECK_CODE)
        {
            // If this phone has the Text to Speech feature installed
            // then we instantiate the TextToSpeech object.
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
            {
                textToSpeech = new TextToSpeech(this, this);
            }
            else
            {
                // The check for checking whether or not the Text to Speech feature
                // is currently installed on this device has failed.  We now need
                // to take the user to the Android market and allow the user
                // to install this feature.
                Intent installTTS = new Intent();
                installTTS.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTS);
            }
        }
    }

    @Override
    public void onInit(int status)
    {
        textToSpeech.setLanguage(Locale.US);
        textToSpeech.speak(Util.removeNumbersInParentheses(answerTextString), TextToSpeech.QUEUE_FLUSH, null);
    }
 
    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnNext:
                nextQuestion();
                break;
            case R.id.btnPrevious:
                previousQuestion();
                break;
        }
    }
   
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        // I am including the Action Down and Action
        // Up Motion Events to just use them to do
        // nothing in our case here.
        switch(event.getActionIndex())
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                tapAnswer();
                return true;
        }
        
        return false;
    }
    
    /**
     * This method just calls the previousQuestion method
     * so that the user can view the question that is 
     * associated to this question.
     */
    private void tapAnswer()
    {
        Intent viewAnswer = new Intent(AnswerActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        
        // Although this is wasteful, it's necessary so  that
        // the user can  move back and forwards in the list of
        // Flash Cards that they are viewing
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.CURRENT_QUESTION, currentQuestion);
        bundle.putInt(QuestionActivity.CURRENT_INDEX, currentIndex);
        viewAnswer.putExtras(bundle);
        
        startActivity(viewAnswer);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        
        // I am calling finish on this activity because I don't want
        // to accumulate a whole bunch of these QuesitonActivity
        // objects on the back stack. If the user wants to go
        // back to the question, I'll just instantiate a new 
        // QuestionActivity object instead.  This means that
        // the back button will be not be used to go back to
        // the previous QuestionActivity, the user will have 
        // to use the navigation buttons that will be provided
        // for them to navigate to the previous question instead
        finish();        
    }

    /**
     * This will allow the user to view the question for
     * this answer.  I will have to create a new 
     * QuestionActivity object since I am automatically
     * killing each QuestionActivity 
     */
    private void previousQuestion()
    {
        Intent question = new Intent(AnswerActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.NUMBER_OF_FLASH_CARDS, numberOfFlashCards);
        bundle.putBoolean(PREVIOUS_QUESTION, true); 
        bundle.putInt(QuestionActivity.CURRENT_INDEX, currentIndex);
        question.putExtras(bundle);
        startActivity(question);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }
    
    private void nextQuestion()
    {
        if(currentIndex == numberOfFlashCards)
        {
            // When we have reached the end of the array index
            // (i.e. 99) then we'll take the user back to the
            // home activity
           finish(); 
        } 
        else
        {
            Intent nextQuestion = new Intent(AnswerActivity.this, QuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
            currentIndex++;
            bundle.putInt(QuestionActivity.CURRENT_INDEX, currentIndex);
            nextQuestion.putExtras(bundle);
            startActivity(nextQuestion);
            overridePendingTransition(R.anim.fade, R.anim.hold);
            finish();
        }
    }
}
