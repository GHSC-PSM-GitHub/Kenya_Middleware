/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.dashboard.VerificationDataDaoImpl;
import com.odkdhis.model.dashboard.VerificationDataModel;
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
public class CommodityVerificationData extends CommonUtil {

    @Autowired
    private VerificationDataDaoImpl verificationDataDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
   @Scheduled(cron ="10 15 * * * ?", zone = "Africa/Nairobi")
    public void prepareSubmissionVerificationData() {
        try {
            basicCommodityVerificationData("Verification_of_commodity_data");
            //Thread.sleep(2000);

        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    private void basicCommodityVerificationData(String formId) throws Exception {
        log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV" + formId);

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
            if (verificationDataDao.getRecordByInstanceId(instanceId) != null) {
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
            JSONObject jsonObjectAl6s = null;
            if (!datajson.isNull("al6s")) {
                jsonObjectAl6s = datajson.getJSONObject("al6s");
            }
            
            //Integer empty = 0;
            
            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stocked = jsonObjectAl6s.isNull("stock_facilityAL6s") ? "" : jsonObjectAl6s.getString("stock_facilityAL6s");
            Integer qtyDardispense = getIntegerFromJsonKey(jsonObjectAl6s, "dispensed_darAL6s");
            Integer qtycdrrdispensed = getIntegerFromJsonKey(jsonObjectAl6s, "dispensed_cdrrAL6s");
            Integer qtydhisdispensed = getIntegerFromJsonKey(jsonObjectAl6s, "dispensed_khis2AL6s");
            Integer physicalCountCdrr = getIntegerFromJsonKey(jsonObjectAl6s, "count_cdrrAL6s");
            Integer physicalCountdhis = getIntegerFromJsonKey(jsonObjectAl6s, "count_khis2AL6s");
            Integer varianceDarMsf = getIntegerFromJsonKey(datajson,"darmsfAL6s");
            Integer varianceMsfDhis = getIntegerFromJsonKey(datajson,"khismsfdispensedAL6s");
            Integer varianceCdrrDhis = getIntegerFromJsonKey(datajson, "khismsfcountAL6s");
            
            Integer numerator = getIntegerFromJsonKey(datajson,"numeratorAL6s");
            Integer denominator = getIntegerFromJsonKey(datajson,"denominatorAL6s");
            

            VerificationDataModel al6s = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 6s").orgUnit(orgUnit)
                    .stocked(stocked).qtyDardispense(qtyDardispense).qtycdrrdispensed(qtycdrrdispensed).qtydhisdispensed(qtydhisdispensed)
                    .physicalCountCdrr(physicalCountCdrr).physicalCountdhis(physicalCountdhis)
                    .varianceDarMsf(varianceDarMsf).varianceCdrrDhis(varianceCdrrDhis).varianceMsfDhis(varianceMsfDhis)
                    .numerator(numerator).denominator(denominator).build();
            try {
                verificationDataDao.save(al6s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al6s);
            
    
            JSONObject jsonObjectAl12s = null;
            if (!datajson.isNull("al12s")) {
                jsonObjectAl12s = datajson.getJSONObject("al12s");
            }        
            

            String countyal12s = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyal12s = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityal12s = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedal12s = jsonObjectAl12s.isNull("stock_facilityAL12s") ? "" : jsonObjectAl12s.getString("stock_facilityAL12s");
            Integer qtyDardispenseal12s = getIntegerFromJsonKey(jsonObjectAl12s, "dispensed_darAL12s");
            Integer qtycdrrdispensedal12s = getIntegerFromJsonKey(jsonObjectAl12s, "dispensed_cdrrAL12s");
            Integer qtydhisdispensedal12s = getIntegerFromJsonKey(jsonObjectAl12s, "dispensed_khis2AL12s");
            Integer physicalCountCdrral12s = getIntegerFromJsonKey(jsonObjectAl12s, "count_cdrrAL12s");
            Integer physicalCountdhisal12s = getIntegerFromJsonKey(jsonObjectAl12s, "count_khis2AL12s");
            Integer varianceDarMsfal12s = getIntegerFromJsonKey(datajson,"darmsfAL12s");
            Integer varianceMsfDhisal12s = getIntegerFromJsonKey(datajson,"khismsfdispensedAL12s");
            Integer varianceCdrrDhisal12s = getIntegerFromJsonKey(datajson, "khismsfcountAL12s");
            
            Integer numeratoral12s = getIntegerFromJsonKey(datajson,"numeratorAL12s");
            Integer denominatoral12s = getIntegerFromJsonKey(datajson,"denominatorAL12s");
            

            VerificationDataModel al12s = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyal12s).facility(facilityal12s).subcounty(subcountyal12s).product("AL 12s").orgUnit(orgUnit)
                    .stocked(stockedal12s).qtyDardispense(qtyDardispenseal12s).qtycdrrdispensed(qtycdrrdispensedal12s).qtydhisdispensed(qtydhisdispensedal12s)
                    .physicalCountCdrr(physicalCountCdrral12s).physicalCountdhis(physicalCountdhisal12s)
                    .varianceDarMsf(varianceDarMsfal12s).varianceCdrrDhis(varianceCdrrDhisal12s).varianceMsfDhis(varianceMsfDhisal12s)
                    .numerator(numeratoral12s).denominator(denominatoral12s).build();
            try {
                verificationDataDao.save(al12s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al12s);            

            
            JSONObject jsonObjectAl18s = null;
            if (!datajson.isNull("al18s")) {
                jsonObjectAl18s = datajson.getJSONObject("al18s");
            }        
            

            String countyal18s = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyal18s = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityal18s = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedal18s = jsonObjectAl18s.isNull("stock_facilityAL18s") ? "" : jsonObjectAl18s.getString("stock_facilityAL18s");
            Integer qtyDardispenseal18s = getIntegerFromJsonKey(jsonObjectAl18s, "dispensed_darAL18s");
            Integer qtycdrrdispensedal18s = getIntegerFromJsonKey(jsonObjectAl18s, "dispensed_cdrrAL18s");
            Integer qtydhisdispensedal18s = getIntegerFromJsonKey(jsonObjectAl18s, "dispensed_khis2AL18s");
            Integer physicalCountCdrral18s = getIntegerFromJsonKey(jsonObjectAl18s, "count_cdrrAL18s");
            Integer physicalCountdhisal18s = getIntegerFromJsonKey(jsonObjectAl18s, "count_khis2AL18s");
            Integer varianceDarMsfal18s = getIntegerFromJsonKey(datajson,"darmsfAL18s");
            Integer varianceMsfDhisal18s = getIntegerFromJsonKey(datajson,"khismsfdispensedAL18s");
            Integer varianceCdrrDhisal18s = getIntegerFromJsonKey(datajson, "khismsfcountAL18s");
            
            Integer numeratoral18s = getIntegerFromJsonKey(datajson,"numeratorAL18s");
            Integer denominatoral18s = getIntegerFromJsonKey(datajson,"denominatorAL18s");
            

            VerificationDataModel al18s = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyal18s).facility(facilityal18s).subcounty(subcountyal18s).product("AL 18s").orgUnit(orgUnit)
                    .stocked(stockedal18s).qtyDardispense(qtyDardispenseal18s).qtycdrrdispensed(qtycdrrdispensedal18s).qtydhisdispensed(qtydhisdispensedal18s)
                    .physicalCountCdrr(physicalCountCdrral18s).physicalCountdhis(physicalCountdhisal18s)
                    .varianceDarMsf(varianceDarMsfal18s).varianceCdrrDhis(varianceCdrrDhisal18s).varianceMsfDhis(varianceMsfDhisal18s)
                    .numerator(numeratoral18s).denominator(denominatoral18s).build();
            try {
                verificationDataDao.save(al18s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al18s);               
            
           JSONObject jsonObjectAl24s = null;
            if (!datajson.isNull("al24s")) {
                jsonObjectAl24s = datajson.getJSONObject("al24s");
            }        
            

            String countyal24s = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyal24s = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityal24s = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedal24s = jsonObjectAl24s.isNull("stock_facilityAL24s") ? "" : jsonObjectAl24s.getString("stock_facilityAL24s");
            Integer qtyDardispenseal24s = getIntegerFromJsonKey(jsonObjectAl24s, "dispensed_darAL24s");
            Integer qtycdrrdispensedal24s = getIntegerFromJsonKey(jsonObjectAl24s, "dispensed_cdrrAL24s");
            Integer qtydhisdispensedal24s = getIntegerFromJsonKey(jsonObjectAl24s, "dispensed_khis2AL24s");
            Integer physicalCountCdrral24s = getIntegerFromJsonKey(jsonObjectAl24s, "count_cdrrAL24s");
            Integer physicalCountdhisal24s = getIntegerFromJsonKey(jsonObjectAl24s, "count_khis2AL24s");
            Integer varianceDarMsfal24s = getIntegerFromJsonKey(datajson,"darmsfAL24s");
            Integer varianceMsfDhisal24s = getIntegerFromJsonKey(datajson,"khismsfdispensedAL24s");
            Integer varianceCdrrDhisal24s = getIntegerFromJsonKey(datajson, "khismsfcountAL24s");
            
            Integer numeratoral24s = getIntegerFromJsonKey(datajson,"numeratorAL24s");
            Integer denominatoral24s = getIntegerFromJsonKey(datajson,"denominatorAL24s");
            

            VerificationDataModel al24s = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyal24s).facility(facilityal24s).subcounty(subcountyal24s).product("AL 24s").orgUnit(orgUnit)
                    .stocked(stockedal24s).qtyDardispense(qtyDardispenseal24s).qtycdrrdispensed(qtycdrrdispensedal24s).qtydhisdispensed(qtydhisdispensedal24s)
                    .physicalCountCdrr(physicalCountCdrral24s).physicalCountdhis(physicalCountdhisal24s)
                    .varianceDarMsf(varianceDarMsfal24s).varianceCdrrDhis(varianceCdrrDhisal24s).varianceMsfDhis(varianceMsfDhisal24s)
                    .numerator(numeratoral24s).denominator(denominatoral24s).build();
            try {
                verificationDataDao.save(al24s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al24s);               
            
            
           JSONObject jsonObjectSPtabs = null;
            if (!datajson.isNull("SPtabs")) {
                jsonObjectSPtabs = datajson.getJSONObject("SPtabs");
            }             
            
            String countysptabs = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountysptabs = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilitysptabs = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedsptabs = jsonObjectSPtabs.isNull("stock_facilitySPtabs") ? "" : jsonObjectSPtabs.getString("stock_facilitySPtabs");
            Integer qtyDardispensesptabs = getIntegerFromJsonKey(jsonObjectSPtabs, "dispensed_darSPtabs");
            Integer qtycdrrdispensedsptabs = getIntegerFromJsonKey(jsonObjectSPtabs, "dispensed_cdrrSPtabs");
            Integer qtydhisdispensedsptabs = getIntegerFromJsonKey(jsonObjectSPtabs, "dispensed_khis2SPtabs");
            Integer physicalCountCdrrsptabs = getIntegerFromJsonKey(jsonObjectSPtabs, "count_cdrrSPtabs");
            Integer physicalCountdhissptabs = getIntegerFromJsonKey(jsonObjectSPtabs, "count_khis2SPtabs");
            Integer varianceDarMsfsptabs = getIntegerFromJsonKey(datajson,"darmsfSPtabs");
            Integer varianceMsfDhissptabs = getIntegerFromJsonKey(datajson,"khismsfdispensedSPtabs");
            Integer varianceCdrrDhissptabs = getIntegerFromJsonKey(datajson, "khismsfcountSPtabs");
            
            Integer numeratorsptabs = getIntegerFromJsonKey(datajson,"numeratorSPtabs");
            Integer denominatorsptabs = getIntegerFromJsonKey(datajson,"denominatorSPtabs");
            

            VerificationDataModel sptabs = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countysptabs).facility(facilitysptabs).subcounty(subcountysptabs).product("SP tabs").orgUnit(orgUnit)
                    .stocked(stockedsptabs).qtyDardispense(qtyDardispensesptabs).qtycdrrdispensed(qtycdrrdispensedsptabs).qtydhisdispensed(qtydhisdispensedsptabs)
                    .physicalCountCdrr(physicalCountCdrrsptabs).physicalCountdhis(physicalCountdhissptabs)
                    .varianceDarMsf(varianceDarMsfsptabs).varianceCdrrDhis(varianceCdrrDhissptabs).varianceMsfDhis(varianceMsfDhissptabs)
                    .numerator(numeratorsptabs).denominator(denominatorsptabs).build();
            try {
                verificationDataDao.save(sptabs);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + sptabs); 
            

           JSONObject jsonObjectArtInj = null;
            if (!datajson.isNull("artesunate")) {
                jsonObjectArtInj = datajson.getJSONObject("artesunate");
            }             
            
            String countyartInj = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyartInj = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityartInj = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedartInj = jsonObjectArtInj.isNull("stock_facilityartInj") ? "" : jsonObjectArtInj.getString("stock_facilityartInj");
            Integer qtyDardispenseartInj = getIntegerFromJsonKey(jsonObjectArtInj, "dispensed_darartInj");
            Integer qtycdrrdispensedartInj = getIntegerFromJsonKey(jsonObjectArtInj, "dispensed_cdrrartInj");
            Integer qtydhisdispensedartInj = getIntegerFromJsonKey(jsonObjectArtInj, "dispensed_khis2artInj");
            Integer physicalCountCdrrartInj = getIntegerFromJsonKey(jsonObjectArtInj, "count_cdrrartInj");
            Integer physicalCountdhisartInj = getIntegerFromJsonKey(jsonObjectArtInj, "count_khis2artInj");
            Integer varianceDarMsfartInj = getIntegerFromJsonKey(datajson,"darmsfartInj");
            Integer varianceMsfDhisartInj = getIntegerFromJsonKey(datajson,"khismsfdispensedartInj");
            Integer varianceCdrrDhisartInj = getIntegerFromJsonKey(datajson, "khismsfcountartInj");
            
            Integer numeratorartInj = getIntegerFromJsonKey(datajson,"numeratorartInj");
            Integer denominatorartInj = getIntegerFromJsonKey(datajson,"denominatorartInj");
            

            VerificationDataModel artInj = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyartInj).facility(facilityartInj).subcounty(subcountyartInj).product("artesunate inj").orgUnit(orgUnit)
                    .stocked(stockedartInj).qtyDardispense(qtyDardispenseartInj).qtycdrrdispensed(qtycdrrdispensedartInj).qtydhisdispensed(qtydhisdispensedartInj)
                    .physicalCountCdrr(physicalCountCdrrartInj).physicalCountdhis(physicalCountdhisartInj)
                    .varianceDarMsf(varianceDarMsfartInj).varianceCdrrDhis(varianceCdrrDhisartInj).varianceMsfDhis(varianceMsfDhisartInj)
                    .numerator(numeratorartInj).denominator(denominatorartInj).build();
            try {
                verificationDataDao.save(artInj);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + artInj); 

           JSONObject jsonObjectLlins = null;
            if (!datajson.isNull("llins")) {
                jsonObjectLlins = datajson.getJSONObject("llins");
            }             
            
            String countyLLINs = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyLLINs = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityLLINs = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedLLINs = jsonObjectLlins.isNull("stock_facilityLLINs") ? "" : jsonObjectLlins.getString("stock_facilityLLINs");
            Integer qtyDardispenseLLINs = getIntegerFromJsonKey(jsonObjectLlins, "dispensed_darLLINs");
            Integer qtycdrrdispensedLLINs = getIntegerFromJsonKey(jsonObjectLlins, "dispensed_cdrrLLINs");
            Integer qtydhisdispensedLLINs = getIntegerFromJsonKey(jsonObjectLlins, "dispensed_khis2LLINs");
            Integer physicalCountCdrrLLINs = getIntegerFromJsonKey(jsonObjectLlins, "count_cdrrLLINs");
            Integer physicalCountdhisLLINs = getIntegerFromJsonKey(jsonObjectLlins, "count_khis2LLINs");
            Integer varianceDarMsfLLINs = getIntegerFromJsonKey(datajson,"darmsfLLINs");
            Integer varianceMsfDhisLLINs = getIntegerFromJsonKey(datajson,"khismsfdispensedLLINs");
            Integer varianceCdrrDhisLLINs = getIntegerFromJsonKey(datajson, "khismsfcountLLINs");
            
            Integer numeratorLLINs = getIntegerFromJsonKey(datajson,"numeratorLLINs");
            Integer denominatorLLINs = getIntegerFromJsonKey(datajson,"denominatorLLINs");
            

            VerificationDataModel llins = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyLLINs).facility(facilityLLINs).subcounty(subcountyLLINs).product("LLINs").orgUnit(orgUnit)
                    .stocked(stockedLLINs).qtyDardispense(qtyDardispenseLLINs).qtycdrrdispensed(qtycdrrdispensedLLINs).qtydhisdispensed(qtydhisdispensedLLINs)
                    .physicalCountCdrr(physicalCountCdrrLLINs).physicalCountdhis(physicalCountdhisLLINs)
                    .varianceDarMsf(varianceDarMsfLLINs).varianceCdrrDhis(varianceCdrrDhisLLINs).varianceMsfDhis(varianceMsfDhisLLINs)
                    .numerator(numeratorLLINs).denominator(denominatorLLINs).build();
            try {
                verificationDataDao.save(llins);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + llins);            

           JSONObject jsonObjectMalaria = null;
            if (!datajson.isNull("malaria")) {
                jsonObjectMalaria = datajson.getJSONObject("malaria");
            }             
            
            String countymrdts = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountymrdts = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilitymrdts = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedmrdts = jsonObjectMalaria.isNull("stock_facilityRDTs") ? "" : jsonObjectMalaria.getString("stock_facilityRDTs");
            Integer qtyDardispensemrdts = getIntegerFromJsonKey(jsonObjectMalaria, "dispensed_darRDTs");
            Integer qtycdrrdispensedmrdts = getIntegerFromJsonKey(jsonObjectMalaria, "dispensed_cdrrRDTs");
            Integer qtydhisdispensedmrdts = getIntegerFromJsonKey(jsonObjectMalaria, "dispensed_khis2RDTs");
            Integer physicalCountCdrrmrdts = getIntegerFromJsonKey(jsonObjectMalaria, "count_cdrrRDTs");
            Integer physicalCountdhismrdts = getIntegerFromJsonKey(jsonObjectMalaria, "count_khis2RDTs");
            Integer varianceDarMsfmrdts = getIntegerFromJsonKey(datajson,"darmsfRDTs");
            Integer varianceMsfDhismrdts = getIntegerFromJsonKey(datajson,"khismsfdispensedRDTs");
            Integer varianceCdrrDhismrdts = getIntegerFromJsonKey(datajson, "khismsfcountRDTs");
            
            Integer numeratormrdts = getIntegerFromJsonKey(datajson,"numeratorRDTs");
            Integer denominatormrdts = getIntegerFromJsonKey(datajson,"denominatorRDTs");
            

            VerificationDataModel mrdts = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countymrdts).facility(facilitymrdts).subcounty(subcountymrdts).product("malaria RDTs").orgUnit(orgUnit)
                    .stocked(stockedmrdts).qtyDardispense(qtyDardispensemrdts).qtycdrrdispensed(qtycdrrdispensedmrdts).qtydhisdispensed(qtydhisdispensedmrdts)
                    .physicalCountCdrr(physicalCountCdrrmrdts).physicalCountdhis(physicalCountdhismrdts)
                    .varianceDarMsf(varianceDarMsfmrdts).varianceCdrrDhis(varianceCdrrDhismrdts).varianceMsfDhis(varianceMsfDhismrdts)
                    .numerator(numeratormrdts).denominator(denominatormrdts).build();
            try {
                verificationDataDao.save(mrdts);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + mrdts); 

           JSONObject jsonObjectTDF = null;
            if (!datajson.isNull("efv")) {
                jsonObjectTDF = datajson.getJSONObject("efv");
            }             
            
            String countytdf = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountytdf = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilitytdf = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedtdf = jsonObjectTDF.isNull("stock_facilityTDF") ? "" : jsonObjectTDF.getString("stock_facilityTDF");
            Integer qtyDardispensetdf = getIntegerFromJsonKey(jsonObjectTDF, "dispensed_darTDF");
            Integer qtycdrrdispensedtdf = getIntegerFromJsonKey(jsonObjectTDF, "dispensed_cdrrTDF");
            Integer qtydhisdispensedtdf = getIntegerFromJsonKey(jsonObjectTDF, "dispensed_khis2TDF");
            Integer physicalCountCdrrtdf = getIntegerFromJsonKey(jsonObjectTDF, "count_cdrrTDF");
            Integer physicalCountdhistdf = getIntegerFromJsonKey(jsonObjectTDF, "count_khis2TDF");
            Integer varianceDarMsftdf = getIntegerFromJsonKey(datajson,"darmsfTDF");
            Integer varianceMsfDhistdf = getIntegerFromJsonKey(datajson,"khismsfdispensedTDF");
            Integer varianceCdrrDhistdf = getIntegerFromJsonKey(datajson, "khismsfcountTDF");
            
            Integer numeratortdf = getIntegerFromJsonKey(datajson,"numeratorTDF");
            Integer denominatortdf = getIntegerFromJsonKey(datajson,"denominatorTDF");
            

            VerificationDataModel tdf = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countytdf).facility(facilitytdf).subcounty(subcountytdf).product("TDF/3TC/EFV adult").orgUnit(orgUnit)
                    .stocked(stockedtdf).qtyDardispense(qtyDardispensetdf).qtycdrrdispensed(qtycdrrdispensedtdf).qtydhisdispensed(qtydhisdispensedtdf)
                    .physicalCountCdrr(physicalCountCdrrtdf).physicalCountdhis(physicalCountdhistdf)
                    .varianceDarMsf(varianceDarMsftdf).varianceCdrrDhis(varianceCdrrDhistdf).varianceMsfDhis(varianceMsfDhistdf)
                    .numerator(numeratortdf).denominator(denominatortdf).build();
            try {
                verificationDataDao.save(tdf);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + tdf);             


