package com.epc.uscis.ui;

import com.epc.uscis.R;
import com.epc.uscis.adapters.QuestionAdapter;
import com.epc.uscis.db.QuestionHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is the main Activity that will allow users
 * to browse all 100 Civic Questions.  This is 
 * inspired by the Tweets time line feature of the
 * Twitter application I am currently using on
 * my Android phone.
 */
public class BrowseQuestionsActivity extends ListActivity 
{
    public final static int MENU_RANDOM = Menu.FIRST + 1;
    public final static int MENU_TEN_QUESTIONS = Menu.FIRST + 2;
    public final static int MENU_VIEW_FLAGGED_QUESTIONS = Menu.FIRST + 3;
    public final static int MENU_CLEAR_FLAGGED_QUESTIONS = Menu.FIRST + 4;
    public final static int MENU_VIEW_CATEGORY = Menu.FIRST + 5;
    public final static int MENU_VIEW_PREFERENCES = Menu.FIRST + 6;
    
    private final static String MENU_RANDOM_TEXT = "Randomize All";
    private final static String MENU_TEN_RANDOM_TEXT = "10 Random";
    private final static String MENU_VIEW_FLAGGED_QUESTIONS_TEXT = "View Flagged";
    private final static String MENU_CLEAR_FLAGGED_QUESTIONS_TEXT = "Clear Flagged";
    private final static String MENU_VIEW_CATEGORY_TEXT = "Categories";
    private final static String MENU_VIEW_PREFERENCES_TEXT = "Preferences";

    /** Used to allow the user to choose the category they wish to view questions for **/
    private static final int DIALOG_CHOOSE_CATEGORY = 0;
    
    /** List of Friendly Categories to display to the user. **/
    private final CharSequence[] items = {"American Government", "American History", "Integrated Civics"}; 
   
    /** Used to show the flagged questions **/
    private final int VIEW_FLAGGED_QUESTIONS = -1;
   
    private Cursor cursor = null;
    private QuestionHelper questionHelper = null;
    private QuestionAdapter adapter = null;

