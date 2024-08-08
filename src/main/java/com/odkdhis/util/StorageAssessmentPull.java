/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.StorageAssessmentDaoImple;
import com.odkdhis.model.dashboard.StorageAssessmentModel;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Paul Omboi
 */
@Component
public class StorageAssessmentPull extends CommonUtil {

    @Autowired
    private StorageAssessmentDaoImple storageAssessmentDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
    @Scheduled(cron ="10 45 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionStorage() {
        try {
            basicStorageData("storage_area_assesment2");
            //Thread.sleep(2000);
            addendaStorageData("storage_area_assesment_org");

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void basicStorageData(String formId) throws Exception {
        log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVv " + formId);

        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID + "/forms/" + formId + "/submissions";
        // Create request headers
        HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("JSON", odkToken));
        // Gets All submissions in summary
        JSONArray jArray = new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());
        log.info("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW " + jArray.toString());
        // Now loop through the summary array to process each submission 
        for (int i = 0; i < jArray.length(); i++) {

            // Get a JSON object 
            JSONObject instance = jArray.getJSONObject(i);
            //logging instance json object
            log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Json Object for form number " + (i + 1) + ":  " + instance.toString());
            // Get the instance ID of the submission xml file
            String instanceId = instance.getString("instanceId");

            //checks if instances have already been saved.
            if (storageAssessmentDao.getRecordByInstanceId(instanceId) != null) {
                log.info("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ>>>>>>" + instanceId);
                continue;
            }
            // Logging instance Id
            log.info("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZzzzzzzzInstance Id:" + instanceId);

            //Get the one submission data file(in xml format) by it's instance ID
            String xmlString = getSubmissionInstanceXml(PROJECTID, formId, instanceId, odkToken);

            log.info("**************************************************************************" + xmlString);

            if (Objects.isNull(xmlString)) {
                continue;
            }

            // Convert submission XML data to JSON for easy parsing
            JSONObject jsonObj = XML.toJSONObject(xmlString);
            String json = jsonObj.toString(INDENTATION);
            JSONObject instancejson = new JSONObject(json);
            JSONObject datajson = instancejson.getJSONObject("data");

            log.info("####################################%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5 Json Object for form number " + (i + 1) + ":  " + datajson.toString());

            //Now you have a well formatted json file and can start parsing it for values.
            //Get the date the data was collected which is called "today"
            String instanceCollectedDate = datajson.getString("today");//error

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));

