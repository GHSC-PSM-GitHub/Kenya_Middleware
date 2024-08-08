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
public class SampleDataPull1 extends CommonUtil {

    @Autowired
    private ProductAccountabilityDaoImpl productDao;

    //@Scheduled(cron = "10 40 12-13 * * ?", zone = "Africa/Nairobi")
    @Scheduled(cron ="10 43 * * * ?", zone = "Africa/Nairobi")
   // for developing @Scheduled(cron ="10 35 * * * ?", zone = "Africa/Nairobi")
    //@Scheduled(cron ="10 * * * * ?", zone = "Africa/Nairobi") 
    public void prepareSubmission() {
        try {
            //basicSupplyChain("supply_chain_audit_v2");
            supplyChainSlim("supply_chain_audit_slim");
            //Thread.sleep(2000);
            //supplyChainAddenda("supply_chain_audit_Addenda");
            //supplyChainHorizontal("supply_chain_audit_v3");

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    } 
    
        // supply chain slimed
    private void supplyChainSlim(String formId) throws Exception {
        // Login to the ODK server
        String odkToken = getODKAuthToken();
        //Declare URL for getting submissions in a summary form
        final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + PROJECTID7 + "/forms/" + formId + "/submissions";
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
                       
                    String scStockcmdName = String.valueOf(jsonObjProducts.getString("product_name"));
                    String deliveryAvailable  = jsonObjectSlim.isNull("deliveryAvailableproduct") ? "No" : jsonObjectSlim.getString("deliveryAvailableproduct");
                    //String deliveryNumber  = jsonObjectSlim.isNull("deliveryNumberproduct") ? "Not Applicable" : jsonObjectSlim.getString("deliveryNumberproduct");
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
                               .deliveryNumber("Delivery Number")
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

                    String scStockcmdName = String.valueOf(jsonObjectSlim.getString("product_name"));
                    String deliveryAvailable  = jsonObjectSlim2.isNull("deliveryAvailableproduct") ? "No" : jsonObjectSlim2.getString("deliveryAvailableproduct");
                    //String deliveryNumber  = jsonObjectSlim2.isNull("deliveryNumberproduct") ? "Not Applicable" : jsonObjectSlim2.getString("deliveryNumberproduct");
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
                               .deliveryNumber("Delivery Number")
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
    
    
    
    /*
    
       // supply chain Horizontal workflow
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
                    .receivedFromSCA(al24Received).totalSOH(0).sohExpected(expectedAl).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(al24);

            String dnoteDmpa = jsonObjectdmpa.isNull("deliveryNumberDMPA") ? "" : jsonObjectdmpa.getString("deliveryNumberDMPA");
            Integer dmpaAtHand = getIntegerFromJsonKey(jsonObjectdmpa, "qtyHandDMPA");
            Integer dmpaReceived = getIntegerFromJsonKey(jsonObjectdmpa, "qtyIssuedDMPA");
            Integer dmpaDispensed = getIntegerFromJsonKey(jsonObjectdmpa, "totalQtyIssuedDispAreaDMPA");
            Integer dmpaClsng = getIntegerFromJsonKey(jsonObjectdmpa, "actualCountDarDMPA");
            Integer dmpaPosAdjust = getIntegerFromJsonKey(jsonObjectdmpa, "pos_adjustmentDMPA");
            Integer dmpaNegAdjust = getIntegerFromJsonKey(jsonObjectdmpa, "neg_adjustmentDarDMPA");
            String expectedDmpa = datajson.isNull("actualSOHExpectedDMPA") ? "" : String.valueOf(datajson.getString("actualSOHExpectedDMPA"));

            ProductAccountability DMPA = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(dmpaClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(dmpaNegAdjust).openSOH(dmpaAtHand).dnote(dnoteDmpa)
                    .orgUnit(orgUnit).positiveAdjust(dmpaPosAdjust).product("DMPA").qtyDispensed(dmpaDispensed)
                    .receivedFromSCA(dmpaReceived).totalSOH(0).sohExpected(expectedDmpa).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(DMPA);
            
            String dnotetdf = jsonObjecttdf90.isNull("deliveryNumberTDF") ? "" : jsonObjecttdf90.getString("deliveryNumberTDF");
            Integer tdfAtHand = getIntegerFromJsonKey(jsonObjecttdf90, "qtyHandTDF");
            Integer tdfReceived = getIntegerFromJsonKey(jsonObjecttdf90, "qtyIssuedTDF");
            Integer tdfDispensed = getIntegerFromJsonKey(jsonObjecttdf90, "totalQtyIssuedDispAreaTDF");
            Integer tdfClsng = getIntegerFromJsonKey(jsonObjecttdf90, "actualCountDarTDF");
            Integer tdfPosAdjust = getIntegerFromJsonKey(jsonObjecttdf90, "pos_adjustmentTDF");
            Integer tdfNegAdjust = getIntegerFromJsonKey(jsonObjecttdf90, "neg_adjustmentDarTDF");
            String expectedTdf = datajson.isNull("actualSOHExpectedTDF") ? "" : String.valueOf(datajson.getString("actualSOHExpectedTDF"));

            ProductAccountability TDF = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(tdfClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(tdfNegAdjust).openSOH(tdfAtHand).dnote(dnotetdf)
                    .orgUnit(orgUnit).positiveAdjust(tdfPosAdjust).product("tdf3tcdtgadult").qtyDispensed(tdfDispensed)
                    .receivedFromSCA(tdfReceived).totalSOH(0).sohExpected(expectedTdf).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(TDF);
            
            String dnotehiv = jsonObjectamoxicillin.isNull("deliveryNumberHIV") ? "" : jsonObjectamoxicillin.getString("deliveryNumberHIV");
            Integer hivAtHand = getIntegerFromJsonKey(jsonObjectamoxicillin, "qtyHandHIV");
            Integer hivReceived = getIntegerFromJsonKey(jsonObjectamoxicillin, "qtyIssuedHIV");
            Integer hivDispensed = getIntegerFromJsonKey(jsonObjectamoxicillin, "totalQtyIssuedDispAreaHIV");
            Integer hivClsng = getIntegerFromJsonKey(jsonObjectamoxicillin, "actualCountDarHIV");
            Integer hivPosAdjust = getIntegerFromJsonKey(jsonObjectamoxicillin, "pos_adjustmentHIV");
            Integer hivNegAdjust = getIntegerFromJsonKey(jsonObjectamoxicillin, "neg_adjustmentDarHIV");
            String expectedHiv = datajson.isNull("actualSOHExpectedHIV") ? "" : String.valueOf(datajson.getString("actualSOHExpectedHIV"));

            ProductAccountability hiv = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(hivClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(hivNegAdjust).openSOH(hivAtHand).dnote(dnotehiv)
                    .orgUnit(orgUnit).positiveAdjust(hivPosAdjust).product("hiv_screeningtest").qtyDispensed(hivDispensed)
                    .receivedFromSCA(hivReceived).totalSOH(0).sohExpected(expectedHiv).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(hiv);

            String dnoteAmoxil = jsonObjecthivkit.isNull("deliveryNumberAmoxil") ? "" : jsonObjecthivkit.getString("deliveryNumberAmoxil");     
            Integer amoxilAtHand = getIntegerFromJsonKey(jsonObjecthivkit, "qtyHandAmoxil");
            Integer amoxilReceived = getIntegerFromJsonKey(jsonObjecthivkit, "qtyIssuedAmoxil");
            Integer amoxilDispensed = getIntegerFromJsonKey(jsonObjecthivkit, "totalQtyIssuedDispAreaAmoxil");
            Integer amoxilClsng = getIntegerFromJsonKey(jsonObjecthivkit, "actualCountDarAmoxil");
            Integer amoxilPosAdjust = getIntegerFromJsonKey(jsonObjecthivkit, "pos_adjustmentAmoxil");
            Integer amoxilNegAdjust = getIntegerFromJsonKey(jsonObjecthivkit, "neg_adjustmentDarAmoxil");
            String expectedAmoxil = datajson.isNull("actualSOHExpectedAmoxil") ? "" : String.valueOf(datajson.getString("actualSOHExpectedAmoxil"));

            ProductAccountability amoxil = new ProductAccountability.ProductAccountabilityBuilder().instanceId(instanceId).actualCount(amoxilClsng)
                    .dateCollected(getDateFromString(instanceCollectedDate)).negativeAdjust(amoxilNegAdjust).openSOH(amoxilAtHand).dnote(dnoteAmoxil)
                    .orgUnit(orgUnit).positiveAdjust(amoxilPosAdjust).product("amoxicillin250mgdt").qtyDispensed(amoxilDispensed)
                    .receivedFromSCA(amoxilReceived).totalSOH(0).sohExpected(expectedAmoxil).perAccounted().county(county).facility(facility).subcounty(subcounty).build();

            productDao.save(amoxil);
            
            } catch (Exception e) {
            }   
           
        }

    }
   */ 
    

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
