package com.mycompany.soundquiz.server.model;

public class Question {
    private String title;
    private String A;
    private String B;
    private String C;
    private String D;
    private String answer;

    public Question() {
    }

    public Question(String title, String A, String B, String C, String D, String answer) {
        this.title = title;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getA() {
        return A;
    }

    public void setA(String A) {
        this.A = A;
    }

    public String getB() {
        return B;
    }

    public void setB(String B) {
        this.B = B;
    }

    public String getC() {
        return C;
    }

    public void setC(String C) {
        this.C = C;
    }

    public String getD() {
        return D;
    }

    public void setD(String D) {
        this.D = D;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
