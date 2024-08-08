/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;


import com.odkdhis.dao.dashboard.ActionPointsReviewDaoImpl;
import com.odkdhis.dao.dashboard.CurrentActionPointsDaoImpl;
import com.odkdhis.model.dashboard.ActionPointsReviewModel;
import com.odkdhis.model.dashboard.CurrentActionPointsModel;
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
public class CurrentActionsPoints extends CommonUtil {

    @Autowired
    private CurrentActionPointsDaoImpl currentactionPointsDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
    @Scheduled(cron ="10 20 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionsCurrentActions() {
        try {
            currentActions("Action_Points");
            //Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void currentActions(String formId) throws Exception {
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
            if (currentactionPointsDao.getRecordByInstanceId(instanceId) != null) {
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

            log.info("####################################%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Json Object for form number " + (i + 1) + ":  " + datajson.toString());

            //Now you have a well formatted json file and can start parsing it for values.
            //Get the date the data was collected which is called "today"
            String instanceCollectedDate = datajson.getString("today");//error

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));

          
            JSONObject jsonObjectFacilityProfile = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectFacilityProfile = datajson.getJSONObject("facility_information");
               log.info("This are the items in the object faccility profile @@@@@@@@@@@@@  " + jsonObjectFacilityProfile.toString()); 
            } 


          try{
            JSONObject jsonObjectCurrentActionPoints = null;
            if (!datajson.isNull("action")) {
                jsonObjectCurrentActionPoints = datajson.getJSONObject("action");
                JSONArray arrChanges = covertJsonObjectToJsonArray(jsonObjectCurrentActionPoints.get("actionpoints"));
                
                log.info("This is the stock status array of objects@@@@@@@@@@@@@QQQQFFFF "+arrChanges);
            
                for(int k=0; k<arrChanges.length(); k++){
               
                JSONObject jsonObjectCurrentPoints = arrChanges.getJSONObject(k);
               
                log.info("These are the objects from the converted array #####!!!!%%%% " + (k + 1) + ":  " + jsonObjectCurrentPoints.toString());
  
            String county = jsonObjectFacilityProfile.isNull("county_facility") ? "" : jsonObjectFacilityProfile.getString("county_facility");
            String subcounty = jsonObjectFacilityProfile.isNull("subCounty") ? "" : jsonObjectFacilityProfile.getString("subCounty");
            String facility = jsonObjectFacilityProfile.isNull("facility") ? "" : jsonObjectFacilityProfile.getString("facility");
            String level = jsonObjectFacilityProfile.isNull("flevel") ? "" : jsonObjectFacilityProfile.getString("flevel");
            

            String smartactionpoint = jsonObjectCurrentPoints.isNull("smartactionpoint") ? "" : jsonObjectCurrentPoints.getString("smartactionpoint");
            String person = jsonObjectCurrentPoints.isNull("responsiblestaff") ? "" : jsonObjectCurrentPoints.getString("responsiblestaff");
            String duedate = jsonObjectCurrentPoints.isNull("bywhen") ? "" : jsonObjectCurrentPoints.getString("bywhen");
            

            
            CurrentActionPointsModel currentactionpoints=new CurrentActionPointsModel.currentActionPointsBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).subcounty(subcounty).facilityname(facility)
                    .flevel(level).orgUnit(orgUnit).actionpoint(smartactionpoint).presponsible(person).duedate(getDateFromString(duedate))
                    .build();
            currentactionPointsDao.save(currentactionpoints);
 
                }                       
               } 
            }
 
          catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
         }
          
          
          
          // second execution
          try{
                JSONArray arrChanges = datajson.getJSONArray("action");
                for (int j = 0; j < arrChanges.length(); j++) {
                    JSONObject jsonObjectCurrentPoints1 = arrChanges.getJSONObject(j);
                    JSONObject jsonObjectCurrentPoints = jsonObjectCurrentPoints1.getJSONObject("actionpoints");
  
            String county = jsonObjectFacilityProfile.isNull("county_facility") ? "" : jsonObjectFacilityProfile.getString("county_facility");
            String subcounty = jsonObjectFacilityProfile.isNull("subCounty") ? "" : jsonObjectFacilityProfile.getString("subCounty");
            String facility = jsonObjectFacilityProfile.isNull("facility") ? "" : jsonObjectFacilityProfile.getString("facility");
            String level = jsonObjectFacilityProfile.isNull("flevel") ? "" : jsonObjectFacilityProfile.getString("flevel");
            

            String smartactionpoint = jsonObjectCurrentPoints.isNull("smartactionpoint") ? "" : jsonObjectCurrentPoints.getString("smartactionpoint");
            String person = jsonObjectCurrentPoints.isNull("responsiblestaff") ? "" : jsonObjectCurrentPoints.getString("responsiblestaff");
            String duedate = jsonObjectCurrentPoints.isNull("bywhen") ? "" : jsonObjectCurrentPoints.getString("bywhen");
            

            
            CurrentActionPointsModel currentactionpoints=new CurrentActionPointsModel.currentActionPointsBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).subcounty(subcounty).facilityname(facility)
                    .flevel(level).orgUnit(orgUnit).actionpoint(smartactionpoint).presponsible(person).duedate(getDateFromString(duedate))
                    .build();
            currentactionPointsDao.save(currentactionpoints);
 
                }                       
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
