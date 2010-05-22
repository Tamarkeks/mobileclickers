/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gr.academic.city.msc.industrial.mobileclickers.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Ivo Neskovic <ivo.neskovic@gmail.com>
 */
@Entity
public class AnsweredQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL, optional=false)
    @JoinColumn(nullable=false)
    private Question question;
    @JoinColumn(nullable=false)
    @ManyToMany(cascade=CascadeType.ALL)
    private List<SubmitedAnswer> submitedAnswers;
    private String questionCode;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date issueDate;
    @ManyToOne(cascade=CascadeType.ALL, optional=false)
    @JoinColumn(nullable=false)
    private Lecturer issuer;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Lob()
    @Basic(fetch = FetchType.LAZY)
    private byte[] resultsChart;

    public byte[] getResultsChart() {
        return resultsChart;
    }

    public void setResultsChart(byte[] resultsChart) {
        this.resultsChart = resultsChart;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Lecturer getIssuer() {
        return issuer;
    }

    public void setIssuer(Lecturer issuer) {
        this.issuer = issuer;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public List<SubmitedAnswer> getSubmitedAnswers() {
        return submitedAnswers;
    }

    public void setSubmitedAnswers(List<SubmitedAnswer> submitedAnswers) {
        this.submitedAnswers = submitedAnswers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AnsweredQuestion)) {
            return false;
        }
        AnsweredQuestion other = (AnsweredQuestion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "gr.academic.city.msc.industrial.mobileclickers.entity.AnsweredQuestion[id=" + id + "]";
    }

}
