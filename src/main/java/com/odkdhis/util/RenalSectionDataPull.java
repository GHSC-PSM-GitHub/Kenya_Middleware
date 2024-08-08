/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.InventoryManagementDaoImpl;
import com.odkdhis.dao.dashboard.RenalSectionDaoImpl;
import com.odkdhis.model.dashboard.InventoryManagement;
import com.odkdhis.model.dashboard.RenalSectionModel;
import java.util.Objects;
import java.util.Optional;
import javassist.bytecode.stackmap.BasicBlock;
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
public class RenalSectionDataPull extends CommonUtil {

    @Autowired
    private RenalSectionDaoImpl renalSectionDao2;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
    //@Scheduled(cron ="10 04 * * * ?", zone = "Africa/Nairobi")
    @Scheduled(cron ="20 * * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionRenalSection() {
        try {
            renalSectionform("mnch");
            //Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void renalSectionform(String formId) throws Exception {
        log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVv " + formId);

        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID3 + "/forms/" + formId + "/submissions";
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
            if (renalSectionDao.getRecordByInstanceId(instanceId) != null) {
                continue;
            }
            // Logging instance Id
            log.info("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZzzzzzzzInstance Id:" + instanceId);

            //Get the one submission data file(in xml format) by it's instance ID
            String xmlString = getSubmissionInstanceXml(PROJECTID3, formId, instanceId, odkToken);

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

            JSONObject jsonObjectCountyFacility = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectCountyFacility = datajson.getJSONObject("facility_information");
            }
            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");
            String flevel = jsonObjectCountyFacility.isNull("flevel") ? "" : jsonObjectCountyFacility.getString("flevel");
            String preparedby = jsonObjectCountyFacility.isNull("preparedby") ? "" : jsonObjectCountyFacility.getString("preparedby");
            Integer noofmonths = getIntegerFromJsonKey(jsonObjectCountyFacility, "noofmonths");
            Integer bufferperiod = getIntegerFromJsonKey(jsonObjectCountyFacility, "bufferperiod");
            String periodfrom = jsonObjectCountyFacility.isNull("periodfrom") ? "" : jsonObjectCountyFacility.getString("periodfrom");
            String periodto = jsonObjectCountyFacility.isNull("periodto") ? "" : jsonObjectCountyFacility.getString("periodto");

            
            
            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));
           
            JSONObject jsonObjSRenalSection = null;
           
           try{
               if (!datajson.isNull("product_group")) {
                jsonObjSRenalSection = datajson.getJSONObject("product_group");
                JSONArray arrChanges = covertJsonObjectToJsonArray(jsonObjSRenalSection.get("product_list"));
                
                log.info("This is the stock status array of objects@@@@@@@@@@@@@QQQQFFFF "+arrChanges);
            
                for(int k=0; k<arrChanges.length(); k++){
               
                JSONObject jsonObjectRenal = arrChanges.getJSONObject(k);
                //JSONObject jsonObjectRenal2 = jsonObjectRenal.getJSONObject("product_list");                
               

                   log.info("These are the objects from the converted array #####!!!!%%%% " + (k + 1) + ":  " + jsonObjectRenal.toString());
                                       
                    String product_name = String.valueOf(jsonObjSRenalSection.getString("product_name"));
                    //Integer denominator = (datajson.isNull("totalDenominator")) ? empty : datajson.getInt("totalDenominator");
                    //String formtype = datajson.isNull("formtype") ? "" : String.valueOf(datajson.getString("formtype"));
                    String essential = String.valueOf(jsonObjectRenal.getString("essential"));
                    String lou = String.valueOf(jsonObjectRenal.getString("lou"));
                    String ven = String.valueOf(jsonObjectRenal.getString("ven"));
                    String funding = String.valueOf(jsonObjectRenal.getString("funding"));
                    String subcategory = String.valueOf(jsonObjectRenal.getString("subcategory"));
                    Integer price = getIntegerFromJsonKey(jsonObjectRenal, "price");
                    Integer serialno = getIntegerFromJsonKey(jsonObjectRenal, "product_post");
                    Integer jandata = getIntegerFromJsonKey(jsonObjectRenal, "jandata");
                    Integer febdata = getIntegerFromJsonKey(jsonObjectRenal, "febdata");
                    Integer mardata = getIntegerFromJsonKey(jsonObjectRenal, "mardata");
                    Integer aprdata = getIntegerFromJsonKey(jsonObjectRenal, "aprdata");
                    Integer maydata = getIntegerFromJsonKey(jsonObjectRenal, "maydata");
                    Integer jundata = getIntegerFromJsonKey(jsonObjectRenal, "jundata");
                    Integer juldata = getIntegerFromJsonKey(jsonObjectRenal, "juldata");
                    Integer augdata = getIntegerFromJsonKey(jsonObjectRenal, "augdata");
                    Integer sepdata = getIntegerFromJsonKey(jsonObjectRenal, "sepdata");
                    Integer octdata = getIntegerFromJsonKey(jsonObjectRenal, "octdata");
                    Integer novdata = getIntegerFromJsonKey(jsonObjectRenal, "novdata");
                    Integer decdata = getIntegerFromJsonKey(jsonObjectRenal, "decdata");

                    //one record to the dbase
                    RenalSectionModel renalSectionData = new RenalSectionModel.RenalSectionPointsBuilder().instanceId(instanceId).instanceId(instanceId)
                            .dateCollected(getDateFromString(instanceCollectedDate)).price(price).product_name(product_name).formtype("Renal")
                            .essential(essential).lou(lou).ven(ven).funding(funding).subcategory(subcategory)
                            .jandata(jandata).febdata(febdata).mardata(mardata).aprdata(aprdata).maydata(maydata).jundata(jundata)
                            .juldata(juldata).augdata(augdata).sepdata(sepdata).octdata(octdata).novdata(novdata).decdata(decdata)
                            .orgUnit(orgUnit).county(county).facilityname(facility).flevel(flevel).subcounty(subcounty)
                            .periodfrom(getDateFromString(periodfrom)).periodto(getDateFromString(periodto))
                            .mcovered(noofmonths).mstock(bufferperiod).preparedby(preparedby).serialno(serialno)
                            .build();
                            

                    renalSectionDao2.save(renalSectionData);
                    
                }
                
               }
            } catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
            }
           
              try{  
                JSONArray jsonArrayRenalSection = datajson.getJSONArray("product_group");
                
                for (int j = 0; j < jsonArrayRenalSection.length(); j++) {
                    JSONObject jsonObjectRenal = jsonArrayRenalSection.getJSONObject(j);
                    JSONObject jsonObjectRenal2 = jsonObjectRenal.getJSONObject("product_list");

                    String product_name = String.valueOf(jsonObjectRenal.getString("product_name"));
                    String formtype = datajson.isNull("formtype") ? "" : String.valueOf(datajson.getString("formtype"));
                    String essential = String.valueOf(jsonObjectRenal2.getString("essential"));
                    String lou = String.valueOf(jsonObjectRenal2.getString("lou"));
                    String ven = String.valueOf(jsonObjectRenal2.getString("ven"));
                    String funding = String.valueOf(jsonObjectRenal2.getString("funding"));
                    String subcategory = String.valueOf(jsonObjectRenal2.getString("subcategory"));
                    Integer price = getIntegerFromJsonKey(jsonObjectRenal2, "price");
                    Integer serialno = getIntegerFromJsonKey(jsonObjectRenal, "product_post");
                    Integer jandata = getIntegerFromJsonKey(jsonObjectRenal2, "jandata");
                    Integer febdata = getIntegerFromJsonKey(jsonObjectRenal2, "febdata");
                    Integer mardata = getIntegerFromJsonKey(jsonObjectRenal2, "mardata");
                    Integer aprdata = getIntegerFromJsonKey(jsonObjectRenal2, "aprdata");
                    Integer maydata = getIntegerFromJsonKey(jsonObjectRenal2, "maydata");
                    Integer jundata = getIntegerFromJsonKey(jsonObjectRenal2, "jundata");
                    Integer juldata = getIntegerFromJsonKey(jsonObjectRenal2, "juldata");
                    Integer augdata = getIntegerFromJsonKey(jsonObjectRenal2, "augdata");
                    Integer sepdata = getIntegerFromJsonKey(jsonObjectRenal2, "sepdata");
                    Integer octdata = getIntegerFromJsonKey(jsonObjectRenal2, "octdata");
                    Integer novdata = getIntegerFromJsonKey(jsonObjectRenal2, "novdata");
                    Integer decdata = getIntegerFromJsonKey(jsonObjectRenal2, "decdata");

                    //one record to the dbase
                    RenalSectionModel renalSectionData = new RenalSectionModel.RenalSectionPointsBuilder().instanceId(instanceId).instanceId(instanceId)
                            .dateCollected(getDateFromString(instanceCollectedDate)).price(price).product_name(product_name).formtype("Renal")
                            .essential(essential).lou(lou).ven(ven).funding(funding).subcategory(subcategory)
                            .jandata(jandata).febdata(febdata).mardata(mardata).aprdata(aprdata).maydata(maydata).jundata(jundata)
                            .juldata(juldata).augdata(augdata).sepdata(sepdata).octdata(octdata).novdata(novdata).decdata(decdata)
                            .orgUnit(orgUnit).county(county).facilityname(facility).flevel(flevel).subcounty(subcounty)
                            .periodfrom(getDateFromString(periodfrom)).periodto(getDateFromString(periodto))
                            .mcovered(noofmonths).mstock(bufferperiod).preparedby(preparedby).serialno(serialno) 
                            .build();
                    renalSectionDao2.save(renalSectionData);
                }

            } catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
            }

              
            try{
               if (!datajson.isNull("product_group2")) {
                jsonObjSRenalSection = datajson.getJSONObject("product_group2");
                JSONArray arrChanges = covertJsonObjectToJsonArray(jsonObjSRenalSection.get("product_group2"));
                
                log.info("This is the stock status array of objects@@@@@@@@@@@@@QQQQFFFF "+arrChanges);
            
                for(int k=0; k<arrChanges.length(); k++){
               
                JSONObject jsonObjectRenal = arrChanges.getJSONObject(k);
                //JSONObject jsonObjectRenal2 = jsonObjectRenal.getJSONObject("product_list2");                
               

                   log.info("These are the objects from the converted array #####!!!!%%%% " + (k + 1) + ":  " + jsonObjectRenal.toString());
                                       
                    String product_name = String.valueOf(jsonObjSRenalSection.getString("product_name2"));
                    String formtype = datajson.isNull("formtype") ? "" : String.valueOf(datajson.getString("formtype"));
                    String essential = String.valueOf(jsonObjectRenal.getString("essential2"));
                    String lou = String.valueOf(jsonObjectRenal.getString("lou2"));
                    String ven = String.valueOf(jsonObjectRenal.getString("ven2"));
                    String funding = String.valueOf(jsonObjectRenal.getString("funding2"));
                    String subcategory = String.valueOf(jsonObjectRenal.getString("subcategory2"));
                    Integer price = getIntegerFromJsonKey(jsonObjectRenal, "price2");
                    Integer serialno = getIntegerFromJsonKey(jsonObjectRenal, "product_post2");
                    //Integer jandata = getIntegerFromJsonKey(jsonObjectRenal2, "jandata2");
                   //Integer febdata = getIntegerFromJsonKey(jsonObjectRenal2, "febdata2");
                    //Integer mardata = getIntegerFromJsonKey(jsonObjectRenal2, "mardata2");
                    //Integer aprdata = getIntegerFromJsonKey(jsonObjectRenal2, "aprdata2");
                    //Integer maydata = getIntegerFromJsonKey(jsonObjectRenal2, "maydata2");
                   // Integer jundata = getIntegerFromJsonKey(jsonObjectRenal2, "jundata2");
                   // Integer juldata = getIntegerFromJsonKey(jsonObjectRenal2, "juldata2");
                   // Integer augdata = getIntegerFromJsonKey(jsonObjectRenal2, "augdata2");
                    //Integer sepdata = getIntegerFromJsonKey(jsonObjectRenal2, "sepdata2");
                   // Integer octdata = getIntegerFromJsonKey(jsonObjectRenal2, "octdata2");
                    //Integer novdata = getIntegerFromJsonKey(jsonObjectRenal2, "novdata2");
                    //Integer decdata = getIntegerFromJsonKey(jsonObjectRenal2, "decdata2");

                    //one record to the dbase
                    RenalSectionModel renalSectionData = new RenalSectionModel.RenalSectionPointsBuilder().instanceId(instanceId).instanceId(instanceId)
                            .dateCollected(getDateFromString(instanceCollectedDate)).price(price).product_name(product_name).formtype(formtype)
                            .essential(essential).lou(lou).ven(ven).funding(funding).subcategory(subcategory)
                            .jandata(0).febdata(0).mardata(0).aprdata(0).maydata(0).jundata(0)
                            .juldata(0).augdata(0).sepdata(0).octdata(0).novdata(0).decdata(0)
                            .orgUnit(orgUnit).county(county).facilityname(facility).flevel(flevel).subcounty(subcounty)
                            .periodfrom(getDateFromString(periodfrom)).periodto(getDateFromString(periodto))
                            .mcovered(noofmonths).mstock(bufferperiod).preparedby(preparedby).serialno(serialno) 
                            .build();

                    renalSectionDao2.save(renalSectionData);
                    
                }
                
               }
            } catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
            }
           
              try{  
                JSONArray jsonArrayRenalSection = datajson.getJSONArray("product_group2");
                
                for (int j = 0; j < jsonArrayRenalSection.length(); j++) {
                    JSONObject jsonObjectRenal = jsonArrayRenalSection.getJSONObject(j);
                    //JSONObject jsonObjectRenal2 = jsonObjectRenal.getJSONObject("product_list");
                    
                    
                    log.info("Array Identification...... #####!!!!%%%% " + (j + 1) + ":  " + jsonArrayRenalSection.toString());
                    log.info("These are the objects from the converted array #####!!!!%%%% " + (j + 1) + ":  " + jsonObjectRenal.toString());
                    //log.info("These are the objects for object 2 for testing #####!!!!%%%% " + (j + 1) + ":  " + jsonObjectRenal2.toString());   
                    
                    String product_name = String.valueOf(jsonObjectRenal.getString("product_name2"));
                    String formtype = datajson.isNull("formtype") ? "" : String.valueOf(datajson.getString("formtype"));
                    String essential = String.valueOf(jsonObjectRenal.getString("essential2"));
                    String lou = String.valueOf(jsonObjectRenal.getString("lou2"));
                    String ven = String.valueOf(jsonObjectRenal.getString("ven2"));
                    String funding = String.valueOf(jsonObjectRenal.getString("funding2"));
                    String subcategory = String.valueOf(jsonObjectRenal.getString("subcategory2"));
                    Integer price = getIntegerFromJsonKey(jsonObjectRenal, "price2");
                    Integer serialno = getIntegerFromJsonKey(jsonObjectRenal, "product_post2");
                    //Integer jandata = getIntegerFromJsonKey(jsonObjectRenal2, "jandata2");
                   //Integer febdata = getIntegerFromJsonKey(jsonObjectRenal2, "febdata2");
                    //Integer mardata = getIntegerFromJsonKey(jsonObjectRenal2, "mardata2");
                    //Integer aprdata = getIntegerFromJsonKey(jsonObjectRenal2, "aprdata2");
                    //Integer maydata = getIntegerFromJsonKey(jsonObjectRenal2, "maydata2");
                   // Integer jundata = getIntegerFromJsonKey(jsonObjectRenal2, "jundata2");
                   // Integer juldata = getIntegerFromJsonKey(jsonObjectRenal2, "juldata2");
                   // Integer augdata = getIntegerFromJsonKey(jsonObjectRenal2, "augdata2");
                    //Integer sepdata = getIntegerFromJsonKey(jsonObjectRenal2, "sepdata2");
                   // Integer octdata = getIntegerFromJsonKey(jsonObjectRenal2, "octdata2");
                    //Integer novdata = getIntegerFromJsonKey(jsonObjectRenal2, "novdata2");
                    //Integer decdata = getIntegerFromJsonKey(jsonObjectRenal2, "decdata2");

                    //one record to the dbase
                    RenalSectionModel renalSectionData = new RenalSectionModel.RenalSectionPointsBuilder().instanceId(instanceId).instanceId(instanceId)
                            .dateCollected(getDateFromString(instanceCollectedDate)).price(price).product_name(product_name).formtype("Renal")
                            .essential(essential).lou(lou).ven(ven).funding(funding).subcategory(subcategory)
                            .jandata(0).febdata(0).mardata(0).aprdata(0).maydata(0).jundata(0)
                            .juldata(0).augdata(0).sepdata(0).octdata(0).novdata(0).decdata(0)
                            .orgUnit(orgUnit).county(county).facilityname(facility).flevel(flevel).subcounty(subcounty)
                            .periodfrom(getDateFromString(periodfrom)).periodto(getDateFromString(periodto))
                            .mcovered(noofmonths).mstock(bufferperiod).preparedby(preparedby).serialno(serialno) 
                            .build();

                    renalSectionDao2.save(renalSectionData);
                }

            } catch (Exception e) {
                log.error("Error Occured on this section ====" + e.getMessage());
            }          
        }

    }
    public static String orElse(String defaultValue) {
         return Optional.ofNullable(System.getProperty("property")).orElse(defaultValue);
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
