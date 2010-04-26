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
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@ManagedBean(name = "issueQuestionManagedBean")
@SessionScoped
public class IssueQuestionManagedBean implements Serializable {

    @EJB
    private QuestionService questionService;
    @EJB
    private CourseService courseService;
    private List<Course> coursesTaught;
    private List<Question> questionsForCourse;
    private long questionID;
    private long courseID = 0;
    private String errorMessage;

    /** Creates a new instance of IssueQuestionManagedBean */
    public IssueQuestionManagedBean() {
        /*FacesContext context = FacesContext.getCurrentInstance();
        LecturerManagedBean lmb = context.getApplication().evaluateExpressionGet(context, "#{lecturerManagedBean}", LecturerManagedBean.class);
        coursesTaught = courseService.getCoursesForLecturer(lmb.getLecturerID());
        questionsForCourse = questionService.getQuestionsForCourse(coursesTaught.get(0).getId());
         */
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
        return null;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
