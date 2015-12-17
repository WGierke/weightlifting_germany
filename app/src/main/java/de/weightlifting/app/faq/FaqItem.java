package de.weightlifting.app.faq;

public class FaqItem {

    private String header = "";
    private String question = "";
    private String answer = "";

    public FaqItem(String header, String question, String answer) {
        this.header = header;
        this.question = question;
        this.answer = answer;
    }

    public String getHeader() {
        return header;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
