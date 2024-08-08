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
public class CommodityVerificationData2 extends CommonUtil {

    @Autowired
    private VerificationDataDaoImpl verificationDataDao;

    // THIS Time is selected so that all the day's data can be aggregated ready to be send to DHIS 2
  @Scheduled(cron ="10 04 * * * ?", zone = "Africa/Nairobi")
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
            JSONObject jsonObjectAl6s = null;
            if (!datajson.isNull("al6s")) {
                jsonObjectAl6s = datajson.getJSONObject("al6s");
            }
            
            //Integer empty = 0;
            
            String county = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcounty = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facility = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");
            
            Integer numerator = getIntegerFromJsonKey(datajson,"tnumerator");
            Integer denominator = getIntegerFromJsonKey(datajson,"tdenominator");
            Integer score = getIntegerFromJsonKey(datajson,"tscore");
            
            String stocked = jsonObjectAl6s.isNull("stock_facilityAL6s") ? "" : jsonObjectAl6s.getString("stock_facilityAL6s");
            Integer qtyDardispense = getIntegerFromJsonKey(jsonObjectAl6s, "dispensed_darAL6s");
            Integer qtycdrrdispensed = getIntegerFromJsonKey(jsonObjectAl6s, "dispensed_cdrrAL6s");
            Integer qtydhisdispensed = getIntegerFromJsonKey(jsonObjectAl6s, "dispensed_khis2AL6s");
            Integer physicalCountCdrr = getIntegerFromJsonKey(jsonObjectAl6s, "count_cdrrAL6s");
            Integer physicalCountdhis = getIntegerFromJsonKey(jsonObjectAl6s, "count_khis2AL6s");
            Integer varianceDarMsf = getIntegerFromJsonKey(datajson,"darmsfAL6s");
            Integer varianceMsfDhis = getIntegerFromJsonKey(datajson,"khismsfdispensedAL6s");
            Integer varianceCdrrDhis = getIntegerFromJsonKey(datajson, "khismsfcountAL6s");           
            

            VerificationDataModel al6s = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(county).facility(facility).subcounty(subcounty).product("AL 6s").orgUnit(orgUnit)
                    .stocked(stocked).qtyDardispense(qtyDardispense).qtycdrrdispensed(qtycdrrdispensed).qtydhisdispensed(qtydhisdispensed)
                    .physicalCountCdrr(physicalCountCdrr).physicalCountdhis(physicalCountdhis)
                    .varianceDarMsf(varianceDarMsf).varianceCdrrDhis(varianceCdrrDhis).varianceMsfDhis(varianceMsfDhis)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(al6s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al6s);
            
      
            
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
            
            

