package com.odkdhis.util;

import com.odkdhis.model.DataValueGroup;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.odkdhis.model.DatasetValue;
import com.odkdhis.model.FormName;
import com.odkdhis.model.OdkForm;
import com.odkdhis.model.SingleForm;
import com.odkdhis.model.Submission;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SubmissionUtil extends CommonUtil {

    /**
     * Downloads data from ODK Processes it and saves it locally to wait to be
     * send to DHIS 2
     *
     */
    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
    //@Scheduled(cron = "09 25 07-23 * * ?", zone = "Africa/Nairobi")
    @Scheduled(cron ="09 55 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmission() {
        try {
            prepareSubmissionData();
        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void prepareSubmissionData() {
        log.info("********PREPARING DATAVALUES FROM REMOTE ODK SERVER *****************************************");
        String odkToken = getODKAuthToken();
        String dhisBasicToken = getDHISBasicToken();
        try {

            // Loop through all form instance Ids
            for (OdkForm form : OdkForm.values()) {
                TimeUnit.SECONDS.sleep(2);
                log.info("***** PREPARING DATAVALUES FOR FORM ID ************>" + form.name());
                final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID + "/forms/" + form.name() + "/submissions";
                HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("JSON", odkToken));
                JSONArray jArray = new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());

                // Loop through each submission of the form under processing
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject instance = jArray.getJSONObject(i);
                    String instanceId = instance.getString("instanceId");
                    String instanceCreatedDate = instance.getString("createdAt");

                    // Check if a submission has already been processed
                    if (submissionDao.getSubmissions(form.name(), instanceId, null, null, null).size() > 0) {
                        continue;
                    }
                    String xmlString = getSubmissionInstanceXml(PROJECTID, form.name(), instanceId, odkToken);

                    if (Objects.isNull(xmlString)) {
                        continue;
                    }

                    // Convert submission XML data to JSON for easy parsing
                    JSONObject jsonObj = XML.toJSONObject(xmlString);
                    String json = jsonObj.toString(INDENTATION);
                    JSONObject instancejson = new JSONObject(json);
                    JSONObject datajson = instancejson.getJSONObject("data");
                    String instanceCollectedDate = datajson.getString("today");
                    Integer numValue=0;
                    try {
                      numValue = (form.name().equals("storage_area_assesment_org") || form.name().equals("storage_area_assesment2"))
                            ? datajson.getJSONObject("Storage_Assessment").getInt(getDenominatorOrNumeratorFieldName(form.name(), "N"))
                            : datajson.getInt(getDenominatorOrNumeratorFieldName(form.name(), "N"));   
                    } catch (Exception e) {
                    }
                    Integer denValue=0;
                    try {
                       denValue = (form.name().equals("storage_area_assesment_org") || form.name().equals("storage_area_assesment2"))
                            ? datajson.getJSONObject("Storage_Assessment").getInt(getDenominatorOrNumeratorFieldName(form.name(), "D"))
                            : datajson.getInt(getDenominatorOrNumeratorFieldName(form.name(), "D")); 
                    } catch (Exception e) {
                    }
 
                     if (numValue > 0 || denValue > 0) {
                        Submission sub = new Submission();
                        
                        
                        sub.setFormId(form.name());
                        sub.setInstanceId(instanceId);
                        sub.setOrgUnitCode(String.valueOf(datajson.getInt("mflcode")));
                        sub.setDateSubmitted(getDateFromString(instanceCreatedDate, "Africa/Nairobi"));
                        sub.setDateCollected(getDateFromStringNoTime(instanceCollectedDate, "Africa/Nairobi"));
                        sub.setPeriod(getDHISDailyPeriodFromString(instanceCollectedDate, "Africa/Nairobi"));
                        sub.setOrgUnit(getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode")), dhisBasicToken));

                        String num = String.valueOf(numValue);
                        String den = String.valueOf(denValue);
                        String score = String.valueOf(getScore(numValue, denValue, form.getMaxScore()));
                        String numScore = String.valueOf(getNumeratorScore(numValue, denValue, form.getMaxScore()));

                        sub.setNumerator(num);
                        sub.setNumeratorScore(numScore);
                        sub.setDenominator(den);
                        sub.setScore(score);
                        sub.setProcessed(Boolean.FALSE);
                        sub.setFormName(form.getName());
                        sub.setMaxScore(String.valueOf(form.getMaxScore()));
                        sub.setDenCode("DENO-" + form.getDataSetId());
                        sub.setNumeCode("NUME-" + form.getDataSetId());
                        sub.setFormNameCode("FORM-" + form.getDataSetId());
                        sub.setMaxCode("MAX-" + form.getDataSetId());
                        sub.setScoreCode("SCORE-" + form.getDataSetId());
                        submissionDao.save(sub);
                    }

                }
            }
            log.info("**************************** DONE PREPARING DATAVALUES ****************************************");

            // Save data values to the database
            TimeUnit.SECONDS.sleep(2);
            saveDataSetValues(dhisBasicToken);
            TimeUnit.SECONDS.sleep(2);
            if (orgDao.getOrganizationUnits(null, null, null, null, null).size() > 0) {
                saveDataSummary();
            }

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }

    }

    private void saveDataSetValues(String dhisBasicToken) {

        log.info("**************************** SAVING DATAVALUES ****************************************");
        try {
            for (SingleForm form : SingleForm.values()) {
                log.info("**** SAVING DATAVALUES FOR FORM ID ************" + form.name());
                TimeUnit.SECONDS.sleep(2);
                List<Submission> submissions = submissionDao.getSubmissions(form.name(), null, null, null, Boolean.FALSE);

                submissions.forEach(sub -> {
                    List<DatasetValue> dataValues = new ArrayList<>();
                    dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getDenCode(), dhisBasicToken), sub.getDenominator()));
                    dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getNumeCode(), dhisBasicToken), sub.getNumeratorScore()));
                    dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getFormNameCode(), dhisBasicToken), sub.getFormName()));
                    dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getScoreCode(), dhisBasicToken), sub.getScore()));
                    dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getMaxCode(), dhisBasicToken), sub.getMaxScore()));
                    sub.setProcessed(Boolean.TRUE);

                    DataValueGroup dvg = new DataValueGroup();
                    dvg.setDataSend(Boolean.FALSE);
                    dvg.setDataSet(getEntityIdFromCode("dataSets", form.getDataSetId(), dhisBasicToken));
                    dvg.setDateCollected(sub.getDateCollected());
                    dvg.setOrgUnit(sub.getOrgUnit());
                    dvg.setPeriod(sub.getPeriod());
                    dvg.addDatavalues(dataValues);
                    // For summary
                    dvg.setOrgUnitCode(sub.getOrgUnitCode());
                    dvg.setFormName(FormName.valueOf(form.getDataSetId()));
                    dvg.setNumerator(new BigDecimal(sub.getNumeratorScore()));
                    dvg.setDenominator(new BigDecimal(sub.getDenominator()));
                    dvg.setScore(new BigDecimal(sub.getScore()));
                    dvg.setDataSendTosummary(Boolean.FALSE);
                    dvg.setRetries(0);

                    try {
                        submissionDao.save(sub);
                        dataValueGroupDao.save(dvg);
                    } catch (Exception ex) {
                        Logger.getLogger(SubmissionUtil.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            // Create the whole datavalues listing
            getDataSetValuesListFromTheDoubleForms("storage_area_assesment2", "storage_area_assesment_org", dhisBasicToken);
            getDataSetValuesListFromTheDoubleForms("Inventory_Management", "Inventory_Management_combined", dhisBasicToken);
            getDataSetValuesListFromTheDoubleForms("supply_chain_audit_v2", "supply_chain_audit_slim", dhisBasicToken);

            log.info("**************************** DONE SAVING DATAVALUES****************************************");
        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void getDataSetValuesListFromTheDoubleForms(String formId1, String formId2, String dhisBasicToken) {

        log.info("**** SAVING DATAVALUES FOR FORM ID ************" + formId1 + "  And  " + formId2);
        try {
            List<Submission> submissions1 = submissionDao.getSubmissions(formId1, null, null, null, Boolean.FALSE);
            List<Submission> submissions2 = submissionDao.getSubmissions(formId2, null, null, null, Boolean.FALSE);

            if (!submissions1.isEmpty() && !submissions2.isEmpty()) {
                for (Submission sub1 : submissions1) {
                    for (Submission sub2 : submissions2) {

                        if (sub1.getPeriod().equalsIgnoreCase(sub2.getPeriod())
                                && sub1.getOrgUnit().equalsIgnoreCase(sub2.getOrgUnit())) {
                            Integer num = Integer.parseInt(sub1.getNumerator()) + Integer.parseInt(sub2.getNumerator());
                            Integer den = Integer.parseInt(sub1.getDenominator()) + Integer.parseInt(sub2.getDenominator());

                            List<DatasetValue> dataValues = new ArrayList<>();
                            dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub1.getDenCode(), dhisBasicToken), String.valueOf(den)));
                            dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub1.getNumeCode(), dhisBasicToken), String.valueOf(getNumeratorScore(num, den, Integer.parseInt(sub1.getMaxScore())))));
                            dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub1.getFormNameCode(), dhisBasicToken), sub1.getFormName()));
                            dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub1.getScoreCode(), dhisBasicToken), String.valueOf(getScore(num, den, Integer.parseInt(sub1.getMaxScore())))));
                            dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub1.getMaxCode(), dhisBasicToken), sub1.getMaxScore()));

                            sub1.setProcessed(Boolean.TRUE);
                            submissionDao.save(sub1);
                            sub2.setProcessed(Boolean.TRUE);
                            submissionDao.save(sub2);

                            DataValueGroup dvg = new DataValueGroup();
                            dvg.setDataSend(Boolean.FALSE);
                            dvg.setDataSet(getEntityIdFromCode("dataSets", OdkForm.valueOf(formId1).getDataSetId(), dhisBasicToken));
                            dvg.setDateCollected(sub1.getDateCollected());
                            dvg.setOrgUnit(sub1.getOrgUnit());
                            dvg.setPeriod(sub1.getPeriod());
                            dvg.addDatavalues(dataValues);
                            // For summary
                            dvg.setOrgUnitCode(sub1.getOrgUnitCode());
                            dvg.setFormName(FormName.valueOf(OdkForm.valueOf(formId2).getDataSetId()));
                            dvg.setNumerator(getNumeratorScore(num, den, Integer.parseInt(sub1.getMaxScore())));
                            dvg.setDenominator(new BigDecimal(den));
                            dvg.setScore(getScore(num, den, Integer.parseInt(sub1.getMaxScore())));
                            dvg.setDataSendTosummary(Boolean.FALSE);
                            dvg.setRetries(0);
                            dataValueGroupDao.save(dvg);
                        }
                    }
                }

                getDataSetValuesListForSingleForm(formId1, dhisBasicToken);
                getDataSetValuesListForSingleForm(formId2, dhisBasicToken);

            } else if (!submissions1.isEmpty() && submissions2.isEmpty()) {
                getDataSetValuesListForSingleForm(formId1, dhisBasicToken);

            } else if (submissions1.isEmpty() && !submissions2.isEmpty()) {
                getDataSetValuesListForSingleForm(formId2, dhisBasicToken);

            } else {
                log.info("Nothing To Process for the 2 forms !!!!!!!!!!!!!");
            }

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void getDataSetValuesListForSingleForm(String formId, String dhisBasicToken) {

        try {
            List<Submission> submissions = submissionDao.getSubmissions(formId, null, null, null, Boolean.FALSE);
            submissions.forEach(sub -> {

                List<DatasetValue> dataValues = new ArrayList<>();
                dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getDenCode(), dhisBasicToken), sub.getDenominator()));
                dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getNumeCode(), dhisBasicToken), sub.getNumeratorScore()));
                dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getFormNameCode(), dhisBasicToken), sub.getFormName()));
                dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getScoreCode(), dhisBasicToken), sub.getScore()));
                dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", sub.getMaxCode(), dhisBasicToken), sub.getMaxScore()));
                sub.setProcessed(Boolean.TRUE);

                DataValueGroup dvg = new DataValueGroup();
                dvg.setDataSend(Boolean.FALSE);
                dvg.setDataSet(getEntityIdFromCode("dataSets", OdkForm.valueOf(formId).getDataSetId(), dhisBasicToken));
                dvg.setDateCollected(sub.getDateCollected());
                dvg.setOrgUnit(sub.getOrgUnit());
                dvg.setPeriod(sub.getPeriod());
                dvg.addDatavalues(dataValues);
                // For summary
                dvg.setOrgUnitCode(sub.getOrgUnitCode());
                dvg.setFormName(FormName.valueOf(OdkForm.valueOf(formId).getDataSetId()));
                dvg.setNumerator(new BigDecimal(sub.getNumeratorScore()));
                dvg.setDenominator(new BigDecimal(sub.getDenominator()));
                dvg.setScore(new BigDecimal(sub.getScore()));
                dvg.setDataSendTosummary(Boolean.FALSE);
                dvg.setRetries(0);

                try {
                    submissionDao.save(sub);
                    dataValueGroupDao.save(dvg);
                } catch (Exception ex) {
                    Logger.getLogger(SubmissionUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


/*
	 * The fields read from left to right are interpreted as follows. second minute
	 * hour day of month month day of week
     */
    
    
   //@Scheduled(cron ="10 55 * * * ?", zone = "Africa/Nairobi")
    //@Scheduled(cron = "10 25 07-23 * * ?", zone = "Africa/Nairobi")

    public void postDataFromODKTODHIS() throws Exception {
        try {
            if (internetConnectionIsAvailable()) {
                checkODKDHISServerHeartBeat();
                if (connDao.getConnectionStatus() != null
                        && connDao.getConnectionStatus().getOdkConnection().equals("YES")
                        && connDao.getConnectionStatus().getDhisConnection().equals("YES")) {
                    SendDataFromODKToDHIS2();
                }
            }
        } catch (JSONException e) {
            log.error("Error Occured ============>" + e.getMessage());
        }

    }

    protected void SendDataFromODKToDHIS2_old() throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            Date leo = cal.getTime();
            cal.add(Calendar.DATE, -35);
            Date firstDay = cal.getTime();

            while (firstDay.before(leo)) {
                List<DatasetValue> datavalues = dataValueDao.getDatasetValues(firstDay, Boolean.FALSE);
                if (!datavalues.isEmpty()) {
                    String jsonStringTobePostedToDHIS = getDataSetValuesForDHISPostRequest(datavalues);
                    log.info("JSON DATA===" + jsonStringTobePostedToDHIS);
                    //postDatasetValuesToDHIS(jsonStringTobePostedToDHIS, datavalues);
                }

                Calendar cal2 = Calendar.getInstance();
                cal2.setTime(firstDay);
                cal2.add(Calendar.DATE, 1);
                firstDay = cal2.getTime();

            }
        } catch (JSONException e) {
            log.error("Error Occured ============>" + e.getMessage());
        }
    }

    //@Scheduled(cron = "22 09 04 * * ?", zone = "Africa/Nairobi")
    public void checkServerHeartBeat() throws Exception {
        if (internetConnectionIsAvailable()) {
            checkODKDHISServerHeartBeat();
        }
    }

}
