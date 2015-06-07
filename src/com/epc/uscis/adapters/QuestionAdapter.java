package com.epc.uscis.adapters;

import com.epc.uscis.R;
import com.epc.uscis.db.QuestionHelper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class QuestionAdapter extends CursorAdapter 
{
    private QuestionHelper helper;
    
    public QuestionAdapter(Context context, Cursor c, QuestionHelper helper)
    {
        super(context, c);
        this.helper = helper;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        QuestionHolder holder = (QuestionHolder)view.getTag();
        holder.populateFrom(cursor, helper);
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.question_row, parent, false);
        QuestionHolder holder = new QuestionHolder(row);
        row.setTag(holder);
        
        return row;
    }
}
