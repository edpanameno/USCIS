package com.epc.uscis.adapters;

import com.epc.uscis.R;
import com.epc.uscis.db.QuestionHelper;

import android.database.Cursor;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class QuestionHolder
{
    private TextView questionNumber = null;
    private TextView question = null;
    private TextView answer = null;
    
    public QuestionHolder(View row)
    {
        questionNumber = (TextView)row.findViewById(R.id.questionNumber);
        question = (TextView)row.findViewById(R.id.question);
        answer = (TextView)row.findViewById(R.id.answer);
    }
    
    public void populateFrom(Cursor c, QuestionHelper helper)
    {
        questionNumber.setText(helper.getQuestionNumber(c));
        question.setText(Html.fromHtml(helper.getQuestion(c)));
        
        // Note:  I'm adding additional formatting here because it's
        // just so much easier to do it here instead of doing it
        // for each of the 100 questions! ;)
        answer.setText(Html.fromHtml("<u><b>Answer:</b></u><br/><b><i>" + helper.getAnswer(c) + "<i/></b><br />"));
        
        // Setting the answer's visibility to GONE has a little side effect
        // that when the user moves away from the question and comes back
        // to the question, the visibility is set back to GONE. The state
        // of the visibility of the question should remain shown after the
        // user comes back to the question.
        // TODO: Come back and fix this later
        answer.setVisibility(TextView.GONE);
    }
}
