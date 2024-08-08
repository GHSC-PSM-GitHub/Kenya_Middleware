/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.ProductAccountabilityDaoImpl;
import com.odkdhis.model.dashboard.ProductAccountability;
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
public class SampleDataPull extends CommonUtil {

    @Autowired
    private ProductAccountabilityDaoImpl productDao;

    //@Scheduled(cron = "10 40 12-13 * * ?", zone = "Africa/Nairobi")
   @Scheduled(cron ="10 28 * * * ?", zone = "Africa/Nairobi")
   // for developing @Scheduled(cron ="10 31 * * * ?", zone = "Africa/Nairobi")
    //@Scheduled(cron ="10 * * * * ?", zone = "Africa/Nairobi") 
    public void prepareSubmission() {
        try {
            basicSupplyChain("supply_chain_audit_v2");
            supplyChainSlim("supply_chain_audit_slim");
            Thread.sleep(2000);
            supplyChainAddenda("supply_chain_audit_Addenda");
            supplyChainHorizontal("supply_chain_audit_v3");

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void basicSupplyChain(String formId) throws Exception {
        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID + "/forms/" + formId + "/submissions";
        // Create request headers
        HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("JSON", odkToken));
        // Gets All submissions in summary
        JSONArray jArray = new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());

        // Now loop through the summary array to process each submission 
        for (int i = 0; i < jArray.length(); i++) {

            // Get a JSON object 
            JSONObject instance = jArray.getJSONObject(i);
            //logging instance json object
            log.info("Json Object for form number " + (i + 1) + ":  " + instance.toString());
            // Get the instance ID of the submission xml file
            String instanceId = instance.getString("instanceId");

             //checks if instances have already been saved.
            if (accountabilityDao.getProductAccountabilities(instanceId, null, null, null, null).size() > 0) {
                continue;
            }

            //Get the one submission data file(in xml format) by it's instance ID
            String xmlString = getSubmissionInstanceXml(PROJECTID, formId, instanceId, odkToken);

             if (Objects.isNull(xmlString)) {
                continue;
            }
            log.info("**************************************************************************" + xmlString);

            // Convert submission XML data to JSON for easy parsing
            JSONObject jsonObj = XML.toJSONObject(xmlString);
            String json = jsonObj.toString(INDENTATION);
            JSONObject instancejson = new JSONObject(json);
            JSONObject datajson = instancejson.getJSONObject("data");
            //log.info("Json Object for form number "+(i+1)+":  "+datajson.toString());

            //Now you have a well formatted json file and can start parsing it for values.
            //Get the date the data was collected which is called "today"
            String instanceCollectedDate = datajson.getString("today");//error

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));

            //JSONArray qtyhandArray=new JSONArray("qtyhand");
            //log.info("This is the qty at ahand " + qtyhandArray);
            //System.out.println(qtyhandArray);
   try{       
            JSONObject jsonObjectqtyHand = null;
            if (!datajson.isNull("qtyhand")) {
                jsonObjectqtyHand = datajson.getJSONObject("qtyhand");
            }

            JSONObject jsonObjectReceived = null;
            if (!datajson.isNull("issuesdata")) {
                jsonObjectReceived = datajson.getJSONObject("issuesdata");
            }

            JSONObject jsonObjectDispensed = null;
            if (!datajson.isNull("qtyDispensing")) {
                jsonObjectDispensed = datajson.getJSONObject("qtyDispensing");
            }
            JSONObject jsonObjectClsng = null;
            if (!datajson.isNull("physicalDAr")) {
                jsonObjectClsng = datajson.getJSONObject("physicalDAr");
            }
            JSONObject jsonObjectPosAdjust = null;
            if (!datajson.isNull("postiveAdjustments")) {
                jsonObjectPosAdjust = datajson.getJSONObject("postiveAdjustments");
            }
            JSONObject jsonObjectNegAdjust = null;
            if (!datajson.isNull("negativeconfirmed")) {
                jsonObjectNegAdjust = datajson.getJSONObject("negativeconfirmed");
            }
            JSONObject jsonObjectQtyDelivered = null;
            if (!datajson.isNull("qrynote")) {
                jsonObjectQtyDelivered = datajson.getJSONObject("qrynote");
            }
            JSONObject jsonObjectQtyonBincard = null;
            if (!datajson.isNull("bincard")) {
                jsonObjectQtyonBincard = datajson.getJSONObject("bincard");
            }            
            JSONObject jsonObjectCountyFacility = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectCountyFacility = datajson.getJSONObject("facility_information");
            }
            Integer empty=0;
                       
            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            //String dnoteAl = jsonObjectReceived.isNull("deliveryNumberAl") ? "Not Captured" : jsonObjectReceived.getString("deliveryNumberAl");
            //String dnoteAl = String.valueOf(jsonObjectReceived.get("deliveryNumberAl"));
            Integer al24AtHand = getIntegerFromJsonKey(jsonObjectqtyHand, "qtyHandAl");
            Integer al24Received = getIntegerFromJsonKey(jsonObjectReceived, "qtyIssuedAl");
            
