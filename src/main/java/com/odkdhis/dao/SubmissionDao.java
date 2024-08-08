package com.odkdhis.dao;

import java.util.Date;
import java.util.List;

import com.odkdhis.model.Submission;

public interface SubmissionDao {

    void save(Submission config) throws Exception;

    List<Submission> getSubmissions(String formId, String instanceId, Date dateSubmitted, Date dateCollected, Boolean processed);

    Submission findById(String id);

    void delete(Submission config) throws Exception;

}
