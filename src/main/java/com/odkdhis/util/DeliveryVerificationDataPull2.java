/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.ProductAccountabilityDaoImpl;
import com.odkdhis.model.dashboard.ProductAccountability;
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
public class DeliveryVerificationDataPull2 extends CommonUtil {

    @Autowired
    private ProductAccountabilityDaoImpl productDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
   //not needed as per now @Scheduled(cron ="10 01 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionVerificationData() {
        try {
            supplyChainSlim("supply_chain_audit_slim");
            //Thread.sleep(2000);

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void supplyChainSlim(String formId) throws Exception {
        log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV" + formId);

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
            if (verificationDataDao.getRecordByInstanceId(instanceId) != null) {
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
            JSONObject jsonObjectpp2 = null;
            if (!datajson.isNull("product")) {
                jsonObjectpp2 = datajson.getJSONObject("product");
            }
            
            //Integer empty = 0;
            
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
                    
                    String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
                    String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
                    String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");           
                    String scStockcmdName = String.valueOf(jsonObjProducts.getString("product_name"));
                    String deliveryAvailable  = jsonObjectSlim.isNull("deliveryAvailableproduct") ? "No" : jsonObjectSlim.getString("deliveryAvailableproduct");
                    String deliveryNumber  = jsonObjectSlim.isNull("deliveryNumberproduct") ? "Not Applicable" : jsonObjectSlim.getString("deliveryNumberproduct");
                    String productReceived = jsonObjectSlim.isNull("ItemReceivedproduct") ? "Not Applicable" : jsonObjectSlim.getString("ItemReceivedproduct");       
                    String deliveryDate =jsonObjectSlim.isNull("deliverydate") ? "No Date" : jsonObjectSlim.getString("deliverydate");         
                    Integer qtyReceivedFromSupply =getIntegerFromJsonKey(jsonObjectSlim, "qtyReceivedFromSupply");  
                    Integer qtyDelivered = getIntegerFromJsonKey(jsonObjectSlim, "qtyDeliveredproduct");
                    Integer qtyOnBincard = getIntegerFromJsonKey(jsonObjectSlim, "qtyBinCardproduct");
                    Integer actualCount = getIntegerFromJsonKey(jsonObjectSlim, "actualCountproduct");
                    Integer scStockatHand = getIntegerFromJsonKey(jsonObjectSlim, "qtyHandproduct");
                    //Integer scStockReceived = getIntegerFromJsonKey(jsonObjectSlim2, "qtyReceivedproduct");
                    Integer scStockaDispensed = getIntegerFromJsonKey(jsonObjectSlim, "totalQtyIssuedDispAreaproduct");
                    Integer scStockclsng = getIntegerFromJsonKey(jsonObjectSlim, "totalSOHproduct");
                    Integer scStockPosAdj = getIntegerFromJsonKey(jsonObjectSlim, "pos_adjustmentproduct");
                    Integer scStockNegAdj = getIntegerFromJsonKey(jsonObjectSlim, "negAdjustmentIssuedToOtherHCproduct");
                    String scStockExpected = String.valueOf(jsonObjProducts.getString("actualSOHExpectedproduct"));
                      // String scStockExpected = datajson.isNull("actualSOHExpectedproduct") ? "" : String.valueOf(datajson.getString("actualSOHExpectedproduct"));
 
                       
                    Integer numerator = getIntegerFromJsonKey(datajson, "finalNumerator");
                    Integer denominator = getIntegerFromJsonKey(datajson, "totalDenominator");
                    Integer score = getIntegerFromJsonKey(datajson, "finalscore");
                       
                       
                       //one record to the dbase
                       ProductAccountability scStock = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).instanceId(instanceId).actualCount(scStockclsng)
                               .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(scStockNegAdj).openSOH(scStockatHand)
                               .orgUnit(orgUnit).positiveAdjust(scStockPosAdj).product(scStockcmdName).qtyDispensed(scStockaDispensed)
                               .deliveryAvailable(deliveryAvailable)
                               .deliveryNumber(deliveryNumber)
                               .productReceived(productReceived)
                               .qtyReceivedFromSupply(qtyReceivedFromSupply).qtyDelivered(qtyDelivered).qtyOnBincard(qtyOnBincard).deliveryDate(deliveryDate)
                               .numerator(numerator).denominator(denominator).score(score)
                               .actualCount(actualCount)
                               .totalSOH(0).sohExpected(scStockExpected).perAccounted().county(county).facility(facility).subcounty(subcounty).build();
                    
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
                    
                    String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
                    String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
                    String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");                    

                    String scStockcmdName = String.valueOf(jsonObjectSlim.getString("product_name"));
                    String deliveryAvailable  = jsonObjectSlim2.isNull("deliveryAvailableproduct") ? "No" : jsonObjectSlim2.getString("deliveryAvailableproduct");
                    String deliveryNumber  = jsonObjectSlim2.isNull("deliveryNumberproduct") ? "Not Applicable" : jsonObjectSlim2.getString("deliveryNumberproduct");
                    String productReceived = jsonObjectSlim2.isNull("ItemReceivedproduct") ? "Not Applicable" : jsonObjectSlim2.getString("ItemReceivedproduct");       
                    String deliveryDate =jsonObjectSlim2.isNull("deliverydate") ? "No Date" : jsonObjectSlim2.getString("deliverydate");         
                    Integer qtyReceivedFromSupply =getIntegerFromJsonKey(jsonObjectSlim2, "qtyReceivedFromSupply");  
                    Integer qtyDelivered = getIntegerFromJsonKey(jsonObjectSlim2, "qtyDeliveredproduct");
                    Integer qtyOnBincard = getIntegerFromJsonKey(jsonObjectSlim2, "qtyBinCardproduct");
                    Integer actualCount = getIntegerFromJsonKey(jsonObjectSlim2, "actualCountproduct");
                    Integer scStockatHand = getIntegerFromJsonKey(jsonObjectSlim2, "qtyHandproduct");
                    //Integer scStockReceived = getIntegerFromJsonKey(jsonObjectSlim2, "qtyReceivedproduct");
                    Integer scStockaDispensed = getIntegerFromJsonKey(jsonObjectSlim2, "totalQtyIssuedDispAreaproduct");
                    Integer scStockclsng = getIntegerFromJsonKey(jsonObjectSlim2, "totalSOHproduct");
                    Integer scStockPosAdj = getIntegerFromJsonKey(jsonObjectSlim2, "pos_adjustmentproduct");
                    Integer scStockNegAdj = getIntegerFromJsonKey(jsonObjectSlim2, "negAdjustmentIssuedToOtherHCproduct");
                    String scStockExpected = String.valueOf(jsonObjectSlim.getString("actualSOHExpectedproduct"));
                      // String scStockExpected = datajson.isNull("actualSOHExpectedproduct") ? "" : String.valueOf(datajson.getString("actualSOHExpectedproduct"));
 
                       
                    Integer numerator = getIntegerFromJsonKey(datajson, "finalNumerator");
                    Integer denominator = getIntegerFromJsonKey(datajson, "totalDenominator");
                    Integer score = getIntegerFromJsonKey(datajson, "finalscore");
                       
                       
                       //one record to the dbase
                       ProductAccountability scStock = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).instanceId(instanceId).actualCount(scStockclsng)
                               .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(scStockNegAdj).openSOH(scStockatHand)
                               .orgUnit(orgUnit).positiveAdjust(scStockPosAdj).product(scStockcmdName).qtyDispensed(scStockaDispensed)
                               .deliveryAvailable(deliveryAvailable)
                               .deliveryNumber(deliveryNumber)
                               .productReceived(productReceived)
                               .qtyReceivedFromSupply(qtyReceivedFromSupply).qtyDelivered(qtyDelivered).qtyOnBincard(qtyOnBincard).deliveryDate(deliveryDate)
                               .numerator(numerator).denominator(denominator).score(score)
                               .actualCount(actualCount)
                               .totalSOH(0).sohExpected(scStockExpected).perAccounted().county(county).facility(facility).subcounty(subcounty).build();
                       
                       productDao.save(scStock);
                }

            } catch (Exception e) {
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
