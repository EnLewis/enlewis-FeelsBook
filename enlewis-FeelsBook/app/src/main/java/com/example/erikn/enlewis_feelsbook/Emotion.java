package com.example.erikn.enlewis_feelsbook;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**************************************************************
 * Emotion
 * Author: Erik Lewis, 1516026
 * Desc: A data class for holding the information gathered
 *         from the strange cyborg trying to keep track of
 *         the emotions it should be eliciting. Originally was
 *         an abstract class that was inhereted by other individual
 *         emotions  (love, anger etc.). But there was no benefit
 *         and a whole lot of issues so the current implementation
 *         was devised.
 * Inputs:
 *   NONE
 * Outputs:
 *  NONE
 ***************************************************************/
public class Emotion implements Comparable<Emotion> {

    private String date;
    private String note;
    //Types aren't preserved when doing the operations this app requires
    //or at least I'm not advanced enough to understand how to. No matter
    //what type it was before each emotion ends up as type 'Emotion' eventually.
    //Faced with that issue of type ambiguity the emotions need another way to
    // store their type for counting later on in the apps lifecycle.
    // If we have to do this, and the emotions have no specialized
    // behaviour then storing them as string values is no worse than making
    // individual classes that inherit 'Emotion' and don't specialize it
    // whatsoever.
    private String type;
    private static final Integer MAX_CHARS = 100;

    //A default consrtuctor that doesn't ever get used...
    // ideally.
    Emotion() {
        this.date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.CANADA).toString();
        this.note = "Beep Boop, I feel nothing.";
        this.type = "fear";
    }

    Emotion(String date, String note, String type) throws NoteTooLongException{
        this.date = date;
        setNote(note); //using the set method here to avoid rewriting try catch logic.
        this.type = type;
    }

    //A comparsion function to make sure that in an ArrayList, theses
    // emotions can be ordered by date. The direct comparison works because
    // of the highest to lowest nature of the numbers in the ISO8601 date
    // format.
    @Override
    public int compareTo(Emotion e){
        return getDate().compareTo(e.getDate());
    }

    public String getDate(){
        return this.date;
    }

    public String getNote(){
        return this.note;
    }

    public String getType(){
        return this.type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNote(String note) throws NoteTooLongException {
        if(note.length() <= this.MAX_CHARS ) {
            this.note = note;
        } else {
            throw new NoteTooLongException();
        }
    }

    public void setType(String type) {
        this.type = type;
    }
}
