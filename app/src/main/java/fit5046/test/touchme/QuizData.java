package fit5046.test.touchme;

import java.util.ArrayList;

public class QuizData {
    String Question;
    ArrayList<Integer> options;
    int correctIndex=0;
    public QuizData() {
        options = new ArrayList<Integer>();
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public ArrayList<Integer> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Integer> options) {
        this.options = options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        this.correctIndex = correctIndex;
    }
}
