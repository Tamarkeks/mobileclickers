/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.ejb.session;

import gr.academic.city.msc.industrial.mobileclickers.entity.Department;
import gr.academic.city.msc.industrial.mobileclickers.entity.Tag;
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
public class SystemWideServices {

    @PersistenceContext
    private EntityManager em;

    public List<Department> getAllDepartments() {
        Query query = em.createQuery("SELECT d FROM Department d");

        return query.getResultList();
    }

    public Tag createTag(String tagName) {
        Tag newTag = new Tag();
        newTag.setName(tagName);
        em.persist(newTag);

        return newTag;
    }
}