    private final static String BROWSE_FIRST_TIME = "browseFirstTime";
    private SharedPreferences preferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_questions);
        
        questionHelper = new QuestionHelper(this);
        initQuestionList();
      
        // Just like in the QuestionActivity, I am using the PreferenceManager 
        // to get the default shared preferences for this application.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        // The first time that the user starts up the application, he/she
        // will get a prompt telling them how to use this part of the application.
        // This will only show up the first time that the user starts up the 
        // application.
        boolean firstTime = preferences.getBoolean(BROWSE_FIRST_TIME, true);
        if(firstTime)
        {
            showHelpDialogBox();
        }
        
        // This is used for registering the context menu
        // on the list of questions that is being shown
        // to the user.
        registerForContextMenu(getListView());
    }
   
    /**
     * This is just a place holder so that when you rotate the 
     * activity it will retain the random questions that the 
     * user has decided to display. This addressed Bug # 28
     * and it's finally resolved! The basic gitz about 
     * this is that I am defining my own implementation 
     * when the phone is rotated.  As stated above, I am 
     * just not doing anything here.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        // nothing being done with this, it's just a place holder
        // I had to add the android:configChanges="orientation" 
        // attribute to this activity's entry in the manifest 
        // file.
        super.onConfigurationChanged(newConfig);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        questionHelper.close();
    }
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, MENU_RANDOM, 0, MENU_RANDOM_TEXT).setIcon(android.R.drawable.ic_menu_rotate);
        menu.add(0, MENU_TEN_QUESTIONS, 0, MENU_TEN_RANDOM_TEXT).setIcon(R.drawable.ic_menu_database);
        menu.add(0, MENU_VIEW_CATEGORY, 0, MENU_VIEW_CATEGORY_TEXT).setIcon(R.drawable.ic_menu_filter);
        menu.add(0, MENU_VIEW_FLAGGED_QUESTIONS, 0, MENU_VIEW_FLAGGED_QUESTIONS_TEXT).setIcon(android.R.drawable.ic_menu_myplaces);
        menu.add(0, MENU_VIEW_PREFERENCES, 0, MENU_VIEW_PREFERENCES_TEXT).setIcon(android.R.drawable.ic_menu_preferences);
        menu.add(0, MENU_CLEAR_FLAGGED_QUESTIONS, 0, MENU_CLEAR_FLAGGED_QUESTIONS_TEXT).setIcon(android.R.drawable.ic_menu_delete);
        
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
            case MENU_TEN_QUESTIONS:
                getTenRandomQuestions();
                return true;
            case MENU_VIEW_FLAGGED_QUESTIONS:
                getFlaggedQuestions();
                return true;
            case MENU_CLEAR_FLAGGED_QUESTIONS:
                clearFlaggedQuestions();
                return true;
            case MENU_VIEW_CATEGORY:
                viewByCategory();
                return true;
            case MENU_VIEW_PREFERENCES:
                viewPreferences();
                return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
        final long questionId = info.id;
       
        // Depending on whether or not the question is already
        // flagged or not, the user will see a different message
        // when they long hold a question.
        String promptMessage = "";
        
        // setting this to final so that it can be accessed by the 
        // onClick Method below
        final boolean isQuestionFlagged = questionHelper.isFlaggedQuestion((int)questionId);
        if(isQuestionFlagged)
        {
            promptMessage = "Un-Flagged Question ";
        }
        else
        {
            promptMessage = "Flag Question ";
        }
      
        // Now we will build the dialog box to show the user an option
        // that will allow them to flag a question or un-flag any current
        // question that has already been flagged previously.
        new AlertDialog.Builder(BrowseQuestionsActivity.this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setMessage(promptMessage + info.id + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        if(isQuestionFlagged)
                        {
                           removeFlaggedQuestion(questionId); 
                        }
                        else 
                        {
                            flagQuestion(questionId);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Because of the new logic that we have above
                        // when the user hit's the No button, then nothing
                        // should happen.  Therefore this method will remain
                        // blank.
                    }
                }).create().show();
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        // here we must call a method to flag this question
        // to make sure that the user knows that the question
        // is either already flagged, or if not then that
        // the question has been successfully flagged
        
        return super.onContextItemSelected(item);
    }
    
    /**
     * When a question is clicked, the user will be shown the
     * answer.  If the user clicks on the question after that
     * then it will hide the answer. 
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        TextView question= (TextView)v.findViewById(R.id.question);
        TextView answer = (TextView)v.findViewById(R.id.answer);
        
        if(answer.getVisibility() == TextView.GONE)
        {
            question.setMinHeight(30);
            answer.setVisibility(TextView.VISIBLE);
        }
        else
        {
            answer.setVisibility(TextView.GONE);
        }
    }
    
    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = null;
        
        switch(id)
        {
            case DIALOG_CHOOSE_CATEGORY:
               showCategoriesDialog(); 
               break;
        }
        
        return dialog;
    }
    
    private void showCategoriesDialog()
    {
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
                   getQuestionsByCategory(categoryId);
               }
           });
       
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    private void initQuestionList()
    {
        // Are we being asked to show the list of Flagged
        // Questions? If so, we check this here.  And if
        // there are any flagged questions, we show them.
        // If there are no questions, then we just show 
        // the user the list of questions in sequential order.
        int viewFlaggedQuestions = getIntent().getIntExtra(QuestionActivity.FLAGGED_QUESTIONS, -1);
        
        if(viewFlaggedQuestions == QuestionActivity.FLAGGED_QUESTIONS_MODE)
        {
            if(questionHelper.getAllFlaggedQuestions().getCount() != 0)
            {
                if(cursor != null)
                {
                    stopManagingCursor(cursor);
                    cursor.close();
                }
                
                cursor = questionHelper.getAllFlaggedQuestions();
              
                // The following lines of code will cause the question
                // count to be displayed in a much more friendly manner
                // to the user. Now, ain't I a nice developer? If so, 
                // squeeze me.  If not, then leave me! :)
                int questionCount = cursor.getCount();
                if(questionCount > 1)
                {
                    setTitle(questionCount + " Flagged Questions");
                }
                else
                {
                    setTitle(questionCount + " Flagged Question");
                }
                
                startManagingCursor(cursor);
                adapter = new QuestionAdapter(this, cursor, questionHelper);
                setListAdapter(adapter);
                
                // Because there is no need for this method to continue
                // since we have retrieved all flagged question
                // we are going to get out of here.
                return;
            }
            else
            {
                Intent noFlaggedQuestionsActivity = new Intent(BrowseQuestionsActivity.this, NoFlaggedQuestionsActivity.class);
                startActivity(noFlaggedQuestionsActivity);
            }
        }
        
        // If we have gotten to this point then it means that
        // the user wishes to see all the questions in 
        // sequential order.
        if(cursor != null)
        {
            stopManagingCursor(cursor);
            cursor.close();
        }

        // The default will always be to show the questions in 
        // sequential order.
        cursor = questionHelper.getAllQuestions("_id");
        startManagingCursor(cursor);
        adapter = new QuestionAdapter(this, cursor, questionHelper);
        setListAdapter(adapter);
    }
    
    private void randomizeAllQuestions()
    {
        if(cursor != null)
        {
            stopManagingCursor(cursor);
            cursor.close();
        }
        
        cursor = questionHelper.getAllRandomQuestions();
        setTitle(cursor.getCount() + " Random Questions");
        startManagingCursor(cursor);
        adapter.changeCursor(cursor);
    }
    
    private void getTenRandomQuestions()
    {
        if(cursor != null)
        {
            stopManagingCursor(cursor);
            cursor.close();
        }
       
        cursor = questionHelper.getTenRandomQuestions();
        setTitle(cursor.getCount() + " Random Questions");
        startManagingCursor(cursor);
        adapter.changeCursor(cursor);
    }
   
    /**
     * This will allow a user to view the list of questions
     * based on the category that has been selected by
     * the user.
     * @param categoryId - the unique id of the category to 
     * display.
     */
    private void getQuestionsByCategory(int categoryId)
    {
        if(cursor != null)
        {
            stopManagingCursor(cursor);
            cursor.close();
        }
       
        cursor = questionHelper.getQuestionsByCategory(categoryId);
        
        // We have to decrement the categoryId because if we 
        // don't do this, we get an array out of index exception
        setTitle(items[categoryId - 1] + " Questions (" + cursor.getCount() + ")");
        startManagingCursor(cursor);
        adapter.changeCursor(cursor);
    }
   
    /**
     * This will show the user the list of Flagged Questions
     * that they have chosen. If there are no flagged questions
     * then the user will be sent to an Activity that tells 
     * them that there are no flagged questions.
     */
    private void getFlaggedQuestions()
    {
        int[] intArray;
        intArray = questionHelper.getAllFlaggedQuestionsIntArray();
       
        if(intArray.length == 0)
        {
            Intent noFlaggedQuestionsActivity = new Intent(BrowseQuestionsActivity.this, NoFlaggedQuestionsActivity.class);
            startActivity(noFlaggedQuestionsActivity);
        }
        else
        {
            // TODO: Fix me!!
            // Notice that currently I am just sending this to a new BrowseQuestionActivity
            // instance.  This just doesn't look right.  That's why I created the created the
            // FlaggedquestionsActivity ... so that the user is taken to view the flagged
            // questions.  Anyhow, check out bug #7 with more info on this.
            //Intent viewFlaggedQuestions = new Intent(BrowseQuestionsActivity.this, FlaggedQuestionsActivity.class);
            Intent viewFlaggedQuestions = new Intent(BrowseQuestionsActivity.this, BrowseQuestionsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putIntArray(QuestionActivity.FLASH_CARD_ARRAY, intArray);
            bundle.putInt(QuestionActivity.FLAGGED_QUESTIONS, QuestionActivity.FLAGGED_QUESTIONS_MODE);
            bundle.putInt(QuestionActivity.CURRENT_INDEX, QuestionActivity.FIRST_QUESTION_INDEX);
            viewFlaggedQuestions.putExtras(bundle);
            viewFlaggedQuestions.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            
            startActivity(viewFlaggedQuestions);
        }
    }
   
    // TODO: This may be removed later on as this was my first attempt
    // at trying to fix bug #7 but was not successful.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == VIEW_FLAGGED_QUESTIONS)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this, "Returned from the FlaggedQuestionsActivity", Toast.LENGTH_LONG).show();
            }
        }
    }
   
    /**
     * Remove all questions that have been flagged.
     */
    private void clearFlaggedQuestions()
    {
        questionHelper.clearAllFlaggedQuestions();
        Toast.makeText(this, "All Flagged Questions Cleared!", Toast.LENGTH_SHORT).show();
        
        int viewFlaggedQuestions = getIntent().getIntExtra(QuestionActivity.FLAGGED_QUESTIONS, -1);
        if(viewFlaggedQuestions == QuestionActivity.FLAGGED_QUESTIONS_MODE)
        {
            // Once the flagged questions have been removed
            // let's take the user back to the default views
            // for questions (a.k.a sequential order). But
            // we only do this if the user is currently
            // viewing the flagged questions.  If the user
            // is not viewing the flagged questions, then
            // they will remain where they are at.
            cursor = questionHelper.getAllQuestions("_id");
            setTitle("All Civics Questions");
            startManagingCursor(cursor);
            adapter.changeCursor(cursor);
        }
    }
   
    /**
     * This will allow the user to view the questions based
     * on the category they belong to.
     */
    private void viewByCategory()
    {
        showDialog(DIALOG_CHOOSE_CATEGORY);
    }
    
    private void viewPreferences()
    {
        Intent appPreferences = new Intent(BrowseQuestionsActivity.this, AppPreferences.class);
        startActivity(appPreferences);
    }

    /**
     * Allows the user to flag a question for future studying.
     * @param questionId - Unique Question ID of question that will be flagged
     */
    private void flagQuestion(long questionId)
    {
        if(questionHelper.isFlaggedQuestion((int)questionId))
        {
            Toast.makeText(this, "Question " + questionId + " is already Flagged!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            questionHelper.insertFlaggedQuestion((int)questionId);
            Toast.makeText(BrowseQuestionsActivity.this, "Question " + questionId + " Flagged!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This will remove the specified question from the list
     * of Flagged Questions. If the user un-flags a question
     * while browsing the questions, they will remain there.
     * If the user is viewing the flagged questions and decides
     * to un-flag a question, the list of flagged questions should
     * refresh.
     * @param questionId
     */
    private void removeFlaggedQuestion(long questionId)
    {
        int questionID = (int)questionId;
        if(questionHelper.isFlaggedQuestion(questionID))
        {
            questionHelper.removeFlaggedQuestion(questionID);
            Toast.makeText(this, "Question " + questionID + " un-flagged!", Toast.LENGTH_SHORT).show();

            int viewFlaggedQuestions = getIntent().getIntExtra(QuestionActivity.FLAGGED_QUESTIONS, -1);
            
            // IF we are not viewing the flagged questions, then just remain
            // in this current activity.
            if(viewFlaggedQuestions == -1)
            {
                return;
            }
            
            // After removing the flagged question, I am refreshing
            // the list of questions the user see's on his screen
            // which will reflect the recent removal of the question
            // from the list of flagged questions
            if(cursor != null)
            {
                stopManagingCursor(cursor);
                cursor.close();
            }
            
            // Check to see if there are any more flagged questions,
            // if there are, then we still show the flagged questions.
            // If there no more flagged questions, then just show all
            // of the questions once more.
            // I am also testing to see that if the user is viewing 
            // flagged questions, then refresh the window, but only
            // refresh it if we are in the Flagged Question Mode.
            if(questionHelper.getAllFlaggedQuestions().getCount() != 0 && viewFlaggedQuestions == QuestionActivity.FLAGGED_QUESTIONS_MODE)
            {
                cursor = questionHelper.getAllFlaggedQuestions();
                setTitle(cursor.getCount() + " Flagged Question(s)");
                startManagingCursor(cursor);
                adapter.changeCursor(cursor);
            }
            else
            {
                cursor = questionHelper.getAllQuestions("_id");
                setTitle("All Civics Questions");
                startManagingCursor(cursor);
                adapter.changeCursor(cursor);
            }
        }
    }

    /**
     * This method will show the user a small dialog box that will
     * give them some tips on how to browse thru the questions.  This
     * will only show up the first time that the activity is launched.
     * The only way this will ever show up on the user's device is if
     * they clear out their data.
     */
    private void showHelpDialogBox()
    {
        new AlertDialog.Builder(BrowseQuestionsActivity.this)
                                .setIcon(R.drawable.alert_dialog_icon)
                                .setTitle("Quick Tips")
                                .setMessage(R.string.browse_help_prompt)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        // Once the user clicks on the ok button, then we
                                        // set the preference value that checks to see if
                                        // this is the first time the application is 
                                        // launched to false so that the user doesn't 
                                        // see this prompt again.
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean(BROWSE_FIRST_TIME, false);
                                        editor.commit();
                                    }
                                })
                                .create().show();
    }
}
