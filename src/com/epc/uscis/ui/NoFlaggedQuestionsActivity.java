package com.epc.uscis.ui;

import com.epc.uscis.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Whenever a user chooses to view Flagged Questions, and
 * there are none, this Activity is what will be shown to 
 * them. I am doing this because this is more intuitive 
 * and will allow the user click on the back button to 
 * go back to the previous window.
 */
public class NoFlaggedQuestionsActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.no_flagged_questions);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
