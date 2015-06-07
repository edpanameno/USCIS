package com.epc.uscis.util;

import java.util.ArrayList;
import java.util.Collections;

public class Util
{
    private static final int NUMBER_OF_FLASH_CARDS = 100;
    
    public static int[] sequentialIntArray()
    {
        int[] intArray = new int[NUMBER_OF_FLASH_CARDS];
        for(int i = 1, j = 0; i <= NUMBER_OF_FLASH_CARDS; i++, j++)
        {
            intArray[j] = i;
        }
        
        return intArray;
    }
    
    public static int[] randomIntArray()
    {
        ArrayList<Integer> intArray = new ArrayList<Integer>();
        for(int i = 1; i <= NUMBER_OF_FLASH_CARDS; i++)
        {
            intArray.add(i);
        }
       
        // I am using the shuffle method to get a really
        // random shuffle of the cards
        Collections.shuffle(intArray);
        int[] array = new int[NUMBER_OF_FLASH_CARDS]; 
       
        // Highly inefficient way of doing this, but
        // for now, this will have to do.  I am doing
        // this so that I get a truly randomized list
        // of questions to show for the user.
        for(int i = 0; i < 100; i++)
        {
            array[i] = intArray.get(i);
        }
        
        return array;
    }
    
    public static String stripOutHtml(String text)
    {
        // Remove any HTML tags which will always be 
        // enclosed in <> tags
        String sanatizedText = text.replaceAll("<[^>]+>", "");
        
        // For some reason or another, the word one doesn't sound
        // very good, therefore I am replacing the word one with 
        // the number 1 so that it can be spoken clearer by the
        // text to speech engine.
        sanatizedText = sanatizedText.replaceAll("one", "1");
       
        // Remove 4 numbers, i.e. 1900 or 1800
        sanatizedText = sanatizedText.replaceAll("\\d{4}\'[s]", "");
        
        // We don't want the text to speech engine to say anything about 
        // the asterisk so it'll be removed.  Note: we have to escape the 
        // character with two backslashes.
        sanatizedText = sanatizedText.replaceAll("\\*", "");
        
        return sanatizedText;
    }
   
    /**
     * This will remove any numbers inside of parentheses so that the
     * text engine doesn't repeat them when the user is viewing 
     * the answers to the current question.
     */
    public static String removeNumbersInParentheses(String text)
    {
        String sanatizedText;
        sanatizedText = text.replaceAll("\\d*", "");
        
        // We still have to remove any hard breaks so that these are
        // not read out loud by the text to speech engine. This is
        // code duplication and I don't like it.  Will need to
        // come back to this later and fix it.
        // TODO: Fix this so that this is not duplicated here.
        sanatizedText = sanatizedText.replaceAll("<[^>]+>", "");
        
        return sanatizedText;
    }
}
