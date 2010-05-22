/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.ejb.session;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.CourseException;
import gr.academic.city.msc.industrial.mobileclickers.entity.Course;
import gr.academic.city.msc.industrial.mobileclickers.entity.Lecturer;
import gr.academic.city.msc.industrial.mobileclickers.entity.Question;
import gr.academic.city.msc.industrial.mobileclickers.entity.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@Stateless
public class CourseService {
    @PersistenceContext
    private EntityManager em;

    public List<Course> getAllCourses() {
        Query query = em.createQuery("SELECT c FROM Course c");
        return query.getResultList();
    }

    public List<Course> getCoursesWithQuestionsForLecturer(long lecturerID) throws CourseException {
        List<Course> coursesTaught = em.find(Lecturer.class, lecturerID).getCoursesTaught();
        
        List<Course> courses = new ArrayList<Course>();
        for (Course course: coursesTaught) {
            if (course.getQuestions().size() != 0) {
                courses.add(course);
            }
        }
        return courses;
    }

    public List<Course> getCoursesForLecturer(long lecturerID) {
        return em.find(Lecturer.class, lecturerID).getCoursesTaught();
    }

    public void enrollOnCourse(long lecturerID, long courseID) throws CourseException {
        Lecturer lecturer = em.find(Lecturer.class, lecturerID);
        Course course = em.find(Course.class, courseID);

        if (lecturer.getCoursesTaught().contains(course)
                || course.getLecturers().contains(lecturer)) {
            throw new CourseException("Lecturer already enrolled on this course!");
        }
        
        lecturer.getCoursesTaught().add(course);
        course.getLecturers().add(lecturer);

        em.merge(lecturer);
        em.merge(course);
    }

    public void createCourse(long lecturerID, String name, String code) throws CourseException {
        if (lecturerID <= 0
                || name == null
                || name.equals("")
                || code == null
                || code.equals("")) {
            throw new CourseException("Please provide all details! (course name and code)");
        }
        
        if (!isCourseUnique(code)) {
            throw new CourseException("Course with the specified code already exists!");
        }

        Course course = new Course();
        course.setName(name);
        course.setCode(code);
        course.setLecturers(new ArrayList<Lecturer>());

        Lecturer lecturer = em.find(Lecturer.class, lecturerID);
        course.getLecturers().add(lecturer);

        em.persist(course);

        lecturer.getCoursesTaught().add(course);

        em.merge(lecturer);
    }

    public Set<Tag> getTagsForCourse(long courseID) throws CourseException {
        Course course = em.find(Course.class, courseID);
        if (course == null) {
            throw new CourseException("Course does not exist!");
        }

        Set<Tag> tags = new HashSet<Tag>();

        for (Question question: course.getQuestions()) {
            for (Tag tag: question.getTags()) {
                tags.add(tag);
            }
        }

        return tags;
    }

    private boolean isCourseUnique(String code) {
        Query query = em.createQuery("SELECT c FROM Course c WHERE c.code = :code").setParameter("code", code);

        return query.getResultList().isEmpty();
    }
}
