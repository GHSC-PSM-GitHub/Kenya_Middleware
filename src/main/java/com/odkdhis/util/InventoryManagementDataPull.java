/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.InventoryManagementDaoImpl;
import com.odkdhis.model.dashboard.InventoryManagement;
import java.util.Objects;
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
public class InventoryManagementDataPull extends CommonUtil {

    @Autowired
    private InventoryManagementDaoImpl inventoryDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
   @Scheduled(cron ="10 48 * * * ?", zone = "Africa/Nairobi")
    //@Scheduled(cron ="20 * * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionInventory() {
        try {
            //basicInventoryManagement("Inventory_Management");
            //Thread.sleep(2000);
            //inventoryManagementAddenda("Inventory_Management_Addenda");
            
            inventoryManagementCombined("Inventory_Management_combined");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }
/*
    private void basicInventoryManagement(String formId) throws Exception {
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
            if (inventorymgtDao.getRecordByInstanceId(instanceId) != null) {
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
           
            
            try {
             
                
                JSONObject jsonObjectCountyFacility = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectCountyFacility = datajson.getJSONObject("facility_information");
            }
            JSONObject jsonObjectAl6s = null;
            if (!datajson.isNull("AL_6s")) {
                jsonObjectAl6s = datajson.getJSONObject("AL_6s");
            }
            JSONObject jsonObjectdmpa = null;
            if (!datajson.isNull("dmpa")) {
                jsonObjectdmpa = datajson.getJSONObject("dmpa");
            }
            JSONObject jsonObjectoxytocin = null;
            if (!datajson.isNull("oxytocin")) {
                jsonObjectoxytocin = datajson.getJSONObject("oxytocin");
            }
            JSONObject jsonObjectllins = null;
            if (!datajson.isNull("llins")) {
                jsonObjectllins = datajson.getJSONObject("llins");
            }
            JSONObject jsonObjecthivs = null;
            if (!datajson.isNull("hivs")) {
                jsonObjecthivs = datajson.getJSONObject("hivs");
            }
            JSONObject jsonObjectabc = null;
            if (!datajson.isNull("abc")) {
                jsonObjectabc = datajson.getJSONObject("abc");
            }
            JSONObject jsonObjectAmox = null;
            if (!datajson.isNull("amoxicillin")) {
                jsonObjectAmox = datajson.getJSONObject("amoxicillin");
            }
            JSONObject jsonObjectlatex = null;
            if (!datajson.isNull("latex")) {
                jsonObjectlatex = datajson.getJSONObject("latex");
            }
            JSONObject jsonObjectartesunate = null;
            if (!datajson.isNull("artesunate")) {
                jsonObjectartesunate = datajson.getJSONObject("artesunate");
            }
            JSONObject jsonObjectmalaria = null;
            if (!datajson.isNull("malaria")) {
                jsonObjectmalaria = datajson.getJSONObject("malaria");
            }
            JSONObject jsonObjectmmr = null;
            if (!datajson.isNull("mmr")) {
                jsonObjectmmr = datajson.getJSONObject("mmr");
            }
            JSONObject jsonObjectadrenaline = null;
            if (!datajson.isNull("adrenaline")) {
                jsonObjectadrenaline = datajson.getJSONObject("adrenaline");
            }
            JSONObject jsonObjectdtg = null;
            if (!datajson.isNull("dtg")) {
                jsonObjectdtg = datajson.getJSONObject("dtg");
            }
            JSONObject jsonObjectpar = null;
            if (!datajson.isNull("paracetamol")) {
                jsonObjectpar = datajson.getJSONObject("paracetamol");
            }
            JSONObject jsonObjectImplant = null;
            if (!datajson.isNull("implant1")) {
                jsonObjectImplant = datajson.getJSONObject("implant1");
            }
            JSONObject jsonObjectors = null;
            if (!datajson.isNull("ors")) {
                jsonObjectors = datajson.getJSONObject("ors");
            }

            JSONObject jsonObjecttdf = null;
            if (!datajson.isNull("tdf")) {
                jsonObjecttdf = datajson.getJSONObject("tdf");
            }

            Integer empty = 0;

            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");
            
           //String cardAvailability = jsonObjectAl6s.getString("sCardAvailableAL6s");
            String cardAvailability = jsonObjectAl6s.isNull("sCardAvailableAL6s") ? "" : jsonObjectAl6s.getString("sCardAvailableAL6s");
            Integer alCounts = getIntegerFromJsonKey(jsonObjectAl6s, "StocksAvailableAL6s");
            Integer daysoutStock = getIntegerFromJsonKey(jsonObjectAl6s, "daysOutOfStockAL6s");
            Integer cardbalance = getIntegerFromJsonKey(jsonObjectAl6s, "stockCardBalanceAL6s");
            Integer amcvalue = getIntegerFromJsonKey(jsonObjectAl6s, "StockCardIssuesAL6s");
            String expiries = jsonObjectAl6s.isNull("expiry6MonthsAL6s") ? "" : jsonObjectAl6s.getString("expiry6MonthsAL6s");
            Integer actualcount = getIntegerFromJsonKey(jsonObjectAl6s, "physicalStockAL6s");
            //Integer cmosone = (datajson.isNull("currentMOSAL6s")) ? empty :datajson.get("currentMOSAL6s");
            //Integer cmostwo = (datajson.isNull("currentMOS2AL6s")) ? empty :datajson.getInt("currentMOS2AL6s");
            Integer cmosone = getIntegerFromJsonKey(datajson, "currentMOSAL6s");
            Integer cmostwo = getIntegerFromJsonKey(datajson, "currentMOS2AL6s");
            Integer stcardscore = (datajson.isNull("stockCardScoreAL6s")) ? empty : datajson.getInt("stockCardScoreAL6s");
            Integer stCardsCountScore = (datajson.isNull("stockCardsCountScoreAL6s")) ? empty : datajson.getInt("stockCardsCountScoreAL6s");
            Integer daysofStockScore = (datajson.isNull("daysOutOfStockScoreAL6s")) ? empty : datajson.getInt("daysOutOfStockScoreAL6s");
            Integer iDenominator = (datajson.isNull("itemDenominatorAL6s")) ? empty : datajson.getInt("itemDenominatorAL6s");
            Integer iNumerator = (datajson.isNull("itemNumeratorAL6s")) ? empty : datajson.getInt("itemNumeratorAL6s");
            
            Integer numerator = (datajson.isNull("totalNumerator")) ? empty : datajson.getInt("totalNumerator");
            Integer denominator = (datajson.isNull("totalDenominator")) ? empty : datajson.getInt("totalDenominator");
            Integer score = (datajson.isNull("finaScore")) ? empty : datajson.getInt("finaScore");
            
            InventoryManagement al6s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 6s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailability).stCountsDone(alCounts).dayOutStock(daysoutStock).cardBalance(cardbalance).amc(amcvalue)
                    .expires6mo(expiries).actualCount(actualcount).cmos1(cmosone).cmos2(cmostwo).stCardScore(stcardscore)
                    .stCardsCountScore(stCardsCountScore).daysofStockScore(daysofStockScore).iDenominator(iDenominator).iNumerator(iNumerator)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();
            try {
                inventoryDao.save(al6s);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Duplication");
            } 

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al6s); 

            String cardAvailabilityartInj = jsonObjectartesunate.isNull("sCardAvailableartInj") ? "" : jsonObjectartesunate.getString("sCardAvailableartInj");
            Integer alCountsartInj = getIntegerFromJsonKey(jsonObjectartesunate, "StocksAvailableartInj");
            Integer daysoutStockartInj = getIntegerFromJsonKey(jsonObjectartesunate, "daysOutOfStockartInj");
            Integer cardbalanceartInj = getIntegerFromJsonKey(jsonObjectartesunate, "stockCardBalanceartInj");
            Integer amcvalueartInj = getIntegerFromJsonKey(jsonObjectartesunate, "StockCardIssuesartInj");
            String expiriesartInj = jsonObjectartesunate.isNull("expiry6MonthsartInj") ? "" : jsonObjectartesunate.getString("expiry6MonthsartInj");
            Integer actualcountartInj = getIntegerFromJsonKey(jsonObjectartesunate, "physicalStockartInj");
            //Integer cmosoneartInj = (datajson.isNull("currentMOSartInj")) ? empty :datajson.getInt("currentMOSartInj");
            //Integer cmostwoartInj = (datajson.isNull("currentMOS2artInj")) ? empty :datajson.getInt("currentMOS2artInj");
            Integer cmosoneartInj = getIntegerFromJsonKey(datajson, "currentMOSartInj");
            Integer cmostwoartInj = getIntegerFromJsonKey(datajson, "currentMOS2artInj");
            Integer stcardscoreartInj = (datajson.isNull("stockCardScoreartInj")) ? empty : datajson.getInt("stockCardScoreartInj");
            Integer stCardsCountScoreartInj = (datajson.isNull("stockCardsCountScoreartInj")) ? empty : datajson.getInt("stockCardsCountScoreartInj");
            Integer daysofStockScoreartInj = (datajson.isNull("daysOutOfStockScoreartInj")) ? empty : datajson.getInt("daysOutOfStockScoreartInj");
            Integer iDenominatorartInj = (datajson.isNull("itemDenominatorartInj")) ? empty : datajson.getInt("itemDenominatorartInj");
            Integer iNumeratorartInj = (datajson.isNull("itemNumeratorartInj")) ? empty : datajson.getInt("itemNumeratorartInj");

            InventoryManagement artInj = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("artesunate inj").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityartInj).stCountsDone(alCountsartInj).dayOutStock(daysoutStockartInj).cardBalance(cardbalanceartInj).amc(amcvalueartInj)
                    .expires6mo(expiriesartInj).actualCount(actualcountartInj).cmos1(cmosoneartInj).cmos2(cmostwoartInj).stCardScore(stcardscoreartInj)
                    .stCardsCountScore(stCardsCountScoreartInj).daysofStockScore(daysofStockScoreartInj).iDenominator(iDenominatorartInj).iNumerator(iNumeratorartInj)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(artInj);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityRDTs = jsonObjectmalaria.isNull("sCardAvailableRDTs") ? "" : jsonObjectmalaria.getString("sCardAvailableRDTs");
            Integer alCountsRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "StocksAvailableRDTs");
            Integer daysoutStockRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "daysOutOfStockRDTs");
            Integer cardbalanceRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "stockCardBalanceRDTs");
            Integer amcvalueRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "StockCardIssuesRDTs");
            String expiriesRDTs = jsonObjectmalaria.isNull("expiry6MonthsRDTs") ? "" : jsonObjectmalaria.getString("expiry6MonthsRDTs");
            Integer actualcountRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "physicalStockRDTs");
            //Integer cmosoneRDTs = (datajson.isNull("currentMOSRDTs")) ? empty :datajson.getInt("currentMOSRDTs");
            //Integer cmostwoRDTs = (datajson.isNull("currentMOS2RDTs")) ? empty :datajson.getInt("currentMOS2RDTs");
            Integer cmosoneRDTs = getIntegerFromJsonKey(datajson, "currentMOSRDTs");
            Integer cmostwoRDTs = getIntegerFromJsonKey(datajson, "currentMOS2RDTs");
            Integer stcardscoreRDTs = (datajson.isNull("stockCardScoreRDTs")) ? empty : datajson.getInt("stockCardScoreRDTs");
            Integer stCardsCountScoreRDTs = (datajson.isNull("stockCardsCountScoreRDTs")) ? empty : datajson.getInt("stockCardsCountScoreRDTs");
            Integer daysofStockScoreRDTs = (datajson.isNull("daysOutOfStockScoreRDTs")) ? empty : datajson.getInt("daysOutOfStockScoreRDTs");
            Integer iDenominatorRDTs = (datajson.isNull("itemDenominatorRDTs")) ? empty : datajson.getInt("itemDenominatorRDTs");
            Integer iNumeratorRDTs = (datajson.isNull("itemNumeratorRDTs")) ? empty : datajson.getInt("itemNumeratorRDTs");

            InventoryManagement rdts = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("malaria RDTs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityRDTs).stCountsDone(alCountsRDTs).dayOutStock(daysoutStockRDTs).cardBalance(cardbalanceRDTs).amc(amcvalueRDTs)
                    .expires6mo(expiriesRDTs).actualCount(actualcountRDTs).cmos1(cmosoneRDTs).cmos2(cmostwoRDTs).stCardScore(stcardscoreRDTs)
                    .stCardsCountScore(stCardsCountScoreRDTs).daysofStockScore(daysofStockScoreRDTs).iDenominator(iDenominatorRDTs).iNumerator(iNumeratorRDTs)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(rdts);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityLLINs = jsonObjectllins.isNull("sCardAvailableLLINs") ? "" : jsonObjectllins.getString("sCardAvailableLLINs");
            Integer alCountsLLINs = getIntegerFromJsonKey(jsonObjectllins, "StocksAvailableLLINs");
            Integer daysoutStockLLINs = getIntegerFromJsonKey(jsonObjectllins, "daysOutOfStockLLINs");
            Integer cardbalanceLLINs = getIntegerFromJsonKey(jsonObjectllins, "stockCardBalanceLLINs");
            Integer amcvalueLLINs = getIntegerFromJsonKey(jsonObjectllins, "StockCardIssuesLLINs");
            String expiriesLLINs = jsonObjectllins.isNull("expiry6MonthsLLINs") ? "" : jsonObjectllins.getString("expiry6MonthsLLINs");
            Integer actualcountLLINs = getIntegerFromJsonKey(jsonObjectllins, "physicalStockLLINs");
            Integer cmosoneLLINs = getIntegerFromJsonKey(datajson, "currentMOSLLINs");
            Integer cmostwoLLINs = getIntegerFromJsonKey(datajson, "currentMOS2LLINs");
            Integer stcardscoreLLINs = (datajson.isNull("stockCardScoreLLINs")) ? empty : datajson.getInt("stockCardScoreLLINs");
            Integer stCardsCountScoreLLINs = (datajson.isNull("stockCardsCountScoreLLINs")) ? empty : datajson.getInt("stockCardsCountScoreLLINs");
            Integer daysofStockScoreLLINs = (datajson.isNull("daysOutOfStockScoreLLINs")) ? empty : datajson.getInt("daysOutOfStockScoreLLINs");
            Integer iDenominatorLLINs = (datajson.isNull("itemDenominatorLLINs")) ? empty : datajson.getInt("itemDenominatorLLINs");
            Integer iNumeratorLLINs = (datajson.isNull("itemNumeratorLLINs")) ? empty : datajson.getInt("itemNumeratorLLINs");

            InventoryManagement llins = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("LLINs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityLLINs).stCountsDone(alCountsLLINs).dayOutStock(daysoutStockLLINs).cardBalance(cardbalanceLLINs).amc(amcvalueLLINs)
                    .expires6mo(expiriesLLINs).actualCount(actualcountLLINs).cmos1(cmosoneLLINs).cmos2(cmostwoLLINs).stCardScore(stcardscoreLLINs)
                    .stCardsCountScore(stCardsCountScoreLLINs).daysofStockScore(daysofStockScoreLLINs).iDenominator(iDenominatorLLINs).iNumerator(iNumeratorLLINs)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(llins);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityDMPA = jsonObjectdmpa.isNull("sCardAvailableDMPA") ? "" : jsonObjectdmpa.getString("sCardAvailableDMPA");
            Integer alCountsDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "StocksAvailableDMPA");
            Integer daysoutStockDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "daysOutOfStockDMPA");
            Integer cardbalanceDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "stockCardBalanceDMPA");
            Integer amcvalueDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "StockCardIssuesDMPA");
            String expiriesDMPA = jsonObjectdmpa.isNull("expiry6MonthsDMPA") ? "" : jsonObjectdmpa.getString("expiry6MonthsDMPA");
            Integer actualcountDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "physicalStockDMPA");
            //Integer cmosoneDMPA = (datajson.isNull("currentMOSDMPA")) ? empty :datajson.getInt("currentMOSDMPA");
            //Integer cmostwoDMPA = (datajson.isNull("currentMOS2DMPA")) ? empty :datajson.getInt("currentMOS2DMPA");
            Integer cmosoneDMPA = getIntegerFromJsonKey(datajson, "currentMOSDMPA");
            Integer cmostwoDMPA = getIntegerFromJsonKey(datajson, "currentMOS2DMPA");
            Integer stcardscoreDMPA = (datajson.isNull("stockCardScoreDMPA")) ? empty : datajson.getInt("stockCardScoreDMPA");
            Integer stCardsCountScoreDMPA = (datajson.isNull("stockCardsCountScoreDMPA")) ? empty : datajson.getInt("stockCardsCountScoreDMPA");
            Integer daysofStockScoreDMPA = (datajson.isNull("daysOutOfStockScoreDMPA")) ? empty : datajson.getInt("daysOutOfStockScoreDMPA");
            Integer iDenominatorDMPA = (datajson.isNull("itemDenominatorDMPA")) ? empty : datajson.getInt("itemDenominatorDMPA");
            Integer iNumeratorDMPA = (datajson.isNull("itemNumeratorDMPA")) ? empty : datajson.getInt("itemNumeratorDMPA");

            InventoryManagement dmpa = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("DMPA inj").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityDMPA).stCountsDone(alCountsDMPA).dayOutStock(daysoutStockDMPA).cardBalance(cardbalanceDMPA).amc(amcvalueDMPA)
                    .expires6mo(expiriesDMPA).actualCount(actualcountDMPA).cmos1(cmosoneDMPA).cmos2(cmostwoDMPA).stCardScore(stcardscoreDMPA)
                    .stCardsCountScore(stCardsCountScoreDMPA).daysofStockScore(daysofStockScoreDMPA).iDenominator(iDenominatorDMPA).iNumerator(iNumeratorDMPA)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(dmpa);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailability1rod = jsonObjectImplant.isNull("sCardAvailable1rod") ? "" : jsonObjectImplant.getString("sCardAvailable1rod");
            Integer alCounts1rod = getIntegerFromJsonKey(jsonObjectImplant, "StocksAvailable1rod");
            Integer daysoutStock1rod = getIntegerFromJsonKey(jsonObjectImplant, "daysOutOfStock1rod");
            Integer cardbalance1rod = getIntegerFromJsonKey(jsonObjectImplant, "stockCardBalance1rod");
            Integer amcvalue1rod = getIntegerFromJsonKey(jsonObjectImplant, "StockCardIssues1rod");
            String expiries1rod = jsonObjectImplant.isNull("expiry6Months1rod") ? "" : jsonObjectImplant.getString("expiry6Months1rod");
            Integer actualcount1rod = getIntegerFromJsonKey(jsonObjectImplant, "physicalStock1rod");
            //Integer cmosone1rod = (datajson.isNull("currentMOS1rod")) ? empty :datajson.getInt("currentMOS1rod");
            //Integer cmostwo1rod = (datajson.isNull("currentMOS21rod")) ? empty :datajson.getInt("currentMOS21rod");
            Integer cmosone1rod = getIntegerFromJsonKey(datajson, "currentMOS1rod");
            Integer cmostwo1rod = getIntegerFromJsonKey(datajson, "currentMOS21rod");
            Integer stcardscore1rod = (datajson.isNull("stockCardScore1rod")) ? empty : datajson.getInt("stockCardScore1rod");
            Integer stCardsCountScore1rod = (datajson.isNull("stockCardsCountScore1rod")) ? empty : datajson.getInt("stockCardsCountScore1rod");
            Integer daysofStockScore1rod = (datajson.isNull("daysOutOfStockScore1rod")) ? empty : datajson.getInt("daysOutOfStockScore1rod");
            Integer iDenominator1rod = (datajson.isNull("itemDenominator1rod")) ? empty : datajson.getInt("itemDenominator1rod");
            Integer iNumerator1rod = (datajson.isNull("itemNumerator1rod")) ? empty : datajson.getInt("itemNumerator1rod");

            InventoryManagement rod1 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("1 rod implant").orgUnit(orgUnit)
                    .stCardAvail(cardAvailability1rod).stCountsDone(alCounts1rod).dayOutStock(daysoutStock1rod).cardBalance(cardbalance1rod).amc(amcvalue1rod)
                    .expires6mo(expiries1rod).actualCount(actualcount1rod).cmos1(cmosone1rod).cmos2(cmostwo1rod).stCardScore(stcardscore1rod)
                    .stCardsCountScore(stCardsCountScore1rod).daysofStockScore(daysofStockScore1rod).iDenominator(iDenominator1rod).iNumerator(iNumerator1rod)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(rod1);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityoxy = jsonObjectoxytocin.isNull("sCardAvailableoxy") ? "" : jsonObjectoxytocin.getString("sCardAvailableoxy");
            Integer alCountsoxy = getIntegerFromJsonKey(jsonObjectoxytocin, "StocksAvailableoxy");
            Integer daysoutStockoxy = getIntegerFromJsonKey(jsonObjectoxytocin, "daysOutOfStockoxy");
            Integer cardbalanceoxy = getIntegerFromJsonKey(jsonObjectoxytocin, "stockCardBalanceoxy");
            Integer amcvalueoxy = getIntegerFromJsonKey(jsonObjectoxytocin, "StockCardIssuesoxy");
            String expiriesoxy = jsonObjectoxytocin.isNull("expiry6Monthsoxy") ? "" : jsonObjectoxytocin.getString("expiry6Monthsoxy");
            Integer actualcountoxy = getIntegerFromJsonKey(jsonObjectoxytocin, "physicalStockoxy");
            //Integer cmosoneoxy = (datajson.isNull("currentMOSoxy")) ? empty :datajson.getInt("currentMOSoxy");
            //Integer cmostwooxy = (datajson.isNull("currentMOS2oxy")) ? empty :datajson.getInt("currentMOS2oxy");
            Integer cmosoneoxy = getIntegerFromJsonKey(datajson, "currentMOSoxy");
            Integer cmostwooxy = getIntegerFromJsonKey(datajson, "currentMOS2oxy");
            Integer stcardscoreoxy = (datajson.isNull("stockCardScoreoxy")) ? empty : datajson.getInt("stockCardScoreoxy");
            Integer stCardsCountScoreoxy = (datajson.isNull("stockCardsCountScoreoxy")) ? empty : datajson.getInt("stockCardsCountScoreoxy");
            Integer daysofStockScoreoxy = (datajson.isNull("daysOutOfStockScoreoxy")) ? empty : datajson.getInt("daysOutOfStockScoreoxy");
            Integer iDenominatoroxy = (datajson.isNull("itemDenominatoroxy")) ? empty : datajson.getInt("itemDenominatoroxy");
            Integer iNumeratoroxy = (datajson.isNull("itemNumeratoroxy")) ? empty : datajson.getInt("itemNumeratoroxy");

            InventoryManagement oxy = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("oxytocin inj").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityoxy).stCountsDone(alCountsoxy).dayOutStock(daysoutStockoxy).cardBalance(cardbalanceoxy).amc(amcvalueoxy)
                    .expires6mo(expiriesoxy).actualCount(actualcountoxy).cmos1(cmosoneoxy).cmos2(cmostwooxy).stCardScore(stcardscoreoxy)
                    .stCardsCountScore(stCardsCountScoreoxy).daysofStockScore(daysofStockScoreoxy).iDenominator(iDenominatoroxy).iNumerator(iNumeratoroxy)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(oxy);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityORS = jsonObjectors.isNull("sCardAvailableORS") ? "" : jsonObjectors.getString("sCardAvailableORS");
            Integer alCountsORS = getIntegerFromJsonKey(jsonObjectors, "StocksAvailableORS");
            Integer daysoutStockORS = getIntegerFromJsonKey(jsonObjectors, "daysOutOfStockORS");
            Integer cardbalanceORS = getIntegerFromJsonKey(jsonObjectors, "stockCardBalanceORS");
            Integer amcvalueORS = getIntegerFromJsonKey(jsonObjectors, "StockCardIssuesORS");
            String expiriesORS = jsonObjectors.isNull("expiry6MonthsORS") ? "" : jsonObjectors.getString("expiry6MonthsORS");
            Integer actualcountORS = getIntegerFromJsonKey(jsonObjectors, "physicalStockORS");
            //Integer cmosoneORS = (datajson.isNull("currentMOSORS")) ? empty :datajson.getInt("currentMOSORS");
            //Integer cmostwoORS = (datajson.isNull("currentMOS2ORS")) ? empty :datajson.getInt("currentMOS2ORS");
            Integer cmosoneORS = getIntegerFromJsonKey(datajson, "currentMOSORS");
            Integer cmostwoORS = getIntegerFromJsonKey(datajson, "currentMOS2ORS");
            Integer stcardscoreORS = (datajson.isNull("stockCardScoreORS")) ? empty : datajson.getInt("stockCardScoreORS");
            Integer stCardsCountScoreORS = (datajson.isNull("stockCardsCountScoreORS")) ? empty : datajson.getInt("stockCardsCountScoreORS");
            Integer daysofStockScoreORS = (datajson.isNull("daysOutOfStockScoreORS")) ? empty : datajson.getInt("daysOutOfStockScoreORS");
            Integer iDenominatorORS = (datajson.isNull("itemDenominatorORS")) ? empty : datajson.getInt("itemDenominatorORS");
            Integer iNumeratorORS = (datajson.isNull("itemNumeratorORS")) ? empty : datajson.getInt("itemNumeratorORS");

            InventoryManagement ors = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("ORS-Zinc co-pack").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityORS).stCountsDone(alCountsORS).dayOutStock(daysoutStockORS).cardBalance(cardbalanceORS).amc(amcvalueORS)
                    .expires6mo(expiriesORS).actualCount(actualcountORS).cmos1(cmosoneORS).cmos2(cmostwoORS).stCardScore(stcardscoreORS)
                    .stCardsCountScore(stCardsCountScoreORS).daysofStockScore(daysofStockScoreORS).iDenominator(iDenominatorORS).iNumerator(iNumeratorORS)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(ors);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityTDF = jsonObjecttdf.isNull("sCardAvailableTDF") ? "" : jsonObjecttdf.getString("sCardAvailableTDF");
            Integer alCountsTDF = getIntegerFromJsonKey(jsonObjecttdf, "StocksAvailableTDF");
            Integer daysoutStockTDF = getIntegerFromJsonKey(jsonObjecttdf, "daysOutOfStockTDF");
            Integer cardbalanceTDF = getIntegerFromJsonKey(jsonObjecttdf, "stockCardBalanceTDF");
            Integer amcvalueTDF = getIntegerFromJsonKey(jsonObjecttdf, "StockCardIssuesTDF");
            String expiriesTDF = jsonObjecttdf.isNull("expiry6MonthsTDF") ? "" : jsonObjecttdf.getString("expiry6MonthsTDF");
            Integer actualcountTDF = getIntegerFromJsonKey(jsonObjecttdf, "physicalStockTDF");
            //Integer cmosoneTDF = (datajson.isNull("currentMOSTDF")) ? empty :datajson.getInt("currentMOSTDF");
            //Integer cmostwoTDF = (datajson.isNull("currentMOS2TDF")) ? empty :datajson.getInt("currentMOS2TDF");
            Integer cmosoneTDF = getIntegerFromJsonKey(datajson, "currentMOSTDF");
            Integer cmostwoTDF = getIntegerFromJsonKey(datajson, "currentMOS2TDF");
            Integer stcardscoreTDF = (datajson.isNull("stockCardScoreTDF")) ? empty : datajson.getInt("stockCardScoreTDF");
            Integer stCardsCountScoreTDF = (datajson.isNull("stockCardsCountScoreTDF")) ? empty : datajson.getInt("stockCardsCountScoreTDF");
            Integer daysofStockScoreTDF = (datajson.isNull("daysOutOfStockScoreTDF")) ? empty : datajson.getInt("daysOutOfStockScoreTDF");
            Integer iDenominatorTDF = (datajson.isNull("itemDenominatorTDF")) ? empty : datajson.getInt("itemDenominatorTDF");
            Integer iNumeratorTDF = (datajson.isNull("itemNumeratorTDF")) ? empty : datajson.getInt("itemNumeratorTDF");

            InventoryManagement tdf = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("TDF/3TC/EFV adult").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityTDF).stCountsDone(alCountsTDF).dayOutStock(daysoutStockTDF).cardBalance(cardbalanceTDF).amc(amcvalueTDF)
                    .expires6mo(expiriesTDF).actualCount(actualcountTDF).cmos1(cmosoneTDF).cmos2(cmostwoTDF).stCardScore(stcardscoreTDF)
                    .stCardsCountScore(stCardsCountScoreTDF).daysofStockScore(daysofStockScoreTDF).iDenominator(iDenominatorTDF).iNumerator(iNumeratorTDF)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(tdf);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityABC = jsonObjectabc.isNull("sCardAvailableABC") ? "" : jsonObjectabc.getString("sCardAvailableABC");
            Integer alCountsABC = getIntegerFromJsonKey(jsonObjectabc, "StocksAvailableABC");
            Integer daysoutStockABC = getIntegerFromJsonKey(jsonObjectabc, "daysOutOfStockABC");
            Integer cardbalanceABC = getIntegerFromJsonKey(jsonObjectabc, "stockCardBalanceABC");
            Integer amcvalueABC = getIntegerFromJsonKey(jsonObjectabc, "StockCardIssuesABC");
            String expiriesABC = jsonObjectabc.isNull("expiry6MonthsABC") ? "" : jsonObjectabc.getString("expiry6MonthsABC");
            Integer actualcountABC = getIntegerFromJsonKey(jsonObjectabc, "physicalStockABC");
            //Integer cmosoneABC = (datajson.isNull("currentMOSABC")) ? empty :datajson.getInt("currentMOSABC");
            //Integer cmostwoABC = (datajson.isNull("currentMOS2ABC")) ? empty :datajson.getInt("currentMOS2ABC");
            Integer cmosoneABC = getIntegerFromJsonKey(datajson, "currentMOSABC");
            Integer cmostwoABC = getIntegerFromJsonKey(datajson, "currentMOS2ABC");
            Integer stcardscoreABC = (datajson.isNull("stockCardScoreABC")) ? empty : datajson.getInt("stockCardScoreABC");
            Integer stCardsCountScoreABC = (datajson.isNull("stockCardsCountScoreABC")) ? empty : datajson.getInt("stockCardsCountScoreABC");
            Integer daysofStockScoreABC = (datajson.isNull("daysOutOfStockScoreABC")) ? empty : datajson.getInt("daysOutOfStockScoreABC");
            Integer iDenominatorABC = (datajson.isNull("itemDenominatorABC")) ? empty : datajson.getInt("itemDenominatorABC");
            Integer iNumeratorABC = (datajson.isNull("itemNumeratorABC")) ? empty : datajson.getInt("itemNumeratorABC");

            InventoryManagement abc = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("ABC/3TC paed").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityABC).stCountsDone(alCountsABC).dayOutStock(daysoutStockABC).cardBalance(cardbalanceABC).amc(amcvalueABC)
                    .expires6mo(expiriesABC).actualCount(actualcountABC).cmos1(cmosoneABC).cmos2(cmostwoABC).stCardScore(stcardscoreABC)
                    .stCardsCountScore(stCardsCountScoreABC).daysofStockScore(daysofStockScoreABC).iDenominator(iDenominatorABC).iNumerator(iNumeratorABC)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(abc);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityDTG = jsonObjectdtg.isNull("sCardAvailableDTG") ? "" : jsonObjectdtg.getString("sCardAvailableDTG");
            Integer alCountsDTG = getIntegerFromJsonKey(jsonObjectdtg, "StocksAvailableDTG");
            Integer daysoutStockDTG = getIntegerFromJsonKey(jsonObjectdtg, "daysOutOfStockDTG");
            Integer cardbalanceDTG = getIntegerFromJsonKey(jsonObjectdtg, "stockCardBalanceDTG");
            Integer amcvalueDTG = getIntegerFromJsonKey(jsonObjectdtg, "StockCardIssuesDTG");
            String expiriesDTG = jsonObjectdtg.isNull("expiry6MonthsDTG") ? "" : jsonObjectdtg.getString("expiry6MonthsDTG");
            Integer actualcountDTG = getIntegerFromJsonKey(jsonObjectdtg, "physicalStockDTG");
            //Integer cmosoneDTG = (datajson.isNull("currentMOSDTG")) ? empty :datajson.getInt("currentMOSDTG");
            //Integer cmostwoDTG = (datajson.isNull("currentMOS2DTG")) ? empty :datajson.getInt("currentMOS2DTG");
            Integer cmosoneDTG = getIntegerFromJsonKey(datajson, "currentMOSDTG");
            Integer cmostwoDTG = getIntegerFromJsonKey(datajson, "currentMOS2DTG");
            Integer stcardscoreDTG = (datajson.isNull("stockCardScoreDTG")) ? empty : datajson.getInt("stockCardScoreDTG");
            Integer stCardsCountScoreDTG = (datajson.isNull("stockCardsCountScoreDTG")) ? empty : datajson.getInt("stockCardsCountScoreDTG");
            Integer daysofStockScoreDTG = (datajson.isNull("daysOutOfStockScoreDTG")) ? empty : datajson.getInt("daysOutOfStockScoreDTG");
            Integer iDenominatorDTG = (datajson.isNull("itemDenominatorDTG")) ? empty : datajson.getInt("itemDenominatorDTG");
            Integer iNumeratorDTG = (datajson.isNull("itemNumeratorDTG")) ? empty : datajson.getInt("itemNumeratorDTG");

            InventoryManagement dtg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("TDF/3TC/DTG adult").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityDTG).stCountsDone(alCountsDTG).dayOutStock(daysoutStockDTG).cardBalance(cardbalanceDTG).amc(amcvalueDTG)
                    .expires6mo(expiriesDTG).actualCount(actualcountDTG).cmos1(cmosoneDTG).cmos2(cmostwoDTG).stCardScore(stcardscoreDTG)
                    .stCardsCountScore(stCardsCountScoreDTG).daysofStockScore(daysofStockScoreDTG).iDenominator(iDenominatorDTG).iNumerator(iNumeratorDTG)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(dtg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityHIV = jsonObjecthivs.isNull("sCardAvailableHIV") ? "" : jsonObjecthivs.getString("sCardAvailableHIV");
            Integer alCountsHIV = getIntegerFromJsonKey(jsonObjecthivs, "StocksAvailableHIV");
            Integer daysoutStockHIV = getIntegerFromJsonKey(jsonObjecthivs, "daysOutOfStockHIV");
            Integer cardbalanceHIV = getIntegerFromJsonKey(jsonObjecthivs, "stockCardBalanceHIV");
            Integer amcvalueHIV = getIntegerFromJsonKey(jsonObjecthivs, "StockCardIssuesHIV");
            String expiriesHIV = jsonObjecthivs.isNull("expiry6MonthsHIV") ? "" : jsonObjecthivs.getString("expiry6MonthsHIV");
            Integer actualcountHIV = getIntegerFromJsonKey(jsonObjecthivs, "physicalStockHIV");
            //Integer cmosoneHIV = (datajson.isNull("currentMOSHIV")) ? empty :datajson.getInt("currentMOSHIV");
            //Integer cmostwoHIV = (datajson.isNull("currentMOS2HIV")) ? empty :datajson.getInt("currentMOS2HIV");
            Integer cmosoneHIV = getIntegerFromJsonKey(datajson, "currentMOSHIV");
            Integer cmostwoHIV = getIntegerFromJsonKey(datajson, "currentMOS2HIV");
            Integer stcardscoreHIV = (datajson.isNull("stockCardScoreHIV")) ? empty : datajson.getInt("stockCardScoreHIV");
            Integer stCardsCountScoreHIV = (datajson.isNull("stockCardsCountScoreHIV")) ? empty : datajson.getInt("stockCardsCountScoreHIV");
            Integer daysofStockScoreHIV = (datajson.isNull("daysOutOfStockScoreHIV")) ? empty : datajson.getInt("daysOutOfStockScoreHIV");
            Integer iDenominatorHIV = (datajson.isNull("itemDenominatorHIV")) ? empty : datajson.getInt("itemDenominatorHIV");
            Integer iNumeratorHIV = (datajson.isNull("itemNumeratorHIV")) ? empty : datajson.getInt("itemNumeratorHIV");

            InventoryManagement hiv = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("HIV screening test").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityHIV).stCountsDone(alCountsHIV).dayOutStock(daysoutStockHIV).cardBalance(cardbalanceHIV).amc(amcvalueHIV)
                    .expires6mo(expiriesHIV).actualCount(actualcountHIV).cmos1(cmosoneHIV).cmos2(cmostwoHIV).stCardScore(stcardscoreHIV)
                    .stCardsCountScore(stCardsCountScoreHIV).daysofStockScore(daysofStockScoreHIV).iDenominator(iDenominatorHIV).iNumerator(iNumeratorHIV)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(hiv);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilitypar = jsonObjectpar.isNull("sCardAvailablepar") ? "" : jsonObjectpar.getString("sCardAvailablepar");
            Integer alCountspar = getIntegerFromJsonKey(jsonObjectpar, "StocksAvailablepar");
            Integer daysoutStockpar = getIntegerFromJsonKey(jsonObjectpar, "daysOutOfStockpar");
            Integer cardbalancepar = getIntegerFromJsonKey(jsonObjectpar, "stockCardBalancepar");
            Integer amcvaluepar = getIntegerFromJsonKey(jsonObjectpar, "StockCardIssuespar");
            String expiriespar = jsonObjectpar.isNull("expiry6Monthspar") ? "" : jsonObjectpar.getString("expiry6Monthspar");
            Integer actualcountpar = getIntegerFromJsonKey(jsonObjectpar, "physicalStockpar");
            //Integer cmosonepar = (datajson.isNull("currentMOSpar")) ? empty :datajson.getInt("currentMOSpar");
            //Integer cmostwopar = (datajson.isNull("currentMOS2par")) ? empty :datajson.getInt("currentMOS2par");
            Integer cmosonepar = getIntegerFromJsonKey(datajson, "currentMOSpar");
            Integer cmostwopar = getIntegerFromJsonKey(datajson, "currentMOS2par");
            Integer stcardscorepar = (datajson.isNull("stockCardScorepar")) ? empty : datajson.getInt("stockCardScorepar");
            Integer stCardsCountScorepar = (datajson.isNull("stockCardsCountScorepar")) ? empty : datajson.getInt("stockCardsCountScorepar");
            Integer daysofStockScorepar = (datajson.isNull("daysOutOfStockScorepar")) ? empty : datajson.getInt("daysOutOfStockScorepar");
            Integer iDenominatorpar = (datajson.isNull("itemDenominatorpar")) ? empty : datajson.getInt("itemDenominatorpar");
            Integer iNumeratorpar = (datajson.isNull("itemNumeratorpar")) ? empty : datajson.getInt("itemNumeratorpar");

            InventoryManagement par = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("paracetamol 500mg tab").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitypar).stCountsDone(alCountspar).dayOutStock(daysoutStockpar).cardBalance(cardbalancepar).amc(amcvaluepar)
                    .expires6mo(expiriespar).actualCount(actualcountpar).cmos1(cmosonepar).cmos2(cmostwopar).stCardScore(stcardscorepar)
                    .stCardsCountScore(stCardsCountScorepar).daysofStockScore(daysofStockScorepar).iDenominator(iDenominatorpar).iNumerator(iNumeratorpar)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(par);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityamox = jsonObjectAmox.isNull("sCardAvailableamox") ? "" : jsonObjectAmox.getString("sCardAvailableamox");
            Integer alCountsamox = getIntegerFromJsonKey(jsonObjectAmox, "StocksAvailableamox");
            Integer daysoutStockamox = getIntegerFromJsonKey(jsonObjectAmox, "daysOutOfStockamox");
            Integer cardbalanceamox = getIntegerFromJsonKey(jsonObjectAmox, "stockCardBalanceamox");
            Integer amcvalueamox = getIntegerFromJsonKey(jsonObjectAmox, "StockCardIssuesamox");
            String expiriesamox = jsonObjectAmox.isNull("expiry6Monthsamox") ? "" : jsonObjectAmox.getString("expiry6Monthsamox");
            Integer actualcountamox = getIntegerFromJsonKey(jsonObjectAmox, "physicalStockamox");
            //Integer cmosoneamox = (datajson.isNull("currentMOSamox")) ? empty :datajson.getInt("currentMOSamox");
            //Integer cmostwoamox = (datajson.isNull("currentMOS2amox")) ? empty :datajson.getInt("currentMOS2amox");
            Integer cmosoneamox = getIntegerFromJsonKey(datajson, "currentMOSamox");
            Integer cmostwoamox = getIntegerFromJsonKey(datajson, "currentMOS2amox");
            Integer stcardscoreamox = (datajson.isNull("stockCardScoreamox")) ? empty : datajson.getInt("stockCardScoreamox");
            Integer stCardsCountScoreamox = (datajson.isNull("stockCardsCountScoreamox")) ? empty : datajson.getInt("stockCardsCountScoreamox");
            Integer daysofStockScoreamox = (datajson.isNull("daysOutOfStockScoreamox")) ? empty : datajson.getInt("daysOutOfStockScoreamox");
            Integer iDenominatoramox = (datajson.isNull("itemDenominatoramox")) ? empty : datajson.getInt("itemDenominatoramox");
            Integer iNumeratoramox = (datajson.isNull("itemNumeratoramox")) ? empty : datajson.getInt("itemNumeratoramox");

            InventoryManagement amox = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("amoxicillin 250mg DT").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityamox).stCountsDone(alCountsamox).dayOutStock(daysoutStockamox).cardBalance(cardbalanceamox).amc(amcvalueamox)
                    .expires6mo(expiriesamox).actualCount(actualcountamox).cmos1(cmosoneamox).cmos2(cmostwoamox).stCardScore(stcardscoreamox)
                    .stCardsCountScore(stCardsCountScoreamox).daysofStockScore(daysofStockScoreamox).iDenominator(iDenominatoramox).iNumerator(iNumeratoramox)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(amox);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityadr = jsonObjectadrenaline.isNull("sCardAvailableadr") ? "" : jsonObjectadrenaline.getString("sCardAvailableadr");
            Integer alCountsadr = getIntegerFromJsonKey(jsonObjectadrenaline, "StocksAvailableadr");
            Integer daysoutStockadr = getIntegerFromJsonKey(jsonObjectadrenaline, "daysOutOfStockadr");
            Integer cardbalanceadr = getIntegerFromJsonKey(jsonObjectadrenaline, "stockCardBalanceadr");
            Integer amcvalueadr = getIntegerFromJsonKey(jsonObjectadrenaline, "StockCardIssuesadr");
            String expiriesadr = jsonObjectadrenaline.isNull("expiry6Monthsadr") ? "" : jsonObjectadrenaline.getString("expiry6Monthsadr");
            Integer actualcountadr = getIntegerFromJsonKey(jsonObjectadrenaline, "physicalStockadr");
            //Integer cmosoneadr = (datajson.isNull("currentMOSadr")) ? empty :datajson.getInt("currentMOSadr");
            //Integer cmostwoadr = (datajson.isNull("currentMOS2adr")) ? empty :datajson.getInt("currentMOS2adr");
            Integer cmosoneadr = getIntegerFromJsonKey(datajson, "currentMOSadr");
            Integer cmostwoadr = getIntegerFromJsonKey(datajson, "currentMOS2adr");
            Integer stcardscoreadr = (datajson.isNull("stockCardScoreadr")) ? empty : datajson.getInt("stockCardScoreadr");
            Integer stCardsCountScoreadr = (datajson.isNull("stockCardsCountScoreadr")) ? empty : datajson.getInt("stockCardsCountScoreadr");
            Integer daysofStockScoreadr = (datajson.isNull("daysOutOfStockScoreadr")) ? empty : datajson.getInt("daysOutOfStockScoreadr");
            Integer iDenominatoradr = (datajson.isNull("itemDenominatoradr")) ? empty : datajson.getInt("itemDenominatoradr");
            Integer iNumeratoradr = (datajson.isNull("itemNumeratoradr")) ? empty : datajson.getInt("itemNumeratoradr");

            InventoryManagement adr = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("adrenaline inj").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityadr).stCountsDone(alCountsadr).dayOutStock(daysoutStockadr).cardBalance(cardbalanceadr).amc(amcvalueadr)
                    .expires6mo(expiriesadr).actualCount(actualcountadr).cmos1(cmosoneadr).cmos2(cmostwoadr).stCardScore(stcardscoreadr)
                    .stCardsCountScore(stCardsCountScoreadr).daysofStockScore(daysofStockScoreadr).iDenominator(iDenominatoradr).iNumerator(iNumeratoradr)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(adr);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityltx = jsonObjectlatex.isNull("sCardAvailableltx") ? "" : jsonObjectlatex.getString("sCardAvailableltx");
            Integer alCountsltx = getIntegerFromJsonKey(jsonObjectlatex, "StocksAvailableltx");
            Integer daysoutStockltx = getIntegerFromJsonKey(jsonObjectlatex, "daysOutOfStockltx");
            Integer cardbalanceltx = getIntegerFromJsonKey(jsonObjectlatex, "stockCardBalanceltx");
            Integer amcvalueltx = getIntegerFromJsonKey(jsonObjectlatex, "StockCardIssuesltx");
            String expiriesltx = jsonObjectlatex.isNull("expiry6Monthsltx") ? "" : jsonObjectlatex.getString("expiry6Monthsltx");
            Integer actualcountltx = getIntegerFromJsonKey(jsonObjectlatex, "physicalStockltx");
            //Integer cmosoneltx = (datajson.isNull("currentMOSltx")) ? empty :datajson.getInt("currentMOSltx");
            //Integer cmostwoltx = (datajson.isNull("currentMOS2ltx")) ? empty :datajson.getInt("currentMOS2ltx");
            Integer cmosoneltx = getIntegerFromJsonKey(datajson, "currentMOSltx");
            Integer cmostwoltx = getIntegerFromJsonKey(datajson, "currentMOS2ltx");
            Integer stcardscoreltx = (datajson.isNull("stockCardScoreltx")) ? empty : datajson.getInt("stockCardScoreltx");
            Integer stCardsCountScoreltx = (datajson.isNull("stockCardsCountScoreltx")) ? empty : datajson.getInt("stockCardsCountScoreltx");
            Integer daysofStockScoreltx = (datajson.isNull("daysOutOfStockScoreltx")) ? empty : datajson.getInt("daysOutOfStockScoreltx");
            Integer iDenominatorltx = (datajson.isNull("itemDenominatorltx")) ? empty : datajson.getInt("itemDenominatorltx");
            Integer iNumeratorltx = (datajson.isNull("itemNumeratorltx")) ? empty : datajson.getInt("itemNumeratorltx");

            InventoryManagement ltex = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("latex gloves").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityltx).stCountsDone(alCountsltx).dayOutStock(daysoutStockltx).cardBalance(cardbalanceltx).amc(amcvalueltx)
                    .expires6mo(expiriesltx).actualCount(actualcountltx).cmos1(cmosoneltx).cmos2(cmostwoltx).stCardScore(stcardscoreltx)
                    .stCardsCountScore(stCardsCountScoreltx).daysofStockScore(daysofStockScoreltx).iDenominator(iDenominatorltx).iNumerator(iNumeratorltx)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(ltex);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityMMR = jsonObjectmmr.isNull("sCardAvailableMMR") ? "" : jsonObjectmmr.getString("sCardAvailableMMR");
            Integer alCountsMMR = getIntegerFromJsonKey(jsonObjectmmr, "StocksAvailableMMR");
            Integer daysoutStockMMR = getIntegerFromJsonKey(jsonObjectmmr, "daysOutOfStockMMR");
            Integer cardbalanceMMR = getIntegerFromJsonKey(jsonObjectmmr, "stockCardBalanceMMR");
            Integer amcvalueMMR = getIntegerFromJsonKey(jsonObjectmmr, "StockCardIssuesMMR");
            String expiriesMMR = jsonObjectmmr.isNull("expiry6MonthsMMR") ? "" : jsonObjectmmr.getString("expiry6MonthsMMR");
            Integer actualcountMMR = getIntegerFromJsonKey(jsonObjectmmr, "physicalStockMMR");
            //Integer cmosoneMMR = (datajson.isNull("currentMOSMMR")) ? empty :datajson.getInt("currentMOSMMR");
            //Integer cmostwoMMR = (datajson.isNull("currentMOS2MMR")) ? empty :datajson.getInt("currentMOS2MMR");
            Integer cmosoneMMR = getIntegerFromJsonKey(datajson, "currentMOSMMR");
            Integer cmostwoMMR = getIntegerFromJsonKey(datajson, "currentMOS2MMR");
            Integer stcardscoreMMR = (datajson.isNull("stockCardScoreMMR")) ? empty : datajson.getInt("stockCardScoreMMR");
            Integer stCardsCountScoreMMR = (datajson.isNull("stockCardsCountScoreMMR")) ? empty : datajson.getInt("stockCardsCountScoreMMR");
            Integer daysofStockScoreMMR = (datajson.isNull("daysOutOfStockScoreMMR")) ? empty : datajson.getInt("daysOutOfStockScoreMMR");
            Integer iDenominatorMMR = (datajson.isNull("itemDenominatorMMR")) ? empty : datajson.getInt("itemDenominatorMMR");
            Integer iNumeratorMMR = (datajson.isNull("itemNumeratorMMR")) ? empty : datajson.getInt("itemNumeratorMMR");

            InventoryManagement mmr = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("MMR vaccine").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityMMR).stCountsDone(alCountsMMR).dayOutStock(daysoutStockMMR).cardBalance(cardbalanceMMR).amc(amcvalueMMR)
                    .expires6mo(expiriesMMR).actualCount(actualcountMMR).cmos1(cmosoneMMR).cmos2(cmostwoMMR).stCardScore(stcardscoreMMR)
                    .stCardsCountScore(stCardsCountScoreMMR).daysofStockScore(daysofStockScoreMMR).iDenominator(iDenominatorMMR).iNumerator(iNumeratorMMR)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();

            try {
                inventoryDao.save(mmr);
            } catch (Exception e) {
                log.info("Duplication");
            }
                         
            } catch (Exception e) {
            }
            

        }

    }
    
    
*/ 
    
    
    //Imventory Management Combined (Revised)
   private void inventoryManagementCombined(String formId) throws Exception {
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
            if (inventorymgtDao.getRecordByInstanceId(instanceId) != null) {
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
           
            
           try {
             
                
            JSONObject jsonObjectCountyFacility = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectCountyFacility = datajson.getJSONObject("facility_information");
            }
            JSONObject jsonObjectAl6s = null;
            if (!datajson.isNull("AL_6s")) {
                jsonObjectAl6s = datajson.getJSONObject("AL_6s");
            }
            JSONObject jsonObjectAl24s = null;
            if (!datajson.isNull("al24s")) {
                jsonObjectAl24s = datajson.getJSONObject("al24s");
            }
            JSONObject jsonObjectartesunate = null;
            if (!datajson.isNull("artesunate")) {
                jsonObjectartesunate = datajson.getJSONObject("artesunate");
            }
            JSONObject jsonObjectmalaria = null;
            if (!datajson.isNull("malaria")) {
                jsonObjectmalaria = datajson.getJSONObject("malaria");
            }
            JSONObject jsonObjectllins = null;
            if (!datajson.isNull("llins")) {
                jsonObjectllins = datajson.getJSONObject("llins");
            }
            JSONObject jsonObjectdmpa = null;
            if (!datajson.isNull("dmpa")) {
                jsonObjectdmpa = datajson.getJSONObject("dmpa");
            }
            JSONObject jsonObjectimplant1 = null;
            if (!datajson.isNull("implant1")) {
                jsonObjectimplant1 = datajson.getJSONObject("implant1");
            }
            JSONObject jsonObjectcalc10 = null;
            if (!datajson.isNull("calc10")) {
                jsonObjectcalc10 = datajson.getJSONObject("calc10");
            }
            JSONObject jsonObjectdexa4mg = null;
            if (!datajson.isNull("dexa4mg")) {
                jsonObjectdexa4mg = datajson.getJSONObject("dexa4mg");
            }
            JSONObject jsonObjectMethyldopa = null;
            if (!datajson.isNull("Methyldopa")) {
                jsonObjectMethyldopa = datajson.getJSONObject("Methyldopa");
            }
            JSONObject jsonObjectphyto1mg = null;
            if (!datajson.isNull("phyto1mg")) {
                jsonObjectphyto1mg = datajson.getJSONObject("phyto1mg");
            }
            JSONObject jsonObjecttdf = null;
            if (!datajson.isNull("tdf")) {
                jsonObjecttdf = datajson.getJSONObject("tdf");
            }
            JSONObject jsonObjectdtg = null;
            if (!datajson.isNull("dtg")) {
                jsonObjectdtg = datajson.getJSONObject("dtg");
            }
            JSONObject jsonObjectabc = null;
            if (!datajson.isNull("abc")) {
                jsonObjectabc = datajson.getJSONObject("abc");
            }
            JSONObject jsonObjecthivs = null;
            if (!datajson.isNull("hivs")) {
                jsonObjecthivs = datajson.getJSONObject("hivs");
            }
            JSONObject jsonObjectoraquick = null;
            if (!datajson.isNull("oraquick")) {
                jsonObjectoraquick = datajson.getJSONObject("oraquick");
            }
            JSONObject jsonObjectlatex = null;
            if (!datajson.isNull("latex")) {
                jsonObjectlatex = datajson.getJSONObject("latex");
            }
            JSONObject jsonObjectmmr = null;
            if (!datajson.isNull("mmr")) {
                jsonObjectmmr = datajson.getJSONObject("mmr");
            }


            Integer empty = 0;

            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");
            
            //String cardAvailability = jsonObjectAl6s.getString("sCardAvailableAL6s");
            String cardAvailability = jsonObjectAl6s.isNull("sCardAvailableAL6s") ? "" : jsonObjectAl6s.getString("sCardAvailableAL6s");
            Integer alCounts = getIntegerFromJsonKey(jsonObjectAl6s, "StocksAvailableAL6s");
            Integer daysoutStock = getIntegerFromJsonKey(jsonObjectAl6s, "daysOutOfStockAL6s");
            Integer cardbalance = getIntegerFromJsonKey(jsonObjectAl6s, "stockCardBalanceAL6s");
            Integer amcvalue = getIntegerFromJsonKey(jsonObjectAl6s, "StockCardIssuesAL6s");
            String expiries = jsonObjectAl6s.isNull("expiry6MonthsAL6s") ? "" : jsonObjectAl6s.getString("expiry6MonthsAL6s");
            Integer actualcount = getIntegerFromJsonKey(jsonObjectAl6s, "physicalStockAL6s");
            //Integer cmosone = (datajson.isNull("currentMOSAL6s")) ? empty :datajson.get("currentMOSAL6s");
            //Integer cmostwo = (datajson.isNull("currentMOS2AL6s")) ? empty :datajson.getInt("currentMOS2AL6s");
            Integer cmosone = getIntegerFromJsonKey(datajson, "currentMOSAL6s");
            Integer cmostwo = getIntegerFromJsonKey(datajson, "currentMOS2AL6s");
            Integer stcardscore = (datajson.isNull("stockCardScoreAL6s")) ? empty : datajson.getInt("stockCardScoreAL6s");
            Integer stCardsCountScore = (datajson.isNull("stockCardsCountScoreAL6s")) ? empty : datajson.getInt("stockCardsCountScoreAL6s");
            //Integer daysofStockScore = (datajson.isNull("daysOutOfStockScoreAL6s")) ? empty : datajson.getInt("daysOutOfStockScoreAL6s");
            Integer iDenominator = (datajson.isNull("itemDenominatorAL6s")) ? empty : datajson.getInt("itemDenominatorAL6s");
            Integer iNumerator = (datajson.isNull("itemNumeratorAL6s")) ? empty : datajson.getInt("itemNumeratorAL6s");
            
            Integer numerator = (datajson.isNull("totalNumerator")) ? empty : datajson.getInt("totalNumerator");
            Integer denominator = (datajson.isNull("totalDenominator")) ? empty : datajson.getInt("totalDenominator");
            Integer score = (datajson.isNull("finaScore")) ? empty : datajson.getInt("finaScore");

            InventoryManagement al6s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 6s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailability).stCountsDone(alCounts).dayOutStock(daysoutStock).cardBalance(cardbalance).amc(amcvalue)
                    .expires6mo(expiries).actualCount(actualcount).cmos1(cmosone).cmos2(cmostwo).stCardScore(stcardscore)
                    .stCardsCountScore(stCardsCountScore).daysofStockScore(0).iDenominator(iDenominator).iNumerator(iNumerator)
                    .numerator(numerator).denominator(denominator).score(score)
                    .build();
            try {
                inventoryDao.save(al6s);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Duplication");
            } 

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al6s); 

