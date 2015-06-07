package com.epc.uscis.ui;

import java.util.Locale;

import com.epc.uscis.R;
import com.epc.uscis.db.QuestionHelper;
import com.epc.uscis.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity will be used to view the Question in a Flash 
 * Card.  The user will be able to tap on the question that 
 * will show up in this activity.  He/she will then be taken
 * to the answer of the question.
 */
public class QuestionActivity extends Activity implements OnClickListener, OnInitListener
{
    // Menu items for the options menu
    private final static int MENU_RANDOM = Menu.FIRST + 1;
    private final static int MENU_SEQUENTIAL = Menu.FIRST + 2;
    private final static int MENU_TEN_RANDOM = Menu.FIRST + 3;
    private final static int MENU_FILTER_BY_CATEGORY = Menu.FIRST + 4;
    private final static int MENU_FLAG_QUESTION = Menu.FIRST + 5;
    private final static int MENU_VIEW_FLAGGED_QUESTIONS = Menu.FIRST + 6;
    private final static int MENU_VIEW_PREFERNECES = Menu.FIRST + 7;
    
    /** Allows users to Choose to view Questions that belong to a particular Category **/
    private static final int DIALOG_CHOOSE_CATEGORY = 0;

    // These will be used to tell whether or not we are in a
    // Flagged Question Mode or not
    public final static String FLAGGED_QUESTIONS = "Flagged Questions";
    public final static int FLAGGED_QUESTIONS_MODE = 1;
    
    private final static String MENU_RANDOM_TEXT = "Randomize Cards";
    private final static String MENU_TEN_RANDOM_TEXT = "10 Random Cards";
    private final static String MENU_FILTER_BY_CATEGORY_TEXT = "Filter By Category";
    private final static String MENU_FLAG_QUESTION_TEXT = "Flag Question";
    private final static String MENU_VIEW_FLAGGED_QUESTIONS_TEXT = "View Flagged Questions";
    private final static String MENU_VIEW_PREFERENCES_TEXT = "Preferences";
    
    private TextView questionText;
    private TextView questionNumber;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    
    private QuestionHelper questionHelper;
    private Cursor cursor;
    
    /** This will hold the answer that will be fetched from the database **/
    private String answer;
    
    public static final String ANSWER = "Answer";
    public static final String FLASH_CARD_ARRAY = "Flash Card Array";
    public static final String CURRENT_INDEX = "Current Index";
    public static final String CURRENT_QUESTION = "Current Question";
    public static final String NEXT_QUESTION = "Next Question";
    public static final String NUMBER_OF_FLASH_CARDS = "Number of Flash Cards";

    /** 
     * This is used to test to see is a valid value has been
     * passed or not.  If a bad value (or not value) has been
     * passed, then it'll be equal to this (-1).
     */
    public static final int BAD_INDEX = -1;
   
    /**
     * This will always point to the beginning of the int array
     * that holds the question numbers.
     */
    public static final int FIRST_QUESTION_INDEX = 0;
    
    /** The current index that will be used to display the question **/
    private int currentIndex;
    
    /** This is the question number **/
    private int currentQuestion;
    
    /** This holds the index of the last item in the array **/
    private int numberOfFlashCards;
    
    /** The integer array which holds the question number **/
    private int[] intArray;
    
    private static final String FIRST_TIME_FLASH_CARDS = "flashCardFirstTime";
    private SharedPreferences preferences;
   
