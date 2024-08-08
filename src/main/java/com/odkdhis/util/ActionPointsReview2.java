/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;


import com.odkdhis.dao.dashboard.ActionPointsReviewDaoImpl;
import com.odkdhis.model.dashboard.ActionPointsReviewModel;
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
public class ActionPointsReview2 extends CommonUtil {

    @Autowired
    private ActionPointsReviewDaoImpl actionPointsReviewDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
   @Scheduled(cron ="10 01 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionInventory() {
        try {
            reviewOfPreviousActionPoints("Facility_Profile_Information");
            //Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void reviewOfPreviousActionPoints(String formId) throws Exception {
        log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVv " + formId);

        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID7 + "/forms/" + formId + "/submissions";
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
            if (actionPointsReviewDao.getRecordByInstanceId(instanceId) != null) {
                log.info("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ>>>>>>" + instanceId);
                continue;
            }
            // Logging instance Id
            log.info("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZzzzzzzzInstance Id:" + instanceId);

            //Get the one submission data file(in xml format) by it's instance ID
            String xmlString = getSubmissionInstanceXml(PROJECTID7, formId, instanceId, odkToken);

            log.info("**************************************************************************" + xmlString);

            if (Objects.isNull(xmlString)) {
                continue;
            }

            // Convert submission XML data to JSON for easy parsing
            JSONObject jsonObj = XML.toJSONObject(xmlString);
            String json = jsonObj.toString(INDENTATION);
            JSONObject instancejson = new JSONObject(json);
            JSONObject datajson = instancejson.getJSONObject("data");

            log.info("####################################%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Json Object for form number " + (i + 1) + ":  " + datajson.toString());

            //Now you have a well formatted json file and can start parsing it for values.
            //Get the date the data was collected which is called "today"
            String instanceCollectedDate = datajson.getString("today");//error

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));
            
            String facilitygps =datajson.getString("facility_gps");

                    
            JSONObject jsonObjectFacilityProfile = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectFacilityProfile = datajson.getJSONObject("facility_information");
               log.info("This are the items in the object faccility profile @@@@@@@@@@@@@  " + jsonObjectFacilityProfile.toString()); 
            } 
            JSONObject jsonObjectInchargeDetails = null;
            if (!datajson.isNull("facility_staff1")) {
                jsonObjectInchargeDetails = datajson.getJSONObject("facility_staff1");
               log.info("This are the items in the object faccility incharge %%%%%%%%%5  " + jsonObjectInchargeDetails.toString()); 
            }

          try{
              
            String county = jsonObjectFacilityProfile.isNull("county_facility") ? "" : jsonObjectFacilityProfile.getString("county_facility");
            String subcounty = jsonObjectFacilityProfile.isNull("subCounty") ? "" : jsonObjectFacilityProfile.getString("subCounty");
            String facility = jsonObjectFacilityProfile.isNull("facility") ? "" : jsonObjectFacilityProfile.getString("facility");
            String level = jsonObjectFacilityProfile.isNull("flevel") ? "" : jsonObjectFacilityProfile.getString("flevel");
            

            String inchargeNames = jsonObjectInchargeDetails.isNull("staff_name") ? "" : jsonObjectInchargeDetails.getString("staff_name");
            String inchargeEmail = jsonObjectInchargeDetails.isNull("email_adress") ? "" : jsonObjectInchargeDetails.getString("email_adress");
            String phonenumber = jsonObjectInchargeDetails.isNull("phone_number") ? "" : jsonObjectInchargeDetails.getString("phone_number");

            
            ActionPointsReviewModel previousPointsReview=new ActionPointsReviewModel.previousActionPointsBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).subcounty(subcounty).facilityname(facility)
                    .flevel(level).orgUnit(orgUnit).gpscordinates(facilitygps).inchargeNames(inchargeNames)
                    .inchargeEmail(inchargeEmail).phonenumber(phonenumber)
                    .build();
            actionPointsReviewDao.save(previousPointsReview);
 
                                      
            } 
            
 
          catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
         }         
     
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
   
  // Method to change objects to arrays
    public  JSONArray covertJsonObjectToJsonArray(Object InsideArray) {

    JSONArray jsonArray;

    if (InsideArray instanceof JSONArray) {
        jsonArray = (JSONArray) InsideArray;
    } else {
        jsonArray = new JSONArray();
        jsonArray.put((JSONObject) InsideArray);
    }
    return jsonArray;
}

}