            //Integer al24Received = (jsonObjectReceived.isNull("qtyIssuedAl")) ? 0 : jsonObjectReceived.getInt("qtyIssuedAl");
            Integer al24Dispensed = getIntegerFromJsonKey(jsonObjectDispensed, "totalQtyIssuedDispAreaAl");
            Integer al24PosAdjust = getIntegerFromJsonKey(jsonObjectPosAdjust, "pos_adjustmentAl");
            Integer al24Clsng = getIntegerFromJsonKey(jsonObjectClsng, "actualCountDarAl");
            Integer al24delivered = getIntegerFromJsonKey(jsonObjectQtyDelivered, "qtyDeliveredAl");
            Integer qtyonBincardal24 = getIntegerFromJsonKey(jsonObjectQtyonBincard, "qtyBinCardAl");
            Integer al24NegAdjust = getIntegerFromJsonKey(jsonObjectNegAdjust, "neg_adjustmentDarAl");
            String expectedAl = datajson.isNull("actualSOHExpectedAl") ? "" : String.valueOf(datajson.getString("actualSOHExpectedAl"));

            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1This expected results for testing%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  " + jsonObjectqtyHand);
            //log.info("This expected results for testing  " + expectedAl);

            //one record to the dbase
            ProductAccountability al24 = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(al24Clsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(al24NegAdjust).openSOH(al24AtHand).dnote("")
                    .orgUnit(orgUnit).positiveAdjust(al24PosAdjust).product("Al 24s").qtyDispensed(al24Dispensed)
                    .qtyOnBincard(qtyonBincardal24).qtyDelivered(al24delivered).receivedFromSCA(al24Received).totalSOH(0).sohExpected(expectedAl).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

              
            productDao.save(al24);
            
 
            //String dnoteDmpa = jsonObjectReceived.isNull("deliveryNumberDMPA") ? "" : jsonObjectReceived.getString("deliveryNumberDMPA");
            Integer dmpaAtHand = getIntegerFromJsonKey(jsonObjectqtyHand, "qtyHandDMPA");
            Integer dmpadeliverd = getIntegerFromJsonKey(jsonObjectQtyDelivered, "qtyDeliveredDMPA");
            Integer qtyonBincarddmpa = getIntegerFromJsonKey(jsonObjectQtyonBincard, "qtyBinCardDMPA");
            Integer dmpaReceived = getIntegerFromJsonKey(jsonObjectReceived, "qtyIssuedDMPA");
            Integer dmpaDispensed = getIntegerFromJsonKey(jsonObjectDispensed, "totalQtyIssuedDispAreaDMPA");
            Integer dmpaClsng = getIntegerFromJsonKey(jsonObjectClsng, "actualCountDarDMPA");
            Integer dmpaPosAdjust = getIntegerFromJsonKey(jsonObjectPosAdjust, "pos_adjustmentDMPA");
            Integer dmpaNegAdjust = getIntegerFromJsonKey(jsonObjectNegAdjust, "neg_adjustmentDarDMPA");
            String expectedDmpa = datajson.isNull("actualSOHExpectedDMPA") ? "" : String.valueOf(datajson.getString("actualSOHExpectedDMPA"));

            ProductAccountability DMPA = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(dmpaClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(dmpaNegAdjust).openSOH(dmpaAtHand).dnote("")
                    .orgUnit(orgUnit).positiveAdjust(dmpaPosAdjust).product("DMPA").qtyDispensed(dmpaDispensed)
                    .qtyOnBincard(qtyonBincarddmpa).qtyDelivered(dmpadeliverd).receivedFromSCA(dmpaReceived).totalSOH(0).sohExpected(expectedDmpa).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(DMPA);
            
            //String dnotetdf = jsonObjectReceived.isNull("deliveryNumberTDF") ? "" : jsonObjectReceived.getString("deliveryNumberTDF");
            Integer tdfAtHand = getIntegerFromJsonKey(jsonObjectqtyHand, "qtyHandTDF");
            Integer qtydelivered = getIntegerFromJsonKey(jsonObjectQtyDelivered, "qtyDeliveredTDF");
            Integer qtyonBincardtdf = getIntegerFromJsonKey(jsonObjectQtyonBincard, "qtyBinCardTDF");
            Integer tdfReceived = getIntegerFromJsonKey(jsonObjectReceived, "qtyIssuedTDF");
            Integer tdfDispensed = getIntegerFromJsonKey(jsonObjectDispensed, "totalQtyIssuedDispAreaTDF");
            Integer tdfClsng = getIntegerFromJsonKey(jsonObjectClsng, "actualCountDarTDF");
            Integer tdfPosAdjust = getIntegerFromJsonKey(jsonObjectPosAdjust, "pos_adjustmentTDF");
            Integer tdfNegAdjust = getIntegerFromJsonKey(jsonObjectNegAdjust, "neg_adjustmentDarTDF");
            String expectedTdf = datajson.isNull("actualSOHExpectedTDF") ? "" : String.valueOf(datajson.getString("actualSOHExpectedTDF"));

            ProductAccountability TDF = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(tdfClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(tdfNegAdjust).openSOH(tdfAtHand).dnote("")
                    .orgUnit(orgUnit).positiveAdjust(tdfPosAdjust).product("tdf3tcdtgadult").qtyDispensed(tdfDispensed)
                    .qtyOnBincard(qtyonBincardtdf).qtyDelivered(qtydelivered).receivedFromSCA(tdfReceived).totalSOH(0).sohExpected(expectedTdf).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(TDF);
            
            //String dnotehiv = jsonObjectReceived.isNull("deliveryNumberHIV") ? "" : jsonObjectReceived.getString("deliveryNumberHIV");
            Integer hivAtHand = getIntegerFromJsonKey(jsonObjectqtyHand, "qtyHandHIV");
            Integer qtydeliveredhiv = getIntegerFromJsonKey(jsonObjectQtyDelivered, "qtyDeliveredHIV");
            Integer qtyonBincardhiv = getIntegerFromJsonKey(jsonObjectQtyonBincard, "qtyBinCardHIV");
            Integer hivReceived = getIntegerFromJsonKey(jsonObjectReceived, "qtyIssuedHIV");
            Integer hivDispensed = getIntegerFromJsonKey(jsonObjectDispensed, "totalQtyIssuedDispAreaHIV");
            Integer hivClsng = getIntegerFromJsonKey(jsonObjectClsng, "actualCountDarHIV");
            Integer hivPosAdjust = getIntegerFromJsonKey(jsonObjectPosAdjust, "pos_adjustmentHIV");
            Integer hivNegAdjust = getIntegerFromJsonKey(jsonObjectNegAdjust, "neg_adjustmentDarHIV");
            String expectedHiv = datajson.isNull("actualSOHExpectedHIV") ? "" : String.valueOf(datajson.getString("actualSOHExpectedHIV"));

            ProductAccountability hiv = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(hivClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(hivNegAdjust).openSOH(hivAtHand).dnote("")
                    .orgUnit(orgUnit).positiveAdjust(hivPosAdjust).product("hiv_screeningtest").qtyDispensed(hivDispensed)
                    .qtyOnBincard(qtyonBincardhiv).qtyDelivered(qtydeliveredhiv).receivedFromSCA(hivReceived).totalSOH(0).sohExpected(expectedHiv).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(hiv);

            //String dnoteAmoxil = jsonObjectReceived.isNull("deliveryNumberAmoxil") ? "" : jsonObjectReceived.getString("deliveryNumberAmoxil");     
            Integer amoxilAtHand = getIntegerFromJsonKey(jsonObjectqtyHand, "qtyHandAmoxil");
            Integer qtyDeliveredAmoxil = getIntegerFromJsonKey(jsonObjectQtyDelivered, "qtyDeliveredAmoxil");
            Integer qtyonBincardAmoxil = getIntegerFromJsonKey(jsonObjectQtyonBincard, "qtyBinCardAmoxil");
            Integer amoxilReceived = getIntegerFromJsonKey(jsonObjectReceived, "qtyIssuedAmoxil");
            Integer amoxilDispensed = getIntegerFromJsonKey(jsonObjectDispensed, "totalQtyIssuedDispAreaAmoxil");
            Integer amoxilClsng = getIntegerFromJsonKey(jsonObjectClsng, "actualCountDarAmoxil");
            Integer amoxilPosAdjust = getIntegerFromJsonKey(jsonObjectPosAdjust, "pos_adjustmentAmoxil");
            Integer amoxilNegAdjust = getIntegerFromJsonKey(jsonObjectNegAdjust, "neg_adjustmentDarAmoxil");
            String expectedAmoxil = datajson.isNull("actualSOHExpectedAmoxil") ? "" : String.valueOf(datajson.getString("actualSOHExpectedAmoxil"));

            ProductAccountability amoxil = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(amoxilClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(amoxilNegAdjust).openSOH(amoxilAtHand).dnote("")
                    .orgUnit(orgUnit).positiveAdjust(amoxilPosAdjust).product("amoxicillin250mgdt").qtyDispensed(amoxilDispensed)
                    .qtyOnBincard(qtyonBincardAmoxil).qtyDelivered(qtyDeliveredAmoxil).receivedFromSCA(amoxilReceived).totalSOH(0).sohExpected(expectedAmoxil).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(amoxil); 
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
        


    // supply chain addenda items
    private void supplyChainAddenda(String formId) throws Exception {
        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID + "/forms/" + formId + "/submissions";
        // Create request headers
        HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("JSON", odkToken));
        // Gets All submissions in summary
        JSONArray jArray = new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());

        // Now loop through the summary array to process each submission 
        for (int i = 0; i < jArray.length(); i++) {

            // Get a JSON object 
            JSONObject instance = jArray.getJSONObject(i);
            //logging instance json object
            //log.info("Json Object for form number "+(i+1)+":  "+instance.toString());
            // Get the instance ID of the submission xml file
            String instanceId = instance.getString("instanceId");
            
            if (accountabilityDao.getProductAccountabilities(instanceId, null, null, null, null).size() > 0) {
                continue;
            }

            //Get the one submission data file(in xml format) by it's instance ID
            String xmlString = getSubmissionInstanceXml(PROJECTID, formId, instanceId, odkToken);

            if (Objects.isNull(xmlString)) {
                continue;
            }

            // Convert submission XML data to JSON for easy parsing
            JSONObject jsonObj = XML.toJSONObject(xmlString);
            String json = jsonObj.toString(INDENTATION);
            JSONObject instancejson = new JSONObject(json);
            JSONObject datajson = instancejson.getJSONObject("data");
            //log.info("Json Object for form number "+(i+1)+":  "+datajson.toString());

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

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));
            
                        //JSONArray qtyhandArray=new JSONArray("qtyhand");
            //log.info("This is the qty at ahand " + qtyhandArray);
            //System.out.println(qtyhandArray);t
            
            
            
          JSONObject jsonObjSKemsaDeliveries = null;
          
          
          try{
               if (!datajson.isNull("KEMSA_Deliveries")) {
                jsonObjSKemsaDeliveries = datajson.getJSONObject("KEMSA_Deliveries");
                JSONArray arrChanges = covertJsonObjectToJsonArray(jsonObjSKemsaDeliveries.get("KEMSA_Deliveries1"));
                
                log.info("This is the stock status array of objects@@@@@@@@@@@@@QQQQFFFF "+arrChanges);
            
                for(int k=0; k<arrChanges.length(); k++){
               
                JSONObject jsonObjectScAddend = arrChanges.getJSONObject(k);
                //JSONObject jsonObjectScAddend2 = jsonObjectScAddend.getJSONObject("KEMSA_Deliveries1");                
               

                   //log.info("These are the objects from the converted array #####!!!!%%%% " + (k + 1) + ":  " + jsonObjectScAddend2.toString());
                   
                    Integer scStockatHand = getIntegerFromJsonKey(jsonObjectScAddend, "qtyHand");
                    Integer qtydeliveredadd = getIntegerFromJsonKey(jsonObjectScAddend, "qtyDelivered");
                    Integer scStockReceived = getIntegerFromJsonKey(jsonObjectScAddend, "totalQtyIssuedDispAreaTDF");
                    Integer scStockaDispensed = getIntegerFromJsonKey(jsonObjectScAddend, "totalQtyIssuedDispAreaTDF");
                    Integer qtyBinCard = getIntegerFromJsonKey(jsonObjectScAddend, "qtyBinCard");
                    Integer scStockclsng = getIntegerFromJsonKey(jsonObjectScAddend, "actualCount");
                    Integer scStockPosAdj = getIntegerFromJsonKey(jsonObjectScAddend, "pos_adjustment");
                    Integer scStockNegAdj = getIntegerFromJsonKey(jsonObjectScAddend, "neg_adjustmentDarTDF");
                    String scStockExpected = String.valueOf(jsonObjSKemsaDeliveries.getString("actualSOHExpected"));
                    String scStockcmdName = String.valueOf(jsonObjectScAddend.getString("commodityk"));

                    //one record to the dbase
                    ProductAccountability scStock = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).instanceId(instanceId).actualCount(scStockclsng)
                            .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(scStockNegAdj).openSOH(scStockatHand)
                            .orgUnit(orgUnit).positiveAdjust(scStockPosAdj).product(scStockcmdName).qtyDispensed(scStockaDispensed)
                            .qtyOnBincard(qtyBinCard).qtyDelivered(qtydeliveredadd).receivedFromSCA(scStockReceived).totalSOH(0).sohExpected(scStockExpected).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

                    productDao.save(scStock);
                    
                }
               }
            } catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
            }
          
          
              try{  
                  
                JSONArray jsonArrayAddenda = datajson.getJSONArray("KEMSA_Deliveries");
                
                for (int j = 0; j < jsonArrayAddenda.length(); j++) {
                    JSONObject jsonObjectScAddend = jsonArrayAddenda.getJSONObject(j);
                    JSONObject jsonObjectScAddend2 = jsonObjectScAddend.getJSONObject("KEMSA_Deliveries1");

                    Integer scStockatHand = getIntegerFromJsonKey(jsonObjectScAddend2, "qtyHand");
                    Integer qtydeliveredadd = getIntegerFromJsonKey(jsonObjectScAddend2, "qtyDelivered");
                    Integer qtyBinCard = getIntegerFromJsonKey(jsonObjectScAddend2, "qtyBinCard");
                    Integer scStockReceived = getIntegerFromJsonKey(jsonObjectScAddend2, "totalQtyIssuedDispAreaTDF");
                    Integer scStockaDispensed = getIntegerFromJsonKey(jsonObjectScAddend2, "totalQtyIssuedDispAreaTDF");
                    Integer scStockclsng = getIntegerFromJsonKey(jsonObjectScAddend2, "actualCount");
                    Integer scStockPosAdj = getIntegerFromJsonKey(jsonObjectScAddend2, "pos_adjustment");
                    Integer scStockNegAdj = getIntegerFromJsonKey(jsonObjectScAddend2, "neg_adjustmentDarTDF");
                    String scStockExpected = String.valueOf(jsonObjectScAddend.getString("actualSOHExpected"));
                    String scStockcmdName = String.valueOf(jsonObjectScAddend2.getString("commodityk"));

                    //one record to the dbase
                    ProductAccountability scStock = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).instanceId(instanceId).actualCount(scStockclsng)
                            .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(scStockNegAdj).openSOH(scStockatHand)
                            .orgUnit(orgUnit).positiveAdjust(scStockPosAdj).product(scStockcmdName).qtyDispensed(scStockaDispensed)
                            .qtyOnBincard(qtyBinCard).qtyDelivered(qtydeliveredadd).receivedFromSCA(scStockReceived).totalSOH(0).sohExpected(scStockExpected).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

                    productDao.save(scStock); 
                }

            } catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
            }


        }

    }
    
    
    
    
    
        // supply chain slimed
    private void supplyChainSlim(String formId) throws Exception {
        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID + "/forms/" + formId + "/submissions";
        // Create request headers
        HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("JSON", odkToken));
        // Gets All submissions in summary
        JSONArray jArray = new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());

        // Now loop through the summary array to process each submission 
        for (int i = 0; i < jArray.length(); i++) {

            // Get a JSON object 
            JSONObject instance = jArray.getJSONObject(i);
            //logging instance json object
            //log.info("Json Object for form number "+(i+1)+":  "+instance.toString());
            // Get the instance ID of the submission xml file
            String instanceId = instance.getString("instanceId");
            
            if (accountabilityDao.getProductAccountabilities(instanceId, null, null, null, null).size() > 0) {
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
            
            log.info("Json Object for form number HHHHHHHHHHHHHHHHHH "+(i+1)+":  "+datajson.toString());
            

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

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));
            
                        //JSONArray qtyhandArray=new JSONArray("qtyhand");
            //log.info("This is the qty at ahand " + qtyhandArray);
            //System.out.println(qtyhandArray);t
            

          JSONObject jsonObjProducts = null;
          
          try{
               if (!datajson.isNull("productrepeat")) {
                   jsonObjProducts = datajson.getJSONObject("productrepeat");
                   JSONArray arrChanges = covertJsonObjectToJsonArray(jsonObjProducts.get("product"));
                   
                   log.info("This is the stock status array of objects@@@@@@@@@@@@@QQQQFFFF "+arrChanges);
                   
                   for(int k=0; k<arrChanges.length(); k++){
                       
                       JSONObject jsonObjectSlim = arrChanges.getJSONObject(k);
                       
                       log.info("this is happening jhiojoijoijoyuitb " + jsonObjectSlim.toString());
                       
                       //JSONObject jsonObjectSlim2 = jsonObjectSlim.getJSONObject("product");
                       
                       log.info("array objectes are herer  cccccdrdredgdsg #####!!!!%%%% " + (k + 1) + ":  " + jsonObjectSlim.toString());
                       //log.info("These are the objects from the converted array #####!!!!%%%% " + (k + 1) + ":  " + jsonObjectSlim2.toString());
                       
                       Integer scStockatHand = getIntegerFromJsonKey(jsonObjectSlim, "qtyHandproduct");
                       Integer scStockReceived = getIntegerFromJsonKey(jsonObjectSlim, "qtyReceivedproduct");
                       Integer scStockaDispensed = getIntegerFromJsonKey(jsonObjectSlim, "totalQtyIssuedDispAreaproduct");
                       String productReceived = jsonObjectSlim.isNull("ItemReceivedproduct") ? "Not Applicable" : jsonObjectSlim.getString("ItemReceivedproduct");
                       Integer qtyDelivered = getIntegerFromJsonKey(jsonObjectSlim, "qtyDeliveredproduct");
                       Integer qtyOnBincard = getIntegerFromJsonKey(jsonObjectSlim, "qtyBinCardproduct");
                       //Integer qtyBinCard = getIntegerFromJsonKey(jsonObjectSlim, "qtyBinCard");
                       Integer scStockclsng = getIntegerFromJsonKey(jsonObjectSlim, "totalSOHproduct");
                       Integer scStockPosAdj = getIntegerFromJsonKey(jsonObjectSlim, "pos_adjustmentproduct");
                       Integer scStockNegAdj = getIntegerFromJsonKey(jsonObjectSlim, "negAdjustmentIssuedToOtherHCproduct");
                       String scStockExpected = String.valueOf(jsonObjProducts.getString("actualSOHExpectedproduct"));
                      // String scStockExpected = datajson.isNull("actualSOHExpectedproduct") ? "" : String.valueOf(datajson.getString("actualSOHExpectedproduct"));
                       String scStockcmdName = String.valueOf(jsonObjectSlim.getString("commodityname"));
                       
                       //one record to the dbase
                       ProductAccountability scStock = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).instanceId(instanceId).actualCount(scStockclsng)
                               .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(scStockNegAdj).openSOH(scStockatHand)
                               .orgUnit(orgUnit).positiveAdjust(scStockPosAdj).product(scStockcmdName).qtyDispensed(scStockaDispensed)
                               .receivedFromSCA(scStockReceived).totalSOH(0).sohExpected(scStockExpected).perAccounted().county(county).facility(facility).subcounty(subcounty)
                               .productReceived(productReceived).qtyDelivered(qtyDelivered).qtyOnBincard(qtyOnBincard).build();
                       
                       productDao.save(scStock);
                       
                   }
               } else {
               }
            } catch (Exception e) {
                log.error("Error on arraychanges Occured ====" + e.getMessage());
            }
          
          
              try{  
                JSONArray jsonArrayAddenda = datajson.getJSONArray("productrepeat");
                for (int j = 0; j < jsonArrayAddenda.length(); j++) {
                    JSONObject jsonObjectSlim = jsonArrayAddenda.getJSONObject(j);
                    JSONObject jsonObjectSlim2 = jsonObjectSlim.getJSONObject("product");

                    Integer scStockatHand = getIntegerFromJsonKey(jsonObjectSlim2, "qtyHandproduct");
                    Integer scStockReceived = getIntegerFromJsonKey(jsonObjectSlim2, "qtyReceivedproduct");
                    Integer scStockaDispensed = getIntegerFromJsonKey(jsonObjectSlim2, "totalQtyIssuedDispAreaproduct");
                    String productReceived = jsonObjectSlim2.isNull("ItemReceivedproduct") ? "Not Applicable" : jsonObjectSlim2.getString("ItemReceivedproduct");
                    Integer qtyDelivered = getIntegerFromJsonKey(jsonObjectSlim2, "qtyDeliveredproduct");
                    Integer qtyOnBincard = getIntegerFromJsonKey(jsonObjectSlim2, "qtyBinCardproduct");
                    //Integer qtyBinCard = getIntegerFromJsonKey(jsonObjectSlim2, "qtyBinCard");
                    Integer scStockclsng = getIntegerFromJsonKey(jsonObjectSlim2, "totalSOHproduct");
                    Integer scStockPosAdj = getIntegerFromJsonKey(jsonObjectSlim2, "pos_adjustmentproduct");
                    Integer scStockNegAdj = getIntegerFromJsonKey(jsonObjectSlim2, "negAdjustmentIssuedToOtherHCproduct");
                    String scStockExpected = String.valueOf(jsonObjectSlim.getString("actualSOHExpectedproduct"));
                    String scStockcmdName = String.valueOf(jsonObjectSlim2.getString("commodityname"));

                    //one record to the dbase
                    ProductAccountability scStock = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).instanceId(instanceId).actualCount(scStockclsng)
                            .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(scStockNegAdj).openSOH(scStockatHand)
                            .orgUnit(orgUnit).positiveAdjust(scStockPosAdj).product(scStockcmdName).qtyDispensed(scStockaDispensed)
                            .receivedFromSCA(scStockReceived).totalSOH(0).sohExpected(scStockExpected).perAccounted().county(county).facility(facility).subcounty(subcounty)
                            .productReceived(productReceived).qtyDelivered(qtyDelivered).qtyOnBincard(qtyOnBincard).build();

                    productDao.save(scStock);
                }

            } catch (Exception e) {
                log.error("Error Occured ====" + e.getMessage());
            } 


        }

    }
    
    
    
