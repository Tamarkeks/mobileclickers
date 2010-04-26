/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.session.CourseService;
import gr.academic.city.msc.industrial.mobileclickers.entity.Course;
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
@ManagedBean(name="enrollManagedBean")
@SessionScoped
public class EnrollManagedBean implements Serializable{
    @EJB
    private CourseService courseService;
    private List<Course> courses;
    private long courseID;
    /** Creates a new instance of EnrollManagedBean */
    public EnrollManagedBean() {
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public List<Course> getCourses() {
        courses = courseService.getAllCourses();
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String enrollActionHandler() {
        FacesContext context = FacesContext.getCurrentInstance();
        LecturerManagedBean lmb = context.getApplication().evaluateExpressionGet(context, "#{lecturerManagedBean}", LecturerManagedBean.class);

        courseService.enrollOnCourse(lmb.getLecturerID(), courseID);

        return "success";
    }
}
