/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.ejb.session;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.AccountException;
import gr.academic.city.msc.industrial.mobileclickers.entity.Department;
import gr.academic.city.msc.industrial.mobileclickers.entity.Lecturer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@Stateless
public class AccountService {

    @PersistenceContext
    private EntityManager em;

    public void registerLecturer(String firstName, String lastName, String title, long deparmentID, String username, String password) throws AccountException {

        if (firstName == null
                || firstName.equals("")
                || lastName == null
                || lastName.equals("")
                || title == null
                || title.equals("")
                || deparmentID <= 0
                || username == null
                || username.equals("")
                || password == null
                || password.equals("")) {
            throw new AccountException("Please provide all details!");
        }

        if (isAccountUnique(username)) {
            Lecturer lecturer = new Lecturer();
            lecturer.setTitle(title);
            lecturer.setFirstName(firstName);
            lecturer.setLastName(lastName);
            lecturer.setUsername(username);
            lecturer.setPassword(password);

            Department department = em.find(Department.class, deparmentID);
            lecturer.setDepartment(department);

            em.persist(lecturer);
        } else {
            throw new AccountException("Username already exists!");
        }
    }

    private boolean isAccountUnique(String username) {
        Query query = em.createQuery("SELECT l FROM Lecturer l WHERE l.username = :username").setParameter("username", username);

        return query.getResultList().isEmpty();
    }

    public long authenticate(String username, String password) throws AccountException {
        Query query = em.createQuery("SELECT l FROM Lecturer l WHERE l.username = :username AND l.password = :password").setParameter("username", username).setParameter("password", password);

        List<Lecturer> result = query.getResultList();

        if (result.size() != 1) {
            throw new AccountException("Username/password mismatch!");
        } else {
            return result.get(0).getId();
        }
    }
}
