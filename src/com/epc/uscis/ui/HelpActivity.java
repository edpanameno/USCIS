package com.epc.uscis.ui;

import com.epc.uscis.R;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class HelpActivity extends Activity
{
    private TextView txtHelpContent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        setTitle("USCIS Civis Flash Cards - Help");
        
        txtHelpContent = (TextView)findViewById(R.id.txtHelpContent);
        txtHelpContent.setText(Html.fromHtml(helpText()));
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        finish();
    }
    
    /**
     * This returns the text that will be shown to the user
     * when they click on the Help button on the HomeActivity
     * class.
     * @return HTML text displaying the help content.
     */
    private String helpText()
    {
        try
        {
            return "<h3>USCIS Civics Cards Help (v" + this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName  + ")</h3>"  + 
                   "Below you'll find information on how to use the features " +
                   "of this application. <br />" + 
                   "<h4><u>All Civics Questions</u></h4>" + 
                   "<p>In this part of the application you'll be able to see all 100 Civics Questions " + 
                   "in sequential order, random order or 10 random questions.  To view questions " + 
                   "in this manner, press the <u>Menu</u> key on your phone and choose " + 
                   "the desired option.</p>" +
                   "<p>While viewing all the questions, you can tap on a question to view the " + 
                   "answer. You can then tap on the question again to hide the answer. " + 
                   "If you come accross a question that you are having problems with, " + 
                   "you can <u>Flag</u> the question by tapping on the question and holding it " + 
                   "for 2 seconds. You'll then get a small pop-up window that allows you to choose " + 
                   " to flag the question. To remove a <u>Flagged</u> question, follow the same steps " + 
                   "and choose 'No' to <u>un-flag</u> the question. To view all flagged questions " +
                   "press the <u>Menu</u> key on your phone and choose 'View Flagged'.</p>" + 
                   "<h4><u>Flash Cards</u></h4>" + 
                   "<p>If you want to view the Civics Cards in a Flash Card format, choose the 'Flash Cards' " + 
                   "options. To view the answer to a question <u>tap on the question</u>, you will " + 
                   "then see the answer the question. To navigate thru each question, press the back or " + 
                   "arrow at the bottom of each Card.</p>" + 
                   "<p>Questions can be shown in a random order, or can also be filtered by Category.  " + 
                   "To do this, click your <u>Menu</u> button and choose the appropriate option.</p>" + 
                   "<p>Clicking on the <u>Menu</u> key on your phone brings up a menu that allows you " + 
                   "randomize all questions, view the questions by category, 10 random questions, " + 
                   "<u>Flag</u> a question and view <u>Flagged</u> questions.</p>" + 
                   "<h4><u>Flagged Questions</u></h4>" + 
                   "<p>Clicking on this option will allow you to see the list of currently flagged " +
                   "questions.  If no questions are currenetly flagged, you'll be taken to the default " +
                   "view of all 100 questions.</p>" +
                   "<h4><u>Credits</u></h4>" +
                   "<p>For more information about the Citizenship Test, please visit " + 
                   "http://www.uscis.gov.</p>" + 
                   "<p>Some icons used on this application can be found at " + 
                   "http://www.androidicons.com.</p>";
        }
        catch(NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