            VerificationDataModel al24s = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyal24s).facility(facilityal24s).subcounty(subcountyal24s).product("AL 24s").orgUnit(orgUnit)
                    .stocked(stockedal24s).qtyDardispense(qtyDardispenseal24s).qtycdrrdispensed(qtycdrrdispensedal24s).qtydhisdispensed(qtydhisdispensedal24s)
                    .physicalCountCdrr(physicalCountCdrral24s).physicalCountdhis(physicalCountdhisal24s)
                    .varianceDarMsf(varianceDarMsfal24s).varianceCdrrDhis(varianceCdrrDhisal24s).varianceMsfDhis(varianceMsfDhisal24s)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(al24s);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + al24s);               
            

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
            
            

            VerificationDataModel artInj = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyartInj).facility(facilityartInj).subcounty(subcountyartInj).product("artesunate inj").orgUnit(orgUnit)
                    .stocked(stockedartInj).qtyDardispense(qtyDardispenseartInj).qtycdrrdispensed(qtycdrrdispensedartInj).qtydhisdispensed(qtydhisdispensedartInj)
                    .physicalCountCdrr(physicalCountCdrrartInj).physicalCountdhis(physicalCountdhisartInj)
                    .varianceDarMsf(varianceDarMsfartInj).varianceCdrrDhis(varianceCdrrDhisartInj).varianceMsfDhis(varianceMsfDhisartInj)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(artInj);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + artInj); 
            
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
        
            

            VerificationDataModel mrdts = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countymrdts).facility(facilitymrdts).subcounty(subcountymrdts).product("malaria RDTs").orgUnit(orgUnit)
                    .stocked(stockedmrdts).qtyDardispense(qtyDardispensemrdts).qtycdrrdispensed(qtycdrrdispensedmrdts).qtydhisdispensed(qtydhisdispensedmrdts)
                    .physicalCountCdrr(physicalCountCdrrmrdts).physicalCountdhis(physicalCountdhismrdts)
                    .varianceDarMsf(varianceDarMsfmrdts).varianceCdrrDhis(varianceCdrrDhismrdts).varianceMsfDhis(varianceMsfDhismrdts)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(mrdts);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + mrdts);            
            
            

           JSONObject jsonObjectLlins = null;
            if (!datajson.isNull("llins")) {
                jsonObjectLlins = datajson.getJSONObject("llins");
            }             
            
            String countyLLINs = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyLLINs = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityLLINs = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedLLINs = jsonObjectLlins.isNull("stock_facilityLLINs") ? "" : jsonObjectLlins.getString("stock_facilityLLINs");
            Integer qtyDardispenseLLINs = getIntegerFromJsonKey(jsonObjectLlins, "dispensed_darLLINs");
            Integer qtycdrrdispensedLLINs = getIntegerFromJsonKey(jsonObjectLlins, "dispensed_cdrrLLINs2");
            Integer qtydhisdispensedLLINs = getIntegerFromJsonKey(jsonObjectLlins, "dispensed_khis2LLINs");
            Integer physicalCountCdrrLLINs = getIntegerFromJsonKey(jsonObjectLlins, "count_cdrrLLINs");
            Integer physicalCountdhisLLINs = getIntegerFromJsonKey(jsonObjectLlins, "count_khis2LLINs");
            Integer varianceDarMsfLLINs = getIntegerFromJsonKey(datajson,"darmsfLLINs");
            Integer varianceMsfDhisLLINs = getIntegerFromJsonKey(datajson,"khismsfdispensedLLINs");
            Integer varianceCdrrDhisLLINs = getIntegerFromJsonKey(datajson, "khismsfcountLLINs");
            
            

            VerificationDataModel llins = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyLLINs).facility(facilityLLINs).subcounty(subcountyLLINs).product("LLINs").orgUnit(orgUnit)
                    .stocked(stockedLLINs).qtyDardispense(qtyDardispenseLLINs).qtycdrrdispensed(qtycdrrdispensedLLINs).qtydhisdispensed(qtydhisdispensedLLINs)
                    .physicalCountCdrr(physicalCountCdrrLLINs).physicalCountdhis(physicalCountdhisLLINs)
                    .varianceDarMsf(varianceDarMsfLLINs).varianceCdrrDhis(varianceCdrrDhisLLINs).varianceMsfDhis(varianceMsfDhisLLINs)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(llins);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + llins);                       
            
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
            

            VerificationDataModel dmpa = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countydmpa).facility(facilitydmpa).subcounty(subcountydmpa).product("DMPA IM").orgUnit(orgUnit)
                    .stocked(stockeddmpa).qtyDardispense(qtyDardispensedmpa).qtycdrrdispensed(qtycdrrdispenseddmpa).qtydhisdispensed(qtydhisdispenseddmpa)
                    .physicalCountCdrr(physicalCountCdrrdmpa).physicalCountdhis(physicalCountdhisdmpa)
                    .varianceDarMsf(varianceDarMsfdmpa).varianceCdrrDhis(varianceCdrrDhisdmpa).varianceMsfDhis(varianceMsfDhisdmpa)
                    .numerator(numerator).denominator(denominator).score(score).build();
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
            
            

            VerificationDataModel rod1 = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyrod1).facility(facilityrod1).subcounty(subcountyrod1).product("1 rod implant").orgUnit(orgUnit)
                    .stocked(stockedrod1).qtyDardispense(qtyDardispenserod1).qtycdrrdispensed(qtycdrrdispensedrod1).qtydhisdispensed(qtydhisdispensedrod1)
                    .physicalCountCdrr(physicalCountCdrrrod1).physicalCountdhis(physicalCountdhisrod1)
                    .varianceDarMsf(varianceDarMsfrod1).varianceCdrrDhis(varianceCdrrDhisrod1).varianceMsfDhis(varianceMsfDhisrod1)
                    .numerator(numerator).denominator(denominator).score(score).build();
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
            

            

            VerificationDataModel rod2 = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyrod2).facility(facilityrod2).subcounty(subcountyrod2).product("2 rod implant").orgUnit(orgUnit)
                    .stocked(stockedrod2).qtyDardispense(qtyDardispenserod2).qtycdrrdispensed(qtycdrrdispensedrod2).qtydhisdispensed(qtydhisdispensedrod2)
                    .physicalCountCdrr(physicalCountCdrrrod2).physicalCountdhis(physicalCountdhisrod2)
                    .varianceDarMsf(varianceDarMsfrod2).varianceCdrrDhis(varianceCdrrDhisrod2).varianceMsfDhis(varianceMsfDhisrod2)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(rod2);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + rod2);  
            
           JSONObject jsonObjectAmoxil = null;
            if (!datajson.isNull("adt")) {
                jsonObjectAmoxil = datajson.getJSONObject("adt");
            }             
            
            String countyADT = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyADT = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityADT = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedADT = jsonObjectAmoxil.isNull("stock_facilityADT") ? "" : jsonObjectAmoxil.getString("stock_facilityADT");
            //Integer qtyDardispenseADT = getIntegerFromJsonKey(jsonObjectAmoxil, "dispensed_darADT");
            Integer qtycdrrdispensedADT = getIntegerFromJsonKey(jsonObjectAmoxil, "dispensed_cdrrADT");
            Integer qtydhisdispensedADT = getIntegerFromJsonKey(jsonObjectAmoxil, "dispensed_khis2ADT");
            Integer physicalCountCdrrADT = getIntegerFromJsonKey(jsonObjectAmoxil, "count_cdrrADT");
            Integer physicalCountdhisADT = getIntegerFromJsonKey(jsonObjectAmoxil, "count_khis2ADT");
            Integer varianceDarMsfADT = getIntegerFromJsonKey(datajson,"darmsfADT");
            Integer varianceMsfDhisADT = getIntegerFromJsonKey(datajson,"khismsfdispensedADT");
            Integer varianceCdrrDhisADT = getIntegerFromJsonKey(datajson, "khismsfcountADT");
            
            

            VerificationDataModel ADT = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyADT).facility(facilityADT).subcounty(subcountyADT).product("Amoxicillin Dispersible Tablets 250mg").orgUnit(orgUnit)
                    .stocked(stockedADT).qtyDardispense(0).qtycdrrdispensed(qtycdrrdispensedADT).qtydhisdispensed(qtydhisdispensedADT)
                    .physicalCountCdrr(physicalCountCdrrADT).physicalCountdhis(physicalCountdhisADT)
                    .varianceDarMsf(varianceDarMsfADT).varianceCdrrDhis(varianceCdrrDhisADT).varianceMsfDhis(varianceMsfDhisADT)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(ADT);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + ADT); 

           JSONObject jsonObjectOxytocin = null;
            if (!datajson.isNull("oxytocin")) {
                jsonObjectOxytocin = datajson.getJSONObject("oxytocin");
            }             
            
            String countyOxytocin = jsonObjectCountyFacility.isNull("county_facility") ? "" : jsonObjectCountyFacility.getString("county_facility");
            String subcountyOxytocin = jsonObjectCountyFacility.isNull("subCounty") ? "" : jsonObjectCountyFacility.getString("subCounty");
            String facilityOxytocin = jsonObjectCountyFacility.isNull("facility") ? "" : jsonObjectCountyFacility.getString("facility");

            String stockedOxytocin = jsonObjectOxytocin.isNull("stock_facilityOxytocin") ? "" : jsonObjectOxytocin.getString("stock_facilityOxytocin");
            //Integer qtyDardispenseOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "dispensed_darOxytocin");
            Integer qtycdrrdispensedOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "dispensed_cdrrOxytocin");
            Integer qtydhisdispensedOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "dispensed_khis2Oxytocin");
            Integer physicalCountCdrrOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "count_cdrrOxytocin");
            Integer physicalCountdhisOxytocin = getIntegerFromJsonKey(jsonObjectOxytocin, "count_khis2Oxytocin");
            Integer varianceDarMsfOxytocin = getIntegerFromJsonKey(datajson,"darmsfOxytocin");
            Integer varianceMsfDhisOxytocin = getIntegerFromJsonKey(datajson,"khismsfdispensedOxytocin");
            Integer varianceCdrrDhisOxytocin = getIntegerFromJsonKey(datajson, "khismsfcountOxytocin");
            
            

            VerificationDataModel oxytocin = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyOxytocin).facility(facilityOxytocin).subcounty(subcountyOxytocin).product("oxytocin").orgUnit(orgUnit)
                    .stocked(stockedOxytocin).qtyDardispense(0).qtycdrrdispensed(qtycdrrdispensedOxytocin).qtydhisdispensed(qtydhisdispensedOxytocin)
                    .physicalCountCdrr(physicalCountCdrrOxytocin).physicalCountdhis(physicalCountdhisOxytocin)
                    .varianceDarMsf(varianceDarMsfOxytocin).varianceCdrrDhis(varianceCdrrDhisOxytocin).varianceMsfDhis(varianceMsfDhisOxytocin)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(oxytocin);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + oxytocin);             
            
            

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
            
            

            VerificationDataModel dtg = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countydtg).facility(facilitydtg).subcounty(subcountydtg).product("TDF/3TC/DTG adult").orgUnit(orgUnit)
                    .stocked(stockeddtg).qtyDardispense(qtyDardispensedtg).qtycdrrdispensed(qtycdrrdispenseddtg).qtydhisdispensed(qtydhisdispenseddtg)
                    .physicalCountCdrr(physicalCountCdrrdtg).physicalCountdhis(physicalCountdhisdtg)
                    .varianceDarMsf(varianceDarMsfdtg).varianceCdrrDhis(varianceCdrrDhisdtg).varianceMsfDhis(varianceMsfDhisdtg)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(dtg);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + dtg);             
            

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
            
            

            VerificationDataModel abc = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyabc).facility(facilityabc).subcounty(subcountyabc).product("ABC/3TC paed").orgUnit(orgUnit)
                    .stocked(stockedabc).qtyDardispense(qtyDardispenseabc).qtycdrrdispensed(qtycdrrdispensedabc).qtydhisdispensed(qtydhisdispensedabc)
                    .physicalCountCdrr(physicalCountCdrrabc).physicalCountdhis(physicalCountdhisabc)
                    .varianceDarMsf(varianceDarMsfabc).varianceCdrrDhis(varianceCdrrDhisabc).varianceMsfDhis(varianceMsfDhisabc)
                    .numerator(numerator).denominator(denominator).score(score).build();
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

            

            VerificationDataModel hiv = new VerificationDataModel.VerificationDataBuilder().instanceId(instanceId)
                    .dateCollected(getDateFromString(instanceCollectedDate)).county(countyhiv).facility(facilityhiv).subcounty(subcountyhiv).product("HIV screening test").orgUnit(orgUnit)
                    .stocked(stockedhiv).qtyDardispense(qtyDardispensehiv).qtycdrrdispensed(qtycdrrdispensedhiv).qtydhisdispensed(qtydhisdispensedhiv)
                    .physicalCountCdrr(physicalCountCdrrhiv).physicalCountdhis(physicalCountdhishiv)
                    .varianceDarMsf(varianceDarMsfhiv).varianceCdrrDhis(varianceCdrrDhishiv).varianceMsfDhis(varianceMsfDhishiv)
                    .numerator(numerator).denominator(denominator).score(score).build();
            try {
                verificationDataDao.save(hiv);
            } catch (Exception e) {
                log.info("Duplication");
            }

            log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " + hiv);       

          
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
