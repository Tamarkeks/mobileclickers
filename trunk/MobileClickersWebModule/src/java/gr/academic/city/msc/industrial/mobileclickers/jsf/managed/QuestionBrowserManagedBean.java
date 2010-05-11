/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.QuestionException;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.CourseService;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.QuestionService;
import gr.academic.city.msc.industrial.mobileclickers.entity.Course;
import gr.academic.city.msc.industrial.mobileclickers.entity.Question;
import gr.academic.city.msc.industrial.mobileclickers.entity.Tag;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@ManagedBean(name = "questionBrowserManagedBean")
@SessionScoped
public class QuestionBrowserManagedBean implements Serializable {

    @EJB
    private QuestionService questionService;
    @EJB
    private CourseService courseService;
    private List<Course> coursesTaught;
    private List<Question> questionsForCourse;
    private Set<Tag> tagsForCourse;
    private long questionID;
    private long courseID = 0;
    private long tagID;
    private String errorMessage;

    /** Creates a new instance of QuestionBrowserManagedBean */
    public QuestionBrowserManagedBean() {
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

    public List<Question> getQuestionsForCourse() {
        return questionsForCourse;
    }

    public void setQuestionsForCourse(List<Question> questionsForCourse) {
        this.questionsForCourse = questionsForCourse;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public String getQuestionsActionHandler() {
        questionsForCourse = questionService.getQuestionsForCourse(courseID);
        tagsForCourse = courseService.getTagsForCourse(courseID);
        return null;
    }

    public String tagFilterActionHandler() {
        questionsForCourse = questionService.filterQuestions(courseID, tagID);
        return null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getTagID() {
        return tagID;
    }

    public void setTagID(long tagID) {
        this.tagID = tagID;
    }

    public Set<Tag> getTagsForCourse() {
        return tagsForCourse;
    }

    public void setTagsForCourse(Set<Tag> tagsForCourse) {
        this.tagsForCourse = tagsForCourse;
    }

    public String issueQuestionActionHandler() {
        try {
            errorMessage = null;

            FacesContext context = FacesContext.getCurrentInstance();
            LecturerManagedBean lmb = context.getApplication().evaluateExpressionGet(context, "#{lecturerManagedBean}", LecturerManagedBean.class);
            String code = questionService.issueQuestion(lmb.getLecturerID(), questionID);
            return code;
        } catch (QuestionException ex) {
            errorMessage = ex.getMessage();
        }

        return "error";
    }
}