            JSONObject jsonObjectCountyFacility = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectCountyFacility = datajson.getJSONObject("facility_information");
            }
            JSONObject jsonObjectStorageAssessment = null;
            if (!datajson.isNull("Storage_Assessment")) {
                jsonObjectStorageAssessment = datajson.getJSONObject("Storage_Assessment");
            }
             


            Integer empty = 0;

            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String storeType = jsonObjectStorageAssessment.isNull("storeNumber") ? "" : jsonObjectStorageAssessment.getString("storeNumber");
            Integer storeCleaning = getIntegerFromJsonKey(jsonObjectStorageAssessment, "storecleaning");
            Integer dSunlight = getIntegerFromJsonKey(jsonObjectStorageAssessment, "directsunlightmoisture");
            Integer vermin = getIntegerFromJsonKey(jsonObjectStorageAssessment, "vermin");
            Integer shelvelPallets = getIntegerFromJsonKey(jsonObjectStorageAssessment, "shelvespallets");
            Integer lighting = getIntegerFromJsonKey(jsonObjectStorageAssessment, "storelighting");
            Integer functioningThermomenter = getIntegerFromJsonKey(datajson, "thermometerfunction");
            Integer tempchart = getIntegerFromJsonKey(jsonObjectStorageAssessment, "thermometerfunction");
            Integer functioningThermomenter2 = getIntegerFromJsonKey(jsonObjectStorageAssessment, "functioningthermometer");
            Integer tempchart2 = getIntegerFromJsonKey(jsonObjectStorageAssessment, "functioningthermometer");
            Integer arrangement = getIntegerFromJsonKey(jsonObjectStorageAssessment, "arrowarrangement");
            Integer hazardoursMaterial = getIntegerFromJsonKey(jsonObjectStorageAssessment, "hazardousmaterial");
            Integer burglarproof = getIntegerFromJsonKey(jsonObjectStorageAssessment, "burglarproof");
            Integer expiredseparated = getIntegerFromJsonKey(jsonObjectStorageAssessment, "burglarproof");
            Integer f058 = getIntegerFromJsonKey(jsonObjectStorageAssessment, "registerFO58");
            Integer bucket = getIntegerFromJsonKey(jsonObjectStorageAssessment, "sandbucket");
            Integer numerator = getIntegerFromJsonKey(jsonObjectStorageAssessment, "storageAssessmentTotal");
            Integer denominator = getIntegerFromJsonKey(jsonObjectStorageAssessment, "cdenominator");
            Integer score = getIntegerFromJsonKey(jsonObjectStorageAssessment, "ppercentage");
            
            

            StorageAssessmentModel storage = new StorageAssessmentModel.storageAssessmentsBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).subcounty(subcounty).facilityname(facility)
                    .storeType(storeType).orgUnit(orgUnit).storeCleaning(storeCleaning).arrangement(arrangement).dSunlight(dSunlight).vermin(vermin).shelvelPallets(shelvelPallets).lighting(lighting)
                    .functioningThermomenter(functioningThermomenter).tempchart(tempchart).functioningThermomenter2(functioningThermomenter2).tempchart2(tempchart2).hazardoursMaterial(hazardoursMaterial)
                    .burglarproof(burglarproof).expiredseparated(expiredseparated).f058(f058).bucket(bucket).numerator(numerator).denominator(denominator).score(score).build();
            try {
                storageAssessmentDao.save(storage);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + storage);

        }


    }
    
    //Storage basic form
 private void addendaStorageData(String formId) throws Exception {
        log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVv " + formId);

        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID + "/forms/" + formId + "/submissions";
        // Create request headers
        HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("JSON", odkToken));
        // Gets All submissions in summary
        JSONArray jArray = new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());
        log.info("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW " + jArray.toString());
        // Now loop through the summary array to process each submission 
        for (int i = 0; i < jArray.length(); i++) {

            // Get a JSON object 
            JSONObject instance = jArray.getJSONObject(i);
            //logging instance json object
            log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Json Object for form number " + (i + 1) + ":  " + instance.toString());
            // Get the instance ID of the submission xml file
            String instanceId = instance.getString("instanceId");

            //checks if instances have already been saved.
            if (storageAssessmentDao.getRecordByInstanceId(instanceId) != null) {
                log.info("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ>>>>>>" + instanceId);
                continue;
            }
            // Logging instance Id
            log.info("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZzzzzzzzInstance Id:" + instanceId);

            //Get the one submission data file(in xml format) by it's instance ID
            String xmlString = getSubmissionInstanceXml(PROJECTID, formId, instanceId, odkToken);

            log.info("**************************************************************************" + xmlString);

            if (Objects.isNull(xmlString)) {
                continue;
            }

            // Convert submission XML data to JSON for easy parsing
            JSONObject jsonObj = XML.toJSONObject(xmlString);
            String json = jsonObj.toString(INDENTATION);
            JSONObject instancejson = new JSONObject(json);
            JSONObject datajson = instancejson.getJSONObject("data");

            log.info("####################################%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5 Json Object for form number " + (i + 1) + ":  " + datajson.toString());

            //Now you have a well formatted json file and can start parsing it for values.
            //Get the date the data was collected which is called "today"
            String instanceCollectedDate = datajson.getString("today");//error

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));

            JSONObject jsonObjectCountyFacility = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectCountyFacility = datajson.getJSONObject("facility_information");
            }
            JSONObject jsonObjectStorageAssessment = null;
            if (!datajson.isNull("Storage_Assessment")) {
                jsonObjectStorageAssessment = datajson.getJSONObject("Storage_Assessment");
            }
             


            Integer empty = 0;

            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            //String storeType = jsonObjectStorageAssessment.isNull("storeNumber") ? "" : jsonObjectStorageAssessment.getString("storeNumber");
            Integer storeCleaning = getIntegerFromJsonKey(jsonObjectStorageAssessment, "storecleaning");
            Integer dSunlight = getIntegerFromJsonKey(jsonObjectStorageAssessment, "directsunlightmoisture");
            Integer vermin = getIntegerFromJsonKey(jsonObjectStorageAssessment, "vermin");
            Integer shelvelPallets = getIntegerFromJsonKey(jsonObjectStorageAssessment, "shelvespallets");
            Integer lighting = getIntegerFromJsonKey(jsonObjectStorageAssessment, "storelighting");
            Integer functioningThermomenter = getIntegerFromJsonKey(datajson, "thermometerfunction");
            Integer tempchart = getIntegerFromJsonKey(jsonObjectStorageAssessment, "thermometerfunction");
            Integer functioningThermomenter2 = getIntegerFromJsonKey(jsonObjectStorageAssessment, "functioningthermometer");
            Integer tempchart2 = getIntegerFromJsonKey(jsonObjectStorageAssessment, "functioningthermometer");
            Integer arrangement = getIntegerFromJsonKey(jsonObjectStorageAssessment, "arrowarrangement");
            Integer hazardoursMaterial = getIntegerFromJsonKey(jsonObjectStorageAssessment, "hazardousmaterial");
            Integer burglarproof = getIntegerFromJsonKey(jsonObjectStorageAssessment, "burglarproof");
            Integer expiredseparated = getIntegerFromJsonKey(jsonObjectStorageAssessment, "burglarproof");
            Integer f058 = getIntegerFromJsonKey(jsonObjectStorageAssessment, "registerFO58");
            Integer bucket = getIntegerFromJsonKey(jsonObjectStorageAssessment, "sandbucket");
            Integer numerator = getIntegerFromJsonKey(jsonObjectStorageAssessment, "storageAssessmentTotal");
            Integer denominator = getIntegerFromJsonKey(jsonObjectStorageAssessment, "cdenominator");
            Integer score = getIntegerFromJsonKey(jsonObjectStorageAssessment, "ppercentage");
            
            

            StorageAssessmentModel storage = new StorageAssessmentModel.storageAssessmentsBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).subcounty(subcounty).facilityname(facility)
                    .storeType("Basic").orgUnit(orgUnit).storeCleaning(storeCleaning).arrangement(arrangement).dSunlight(dSunlight).vermin(vermin).shelvelPallets(shelvelPallets).lighting(lighting)
                    .functioningThermomenter(functioningThermomenter).tempchart(tempchart).functioningThermomenter2(functioningThermomenter2).tempchart2(tempchart2).hazardoursMaterial(hazardoursMaterial)
                    .burglarproof(burglarproof).expiredseparated(expiredseparated).f058(f058).bucket(bucket).numerator(numerator).denominator(denominator).score(score).build();
            try {
                storageAssessmentDao.save(storage);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + storage);

        }


    }
    
    protected Integer getIntegerFromJsonKey(JSONObject object, String keyAttribute) {
        //String keyValue;
        //Integer keyInt;
        try {
            if (Objects.isNull(object)) {
                return 0;
            }
            return object.getInt(keyAttribute);

        } catch (JSONException e) {

        }
        return 0;
    }

}
