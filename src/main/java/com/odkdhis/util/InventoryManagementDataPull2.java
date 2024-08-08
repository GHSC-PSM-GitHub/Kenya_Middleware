/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.InventoryManagementDaoImpl;
import com.odkdhis.model.dashboard.InventoryManagement;
import static com.odkdhis.util.CommonUtil.INDENTATION;
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
public class InventoryManagementDataPull2 extends CommonUtil {

    @Autowired
    private InventoryManagementDaoImpl inventoryDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
   @Scheduled(cron ="20 * * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionImventoryCombined() {
        try {
            //basicCommodityVerificationData("Verification_of_commodity_data");
            inventoryManagementCombined("Inventory_Management_combined");
            //Thread.sleep(2000);

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void inventoryManagementCombined(String formId) throws Exception {
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
            if (inventorymgtDao.getRecordByInstanceId(instanceId) != null) {
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
            JSONObject jsonObject2rod = null;
            if (!datajson.isNull("tworod")) {
                jsonObject2rod = datajson.getJSONObject("tworod");
            } 
            JSONObject jsonObjectAmoxil = null;
            if (!datajson.isNull("ADT")) {
                jsonObjectAmoxil = datajson.getJSONObject("ADT");
            }  
            JSONObject jsonObjectOxytocin = null;
            if (!datajson.isNull("oxytocin")) {
                jsonObjectOxytocin = datajson.getJSONObject("oxytocin");
            }

            JSONObject jsonObjectdtg = null;
            if (!datajson.isNull("dtg")) {
                jsonObjectdtg = datajson.getJSONObject("dtg");
            }
            JSONObject jsonObjectdtg10 = null;
            if (!datajson.isNull("dtg10")) {
                jsonObjectdtg10 = datajson.getJSONObject("dtg10");
            }
            JSONObject jsonObjectftc300 = null;
            if (!datajson.isNull("ftc300")) {
                jsonObjectftc300 = datajson.getJSONObject("ftc300");
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
            JSONObject jsonObjectBenzyl = null;
            if (!datajson.isNull("benzyl")) {
                jsonObjectBenzyl = datajson.getJSONObject("benzyl");
            }
            JSONObject jsonObjectgenta = null;
            if (!datajson.isNull("genta")) {
                jsonObjectgenta = datajson.getJSONObject("genta");
            }
            JSONObject jsonObjectmmr = null;
            if (!datajson.isNull("mmr")) {
                jsonObjectmmr = datajson.getJSONObject("mmr");
            }
            JSONObject jsonObjectexgloves = null;
            if (!datajson.isNull("exgloves")) {
                jsonObjectexgloves = datajson.getJSONObject("exgloves");
            }
            JSONObject jsonObjectRUSF = null;
            if (!datajson.isNull("RUSF")) {
                jsonObjectRUSF = datajson.getJSONObject("RUSF");
            }
            JSONObject jsonObjectRUTF = null;
            if (!datajson.isNull("RUTF")) {
                jsonObjectRUTF = datajson.getJSONObject("RUTF");
            }            
            JSONObject jsonObjectIFAS = null;
            if (!datajson.isNull("IFAS")) {
                jsonObjectIFAS = datajson.getJSONObject("IFAS");
            }            
   
            Integer empty = 0;

            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");
            String cardAvailability = jsonObjectAl6s.isNull("sCardAvailableAL6s") ? "" : jsonObjectAl6s.getString("sCardAvailableAL6s");
            
            String stockstatus = jsonObjectAl6s.isNull("al6status") ? "" : jsonObjectAl6s.getString("al6status");
            
            Integer alCounts = getIntegerFromJsonKey(jsonObjectAl6s, "StocksAvailableAL6s");
            Integer cardbalance = getIntegerFromJsonKey(jsonObjectAl6s, "stockCardBalanceAL6s");
            Integer amcvalue = getIntegerFromJsonKey(jsonObjectAl6s, "StockCardIssuesAL6s");
            Integer daysoutstock = getIntegerFromJsonKey(jsonObjectAl6s, "daysOutOfStockAL6s");
            
            //Integer expiries = getIntegerFromJsonKey(jsonObjectAl6s, "expiry6MonthsAL6s");
            //String expiries = jsonObjectAl6s.isNull("expiry6MonthsAL6s") ? "" : jsonObjectAl6s.getString("expiry6MonthsAL6s");
            Integer actualcount = getIntegerFromJsonKey(jsonObjectAl6s, "physicalStockAL6s");
            Integer cmosone = getIntegerFromJsonKey(datajson, "currentMOSAL6s");
            Integer cmostwo = getIntegerFromJsonKey(datajson, "currentMOS2AL6s");
            Integer stcardscore = (datajson.isNull("stockCardScoreAL6s")) ? empty : datajson.getInt("stockCardScoreAL6s");
            Integer stCardsCountScore = (datajson.isNull("stockCardsCountScoreAL6s")) ? empty : datajson.getInt("stockCardsCountScoreAL6s");
            Integer iDenominator = (datajson.isNull("itemDenominatorAL6s")) ? empty : datajson.getInt("itemDenominatorAL6s");
            Integer iNumerator = (datajson.isNull("itemNumeratorAL6s")) ? empty : datajson.getInt("itemNumeratorAL6s");
            
            Integer numerator = (datajson.isNull("totalNumerator")) ? empty : datajson.getInt("totalNumerator");
            Integer denominator = (datajson.isNull("totalDenominator")) ? empty : datajson.getInt("totalDenominator");
            Integer score = (datajson.isNull("finaScore")) ? empty : datajson.getInt("finaScore");

            InventoryManagement al6s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 6s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailability).stCountsDone(alCounts).dayOutStock(daysoutstock).cardBalance(cardbalance).amc(amcvalue)
                    .actualCount(actualcount).cmos1(cmosone).cmos2(cmostwo).stCardScore(stcardscore)
                    .iDenominator(iDenominator).iNumerator(iNumerator)
                    .stCardsCountScore(stCardsCountScore).iDenominator(0).iNumerator(0)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatus)
                    .build();
            try {
                inventoryDao.save(al6s);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("Duplication");
            } 

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al6s);

            String county1 = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty1 = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility1 = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");
            
            String cardAvailabilityAL24s = jsonObjectAl24s.isNull("sCardAvailableAL24s") ? "" : jsonObjectAl24s.getString("sCardAvailableAL24s");
           
            String stockstatusAL24s = jsonObjectAl24s.isNull("al24status") ? "" : jsonObjectAl24s.getString("al24status");
            
            Integer alCountsAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "StocksAvailableAL24s");
            Integer cardbalanceAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "stockCardBalanceAL24s");
            Integer amcvalueAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "StockCardIssuesAL24s");
            Integer daysoutstockAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "daysOutOfStockAL24s");
            //String expiriesAL24s = jsonObjectAl24s.isNull("expiry6MonthsAL24s") ? "" : jsonObjectAl24s.getString("expiry6MonthsAL24s");
            Integer actualcountAL24s = getIntegerFromJsonKey(jsonObjectAl24s, "physicalStockAL24s");
            Integer cmosoneAL24s = getIntegerFromJsonKey(datajson, "currentMOSAL24s");
            Integer cmostwoAL24s = getIntegerFromJsonKey(datajson, "currentMOS2AL24s");
            Integer stcardscoreAL24s = (datajson.isNull("stockCardScoreAL24s")) ? empty : datajson.getInt("stockCardScoreAL24s");
            Integer stCardsCountScoreAL24s = (datajson.isNull("stockCardsCountScoreAL24s")) ? empty : datajson.getInt("stockCardsCountScoreAL24s");
            Integer iDenominatorAL24s = (datajson.isNull("itemDenominatorAL24s")) ? empty : datajson.getInt("itemDenominatorAL24s");
            Integer iNumeratorAL24s = (datajson.isNull("itemNumeratorAL24s")) ? empty : datajson.getInt("itemNumeratorAL24s");
            
            InventoryManagement AL24s = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 24s").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityAL24s).stCountsDone(alCountsAL24s).dayOutStock(daysoutstockAL24s).cardBalance(cardbalanceAL24s).amc(amcvalueAL24s)
                    .actualCount(actualcountAL24s).cmos1(cmosoneAL24s).cmos2(cmostwoAL24s).stCardScore(stcardscoreAL24s)
                    .stCardsCountScore(stCardsCountScoreAL24s).daysofStockScore(0).iDenominator(iDenominatorAL24s).iNumerator(iNumeratorAL24s)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusAL24s)
                    .build();

            try {
                inventoryDao.save(AL24s);
            } catch (Exception e) {
                log.info("Duplication");
           }

            
            String cardAvailabilityartInj = jsonObjectartesunate.isNull("sCardAvailableartInj") ? "" : jsonObjectartesunate.getString("sCardAvailableartInj");
           
            String stockstatusartInj = jsonObjectartesunate.isNull("atstatus") ? "" : jsonObjectartesunate.getString("atstatus");
            
            Integer alCountsartInj = getIntegerFromJsonKey(jsonObjectartesunate, "StocksAvailableartInj");

            Integer cardbalanceartInj = getIntegerFromJsonKey(jsonObjectartesunate, "stockCardBalanceartInj");
            Integer amcvalueartInj = getIntegerFromJsonKey(jsonObjectartesunate, "StockCardIssuesartInj");
            Integer daysourstockartInj = getIntegerFromJsonKey(jsonObjectartesunate, "daysOutOfStockartInj");
            //String expiriesartInj = jsonObjectartesunate.isNull("expiry6MonthsartInj") ? "" : jsonObjectartesunate.getString("expiry6MonthsartInj");
            Integer actualcountartInj = getIntegerFromJsonKey(jsonObjectartesunate, "physicalStockartInj");
            Integer cmosoneartInj = getIntegerFromJsonKey(datajson, "currentMOSartInj");
            Integer cmostwoartInj = getIntegerFromJsonKey(datajson, "currentMOS2artInj");
            Integer stcardscoreartInj = (datajson.isNull("stockCardScoreartInj")) ? empty : datajson.getInt("stockCardScoreartInj");
            Integer stCardsCountScoreartInj = (datajson.isNull("stockCardsCountScoreartInj")) ? empty : datajson.getInt("stockCardsCountScoreartInj");
            Integer iDenominatorartInj = (datajson.isNull("itemDenominatorartInj")) ? empty : datajson.getInt("itemDenominatorartInj");
            Integer iNumeratorartInj = (datajson.isNull("itemNumeratorartInj")) ? empty : datajson.getInt("itemNumeratorartInj");
            
            InventoryManagement artInj = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("artesunate inj").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityartInj).stCountsDone(alCountsartInj).dayOutStock(daysourstockartInj).cardBalance(cardbalanceartInj).amc(amcvalueartInj)
                    .actualCount(actualcountartInj).cmos1(cmosoneartInj).cmos2(cmostwoartInj).stCardScore(stcardscoreartInj)
                    .stCardsCountScore(stCardsCountScoreartInj).daysofStockScore(0).iDenominator(iDenominatorartInj).iNumerator(iNumeratorartInj)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusartInj)
                    .build();

            try {
                inventoryDao.save(artInj);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            
            String cardAvailabilityRDTs = jsonObjectmalaria.isNull("sCardAvailableRDTs") ? "" : jsonObjectmalaria.getString("sCardAvailableRDTs");
            
            String stockstatusRDTs = jsonObjectmalaria.isNull("rdtstatus") ? "" : jsonObjectmalaria.getString("rdtstatus");
            
            Integer alCountsRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "StocksAvailableRDTs");
            Integer daysoutStockRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "daysOutOfStockRDTs");
            Integer cardbalanceRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "stockCardBalanceRDTs");
            Integer amcvalueRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "StockCardIssuesRDTs");
            
            //String expiriesRDTs = jsonObjectmalaria.isNull("expiry6MonthsRDTs") ? "" : jsonObjectmalaria.getString("expiry6MonthsRDTs");
            Integer actualcountRDTs = getIntegerFromJsonKey(jsonObjectmalaria, "physicalStockRDTs");
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
                    .actualCount(actualcountRDTs).cmos1(cmosoneRDTs).cmos2(cmostwoRDTs).stCardScore(stcardscoreRDTs)
                    .stCardsCountScore(stCardsCountScoreRDTs).daysofStockScore(daysofStockScoreRDTs).iDenominator(iDenominatorRDTs).iNumerator(iNumeratorRDTs)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusRDTs)
                    .build();

            try {
                inventoryDao.save(rdts);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            
            String cardAvailabilityLLINs = jsonObjectllins.isNull("sCardAvailableLLINs") ? "" : jsonObjectllins.getString("sCardAvailableLLINs");
            
            String stockstatusLLINs = jsonObjectllins.isNull("llinstatus") ? "" : jsonObjectllins.getString("llinstatus");
            
            Integer alCountsLLINs = getIntegerFromJsonKey(jsonObjectllins, "StocksAvailableLLINs");
            Integer daysoutStockLLINs = getIntegerFromJsonKey(jsonObjectllins, "daysOutOfStockLLINs");
            Integer cardbalanceLLINs = getIntegerFromJsonKey(jsonObjectllins, "stockCardBalanceLLINs");
            Integer amcvalueLLINs = getIntegerFromJsonKey(jsonObjectllins, "StockCardIssuesLLINs");
            //String expiriesLLINs = jsonObjectllins.isNull("expiry6MonthsLLINs") ? "" : jsonObjectllins.getString("expiry6MonthsLLINs");
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
                    .actualCount(actualcountLLINs).cmos1(cmosoneLLINs).cmos2(cmostwoLLINs).stCardScore(stcardscoreLLINs)
                    .stCardsCountScore(stCardsCountScoreLLINs).daysofStockScore(daysofStockScoreLLINs).iDenominator(iDenominatorLLINs).iNumerator(iNumeratorLLINs)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusLLINs)
                    .build();

            try {
                inventoryDao.save(llins);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
           
            String cardAvailabilityDMPA = jsonObjectdmpa.isNull("sCardAvailableDMPA") ? "" : jsonObjectdmpa.getString("sCardAvailableDMPA");
            
            String stockstatusDMPA = jsonObjectdmpa.isNull("imstatus") ? "" : jsonObjectdmpa.getString("imstatus");
            
            Integer alCountsDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "StocksAvailableDMPA");
            Integer cardbalanceDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "stockCardBalanceDMPA");
            Integer amcvalueDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "StockCardIssuesDMPA");
            Integer daysoutstockDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "daysOutOfStockDMPA");
            //String expiriesDMPA = jsonObjectdmpa.isNull("expiry6MonthsDMPA") ? "" : jsonObjectdmpa.getString("expiry6MonthsDMPA");
            Integer actualcountDMPA = getIntegerFromJsonKey(jsonObjectdmpa, "physicalStockDMPA");
            Integer cmosoneDMPA = getIntegerFromJsonKey(datajson, "currentMOSDMPA");
            Integer cmostwoDMPA = getIntegerFromJsonKey(datajson, "currentMOS2DMPA");
            Integer stcardscoreDMPA = (datajson.isNull("stockCardScoreDMPA")) ? empty : datajson.getInt("stockCardScoreDMPA");
            Integer stCardsCountScoreDMPA = (datajson.isNull("stockCardsCountScoreDMPA")) ? empty : datajson.getInt("stockCardsCountScoreDMPA");
            Integer iDenominatorDMPA = (datajson.isNull("itemDenominatorDMPA")) ? empty : datajson.getInt("itemDenominatorDMPA");
            Integer iNumeratorDMPA = (datajson.isNull("itemNumeratorDMPA")) ? empty : datajson.getInt("itemNumeratorDMPA");
            
            InventoryManagement dmpa = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("DMPA IM").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityDMPA).stCountsDone(alCountsDMPA).dayOutStock(daysoutstockDMPA).cardBalance(cardbalanceDMPA).amc(amcvalueDMPA)
                    .actualCount(actualcountDMPA).cmos1(cmosoneDMPA).cmos2(cmostwoDMPA).stCardScore(stcardscoreDMPA)
                    .stCardsCountScore(stCardsCountScoreDMPA).daysofStockScore(0).iDenominator(iDenominatorDMPA).iNumerator(iNumeratorDMPA)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusDMPA)
                    .build();

            try {
                inventoryDao.save(dmpa);
            } catch (Exception e) {
                log.info("Duplication");
            }
               

            String cardAvailability1rod = jsonObjectimplant1.isNull("sCardAvailable1rod") ? "" : jsonObjectimplant1.getString("sCardAvailable1rod");
            
            String stockstatus1rod = jsonObjectimplant1.isNull("impstatus") ? "" : jsonObjectimplant1.getString("impstatus");
            
            Integer alCounts1rod = getIntegerFromJsonKey(jsonObjectimplant1, "StocksAvailable1rod");
            Integer cardbalance1rod = getIntegerFromJsonKey(jsonObjectimplant1, "stockCardBalance1rod");
            Integer amcvalue1rod = getIntegerFromJsonKey(jsonObjectimplant1, "StockCardIssues1rod");
            Integer daysoutstock1rod = getIntegerFromJsonKey(jsonObjectimplant1, "daysOutOfStock1rod");
            //String expiries1rod = jsonObjectimplant1.isNull("expiry6Months1rod") ? "" : jsonObjectimplant1.getString("expiry6Months1rod");
            Integer actualcount1rod = getIntegerFromJsonKey(jsonObjectimplant1, "physicalStock1rod");
            Integer cmosone1rod = getIntegerFromJsonKey(datajson, "currentMOS1rod");
            Integer cmostwo1rod = getIntegerFromJsonKey(datajson, "currentMOS21rod");
            Integer stcardscore1rod = (datajson.isNull("stockCardScore1rod")) ? empty : datajson.getInt("stockCardScore1rod");
            Integer stCardsCountScore1rod = (datajson.isNull("stockCardsCountScore1rod")) ? empty : datajson.getInt("stockCardsCountScore1rod");
            Integer iDenominator1rod = (datajson.isNull("itemDenominator1rod")) ? empty : datajson.getInt("itemDenominator1rod");
            Integer iNumerator1rod = (datajson.isNull("itemNumerator1rod")) ? empty : datajson.getInt("itemNumerator1rod");
            
            InventoryManagement rod1 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("1 rod implant").orgUnit(orgUnit)
                    .stCardAvail(cardAvailability1rod).stCountsDone(alCounts1rod).dayOutStock(daysoutstock1rod).cardBalance(cardbalance1rod).amc(amcvalue1rod)
                    .actualCount(actualcount1rod).cmos1(cmosone1rod).cmos2(cmostwo1rod).stCardScore(stcardscore1rod)
                    .stCardsCountScore(stCardsCountScore1rod).daysofStockScore(0).iDenominator(iDenominator1rod).iNumerator(iNumerator1rod)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatus1rod)
                    .build();

            try {
                inventoryDao.save(rod1);
            } catch (Exception e) {
                log.info("Duplication");
            }

            
            String cardAvailability2rod = jsonObject2rod.isNull("sCardAvailable2rod") ? "" : jsonObject2rod.getString("sCardAvailable2rod");
            
            String stockstatus2rod = jsonObject2rod.isNull("twstatus") ? "" : jsonObject2rod.getString("twstatus");
            
            Integer alCounts2rod = getIntegerFromJsonKey(jsonObject2rod, "StocksAvailable2rod");
            Integer cardbalance2rod = getIntegerFromJsonKey(jsonObject2rod, "stockCardBalance2rod");
            Integer amcvalue2rod = getIntegerFromJsonKey(jsonObject2rod, "StockCardIssues2rod");
            Integer daysoutstock2rod = getIntegerFromJsonKey(jsonObject2rod, "StocksAvailable2rod");
            //String expiries2rod = jsonObject2rod.isNull("expiry6Months2rod") ? "" : jsonObject2rod.getString("expiry6Months2rod");
            Integer actualcount2rod = getIntegerFromJsonKey(jsonObject2rod, "physicalStock2rod");
            Integer cmosone2rod = getIntegerFromJsonKey(datajson, "currentMOS2rod");
            Integer cmostwo2rod = getIntegerFromJsonKey(datajson, "currentMOS22rod");
            Integer stcardscore2rod = (datajson.isNull("stockCardScore2rod")) ? empty : datajson.getInt("stockCardScore2rod");
            Integer stCardsCountScore2rod = (datajson.isNull("stockCardsCountScore2rod")) ? empty : datajson.getInt("stockCardsCountScore2rod");
            Integer iDenominator2rod = (datajson.isNull("itemDenominator2rod")) ? empty : datajson.getInt("itemDenominator2rod");
            Integer iNumerator2rod = (datajson.isNull("itemNumerator2rod")) ? empty : datajson.getInt("itemNumerator2rod");

            InventoryManagement rod2 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("2 rod implant").orgUnit(orgUnit)
                    .stCardAvail(cardAvailability2rod).stCountsDone(alCounts2rod).dayOutStock(daysoutstock2rod).cardBalance(cardbalance2rod).amc(amcvalue2rod)
                    .actualCount(actualcount2rod).cmos1(cmosone2rod).cmos2(cmostwo2rod).stCardScore(stcardscore2rod)
                    .stCardsCountScore(stCardsCountScore2rod).daysofStockScore(0).iDenominator(iDenominator2rod).iNumerator(iNumerator2rod)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatus2rod)
                    .build();

            try {
                inventoryDao.save(rod2);
            } catch (Exception e) {
                log.info("Duplication");
            }

            
            String cardAvailabilityADT = jsonObjectAmoxil.isNull("sCardAvailableADT") ? "" : jsonObjectAmoxil.getString("sCardAvailableADT");
            
            String stockstatusADT = jsonObjectAmoxil.isNull("adtstatus") ? "" : jsonObjectAmoxil.getString("adtstatus");
            
            
            Integer alCountsADT = getIntegerFromJsonKey(jsonObjectAmoxil, "StocksAvailableADT");
            Integer cardbalanceADT = getIntegerFromJsonKey(jsonObjectAmoxil, "stockCardBalanceADT");
            Integer amcvalueADT = getIntegerFromJsonKey(jsonObjectAmoxil, "StockCardIssuesADT");
            Integer daysoutstockADT = getIntegerFromJsonKey(jsonObjectAmoxil, "daysOutOfStockADT");
            //String expiriesADT = jsonObjectAmoxil.isNull("expiry6MonthsADT") ? "" : jsonObjectAmoxil.getString("expiry6MonthsADT");
            Integer actualcountADT = getIntegerFromJsonKey(jsonObjectAmoxil, "physicalStockADT");
            Integer cmosoneADT = getIntegerFromJsonKey(datajson, "currentMOSADT");
            Integer cmostwoADT = getIntegerFromJsonKey(datajson, "currentMOS2ADT");
            Integer stcardscoreADT = (datajson.isNull("stockCardScoreADT")) ? empty : datajson.getInt("stockCardScoreADT");
            Integer stCardsCountScoreADT = (datajson.isNull("stockCardsCountScoreADT")) ? empty : datajson.getInt("stockCardsCountScoreADT");
            Integer iDenominatorADT = (datajson.isNull("itemDenominatorADT")) ? empty : datajson.getInt("itemDenominatorADT");
            Integer iNumeratorADT = (datajson.isNull("itemNumeratorADT")) ? empty : datajson.getInt("itemNumeratorADT");
            
            InventoryManagement adt = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Amoxicillin Dispersible Tablets 250mg").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityADT).stCountsDone(alCountsADT).dayOutStock(daysoutstockADT).cardBalance(cardbalanceADT).amc(amcvalueADT)
                    .actualCount(actualcountADT).cmos1(cmosoneADT).cmos2(cmostwoADT).stCardScore(stcardscoreADT)
                    .stCardsCountScore(stCardsCountScoreADT).daysofStockScore(0).iDenominator(iDenominatorADT).iNumerator(iNumeratorADT)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusADT)
                    .build();

            try {
                inventoryDao.save(adt);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
     
            
            String cardAvailabilityOxytocin = jsonObjectOxytocin.isNull("sCardAvailableOxytocin") ? "" : jsonObjectOxytocin.getString("sCardAvailableOxytocin");
            
            String stockstatusOxytocin = jsonObjectOxytocin.isNull("oxystatus") ? "" : jsonObjectOxytocin.getString("oxystatus");
            
            Integer alCountsOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "StocksAvailableOxytocin");
            Integer cardbalanceOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "stockCardBalanceOxytocin");
            Integer amcvalueOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "StockCardIssuesOxytocin");
            Integer daysoutstockOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "daysOutOfStockOxytocin");
            //String expiriesOxytocin = jsonObjectOxytocin.isNull("expiry6MonthsOxytocin") ? "" : jsonObjectOxytocin.getString("expiry6MonthsOxytocin");
            Integer actualcountOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "physicalStockOxytocin");
            Integer cmosoneOxytocin = getIntegerFromJsonKey(datajson, "currentMOSOxytocin");
            Integer cmostwoOxytocin = getIntegerFromJsonKey(datajson, "currentMOS2Oxytocin");
            Integer stcardscoreOxytocin = (datajson.isNull("stockCardScoreOxytocin")) ? empty : datajson.getInt("stockCardScoreOxytocin");
            Integer stCardsCountScoreOxytocin = (datajson.isNull("stockCardsCountScoreOxytocin")) ? empty : datajson.getInt("stockCardsCountScoreOxytocin");           Integer iDenominatorOxytocin = (datajson.isNull("itemDenominatorOxytocin")) ? empty : datajson.getInt("itemDenominatorOxytocin");
            Integer iNumeratorOxytocin = (datajson.isNull("itemNumeratorOxytocin")) ? empty : datajson.getInt("itemNumeratorOxytocin");

            
            InventoryManagement Oxytocin = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Oxytocin").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityOxytocin).stCountsDone(alCountsOxytocin).dayOutStock(daysoutstockOxytocin).cardBalance(cardbalanceOxytocin).amc(amcvalueOxytocin)
                    .actualCount(actualcountOxytocin).cmos1(cmosoneOxytocin).cmos2(cmostwoOxytocin).stCardScore(stcardscoreOxytocin)
                    .stCardsCountScore(stCardsCountScoreOxytocin).daysofStockScore(0).iDenominator(iDenominatorOxytocin).iNumerator(iNumeratorOxytocin)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusOxytocin)
                    .build();

            try {
                inventoryDao.save(Oxytocin);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String  cardAvailabilityDTG = jsonObjectdtg.isNull("sCardAvailableDTG") ? "" : jsonObjectdtg.getString("sCardAvailableDTG");
            
            String  stockstatusDTG = jsonObjectdtg.isNull("tldstatus") ? "" : jsonObjectdtg.getString("tldstatus");
            
            Integer alCountsDTG = getIntegerFromJsonKey(jsonObjectdtg, "StocksAvailableDTG");
            Integer cardbalanceDTG = getIntegerFromJsonKey(jsonObjectdtg, "stockCardBalanceDTG");
            Integer amcvalueDTG = getIntegerFromJsonKey(jsonObjectdtg, "StockCardIssuesDTG");
            Integer daysoutstockDTG = getIntegerFromJsonKey(jsonObjectdtg, "daysOutOfStockDTG");
            //String expiriesDTG = jsonObjectdtg.isNull("expiry6MonthsDTG") ? "" : jsonObjectdtg.getString("expiry6MonthsDTG");
            Integer actualcountDTG = getIntegerFromJsonKey(jsonObjectdtg, "physicalStockDTG");
            Integer cmosoneDTG = getIntegerFromJsonKey(datajson, "currentMOSDTG");
            Integer cmostwoDTG = getIntegerFromJsonKey(datajson, "currentMOS2DTG");
            Integer stcardscoreDTG = (datajson.isNull("stockCardScoreDTG")) ? empty : datajson.getInt("stockCardScoreDTG");
            Integer stCardsCountScoreDTG = (datajson.isNull("stockCardsCountScoreDTG")) ? empty : datajson.getInt("stockCardsCountScoreDTG");
            Integer iDenominatorDTG = (datajson.isNull("itemDenominatorDTG")) ? empty : datajson.getInt("itemDenominatorDTG");
            Integer iNumeratorDTG = (datajson.isNull("itemNumeratorDTG")) ? empty : datajson.getInt("itemNumeratorDTG");

            InventoryManagement dtg = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("TDF/3TC/DTG adult").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityDTG).stCountsDone(alCountsDTG).dayOutStock(daysoutstockDTG).cardBalance(cardbalanceDTG).amc(amcvalueDTG)
                    .actualCount(actualcountDTG).cmos1(cmosoneDTG).cmos2(cmostwoDTG).stCardScore(stcardscoreDTG)
                    .stCardsCountScore(stCardsCountScoreDTG).daysofStockScore(0).iDenominator(iDenominatorDTG).iNumerator(iNumeratorDTG)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusDTG)
                    .build();

            try {
                inventoryDao.save(dtg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityDTG10 = jsonObjectdtg10.isNull("sCardAvailableDTG10") ? "" : jsonObjectdtg10.getString("sCardAvailableDTG10");
            
            String stockstatusDTG10 = jsonObjectdtg10.isNull("DTG10status") ? "" : jsonObjectdtg10.getString("DTG10status");
            
            Integer alCountsDTG10 = getIntegerFromJsonKey(jsonObjectdtg10, "StocksAvailableDTG10");
            Integer cardbalanceDTG10 = getIntegerFromJsonKey(jsonObjectdtg10, "stockCardBalanceDTG10");
            Integer amcvalueDTG10 = getIntegerFromJsonKey(jsonObjectdtg10, "StockCardIssuesDTG10");
            Integer daysoutstockdtg10 = getIntegerFromJsonKey(jsonObjectdtg10, "daysOutOfStockDTG10");
            //String expiriesDTG10 = jsonObjectdtg10.isNull("expiry6MonthsDTG10") ? "" : jsonObjectdtg10.getString("expiry6MonthsDTG10");
            Integer actualcountDTG10 = getIntegerFromJsonKey(jsonObjectdtg10, "physicalStockDTG10");
            Integer cmosoneDTG10 = getIntegerFromJsonKey(datajson, "currentMOSDTG10");
            Integer cmostwoDTG10 = getIntegerFromJsonKey(datajson, "currentMOS2DTG10");
            Integer stcardscoreDTG10 = (datajson.isNull("stockCardScoreDTG10")) ? empty : datajson.getInt("stockCardScoreDTG10");
            Integer stCardsCountScoreDTG10 = (datajson.isNull("stockCardsCountScoreDTG10")) ? empty : datajson.getInt("stockCardsCountScoreDTG10");
            Integer iDenominatorDTG10 = (datajson.isNull("itemDenominatorDTG10")) ? empty : datajson.getInt("itemDenominatorDTG10");
            Integer iNumeratorDTG10 = (datajson.isNull("itemNumeratorDTG10")) ? empty : datajson.getInt("itemNumeratorDTG10");

            InventoryManagement dtg10 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("DTG 10mg").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityDTG10).stCountsDone(alCountsDTG10).dayOutStock(daysoutstockdtg10).cardBalance(cardbalanceDTG10).amc(amcvalueDTG10)
                    .actualCount(actualcountDTG10).cmos1(cmosoneDTG10).cmos2(cmostwoDTG10).stCardScore(stcardscoreDTG10)
                    .stCardsCountScore(stCardsCountScoreDTG10).daysofStockScore(0).iDenominator(iDenominatorDTG10).iNumerator(iNumeratorDTG10)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusDTG10)
                    .build();

            try {
                inventoryDao.save(dtg10);
            } catch (Exception e) {
                log.info("Duplication");
            }

            String cardAvailabilityFTC300 = jsonObjectftc300.isNull("sCardAvailableFTC300") ? "" : jsonObjectftc300.getString("sCardAvailableFTC300");
            
            String stockstatusFTC300 = jsonObjectftc300.isNull("FTC300status") ? "" : jsonObjectftc300.getString("FTC300status");
            
            Integer alCountsFTC300 = getIntegerFromJsonKey(jsonObjectftc300, "StocksAvailableFTC300");
            Integer cardbalanceFTC300 = getIntegerFromJsonKey(jsonObjectftc300, "stockCardBalanceFTC300");
            Integer amcvalueFTC300 = getIntegerFromJsonKey(jsonObjectftc300, "StockCardIssuesFTC300");
            Integer daysoutstockFTC300 = getIntegerFromJsonKey(jsonObjectftc300, "daysOutOfStockFTC300");
            //String expiriesFTC300 = jsonObjectftc300.isNull("expiry6MonthsFTC300") ? "" : jsonObjectdtg.getString("expiry6MonthsFTC300");
            Integer actualcountFTC300 = getIntegerFromJsonKey(jsonObjectftc300, "physicalStockFTC300");
            Integer cmosoneFTC300 = getIntegerFromJsonKey(datajson, "currentMOSFTC300");
            Integer cmostwoFTC300 = getIntegerFromJsonKey(datajson, "currentMOS2FTC300");
            Integer stcardscoreFTC300 = (datajson.isNull("stockCardScoreFTC300")) ? empty : datajson.getInt("stockCardScoreFTC300");
            Integer stCardsCountScoreFTC300 = (datajson.isNull("stockCardsCountScoreFTC300")) ? empty : datajson.getInt("stockCardsCountScoreFTC300");
            Integer iDenominatorFTC300 = (datajson.isNull("itemDenominatorFTC300")) ? empty : datajson.getInt("itemDenominatorFTC300");
            Integer iNumeratorFTC300 = (datajson.isNull("itemNumeratorFTC300")) ? empty : datajson.getInt("itemNumeratorFTC300");

            InventoryManagement ftc300 = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("TDF/FTC 300mg/200mg").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityFTC300).stCountsDone(alCountsFTC300).dayOutStock(daysoutstockFTC300).cardBalance(cardbalanceFTC300).amc(amcvalueFTC300)
                    .actualCount(actualcountFTC300).cmos1(cmosoneFTC300).cmos2(cmostwoFTC300).stCardScore(stcardscoreFTC300)
                    .stCardsCountScore(stCardsCountScoreFTC300).daysofStockScore(0).iDenominator(iDenominatorFTC300).iNumerator(iNumeratorFTC300)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusFTC300)
                    .build();

            try {
                inventoryDao.save(ftc300);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
         
            String county12 = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty12 = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility12 = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");            
            
            String cardAvailabilityABC = jsonObjectabc.isNull("sCardAvailableABC") ? "" : jsonObjectabc.getString("sCardAvailableABC");
            
            String stockstatusABC = jsonObjectabc.isNull("abcstatus") ? "" : jsonObjectabc.getString("abcstatus");
            
            Integer alCountsABC = getIntegerFromJsonKey(jsonObjectabc, "StocksAvailableABC");
            Integer cardbalanceABC = getIntegerFromJsonKey(jsonObjectabc, "stockCardBalanceABC");
            Integer amcvalueABC = getIntegerFromJsonKey(jsonObjectabc, "StockCardIssuesABC");
            Integer daysoutstockABC = getIntegerFromJsonKey(jsonObjectabc, "daysOutOfStockABC");
            //String expiriesABC = jsonObjectabc.isNull("expiry6MonthsABC") ? "" : jsonObjectabc.getString("expiry6MonthsABC");
            Integer actualcountABC = getIntegerFromJsonKey(jsonObjectabc, "physicalStockABC");
            Integer cmosoneABC = getIntegerFromJsonKey(datajson, "currentMOSABC");
            Integer cmostwoABC = getIntegerFromJsonKey(datajson, "currentMOS2ABC");
            Integer stcardscoreABC = (datajson.isNull("stockCardScoreABC")) ? empty : datajson.getInt("stockCardScoreABC");
            Integer stCardsCountScoreABC = (datajson.isNull("stockCardsCountScoreABC")) ? empty : datajson.getInt("stockCardsCountScoreABC");
            Integer iDenominatorABC = (datajson.isNull("itemDenominatorABC")) ? empty : datajson.getInt("itemDenominatorABC");
            Integer iNumeratorABC = (datajson.isNull("itemNumeratorABC")) ? empty : datajson.getInt("itemNumeratorABC");

            InventoryManagement abc = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county12).facility(facility12).subcounty(subcounty12).product("ABC/3TC paed").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityABC).stCountsDone(alCountsABC).dayOutStock(daysoutstockABC).cardBalance(cardbalanceABC).amc(amcvalueABC)
                    .actualCount(actualcountABC).cmos1(cmosoneABC).cmos2(cmostwoABC).stCardScore(stcardscoreABC)
                    .stCardsCountScore(stCardsCountScoreABC).daysofStockScore(0).iDenominator(iDenominatorABC).iNumerator(iNumeratorABC)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusABC)
                    .build();

            try {
                inventoryDao.save(abc);
            } catch (Exception e) {
                log.info("Duplication");
            }
          

            String cardAvailabilityHIV = jsonObjecthivs.isNull("sCardAvailableHIV") ? "" : jsonObjecthivs.getString("sCardAvailableHIV");
            
            String stockstatusHIV = jsonObjecthivs.isNull("hivstatus") ? "" : jsonObjecthivs.getString("hivstatus");
            
            Integer alCountsHIV = getIntegerFromJsonKey(jsonObjecthivs, "StocksAvailableHIV");
            Integer cardbalanceHIV = getIntegerFromJsonKey(jsonObjecthivs, "stockCardBalanceHIV");
            Integer amcvalueHIV = getIntegerFromJsonKey(jsonObjecthivs, "StockCardIssuesHIV");
            Integer daysoutstockHIV = getIntegerFromJsonKey(jsonObjecthivs, "daysOutOfStockHIV");
            //String expiriesHIV = jsonObjecthivs.isNull("expiry6MonthsHIV") ? "" : jsonObjecthivs.getString("expiry6MonthsHIV");
            Integer actualcountHIV = getIntegerFromJsonKey(jsonObjecthivs, "physicalStockHIV");
            Integer cmosoneHIV = getIntegerFromJsonKey(datajson, "currentMOSHIV");
            Integer cmostwoHIV = getIntegerFromJsonKey(datajson, "currentMOS2HIV");
            Integer stcardscoreHIV = (datajson.isNull("stockCardScoreHIV")) ? empty : datajson.getInt("stockCardScoreHIV");
            Integer stCardsCountScoreHIV = (datajson.isNull("stockCardsCountScoreHIV")) ? empty : datajson.getInt("stockCardsCountScoreHIV");
            Integer iDenominatorHIV = (datajson.isNull("itemDenominatorHIV")) ? empty : datajson.getInt("itemDenominatorHIV");
            Integer iNumeratorHIV = (datajson.isNull("itemNumeratorHIV")) ? empty : datajson.getInt("itemNumeratorHIV");
       
            InventoryManagement HIV = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("HIV screening test").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityHIV).stCountsDone(alCountsHIV).dayOutStock(daysoutstockHIV).cardBalance(cardbalanceHIV).amc(amcvalueHIV)
                    .actualCount(actualcountHIV).cmos1(cmosoneHIV).cmos2(cmostwoHIV).stCardScore(stcardscoreHIV)
                    .stCardsCountScore(stCardsCountScoreHIV).daysofStockScore(0).iDenominator(iDenominatorHIV).iNumerator(iNumeratorHIV)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusHIV)
                    .build();

            try {
                inventoryDao.save(HIV);
            } catch (Exception e) {
                log.info("Duplication");
            }            
            
            String cardAvailabilityoraquick = jsonObjectoraquick.isNull("sCardAvailableoraquick") ? "" : jsonObjectoraquick.getString("sCardAvailableoraquick");
            
            String stockstatusoraquick = jsonObjectoraquick.isNull("selfteststatus") ? "" : jsonObjectoraquick.getString("selfteststatus");
            
            Integer alCountsoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "StocksAvailableoraquick");
            Integer cardbalanceoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "stockCardBalanceoraquick");
            Integer amcvalueoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "StockCardIssuesoraquick");
            Integer daysoutstockquick = getIntegerFromJsonKey(jsonObjectoraquick, "daysOutOfStockoraquick");
            //String expiriesoraquick = jsonObjectoraquick.isNull("expiry6Monthsoraquick") ? "" : jsonObjectoraquick.getString("expiry6Monthsoraquick");
            Integer actualcountoraquick = getIntegerFromJsonKey(jsonObjectoraquick, "physicalStockoraquick");
            Integer cmosoneoraquick = getIntegerFromJsonKey(datajson, "currentMOSoraquick");
            Integer cmostwooraquick = getIntegerFromJsonKey(datajson, "currentMOS2oraquick");
            Integer stcardscoreoraquick = (datajson.isNull("stockCardScoreoraquick")) ? empty : datajson.getInt("stockCardScoreoraquick");
            Integer stCardsCountScoreoraquick = (datajson.isNull("stockCardsCountScoreoraquick")) ? empty : datajson.getInt("stockCardsCountScoreoraquick");
            Integer iDenominatororaquick = (datajson.isNull("itemDenominatororaquick")) ? empty : datajson.getInt("itemDenominatororaquick");
            Integer iNumeratororaquick = (datajson.isNull("itemNumeratororaquick")) ? empty : datajson.getInt("itemNumeratororaquick");

            InventoryManagement oraquick = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("HIV self-test (Oraquick), tests").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityoraquick).stCountsDone(alCountsoraquick).dayOutStock(daysoutstockquick).cardBalance(cardbalanceoraquick).amc(amcvalueoraquick)
                    .actualCount(actualcountoraquick).cmos1(cmosoneoraquick).cmos2(cmostwooraquick).stCardScore(stcardscoreoraquick)
                    .stCardsCountScore(stCardsCountScoreoraquick).daysofStockScore(0).iDenominator(iDenominatororaquick).iNumerator(iNumeratororaquick)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusoraquick)
                    .build();

            try {
                inventoryDao.save(oraquick);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            
            String cardAvailabilityBenzyl = jsonObjectBenzyl.isNull("sCardAvailableBenzyl") ? "" : jsonObjectBenzyl.getString("sCardAvailableBenzyl");
            
            String stockstatusBenzyl = jsonObjectBenzyl.isNull("benstatus") ? "" : jsonObjectBenzyl.getString("benstatus");
            
            Integer alCountsBenzyl = getIntegerFromJsonKey(jsonObjectBenzyl, "StocksAvailableBenzyl");
            Integer cardbalanceBenzyl = getIntegerFromJsonKey(jsonObjectBenzyl, "stockCardBalanceBenzyl");
            Integer amcvalueBenzyl = getIntegerFromJsonKey(jsonObjectBenzyl, "StockCardIssuesBenzyl");
            Integer daysoutstockBenzyl = getIntegerFromJsonKey(jsonObjectBenzyl, "daysOutOfStockBenzyl");
            //String expiriesBenzyl = jsonObjectBenzyl.isNull("expiry6MonthsBenzyl") ? "" : jsonObjectBenzyl.getString("expiry6MonthsBenzyl");
            Integer actualcountBenzyl = getIntegerFromJsonKey(jsonObjectBenzyl, "physicalStockBenzyl");
            Integer cmosoneBenzyl = getIntegerFromJsonKey(datajson, "currentMOSBenzyl");
            Integer cmostwoBenzyl = getIntegerFromJsonKey(datajson, "currentMOS2Benzyl");
            Integer stcardscoreBenzyl = (datajson.isNull("stockCardScoreBenzyl")) ? empty : datajson.getInt("stockCardScoreBenzyl");
            Integer stCardsCountScoreBenzyl = (datajson.isNull("stockCardsCountScoreBenzyl")) ? empty : datajson.getInt("stockCardsCountScoreBenzyl");
            Integer iDenominatorBenzyl = (datajson.isNull("itemDenominatorBenzyl")) ? empty : datajson.getInt("itemDenominatorBenzyl");
            Integer iNumeratorBenzyl = (datajson.isNull("itemNumeratorBenzyl")) ? empty : datajson.getInt("itemNumeratorBenzyl");
  
            InventoryManagement Benzyl = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Benzyl Penicillin 1MU").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityBenzyl).stCountsDone(alCountsBenzyl).dayOutStock(daysoutstockBenzyl).cardBalance(cardbalanceBenzyl).amc(amcvalueBenzyl)
                    .actualCount(actualcountBenzyl).cmos1(cmosoneBenzyl).cmos2(cmostwoBenzyl).stCardScore(stcardscoreBenzyl)
                    .stCardsCountScore(stCardsCountScoreBenzyl).daysofStockScore(0).iDenominator(iDenominatorBenzyl).iNumerator(iNumeratorBenzyl)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusBenzyl)
                    .build();

            try {
                inventoryDao.save(Benzyl);
            } catch (Exception e) {
                log.info("Duplication");
            }            
            
            String cardAvailabilitygenta = jsonObjectgenta.isNull("sCardAvailablegenta") ? "" : jsonObjectgenta.getString("sCardAvailablegenta");
            
            String stockstatusgenta = jsonObjectgenta.isNull("gentastatus") ? "" : jsonObjectgenta.getString("gentastatus");
            
            Integer alCountsgenta = getIntegerFromJsonKey(jsonObjectgenta, "StocksAvailablegenta");
            Integer cardbalancegenta = getIntegerFromJsonKey(jsonObjectgenta, "stockCardBalancegenta");
            Integer amcvaluegenta = getIntegerFromJsonKey(jsonObjectgenta, "StockCardIssuesgenta");
            Integer daysoutstockgenta = getIntegerFromJsonKey(jsonObjectgenta, "daysOutOfStockgenta");
            //String expiriesgenta = jsonObjectgenta.isNull("expiry6Monthsgenta") ? "" : jsonObjectgenta.getString("expiry6Monthsgenta");
            Integer actualcountgenta = getIntegerFromJsonKey(jsonObjectgenta, "physicalStockgenta");
            Integer cmosonegenta = getIntegerFromJsonKey(datajson, "currentMOSgenta");
            Integer cmostwogenta = getIntegerFromJsonKey(datajson, "currentMOS2genta");
            Integer stcardscoregenta = (datajson.isNull("stockCardScoregenta")) ? empty : datajson.getInt("stockCardScoregenta");
            Integer stCardsCountScoregenta = (datajson.isNull("stockCardsCountScoregenta")) ? empty : datajson.getInt("stockCardsCountScoregenta");
            Integer iDenominatorgenta = (datajson.isNull("itemDenominatorgenta")) ? empty : datajson.getInt("itemDenominatorgenta");
            Integer iNumeratorgenta = (datajson.isNull("itemNumeratorgenta")) ? empty : datajson.getInt("itemNumeratorgenta");

            InventoryManagement genta = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Gentamycin inj 80mg/2ml").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilitygenta).stCountsDone(alCountsgenta).dayOutStock(daysoutstockgenta).cardBalance(cardbalancegenta).amc(amcvaluegenta)
                    .actualCount(actualcountgenta).cmos1(cmosonegenta).cmos2(cmostwogenta).stCardScore(stcardscoregenta)
                    .stCardsCountScore(stCardsCountScoregenta).daysofStockScore(0).iDenominator(iDenominatorgenta).iNumerator(iNumeratorgenta)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusgenta)
                    .build();

            try {
                inventoryDao.save(genta);
            } catch (Exception e) {
                log.info("Duplication");
            }

            
            String cardAvailabilityMMR = jsonObjectmmr.isNull("sCardAvailableMMR") ? "" : jsonObjectmmr.getString("sCardAvailableMMR");
            
            String stockstatusMMR = jsonObjectmmr.isNull("mmrstatus") ? "" : jsonObjectmmr.getString("mmrstatus");
            
            Integer alCountsMMR = getIntegerFromJsonKey(jsonObjectmmr, "StocksAvailableMMR");
            Integer cardbalanceMMR = getIntegerFromJsonKey(jsonObjectmmr, "stockCardBalanceMMR");
            Integer amcvalueMMR = getIntegerFromJsonKey(jsonObjectmmr, "StockCardIssuesMMR");
            Integer daysoutstockMMR = getIntegerFromJsonKey(jsonObjectmmr, "daysOutOfStockMMR");
            //String expiriesMMR = jsonObjectmmr.isNull("expiry6MonthsMMR") ? "" : jsonObjectmmr.getString("expiry6MonthsMMR");
            Integer actualcountMMR = getIntegerFromJsonKey(jsonObjectmmr, "physicalStockMMR");
            Integer cmosoneMMR = getIntegerFromJsonKey(datajson, "currentMOSMMR");
            Integer cmostwoMMR = getIntegerFromJsonKey(datajson, "currentMOS2MMR");
            Integer stcardscoreMMR = (datajson.isNull("stockCardScoreMMR")) ? empty : datajson.getInt("stockCardScoreMMR");
            Integer stCardsCountScoreMMR = (datajson.isNull("stockCardsCountScoreMMR")) ? empty : datajson.getInt("stockCardsCountScoreMMR");
            Integer iDenominatorMMR = (datajson.isNull("itemDenominatorMMR")) ? empty : datajson.getInt("itemDenominatorMMR");
            Integer iNumeratorMMR = (datajson.isNull("itemNumeratorMMR")) ? empty : datajson.getInt("itemNumeratorMMR");
