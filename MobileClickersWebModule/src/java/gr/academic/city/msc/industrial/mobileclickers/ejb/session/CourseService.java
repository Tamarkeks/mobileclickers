/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.ejb.session;

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

    public List<Course> getCoursesForLecturer(long lecturerID) {
        return em.find(Lecturer.class, lecturerID).getCoursesTaught();
    }

    public void enrollOnCourse(long lecturerID, long courseID) {
        Lecturer lecturer = em.find(Lecturer.class, lecturerID);
        Course course = em.find(Course.class, courseID);

        lecturer.getCoursesTaught().add(course);
        course.getLecturers().add(lecturer);

        em.merge(lecturer);
        em.merge(course);
    }

    public void createCourse(long lecturerID, String name, String code) {
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

    public Set<Tag> getTagsForCourse(long courseID) {
        Course course = em.find(Course.class, courseID);

        Set<Tag> tags = new HashSet<Tag>();

        for (Question question: course.getQuestions()) {
            for (Tag tag: question.getTags()) {
                tags.add(tag);
            }
        }

        return tags;
    }
}
