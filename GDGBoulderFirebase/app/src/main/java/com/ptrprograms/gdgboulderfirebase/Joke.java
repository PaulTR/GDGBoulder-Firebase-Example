package com.ptrprograms.gdgboulderfirebase;

import java.util.HashMap;
import java.util.Map;

public class Joke {

    private String question;
    private String answer;
    public int plusOned;

    public Joke() {

    }

    public Joke(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getPlusOned() {
        return plusOned;
    }

    public void setPlusOned(int plusOned) {
        this.plusOned = plusOned;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("answer", answer);
        result.put("question", question);
        result.put("plusOned", plusOned);

        return result;
    }

}