            String cardAvailabilityAL24s = jsonObjectAl24s.isNull("sCardAvailableAL24s") ? "" : jsonObjectAl24s.getString("sCardAvailableAL24s");
            Integer alCountsAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "StocksAvailableAL24s");
            Integer daysoutStockAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "daysOutOfStockAL24s");
            Integer cardbalanceAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "stockCardBalanceAL24s");
            Integer amcvalueAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "StockCardIssuesAL24s");
            String expiriesAL24s = jsonObjectAl24s.isNull("expiry6MonthsAL24s") ? "" : jsonObjectAl24s.getString("expiry6MonthsAL24s");
            Integer actualcountAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "physicalStockAL24s");
            //Integer cmosoneAL24s = (datajson.isNull("currentMOSAL24s")) ? empty :datajson.getInt("currentMOSAL24s");
            //Integer cmostwoAL24s = (datajson.isNull("currentMOS2AL24s")) ? empty :datajson.getInt("currentMOS2AL24s");
            Integer cmosoneAL24s = getIntegerFromJsonKey(datajson, "currentMOSAL24s");
            Integer cmostwoAL24s = getIntegerFromJsonKey(datajson, "currentMOS2AL24s");
            Integer stcardscoreAL24s = (datajson.isNull("stockCardScoreAL24s")) ? empty : datajson.getInt("stockCardScoreAL24s");
            Integer stCardsCountScoreAL24s = (datajson.isNull("stockCardsCountScoreAL24s")) ? empty : datajson.getInt("stockCardsCountScoreAL24s");
            //Integer daysofStockScoreAL24s = (datajson.isNull("daysOutOfStockScoreAL24s")) ? empty : datajson.getInt("daysOutOfStockScoreAL24s");
            Integer iDenominatorAL24s = (datajson.isNull("itemDenominatorAL24s")) ? empty : datajson.getInt("itemDenominatorAL24s");
            Integer iNumeratorAL24s = (datajson.isNull("itemNumeratorAL24s")) ? empty : datajson.getInt("itemNumeratorAL24s");

            InventoryManagement AL24s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 24s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityAL24s).stCountsDone(alCountsAL24s).dayOutStock(daysoutStockAL24s).cardBalance(cardbalanceAL24s).amc(amcvalueAL24s)
                    .expires6mo(expiriesAL24s).actualCount(actualcountAL24s).cmos1(cmosoneAL24s).cmos2(cmostwoAL24s).stCardScore(stcardscoreAL24s)
                    .stCardsCountScore(stCardsCountScoreAL24s).daysofStockScore(0).iDenominator(iDenominatorAL24s).iNumerator(iNumeratorAL24s).build();

            try {
                inventoryDao.save(AL24s);
            } catch (Exception e) {
                log.info("Duplication");
           }

            String cardAvailabilityartInj = jsonObjectartesunate.isNull("sCardAvailableartInj") ? "" : jsonObjectartesunate.getString("sCardAvailableartInj");
            Integer alCountsartInj = getIntegerFromJsonKey(jsonObjectartesunate, "StocksAvailableartInj");
            Integer daysoutStockartInj = getIntegerFromJsonKey(jsonObjectartesunate, "daysOutOfStockartInj");
            Integer cardbalanceartInj = getIntegerFromJsonKey(jsonObjectartesunate, "stockCardBalanceartInj");
            Integer amcvalueartInj = getIntegerFromJsonKey(jsonObjectartesunate, "StockCardIssuesartInj");
            String expiriesartInj = jsonObjectartesunate.isNull("expiry6MonthsartInj") ? "" : jsonObjectartesunate.getString("expiry6MonthsartInj");
            Integer actualcountartInj = getIntegerFromJsonKey(jsonObjectartesunate, "physicalStockartInj");
            //Integer cmosoneartInj = (datajson.isNull("currentMOSartInj")) ? empty :datajson.getInt("currentMOSartInj");
            //Integer cmostwoartInj = (datajson.isNull("currentMOS2artInj")) ? empty :datajson.getInt("currentMOS2artInj");
            Integer cmosoneartInj = getIntegerFromJsonKey(datajson, "currentMOSartInj");
            Integer cmostwoartInj = getIntegerFromJsonKey(datajson, "currentMOS2artInj");
            Integer stcardscoreartInj = (datajson.isNull("stockCardScoreartInj")) ? empty : datajson.getInt("stockCardScoreartInj");
            Integer stCardsCountScoreartInj = (datajson.isNull("stockCardsCountScoreartInj")) ? empty : datajson.getInt("stockCardsCountScoreartInj");
            //Integer daysofStockScoreartInj = (datajson.isNull("daysOutOfStockScoreartInj")) ? empty : datajson.getInt("daysOutOfStockScoreartInj");
            Integer iDenominatorartInj = (datajson.isNull("itemDenominatorartInj")) ? empty : datajson.getInt("itemDenominatorartInj");
            Integer iNumeratorartInj = (datajson.isNull("itemNumeratorartInj")) ? empty : datajson.getInt("itemNumeratorartInj");

            InventoryManagement artInj = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("artesunate inj").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityartInj).stCountsDone(alCountsartInj).dayOutStock(daysoutStockartInj).cardBalance(cardbalanceartInj).amc(amcvalueartInj)
                    .expires6mo(expiriesartInj).actualCount(actualcountartInj).cmos1(cmosoneartInj).cmos2(cmostwoartInj).stCardScore(stcardscoreartInj)
                    .stCardsCountScore(stCardsCountScoreartInj).daysofStockScore(0).iDenominator(iDenominatorartInj).iNumerator(iNumeratorartInj).build();

            try {
                inventoryDao.save(artInj);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            
            String cardAvailabilityRDTs = jsonObjectmalaria.isNull("sCardAvailableRDTs") ? "" : jsonObjectmalaria.getString("sCardAvailableRDTs");
            Integer alCountsRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "StocksAvailableRDTs");
            Integer daysoutStockRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "daysOutOfStockRDTs");
            Integer cardbalanceRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "stockCardBalanceRDTs");
            Integer amcvalueRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "StockCardIssuesRDTs");
            String expiriesRDTs = jsonObjectmalaria.isNull("expiry6MonthsRDTs") ? "" : jsonObjectmalaria.getString("expiry6MonthsRDTs");
            Integer actualcountRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "physicalStockRDTs");
            //Integer cmosoneRDTs = (datajson.isNull("currentMOSRDTs")) ? empty :datajson.getInt("currentMOSRDTs");
            //Integer cmostwoRDTs = (datajson.isNull("currentMOS2RDTs")) ? empty :datajson.getInt("currentMOS2RDTs");
            Integer cmosoneRDTs = getIntegerFromJsonKey(datajson, "currentMOSRDTs");
            Integer cmostwoRDTs = getIntegerFromJsonKey(datajson, "currentMOS2RDTs");
            Integer stcardscoreRDTs = (datajson.isNull("stockCardScoreRDTs")) ? empty : datajson.getInt("stockCardScoreRDTs");
            Integer stCardsCountScoreRDTs = (datajson.isNull("stockCardsCountScoreRDTs")) ? empty : datajson.getInt("stockCardsCountScoreRDTs");
            Integer daysofStockScoreRDTs = (datajson.isNull("daysOutOfStockScoreRDTs")) ? empty : datajson.getInt("daysOutOfStockScoreRDTs");
            Integer iDenominatorRDTs = (datajson.isNull("itemDenominatorRDTs")) ? empty : datajson.getInt("itemDenominatorRDTs");
            Integer iNumeratorRDTs = (datajson.isNull("itemNumeratorRDTs")) ? empty : datajson.getInt("itemNumeratorRDTs");

            InventoryManagement rdts = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("malaria RDTs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityRDTs).stCountsDone(alCountsRDTs).dayOutStock(daysoutStockRDTs).cardBalance(cardbalanceRDTs).amc(amcvalueRDTs)
                    .expires6mo(expiriesRDTs).actualCount(actualcountRDTs).cmos1(cmosoneRDTs).cmos2(cmostwoRDTs).stCardScore(stcardscoreRDTs)
                    .stCardsCountScore(stCardsCountScoreRDTs).daysofStockScore(daysofStockScoreRDTs).iDenominator(iDenominatorRDTs).iNumerator(iNumeratorRDTs).build();

            try {
                inventoryDao.save(rdts);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            
            String cardAvailabilityLLINs = jsonObjectllins.isNull("sCardAvailableLLINs") ? "" : jsonObjectllins.getString("sCardAvailableLLINs");
            Integer alCountsLLINs = getIntegerFromJsonKey(jsonObjectllins, "StocksAvailableLLINs");
            Integer daysoutStockLLINs = getIntegerFromJsonKey(jsonObjectllins, "daysOutOfStockLLINs");
            Integer cardbalanceLLINs = getIntegerFromJsonKey(jsonObjectllins, "stockCardBalanceLLINs");
            Integer amcvalueLLINs = getIntegerFromJsonKey(jsonObjectllins, "StockCardIssuesLLINs");
            String expiriesLLINs = jsonObjectllins.isNull("expiry6MonthsLLINs") ? "" : jsonObjectllins.getString("expiry6MonthsLLINs");
            Integer actualcountLLINs = getIntegerFromJsonKey(jsonObjectllins, "physicalStockLLINs");
            Integer cmosoneLLINs = getIntegerFromJsonKey(datajson, "currentMOSLLINs");
            Integer cmostwoLLINs = getIntegerFromJsonKey(datajson, "currentMOS2LLINs");
            Integer stcardscoreLLINs = (datajson.isNull("stockCardScoreLLINs")) ? empty : datajson.getInt("stockCardScoreLLINs");
            Integer stCardsCountScoreLLINs = (datajson.isNull("stockCardsCountScoreLLINs")) ? empty : datajson.getInt("stockCardsCountScoreLLINs");
            Integer daysofStockScoreLLINs = (datajson.isNull("daysOutOfStockScoreLLINs")) ? empty : datajson.getInt("daysOutOfStockScoreLLINs");
            Integer iDenominatorLLINs = (datajson.isNull("itemDenominatorLLINs")) ? empty : datajson.getInt("itemDenominatorLLINs");
            Integer iNumeratorLLINs = (datajson.isNull("itemNumeratorLLINs")) ? empty : datajson.getInt("itemNumeratorLLINs");

            InventoryManagement llins = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("LLINs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityLLINs).stCountsDone(alCountsLLINs).dayOutStock(daysoutStockLLINs).cardBalance(cardbalanceLLINs).amc(amcvalueLLINs)
                    .expires6mo(expiriesLLINs).actualCount(actualcountLLINs).cmos1(cmosoneLLINs).cmos2(cmostwoLLINs).stCardScore(stcardscoreLLINs)
                    .stCardsCountScore(stCardsCountScoreLLINs).daysofStockScore(daysofStockScoreLLINs).iDenominator(iDenominatorLLINs).iNumerator(iNumeratorLLINs).build();

            try {
                inventoryDao.save(llins);
            } catch (Exception e) {
                log.info("Duplication");
            }
/*
            String cardAvailabilityLLINs = jsonObjectmalaria.isNull("sCardAvailableLLINs") ? "" : jsonObjectmalaria.getString("sCardAvailableLLINs");
            Integer alCountsLLINs = getIntegerFromJsonKey(jsonObjectmalaria, "StocksAvailableLLINs");
            //Integer daysoutStockLLINs = getIntegerFromJsonKey(jsonObjectmalaria, "daysOutOfStockLLINs");
            Integer cardbalanceLLINs = getIntegerFromJsonKey(jsonObjectmalaria, "stockCardBalanceLLINs");
            Integer amcvalueLLINs = getIntegerFromJsonKey(jsonObjectmalaria, "StockCardIssuesLLINs");
            String expiriesLLINs = jsonObjectmalaria.isNull("expiry6MonthsLLINs") ? "" : jsonObjectmalaria.getString("expiry6MonthsLLINs");
            Integer actualcountLLINs = getIntegerFromJsonKey(jsonObjectmalaria, "physicalStockLLINs");
            //Integer cmosoneLLINs = (datajson.isNull("currentMOSLLINs")) ? empty :datajson.getInt("currentMOSLLINs");
            //Integer cmostwoLLINs = (datajson.isNull("currentMOS2LLINs")) ? empty :datajson.getInt("currentMOS2LLINs");
            Integer cmosoneLLINs = getIntegerFromJsonKey(datajson, "currentMOSLLINs");
            Integer cmostwoLLINs = getIntegerFromJsonKey(datajson, "currentMOS2LLINs");
            Integer stcardscoreLLINs = (datajson.isNull("stockCardScoreLLINs")) ? empty : datajson.getInt("stockCardScoreLLINs");
            Integer stCardsCountScoreLLINs = (datajson.isNull("stockCardsCountScoreLLINs")) ? empty : datajson.getInt("stockCardsCountScoreLLINs");
            //Integer daysofStockScoreLLINs = (datajson.isNull("daysOutOfStockScoreLLINs")) ? empty : datajson.getInt("daysOutOfStockScoreLLINs");
            Integer iDenominatorLLINs = (datajson.isNull("itemDenominatorLLINs")) ? empty : datajson.getInt("itemDenominatorLLINs");
            Integer iNumeratorLLINs = (datajson.isNull("itemNumeratorLLINs")) ? empty : datajson.getInt("itemNumeratorLLINs");

            InventoryManagement LLINs = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("LLINs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityLLINs).stCountsDone(alCountsLLINs).dayOutStock(0).cardBalance(cardbalanceLLINs).amc(amcvalueLLINs)
                    .expires6mo(expiriesLLINs).actualCount(actualcountLLINs).cmos1(cmosoneLLINs).cmos2(cmostwoLLINs).stCardScore(stcardscoreLLINs)
                    .stCardsCountScore(stCardsCountScoreLLINs).daysofStockScore(0).iDenominator(iDenominatorLLINs).iNumerator(iNumeratorLLINs).build();

            try {
                inventoryDao.save(LLINs);
            } catch (Exception e) {
                log.info("Duplication");
            }
*/

            String cardAvailabilityDMPA = jsonObjectdmpa.isNull("sCardAvailableDMPA") ? "" : jsonObjectdmpa.getString("sCardAvailableDMPA");
            Integer alCountsDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "StocksAvailableDMPA");
            Integer daysoutStockDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "daysOutOfStockDMPA");
            Integer cardbalanceDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "stockCardBalanceDMPA");
            Integer amcvalueDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "StockCardIssuesDMPA");
            String expiriesDMPA = jsonObjectdmpa.isNull("expiry6MonthsDMPA") ? "" : jsonObjectdmpa.getString("expiry6MonthsDMPA");
            Integer actualcountDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "physicalStockDMPA");
            //Integer cmosoneDMPA = (datajson.isNull("currentMOSDMPA")) ? empty :datajson.getInt("currentMOSDMPA");
            //Integer cmostwoDMPA = (datajson.isNull("currentMOS2DMPA")) ? empty :datajson.getInt("currentMOS2DMPA");
            Integer cmosoneDMPA = getIntegerFromJsonKey(datajson, "currentMOSDMPA");
            Integer cmostwoDMPA = getIntegerFromJsonKey(datajson, "currentMOS2DMPA");
            Integer stcardscoreDMPA = (datajson.isNull("stockCardScoreDMPA")) ? empty : datajson.getInt("stockCardScoreDMPA");
            Integer stCardsCountScoreDMPA = (datajson.isNull("stockCardsCountScoreDMPA")) ? empty : datajson.getInt("stockCardsCountScoreDMPA");
            //Integer daysofStockScoreDMPA = (datajson.isNull("daysOutOfStockScoreDMPA")) ? empty : datajson.getInt("daysOutOfStockScoreDMPA");
            Integer iDenominatorDMPA = (datajson.isNull("itemDenominatorDMPA")) ? empty : datajson.getInt("itemDenominatorDMPA");
            Integer iNumeratorDMPA = (datajson.isNull("itemNumeratorDMPA")) ? empty : datajson.getInt("itemNumeratorDMPA");

            InventoryManagement dmpa = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("DMPA inj").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityDMPA).stCountsDone(alCountsDMPA).dayOutStock(daysoutStockDMPA).cardBalance(cardbalanceDMPA).amc(amcvalueDMPA)
                    .expires6mo(expiriesDMPA).actualCount(actualcountDMPA).cmos1(cmosoneDMPA).cmos2(cmostwoDMPA).stCardScore(stcardscoreDMPA)
                    .stCardsCountScore(stCardsCountScoreDMPA).daysofStockScore(0).iDenominator(iDenominatorDMPA).iNumerator(iNumeratorDMPA).build();

            try {
                inventoryDao.save(dmpa);
            } catch (Exception e) {
                log.info("Duplication");
            }


            String cardAvailability1rod = jsonObjectimplant1.isNull("sCardAvailable1rod") ? "" : jsonObjectimplant1.getString("sCardAvailable1rod");
            Integer alCounts1rod = getIntegerFromJsonKey(jsonObjectimplant1, "StocksAvailable1rod");
            Integer daysoutStock1rod = getIntegerFromJsonKey(jsonObjectimplant1, "daysOutOfStock1rod");
            Integer cardbalance1rod = getIntegerFromJsonKey(jsonObjectimplant1, "stockCardBalance1rod");
            Integer amcvalue1rod = getIntegerFromJsonKey(jsonObjectimplant1, "StockCardIssues1rod");
            String expiries1rod = jsonObjectimplant1.isNull("expiry6Months1rod") ? "" : jsonObjectimplant1.getString("expiry6Months1rod");
            Integer actualcount1rod = getIntegerFromJsonKey(jsonObjectimplant1, "physicalStock1rod");
            //Integer cmosone1rod = (datajson.isNull("currentMOS1rod")) ? empty :datajson.getInt("currentMOS1rod");
            //Integer cmostwo1rod = (datajson.isNull("currentMOS21rod")) ? empty :datajson.getInt("currentMOS21rod");
            Integer cmosone1rod = getIntegerFromJsonKey(datajson, "currentMOS1rod");
            Integer cmostwo1rod = getIntegerFromJsonKey(datajson, "currentMOS21rod");
            Integer stcardscore1rod = (datajson.isNull("stockCardScore1rod")) ? empty : datajson.getInt("stockCardScore1rod");
            Integer stCardsCountScore1rod = (datajson.isNull("stockCardsCountScore1rod")) ? empty : datajson.getInt("stockCardsCountScore1rod");
            //Integer daysofStockScore1rod = (datajson.isNull("daysOutOfStockScore1rod")) ? empty : datajson.getInt("daysOutOfStockScore1rod");
            Integer iDenominator1rod = (datajson.isNull("itemDenominator1rod")) ? empty : datajson.getInt("itemDenominator1rod");
            Integer iNumerator1rod = (datajson.isNull("itemNumerator1rod")) ? empty : datajson.getInt("itemNumerator1rod");

            InventoryManagement rod1 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("1 rod implant").orgUnit(orgUnit)
                    .stCardAvail(cardAvailability1rod).stCountsDone(alCounts1rod).dayOutStock(daysoutStock1rod).cardBalance(cardbalance1rod).amc(amcvalue1rod)
                    .expires6mo(expiries1rod).actualCount(actualcount1rod).cmos1(cmosone1rod).cmos2(cmostwo1rod).stCardScore(stcardscore1rod)
                    .stCardsCountScore(stCardsCountScore1rod).daysofStockScore(0).iDenominator(iDenominator1rod).iNumerator(iNumerator1rod).build();

            try {
                inventoryDao.save(rod1);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilitycalc10 = jsonObjectcalc10.isNull("sCardAvailablecalc10") ? "" : jsonObjectcalc10.getString("sCardAvailablecalc10");
            Integer alCountscalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "StocksAvailablecalc10");
            Integer daysoutStockcalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "daysOutOfStockcalc10");
            Integer cardbalancecalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "stockCardBalancecalc10");
            Integer amcvaluecalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "StockCardIssuescalc10");
            String expiriescalc10 = jsonObjectcalc10.isNull("expiry6Monthscalc10") ? "" : jsonObjectcalc10.getString("expiry6Monthscalc10");
            Integer actualcountcalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "physicalStockcalc10");
            //Integer cmosonecalc10 = (datajson.isNull("currentMOScalc10")) ? empty :datajson.getInt("currentMOScalc10");
            //Integer cmostwocalc10 = (datajson.isNull("currentMOS2calc10")) ? empty :datajson.getInt("currentMOS2calc10");
            Integer cmosonecalc10 = getIntegerFromJsonKey(datajson, "currentMOScalc10");
            Integer cmostwocalc10 = getIntegerFromJsonKey(datajson, "currentMOS2calc10");
            Integer stcardscorecalc10 = (datajson.isNull("stockCardScorecalc10")) ? empty : datajson.getInt("stockCardScorecalc10");
            Integer stCardsCountScorecalc10 = (datajson.isNull("stockCardsCountScorecalc10")) ? empty : datajson.getInt("stockCardsCountScorecalc10");
            //Integer daysofStockScorecalc10 = (datajson.isNull("daysOutOfStockScorecalc10")) ? empty : datajson.getInt("daysOutOfStockScorecalc10");
            Integer iDenominatorcalc10 = (datajson.isNull("itemDenominatorcalc10")) ? empty : datajson.getInt("itemDenominatorcalc10");
            Integer iNumeratorcalc10 = (datajson.isNull("itemNumeratorcalc10")) ? empty : datajson.getInt("itemNumeratorcalc10");

            InventoryManagement calc10 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Calcium gluconate Inj, 10%, 10ml amp").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitycalc10).stCountsDone(alCountscalc10).dayOutStock(daysoutStockcalc10).cardBalance(cardbalancecalc10).amc(amcvaluecalc10)
                    .expires6mo(expiriescalc10).actualCount(actualcountcalc10).cmos1(cmosonecalc10).cmos2(cmostwocalc10).stCardScore(stcardscorecalc10)
                    .stCardsCountScore(stCardsCountScorecalc10).daysofStockScore(0).iDenominator(iDenominatorcalc10).iNumerator(iNumeratorcalc10).build();

            try {
                inventoryDao.save(calc10);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilitydexa4mg = jsonObjectdexa4mg.isNull("sCardAvailabledexa4mg") ? "" : jsonObjectdexa4mg.getString("sCardAvailabledexa4mg");
            Integer alCountsdexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "StocksAvailabledexa4mg");
            Integer daysoutStockdexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "daysOutOfStockdexa4mg");
            Integer cardbalancedexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "stockCardBalancedexa4mg");
            Integer amcvaluedexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "StockCardIssuesdexa4mg");
            String expiriesdexa4mg = jsonObjectdexa4mg.isNull("expiry6Monthsdexa4mg") ? "" : jsonObjectdexa4mg.getString("expiry6Monthsdexa4mg");
            Integer actualcountdexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "physicalStockdexa4mg");
            //Integer cmosonedexa4mg = (datajson.isNull("currentMOSdexa4mg")) ? empty :datajson.getInt("currentMOSdexa4mg");
            //Integer cmostwodexa4mg = (datajson.isNull("currentMOS2dexa4mg")) ? empty :datajson.getInt("currentMOS2dexa4mg");
            Integer cmosonedexa4mg = getIntegerFromJsonKey(datajson, "currentMOSdexa4mg");
            Integer cmostwodexa4mg = getIntegerFromJsonKey(datajson, "currentMOS2dexa4mg");
            Integer stcardscoredexa4mg = (datajson.isNull("stockCardScoredexa4mg")) ? empty : datajson.getInt("stockCardScoredexa4mg");
            Integer stCardsCountScoredexa4mg = (datajson.isNull("stockCardsCountScoredexa4mg")) ? empty : datajson.getInt("stockCardsCountScoredexa4mg");
            //Integer daysofStockScoredexa4mg = (datajson.isNull("daysOutOfStockScoredexa4mg")) ? empty : datajson.getInt("daysOutOfStockScoredexa4mg");
            Integer iDenominatordexa4mg = (datajson.isNull("itemDenominatordexa4mg")) ? empty : datajson.getInt("itemDenominatordexa4mg");
            Integer iNumeratordexa4mg = (datajson.isNull("itemNumeratordexa4mg")) ? empty : datajson.getInt("itemNumeratordexa4mg");

            InventoryManagement dexa4mg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Dexamethasone Inj, 4mg/ml, amp").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitydexa4mg).stCountsDone(alCountsdexa4mg).dayOutStock(daysoutStockdexa4mg).cardBalance(cardbalancedexa4mg).amc(amcvaluedexa4mg)
                    .expires6mo(expiriesdexa4mg).actualCount(actualcountdexa4mg).cmos1(cmosonedexa4mg).cmos2(cmostwodexa4mg).stCardScore(stcardscoredexa4mg)
                    .stCardsCountScore(stCardsCountScoredexa4mg).daysofStockScore(0).iDenominator(iDenominatordexa4mg).iNumerator(iNumeratordexa4mg).build();

            try {
                inventoryDao.save(dexa4mg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityMethyldopa = jsonObjectMethyldopa.isNull("sCardAvailableMethyldopa") ? "" : jsonObjectMethyldopa.getString("sCardAvailableMethyldopa");
            Integer alCountsMethyldopa = getIntegerFromJsonKey(jsonObjectMethyldopa, "StocksAvailableMethyldopa");
            Integer daysoutStockMethyldopa = getIntegerFromJsonKey(jsonObjectMethyldopa, "daysOutOfStockMethyldopa");
            Integer cardbalanceMethyldopa = getIntegerFromJsonKey(jsonObjectMethyldopa, "stockCardBalanceMethyldopa");
            Integer amcvalueMethyldopa = getIntegerFromJsonKey(jsonObjectMethyldopa, "StockCardIssuesMethyldopa");
            String expiriesMethyldopa = jsonObjectMethyldopa.isNull("expiry6MonthsMethyldopa") ? "" : jsonObjectMethyldopa.getString("expiry6MonthsMethyldopa");
            Integer actualcountMethyldopa = getIntegerFromJsonKey(jsonObjectMethyldopa, "physicalStockMethyldopa");
            //Integer cmosoneMethyldopa = (datajson.isNull("currentMOSMethyldopa")) ? empty :datajson.getInt("currentMOSMethyldopa");
            //Integer cmostwoMethyldopa = (datajson.isNull("currentMOS2Methyldopa")) ? empty :datajson.getInt("currentMOS2Methyldopa");
            Integer cmosoneMethyldopa = getIntegerFromJsonKey(datajson, "currentMOSMethyldopa");
            Integer cmostwoMethyldopa = getIntegerFromJsonKey(datajson, "currentMOS2Methyldopa");
            Integer stcardscoreMethyldopa = (datajson.isNull("stockCardScoreMethyldopa")) ? empty : datajson.getInt("stockCardScoreMethyldopa");
            Integer stCardsCountScoreMethyldopa = (datajson.isNull("stockCardsCountScoreMethyldopa")) ? empty : datajson.getInt("stockCardsCountScoreMethyldopa");
            //Integer daysofStockScoreMethyldopa = (datajson.isNull("daysOutOfStockScoreMethyldopa")) ? empty : datajson.getInt("daysOutOfStockScoreMethyldopa");
            Integer iDenominatorMethyldopa = (datajson.isNull("itemDenominatorMethyldopa")) ? empty : datajson.getInt("itemDenominatorMethyldopa");
            Integer iNumeratorMethyldopa = (datajson.isNull("itemNumeratorMethyldopa")) ? empty : datajson.getInt("itemNumeratorMethyldopa");

            InventoryManagement Methyldopa = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Methyldopa 250mg - Tablets").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityMethyldopa).stCountsDone(alCountsMethyldopa).dayOutStock(daysoutStockMethyldopa).cardBalance(cardbalanceMethyldopa).amc(amcvalueMethyldopa)
                    .expires6mo(expiriesMethyldopa).actualCount(actualcountMethyldopa).cmos1(cmosoneMethyldopa).cmos2(cmostwoMethyldopa).stCardScore(stcardscoreMethyldopa)
                    .stCardsCountScore(stCardsCountScoreMethyldopa).daysofStockScore(0).iDenominator(iDenominatorMethyldopa).iNumerator(iNumeratorMethyldopa).build();

            try {
                inventoryDao.save(Methyldopa);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityphyto1mg = jsonObjectphyto1mg.isNull("sCardAvailablephyto1mg") ? "" : jsonObjectphyto1mg.getString("sCardAvailablephyto1mg");
            Integer alCountsphyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "StocksAvailablephyto1mg");
            Integer daysoutStockphyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "daysOutOfStockphyto1mg");
            Integer cardbalancephyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "stockCardBalancephyto1mg");
            Integer amcvaluephyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "StockCardIssuesphyto1mg");
            String expiriesphyto1mg = jsonObjectphyto1mg.isNull("expiry6Monthsphyto1mg") ? "" : jsonObjectphyto1mg.getString("expiry6Monthsphyto1mg");
            Integer actualcountphyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "physicalStockphyto1mg");
            //Integer cmosonephyto1mg = (datajson.isNull("currentMOSphyto1mg")) ? empty :datajson.getInt("currentMOSphyto1mg");
            //Integer cmostwophyto1mg = (datajson.isNull("currentMOS2phyto1mg")) ? empty :datajson.getInt("currentMOS2phyto1mg");
            Integer cmosonephyto1mg = getIntegerFromJsonKey(datajson, "currentMOSphyto1mg");
            Integer cmostwophyto1mg = getIntegerFromJsonKey(datajson, "currentMOS2phyto1mg");
            Integer stcardscorephyto1mg = (datajson.isNull("stockCardScorephyto1mg")) ? empty : datajson.getInt("stockCardScorephyto1mg");
            Integer stCardsCountScorephyto1mg = (datajson.isNull("stockCardsCountScorephyto1mg")) ? empty : datajson.getInt("stockCardsCountScorephyto1mg");
            //Integer daysofStockScorephyto1mg = (datajson.isNull("daysOutOfStockScorephyto1mg")) ? empty : datajson.getInt("daysOutOfStockScorephyto1mg");
            Integer iDenominatorphyto1mg = (datajson.isNull("itemDenominatorphyto1mg")) ? empty : datajson.getInt("itemDenominatorphyto1mg");
            Integer iNumeratorphyto1mg = (datajson.isNull("itemNumeratorphyto1mg")) ? empty : datajson.getInt("itemNumeratorphyto1mg");

            InventoryManagement phyto1mg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Phytomenadione Inj, 1mg/0.1ml").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityphyto1mg).stCountsDone(alCountsphyto1mg).dayOutStock(daysoutStockphyto1mg).cardBalance(cardbalancephyto1mg).amc(amcvaluephyto1mg)
                    .expires6mo(expiriesphyto1mg).actualCount(actualcountphyto1mg).cmos1(cmosonephyto1mg).cmos2(cmostwophyto1mg).stCardScore(stcardscorephyto1mg)
                    .stCardsCountScore(stCardsCountScorephyto1mg).daysofStockScore(0).iDenominator(iDenominatorphyto1mg).iNumerator(iNumeratorphyto1mg).build();

            try {
                inventoryDao.save(phyto1mg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityTDF30s = jsonObjecttdf .isNull("sCardAvailableTDF30s") ? "" : jsonObjecttdf .getString("sCardAvailableTDF30s");
            Integer alCountsTDF30s = getIntegerFromJsonKey(jsonObjecttdf , "StocksAvailableTDF30s");
            Integer daysoutStockTDF30s = getIntegerFromJsonKey(jsonObjecttdf , "daysOutOfStockTDF30s");
            Integer cardbalanceTDF30s = getIntegerFromJsonKey(jsonObjecttdf , "stockCardBalanceTDF30s");
            Integer amcvalueTDF30s = getIntegerFromJsonKey(jsonObjecttdf , "StockCardIssuesTDF30s");
            String expiriesTDF30s = jsonObjecttdf .isNull("expiry6MonthsTDF30s") ? "" : jsonObjecttdf .getString("expiry6MonthsTDF30s");
            Integer actualcountTDF30s = getIntegerFromJsonKey(jsonObjecttdf , "physicalStockTDF30s");
            //Integer cmosoneTDF30s = (datajson.isNull("currentMOSTDF30s")) ? empty :datajson.getInt("currentMOSTDF30s");
            //Integer cmostwoTDF30s = (datajson.isNull("currentMOS2TDF30s")) ? empty :datajson.getInt("currentMOS2TDF30s");
            Integer cmosoneTDF30s = getIntegerFromJsonKey(datajson, "currentMOSTDF30s");
            Integer cmostwoTDF30s = getIntegerFromJsonKey(datajson, "currentMOS2TDF30s");
            Integer stcardscoreTDF30s = (datajson.isNull("stockCardScoreTDF30s")) ? empty : datajson.getInt("stockCardScoreTDF30s");
            Integer stCardsCountScoreTDF30s = (datajson.isNull("stockCardsCountScoreTDF30s")) ? empty : datajson.getInt("stockCardsCountScoreTDF30s");
            //Integer daysofStockScoreTDF30s = (datajson.isNull("daysOutOfStockScoreTDF30s")) ? empty : datajson.getInt("daysOutOfStockScoreTDF30s");
            Integer iDenominatorTDF30s = (datajson.isNull("itemDenominatorTDF30s")) ? empty : datajson.getInt("itemDenominatorTDF30s");
            Integer iNumeratorTDF30s = (datajson.isNull("itemNumeratorTDF30s")) ? empty : datajson.getInt("itemNumeratorTDF30s");

            InventoryManagement TDF30s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("TDF/3TC 30s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityTDF30s).stCountsDone(alCountsTDF30s).dayOutStock(daysoutStockTDF30s).cardBalance(cardbalanceTDF30s).amc(amcvalueTDF30s)
                    .expires6mo(expiriesTDF30s).actualCount(actualcountTDF30s).cmos1(cmosoneTDF30s).cmos2(cmostwoTDF30s).stCardScore(stcardscoreTDF30s)
                    .stCardsCountScore(stCardsCountScoreTDF30s).daysofStockScore(0).iDenominator(iDenominatorTDF30s).iNumerator(iNumeratorTDF30s).build();

            try {
                inventoryDao.save(TDF30s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityDTG = jsonObjectdtg.isNull("sCardAvailableDTG") ? "" : jsonObjectdtg.getString("sCardAvailableDTG");
            Integer alCountsDTG = getIntegerFromJsonKey(jsonObjectdtg, "StocksAvailableDTG");
            Integer daysoutStockDTG = getIntegerFromJsonKey(jsonObjectdtg, "daysOutOfStockDTG");
            Integer cardbalanceDTG = getIntegerFromJsonKey(jsonObjectdtg, "stockCardBalanceDTG");
            Integer amcvalueDTG = getIntegerFromJsonKey(jsonObjectdtg, "StockCardIssuesDTG");
            String expiriesDTG = jsonObjectdtg.isNull("expiry6MonthsDTG") ? "" : jsonObjectdtg.getString("expiry6MonthsDTG");
            Integer actualcountDTG = getIntegerFromJsonKey(jsonObjectdtg, "physicalStockDTG");
            //Integer cmosoneDTG = (datajson.isNull("currentMOSDTG")) ? empty :datajson.getInt("currentMOSDTG");
            //Integer cmostwoDTG = (datajson.isNull("currentMOS2DTG")) ? empty :datajson.getInt("currentMOS2DTG");
            Integer cmosoneDTG = getIntegerFromJsonKey(datajson, "currentMOSDTG");
            Integer cmostwoDTG = getIntegerFromJsonKey(datajson, "currentMOS2DTG");
            Integer stcardscoreDTG = (datajson.isNull("stockCardScoreDTG")) ? empty : datajson.getInt("stockCardScoreDTG");
            Integer stCardsCountScoreDTG = (datajson.isNull("stockCardsCountScoreDTG")) ? empty : datajson.getInt("stockCardsCountScoreDTG");
            //Integer daysofStockScoreDTG = (datajson.isNull("daysOutOfStockScoreDTG")) ? empty : datajson.getInt("daysOutOfStockScoreDTG");
            Integer iDenominatorDTG = (datajson.isNull("itemDenominatorDTG")) ? empty : datajson.getInt("itemDenominatorDTG");
            Integer iNumeratorDTG = (datajson.isNull("itemNumeratorDTG")) ? empty : datajson.getInt("itemNumeratorDTG");

            InventoryManagement dtg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("TDF/3TC/DTG adult").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityDTG).stCountsDone(alCountsDTG).dayOutStock(daysoutStockDTG).cardBalance(cardbalanceDTG).amc(amcvalueDTG)
                    .expires6mo(expiriesDTG).actualCount(actualcountDTG).cmos1(cmosoneDTG).cmos2(cmostwoDTG).stCardScore(stcardscoreDTG)
                    .stCardsCountScore(stCardsCountScoreDTG).daysofStockScore(0).iDenominator(iDenominatorDTG).iNumerator(iNumeratorDTG).build();

            try {
                inventoryDao.save(dtg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityABC = jsonObjectabc.isNull("sCardAvailableABC") ? "" : jsonObjectabc.getString("sCardAvailableABC");
            Integer alCountsABC = getIntegerFromJsonKey(jsonObjectabc, "StocksAvailableABC");
            Integer daysoutStockABC = getIntegerFromJsonKey(jsonObjectabc, "daysOutOfStockABC");
            Integer cardbalanceABC = getIntegerFromJsonKey(jsonObjectabc, "stockCardBalanceABC");
            Integer amcvalueABC = getIntegerFromJsonKey(jsonObjectabc, "StockCardIssuesABC");
            String expiriesABC = jsonObjectabc.isNull("expiry6MonthsABC") ? "" : jsonObjectabc.getString("expiry6MonthsABC");
            Integer actualcountABC = getIntegerFromJsonKey(jsonObjectabc, "physicalStockABC");
            //Integer cmosoneABC = (datajson.isNull("currentMOSABC")) ? empty :datajson.getInt("currentMOSABC");
            //Integer cmostwoABC = (datajson.isNull("currentMOS2ABC")) ? empty :datajson.getInt("currentMOS2ABC");
            Integer cmosoneABC = getIntegerFromJsonKey(datajson, "currentMOSABC");
            Integer cmostwoABC = getIntegerFromJsonKey(datajson, "currentMOS2ABC");
            Integer stcardscoreABC = (datajson.isNull("stockCardScoreABC")) ? empty : datajson.getInt("stockCardScoreABC");
            Integer stCardsCountScoreABC = (datajson.isNull("stockCardsCountScoreABC")) ? empty : datajson.getInt("stockCardsCountScoreABC");
            //Integer daysofStockScoreABC = (datajson.isNull("daysOutOfStockScoreABC")) ? empty : datajson.getInt("daysOutOfStockScoreABC");
            Integer iDenominatorABC = (datajson.isNull("itemDenominatorABC")) ? empty : datajson.getInt("itemDenominatorABC");
            Integer iNumeratorABC = (datajson.isNull("itemNumeratorABC")) ? empty : datajson.getInt("itemNumeratorABC");

            InventoryManagement abc = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("ABC/3TC paed").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityABC).stCountsDone(alCountsABC).dayOutStock(daysoutStockABC).cardBalance(cardbalanceABC).amc(amcvalueABC)
                    .expires6mo(expiriesABC).actualCount(actualcountABC).cmos1(cmosoneABC).cmos2(cmostwoABC).stCardScore(stcardscoreABC)
                    .stCardsCountScore(stCardsCountScoreABC).daysofStockScore(0).iDenominator(iDenominatorABC).iNumerator(iNumeratorABC).build();

            try {
                inventoryDao.save(abc);
            } catch (Exception e) {
                log.info("Duplication");
            }


            String cardAvailabilityHIV = jsonObjecthivs.isNull("sCardAvailableHIV") ? "" : jsonObjecthivs.getString("sCardAvailableHIV");
            Integer alCountsHIV = getIntegerFromJsonKey(jsonObjecthivs, "StocksAvailableHIV");
            Integer daysoutStockHIV = getIntegerFromJsonKey(jsonObjecthivs, "daysOutOfStockHIV");
            Integer cardbalanceHIV = getIntegerFromJsonKey(jsonObjecthivs, "stockCardBalanceHIV");
            Integer amcvalueHIV = getIntegerFromJsonKey(jsonObjecthivs, "StockCardIssuesHIV");
            String expiriesHIV = jsonObjecthivs.isNull("expiry6MonthsHIV") ? "" : jsonObjecthivs.getString("expiry6MonthsHIV");
            Integer actualcountHIV = getIntegerFromJsonKey(jsonObjecthivs, "physicalStockHIV");
            //Integer cmosoneHIV = (datajson.isNull("currentMOSHIV")) ? empty :datajson.getInt("currentMOSHIV");
            //Integer cmostwoHIV = (datajson.isNull("currentMOS2HIV")) ? empty :datajson.getInt("currentMOS2HIV");
            Integer cmosoneHIV = getIntegerFromJsonKey(datajson, "currentMOSHIV");
            Integer cmostwoHIV = getIntegerFromJsonKey(datajson, "currentMOS2HIV");
            Integer stcardscoreHIV = (datajson.isNull("stockCardScoreHIV")) ? empty : datajson.getInt("stockCardScoreHIV");
            Integer stCardsCountScoreHIV = (datajson.isNull("stockCardsCountScoreHIV")) ? empty : datajson.getInt("stockCardsCountScoreHIV");
            //Integer daysofStockScoreHIV = (datajson.isNull("daysOutOfStockScoreHIV")) ? empty : datajson.getInt("daysOutOfStockScoreHIV");
            Integer iDenominatorHIV = (datajson.isNull("itemDenominatorHIV")) ? empty : datajson.getInt("itemDenominatorHIV");
            Integer iNumeratorHIV = (datajson.isNull("itemNumeratorHIV")) ? empty : datajson.getInt("itemNumeratorHIV");

            InventoryManagement HIV = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("HIV screening test").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityHIV).stCountsDone(alCountsHIV).dayOutStock(daysoutStockHIV).cardBalance(cardbalanceHIV).amc(amcvalueHIV)
                    .expires6mo(expiriesHIV).actualCount(actualcountHIV).cmos1(cmosoneHIV).cmos2(cmostwoHIV).stCardScore(stcardscoreHIV)
                    .stCardsCountScore(stCardsCountScoreHIV).daysofStockScore(0).iDenominator(iDenominatorHIV).iNumerator(iNumeratorHIV).build();

            try {
                inventoryDao.save(HIV);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityoraquick = jsonObjectoraquick.isNull("sCardAvailableoraquick") ? "" : jsonObjectoraquick.getString("sCardAvailableoraquick");
            Integer alCountsoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "StocksAvailableoraquick");
            Integer daysoutStockoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "daysOutOfStockoraquick");
            Integer cardbalanceoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "stockCardBalanceoraquick");
            Integer amcvalueoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "StockCardIssuesoraquick");
            String expiriesoraquick = jsonObjectoraquick.isNull("expiry6Monthsoraquick") ? "" : jsonObjectoraquick.getString("expiry6Monthsoraquick");
            Integer actualcountoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "physicalStockoraquick");
            //Integer cmosoneoraquick = (datajson.isNull("currentMOSoraquick")) ? empty :datajson.getInt("currentMOSoraquick");
            //Integer cmostwooraquick = (datajson.isNull("currentMOS2oraquick")) ? empty :datajson.getInt("currentMOS2oraquick");
            Integer cmosoneoraquick = getIntegerFromJsonKey(datajson, "currentMOSoraquick");
            Integer cmostwooraquick = getIntegerFromJsonKey(datajson, "currentMOS2oraquick");
            Integer stcardscoreoraquick = (datajson.isNull("stockCardScoreoraquick")) ? empty : datajson.getInt("stockCardScoreoraquick");
            Integer stCardsCountScoreoraquick = (datajson.isNull("stockCardsCountScoreoraquick")) ? empty : datajson.getInt("stockCardsCountScoreoraquick");
            //Integer daysofStockScoreoraquick = (datajson.isNull("daysOutOfStockScoreoraquick")) ? empty : datajson.getInt("daysOutOfStockScoreoraquick");
            Integer iDenominatororaquick = (datajson.isNull("itemDenominatororaquick")) ? empty : datajson.getInt("itemDenominatororaquick");
            Integer iNumeratororaquick = (datajson.isNull("itemNumeratororaquick")) ? empty : datajson.getInt("itemNumeratororaquick");

            InventoryManagement oraquick = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("HIV self-test (Oraquick), tests").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityoraquick).stCountsDone(alCountsoraquick).dayOutStock(daysoutStockoraquick).cardBalance(cardbalanceoraquick).amc(amcvalueoraquick)
                    .expires6mo(expiriesoraquick).actualCount(actualcountoraquick).cmos1(cmosoneoraquick).cmos2(cmostwooraquick).stCardScore(stcardscoreoraquick)
                    .stCardsCountScore(stCardsCountScoreoraquick).daysofStockScore(0).iDenominator(iDenominatororaquick).iNumerator(iNumeratororaquick).build();

            try {
                inventoryDao.save(oraquick);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityltx = jsonObjectlatex.isNull("sCardAvailableltx") ? "" : jsonObjectlatex.getString("sCardAvailableltx");
            Integer alCountsltx = getIntegerFromJsonKey(jsonObjectlatex, "StocksAvailableltx");
            Integer daysoutStockltx = getIntegerFromJsonKey(jsonObjectlatex, "daysOutOfStockltx");
            Integer cardbalanceltx = getIntegerFromJsonKey(jsonObjectlatex, "stockCardBalanceltx");
            Integer amcvalueltx = getIntegerFromJsonKey(jsonObjectlatex, "StockCardIssuesltx");
            String expiriesltx = jsonObjectlatex.isNull("expiry6Monthsltx") ? "" : jsonObjectlatex.getString("expiry6Monthsltx");
            Integer actualcountltx = getIntegerFromJsonKey(jsonObjectlatex, "physicalStockltx");
            //Integer cmosoneltx = (datajson.isNull("currentMOSltx")) ? empty :datajson.getInt("currentMOSltx");
            //Integer cmostwoltx = (datajson.isNull("currentMOS2ltx")) ? empty :datajson.getInt("currentMOS2ltx");
            Integer cmosoneltx = getIntegerFromJsonKey(datajson, "currentMOSltx");
            Integer cmostwoltx = getIntegerFromJsonKey(datajson, "currentMOS2ltx");
            Integer stcardscoreltx = (datajson.isNull("stockCardScoreltx")) ? empty : datajson.getInt("stockCardScoreltx");
            Integer stCardsCountScoreltx = (datajson.isNull("stockCardsCountScoreltx")) ? empty : datajson.getInt("stockCardsCountScoreltx");
            //Integer daysofStockScoreltx = (datajson.isNull("daysOutOfStockScoreltx")) ? empty : datajson.getInt("daysOutOfStockScoreltx");
            Integer iDenominatorltx = (datajson.isNull("itemDenominatorltx")) ? empty : datajson.getInt("itemDenominatorltx");
            Integer iNumeratorltx = (datajson.isNull("itemNumeratorltx")) ? empty : datajson.getInt("itemNumeratorltx");

            InventoryManagement ltex = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("latex gloves").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityltx).stCountsDone(alCountsltx).dayOutStock(daysoutStockltx).cardBalance(cardbalanceltx).amc(amcvalueltx)
                    .expires6mo(expiriesltx).actualCount(actualcountltx).cmos1(cmosoneltx).cmos2(cmostwoltx).stCardScore(stcardscoreltx)
                    .stCardsCountScore(stCardsCountScoreltx).daysofStockScore(0).iDenominator(iDenominatorltx).iNumerator(iNumeratorltx).build();

            try {
                inventoryDao.save(ltex);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityMMR = jsonObjectmmr.isNull("sCardAvailableMMR") ? "" : jsonObjectmmr.getString("sCardAvailableMMR");
            Integer alCountsMMR = getIntegerFromJsonKey(jsonObjectmmr, "StocksAvailableMMR");
            Integer daysoutStockMMR = getIntegerFromJsonKey(jsonObjectmmr, "daysOutOfStockMMR");
            Integer cardbalanceMMR = getIntegerFromJsonKey(jsonObjectmmr, "stockCardBalanceMMR");
            Integer amcvalueMMR = getIntegerFromJsonKey(jsonObjectmmr, "StockCardIssuesMMR");
            String expiriesMMR = jsonObjectmmr.isNull("expiry6MonthsMMR") ? "" : jsonObjectmmr.getString("expiry6MonthsMMR");
            Integer actualcountMMR = getIntegerFromJsonKey(jsonObjectmmr, "physicalStockMMR");
            //Integer cmosoneMMR = (datajson.isNull("currentMOSMMR")) ? empty :datajson.getInt("currentMOSMMR");
            //Integer cmostwoMMR = (datajson.isNull("currentMOS2MMR")) ? empty :datajson.getInt("currentMOS2MMR");
            Integer cmosoneMMR = getIntegerFromJsonKey(datajson, "currentMOSMMR");
            Integer cmostwoMMR = getIntegerFromJsonKey(datajson, "currentMOS2MMR");
            Integer stcardscoreMMR = (datajson.isNull("stockCardScoreMMR")) ? empty : datajson.getInt("stockCardScoreMMR");
            Integer stCardsCountScoreMMR = (datajson.isNull("stockCardsCountScoreMMR")) ? empty : datajson.getInt("stockCardsCountScoreMMR");
            //Integer daysofStockScoreMMR = (datajson.isNull("daysOutOfStockScoreMMR")) ? empty : datajson.getInt("daysOutOfStockScoreMMR");
            Integer iDenominatorMMR = (datajson.isNull("itemDenominatorMMR")) ? empty : datajson.getInt("itemDenominatorMMR");
            Integer iNumeratorMMR = (datajson.isNull("itemNumeratorMMR")) ? empty : datajson.getInt("itemNumeratorMMR");

            InventoryManagement mmr = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("MMR vaccine").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityMMR).stCountsDone(alCountsMMR).dayOutStock(daysoutStockMMR).cardBalance(cardbalanceMMR).amc(amcvalueMMR)
                    .expires6mo(expiriesMMR).actualCount(actualcountMMR).cmos1(cmosoneMMR).cmos2(cmostwoMMR).stCardScore(stcardscoreMMR)
                    .stCardsCountScore(stCardsCountScoreMMR).daysofStockScore(0).iDenominator(iDenominatorMMR).iNumerator(iNumeratorMMR).build();

            try {
                inventoryDao.save(mmr);
            } catch (Exception e) {
                log.info("Duplication");
            }


            } catch (Exception e) {
            }
            

        }

    }

    
    
