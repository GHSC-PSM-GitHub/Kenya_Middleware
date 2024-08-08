package com.odkdhis.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.Submission;

@Repository
@Transactional
public class SubmissionDaoImpl extends GenericDaoImpl<Submission, String> implements SubmissionDao {

    @Override
    public void save(Submission sub) throws Exception {
        saveBean(sub);
    }

    @Override
    public List<Submission> getSubmissions(String formId, String instanceId, Date dateSubmitted, Date dateCollected, Boolean processed) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Submission> criteria = builder.createQuery(Submission.class);
        Root<Submission> root = criteria.from(Submission.class);

        List<Predicate> predicates = new ArrayList<>();

        if (formId != null) {
            predicates.add(builder.equal(root.get("formId"), formId));
        }
        if (instanceId != null) {
            predicates.add(builder.equal(root.get("instanceId"), instanceId));
        }
        if (dateSubmitted != null) {
            predicates.add(builder.equal(root.get("dateSubmitted"), dateSubmitted));
        }
        if (dateCollected != null) {
            predicates.add(builder.equal(root.get("dateCollected"), dateCollected));
        }
        if (processed != null) {
            predicates.add(builder.equal(root.get("processed"), processed));
        }
        if (predicates.size() > 0) {
            criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }
        return findByCriteria(-1, -1, criteria.select(root));
    }

    @Override
    public Submission findById(String id) {
        return findBeanById(id);
    }

    @Override
    public void delete(Submission sub) throws Exception {
        deleteBeanById(sub.getId());
    }

}