;
            InventoryManagement mmr = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("MMR vaccine").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityMMR).stCountsDone(alCountsMMR).dayOutStock(daysoutstockMMR).cardBalance(cardbalanceMMR).amc(amcvalueMMR)
                    .actualCount(actualcountMMR).cmos1(cmosoneMMR).cmos2(cmostwoMMR).stCardScore(stcardscoreMMR)
                    .stCardsCountScore(stCardsCountScoreMMR).daysofStockScore(0).iDenominator(iDenominatorMMR).iNumerator(iNumeratorMMR)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusMMR)
                    .build();

            try {
                inventoryDao.save(mmr);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            String cardAvailabilityexgloves = jsonObjectexgloves.isNull("sCardAvailableexgloves") ? "" : jsonObjectexgloves.getString("sCardAvailableexgloves");
            
            String stockstatusexgloves = jsonObjectexgloves.isNull("exglovesstatus") ? "" : jsonObjectexgloves.getString("exglovesstatus");
            
            Integer alCountsexgloves = getIntegerFromJsonKey(jsonObjectexgloves, "StocksAvailableexgloves");
            Integer cardbalanceexgloves = getIntegerFromJsonKey(jsonObjectexgloves, "stockCardBalanceexgloves");
            Integer amcvalueexgloves = getIntegerFromJsonKey(jsonObjectexgloves, "StockCardIssuesexgloves");
            Integer daysoutstockexgloves = getIntegerFromJsonKey(jsonObjectexgloves, "daysOutOfStockexgloves");
            //String expiriesexgloves = jsonObjectexgloves.isNull("expiry6Monthsexgloves") ? "" : jsonObjectexgloves.getString("expiry6Monthsexgloves");
            Integer actualcountexgloves = getIntegerFromJsonKey(jsonObjectexgloves, "physicalStockexgloves");
            Integer cmosoneexgloves = getIntegerFromJsonKey(datajson, "currentMOSexgloves");
            Integer cmostwoexgloves = getIntegerFromJsonKey(datajson, "currentMOS2exgloves");
            Integer stcardscoreexgloves = (datajson.isNull("stockCardScoreexgloves")) ? empty : datajson.getInt("stockCardScoreexgloves");
            Integer stCardsCountScoreexgloves = (datajson.isNull("stockCardsCountScoreexgloves")) ? empty : datajson.getInt("stockCardsCountScoreexgloves");
            Integer iDenominatorexgloves = (datajson.isNull("itemDenominatorexgloves")) ? empty : datajson.getInt("itemDenominatorexgloves");
            Integer iNumeratorexgloves = (datajson.isNull("itemNumeratorexgloves")) ? empty : datajson.getInt("itemNumeratorexgloves");

            InventoryManagement exgloves = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("Examination Gloves").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityexgloves).stCountsDone(alCountsexgloves).dayOutStock(daysoutstockexgloves).cardBalance(cardbalanceexgloves).amc(amcvalueexgloves)
                    .actualCount(actualcountexgloves).cmos1(cmosoneexgloves).cmos2(cmostwoexgloves).stCardScore(stcardscoreexgloves)
                    .stCardsCountScore(stCardsCountScoreexgloves).daysofStockScore(0).iDenominator(iDenominatorexgloves).iNumerator(iNumeratorexgloves)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusexgloves)
                    .build();

            try {
                inventoryDao.save(exgloves);
            } catch (Exception e) {
                log.info("Duplication");
            } 

            String cardAvailabilityRUSF = jsonObjectRUSF.isNull("sCardAvailableRUSF") ? "" : jsonObjectRUSF.getString("sCardAvailableRUSF");
            
            String stockstatusRUSF = jsonObjectRUSF.isNull("RUSFstatus") ? "" : jsonObjectRUSF.getString("RUSFstatus");
            
            Integer alCountsRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "StocksAvailableRUSF");
            Integer cardbalanceRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "stockCardBalanceRUSF");
            Integer amcvalueRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "StockCardIssuesRUSF");
            Integer daysoutstockRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "daysOutOfStockRUSF");
            //String expiriesRUSF = jsonObjectRUSF.isNull("expiry6MonthsRUSF") ? "" : jsonObjectRUSF.getString("expiry6MonthsRUSF");
            Integer actualcountRUSF = getIntegerFromJsonKey(jsonObjectRUSF, "physicalStockRUSF");
            Integer cmosoneRUSF = getIntegerFromJsonKey(datajson, "currentMOSRUSF");
            Integer cmostwoRUSF = getIntegerFromJsonKey(datajson, "currentMOS2RUSF");
            Integer stcardscoreRUSF = (datajson.isNull("stockCardScoreRUSF")) ? empty : datajson.getInt("stockCardScoreRUSF");
            Integer stCardsCountScoreRUSF = (datajson.isNull("stockCardsCountScoreRUSF")) ? empty : datajson.getInt("stockCardsCountScoreRUSF");
            Integer iDenominatorRUSF = (datajson.isNull("itemDenominatorRUSF")) ? empty : datajson.getInt("itemDenominatorRUSF");
            Integer iNumeratorRUSF = (datajson.isNull("itemNumeratorRUSF")) ? empty : datajson.getInt("itemNumeratorRUSF");

            InventoryManagement RUSF = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("RUSF").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityRUSF).stCountsDone(alCountsRUSF).dayOutStock(daysoutstockRUSF).cardBalance(cardbalanceRUSF).amc(amcvalueRUSF)
                    .actualCount(actualcountRUSF).cmos1(cmosoneRUSF).cmos2(cmostwoRUSF).stCardScore(stcardscoreRUSF)
                    .stCardsCountScore(stCardsCountScoreRUSF).daysofStockScore(0).iDenominator(iDenominatorRUSF).iNumerator(iNumeratorRUSF)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusRUSF)
                    .build();

            try {
                inventoryDao.save(RUSF);
            } catch (Exception e) {
                log.info("Duplication");
            }
            
            String cardAvailabilityRUTF = jsonObjectRUTF.isNull("sCardAvailableRUTF") ? "" : jsonObjectRUTF.getString("sCardAvailableRUTF");
            
            String stockstatusRUTF = jsonObjectRUTF.isNull("RUTFstatus") ? "" : jsonObjectRUTF.getString("RUTFstatus");
            
            Integer alCountsRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "StocksAvailableRUTF");
            Integer cardbalanceRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "stockCardBalanceRUTF");
            Integer amcvalueRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "StockCardIssuesRUTF");
            Integer daysoutstockRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "daysOutOfStockRUTF");
            //String expiriesRUTF = jsonObjectRUTF.isNull("expiry6MonthsRUTF") ? "" : jsonObjectRUTF.getString("expiry6MonthsRUTF");
            Integer actualcountRUTF = getIntegerFromJsonKey(jsonObjectRUTF, "physicalStockRUTF");
            Integer cmosoneRUTF = getIntegerFromJsonKey(datajson, "currentMOSRUTF");
            Integer cmostwoRUTF = getIntegerFromJsonKey(datajson, "currentMOS2RUTF");
            Integer stcardscoreRUTF = (datajson.isNull("stockCardScoreRUTF")) ? empty : datajson.getInt("stockCardScoreRUTF");
            Integer stCardsCountScoreRUTF = (datajson.isNull("stockCardsCountScoreRUTF")) ? empty : datajson.getInt("stockCardsCountScoreRUTF");
            Integer iDenominatorRUTF = (datajson.isNull("itemDenominatorRUTF")) ? empty : datajson.getInt("itemDenominatorRUTF");
            Integer iNumeratorRUTF = (datajson.isNull("itemNumeratorRUTF")) ? empty : datajson.getInt("itemNumeratorRUTF");

            InventoryManagement RUTF = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("RUTF").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityRUTF).stCountsDone(alCountsRUTF).dayOutStock(daysoutstockRUTF).cardBalance(cardbalanceRUTF).amc(amcvalueRUTF)
                    .actualCount(actualcountRUTF).cmos1(cmosoneRUTF).cmos2(cmostwoRUTF).stCardScore(stcardscoreRUTF)
                    .stCardsCountScore(stCardsCountScoreRUTF).daysofStockScore(0).iDenominator(iDenominatorRUTF).iNumerator(iNumeratorRUTF)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusRUTF)
                    .build();

            try {
                inventoryDao.save(RUTF);
            } catch (Exception e) {
                log.info("Duplication");
            }             
            
            String cardAvailabilityIFAS = jsonObjectIFAS.isNull("sCardAvailableIFAS") ? "" : jsonObjectIFAS.getString("sCardAvailableIFAS");
            
            String stockstatusIFAS = jsonObjectIFAS.isNull("IFASstatus") ? "" : jsonObjectIFAS.getString("IFASstatus");
            
            Integer alCountsIFAS = getIntegerFromJsonKey(jsonObjectIFAS, "StocksAvailableIFAS");
            Integer cardbalanceIFAS = getIntegerFromJsonKey(jsonObjectIFAS, "stockCardBalanceIFAS");
            Integer amcvalueIFAS = getIntegerFromJsonKey(jsonObjectIFAS, "StockCardIssuesIFAS");
            Integer daysoutstockIFAS = getIntegerFromJsonKey(jsonObjectIFAS, "daysOutOfStockIFAS");
            //String expiriesIFAS = jsonObjectIFAS.isNull("expiry6MonthsIFAS") ? "" : jsonObjectIFAS.getString("expiry6MonthsIFAS");
            Integer actualcountIFAS = getIntegerFromJsonKey(jsonObjectIFAS, "physicalStockIFAS");
            Integer cmosoneIFAS = getIntegerFromJsonKey(datajson, "currentMOSIFAS");
            Integer cmostwoIFAS = getIntegerFromJsonKey(datajson, "currentMOS2IFAS");
            Integer stcardscoreIFAS = (datajson.isNull("stockCardScoreIFAS")) ? empty : datajson.getInt("stockCardScoreIFAS");
            Integer stCardsCountScoreIFAS = (datajson.isNull("stockCardsCountScoreIFAS")) ? empty : datajson.getInt("stockCardsCountScoreIFAS");
            Integer iDenominatorIFAS = (datajson.isNull("itemDenominatorIFAS")) ? empty : datajson.getInt("itemDenominatorIFAS");
            Integer iNumeratorIFAS = (datajson.isNull("itemNumeratorIFAS")) ? empty : datajson.getInt("itemNumeratorIFAS");

            InventoryManagement IFAS = new InventoryManagement.InventoryManagementBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("IFAS").orgUnit(orgUnit)
                    .stCardAvail(cardAvailabilityIFAS).stCountsDone(alCountsIFAS).dayOutStock(daysoutstockIFAS).cardBalance(cardbalanceIFAS).amc(amcvalueIFAS)
                    .actualCount(actualcountIFAS).cmos1(cmosoneIFAS).cmos2(cmostwoIFAS).stCardScore(stcardscoreIFAS)
                    .stCardsCountScore(stCardsCountScoreIFAS).daysofStockScore(0).iDenominator(iDenominatorIFAS).iNumerator(iNumeratorIFAS)
                    .numerator(numerator).denominator(denominator).score(score).stockstatus(stockstatusIFAS)
                    .build();

            try {
                inventoryDao.save(IFAS);
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
