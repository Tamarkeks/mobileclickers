/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.academic.city.msc.industrial.mobileclickers.jsf.managed;

import gr.academic.city.msc.industrial.mobileclickers.ejb.exception.AccountException;
import gr.academic.city.msc.industrial.mobileclickers.ejb.session.AccountService;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@ManagedBean(name = "lecturerManagedBean")
@SessionScoped
public class LecturerManagedBean implements Serializable {

    @EJB
    private AccountService accountService;
    private String username;
    private String password;
    private String errorMessage;
    private long lecturerID;

    /** Creates a new instance of LecturerManagedBean */
    public LecturerManagedBean() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public long getLecturerID() {
        return lecturerID;
    }

    public String logInActionHandler() {
        try {
            errorMessage = null;
            lecturerID = accountService.authenticate(username, password);
        } catch (AccountException ex) {
            errorMessage = ex.getMessage();
            return null;
        }

        return "profile";
    }
}
