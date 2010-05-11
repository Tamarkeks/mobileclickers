/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.QuestionException;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.CourseService;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.QuestionService;
import gr.academic.city.msc.industrial.mobileclickers.entity.Course;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@ManagedBean(name = "questionManagedBean")
@SessionScoped
public class QuestionManagedBean implements Serializable {

    @EJB
    private QuestionService questionService;
    @EJB
    private CourseService courseService;
    private String questionText;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;
    private String errorMessage;
    private char correctAnswer;
    private int shownAnswers;
    private List<SelectItem> correctAnswersSelectItems;
    private List<Course> coursesTaught;
    private long courseID;
    private String tagString;
    private String chartType;

    /** Creates a new instance of QuestionManagedBean */
    public QuestionManagedBean() {
        shownAnswers = 2;
        correctAnswersSelectItems = new ArrayList<SelectItem>();

        correctAnswersSelectItems.add(new SelectItem('A', "A"));
        correctAnswersSelectItems.add(new SelectItem('B', "B"));
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getAnswer6() {
        return answer6;
    }

    public void setAnswer6(String answer6) {
        this.answer6 = answer6;
    }

    public int getShownAnswers() {
        return shownAnswers;
    }

    public void setShownAnswers(int shownAnswers) {
        this.shownAnswers = shownAnswers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(char correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<SelectItem> getCorrectAnswersSelectItems() {
        return correctAnswersSelectItems;
    }

    public void setCorrectAnswersSelectItems(List<SelectItem> correctAnswersSelectItems) {
        this.correctAnswersSelectItems = correctAnswersSelectItems;
    }

    public List<Course> getCoursesTaught() {
        FacesContext context = FacesContext.getCurrentInstance();
        LecturerManagedBean lmb = context.getApplication().evaluateExpressionGet(context, "#{lecturerManagedBean}", LecturerManagedBean.class);
        coursesTaught = courseService.getCoursesForLecturer(lmb.getLecturerID());
        return coursesTaught;
    }

    public void setCoursesTaught(List<Course> coursesTaught) {
        this.coursesTaught = coursesTaught;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }

    public String createQuestionActionHandler() {
        Map<Character, String> possibleAnswers = new HashMap<Character, String>();


        possibleAnswers.put(new Character('A'), answer1);
        possibleAnswers.put(new Character('B'), answer2);

        if (shownAnswers >= 3) {
            possibleAnswers.put(new Character('C'), answer3);
        }
        if (shownAnswers >= 4) {
            possibleAnswers.put(new Character('D'), answer4);
        }
        if (shownAnswers >= 5) {
            possibleAnswers.put(new Character('E'), answer5);
        }
        if (shownAnswers >= 6) {
            possibleAnswers.put(new Character('F'), answer6);
        }

        List<String> tagStrings = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(tagString, ",; ");
        while (tokenizer.hasMoreTokens()) {
            tagStrings.add(tokenizer.nextToken());
        }
        
        try {
            errorMessage = null;
            questionService.createQuestion(questionText, possibleAnswers, correctAnswer, courseID, tagStrings, chartType);
            resetForm();
        } catch (QuestionException ex) {
            errorMessage = ex.getMessage();
            return null;
        }

        return "success";
    }

    public String addAnswerActionHandler() {
        char previous = correctAnswersSelectItems.get(correctAnswersSelectItems.size() - 1).getValue().toString().charAt(0);
        Character newChar = new Character((char) (previous + 1));
        correctAnswersSelectItems.add(new SelectItem(newChar.charValue(), newChar.toString()));

        shownAnswers++;
        return null;
    }

    public String deleteAnswerActionHandler() {
        correctAnswersSelectItems.remove(correctAnswersSelectItems.size() - 1);

        switch (shownAnswers) {
            case 3:
                answer3 = null;
                break;
            case 4:
                answer4 = null;
                break;
            case 5:
                answer5 = null;
                break;
            case 6:
                answer6 = null;
                break;
        }

        shownAnswers--;
        return null;
    }

    private void resetForm() {
        answer1 = null;
        answer2 = null;
        answer3 = null;
        answer4 = null;
        answer5 = null;
        answer6 = null;

        correctAnswersSelectItems.clear();
        correctAnswersSelectItems.add(new SelectItem('A', "A"));
        correctAnswersSelectItems.add(new SelectItem('B', "B"));

        questionText = null;
        chartType = null;
        tagString = null;
    }
}
