/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.AdministratorReportsDaoImpl;
import com.odkdhis.model.dashboard.AdministratorReports;
import java.time.LocalDate;
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
public class AdminReportDataPull extends CommonUtil {

    @Autowired
    private AdministratorReportsDaoImpl adminReportsDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
    //not needed as per now @Scheduled(cron ="10 32 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionAdminReport() {
        try {
            administratorReport("Administrator_Report");
            //Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void administratorReport(String formId) throws Exception {
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
            if (adminReportsDao.getRecordByInstanceId(instanceId) != null) {
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

           // String orgUnit = String.valueOf(datajson.getInt("mflcode"));

            JSONObject jsonObjectFacilityProfile = null;
            if (!datajson.isNull("facility_profile")) {
                jsonObjectFacilityProfile = datajson.getJSONObject("facility_profile");
               log.info("This are the items in the object faccility profile @@@@@@@@@@@@@  " + jsonObjectFacilityProfile.toString()); 
            } 
            
           JSONObject jsonObjectPersonDetails = null;
            if (!datajson.isNull("facility_profile")) {
                jsonObjectPersonDetails = datajson.getJSONObject("Person");
               log.info("This are the items in the object person details %%%%%%%  " + jsonObjectPersonDetails.toString()); 
            } 
           
            
            
         //converting objects with one/single object into arrays    
          
          try{
            JSONObject jsonObjectStockStatus = null;
            if (!datajson.isNull("StockStatus")) {
                jsonObjectStockStatus = datajson.getJSONObject("StockStatus");
                JSONArray arrChanges = covertJsonObjectToJsonArray(jsonObjectStockStatus.get("Stock"));
                
                log.info("This is the stock status array of objects@@@@@@@@@@@@@QQQQFFFF "+arrChanges);
            
                for(int k=0; k<arrChanges.length(); k++){
               
                JSONObject jsonObjectstockData = arrChanges.getJSONObject(k);
               
               
                log.info("These are the objects from the converted array #####!!!!%%%% " + (k + 1) + ":  " + jsonObjectstockData.toString());
            
            String county = jsonObjectFacilityProfile.isNull("county") ? "" : jsonObjectFacilityProfile.getString("county");
            String subcounty = jsonObjectFacilityProfile.isNull("subCounty") ? "" : jsonObjectFacilityProfile.getString("subCounty");
            String facility = jsonObjectFacilityProfile.isNull("facility") ? "" : jsonObjectFacilityProfile.getString("facility");
            String facilitytype = jsonObjectFacilityProfile.isNull("facility_type") ? "" : jsonObjectFacilityProfile.getString("facility_type");
            
            String dept = String.valueOf(jsonObjectstockData.getString("department"));
            String commodityType = String.valueOf(jsonObjectstockData.getString("commodityType"));
            String commodity = String.valueOf(jsonObjectstockData.getString("commodity"));
            Integer instock = getIntegerFromJsonKey(jsonObjectstockData, "qtyinstock");
            Integer expiring6m = getIntegerFromJsonKey(jsonObjectstockData, "qtyexpiring6m");
            Integer expired = getIntegerFromJsonKey(jsonObjectstockData, "qtyexpired");
            String expireDate = String.valueOf(jsonObjectstockData.getString("expryDate"));
            String cremark = String.valueOf(jsonObjectstockData.getString("cRemarks"));
            //String cremark = jsonObjectstockData.isNull("cRemarks") ? "" : jsonObjectstockData.getString("cRemarks");
            
    
            String pname = jsonObjectPersonDetails.isNull("Name") ? "" : jsonObjectPersonDetails.getString("Name");
            String pcounty = jsonObjectPersonDetails.isNull("county1") ? "" : jsonObjectPersonDetails.getString("county1");
            String psubcounty = jsonObjectPersonDetails.isNull("subCounty1") ? "" : jsonObjectPersonDetails.getString("subCounty1");
            String tel = jsonObjectPersonDetails.isNull("telphone") ? "" : jsonObjectPersonDetails.getString("telphone");
            String agecategory = jsonObjectPersonDetails.isNull("agecategory") ? "" : jsonObjectPersonDetails.getString("agecategory");
            String email = jsonObjectPersonDetails.isNull("email") ? "" : jsonObjectPersonDetails.getString("email");
            
            String genRemarks = datajson.getString("remarks");
            
            AdministratorReports adminReport=new AdministratorReports.AdministratorReportsBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).subcounty(subcounty).facilityname(facility)
                    .facilitytype(facilitytype).department(dept).commodity(commodity).commoditytype(commodityType).qtyinstock(instock)
                    .qtyexpringin6months(expiring6m).qtyexpired(expired).earliestexpirydate(getDateFromString(expireDate))
                    .commodityremarks(cremark).pname(pname).personcounty(pcounty).phonenumber(tel).agecategory(agecategory)
                    .psubcounty(psubcounty).emailaddress(email).generalremarks(genRemarks).build();
            adminReportsDao.save(adminReport);
                
                
                }
 
               } 
           } catch(JSONException e){
            
        } 
            
            try{
            // if its an array then the below will be executed    
            JSONArray stockStatusArray=datajson.getJSONArray("StockStatus");
            log.info("This is the stock status array of objects@@@@@@@@@@@@@QQQQFFFF "+stockStatusArray);
            
            for(int k=0; k<stockStatusArray.length(); k++){
               
               JSONObject jsonObjectstock = stockStatusArray.getJSONObject(k);
                   
               JSONObject jsonObjectstockData = jsonObjectstock.getJSONObject("Stock");            
               log.info("This are the object in the arrayZZZZZZZZZZZZZ " + (k + 1) + ":  " + jsonObjectstockData.toString());
               
               
            
            String county = jsonObjectFacilityProfile.isNull("county") ? "" : jsonObjectFacilityProfile.getString("county");
            String subcounty = jsonObjectFacilityProfile.isNull("subCounty") ? "" : jsonObjectFacilityProfile.getString("subCounty");
            String facility = jsonObjectFacilityProfile.isNull("facility") ? "" : jsonObjectFacilityProfile.getString("facility");
            String facilitytype = jsonObjectFacilityProfile.isNull("facility_type") ? "" : jsonObjectFacilityProfile.getString("facility_type");
            
            String dept = String.valueOf(jsonObjectstockData.getString("department"));
            String commodityType = String.valueOf(jsonObjectstockData.getString("commodityType"));
            String commodity = String.valueOf(jsonObjectstockData.getString("commodity"));
            Integer instock = getIntegerFromJsonKey(jsonObjectstockData, "qtyinstock");
            Integer expiring6m = getIntegerFromJsonKey(jsonObjectstockData, "qtyexpiring6m");
            Integer expired = getIntegerFromJsonKey(jsonObjectstockData, "qtyexpired");
            String expireDate = String.valueOf(jsonObjectstockData.getString("expryDate"));
            String cremark = String.valueOf(jsonObjectstockData.getString("cRemarks"));
            //String cremark = jsonObjectstockData.isNull("cRemarks") ? "" : jsonObjectstockData.getString("cRemarks");
            
    
            String pname = jsonObjectPersonDetails.isNull("Name") ? "" : jsonObjectPersonDetails.getString("Name");
            String pcounty = jsonObjectPersonDetails.isNull("county1") ? "" : jsonObjectPersonDetails.getString("county1");
            String psubcounty = jsonObjectPersonDetails.isNull("subCounty1") ? "" : jsonObjectPersonDetails.getString("subCounty1");
            String tel = jsonObjectPersonDetails.isNull("telphone") ? "" : jsonObjectPersonDetails.getString("telphone");
            String agecategory = jsonObjectPersonDetails.isNull("agecategory") ? "" : jsonObjectPersonDetails.getString("agecategory");
            String email = jsonObjectPersonDetails.isNull("email") ? "" : jsonObjectPersonDetails.getString("email");
            
            String genRemarks = datajson.getString("remarks");
            
            AdministratorReports adminReport=new AdministratorReports.AdministratorReportsBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).subcounty(subcounty).facilityname(facility)
                    .facilitytype(facilitytype).department(dept).commodity(commodity).commoditytype(commodityType).qtyinstock(instock)
                    .qtyexpringin6months(expiring6m).qtyexpired(expired).earliestexpirydate(getDateFromString(expireDate))
                    .commodityremarks(cremark).pname(pname).personcounty(pcounty).phonenumber(tel).agecategory(agecategory)
                    .psubcounty(psubcounty).emailaddress(email).generalremarks(genRemarks).build();
            adminReportsDao.save(adminReport);
            
            }
        
          } catch(JSONException e){
            
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

        } catch (Exception e) {
           log.error("Error Occured ====" + e.getMessage());
        }
        return 0;
    }
    
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