           JSONObject jsonObjectABC = null;
            if (!datajson.isNull("abc")) {
                jsonObjectABC = datajson.getJSONObject("abc");
            }             
            
            String countyabc = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyabc = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityabc = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedabc = jsonObjectABC.isNull("stock_facilityABC") ? "" : jsonObjectABC.getString("stock_facilityABC");
            Integer qtyDardispenseabc = getIntegerFromJsonKey(jsonObjectABC, "dispensed_darABC");
            Integer qtycdrrdispensedabc = getIntegerFromJsonKey(jsonObjectABC, "dispensed_cdrrABC");
            Integer qtydhisdispensedabc = getIntegerFromJsonKey(jsonObjectABC, "dispensed_khis2ABC");
            Integer physicalCountCdrrabc = getIntegerFromJsonKey(jsonObjectABC, "count_cdrrABC");
            Integer physicalCountdhisabc = getIntegerFromJsonKey(jsonObjectABC, "count_khis2ABC");
            Integer varianceDarMsfabc = getIntegerFromJsonKey(datajson,"darmsfABC");
            Integer varianceMsfDhisabc = getIntegerFromJsonKey(datajson,"khismsfdispensedABC");
            Integer varianceCdrrDhisabc = getIntegerFromJsonKey(datajson, "khismsfcountABC");
            