    // Text to Speech Objects
    private String questionTextString; // this will be used by the text to speech engine
    private boolean textToSpeechEnabled;
    public static int TTS_CHECK_CODE = 1;
    public static final String TEXT_TO_SPEECH_ENABLED = "text_to_speech_enabled";
    private TextToSpeech textToSpeech;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_question_layout);
        
        questionHelper = new QuestionHelper(this);
       
        questionNumber = (TextView)findViewById(R.id.questionNumber);
        questionText = (TextView)findViewById(R.id.question);
        questionText.setOnClickListener(this);
        
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnPrevious = (ImageButton)findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
        
        loadActivity();
       
        // I am using the PrefernceManager because without this I will not
        // be able to get access to the preferences set in the AppPreferences
        // activity. This should also have been used anyway as it's the more
        // clean way of accessing preferences for the application.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstTime = preferences.getBoolean(FIRST_TIME_FLASH_CARDS, true);
        
        if(firstTime)
        {
            showHelpDialogBox();
        }
        
        // By default, when you first run the application this will be set to 
        // false which is what we want as the default setting for this feature
        // should be that it should be disabled.  After the application has been
        // Initialized for the first time the user then has the option to enable
        // or disable this.
        textToSpeechEnabled = preferences.getBoolean(AppPreferences.TEXT_TO_SPEECH, false);
        
        if(textToSpeechEnabled)
        {
            Intent checkTextToSpeechIntent = new Intent();
            checkTextToSpeechIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkTextToSpeechIntent, TTS_CHECK_CODE);
        }
    }
   
    /**
     * This function is currently being used to test to see if the Text
     * to Speech component is installed on the device. If it's not, then
     * it'll get installed by having the users go to the Android Market
     * and have this installed.  If the Text to Speech component is 
     * installed then we set the text to be read out loud by the Text to
     * Speech component to the question text.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TTS_CHECK_CODE)
        {
            // If this phone has the Text to Speech functunality installed
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
   
    /**
     * This method will be used to initialize the Text to Speech object
     * so that we can use it in this application.
     */
    @Override
    public void onInit(int status)
    {
        textToSpeech.setLanguage(Locale.ENGLISH);
        textToSpeech.speak(Util.stripOutHtml(questionTextString), TextToSpeech.QUEUE_FLUSH, null);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, MENU_RANDOM, 0, MENU_RANDOM_TEXT).setIcon(android.R.drawable.ic_menu_rotate);
        menu.add(1, MENU_TEN_RANDOM, 0, MENU_TEN_RANDOM_TEXT).setIcon(R.drawable.ic_menu_database);
        menu.add(1, MENU_FILTER_BY_CATEGORY, 1, MENU_FILTER_BY_CATEGORY_TEXT).setIcon(R.drawable.ic_menu_filter);
        menu.add(1, MENU_FLAG_QUESTION, 1, MENU_FLAG_QUESTION_TEXT).setIcon(R.drawable.ic_menu_flag);
        menu.add(1, MENU_VIEW_FLAGGED_QUESTIONS, 1, MENU_VIEW_FLAGGED_QUESTIONS_TEXT).setIcon(android.R.drawable.ic_menu_myplaces);
        menu.add(1, MENU_VIEW_PREFERNECES, 1, MENU_VIEW_PREFERENCES_TEXT).setIcon(android.R.drawable.ic_menu_preferences);
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case MENU_RANDOM:
                randomizeAllQuestions();
                return true;
            case MENU_SEQUENTIAL:
                sequentialCardOrder();
                return true;
            case MENU_TEN_RANDOM:
                tenRandomQuestions();
                return true;
            case MENU_FILTER_BY_CATEGORY:
                filterByCategoryName();
                return true;
            case MENU_FLAG_QUESTION:
                flagQuestion();
                return true;
            case MENU_VIEW_FLAGGED_QUESTIONS:
                viewFlaggedQuestions();
                return true;
            case MENU_VIEW_PREFERNECES:
                viewPreferences();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = null;
        
        switch(id)
        {
            case DIALOG_CHOOSE_CATEGORY:
                loadCategoryDialog();
                break;
            default:
                // nothing done here (for now)
        }
        
        return dialog;
    }
    
    private void loadCategoryDialog()
    {
        // I am adding these here because I don't want to pay the (small)
        // price of querying the database for these.  I am also assuming that
        // the values for the category table will never change.
        final CharSequence[] items = {"American Government", "American History", "Integrated Civics"}; 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter Questions by Category");
        builder.setItems(items, new DialogInterface.OnClickListener()
           {
               @Override
               public void onClick(DialogInterface dialog, int categoryId)
               {
                   // Need to increment the value of the category
                   // id because of the zero-based indexing on the
                   // CharSequence array from above.
                   categoryId++;
                   getQuestionsByCategoryId(categoryId);
               }
           });
       
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void loadActivity()
    {
        Intent intent = getIntent();
        intArray = intent.getIntArrayExtra(FLASH_CARD_ARRAY);
        currentIndex = intent.getIntExtra(CURRENT_INDEX, BAD_INDEX);
        numberOfFlashCards = intArray.length - 1;
       
        // For sanity's sake, let's check to see that we have not
        // gotten a bad index (i.e. < 1 or > 100).  Also check to make
        // sure that we have a valid number of flash cards (i.e. non-negative)
        if(currentIndex == BAD_INDEX || numberOfFlashCards == BAD_INDEX)
        {
            Toast.makeText(this, "Bad Current Question Index: " + currentIndex, Toast.LENGTH_SHORT).show();
            return;
        }
        
        // This is the first time we have arrived here, i.e.
        // the user has hit the flash cards button on the 
        // HomeActivity Object.
        int cardNumber = currentIndex;
        setTitle("US Civics Flash Cards (" + ++cardNumber + "/" + intArray.length + ")");
        currentQuestion = intArray[currentIndex];
        
        cursor = questionHelper.getQuestionById(currentQuestion);
        cursor.moveToFirst();
        questionNumber.setText(questionHelper.getQuestionNumber(cursor));
      
        // We need to retrieve the text that will be used by the 
        // Text to Speech object.
        questionTextString = questionHelper.getQuestion(cursor);
        
        questionText.setText(Html.fromHtml(questionHelper.getQuestion(cursor)));
        answer = questionHelper.getAnswer(cursor);
        
        cursor.close();
        //return;
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        questionHelper.close();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.question:
                tapOnQuestion();
                break;
            case R.id.btnPrevious:
                previousQuestion();
                break;
            case R.id.btnNext:
                nextQuestion();
                break;
            default:
                break;
        }
    }
    
    /**
     * When a user clicks on the question, they will be 
     * directed to a AnswerActivity object with the answer
     * to the question.
     */
    private void tapOnQuestion()
    {
        Intent viewAnswer = new Intent(QuestionActivity.this, AnswerActivity.class);
        Bundle bundle = new Bundle();
        
        // Although this is wasteful, it's necessary so  that
        // the user can  move back and forwards in the list of
        // Flash Cards that they are viewing
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(CURRENT_QUESTION, currentQuestion);
        bundle.putInt(CURRENT_INDEX, currentIndex);
        bundle.putBoolean(TEXT_TO_SPEECH_ENABLED, textToSpeechEnabled);
        bundle.putString(ANSWER, answer);
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
    
    private void previousQuestion()
    {
        // Have we reached the beginning of the array? If so
        // then the user is taken back to the home activity
        if(currentIndex == FIRST_QUESTION_INDEX)
        {
            finish();
        }
        else
        {
            Intent previousQuestion = new Intent(QuestionActivity.this, QuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
            currentIndex--;
            bundle.putInt(CURRENT_INDEX, currentIndex);
            bundle.putBoolean(AnswerActivity.PREVIOUS_QUESTION, true);
            previousQuestion.putExtras(bundle);
            startActivity(previousQuestion);
            overridePendingTransition(R.anim.fade, R.anim.hold);
            finish();
        }
    }
    
    private void nextQuestion()
    {
        // Have we reached the end of the index? If so then
        // the user will be taken back to the home activity
        if(currentIndex == numberOfFlashCards)
        {
           finish(); 
        }
        else
        {
            Intent nextQuestion = new Intent(QuestionActivity.this, QuestionActivity.class);
            Bundle bundle = new Bundle();
            bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
            bundle.putInt(QuestionActivity.NUMBER_OF_FLASH_CARDS, numberOfFlashCards);
            currentIndex++;
            bundle.putInt(CURRENT_INDEX, currentIndex);
            nextQuestion.putExtras(bundle);
            startActivity(nextQuestion);
            overridePendingTransition(R.anim.fade, R.anim.hold);
            finish();
        }
    }
    
    private void getQuestionsByCategoryId(int categoryId)
    {
        Intent flashCardActivity = new Intent(QuestionActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        intArray = questionHelper.questionsArrayByCategory(categoryId);
        numberOfFlashCards = intArray.length - 1;
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
        flashCardActivity.putExtras(bundle);
        startActivity(flashCardActivity);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }

    /**
     * Randomizes all questions.  This method also begins the set of
     * Randomly Generated Flash Cards from the beginning (i.e. begins
     * at index 0).
     */
    private void randomizeAllQuestions()
    {
        Intent flashCardActivity = new Intent(QuestionActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        intArray = Util.randomIntArray();
        numberOfFlashCards = intArray.length - 1;
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
        flashCardActivity.putExtras(bundle);
        startActivity(flashCardActivity);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }
    
    private void sequentialCardOrder()
    {
        Intent flashCardActivity = new Intent(QuestionActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        intArray = Util.sequentialIntArray();
        numberOfFlashCards = intArray.length - 1;
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
        flashCardActivity.putExtras(bundle);
        startActivity(flashCardActivity);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }
    
    private void tenRandomQuestions()
    {
        Intent flashCardActivity = new Intent(QuestionActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        intArray = questionHelper.randomIntArray(10);
        numberOfFlashCards = intArray.length - 1;
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
        flashCardActivity.putExtras(bundle);
        startActivity(flashCardActivity);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }

    /**
     * This will allow the user to view the Questions based on
     * a specific Category chosen by the user.
     */
    private void filterByCategoryName()
    {
        showDialog(DIALOG_CHOOSE_CATEGORY);
    }
    
    private void flagQuestion()
    {
        if(questionHelper.isFlaggedQuestion(currentQuestion))
        {
            Toast.makeText(this, "Question " + currentQuestion + " is already flagged!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            questionHelper.insertFlaggedQuestion(currentQuestion);
            Toast.makeText(this, "Question " + currentQuestion + " has been flagged!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void viewFlaggedQuestions()
    {
        Intent flashCardActivity = new Intent(QuestionActivity.this, QuestionActivity.class);
        Bundle bundle = new Bundle();
        
        // If there are no cards currently flagged, then we just
        // show a brief message and then go back to the activity.
        if(questionHelper.getAllFlaggedQuestions().getCount() == 0)
        {
           Toast.makeText(this, "There are Currently No Flagged Questions!", Toast.LENGTH_SHORT).show(); 
           return;
        }
        else
        {
            intArray = questionHelper.getAllFlaggedQuestionsIntArray();
        }
        
        numberOfFlashCards = intArray.length - 1;
        bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
        bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
        flashCardActivity.putExtras(bundle);
        startActivity(flashCardActivity);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        finish();
    }
    
    private void viewPreferences()
    {
        Intent appPreferences = new Intent(QuestionActivity.this, AppPreferences.class);
        startActivity(appPreferences);
    }
   
    /**
     * This method will show a small dialog box with a message
     * on how to navigate thru each question on this activity.
     * This only runs the first time that the activity is launched
     * afterwards the user won't see this. This can come back only 
     * if the user decides to remove all data.
     */
    private void showHelpDialogBox()
    {
        new AlertDialog.Builder(QuestionActivity.this)
                                .setIcon(R.drawable.alert_dialog_icon)
                                .setTitle("Quick Tips")
                                .setMessage(R.string.flash_card_help_prompt)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean(FIRST_TIME_FLASH_CARDS, false);
                                        editor.commit();
                                    }
                                })
                                .create().show();
    }

}