//   private void supply chain Horizontal workflow
    private void supplyChainHorizontal(String formId) throws Exception {
        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID + "/forms/" + formId + "/submissions";
        // Create request headers
        HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("JSON", odkToken));
        // Gets All submissions in summary
        JSONArray jArray = new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());

        // Now loop through the summary array to process each submission 
        for (int i = 0; i < jArray.length(); i++) {

            // Get a JSON object 
            JSONObject instance = jArray.getJSONObject(i);
            //logging instance json object
            //log.info("Json Object for form number "+(i+1)+":  "+instance.toString());
            // Get the instance ID of the submission xml file
            String instanceId = instance.getString("instanceId");
            
            if (accountabilityDao.getProductAccountabilities(instanceId, null, null, null, null).size() > 0) {
                continue;
            }

            //Get the one submission data file(in xml format) by it's instance ID
            String xmlString = getSubmissionInstanceXml(PROJECTID, formId, instanceId, odkToken);

            if (Objects.isNull(xmlString)) {
                continue;
            }

            // Convert submission XML data to JSON for easy parsing
            JSONObject jsonObj = XML.toJSONObject(xmlString);
            String json = jsonObj.toString(INDENTATION);
            JSONObject instancejson = new JSONObject(json);
            JSONObject datajson = instancejson.getJSONObject("data");
            //log.info("Json Object for form number "+(i+1)+":  "+datajson.toString());

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

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));

                     try{
            
            JSONObject jsonObjectal24 = null;
            if (!datajson.isNull("al24")) {
                jsonObjectal24 = datajson.getJSONObject("al24");
            }

            JSONObject jsonObjectdmpa = null;
            if (!datajson.isNull("dmpa")) {
                jsonObjectdmpa = datajson.getJSONObject("dmpa");
            }

            JSONObject jsonObjecttdf90 = null;
            if (!datajson.isNull("tdf90")) {
                jsonObjecttdf90 = datajson.getJSONObject("tdf90");
            }
            JSONObject jsonObjectamoxicillin = null;
            if (!datajson.isNull("amoxicillin")) {
                jsonObjectamoxicillin = datajson.getJSONObject("amoxicillin");
            }
            JSONObject jsonObjecthivkit = null;
            if (!datajson.isNull("hivkit")) {
                jsonObjecthivkit = datajson.getJSONObject("hivkit");
            }

            

            String dnoteAl = jsonObjectal24.isNull("deliveryNumberAl") ? "" : jsonObjectal24.getString("deliveryNumberAl");
            Integer al24AtHand = getIntegerFromJsonKey(jsonObjectal24, "qtyHandAl");
            Integer qtyDeliveredAl = getIntegerFromJsonKey(jsonObjectal24, "qtyDeliveredAl");
            Integer al24Received = getIntegerFromJsonKey(jsonObjectal24, "qtyIssuedAl");
            Integer al24Dispensed = getIntegerFromJsonKey(jsonObjectal24, "totalQtyIssuedDispAreaAl");
            Integer al24PosAdjust = getIntegerFromJsonKey(jsonObjectal24, "pos_adjustmentAl");
            Integer al24Clsng = getIntegerFromJsonKey(jsonObjectal24, "actualCountDarAl");
            Integer al24NegAdjust = getIntegerFromJsonKey(jsonObjectal24, "neg_adjustmentDarAl");
            String expectedAl = datajson.isNull("actualSOHExpectedAl") ? "" : String.valueOf(datajson.getString("actualSOHExpectedAl"));

            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1This expected results for testing%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  " + jsonObjectal24);
            //log.info("This expected results for testing  " + expectedAl);

            //one record to the dbase
            ProductAccountability al24 = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(al24Clsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(al24NegAdjust).openSOH(al24AtHand).dnote(dnoteAl)
                    .orgUnit(orgUnit).positiveAdjust(al24PosAdjust).product("Al 24s").qtyDispensed(al24Dispensed)
                    .qtyDelivered(qtyDeliveredAl).receivedFromSCA(al24Received).totalSOH(0).sohExpected(expectedAl).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(al24);

            String dnoteDmpa = jsonObjectdmpa.isNull("deliveryNumberDMPA") ? "" : jsonObjectdmpa.getString("deliveryNumberDMPA");
            Integer dmpaAtHand = getIntegerFromJsonKey(jsonObjectdmpa, "qtyHandDMPA");
            Integer qtyDeliveredDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "qtyDeliveredDMPA");
            Integer dmpaReceived = getIntegerFromJsonKey(jsonObjectdmpa, "qtyIssuedDMPA");
            Integer dmpaDispensed = getIntegerFromJsonKey(jsonObjectdmpa, "totalQtyIssuedDispAreaDMPA");
            Integer dmpaClsng = getIntegerFromJsonKey(jsonObjectdmpa, "actualCountDarDMPA");
            Integer dmpaPosAdjust = getIntegerFromJsonKey(jsonObjectdmpa, "pos_adjustmentDMPA");
            Integer dmpaNegAdjust = getIntegerFromJsonKey(jsonObjectdmpa, "neg_adjustmentDarDMPA");
            String expectedDmpa = datajson.isNull("actualSOHExpectedDMPA") ? "" : String.valueOf(datajson.getString("actualSOHExpectedDMPA"));

            ProductAccountability DMPA = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(dmpaClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(dmpaNegAdjust).openSOH(dmpaAtHand).dnote(dnoteDmpa)
                    .orgUnit(orgUnit).positiveAdjust(dmpaPosAdjust).product("DMPA").qtyDispensed(dmpaDispensed)
                    .qtyDelivered(qtyDeliveredDMPA).receivedFromSCA(dmpaReceived).totalSOH(0).sohExpected(expectedDmpa).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(DMPA);
            
            String dnotetdf = jsonObjecttdf90.isNull("deliveryNumberTDF") ? "" : jsonObjecttdf90.getString("deliveryNumberTDF");
            Integer tdfAtHand = getIntegerFromJsonKey(jsonObjecttdf90, "qtyHandTDF");
            Integer qtyDeliveredTDF = getIntegerFromJsonKey(jsonObjecttdf90, "qtyDeliveredTDF");
            Integer tdfReceived = getIntegerFromJsonKey(jsonObjecttdf90, "qtyIssuedTDF");
            Integer tdfDispensed = getIntegerFromJsonKey(jsonObjecttdf90, "totalQtyIssuedDispAreaTDF");
            Integer tdfClsng = getIntegerFromJsonKey(jsonObjecttdf90, "actualCountDarTDF");
            Integer tdfPosAdjust = getIntegerFromJsonKey(jsonObjecttdf90, "pos_adjustmentTDF");
            Integer tdfNegAdjust = getIntegerFromJsonKey(jsonObjecttdf90, "neg_adjustmentDarTDF");
            String expectedTdf = datajson.isNull("actualSOHExpectedTDF") ? "" : String.valueOf(datajson.getString("actualSOHExpectedTDF"));

            ProductAccountability TDF = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(tdfClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(tdfNegAdjust).openSOH(tdfAtHand).dnote(dnotetdf)
                    .orgUnit(orgUnit).positiveAdjust(tdfPosAdjust).product("tdf3tcdtgadult").qtyDispensed(tdfDispensed)
                    .qtyDelivered(qtyDeliveredTDF).receivedFromSCA(tdfReceived).totalSOH(0).sohExpected(expectedTdf).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(TDF);
            
            String dnotehiv = jsonObjecthivkit.isNull("deliveryNumberHIV") ? "" : jsonObjecthivkit.getString("deliveryNumberHIV");
            Integer hivAtHand = getIntegerFromJsonKey(jsonObjecthivkit, "qtyHandHIV");
            Integer qtyDeliveredHIV = getIntegerFromJsonKey(jsonObjecthivkit, "qtyDeliveredHIV");
            Integer hivReceived = getIntegerFromJsonKey(jsonObjecthivkit, "qtyIssuedHIV");
            Integer hivDispensed = getIntegerFromJsonKey(jsonObjecthivkit, "totalQtyIssuedDispAreaHIV");
            Integer hivClsng = getIntegerFromJsonKey(jsonObjecthivkit, "actualCountDarHIV");
            Integer hivPosAdjust = getIntegerFromJsonKey(jsonObjecthivkit, "pos_adjustmentHIV");
            Integer hivNegAdjust = getIntegerFromJsonKey(jsonObjecthivkit, "neg_adjustmentDarHIV");
            String expectedHiv = datajson.isNull("actualSOHExpectedHIV") ? "" : String.valueOf(datajson.getString("actualSOHExpectedHIV"));

            ProductAccountability hiv = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(hivClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(hivNegAdjust).openSOH(hivAtHand).dnote(dnotehiv)
                    .orgUnit(orgUnit).positiveAdjust(hivPosAdjust).product("hiv_screeningtest").qtyDispensed(hivDispensed)
                    .qtyDelivered(qtyDeliveredHIV).receivedFromSCA(hivReceived).totalSOH(0).sohExpected(expectedHiv).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(hiv);

            String dnoteAmoxil = jsonObjectamoxicillin.isNull("deliveryNumberAmoxil") ? "" : jsonObjectamoxicillin.getString("deliveryNumberAmoxil");     
            Integer amoxilAtHand = getIntegerFromJsonKey(jsonObjectamoxicillin, "qtyHandAmoxil");
            Integer qtyDeliveredAmoxil = getIntegerFromJsonKey(jsonObjectamoxicillin, "qtyDeliveredAmoxil");
            Integer amoxilReceived = getIntegerFromJsonKey(jsonObjectamoxicillin, "qtyIssuedAmoxil");
            Integer amoxilDispensed = getIntegerFromJsonKey(jsonObjectamoxicillin, "totalQtyIssuedDispAreaAmoxil");
            Integer amoxilClsng = getIntegerFromJsonKey(jsonObjectamoxicillin, "actualCountDarAmoxil");
            Integer amoxilPosAdjust = getIntegerFromJsonKey(jsonObjectamoxicillin, "pos_adjustmentAmoxil");
            Integer amoxilNegAdjust = getIntegerFromJsonKey(jsonObjecthivkit, "neg_adjustmentDarAmoxil");
            String expectedAmoxil = datajson.isNull("actualSOHExpectedAmoxil") ? "" : String.valueOf(datajson.getString("actualSOHExpectedAmoxil"));

            ProductAccountability amoxil = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(amoxilClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(amoxilNegAdjust).openSOH(amoxilAtHand).dnote(dnoteAmoxil)
                    .orgUnit(orgUnit).positiveAdjust(amoxilPosAdjust).product("amoxicillin250mgdt").qtyDispensed(amoxilDispensed)
                    .qtyDelivered(qtyDeliveredAmoxil).receivedFromSCA(amoxilReceived).totalSOH(0).sohExpected(expectedAmoxil).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(amoxil);
            
            } catch (Exception e) {
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
