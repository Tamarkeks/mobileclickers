/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.AccountException;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.AccountService;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.SystemWideServices;
import gr.academic.city.msc.industrial.mobileclickers.entity.Department;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@ManagedBean(name = "accountManagedBean")
@RequestScoped
public class AccountManagedBean {

    @EJB
    private SystemWideServices systemWideServices;
    @EJB
    private AccountService accountService;
    private String title;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String errorMessage;
    private List<Department> departments;
    private long departmentID;

    /** Creates a new instance of AccountManagedBean */
    public AccountManagedBean() {
    }

    public long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    public List<Department> getDepartments() {
        return systemWideServices.getAllDepartments();
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String registerActionHandler() {
        try {
            errorMessage = null;
            accountService.registerLecturer(firstName, lastName, title, departmentID, username, password);
        } catch (AccountException ex) {
            errorMessage = ex.getMessage();
            return null;
        }

        return "profile";
    }
}
