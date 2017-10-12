package userCenter.model.batis;

public class PasswordQuestion {
    private Long id;

    private Long uid;

    private String questionName;

    private String questionAnswer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getQuestionName() {
        return questionName;
    }

    public void setQuestionName(String questionName) {
        this.questionName = questionName == null ? null : questionName.trim();
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer == null ? null : questionAnswer.trim();
    }

    public PasswordQuestion(Long uid, String questionName, String questionAnswer) {
        this.uid = uid;
        this.questionName = questionName;
        this.questionAnswer = questionAnswer;
    }

    public PasswordQuestion() {
        super();
    }
}