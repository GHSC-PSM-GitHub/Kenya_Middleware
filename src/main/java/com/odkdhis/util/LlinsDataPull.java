/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.LlinsDaoImpl;
import com.odkdhis.model.dashboard.LlinsModel;
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
public class LlinsDataPull extends CommonUtil {

    @Autowired
    private LlinsDaoImpl llinsDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
    @Scheduled(cron ="10 30 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionLlinsInventory() {
        try {
            llinsInventoryManagement("LLINs");
            //Thread.sleep(2000);

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void llinsInventoryManagement(String formId) throws Exception {
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
            if (llinsDao.getRecordByInstanceId(instanceId) != null) {
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
            
            JSONObject jsonObjectInventory = null;
            if (!datajson.isNull("Inventory")) {
                jsonObjectInventory = datajson.getJSONObject("Inventory");
            }

            JSONObject jsonObjectAccountability = null;
            if (!datajson.isNull("section3")) {
                jsonObjectAccountability = datajson.getJSONObject("section3");
            }

            Integer empty = 0;

            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String cardAvailability = jsonObjectInventory.isNull("stockCardFNPR") ? "" : jsonObjectInventory.getString("stockCardFNPR");
            Integer alCounts = getIntegerFromJsonKey(jsonObjectInventory, "nostockcounts");
            Integer cmos = getIntegerFromJsonKey(jsonObjectInventory, "currentMOS");
            String expiries = jsonObjectInventory.isNull("anyNetsExpired") ? "" : jsonObjectInventory.getString("anyNetsExpired");
            Integer iDenominator = getIntegerFromJsonKey(jsonObjectInventory, "invDenominator");
            Integer iNumerator = getIntegerFromJsonKey(jsonObjectInventory, "invNumerator");
            Integer actualcount = getIntegerFromJsonKey(jsonObjectInventory, "actualstockonvisit");
            Integer daysoutstock = getIntegerFromJsonKey(jsonObjectInventory, "nooutofdaysStock");
            Integer amc = getIntegerFromJsonKey(jsonObjectInventory, "amcfromservice");
            Integer stockcardsscore = getIntegerFromJsonKey(jsonObjectInventory, "stockCardScore");
            Integer cardBalance = getIntegerFromJsonKey(jsonObjectInventory, "stockcardFNPRBalance");
            
            Integer accountabilityscore = getIntegerFromJsonKey(jsonObjectAccountability, "ScoreForAccountability");
            
                        
            Integer finalscore = getIntegerFromJsonKey(datajson, "lfnalScore");
            //Integer stcardscore = (datajson.isNull("stockCardScoreAL6s")) ? empty : datajson.getInt("stockCardScoreAL6s");
            //Integer iDenominator = (datajson.isNull("itemDenominatorAL6s")) ? empty : datajson.getInt("itemDenominatorAL6s");
            //  Integer iNumerator = (datajson.isNull("itemNumeratorAL6s")) ? empty : datajson.getInt("itemNumeratorAL6s");

            LlinsModel llins = new LlinsModel.LlinsBuilder().instanceId(instanceId).product("LLINs")
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty)
                    .orgUnit(orgUnit).dayOutStock(daysoutstock).amc(amc).stCardScore(stockcardsscore)
                    .accountabilityscore(accountabilityscore)
                    .stCardAvail(cardAvailability).stCountsDone(alCounts).finalscore(finalscore)
                    .expires6mo(expiries).actualCount(actualcount).cmos(cmos).cardBalance(cardBalance)
                    .iDenominator(iDenominator).iNumerator(iNumerator).build();
            try {
                llinsDao.save(llins);
            } catch (Exception e) {
                log.info("Duplication");
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

}
