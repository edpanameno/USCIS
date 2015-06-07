package com.epc.uscis.ui;

import com.epc.uscis.R;
import com.epc.uscis.db.QuestionHelper;
import com.epc.uscis.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class HomeActivity extends Activity implements OnClickListener
{
    private Button btnBrowseQuestions;
    private Button btnFlashCards;
    private Button btnFlaggedQuestions;
    private Button btnAppPreferences;
    private Button btnHelp;
    
    /** The int array that will hold the question id's **/
    private int[] intArray;
    
    private QuestionHelper questionHelper;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        btnBrowseQuestions = (Button)findViewById(R.id.btnBrowseQuestions);
        btnBrowseQuestions.setOnClickListener(this);
        
        btnFlashCards = (Button)findViewById(R.id.btnFlashCards);
        btnFlashCards.setOnClickListener(this);
        
        btnFlaggedQuestions = (Button)findViewById(R.id.btnFlaggedQuestions);
        btnFlaggedQuestions.setOnClickListener(this);
        
        btnAppPreferences = (Button)findViewById(R.id.btnAppPreferences);
        btnAppPreferences.setOnClickListener(this);
        
        btnHelp = (Button)findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(this);
        
        questionHelper = new QuestionHelper(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnBrowseQuestions:
                browseQuestions();
                break;
            case R.id.btnFlashCards:
                flashCards();
                break;
            case R.id.btnFlaggedQuestions:
                flaggedQuestions();
                break;
            case R.id.btnAppPreferences:
                showPreferences();
                break;
            case R.id.btnHelp:
                helpActivity();
                break;
        }
    }
    
    private void browseQuestions()
    {
        Intent browseAllQuestions = new Intent(HomeActivity.this, BrowseQuestionsActivity.class);
        startActivity(browseAllQuestions);
    }
    
    private void flashCards()
    {
        Intent flashCardActivity = new Intent(HomeActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        intArray = Util.sequentialIntArray();
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
        flashCardActivity.putExtras(bundle);
        startActivity(flashCardActivity);
    }
    
    private void flaggedQuestions()
    {
        intArray = questionHelper.getAllFlaggedQuestionsIntArray();
        
        // Check to see if we have any flagged questions
        // if not, then we re-direct the user to the 
        // NoFlaggedQuestionsActivity
        if(intArray.length == 0)
        {
            Intent noFlaggedQuestionsActivity = new Intent(HomeActivity.this, NoFlaggedQuestionsActivity.class);
            startActivity(noFlaggedQuestionsActivity);
        }
        else
        {
            Intent flashCardActivity = new Intent(HomeActivity.this, BrowseQuestionsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        
            // This is used to tell that we are viewing the flagged questions
            bundle.putInt(QuestionActivity.FLAGGED_QUESTIONS, QuestionActivity.FLAGGED_QUESTIONS_MODE);
            bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
            flashCardActivity.putExtras(bundle);
            startActivity(flashCardActivity);
        }
        
    }
    
    private void showPreferences()
    {
        Intent appPreferences = new Intent(HomeActivity.this, AppPreferences.class);
        startActivity(appPreferences);
    }
    
    private void helpActivity()
    {
        Intent helpActivity = new Intent(HomeActivity.this, HelpActivity.class);
        startActivity(helpActivity);
    }
}
