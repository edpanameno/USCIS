package com.epc.uscis.ui;

import com.epc.uscis.R;
import com.epc.uscis.db.QuestionHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * This class will implement the logic to store and set preferences
 * for the application.
 */
public class AppPreferences extends PreferenceActivity 
{
    /**
     * This is the name of the preference that is used to tell whether or not
     * text to speech should be enabled or not. This is stored as a boolean 
     * in the application preferences option.
     */
    public static String TEXT_TO_SPEECH = "text_to_speech";
    
    private Preference clearFlaggedQuestions;
    private QuestionHelper questionHelper; 
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);
        
        // We are looking for the preference object by it's key value
        clearFlaggedQuestions = (Preference)findPreference("remove_flagged_questions");
        
        // This is not my preferred method of setting up the click listener for
        // this preference object.  The reason is that this breaks the format 
        // I've used in other classes where I've had the class implement the 
        // appropriate interface and implemented the required method.
        clearFlaggedQuestions.setOnPreferenceClickListener(new OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                deleteAllFlaggedQuestions();
                return true;
            }
        });
    }
    
    private void deleteAllFlaggedQuestions()
    {
        new AlertDialog.Builder(AppPreferences.this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setMessage("Do you wish to delete all Flagged Questions?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        questionHelper = new QuestionHelper(AppPreferences.this);
                      
                        if(questionHelper.getFlaggedQuestionsCount() > 0)
                        {
                            questionHelper.clearAllFlaggedQuestions();
                            questionHelper.close();
                            Toast.makeText(AppPreferences.this, "Successfully Removed all Flagged Questions!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(AppPreferences.this, "You currently have no Flagged Questions!", Toast.LENGTH_SHORT).show();
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
}