            Integer numeratorabc = getIntegerFromJsonKey(datajson,"numeratorABC");
            Integer denominatorabc = getIntegerFromJsonKey(datajson,"denominatorABC");
            

            VerificationDataModel abc = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyabc).facility(facilityabc).subcounty(subcountyabc).product("ABC/3TC paed").orgUnit(orgUnit)
                    .stocked(stockedabc).qtyDardispense(qtyDardispenseabc).qtycdrrdispensed(qtycdrrdispensedabc).qtydhisdispensed(qtydhisdispensedabc)
                    .physicalCountCdrr(physicalCountCdrrabc).physicalCountdhis(physicalCountdhisabc)
                    .varianceDarMsf(varianceDarMsfabc).varianceCdrrDhis(varianceCdrrDhisabc).varianceMsfDhis(varianceMsfDhisabc)
                    .numerator(numeratorabc).denominator(denominatorabc).build();
            try {
                verificationDataDao.save(abc);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + abc);             
 

           JSONObject jsonObjectHIV = null;
            if (!datajson.isNull("hivs")) {
                jsonObjectHIV = datajson.getJSONObject("hivs");
            }             
            
            String countyhiv = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyhiv = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityhiv = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedhiv = jsonObjectHIV.isNull("stock_facilityHIV") ? "" : jsonObjectHIV.getString("stock_facilityHIV");
            Integer qtyDardispensehiv = getIntegerFromJsonKey(jsonObjectHIV, "dispensed_darHIV");
            Integer qtycdrrdispensedhiv = getIntegerFromJsonKey(jsonObjectHIV, "dispensed_cdrrHIV");
            Integer qtydhisdispensedhiv = getIntegerFromJsonKey(jsonObjectHIV, "dispensed_khis2HIV");
            Integer physicalCountCdrrhiv = getIntegerFromJsonKey(jsonObjectHIV, "count_cdrrHIV");
            Integer physicalCountdhishiv = getIntegerFromJsonKey(jsonObjectHIV, "count_khis2HIV");
            Integer varianceDarMsfhiv = getIntegerFromJsonKey(datajson,"darmsfHIV");
            Integer varianceMsfDhishiv = getIntegerFromJsonKey(datajson,"khismsfdispensedHIV");
            Integer varianceCdrrDhishiv = getIntegerFromJsonKey(datajson, "khismsfcountHIV");
            
            Integer numeratorhiv = getIntegerFromJsonKey(datajson,"numeratorHIV");
            Integer denominatorhiv = getIntegerFromJsonKey(datajson,"denominatorHIV");
            

            VerificationDataModel hiv = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyhiv).facility(facilityhiv).subcounty(subcountyhiv).product("HIV screening test").orgUnit(orgUnit)
                    .stocked(stockedhiv).qtyDardispense(qtyDardispensehiv).qtycdrrdispensed(qtycdrrdispensedhiv).qtydhisdispensed(qtydhisdispensedhiv)
                    .physicalCountCdrr(physicalCountCdrrhiv).physicalCountdhis(physicalCountdhishiv)
                    .varianceDarMsf(varianceDarMsfhiv).varianceCdrrDhis(varianceCdrrDhishiv).varianceMsfDhis(varianceMsfDhishiv)
                    .numerator(numeratorhiv).denominator(denominatorhiv).build();
            try {
                verificationDataDao.save(hiv);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + hiv);  

           JSONObject jsonObjectDTG = null;
            if (!datajson.isNull("dtg")) {
                jsonObjectDTG = datajson.getJSONObject("dtg");
            }             
            
            String countydtg = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountydtg = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilitydtg = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockeddtg = jsonObjectDTG.isNull("stock_facilityDTG") ? "" : jsonObjectDTG.getString("stock_facilityDTG");
            Integer qtyDardispensedtg = getIntegerFromJsonKey(jsonObjectDTG, "dispensed_darDTG");
            Integer qtycdrrdispenseddtg = getIntegerFromJsonKey(jsonObjectDTG, "dispensed_cdrrDTG");
            Integer qtydhisdispenseddtg = getIntegerFromJsonKey(jsonObjectDTG, "dispensed_khis2DTG");
            Integer physicalCountCdrrdtg = getIntegerFromJsonKey(jsonObjectDTG, "count_cdrrDTG");
            Integer physicalCountdhisdtg = getIntegerFromJsonKey(jsonObjectDTG, "count_khis2DTG");
            Integer varianceDarMsfdtg = getIntegerFromJsonKey(datajson,"darmsfDTG");
            Integer varianceMsfDhisdtg = getIntegerFromJsonKey(datajson,"khismsfdispensedDTG");
            Integer varianceCdrrDhisdtg = getIntegerFromJsonKey(datajson, "khismsfcountDTG");
            
            Integer numeratordtg = getIntegerFromJsonKey(datajson,"numeratorDTG");
            Integer denominatordtg = getIntegerFromJsonKey(datajson,"denominatorDTG");
            

            VerificationDataModel dtg = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countydtg).facility(facilitydtg).subcounty(subcountydtg).product("TDF/3TC/DTG adult").orgUnit(orgUnit)
                    .stocked(stockeddtg).qtyDardispense(qtyDardispensedtg).qtycdrrdispensed(qtycdrrdispenseddtg).qtydhisdispensed(qtydhisdispenseddtg)
                    .physicalCountCdrr(physicalCountCdrrdtg).physicalCountdhis(physicalCountdhisdtg)
                    .varianceDarMsf(varianceDarMsfdtg).varianceCdrrDhis(varianceCdrrDhisdtg).varianceMsfDhis(varianceMsfDhisdtg)
                    .numerator(numeratordtg).denominator(denominatordtg).build();
            try {
                verificationDataDao.save(dtg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + dtg); 

           JSONObject jsonObjectDMPA = null;
            if (!datajson.isNull("dmpa")) {
                jsonObjectDMPA = datajson.getJSONObject("dmpa");
            }             
            
            String countydmpa = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountydmpa = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilitydmpa = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockeddmpa = jsonObjectDMPA.isNull("stock_facilityDMPA") ? "" : jsonObjectDMPA.getString("stock_facilityDMPA");
            Integer qtyDardispensedmpa = getIntegerFromJsonKey(jsonObjectDMPA, "dispensed_darDMPA");
            Integer qtycdrrdispenseddmpa = getIntegerFromJsonKey(jsonObjectDMPA, "dispensed_cdrrDMPA");
            Integer qtydhisdispenseddmpa = getIntegerFromJsonKey(jsonObjectDMPA, "dispensed_khis2DMPA");
            Integer physicalCountCdrrdmpa = getIntegerFromJsonKey(jsonObjectDMPA, "count_cdrrDMPA");
            Integer physicalCountdhisdmpa = getIntegerFromJsonKey(jsonObjectDMPA, "count_khis2DMPA");
            Integer varianceDarMsfdmpa = getIntegerFromJsonKey(datajson,"darmsfDMPA");
            Integer varianceMsfDhisdmpa = getIntegerFromJsonKey(datajson,"khismsfdispensedDMPA");
            Integer varianceCdrrDhisdmpa = getIntegerFromJsonKey(datajson, "khismsfcountDMPA");
            
            Integer numeratordmpa = getIntegerFromJsonKey(datajson,"numeratorDMPA");
            Integer denominatordmpa = getIntegerFromJsonKey(datajson,"denominatorDMPA");
            

            VerificationDataModel dmpa = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countydmpa).facility(facilitydmpa).subcounty(subcountydmpa).product("DMPA").orgUnit(orgUnit)
                    .stocked(stockeddmpa).qtyDardispense(qtyDardispensedmpa).qtycdrrdispensed(qtycdrrdispenseddmpa).qtydhisdispensed(qtydhisdispenseddmpa)
                    .physicalCountCdrr(physicalCountCdrrdmpa).physicalCountdhis(physicalCountdhisdmpa)
                    .varianceDarMsf(varianceDarMsfdmpa).varianceCdrrDhis(varianceCdrrDhisdmpa).varianceMsfDhis(varianceMsfDhisdmpa)
                    .numerator(numeratordmpa).denominator(denominatordmpa).build();
            try {
                verificationDataDao.save(dmpa);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + dmpa); 

           JSONObject jsonObjectROD1 = null;
            if (!datajson.isNull("rod1")) {
                jsonObjectROD1 = datajson.getJSONObject("rod1");
            }             
            
            String countyrod1 = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyrod1 = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityrod1 = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedrod1 = jsonObjectROD1.isNull("stock_facility1rod") ? "" : jsonObjectROD1.getString("stock_facility1rod");
            Integer qtyDardispenserod1 = getIntegerFromJsonKey(jsonObjectROD1, "dispensed_dar1rod");
            Integer qtycdrrdispensedrod1 = getIntegerFromJsonKey(jsonObjectROD1, "dispensed_cdrr1rod");
            Integer qtydhisdispensedrod1 = getIntegerFromJsonKey(jsonObjectROD1, "dispensed_khis21rod");
            Integer physicalCountCdrrrod1 = getIntegerFromJsonKey(jsonObjectROD1, "count_cdrr1rod");
            Integer physicalCountdhisrod1 = getIntegerFromJsonKey(jsonObjectROD1, "count_khis21rod");
            Integer varianceDarMsfrod1 = getIntegerFromJsonKey(datajson,"darmsf1rod");
            Integer varianceMsfDhisrod1 = getIntegerFromJsonKey(datajson,"khismsfdispensed1rod");
            Integer varianceCdrrDhisrod1 = getIntegerFromJsonKey(datajson, "khismsfcount1rod");
            
            Integer numeratorrod1 = getIntegerFromJsonKey(datajson,"numerator1rod");
            Integer denominatorrod1 = getIntegerFromJsonKey(datajson,"denominator1rod");
            

            VerificationDataModel rod1 = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyrod1).facility(facilityrod1).subcounty(subcountyrod1).product("1 rod implant").orgUnit(orgUnit)
                    .stocked(stockedrod1).qtyDardispense(qtyDardispenserod1).qtycdrrdispensed(qtycdrrdispensedrod1).qtydhisdispensed(qtydhisdispensedrod1)
                    .physicalCountCdrr(physicalCountCdrrrod1).physicalCountdhis(physicalCountdhisrod1)
                    .varianceDarMsf(varianceDarMsfrod1).varianceCdrrDhis(varianceCdrrDhisrod1).varianceMsfDhis(varianceMsfDhisrod1)
                    .numerator(numeratorrod1).denominator(denominatorrod1).build();
            try {
                verificationDataDao.save(rod1);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + rod1);             

           JSONObject jsonObjectROD2 = null;
            if (!datajson.isNull("rod2")) {
                jsonObjectROD2 = datajson.getJSONObject("rod2");
            }             
            
            String countyrod2 = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyrod2 = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityrod2 = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedrod2 = jsonObjectROD2.isNull("stock_facility2rod") ? "" : jsonObjectROD2.getString("stock_facility2rod");
            Integer qtyDardispenserod2 = getIntegerFromJsonKey(jsonObjectROD2, "dispensed_dar2rod");
            Integer qtycdrrdispensedrod2 = getIntegerFromJsonKey(jsonObjectROD2, "dispensed_cdrr2rod");
            Integer qtydhisdispensedrod2 = getIntegerFromJsonKey(jsonObjectROD2, "dispensed_khis22rod");
            Integer physicalCountCdrrrod2 = getIntegerFromJsonKey(jsonObjectROD2, "count_cdrr2rod");
            Integer physicalCountdhisrod2 = getIntegerFromJsonKey(jsonObjectROD2, "count_khis22rod");
            Integer varianceDarMsfrod2 = getIntegerFromJsonKey(datajson,"darmsf2rod");
            Integer varianceMsfDhisrod2 = getIntegerFromJsonKey(datajson,"khismsfdispensed2rod");
            Integer varianceCdrrDhisrod2 = getIntegerFromJsonKey(datajson, "khismsfcount2rod");
            
            Integer numeratorrod2 = getIntegerFromJsonKey(datajson,"numerator2rod");
            Integer denominatorrod2 = getIntegerFromJsonKey(datajson,"denominator2rod");
            

            VerificationDataModel rod2 = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyrod2).facility(facilityrod2).subcounty(subcountyrod2).product("2 rod implant").orgUnit(orgUnit)
                    .stocked(stockedrod2).qtyDardispense(qtyDardispenserod2).qtycdrrdispensed(qtycdrrdispensedrod2).qtydhisdispensed(qtydhisdispensedrod2)
                    .physicalCountCdrr(physicalCountCdrrrod2).physicalCountdhis(physicalCountdhisrod2)
                    .varianceDarMsf(varianceDarMsfrod2).varianceCdrrDhis(varianceCdrrDhisrod2).varianceMsfDhis(varianceMsfDhisrod2)
                    .numerator(numeratorrod2).denominator(denominatorrod2).build();
            try {
                verificationDataDao.save(rod2);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + rod2);            
            
           JSONObject jsonObjectCOC = null;
            if (!datajson.isNull("coc")) {
                jsonObjectCOC = datajson.getJSONObject("coc");
            }
            
            
            String countycoc = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountycoc = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilitycoc = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedcoc = jsonObjectCOC.isNull("stock_facilityCOC") ? "" : jsonObjectCOC.getString("stock_facilityCOC");
            Integer qtyDardispensecoc = getIntegerFromJsonKey(jsonObjectCOC, "dispensed_darCOC");
            Integer qtycdrrdispensedcoc = getIntegerFromJsonKey(jsonObjectCOC, "dispensed_cdrrCOC");
            Integer qtydhisdispensedcoc = getIntegerFromJsonKey(jsonObjectCOC, "dispensed_khis2COC");
            Integer physicalCountCdrrcoc = getIntegerFromJsonKey(jsonObjectCOC, "count_cdrrCOC");
            Integer physicalCountdhiscoc = getIntegerFromJsonKey(jsonObjectCOC, "count_khis2COC");
            Integer varianceDarMsfcoc = getIntegerFromJsonKey(datajson,"darmsfCOC");
            Integer varianceMsfDhiscoc = getIntegerFromJsonKey(datajson,"khismsfdispensedCOC");
            Integer varianceCdrrDhiscoc = getIntegerFromJsonKey(datajson, "khismsfcountCOC");
            
            Integer numeratorcoc = getIntegerFromJsonKey(datajson,"numeratorCOC");
            Integer denominatorcoc = getIntegerFromJsonKey(datajson,"denominatorCOC");
            

            VerificationDataModel coc = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countycoc).facility(facilitycoc).subcounty(subcountycoc).product("COC").orgUnit(orgUnit)
                    .stocked(stockedcoc).qtyDardispense(qtyDardispensecoc).qtycdrrdispensed(qtycdrrdispensedcoc).qtydhisdispensed(qtydhisdispensedcoc)
                    .physicalCountCdrr(physicalCountCdrrcoc).physicalCountdhis(physicalCountdhiscoc)
                    .varianceDarMsf(varianceDarMsfcoc).varianceCdrrDhis(varianceCdrrDhiscoc).varianceMsfDhis(varianceMsfDhiscoc)
                    .numerator(numeratorcoc).denominator(denominatorcoc).build();
            try {
                verificationDataDao.save(coc);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + coc);            

           JSONObject jsonObjectIUCD = null;
            if (!datajson.isNull("iucd")) {
                jsonObjectIUCD = datajson.getJSONObject("iucd");
            }
            
            
            String countyiucd = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyiucd = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityiucd = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockediucd = jsonObjectIUCD.isNull("stock_facilityIUCD") ? "" : jsonObjectIUCD.getString("stock_facilityIUCD");
            Integer qtyDardispenseiucd = getIntegerFromJsonKey(jsonObjectIUCD, "dispensed_darIUCD");
            Integer qtycdrrdispensediucd = getIntegerFromJsonKey(jsonObjectIUCD, "dispensed_cdrrIUCD");
            Integer qtydhisdispensediucd = getIntegerFromJsonKey(jsonObjectIUCD, "dispensed_khis2IUCD");
            Integer physicalCountCdrriucd = getIntegerFromJsonKey(jsonObjectIUCD, "count_cdrrIUCD");
            Integer physicalCountdhisiucd = getIntegerFromJsonKey(jsonObjectIUCD, "count_khis2IUCD");
            Integer varianceDarMsfiucd = getIntegerFromJsonKey(datajson,"darmsfIUCD");
            Integer varianceMsfDhisiucd = getIntegerFromJsonKey(datajson,"khismsfdispensedIUCD");
            Integer varianceCdrrDhisiucd = getIntegerFromJsonKey(datajson, "khismsfcountIUCD");
            
            Integer numeratoriucd = getIntegerFromJsonKey(datajson,"numeratorIUCD");
            Integer denominatoriucd = getIntegerFromJsonKey(datajson,"denominatorIUCD");
            

            VerificationDataModel iucd = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyiucd).facility(facilityiucd).subcounty(subcountyiucd).product("IUCD").orgUnit(orgUnit)
                    .stocked(stockediucd).qtyDardispense(qtyDardispenseiucd).qtycdrrdispensed(qtycdrrdispensediucd).qtydhisdispensed(qtydhisdispensediucd)
                    .physicalCountCdrr(physicalCountCdrriucd).physicalCountdhis(physicalCountdhisiucd)
                    .varianceDarMsf(varianceDarMsfiucd).varianceCdrrDhis(varianceCdrrDhisiucd).varianceMsfDhis(varianceMsfDhisiucd)
                    .numerator(numeratoriucd).denominator(denominatoriucd).build();
            try {
                verificationDataDao.save(iucd);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + iucd); 
            

          
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