/*
    //Inventory management addenda
      private void inventoryManagementAddenda(String formId) throws Exception {
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
            //log.info("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX Json Object for form number "+(i+1)+":  "+instance.toString());
            // Get the instance ID of the submission xml file

            String instanceId = instance.getString("instanceId");

            //checks if instances have already been saved.
            if (inventorymgtDao.getRecordByInstanceId(instanceId) != null) {
                log.info("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ>>>>22>>>>" + instanceId);
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

            log.info("####################################%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5 Json Object for form number " + (i + 1) + ":  " + datajson.toString());

            //Now you have a well formatted json file and can start parsing it for values.
            //Get the date the data was collected which is called "today"
            String instanceCollectedDate = datajson.getString("today");//error

            //log.info("my Date ======" + instanceCollectedDate);//sample log/error
            // Integer opeSoh = datajson.getInt("group3ScoreAmoxil");// prev
            System.out.println("Date Colleted =======" + instanceCollectedDate);//error

            String orgUnit = String.valueOf(datajson.getInt("mflcode"));
    try{
        
        
        
        
             JSONObject jsonObjectCountyFacility = null;
            if (!datajson.isNull("facility_information")) {
                jsonObjectCountyFacility = datajson.getJSONObject("facility_information");
            }
            JSONObject jsonObjectAL12s = null;
            if (!datajson.isNull("AL_12s")) {
                jsonObjectAL12s = datajson.getJSONObject("AL_12s");
            }

            Integer empty = 0;

            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String cardAvailabilityAL12s = jsonObjectAL12s.isNull("sCardAvailableAL12s") ? "" : jsonObjectAL12s.getString("sCardAvailableAL12s");
            Integer alCountsAL12s = getIntegerFromJsonKey(jsonObjectAL12s, "StocksAvailableAL12s");
            Integer daysoutStockAL12s = getIntegerFromJsonKey(jsonObjectAL12s, "daysOutOfStockAL12s");
            Integer cardbalanceAL12s = getIntegerFromJsonKey(jsonObjectAL12s, "stockCardBalanceAL12s");
            Integer amcvalueAL12s = getIntegerFromJsonKey(jsonObjectAL12s, "StockCardIssuesAL12s");
            String expiriesAL12s = jsonObjectAL12s.isNull("expiry6MonthsAL12s") ? "" : jsonObjectAL12s.getString("expiry6MonthsAL12s");
            Integer actualcountAL12s = getIntegerFromJsonKey(jsonObjectAL12s, "physicalStockAL12s");
            //Integer cmosoneAL12s = (datajson.isNull("currentMOSAL12s")) ? empty :datajson.getInt("currentMOSAL12s");
            //Integer cmostwoAL12s = (datajson.isNull("currentMOS2AL12s")) ? empty :datajson.getInt("currentMOS2AL12s");
            Integer cmosoneAL12s = getIntegerFromJsonKey(datajson, "currentMOSAL12s");
            Integer cmostwoAL12s = getIntegerFromJsonKey(datajson, "currentMOS2AL12s");
            Integer stcardscoreAL12s = (datajson.isNull("stockCardScoreAL12s")) ? empty : datajson.getInt("stockCardScoreAL12s");
            Integer stCardsCountScoreAL12s = (datajson.isNull("stockCardsCountScoreAL12s")) ? empty : datajson.getInt("stockCardsCountScoreAL12s");
            Integer daysofStockScoreAL12s = (datajson.isNull("daysOutOfStockScoreAL12s")) ? empty : datajson.getInt("daysOutOfStockScoreAL12s");
            Integer iDenominatorAL12s = (datajson.isNull("itemDenominatorAL12s")) ? empty : datajson.getInt("itemDenominatorAL12s");
            Integer iNumeratorAL12s = (datajson.isNull("itemNumeratorAL12s")) ? empty : datajson.getInt("itemNumeratorAL12s");

            InventoryManagement al12s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 12s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityAL12s).stCountsDone(alCountsAL12s).dayOutStock(daysoutStockAL12s).cardBalance(cardbalanceAL12s).amc(amcvalueAL12s)
                    .expires6mo(expiriesAL12s).actualCount(actualcountAL12s).cmos1(cmosoneAL12s).cmos2(cmostwoAL12s).stCardScore(stcardscoreAL12s)
                    .stCardsCountScore(stCardsCountScoreAL12s).daysofStockScore(daysofStockScoreAL12s).iDenominator(iDenominatorAL12s).iNumerator(iNumeratorAL12s).build();

            try {
                inventoryDao.save(al12s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectAL18s = null;
            if (!datajson.isNull("AL_18s")) {
                jsonObjectAL18s = datajson.getJSONObject("AL_18s");
            }
            String cardAvailabilityAL18s = jsonObjectAL18s.isNull("sCardAvailableAL18s") ? "" : jsonObjectAL18s.getString("sCardAvailableAL18s");
            Integer alCountsAL18s = getIntegerFromJsonKey(jsonObjectAL18s, "StocksAvailableAL18s");
            Integer daysoutStockAL18s = getIntegerFromJsonKey(jsonObjectAL18s, "daysOutOfStockAL18s");
            Integer cardbalanceAL18s = getIntegerFromJsonKey(jsonObjectAL18s, "stockCardBalanceAL18s");
            Integer amcvalueAL18s = getIntegerFromJsonKey(jsonObjectAL18s, "StockCardIssuesAL18s");
            String expiriesAL18s = jsonObjectAL18s.isNull("expiry6MonthsAL18s") ? "" : jsonObjectAL18s.getString("expiry6MonthsAL18s");
            Integer actualcountAL18s = getIntegerFromJsonKey(jsonObjectAL18s, "physicalStockAL18s");
            //Integer cmosoneAL18s = (datajson.isNull("currentMOSAL18s")) ? empty :datajson.getInt("currentMOSAL18s");
            //Integer cmostwoAL18s = (datajson.isNull("currentMOS2AL18s")) ? empty :datajson.getInt("currentMOS2AL18s");
            Integer cmosoneAL18s = getIntegerFromJsonKey(datajson, "currentMOSAL18s");
            Integer cmostwoAL18s = getIntegerFromJsonKey(datajson, "currentMOS2AL18s");
            Integer stcardscoreAL18s = (datajson.isNull("stockCardScoreAL18s")) ? empty : datajson.getInt("stockCardScoreAL18s");
            Integer stCardsCountScoreAL18s = (datajson.isNull("stockCardsCountScoreAL18s")) ? empty : datajson.getInt("stockCardsCountScoreAL18s");
            Integer daysofStockScoreAL18s = (datajson.isNull("daysOutOfStockScoreAL18s")) ? empty : datajson.getInt("daysOutOfStockScoreAL18s");
            Integer iDenominatorAL18s = (datajson.isNull("itemDenominatorAL18s")) ? empty : datajson.getInt("itemDenominatorAL18s");
            Integer iNumeratorAL18s = (datajson.isNull("itemNumeratorAL18s")) ? empty : datajson.getInt("itemNumeratorAL18s");

            InventoryManagement al18s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 18s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityAL18s).stCountsDone(alCountsAL18s).dayOutStock(daysoutStockAL18s).cardBalance(cardbalanceAL18s).amc(amcvalueAL18s)
                    .expires6mo(expiriesAL18s).actualCount(actualcountAL18s).cmos1(cmosoneAL18s).cmos2(cmostwoAL18s).stCardScore(stcardscoreAL18s)
                    .stCardsCountScore(stCardsCountScoreAL18s).daysofStockScore(daysofStockScoreAL18s).iDenominator(iDenominatorAL18s).iNumerator(iNumeratorAL18s).build();

            try {
                inventoryDao.save(al18s);
            } catch (Exception e) {
                log.info("Duplication");
            }
/*
            JSONObject jsonObjectAL24s = null;
            if (!datajson.isNull("AL_24s")) {
                jsonObjectAL24s = datajson.getJSONObject("AL_24s");
            }
            String cardAvailabilityAL24s = jsonObjectAL24s.isNull("sCardAvailableAL24s") ? "" : jsonObjectAL24s.getString("sCardAvailableAL24s");
            Integer alCountsAL24s = getIntegerFromJsonKey(jsonObjectAL24s, "StocksAvailableAL24s");
            Integer daysoutStockAL24s = getIntegerFromJsonKey(jsonObjectAL24s, "daysOutOfStockAL24s");
            Integer cardbalanceAL24s = getIntegerFromJsonKey(jsonObjectAL24s, "stockCardBalanceAL24s");
            Integer amcvalueAL24s = getIntegerFromJsonKey(jsonObjectAL24s, "StockCardIssuesAL24s");
            String expiriesAL24s = jsonObjectAL24s.isNull("expiry6MonthsAL24s") ? "" : jsonObjectAL24s.getString("expiry6MonthsAL24s");
            Integer actualcountAL24s = getIntegerFromJsonKey(jsonObjectAL24s, "physicalStockAL24s");
            //Integer cmosoneAL24s = (datajson.isNull("currentMOSAL24s")) ? empty :datajson.getInt("currentMOSAL24s");
            //Integer cmostwoAL24s = (datajson.isNull("currentMOS2AL24s")) ? empty :datajson.getInt("currentMOS2AL24s");
            Integer cmosoneAL24s = getIntegerFromJsonKey(datajson, "currentMOSAL24s");
            Integer cmostwoAL24s = getIntegerFromJsonKey(datajson, "currentMOS2AL24s");
            Integer stcardscoreAL24s = (datajson.isNull("stockCardScoreAL24s")) ? empty : datajson.getInt("stockCardScoreAL24s");
            Integer stCardsCountScoreAL24s = (datajson.isNull("stockCardsCountScoreAL24s")) ? empty : datajson.getInt("stockCardsCountScoreAL24s");
            Integer daysofStockScoreAL24s = (datajson.isNull("daysOutOfStockScoreAL24s")) ? empty : datajson.getInt("daysOutOfStockScoreAL24s");
            Integer iDenominatorAL24s = (datajson.isNull("itemDenominatorAL24s")) ? empty : datajson.getInt("itemDenominatorAL24s");
            Integer iNumeratorAL24s = (datajson.isNull("itemNumeratorAL24s")) ? empty : datajson.getInt("itemNumeratorAL24s");

            InventoryManagement al24s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 24s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityAL24s).stCountsDone(alCountsAL24s).dayOutStock(daysoutStockAL24s).cardBalance(cardbalanceAL24s).amc(amcvalueAL24s)
                    .expires6mo(expiriesAL24s).actualCount(actualcountAL24s).cmos1(cmosoneAL24s).cmos2(cmostwoAL24s).stCardScore(stcardscoreAL24s)
                    .stCardsCountScore(stCardsCountScoreAL24s).daysofStockScore(daysofStockScoreAL24s).iDenominator(iDenominatorAL24s).iNumerator(iNumeratorAL24s).build();

            try {
                inventoryDao.save(al24s);
            } catch (Exception e) {
                log.info("Duplication");
            }
 was closed earlie
   
            JSONObject jsonObjectsPTabs = null;
            if (!datajson.isNull("SP_tabs")) {
                jsonObjectsPTabs = datajson.getJSONObject("SP_tabs");
            }

            String cardAvailabilitysPTabs = jsonObjectsPTabs.isNull("sCardAvailablesPTabs") ? "" : jsonObjectsPTabs.getString("sCardAvailablesPTabs");
            Integer alCountssPTabs = getIntegerFromJsonKey(jsonObjectsPTabs, "StocksAvailablesPTabs");
            Integer daysoutStocksPTabs = getIntegerFromJsonKey(jsonObjectsPTabs, "daysOutOfStocksPTabs");
            Integer cardbalancesPTabs = getIntegerFromJsonKey(jsonObjectsPTabs, "stockCardBalancesPTabs");
            Integer amcvaluesPTabs = getIntegerFromJsonKey(jsonObjectsPTabs, "StockCardIssuessPTabs");
            String expiriessPTabs = jsonObjectsPTabs.isNull("expiry6MonthssPTabs") ? "" : jsonObjectsPTabs.getString("expiry6MonthssPTabs");
            Integer actualcountsPTabs = getIntegerFromJsonKey(jsonObjectsPTabs, "physicalStocksPTabs");
            //Integer cmosonesPTabs = (datajson.isNull("currentMOSsPTabs")) ? empty :datajson.getInt("currentMOSsPTabs");
            //Integer cmostwosPTabs = (datajson.isNull("currentMOS2sPTabs")) ? empty :datajson.getInt("currentMOS2sPTabs");
            Integer cmosonesPTabs = getIntegerFromJsonKey(datajson, "currentMOSsPTabs");
            Integer cmostwosPTabs = getIntegerFromJsonKey(datajson, "currentMOS2sPTabs");
            Integer stcardscoresPTabs = (datajson.isNull("stockCardScoresPTabs")) ? empty : datajson.getInt("stockCardScoresPTabs");
            Integer stCardsCountScoresPTabs = (datajson.isNull("stockCardsCountScoresPTabs")) ? empty : datajson.getInt("stockCardsCountScoresPTabs");
            Integer daysofStockScoresPTabs = (datajson.isNull("daysOutOfStockScoresPTabs")) ? empty : datajson.getInt("daysOutOfStockScoresPTabs");
            Integer iDenominatorsPTabs = (datajson.isNull("itemDenominatorsPTabs")) ? empty : datajson.getInt("itemDenominatorsPTabs");
            Integer iNumeratorsPTabs = (datajson.isNull("itemNumeratorsPTabs")) ? empty : datajson.getInt("itemNumeratorsPTabs");

            InventoryManagement sptabs = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("SP tabs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitysPTabs).stCountsDone(alCountssPTabs).dayOutStock(daysoutStocksPTabs).cardBalance(cardbalancesPTabs).amc(amcvaluesPTabs)
                    .expires6mo(expiriessPTabs).actualCount(actualcountsPTabs).cmos1(cmosonesPTabs).cmos2(cmostwosPTabs).stCardScore(stcardscoresPTabs)
                    .stCardsCountScore(stCardsCountScoresPTabs).daysofStockScore(daysofStockScoresPTabs).iDenominator(iDenominatorsPTabs).iNumerator(iNumeratorsPTabs).build();

            try {
                inventoryDao.save(sptabs);
            } catch (Exception e) {
                log.info("Duplication");
            }
            JSONObject jsonObjectCOC = null;
            if (!datajson.isNull("COC")) {
                jsonObjectCOC = datajson.getJSONObject("COC");
            }
            String cardAvailabilityCOC = jsonObjectCOC.isNull("sCardAvailableCOC") ? "" : jsonObjectCOC.getString("sCardAvailableCOC");
            Integer alCountsCOC = getIntegerFromJsonKey(jsonObjectCOC, "StocksAvailableCOC");
            Integer daysoutStockCOC = getIntegerFromJsonKey(jsonObjectCOC, "daysOutOfStockCOC");
            Integer cardbalanceCOC = getIntegerFromJsonKey(jsonObjectCOC, "stockCardBalanceCOC");
            Integer amcvalueCOC = getIntegerFromJsonKey(jsonObjectCOC, "StockCardIssuesCOC");
            String expiriesCOC = jsonObjectCOC.isNull("expiry6MonthsCOC") ? "" : jsonObjectCOC.getString("expiry6MonthsCOC");
            Integer actualcountCOC = getIntegerFromJsonKey(jsonObjectCOC, "physicalStockCOC");
            //Integer cmosoneCOC = (datajson.isNull("currentMOSCOC")) ? empty :datajson.getInt("currentMOSCOC");
            //Integer cmostwoCOC = (datajson.isNull("currentMOS2COC")) ? empty :datajson.getInt("currentMOS2COC");
            Integer cmosoneCOC = getIntegerFromJsonKey(datajson, "currentMOSCOC");
            Integer cmostwoCOC = getIntegerFromJsonKey(datajson, "currentMOS2COC");
            Integer stcardscoreCOC = (datajson.isNull("stockCardScoreCOC")) ? empty : datajson.getInt("stockCardScoreCOC");
            Integer stCardsCountScoreCOC = (datajson.isNull("stockCardsCountScoreCOC")) ? empty : datajson.getInt("stockCardsCountScoreCOC");
            Integer daysofStockScoreCOC = (datajson.isNull("daysOutOfStockScoreCOC")) ? empty : datajson.getInt("daysOutOfStockScoreCOC");
            Integer iDenominatorCOC = (datajson.isNull("itemDenominatorCOC")) ? empty : datajson.getInt("itemDenominatorCOC");
            Integer iNumeratorCOC = (datajson.isNull("itemNumeratorCOC")) ? empty : datajson.getInt("itemNumeratorCOC");

            InventoryManagement coc = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("COC").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityCOC).stCountsDone(alCountsCOC).dayOutStock(daysoutStockCOC).cardBalance(cardbalanceCOC).amc(amcvalueCOC)
                    .expires6mo(expiriesCOC).actualCount(actualcountCOC).cmos1(cmosoneCOC).cmos2(cmostwoCOC).stCardScore(stcardscoreCOC)
                    .stCardsCountScore(stCardsCountScoreCOC).daysofStockScore(daysofStockScoreCOC).iDenominator(iDenominatorCOC).iNumerator(iNumeratorCOC).build();

            try {
                inventoryDao.save(coc);
            } catch (Exception e) {
                log.info("Duplication");
            }
            JSONObject jsonObjectIUCD = null;
            if (!datajson.isNull("IUCD")) {
                jsonObjectIUCD = datajson.getJSONObject("IUCD");
            }
            String cardAvailabilityIUCD = jsonObjectIUCD.isNull("sCardAvailableIUCD") ? "" : jsonObjectIUCD.getString("sCardAvailableIUCD");
            Integer alCountsIUCD = getIntegerFromJsonKey(jsonObjectIUCD, "StocksAvailableIUCD");
            Integer daysoutStockIUCD = getIntegerFromJsonKey(jsonObjectIUCD, "daysOutOfStockIUCD");
            Integer cardbalanceIUCD = getIntegerFromJsonKey(jsonObjectIUCD, "stockCardBalanceIUCD");
            Integer amcvalueIUCD = getIntegerFromJsonKey(jsonObjectIUCD, "StockCardIssuesIUCD");
            String expiriesIUCD = jsonObjectIUCD.isNull("expiry6MonthsIUCD") ? "" : jsonObjectIUCD.getString("expiry6MonthsIUCD");
            Integer actualcountIUCD = getIntegerFromJsonKey(jsonObjectIUCD, "physicalStockIUCD");
            //Integer cmosoneIUCD = (datajson.isNull("currentMOSIUCD")) ? empty :datajson.getInt("currentMOSIUCD");
            //Integer cmostwoIUCD = (datajson.isNull("currentMOS2IUCD")) ? empty :datajson.getInt("currentMOS2IUCD");
            Integer cmosoneIUCD = getIntegerFromJsonKey(datajson, "currentMOSIUCD");
            Integer cmostwoIUCD = getIntegerFromJsonKey(datajson, "currentMOS2IUCD");
            Integer stcardscoreIUCD = (datajson.isNull("stockCardScoreIUCD")) ? empty : datajson.getInt("stockCardScoreIUCD");
            Integer stCardsCountScoreIUCD = (datajson.isNull("stockCardsCountScoreIUCD")) ? empty : datajson.getInt("stockCardsCountScoreIUCD");
            Integer daysofStockScoreIUCD = (datajson.isNull("daysOutOfStockScoreIUCD")) ? empty : datajson.getInt("daysOutOfStockScoreIUCD");
            Integer iDenominatorIUCD = (datajson.isNull("itemDenominatorIUCD")) ? empty : datajson.getInt("itemDenominatorIUCD");
            Integer iNumeratorIUCD = (datajson.isNull("itemNumeratorIUCD")) ? empty : datajson.getInt("itemNumeratorIUCD");

            InventoryManagement iucd = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("IUCD").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityIUCD).stCountsDone(alCountsIUCD).dayOutStock(daysoutStockIUCD).cardBalance(cardbalanceIUCD).amc(amcvalueIUCD)
                    .expires6mo(expiriesIUCD).actualCount(actualcountIUCD).cmos1(cmosoneIUCD).cmos2(cmostwoIUCD).stCardScore(stcardscoreIUCD)
                    .stCardsCountScore(stCardsCountScoreIUCD).daysofStockScore(daysofStockScoreIUCD).iDenominator(iDenominatorIUCD).iNumerator(iNumeratorIUCD).build();

            try {
                inventoryDao.save(iucd);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectmCondom = null;
            if (!datajson.isNull("Male_Condom")) {
                jsonObjectmCondom = datajson.getJSONObject("Male_Condom");
            }
            String cardAvailabilitymCondom = jsonObjectmCondom.isNull("sCardAvailablemCondom") ? "" : jsonObjectmCondom.getString("sCardAvailablemCondom");
            Integer alCountsmCondom = getIntegerFromJsonKey(jsonObjectmCondom, "StocksAvailablemCondom");
            Integer daysoutStockmCondom = getIntegerFromJsonKey(jsonObjectmCondom, "daysOutOfStockmCondom");
            Integer cardbalancemCondom = getIntegerFromJsonKey(jsonObjectmCondom, "stockCardBalancemCondom");
            Integer amcvaluemCondom = getIntegerFromJsonKey(jsonObjectmCondom, "StockCardIssuesmCondom");
            String expiriesmCondom = jsonObjectmCondom.isNull("expiry6MonthsmCondom") ? "" : jsonObjectmCondom.getString("expiry6MonthsmCondom");
            Integer actualcountmCondom = getIntegerFromJsonKey(jsonObjectmCondom, "physicalStockmCondom");
            //Integer cmosonemCondom = (datajson.isNull("currentMOSmCondom")) ? empty :datajson.getInt("currentMOSmCondom");
            //Integer cmostwomCondom = (datajson.isNull("currentMOS2mCondom")) ? empty :datajson.getInt("currentMOS2mCondom");
            Integer cmosonemCondom = getIntegerFromJsonKey(datajson, "here");
            Integer cmostwomCondom = getIntegerFromJsonKey(datajson, "here");
            Integer stcardscoremCondom = (datajson.isNull("stockCardScoremCondom")) ? empty : datajson.getInt("stockCardScoremCondom");
            Integer stCardsCountScoremCondom = (datajson.isNull("stockCardsCountScoremCondom")) ? empty : datajson.getInt("stockCardsCountScoremCondom");
            Integer daysofStockScoremCondom = (datajson.isNull("daysOutOfStockScoremCondom")) ? empty : datajson.getInt("daysOutOfStockScoremCondom");
            Integer iDenominatormCondom = (datajson.isNull("itemDenominatormCondom")) ? empty : datajson.getInt("itemDenominatormCondom");
            Integer iNumeratormCondom = (datajson.isNull("itemNumeratormCondom")) ? empty : datajson.getInt("itemNumeratormCondom");

            InventoryManagement mcondom = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Male Condom").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitymCondom).stCountsDone(alCountsmCondom).dayOutStock(daysoutStockmCondom).cardBalance(cardbalancemCondom).amc(amcvaluemCondom)
                    .expires6mo(expiriesmCondom).actualCount(actualcountmCondom).cmos1(cmosonemCondom).cmos2(cmostwomCondom).stCardScore(stcardscoremCondom)
                    .stCardsCountScore(stCardsCountScoremCondom).daysofStockScore(daysofStockScoremCondom).iDenominator(iDenominatormCondom).iNumerator(iNumeratormCondom).build();

            try {
                inventoryDao.save(mcondom);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectChdine = null;
            if (!datajson.isNull("Chlorhexidine")) {
                jsonObjectChdine = datajson.getJSONObject("Chlorhexidine");
            }
            String cardAvailabilityChdine = jsonObjectChdine.isNull("sCardAvailableChdine") ? "" : jsonObjectChdine.getString("sCardAvailableChdine");
            Integer alCountsChdine = getIntegerFromJsonKey(jsonObjectChdine, "StocksAvailableChdine");
            Integer daysoutStockChdine = getIntegerFromJsonKey(jsonObjectChdine, "daysOutOfStockChdine");
            Integer cardbalanceChdine = getIntegerFromJsonKey(jsonObjectChdine, "stockCardBalanceChdine");
            Integer amcvalueChdine = getIntegerFromJsonKey(jsonObjectChdine, "StockCardIssuesChdine");
            String expiriesChdine = jsonObjectChdine.isNull("expiry6MonthsChdine") ? "" : jsonObjectChdine.getString("expiry6MonthsChdine");
            Integer actualcountChdine = getIntegerFromJsonKey(jsonObjectChdine, "physicalStockChdine");
            //Integer cmosoneChdine = (datajson.isNull("currentMOSChdine")) ? empty :datajson.getInt("currentMOSChdine");
            //Integer cmostwoChdine = (datajson.isNull("currentMOS2Chdine")) ? empty :datajson.getInt("currentMOS2Chdine");
            Integer cmosoneChdine = getIntegerFromJsonKey(datajson, "currentMOSChdine");
            Integer cmostwoChdine = getIntegerFromJsonKey(datajson, "currentMOS2Chdine");
            Integer stcardscoreChdine = (datajson.isNull("stockCardScoreChdine")) ? empty : datajson.getInt("stockCardScoreChdine");
            Integer stCardsCountScoreChdine = (datajson.isNull("stockCardsCountScoreChdine")) ? empty : datajson.getInt("stockCardsCountScoreChdine");
            Integer daysofStockScoreChdine = (datajson.isNull("daysOutOfStockScoreChdine")) ? empty : datajson.getInt("daysOutOfStockScoreChdine");
            Integer iDenominatorChdine = (datajson.isNull("itemDenominatorChdine")) ? empty : datajson.getInt("itemDenominatorChdine");
            Integer iNumeratorChdine = (datajson.isNull("itemNumeratorChdine")) ? empty : datajson.getInt("itemNumeratorChdine");

            InventoryManagement chdine = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Chlorhexidine 7.1%").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityChdine).stCountsDone(alCountsChdine).dayOutStock(daysoutStockChdine).cardBalance(cardbalanceChdine).amc(amcvalueChdine)
                    .expires6mo(expiriesChdine).actualCount(actualcountChdine).cmos1(cmosoneChdine).cmos2(cmostwoChdine).stCardScore(stcardscoreChdine)
                    .stCardsCountScore(stCardsCountScoreChdine).daysofStockScore(daysofStockScoreChdine).iDenominator(iDenominatorChdine).iNumerator(iNumeratorChdine).build();

            try {
                inventoryDao.save(chdine);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectNevirapine = null;
            if (!datajson.isNull("Nevirapine")) {
                jsonObjectNevirapine = datajson.getJSONObject("Nevirapine");
            }
            String cardAvailabilityNevirapine = jsonObjectNevirapine.isNull("sCardAvailableNevirapine") ? "" : jsonObjectNevirapine.getString("sCardAvailableNevirapine");
            Integer alCountsNevirapine = getIntegerFromJsonKey(jsonObjectNevirapine, "StocksAvailableNevirapine");
            Integer daysoutStockNevirapine = getIntegerFromJsonKey(jsonObjectNevirapine, "daysOutOfStockNevirapine");
            Integer cardbalanceNevirapine = getIntegerFromJsonKey(jsonObjectNevirapine, "stockCardBalanceNevirapine");
            Integer amcvalueNevirapine = getIntegerFromJsonKey(jsonObjectNevirapine, "StockCardIssuesNevirapine");
            String expiriesNevirapine = jsonObjectNevirapine.isNull("expiry6MonthsNevirapine") ? "" : jsonObjectNevirapine.getString("expiry6MonthsNevirapine");
            Integer actualcountNevirapine = getIntegerFromJsonKey(jsonObjectNevirapine, "physicalStockNevirapine");
            //Integer cmosoneNevirapine = (datajson.isNull("currentMOSNevirapine")) ? empty :datajson.getInt("currentMOSNevirapine");
            //Integer cmostwoNevirapine = (datajson.isNull("currentMOS2Nevirapine")) ? empty :datajson.getInt("currentMOS2Nevirapine");
            Integer cmosoneNevirapine = getIntegerFromJsonKey(datajson, "currentMOSNevirapine");
            Integer cmostwoNevirapine = getIntegerFromJsonKey(datajson, "currentMOS2Nevirapine");
            Integer stcardscoreNevirapine = (datajson.isNull("stockCardScoreNevirapine")) ? empty : datajson.getInt("stockCardScoreNevirapine");
            Integer stCardsCountScoreNevirapine = (datajson.isNull("stockCardsCountScoreNevirapine")) ? empty : datajson.getInt("stockCardsCountScoreNevirapine");
            Integer daysofStockScoreNevirapine = (datajson.isNull("daysOutOfStockScoreNevirapine")) ? empty : datajson.getInt("daysOutOfStockScoreNevirapine");
            Integer iDenominatorNevirapine = (datajson.isNull("itemDenominatorNevirapine")) ? empty : datajson.getInt("itemDenominatorNevirapine");
            Integer iNumeratorNevirapine = (datajson.isNull("itemNumeratorNevirapine")) ? empty : datajson.getInt("itemNumeratorNevirapine");

            InventoryManagement nevirapine = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Nevirapine liquid").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityNevirapine).stCountsDone(alCountsNevirapine).dayOutStock(daysoutStockNevirapine).cardBalance(cardbalanceNevirapine).amc(amcvalueNevirapine)
                    .expires6mo(expiriesNevirapine).actualCount(actualcountNevirapine).cmos1(cmosoneNevirapine).cmos2(cmostwoNevirapine).stCardScore(stcardscoreNevirapine)
                    .stCardsCountScore(stCardsCountScoreNevirapine).daysofStockScore(daysofStockScoreNevirapine).iDenominator(iDenominatorNevirapine).iNumerator(iNumeratorNevirapine).build();

            try {
                inventoryDao.save(nevirapine);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectctx = null;
            if (!datajson.isNull("cotrimoxazole")) {
                jsonObjectctx = datajson.getJSONObject("cotrimoxazole");
            }
            String cardAvailabilityctx = jsonObjectctx.isNull("sCardAvailablectx") ? "" : jsonObjectctx.getString("sCardAvailablectx");
            Integer alCountsctx = getIntegerFromJsonKey(jsonObjectctx, "StocksAvailablectx");
            Integer daysoutStockctx = getIntegerFromJsonKey(jsonObjectctx, "daysOutOfStockctx");
            Integer cardbalancectx = getIntegerFromJsonKey(jsonObjectctx, "stockCardBalancectx");
            Integer amcvaluectx = getIntegerFromJsonKey(jsonObjectctx, "StockCardIssuesctx");
            String expiriesctx = jsonObjectctx.isNull("expiry6Monthsctx") ? "" : jsonObjectctx.getString("expiry6Monthsctx");
            Integer actualcountctx = getIntegerFromJsonKey(jsonObjectctx, "physicalStockctx");
            //Integer cmosonectx = (datajson.isNull("currentMOSctx")) ? empty :datajson.getInt("currentMOSctx");
            //Integer cmostwoctx = (datajson.isNull("currentMOS2ctx")) ? empty :datajson.getInt("currentMOS2ctx");
            Integer cmosonectx = getIntegerFromJsonKey(datajson, "currentMOSctx");
            Integer cmostwoctx = getIntegerFromJsonKey(datajson, "currentMOS2ctx");
            Integer stcardscorectx = (datajson.isNull("stockCardScorectx")) ? empty : datajson.getInt("stockCardScorectx");
            Integer stCardsCountScorectx = (datajson.isNull("stockCardsCountScorectx")) ? empty : datajson.getInt("stockCardsCountScorectx");
            Integer daysofStockScorectx = (datajson.isNull("daysOutOfStockScorectx")) ? empty : datajson.getInt("daysOutOfStockScorectx");
            Integer iDenominatorctx = (datajson.isNull("itemDenominatorctx")) ? empty : datajson.getInt("itemDenominatorctx");
            Integer iNumeratorctx = (datajson.isNull("itemNumeratorctx")) ? empty : datajson.getInt("itemNumeratorctx");

            InventoryManagement al6s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("cotrimoxazole 960mg tabs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityctx).stCountsDone(alCountsctx).dayOutStock(daysoutStockctx).cardBalance(cardbalancectx).amc(amcvaluectx)
                    .expires6mo(expiriesctx).actualCount(actualcountctx).cmos1(cmosonectx).cmos2(cmostwoctx).stCardScore(stcardscorectx)
                    .stCardsCountScore(stCardsCountScorectx).daysofStockScore(daysofStockScorectx).iDenominator(iDenominatorctx).iNumerator(iNumeratorctx).build();

            try {
                inventoryDao.save(al6s);
            } catch (Exception e) {
                log.info("Duplication");
            }
/*
            JSONObject jsonObjectATVr = null;
            if (!datajson.isNull("ATV")) {
                jsonObjectATVr = datajson.getJSONObject("ATV");
            }
            String cardAvailabilityATVr = jsonObjectATVr.isNull("sCardAvailableATVr") ? "" : jsonObjectATVr.getString("sCardAvailableATVr");
            Integer alCountsATvVr = getIntegerFromJsonKey(jsonObjectATVr, "StocksAvailableATVr");
            Integer daysoutStockATVr = getIntegerFromJsonKey(jsonObjectATVr, "daysOutOfStockATVr");
            Integer cardbalanceATVr = getIntegerFromJsonKey(jsonObjectATVr, "stockCardBalanceATVr");
            Integer amcvalueATVr = getIntegerFromJsonKey(jsonObjectATVr, "StockCardIssuesATVr");
            String expiriesATVr = jsonObjectATVr.isNull("expiry6MonthsATVr") ? "" : jsonObjectATVr.getString("expiry6MonthsATVr");
            Integer actualcountATVr = getIntegerFromJsonKey(jsonObjectATVr, "physicalStockATVr");
            //Integer cmosoneATVr = (datajson.isNull("currentMOSATVr")) ? empty :datajson.getInt("currentMOSATVr");
            //Integer cmostwoATVr = (datajson.isNull("currentMOS2ATVr")) ? empty :datajson.getInt("currentMOS2ATVr");
            Integer cmosoneATVr = getIntegerFromJsonKey(datajson, "currentMOSATVr");
            Integer cmostwoATVr = getIntegerFromJsonKey(datajson, "currentMOS2ATVr");
            Integer stcardscoreATVr = (datajson.isNull("stockCardScoreATVr")) ? empty : datajson.getInt("stockCardScoreATVr");
            Integer stCardsCountScoreATVr = (datajson.isNull("stockCardsCountScoreATVr")) ? empty : datajson.getInt("stockCardsCountScoreATVr");
            Integer daysofStockScoreATVr = (datajson.isNull("daysOutOfStockScoreATVr")) ? empty : datajson.getInt("daysOutOfStockScoreATVr");
            Integer iDenominatorATVr = (datajson.isNull("itemDenominatorATVr")) ? empty : datajson.getInt("itemDenominatorATVr");
            Integer iNumeratorATVr = (datajson.isNull("itemNumeratorATVr")) ? empty : datajson.getInt("itemNumeratorATVr");

            InventoryManagement atvr = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("ATV/r 300/100mg tabs").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityATVr).stCountsDone(alCountsATVr).dayOutStock(daysoutStockATVr).cardBalance(cardbalanceATVr).amc(amcvalueATVr)
                    .expires6mo(expiriesATVr).actualCount(actualcountATVr).cmos1(cmosoneATVr).cmos2(cmostwoATVr).stCardScore(stcardscoreATVr)
                    .stCardsCountScore(stCardsCountScoreATVr).daysofStockScore(daysofStockScoreATVr).iDenominator(iDenominatorATVr).iNumerator(iNumeratorATVr).build();

            try {
                inventoryDao.save(atvr);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectLPVr = null;
            if (!datajson.isNull("LPV")) {
                jsonObjectLPVr = datajson.getJSONObject("LPV");
            }

            String cardAvailabilityLPVr = jsonObjectLPVr.isNull("sCardAvailableLPVr") ? "" : jsonObjectLPVr.getString("sCardAvailableLPVr");
            Integer alCountsLPVr = getIntegerFromJsonKey(jsonObjectLPVr, "StocksAvailableLPVr");
            Integer daysoutStockLPVr = getIntegerFromJsonKey(jsonObjectLPVr, "daysOutOfStockLPVr");
            Integer cardbalanceLPVr = getIntegerFromJsonKey(jsonObjectLPVr, "stockCardBalanceLPVr");
            Integer amcvalueLPVr = getIntegerFromJsonKey(jsonObjectLPVr, "StockCardIssuesLPVr");
            String expiriesLPVr = jsonObjectLPVr.isNull("expiry6MonthsLPVr") ? "" : jsonObjectLPVr.getString("expiry6MonthsLPVr");
            Integer actualcountLPVr = getIntegerFromJsonKey(jsonObjectLPVr, "physicalStockLPVr");
            //Integer cmosoneLPVr = (datajson.isNull("currentMOSLPVr")) ? empty :datajson.getInt("currentMOSLPVr");
            //Integer cmostwoLPVr = (datajson.isNull("currentMOS2LPVr")) ? empty :datajson.getInt("currentMOS2LPVr");
            Integer cmosoneLPVr = getIntegerFromJsonKey(datajson, "currentMOSLPVr");
            Integer cmostwoLPVr = getIntegerFromJsonKey(datajson, "currentMOS2LPVr");
            Integer stcardscoreLPVr = (datajson.isNull("stockCardScoreLPVr")) ? empty : datajson.getInt("stockCardScoreLPVr");
            Integer stCardsCountScoreLPVr = (datajson.isNull("stockCardsCountScoreLPVr")) ? empty : datajson.getInt("stockCardsCountScoreLPVr");
            Integer daysofStockScoreLPVr = (datajson.isNull("daysOutOfStockScoreLPVr")) ? empty : datajson.getInt("daysOutOfStockScoreLPVr");
            Integer iDenominatorLPVr = (datajson.isNull("itemDenominatorLPVr")) ? empty : datajson.getInt("itemDenominatorLPVr");
            Integer iNumeratorLPVr = (datajson.isNull("itemNumeratorLPVr")) ? empty : datajson.getInt("itemNumeratorLPVr");

            InventoryManagement lpvr = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("LPV/r 80/20mg/mL").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityLPVr).stCountsDone(alCountsLPVr).dayOutStock(daysoutStockLPVr).cardBalance(cardbalanceLPVr).amc(amcvalueLPVr)
                    .expires6mo(expiriesLPVr).actualCount(actualcountLPVr).cmos1(cmosoneLPVr).cmos2(cmostwoLPVr).stCardScore(stcardscoreLPVr)
                    .stCardsCountScore(stCardsCountScoreLPVr).daysofStockScore(daysofStockScoreLPVr).iDenominator(iDenominatorLPVr).iNumerator(iNumeratorLPVr).build();

            try {
                inventoryDao.save(lpvr);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
was closed earlier


            JSONObject jsonObjecttdf300 = null;
            if (!datajson.isNull("tdf300")) {
                jsonObjecttdf300 = datajson.getJSONObject("tdf300");
            }
            String cardAvailabilitytdf300 = jsonObjecttdf300.isNull("sCardAvailabletdf300") ? "" : jsonObjecttdf300.getString("sCardAvailabletdf300");
            Integer alCountstdf300 = getIntegerFromJsonKey(jsonObjecttdf300, "StocksAvailabletdf300");
            Integer daysoutStocktdf300 = getIntegerFromJsonKey(jsonObjecttdf300, "daysOutOfStocktdf300");
            Integer cardbalancetdf300 = getIntegerFromJsonKey(jsonObjecttdf300, "stockCardBalancetdf300");
            Integer amcvaluetdf300 = getIntegerFromJsonKey(jsonObjecttdf300, "StockCardIssuestdf300");
            String expiriestdf300 = jsonObjecttdf300.isNull("expiry6Monthstdf300") ? "" : jsonObjecttdf300.getString("expiry6Monthstdf300");
            Integer actualcounttdf300 = getIntegerFromJsonKey(jsonObjecttdf300, "physicalStocktdf300");
            //Integer cmosonetdf300 = (datajson.isNull("currentMOStdf300")) ? empty :datajson.getInt("currentMOStdf300");
            //Integer cmostwotdf300 = (datajson.isNull("currentMOS2tdf300")) ? empty :datajson.getInt("currentMOS2tdf300");
            Integer cmosonetdf300 = getIntegerFromJsonKey(datajson, "currentMOStdf300");
            Integer cmostwotdf300 = getIntegerFromJsonKey(datajson, "currentMOS2tdf300");
            Integer stcardscoretdf300 = (datajson.isNull("stockCardScoretdf300")) ? empty : datajson.getInt("stockCardScoretdf300");
            Integer stCardsCountScoretdf300 = (datajson.isNull("stockCardsCountScoretdf300")) ? empty : datajson.getInt("stockCardsCountScoretdf300");
            Integer daysofStockScoretdf300 = (datajson.isNull("daysOutOfStockScoretdf300")) ? empty : datajson.getInt("daysOutOfStockScoretdf300");
            Integer iDenominatortdf300 = (datajson.isNull("itemDenominatortdf300")) ? empty : datajson.getInt("itemDenominatortdf300");
            Integer iNumeratortdf300 = (datajson.isNull("itemNumeratortdf300")) ? empty : datajson.getInt("itemNumeratortdf300");

            InventoryManagement tdf300 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("TDF/3TC 300/300mg, 30s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitytdf300).stCountsDone(alCountstdf300).dayOutStock(daysoutStocktdf300).cardBalance(cardbalancetdf300).amc(amcvaluetdf300)
                    .expires6mo(expiriestdf300).actualCount(actualcounttdf300).cmos1(cmosonetdf300).cmos2(cmostwotdf300).stCardScore(stcardscoretdf300)
                    .stCardsCountScore(stCardsCountScoretdf300).daysofStockScore(daysofStockScoretdf300).iDenominator(iDenominatortdf300).iNumerator(iNumeratortdf300).build();
         
            try {
                inventoryDao.save(tdf300);
            } catch (Exception e) {
                log.info("Duplication");
            }




            JSONObject jsonObjectdtg5030s = null;
            if (!datajson.isNull("dtg5030s")) {
                jsonObjectdtg5030s = datajson.getJSONObject("dtg5030s");
            }
            String cardAvailabilitydtg5030s = jsonObjectdtg5030s.isNull("sCardAvailabledtg5030s") ? "" : jsonObjectdtg5030s.getString("sCardAvailabledtg5030s");
            Integer alCountsdtg5030s = getIntegerFromJsonKey(jsonObjectdtg5030s, "StocksAvailabledtg5030s");
            Integer daysoutStockdtg5030s = getIntegerFromJsonKey(jsonObjectdtg5030s, "daysOutOfStockdtg5030s");
            Integer cardbalancedtg5030s = getIntegerFromJsonKey(jsonObjectdtg5030s, "stockCardBalancedtg5030s");
            Integer amcvaluedtg5030s = getIntegerFromJsonKey(jsonObjectdtg5030s, "StockCardIssuesdtg5030s");
            String expiriesdtg5030s = jsonObjectdtg5030s.isNull("expiry6Monthsdtg5030s") ? "" : jsonObjectdtg5030s.getString("expiry6Monthsdtg5030s");
            Integer actualcountdtg5030s = getIntegerFromJsonKey(jsonObjectdtg5030s, "physicalStockdtg5030s");
            //Integer cmosonedtg5030s = (datajson.isNull("currentMOSdtg5030s")) ? empty :datajson.getInt("currentMOSdtg5030s");
            //Integer cmostwodtg5030s = (datajson.isNull("currentMOS2dtg5030s")) ? empty :datajson.getInt("currentMOS2dtg5030s");
            Integer cmosonedtg5030s = getIntegerFromJsonKey(datajson, "currentMOSdtg5030s");
            Integer cmostwodtg5030s = getIntegerFromJsonKey(datajson, "currentMOS2dtg5030s");
            Integer stcardscoredtg5030s = (datajson.isNull("stockCardScoredtg5030s")) ? empty : datajson.getInt("stockCardScoredtg5030s");
            Integer stCardsCountScoredtg5030s = (datajson.isNull("stockCardsCountScoredtg5030s")) ? empty : datajson.getInt("stockCardsCountScoredtg5030s");
            Integer daysofStockScoredtg5030s = (datajson.isNull("daysOutOfStockScoredtg5030s")) ? empty : datajson.getInt("daysOutOfStockScoredtg5030s");
            Integer iDenominatordtg5030s = (datajson.isNull("itemDenominatordtg5030s")) ? empty : datajson.getInt("itemDenominatordtg5030s");
            Integer iNumeratordtg5030s = (datajson.isNull("itemNumeratordtg5030s")) ? empty : datajson.getInt("itemNumeratordtg5030s");

            InventoryManagement dtg5030s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("DTG 50mg, 30s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitydtg5030s).stCountsDone(alCountsdtg5030s).dayOutStock(daysoutStockdtg5030s).cardBalance(cardbalancedtg5030s).amc(amcvaluedtg5030s)
                    .expires6mo(expiriesdtg5030s).actualCount(actualcountdtg5030s).cmos1(cmosonedtg5030s).cmos2(cmostwodtg5030s).stCardScore(stcardscoredtg5030s)
                    .stCardsCountScore(stCardsCountScoredtg5030s).daysofStockScore(daysofStockScoredtg5030s).iDenominator(iDenominatordtg5030s).iNumerator(iNumeratordtg5030s).build();

            try {
                inventoryDao.save(dtg5030s);
            } catch (Exception e) {
                log.info("Duplication");
            }


            JSONObject jsonObjectazt30060s = null;
            if (!datajson.isNull("azt30060s")) {
                jsonObjectazt30060s = datajson.getJSONObject("azt30060s");
            }
            String cardAvailabilityazt30060s = jsonObjectazt30060s.isNull("sCardAvailableazt30060s") ? "" : jsonObjectazt30060s.getString("sCardAvailableazt30060s");
            Integer alCountsazt30060s = getIntegerFromJsonKey(jsonObjectazt30060s, "StocksAvailableazt30060s");
            Integer daysoutStockazt30060s = getIntegerFromJsonKey(jsonObjectazt30060s, "daysOutOfStockazt30060s");
            Integer cardbalanceazt30060s = getIntegerFromJsonKey(jsonObjectazt30060s, "stockCardBalanceazt30060s");
            Integer amcvalueazt30060s = getIntegerFromJsonKey(jsonObjectazt30060s, "StockCardIssuesazt30060s");
            String expiriesazt30060s = jsonObjectazt30060s.isNull("expiry6Monthsazt30060s") ? "" : jsonObjectazt30060s.getString("expiry6Monthsazt30060s");
            Integer actualcountazt30060s = getIntegerFromJsonKey(jsonObjectazt30060s, "physicalStockazt30060s");
            //Integer cmosoneazt30060s = (datajson.isNull("currentMOSazt30060s")) ? empty :datajson.getInt("currentMOSazt30060s");
            //Integer cmostwoazt30060s = (datajson.isNull("currentMOS2azt30060s")) ? empty :datajson.getInt("currentMOS2azt30060s");
            Integer cmosoneazt30060s = getIntegerFromJsonKey(datajson, "currentMOSazt30060s");
            Integer cmostwoazt30060s = getIntegerFromJsonKey(datajson, "currentMOS2azt30060s");
            Integer stcardscoreazt30060s = (datajson.isNull("stockCardScoreazt30060s")) ? empty : datajson.getInt("stockCardScoreazt30060s");
            Integer stCardsCountScoreazt30060s = (datajson.isNull("stockCardsCountScoreazt30060s")) ? empty : datajson.getInt("stockCardsCountScoreazt30060s");
            Integer daysofStockScoreazt30060s = (datajson.isNull("daysOutOfStockScoreazt30060s")) ? empty : datajson.getInt("daysOutOfStockScoreazt30060s");
            Integer iDenominatorazt30060s = (datajson.isNull("itemDenominatorazt30060s")) ? empty : datajson.getInt("itemDenominatorazt30060s");
            Integer iNumeratorazt30060s = (datajson.isNull("itemNumeratorazt30060s")) ? empty : datajson.getInt("itemNumeratorazt30060s");

            InventoryManagement azt30060s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AZT/3TC 300/150mg, 60s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityazt30060s).stCountsDone(alCountsazt30060s).dayOutStock(daysoutStockazt30060s).cardBalance(cardbalanceazt30060s).amc(amcvalueazt30060s)
                    .expires6mo(expiriesazt30060s).actualCount(actualcountazt30060s).cmos1(cmosoneazt30060s).cmos2(cmostwoazt30060s).stCardScore(stcardscoreazt30060s)
                    .stCardsCountScore(stCardsCountScoreazt30060s).daysofStockScore(daysofStockScoreazt30060s).iDenominator(iDenominatorazt30060s).iNumerator(iNumeratorazt30060s).build();

            try {
                inventoryDao.save(azt30060s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectfluco300mgs = null;
            if (!datajson.isNull("fluconazole200mgs")) {
                jsonObjectfluco300mgs = datajson.getJSONObject("fluconazole200mgs");
            }
            String cardAvailabilityfluco300mgs = jsonObjectfluco300mgs.isNull("sCardAvailablefluco300mgs") ? "" : jsonObjectfluco300mgs.getString("sCardAvailablefluco300mgs");
            Integer alCountsfluco300mgs = getIntegerFromJsonKey(jsonObjectfluco300mgs, "StocksAvailablefluco300mgs");
            Integer daysoutStockfluco300mgs = getIntegerFromJsonKey(jsonObjectfluco300mgs, "daysOutOfStockfluco300mgs");
            Integer cardbalancefluco300mgs = getIntegerFromJsonKey(jsonObjectfluco300mgs, "stockCardBalancefluco300mgs");
            Integer amcvaluefluco300mgs = getIntegerFromJsonKey(jsonObjectfluco300mgs, "StockCardIssuesfluco300mgs");
            String expiriesfluco300mgs = jsonObjectfluco300mgs.isNull("expiry6Monthsfluco300mgs") ? "" : jsonObjectfluco300mgs.getString("expiry6Monthsfluco300mgs");
            Integer actualcountfluco300mgs = getIntegerFromJsonKey(jsonObjectfluco300mgs, "physicalStockfluco300mgs");
            //Integer cmosonefluco300mgs = (datajson.isNull("currentMOSfluco300mgs")) ? empty :datajson.getInt("currentMOSfluco300mgs");
            //Integer cmostwofluco300mgs = (datajson.isNull("currentMOS2fluco300mgs")) ? empty :datajson.getInt("currentMOS2fluco300mgs");
            Integer cmosonefluco300mgs = getIntegerFromJsonKey(datajson, "currentMOSfluco300mgs");
            Integer cmostwofluco300mgs = getIntegerFromJsonKey(datajson, "currentMOS2fluco300mgs");
            Integer stcardscorefluco300mgs = (datajson.isNull("stockCardScorefluco300mgs")) ? empty : datajson.getInt("stockCardScorefluco300mgs");
            Integer stCardsCountScorefluco300mgs = (datajson.isNull("stockCardsCountScorefluco300mgs")) ? empty : datajson.getInt("stockCardsCountScorefluco300mgs");
            Integer daysofStockScorefluco300mgs = (datajson.isNull("daysOutOfStockScorefluco300mgs")) ? empty : datajson.getInt("daysOutOfStockScorefluco300mgs");
            Integer iDenominatorfluco300mgs = (datajson.isNull("itemDenominatorfluco300mgs")) ? empty : datajson.getInt("itemDenominatorfluco300mgs");
            Integer iNumeratorfluco300mgs = (datajson.isNull("itemNumeratorfluco300mgs")) ? empty : datajson.getInt("itemNumeratorfluco300mgs");

            InventoryManagement fluco300mgs = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Fluconazole 200mg tablets").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityfluco300mgs).stCountsDone(alCountsfluco300mgs).dayOutStock(daysoutStockfluco300mgs).cardBalance(cardbalancefluco300mgs).amc(amcvaluefluco300mgs)
                    .expires6mo(expiriesfluco300mgs).actualCount(actualcountfluco300mgs).cmos1(cmosonefluco300mgs).cmos2(cmostwofluco300mgs).stCardScore(stcardscorefluco300mgs)
                    .stCardsCountScore(stCardsCountScorefluco300mgs).daysofStockScore(daysofStockScorefluco300mgs).iDenominator(iDenominatorfluco300mgs).iNumerator(iNumeratorfluco300mgs).build();

            try {
                inventoryDao.save(fluco300mgs);
            } catch (Exception e) {
                log.info("Duplication");
            }


            JSONObject jsonObjectoraquick = null;
            if (!datajson.isNull("oraquick")) {
                jsonObjectoraquick = datajson.getJSONObject("oraquick");
            }
            String cardAvailabilityoraquick = jsonObjectoraquick.isNull("sCardAvailableoraquick") ? "" : jsonObjectoraquick.getString("sCardAvailableoraquick");
            Integer alCountsoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "StocksAvailableoraquick");
            Integer daysoutStockoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "daysOutOfStockoraquick");
            Integer cardbalanceoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "stockCardBalanceoraquick");
            Integer amcvalueoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "StockCardIssuesoraquick");
            String expiriesoraquick = jsonObjectoraquick.isNull("expiry6Monthsoraquick") ? "" : jsonObjectoraquick.getString("expiry6Monthsoraquick");
            Integer actualcountoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "physicalStockoraquick");
            //Integer cmosoneoraquick = (datajson.isNull("currentMOSoraquick")) ? empty :datajson.getInt("currentMOSoraquick");
            //Integer cmostwooraquick = (datajson.isNull("currentMOS2oraquick")) ? empty :datajson.getInt("currentMOS2oraquick");
            Integer cmosoneoraquick = getIntegerFromJsonKey(datajson, "currentMOSoraquick");
            Integer cmostwooraquick = getIntegerFromJsonKey(datajson, "currentMOS2oraquick");
            Integer stcardscoreoraquick = (datajson.isNull("stockCardScoreoraquick")) ? empty : datajson.getInt("stockCardScoreoraquick");
            Integer stCardsCountScoreoraquick = (datajson.isNull("stockCardsCountScoreoraquick")) ? empty : datajson.getInt("stockCardsCountScoreoraquick");
            Integer daysofStockScoreoraquick = (datajson.isNull("daysOutOfStockScoreoraquick")) ? empty : datajson.getInt("daysOutOfStockScoreoraquick");
            Integer iDenominatororaquick = (datajson.isNull("itemDenominatororaquick")) ? empty : datajson.getInt("itemDenominatororaquick");
            Integer iNumeratororaquick = (datajson.isNull("itemNumeratororaquick")) ? empty : datajson.getInt("itemNumeratororaquick");

            InventoryManagement oraquick = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("HIV self-test (Oraquick), tests").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityoraquick).stCountsDone(alCountsoraquick).dayOutStock(daysoutStockoraquick).cardBalance(cardbalanceoraquick).amc(amcvalueoraquick)
                    .expires6mo(expiriesoraquick).actualCount(actualcountoraquick).cmos1(cmosoneoraquick).cmos2(cmostwooraquick).stCardScore(stcardscoreoraquick)
                    .stCardsCountScore(stCardsCountScoreoraquick).daysofStockScore(daysofStockScoreoraquick).iDenominator(iDenominatororaquick).iNumerator(iNumeratororaquick).build();

            try {
                inventoryDao.save(oraquick);
            } catch (Exception e) {
                log.info("Duplication");
            }


            JSONObject jsonObjecthivconf = null;
            if (!datajson.isNull("hivconf")) {
                jsonObjecthivconf = datajson.getJSONObject("hivconf");
            }
            String cardAvailabilityhivconf = jsonObjecthivconf.isNull("sCardAvailablehivconf") ? "" : jsonObjecthivconf.getString("sCardAvailablehivconf");
            Integer alCountshivconf = getIntegerFromJsonKey(jsonObjecthivconf, "StocksAvailablehivconf");
            Integer daysoutStockhivconf = getIntegerFromJsonKey(jsonObjecthivconf, "daysOutOfStockhivconf");
            Integer cardbalancehivconf = getIntegerFromJsonKey(jsonObjecthivconf, "stockCardBalancehivconf");
            Integer amcvaluehivconf = getIntegerFromJsonKey(jsonObjecthivconf, "StockCardIssueshivconf");
            String expirieshivconf = jsonObjecthivconf.isNull("expiry6Monthshivconf") ? "" : jsonObjecthivconf.getString("expiry6Monthshivconf");
            Integer actualcounthivconf = getIntegerFromJsonKey(jsonObjecthivconf, "physicalStockhivconf");
            //Integer cmosonehivconf = (datajson.isNull("currentMOShivconf")) ? empty :datajson.getInt("currentMOShivconf");
            //Integer cmostwohivconf = (datajson.isNull("currentMOS2hivconf")) ? empty :datajson.getInt("currentMOS2hivconf");
            Integer cmosonehivconf = getIntegerFromJsonKey(datajson, "currentMOShivconf");
            Integer cmostwohivconf = getIntegerFromJsonKey(datajson, "currentMOS2hivconf");
            Integer stcardscorehivconf = (datajson.isNull("stockCardScorehivconf")) ? empty : datajson.getInt("stockCardScorehivconf");
            Integer stCardsCountScorehivconf = (datajson.isNull("stockCardsCountScorehivconf")) ? empty : datajson.getInt("stockCardsCountScorehivconf");
            Integer daysofStockScorehivconf = (datajson.isNull("daysOutOfStockScorehivconf")) ? empty : datajson.getInt("daysOutOfStockScorehivconf");
            Integer iDenominatorhivconf = (datajson.isNull("itemDenominatorhivconf")) ? empty : datajson.getInt("itemDenominatorhivconf");
            Integer iNumeratorhivconf = (datajson.isNull("itemNumeratorhivconf")) ? empty : datajson.getInt("itemNumeratorhivconf");

            InventoryManagement hivconf = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("HIV Confirmatory Test Kit, Tests").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityhivconf).stCountsDone(alCountshivconf).dayOutStock(daysoutStockhivconf).cardBalance(cardbalancehivconf).amc(amcvaluehivconf)
                    .expires6mo(expirieshivconf).actualCount(actualcounthivconf).cmos1(cmosonehivconf).cmos2(cmostwohivconf).stCardScore(stcardscorehivconf)
                    .stCardsCountScore(stCardsCountScorehivconf).daysofStockScore(daysofStockScorehivconf).iDenominator(iDenominatorhivconf).iNumerator(iNumeratorhivconf).build();

            try {
                inventoryDao.save(hivconf);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectcalc10 = null;
            if (!datajson.isNull("calc10")) {
                jsonObjectcalc10 = datajson.getJSONObject("calc10");
            }
            String cardAvailabilitycalc10 = jsonObjectcalc10.isNull("sCardAvailablecalc10") ? "" : jsonObjectcalc10.getString("sCardAvailablecalc10");
            Integer alCountscalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "StocksAvailablecalc10");
            Integer daysoutStockcalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "daysOutOfStockcalc10");
            Integer cardbalancecalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "stockCardBalancecalc10");
            Integer amcvaluecalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "StockCardIssuescalc10");
            String expiriescalc10 = jsonObjectcalc10.isNull("expiry6Monthscalc10") ? "" : jsonObjectcalc10.getString("expiry6Monthscalc10");
            Integer actualcountcalc10 = getIntegerFromJsonKey(jsonObjectcalc10, "physicalStockcalc10");
            //Integer cmosonecalc10 = (datajson.isNull("currentMOScalc10")) ? empty :datajson.getInt("currentMOScalc10");
            //Integer cmostwocalc10 = (datajson.isNull("currentMOS2calc10")) ? empty :datajson.getInt("currentMOS2calc10");
            Integer cmosonecalc10 = getIntegerFromJsonKey(datajson, "currentMOScalc10");
            Integer cmostwocalc10 = getIntegerFromJsonKey(datajson, "currentMOS2calc10");
            Integer stcardscorecalc10 = (datajson.isNull("stockCardScorecalc10")) ? empty : datajson.getInt("stockCardScorecalc10");
            Integer stCardsCountScorecalc10 = (datajson.isNull("stockCardsCountScorecalc10")) ? empty : datajson.getInt("stockCardsCountScorecalc10");
            Integer daysofStockScorecalc10 = (datajson.isNull("daysOutOfStockScorecalc10")) ? empty : datajson.getInt("daysOutOfStockScorecalc10");
            Integer iDenominatorcalc10 = (datajson.isNull("itemDenominatorcalc10")) ? empty : datajson.getInt("itemDenominatorcalc10");
            Integer iNumeratorcalc10 = (datajson.isNull("itemNumeratorcalc10")) ? empty : datajson.getInt("itemNumeratorcalc10");

            InventoryManagement calc10 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Calcium gluconate Inj, 10%, 10ml amp").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitycalc10).stCountsDone(alCountscalc10).dayOutStock(daysoutStockcalc10).cardBalance(cardbalancecalc10).amc(amcvaluecalc10)
                    .expires6mo(expiriescalc10).actualCount(actualcountcalc10).cmos1(cmosonecalc10).cmos2(cmostwocalc10).stCardScore(stcardscorecalc10)
                    .stCardsCountScore(stCardsCountScorecalc10).daysofStockScore(daysofStockScorecalc10).iDenominator(iDenominatorcalc10).iNumerator(iNumeratorcalc10).build();

            try {
                inventoryDao.save(calc10);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectdexa4mg = null;
            if (!datajson.isNull("dexa4mg")) {
                jsonObjectdexa4mg = datajson.getJSONObject("dexa4mg");
            }
            String cardAvailabilitydexa4mg = jsonObjectdexa4mg.isNull("sCardAvailabledexa4mg") ? "" : jsonObjectdexa4mg.getString("sCardAvailabledexa4mg");
            Integer alCountsdexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "StocksAvailabledexa4mg");
            Integer daysoutStockdexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "daysOutOfStockdexa4mg");
            Integer cardbalancedexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "stockCardBalancedexa4mg");
            Integer amcvaluedexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "StockCardIssuesdexa4mg");
            String expiriesdexa4mg = jsonObjectdexa4mg.isNull("expiry6Monthsdexa4mg") ? "" : jsonObjectdexa4mg.getString("expiry6Monthsdexa4mg");
            Integer actualcountdexa4mg = getIntegerFromJsonKey(jsonObjectdexa4mg, "physicalStockdexa4mg");
            //Integer cmosonedexa4mg = (datajson.isNull("currentMOSdexa4mg")) ? empty :datajson.getInt("currentMOSdexa4mg");
            //Integer cmostwodexa4mg = (datajson.isNull("currentMOS2dexa4mg")) ? empty :datajson.getInt("currentMOS2dexa4mg");
            Integer cmosonedexa4mg = getIntegerFromJsonKey(datajson, "currentMOSdexa4mg");
            Integer cmostwodexa4mg = getIntegerFromJsonKey(datajson, "currentMOS2dexa4mg");
            Integer stcardscoredexa4mg = (datajson.isNull("stockCardScoredexa4mg")) ? empty : datajson.getInt("stockCardScoredexa4mg");
            Integer stCardsCountScoredexa4mg = (datajson.isNull("stockCardsCountScoredexa4mg")) ? empty : datajson.getInt("stockCardsCountScoredexa4mg");
            Integer daysofStockScoredexa4mg = (datajson.isNull("daysOutOfStockScoredexa4mg")) ? empty : datajson.getInt("daysOutOfStockScoredexa4mg");
            Integer iDenominatordexa4mg = (datajson.isNull("itemDenominatordexa4mg")) ? empty : datajson.getInt("itemDenominatordexa4mg");
            Integer iNumeratordexa4mg = (datajson.isNull("itemNumeratordexa4mg")) ? empty : datajson.getInt("itemNumeratordexa4mg");

            InventoryManagement dexa4mg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Dexamethasone Inj, 4mg/ml, amp").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitydexa4mg).stCountsDone(alCountsdexa4mg).dayOutStock(daysoutStockdexa4mg).cardBalance(cardbalancedexa4mg).amc(amcvaluedexa4mg)
                    .expires6mo(expiriesdexa4mg).actualCount(actualcountdexa4mg).cmos1(cmosonedexa4mg).cmos2(cmostwodexa4mg).stCardScore(stcardscoredexa4mg)
                    .stCardsCountScore(stCardsCountScoredexa4mg).daysofStockScore(daysofStockScoredexa4mg).iDenominator(iDenominatordexa4mg).iNumerator(iNumeratordexa4mg).build();

            try {
                inventoryDao.save(dexa4mg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectmetro500mgs = null;
            if (!datajson.isNull("metro500mgs")) {
                jsonObjectmetro500mgs = datajson.getJSONObject("metro500mgs");
            }
            String cardAvailabilitymetro500mgs = jsonObjectmetro500mgs.isNull("sCardAvailablemetro500mgs") ? "" : jsonObjectmetro500mgs.getString("sCardAvailablemetro500mgs");
            Integer alCountsmetro500mgs = getIntegerFromJsonKey(jsonObjectmetro500mgs, "StocksAvailablemetro500mgs");
            Integer daysoutStockmetro500mgs = getIntegerFromJsonKey(jsonObjectmetro500mgs, "daysOutOfStockmetro500mgs");
            Integer cardbalancemetro500mgs = getIntegerFromJsonKey(jsonObjectmetro500mgs, "stockCardBalancemetro500mgs");
            Integer amcvaluemetro500mgs = getIntegerFromJsonKey(jsonObjectmetro500mgs, "StockCardIssuesmetro500mgs");
            String expiriesmetro500mgs = jsonObjectmetro500mgs.isNull("expiry6Monthsmetro500mgs") ? "" : jsonObjectmetro500mgs.getString("expiry6Monthsmetro500mgs");
            Integer actualcountmetro500mgs = getIntegerFromJsonKey(jsonObjectmetro500mgs, "physicalStockmetro500mgs");
            //Integer cmosonemetro500mgs = (datajson.isNull("currentMOSmetro500mgs")) ? empty :datajson.getInt("currentMOSmetro500mgs");
            //Integer cmostwometro500mgs = (datajson.isNull("currentMOS2metro500mgs")) ? empty :datajson.getInt("currentMOS2metro500mgs");
            Integer cmosonemetro500mgs = getIntegerFromJsonKey(datajson, "currentMOSmetro500mgs");
            Integer cmostwometro500mgs = getIntegerFromJsonKey(datajson, "currentMOS2metro500mgs");
            Integer stcardscoremetro500mgs = (datajson.isNull("stockCardScoremetro500mgs")) ? empty : datajson.getInt("stockCardScoremetro500mgs");
            Integer stCardsCountScoremetro500mgs = (datajson.isNull("stockCardsCountScoremetro500mgs")) ? empty : datajson.getInt("stockCardsCountScoremetro500mgs");
            Integer daysofStockScoremetro500mgs = (datajson.isNull("daysOutOfStockScoremetro500mgs")) ? empty : datajson.getInt("daysOutOfStockScoremetro500mgs");
            Integer iDenominatormetro500mgs = (datajson.isNull("itemDenominatormetro500mgs")) ? empty : datajson.getInt("itemDenominatormetro500mgs");
            Integer iNumeratormetro500mgs = (datajson.isNull("itemNumeratormetro500mgs")) ? empty : datajson.getInt("itemNumeratormetro500mgs");

            InventoryManagement metro500mgs = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Metronidazole 500mg IV, bottle").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitymetro500mgs).stCountsDone(alCountsmetro500mgs).dayOutStock(daysoutStockmetro500mgs).cardBalance(cardbalancemetro500mgs).amc(amcvaluemetro500mgs)
                    .expires6mo(expiriesmetro500mgs).actualCount(actualcountmetro500mgs).cmos1(cmosonemetro500mgs).cmos2(cmostwometro500mgs).stCardScore(stcardscoremetro500mgs)
                    .stCardsCountScore(stCardsCountScoremetro500mgs).daysofStockScore(daysofStockScoremetro500mgs).iDenominator(iDenominatormetro500mgs).iNumerator(iNumeratormetro500mgs).build();

            try {
                inventoryDao.save(metro500mgs);
            } catch (Exception e) {
                log.info("Duplication");
            }


            JSONObject jsonObjectnifed10 = null;
            if (!datajson.isNull("nifed10")) {
                jsonObjectnifed10 = datajson.getJSONObject("nifed10");
            }
            String cardAvailabilitynifed10 = jsonObjectnifed10.isNull("sCardAvailablenifed10") ? "" : jsonObjectnifed10.getString("sCardAvailablenifed10");
            Integer alCountsnifed10 = getIntegerFromJsonKey(jsonObjectnifed10, "StocksAvailablenifed10");
            Integer daysoutStocknifed10 = getIntegerFromJsonKey(jsonObjectnifed10, "daysOutOfStocknifed10");
            Integer cardbalancenifed10 = getIntegerFromJsonKey(jsonObjectnifed10, "stockCardBalancenifed10");
            Integer amcvaluenifed10 = getIntegerFromJsonKey(jsonObjectnifed10, "StockCardIssuesnifed10");
            String expiriesnifed10 = jsonObjectnifed10.isNull("expiry6Monthsnifed10") ? "" : jsonObjectnifed10.getString("expiry6Monthsnifed10");
            Integer actualcountnifed10 = getIntegerFromJsonKey(jsonObjectnifed10, "physicalStocknifed10");
            //Integer cmosonenifed10 = (datajson.isNull("currentMOSnifed10")) ? empty :datajson.getInt("currentMOSnifed10");
            //Integer cmostwonifed10 = (datajson.isNull("currentMOS2nifed10")) ? empty :datajson.getInt("currentMOS2nifed10");
            Integer cmosonenifed10 = getIntegerFromJsonKey(datajson, "currentMOSnifed10");
            Integer cmostwonifed10 = getIntegerFromJsonKey(datajson, "currentMOS2nifed10");
            Integer stcardscorenifed10 = (datajson.isNull("stockCardScorenifed10")) ? empty : datajson.getInt("stockCardScorenifed10");
            Integer stCardsCountScorenifed10 = (datajson.isNull("stockCardsCountScorenifed10")) ? empty : datajson.getInt("stockCardsCountScorenifed10");
            Integer daysofStockScorenifed10 = (datajson.isNull("daysOutOfStockScorenifed10")) ? empty : datajson.getInt("daysOutOfStockScorenifed10");
            Integer iDenominatornifed10 = (datajson.isNull("itemDenominatornifed10")) ? empty : datajson.getInt("itemDenominatornifed10");
            Integer iNumeratornifed10 = (datajson.isNull("itemNumeratornifed10")) ? empty : datajson.getInt("itemNumeratornifed10");

            InventoryManagement nifed10 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Nifedipine 10mg capsule").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitynifed10).stCountsDone(alCountsnifed10).dayOutStock(daysoutStocknifed10).cardBalance(cardbalancenifed10).amc(amcvaluenifed10)
                    .expires6mo(expiriesnifed10).actualCount(actualcountnifed10).cmos1(cmosonenifed10).cmos2(cmostwonifed10).stCardScore(stcardscorenifed10)
                    .stCardsCountScore(stCardsCountScorenifed10).daysofStockScore(daysofStockScorenifed10).iDenominator(iDenominatornifed10).iNumerator(iNumeratornifed10).build();

            try {
                inventoryDao.save(nifed10);
            } catch (Exception e) {
                log.info("Duplication");
            }


            JSONObject jsonObjectphyto1mg = null;
            if (!datajson.isNull("phyto1mg")) {
                jsonObjectphyto1mg = datajson.getJSONObject("phyto1mg");
            }
            String cardAvailabilityphyto1mg = jsonObjectphyto1mg.isNull("sCardAvailablephyto1mg") ? "" : jsonObjectphyto1mg.getString("sCardAvailablephyto1mg");
            Integer alCountsphyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "StocksAvailablephyto1mg");
            Integer daysoutStockphyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "daysOutOfStockphyto1mg");
            Integer cardbalancephyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "stockCardBalancephyto1mg");
            Integer amcvaluephyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "StockCardIssuesphyto1mg");
            String expiriesphyto1mg = jsonObjectphyto1mg.isNull("expiry6Monthsphyto1mg") ? "" : jsonObjectphyto1mg.getString("expiry6Monthsphyto1mg");
            Integer actualcountphyto1mg = getIntegerFromJsonKey(jsonObjectphyto1mg, "physicalStockphyto1mg");
            //Integer cmosonephyto1mg = (datajson.isNull("currentMOSphyto1mg")) ? empty :datajson.getInt("currentMOSphyto1mg");
            //Integer cmostwophyto1mg = (datajson.isNull("currentMOS2phyto1mg")) ? empty :datajson.getInt("currentMOS2phyto1mg");
            Integer cmosonephyto1mg = getIntegerFromJsonKey(datajson, "currentMOSphyto1mg");
            Integer cmostwophyto1mg = getIntegerFromJsonKey(datajson, "currentMOS2phyto1mg");
            Integer stcardscorephyto1mg = (datajson.isNull("stockCardScorephyto1mg")) ? empty : datajson.getInt("stockCardScorephyto1mg");
            Integer stCardsCountScorephyto1mg = (datajson.isNull("stockCardsCountScorephyto1mg")) ? empty : datajson.getInt("stockCardsCountScorephyto1mg");
            Integer daysofStockScorephyto1mg = (datajson.isNull("daysOutOfStockScorephyto1mg")) ? empty : datajson.getInt("daysOutOfStockScorephyto1mg");
            Integer iDenominatorphyto1mg = (datajson.isNull("itemDenominatorphyto1mg")) ? empty : datajson.getInt("itemDenominatorphyto1mg");
            Integer iNumeratorphyto1mg = (datajson.isNull("itemNumeratorphyto1mg")) ? empty : datajson.getInt("itemNumeratorphyto1mg");

            InventoryManagement phyto1mg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Phytomenadione Inj, 1mg/0.1ml").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityphyto1mg).stCountsDone(alCountsphyto1mg).dayOutStock(daysoutStockphyto1mg).cardBalance(cardbalancephyto1mg).amc(amcvaluephyto1mg)
                    .expires6mo(expiriesphyto1mg).actualCount(actualcountphyto1mg).cmos1(cmosonephyto1mg).cmos2(cmostwophyto1mg).stCardScore(stcardscorephyto1mg)
                    .stCardsCountScore(stCardsCountScorephyto1mg).daysofStockScore(daysofStockScorephyto1mg).iDenominator(iDenominatorphyto1mg).iNumerator(iNumeratorphyto1mg).build();

            try {
                inventoryDao.save(phyto1mg);
            } catch (Exception e) {
                log.info("Duplication");
            }


            JSONObject jsonObjectFBF = null;
            if (!datajson.isNull("Paediatrics")) {
                jsonObjectFBF = datajson.getJSONObject("Paediatrics");
            }
            String cardAvailabilityFBF = jsonObjectFBF.isNull("sCardAvailableFBF") ? "" : jsonObjectFBF.getString("sCardAvailableFBF");
            Integer alCountsFBF = getIntegerFromJsonKey(jsonObjectFBF, "StocksAvailableFBF");
            Integer daysoutStockFBF = getIntegerFromJsonKey(jsonObjectFBF, "daysOutOfStockFBF");
            Integer cardbalanceFBF = getIntegerFromJsonKey(jsonObjectFBF, "stockCardBalanceFBF");
            Integer amcvalueFBF = getIntegerFromJsonKey(jsonObjectFBF, "StockCardIssuesFBF");
            String expiriesFBF = jsonObjectFBF.isNull("expiry6MonthsFBF") ? "" : jsonObjectFBF.getString("expiry6MonthsFBF");
            Integer actualcountFBF = getIntegerFromJsonKey(jsonObjectFBF, "physicalStockFBF");
            //Integer cmosoneFBF = (datajson.isNull("currentMOSFBF")) ? empty :datajson.getInt("currentMOSFBF");
            //Integer cmostwoFBF = (datajson.isNull("currentMOS2FBF")) ? empty :datajson.getInt("currentMOS2FBF");
            Integer cmosoneFBF = getIntegerFromJsonKey(datajson, "currentMOSFBF");
            Integer cmostwoFBF = getIntegerFromJsonKey(datajson, "currentMOS2FBF");
            Integer stcardscoreFBF = (datajson.isNull("stockCardScoreFBF")) ? empty : datajson.getInt("stockCardScoreFBF");
            Integer stCardsCountScoreFBF = (datajson.isNull("stockCardsCountScoreFBF")) ? empty : datajson.getInt("stockCardsCountScoreFBF");
            Integer daysofStockScoreFBF = (datajson.isNull("daysOutOfStockScoreFBF")) ? empty : datajson.getInt("daysOutOfStockScoreFBF");
            Integer iDenominatorFBF = (datajson.isNull("itemDenominatorFBF")) ? empty : datajson.getInt("itemDenominatorFBF");
            Integer iNumeratorFBF = (datajson.isNull("itemNumeratorFBF")) ? empty : datajson.getInt("itemNumeratorFBF");

            InventoryManagement fbf = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("FBF").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityFBF).stCountsDone(alCountsFBF).dayOutStock(daysoutStockFBF).cardBalance(cardbalanceFBF).amc(amcvalueFBF)
                    .expires6mo(expiriesFBF).actualCount(actualcountFBF).cmos1(cmosoneFBF).cmos2(cmostwoFBF).stCardScore(stcardscoreFBF)
                    .stCardsCountScore(stCardsCountScoreFBF).daysofStockScore(daysofStockScoreFBF).iDenominator(iDenominatorFBF).iNumerator(iNumeratorFBF).build();

            try {
                inventoryDao.save(fbf);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectRUSF = null;
            if (!datajson.isNull("RUSF")) {
                jsonObjectRUSF = datajson.getJSONObject("RUSF");
            }
            String cardAvailabilityRUSF = jsonObjectRUSF.isNull("sCardAvailableRUSF") ? "" : jsonObjectRUSF.getString("sCardAvailableRUSF");
            Integer alCountsRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "StocksAvailableRUSF");
            Integer daysoutStockRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "daysOutOfStockRUSF");
            Integer cardbalanceRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "stockCardBalanceRUSF");
            Integer amcvalueRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "StockCardIssuesRUSF");
            String expiriesRUSF = jsonObjectRUSF.isNull("expiry6MonthsRUSF") ? "" : jsonObjectRUSF.getString("expiry6MonthsRUSF");
            Integer actualcountRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "physicalStockRUSF");
            //Integer cmosoneRUSF = (datajson.isNull("currentMOSRUSF")) ? empty :datajson.getInt("currentMOSRUSF");
            //Integer cmostwoRUSF = (datajson.isNull("currentMOS2RUSF")) ? empty :datajson.getInt("currentMOS2RUSF");
            Integer cmosoneRUSF = getIntegerFromJsonKey(datajson, "currentMOSRUSF");
            Integer cmostwoRUSF = getIntegerFromJsonKey(datajson, "currentMOS2RUSF");
            Integer stcardscoreRUSF = (datajson.isNull("stockCardScoreRUSF")) ? empty : datajson.getInt("stockCardScoreRUSF");
            Integer stCardsCountScoreRUSF = (datajson.isNull("stockCardsCountScoreRUSF")) ? empty : datajson.getInt("stockCardsCountScoreRUSF");
            Integer daysofStockScoreRUSF = (datajson.isNull("daysOutOfStockScoreRUSF")) ? empty : datajson.getInt("daysOutOfStockScoreRUSF");
            Integer iDenominatorRUSF = (datajson.isNull("itemDenominatorRUSF")) ? empty : datajson.getInt("itemDenominatorRUSF");
            Integer iNumeratorRUSF = (datajson.isNull("itemNumeratorRUSF")) ? empty : datajson.getInt("itemNumeratorRUSF");

            InventoryManagement rusf = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("RUSF").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityRUSF).stCountsDone(alCountsRUSF).dayOutStock(daysoutStockRUSF).cardBalance(cardbalanceRUSF).amc(amcvalueRUSF)
                    .expires6mo(expiriesRUSF).actualCount(actualcountRUSF).cmos1(cmosoneRUSF).cmos2(cmostwoRUSF).stCardScore(stcardscoreRUSF)
                    .stCardsCountScore(stCardsCountScoreRUSF).daysofStockScore(daysofStockScoreRUSF).iDenominator(iDenominatorRUSF).iNumerator(iNumeratorRUSF).build();

            try {
                inventoryDao.save(rusf);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectRUTF = null;
            if (!datajson.isNull("RUTF")) {
                jsonObjectRUTF = datajson.getJSONObject("RUTF");
            }
            String cardAvailabilityRUTF = jsonObjectRUTF.isNull("sCardAvailableRUTF") ? "" : jsonObjectRUTF.getString("sCardAvailableRUTF");
            Integer alCountsRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "StocksAvailableRUTF");
            Integer daysoutStockRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "daysOutOfStockRUTF");
            Integer cardbalanceRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "stockCardBalanceRUTF");
            Integer amcvalueRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "StockCardIssuesRUTF");
            String expiriesRUTF = jsonObjectRUTF.isNull("expiry6MonthsRUTF") ? "" : jsonObjectRUTF.getString("expiry6MonthsRUTF");
            Integer actualcountRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "physicalStockRUTF");
            //Integer cmosoneRUTF = (datajson.isNull("currentMOSRUTF")) ? empty :datajson.getInt("currentMOSRUTF");
            //Integer cmostwoRUTF = (datajson.isNull("currentMOS2RUTF")) ? empty :datajson.getInt("currentMOS2RUTF");
            Integer cmosoneRUTF = getIntegerFromJsonKey(datajson, "currentMOSRUTF");
            Integer cmostwoRUTF = getIntegerFromJsonKey(datajson, "currentMOS2RUTF");
            Integer stcardscoreRUTF = (datajson.isNull("stockCardScoreRUTF")) ? empty : datajson.getInt("stockCardScoreRUTF");
            Integer stCardsCountScoreRUTF = (datajson.isNull("stockCardsCountScoreRUTF")) ? empty : datajson.getInt("stockCardsCountScoreRUTF");
            Integer daysofStockScoreRUTF = (datajson.isNull("daysOutOfStockScoreRUTF")) ? empty : datajson.getInt("daysOutOfStockScoreRUTF");
            Integer iDenominatorRUTF = (datajson.isNull("itemDenominatorRUTF")) ? empty : datajson.getInt("itemDenominatorRUTF");
            Integer iNumeratorRUTF = (datajson.isNull("itemNumeratorRUTF")) ? empty : datajson.getInt("itemNumeratorRUTF");

            InventoryManagement rutf = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("RUTF").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityRUTF).stCountsDone(alCountsRUTF).dayOutStock(daysoutStockRUTF).cardBalance(cardbalanceRUTF).amc(amcvalueRUTF)
                    .expires6mo(expiriesRUTF).actualCount(actualcountRUTF).cmos1(cmosoneRUTF).cmos2(cmostwoRUTF).stCardScore(stcardscoreRUTF)
                    .stCardsCountScore(stCardsCountScoreRUTF).daysofStockScore(daysofStockScoreRUTF).iDenominator(iDenominatorRUTF).iNumerator(iNumeratorRUTF).build();

            try {
                inventoryDao.save(rutf);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectVitaminA = null;
            if (!datajson.isNull("Vitamin")) {
                jsonObjectVitaminA = datajson.getJSONObject("Vitamin");
            }
            String cardAvailabilityVitaminA = jsonObjectVitaminA.isNull("sCardAvailableVitaminA") ? "" : jsonObjectVitaminA.getString("sCardAvailableVitaminA");
            Integer alCountsVitaminA = getIntegerFromJsonKey(jsonObjectVitaminA, "StocksAvailableVitaminA");
            Integer daysoutStockVitaminA = getIntegerFromJsonKey(jsonObjectVitaminA, "daysOutOfStockVitaminA");
            Integer cardbalanceVitaminA = getIntegerFromJsonKey(jsonObjectVitaminA, "stockCardBalanceVitaminA");
            Integer amcvalueVitaminA = getIntegerFromJsonKey(jsonObjectVitaminA, "StockCardIssuesVitaminA");
            String expiriesVitaminA = jsonObjectVitaminA.isNull("expiry6MonthsVitaminA") ? "" : jsonObjectVitaminA.getString("expiry6MonthsVitaminA");
            Integer actualcountVitaminA = getIntegerFromJsonKey(jsonObjectVitaminA, "physicalStockVitaminA");
            //Integer cmosoneVitaminA = (datajson.isNull("currentMOSVitaminA")) ? empty :datajson.getInt("currentMOSVitaminA");
            //Integer cmostwoVitaminA = (datajson.isNull("currentMOS2VitaminA")) ? empty :datajson.getInt("currentMOS2VitaminA");
            Integer cmosoneVitaminA = getIntegerFromJsonKey(datajson, "currentMOSVitaminA");
            Integer cmostwoVitaminA = getIntegerFromJsonKey(datajson, "currentMOS2VitaminA");
            Integer stcardscoreVitaminA = (datajson.isNull("stockCardScoreVitaminA")) ? empty : datajson.getInt("stockCardScoreVitaminA");
            Integer stCardsCountScoreVitaminA = (datajson.isNull("stockCardsCountScoreVitaminA")) ? empty : datajson.getInt("stockCardsCountScoreVitaminA");
            Integer daysofStockScoreVitaminA = (datajson.isNull("daysOutOfStockScoreVitaminA")) ? empty : datajson.getInt("daysOutOfStockScoreVitaminA");
            Integer iDenominatorVitaminA = (datajson.isNull("itemDenominatorVitaminA")) ? empty : datajson.getInt("itemDenominatorVitaminA");
            Integer iNumeratorVitaminA = (datajson.isNull("itemNumeratorVitaminA")) ? empty : datajson.getInt("itemNumeratorVitaminA");

            InventoryManagement vitamin = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Vitamin A").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityVitaminA).stCountsDone(alCountsVitaminA).dayOutStock(daysoutStockVitaminA).cardBalance(cardbalanceVitaminA).amc(amcvalueVitaminA)
                    .expires6mo(expiriesVitaminA).actualCount(actualcountVitaminA).cmos1(cmosoneVitaminA).cmos2(cmostwoVitaminA).stCardScore(stcardscoreVitaminA)
                    .stCardsCountScore(stCardsCountScoreVitaminA).daysofStockScore(daysofStockScoreVitaminA).iDenominator(iDenominatorVitaminA).iNumerator(iNumeratorVitaminA).build();

            try {
                inventoryDao.save(vitamin);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectBCG = null;
            if (!datajson.isNull("BCG")) {
                jsonObjectBCG = datajson.getJSONObject("BCG");
            }
            String cardAvailabilityBCG = jsonObjectBCG.isNull("sCardAvailableBCG") ? "" : jsonObjectBCG.getString("sCardAvailableBCG");
            Integer alCountsBCG = getIntegerFromJsonKey(jsonObjectBCG, "StocksAvailableBCG");
            Integer daysoutStockBCG = getIntegerFromJsonKey(jsonObjectBCG, "daysOutOfStockBCG");
            Integer cardbalanceBCG = getIntegerFromJsonKey(jsonObjectBCG, "stockCardBalanceBCG");
            Integer amcvalueBCG = getIntegerFromJsonKey(jsonObjectBCG, "StockCardIssuesBCG");
            String expiriesBCG = jsonObjectBCG.isNull("expiry6MonthsBCG") ? "" : jsonObjectBCG.getString("expiry6MonthsBCG");
            Integer actualcountBCG = getIntegerFromJsonKey(jsonObjectBCG, "physicalStockBCG");
            //Integer cmosoneBCG = (datajson.isNull("currentMOSBCG")) ? empty :datajson.getInt("currentMOSBCG");
            //Integer cmostwoBCG = (datajson.isNull("currentMOS2BCG")) ? empty :datajson.getInt("currentMOS2BCG");
            Integer cmosoneBCG = getIntegerFromJsonKey(datajson, "currentMOSBCG");
            Integer cmostwoBCG = getIntegerFromJsonKey(datajson, "currentMOS2BCG");
            Integer stcardscoreBCG = (datajson.isNull("stockCardScoreBCG")) ? empty : datajson.getInt("stockCardScoreBCG");
            Integer stCardsCountScoreBCG = (datajson.isNull("stockCardsCountScoreBCG")) ? empty : datajson.getInt("stockCardsCountScoreBCG");
            Integer daysofStockScoreBCG = (datajson.isNull("daysOutOfStockScoreBCG")) ? empty : datajson.getInt("daysOutOfStockScoreBCG");
            Integer iDenominatorBCG = (datajson.isNull("itemDenominatorBCG")) ? empty : datajson.getInt("itemDenominatorBCG");
            Integer iNumeratorBCG = (datajson.isNull("itemNumeratorBCG")) ? empty : datajson.getInt("itemNumeratorBCG");

            InventoryManagement bcg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("BCG").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityBCG).stCountsDone(alCountsBCG).dayOutStock(daysoutStockBCG).cardBalance(cardbalanceBCG).amc(amcvalueBCG)
                    .expires6mo(expiriesBCG).actualCount(actualcountBCG).cmos1(cmosoneBCG).cmos2(cmostwoBCG).stCardScore(stcardscoreBCG)
                    .stCardsCountScore(stCardsCountScoreBCG).daysofStockScore(daysofStockScoreBCG).iDenominator(iDenominatorBCG).iNumerator(iNumeratorBCG).build();

            try {
                inventoryDao.save(bcg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectPenta = null;
            if (!datajson.isNull("Penta")) {
                jsonObjectPenta = datajson.getJSONObject("Penta");
            }
            String cardAvailabilityPenta = jsonObjectPenta.isNull("sCardAvailablePenta") ? "" : jsonObjectPenta.getString("sCardAvailablePenta");
            Integer alCountsPenta = getIntegerFromJsonKey(jsonObjectPenta, "StocksAvailablePenta");
            Integer daysoutStockPenta = getIntegerFromJsonKey(jsonObjectPenta, "daysOutOfStockPenta");
            Integer cardbalancePenta = getIntegerFromJsonKey(jsonObjectPenta, "stockCardBalancePenta");
            Integer amcvaluePenta = getIntegerFromJsonKey(jsonObjectPenta, "StockCardIssuesPenta");
            String expiriesPenta = jsonObjectPenta.isNull("expiry6MonthsPenta") ? "" : jsonObjectPenta.getString("expiry6MonthsPenta");
            Integer actualcountPenta = getIntegerFromJsonKey(jsonObjectPenta, "physicalStockPenta");
            //Integer cmosonePenta = (datajson.isNull("currentMOSPenta")) ? empty :datajson.getInt("currentMOSPenta");
            //Integer cmostwoPenta = (datajson.isNull("currentMOS2Penta")) ? empty :datajson.getInt("currentMOS2Penta");
            Integer cmosonePenta = getIntegerFromJsonKey(datajson, "currentMOSPenta");
            Integer cmostwoPenta = getIntegerFromJsonKey(datajson, "currentMOS2Penta");
            Integer stcardscorePenta = (datajson.isNull("stockCardScorePenta")) ? empty : datajson.getInt("stockCardScorePenta");
            Integer stCardsCountScorePenta = (datajson.isNull("stockCardsCountScorePenta")) ? empty : datajson.getInt("stockCardsCountScorePenta");
            Integer daysofStockScorePenta = (datajson.isNull("daysOutOfStockScorePenta")) ? empty : datajson.getInt("daysOutOfStockScorePenta");
            Integer iDenominatorPenta = (datajson.isNull("itemDenominatorPenta")) ? empty : datajson.getInt("itemDenominatorPenta");
            Integer iNumeratorPenta = (datajson.isNull("itemNumeratorPenta")) ? empty : datajson.getInt("itemNumeratorPenta");

            InventoryManagement penta = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Penta").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityPenta).stCountsDone(alCountsPenta).dayOutStock(daysoutStockPenta).cardBalance(cardbalancePenta).amc(amcvaluePenta)
                    .expires6mo(expiriesPenta).actualCount(actualcountPenta).cmos1(cmosonePenta).cmos2(cmostwoPenta).stCardScore(stcardscorePenta)
                    .stCardsCountScore(stCardsCountScorePenta).daysofStockScore(daysofStockScorePenta).iDenominator(iDenominatorPenta).iNumerator(iNumeratorPenta).build();

            try {
                inventoryDao.save(penta);
            } catch (Exception e) {
                log.info("Duplication");
            }

            JSONObject jsonObjectPolio = null;
            if (!datajson.isNull("Polio")) {
                jsonObjectPolio = datajson.getJSONObject("Polio");
            }
            String cardAvailabilityPolio = jsonObjectPolio.isNull("sCardAvailablePolio") ? "" : jsonObjectPolio.getString("sCardAvailablePolio");
            Integer alCountsPolio = getIntegerFromJsonKey(jsonObjectPolio, "StocksAvailablePolio");
            Integer daysoutStockPolio = getIntegerFromJsonKey(jsonObjectPolio, "daysOutOfStockPolio");
            Integer cardbalancePolio = getIntegerFromJsonKey(jsonObjectPolio, "stockCardBalancePolio");
            Integer amcvaluePolio = getIntegerFromJsonKey(jsonObjectPolio, "StockCardIssuesPolio");
            String expiriesPolio = jsonObjectPolio.isNull("expiry6MonthsPolio") ? "" : jsonObjectPolio.getString("expiry6MonthsPolio");
            Integer actualcountPolio = getIntegerFromJsonKey(jsonObjectPolio, "physicalStockPolio");
            //Integer cmosonePolio = (datajson.isNull("currentMOSPolio")) ? empty :datajson.getInt("currentMOSPolio");
            //Integer cmostwoPolio = (datajson.isNull("currentMOS2Polio")) ? empty :datajson.getInt("currentMOS2Polio");
            Integer cmosonePolio = getIntegerFromJsonKey(datajson, "currentMOSPolio");
            Integer cmostwoPolio = getIntegerFromJsonKey(datajson, "currentMOS2Polio");
            Integer stcardscorePolio = (datajson.isNull("stockCardScorePolio")) ? empty : datajson.getInt("stockCardScorePolio");
            Integer stCardsCountScorePolio = (datajson.isNull("stockCardsCountScorePolio")) ? empty : datajson.getInt("stockCardsCountScorePolio");
            Integer daysofStockScorePolio = (datajson.isNull("daysOutOfStockScorePolio")) ? empty : datajson.getInt("daysOutOfStockScorePolio");
            Integer iDenominatorPolio = (datajson.isNull("itemDenominatorPolio")) ? empty : datajson.getInt("itemDenominatorPolio");
            Integer iNumeratorPolio = (datajson.isNull("itemNumeratorPolio")) ? empty : datajson.getInt("itemNumeratorPolio");

            InventoryManagement polio = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Polio (IPV)").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityPolio).stCountsDone(alCountsPolio).dayOutStock(daysoutStockPolio).cardBalance(cardbalancePolio).amc(amcvaluePolio)
                    .expires6mo(expiriesPolio).actualCount(actualcountPolio).cmos1(cmosonePolio).cmos2(cmostwoPolio).stCardScore(stcardscorePolio)
                    .stCardsCountScore(stCardsCountScorePolio).daysofStockScore(daysofStockScorePolio).iDenominator(iDenominatorPolio).iNumerator(iNumeratorPolio).build();

            try {
                inventoryDao.save(polio);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            JSONObject jsonObjectdtg1030s = null;
            if (!datajson.isNull("dtg1030s")) {
                jsonObjectdtg1030s = datajson.getJSONObject("dtg1030s");
            }
            String cardAvailabilitydtg1030s = jsonObjectdtg1030s.isNull("sCardAvailabledtg1030s") ? "" : jsonObjectdtg1030s.getString("sCardAvailabledtg1030s");
            Integer alCountsdtg1030s = getIntegerFromJsonKey(jsonObjectdtg1030s, "StocksAvailabledtg1030s");
            Integer daysoutStockdtg1030s = getIntegerFromJsonKey(jsonObjectdtg1030s, "daysOutOfStockdtg1030s");
            Integer cardbalancedtg1030s = getIntegerFromJsonKey(jsonObjectdtg1030s, "stockCardBalancedtg1030s");
            Integer amcvaluedtg1030s = getIntegerFromJsonKey(jsonObjectdtg1030s, "StockCardIssuesdtg1030s");
            String expiriesdtg1030s = jsonObjectdtg1030s.isNull("expiry6Monthsdtg1030s") ? "" : jsonObjectdtg1030s.getString("expiry6Monthsdtg1030s");
            Integer actualcountdtg1030s = getIntegerFromJsonKey(jsonObjectdtg1030s, "physicalStockdtg1030s");
            //Integer cmosonedtg1030s = (datajson.isNull("currentMOSdtg1030s")) ? empty :datajson.getInt("currentMOSdtg1030s");
            //Integer cmostwodtg1030s = (datajson.isNull("currentMOS2dtg1030s")) ? empty :datajson.getInt("currentMOS2dtg1030s");
            Integer cmosonedtg1030s = getIntegerFromJsonKey(datajson, "currentMOSdtg1030s");
            Integer cmostwodtg1030s = getIntegerFromJsonKey(datajson, "currentMOS2dtg1030s");
            Integer stcardscoredtg1030s = (datajson.isNull("stockCardScoredtg1030s")) ? empty : datajson.getInt("stockCardScoredtg1030s");
            Integer stCardsCountScoredtg1030s = (datajson.isNull("stockCardsCountScoredtg1030s")) ? empty : datajson.getInt("stockCardsCountScoredtg1030s");
            Integer daysofStockScoredtg1030s = (datajson.isNull("daysOutOfStockScoredtg1030s")) ? empty : datajson.getInt("daysOutOfStockScoredtg1030s");
            Integer iDenominatordtg1030s = (datajson.isNull("itemDenominatordtg1030s")) ? empty : datajson.getInt("itemDenominatordtg1030s");
            Integer iNumeratordtg1030s = (datajson.isNull("itemNumeratordtg1030s")) ? empty : datajson.getInt("itemNumeratordtg1030s");

            InventoryManagement dtg1030s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("DTG 10mg, 30s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitydtg1030s).stCountsDone(alCountsdtg1030s).dayOutStock(daysoutStockdtg1030s).cardBalance(cardbalancedtg1030s).amc(amcvaluedtg1030s)
                    .expires6mo(expiriesdtg1030s).actualCount(actualcountdtg1030s).cmos1(cmosonedtg1030s).cmos2(cmostwodtg1030s).stCardScore(stcardscoredtg1030s)
                    .stCardsCountScore(stCardsCountScoredtg1030s).daysofStockScore(daysofStockScoredtg1030s).iDenominator(iDenominatordtg1030s).iNumerator(iNumeratordtg1030s).build();

            try {
                inventoryDao.save(dtg1030s);
            } catch (Exception e) {
                log.info("Duplication");
            }


        
        
        
        
        
        
        
        
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

}
