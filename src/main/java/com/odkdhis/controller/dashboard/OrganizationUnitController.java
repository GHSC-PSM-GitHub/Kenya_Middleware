/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller.dashboard;

import com.odkdhis.dao.dashboard.OrganizationUnitDaoImpl;
import com.odkdhis.model.dashboard.OrganizationUnit;
import com.odkdhis.util.CommonUtil;
import com.odkdhis.util.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ssvmoh
 */
@Controller
public class OrganizationUnitController extends CommonUtil {

    @Autowired
    private OrganizationUnitDaoImpl orgDao;

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"code", "id", "level", "name", "description", "path"};

    @PostMapping(value = "/uploadOrganizationUnits")
    public @ResponseBody
    Map<String, ? extends Object> uploadOrganizationUnits() throws Exception {

        try {
            OrganizationUnit org = orgDao.getOrganizationUnits(null, null, null, null, null).size() < 1 ? null : orgDao.getOrganizationUnits(null, null, null, null, null).get(0);
            if (org == null) {
                File csvFile = getFileFromResourceFolder("orgunits", "metadata.csv");
                InputStream in = new FileInputStream(csvFile);
                csvToOrganizationUnits(in);
            }
            return Response.failure("File Uploaded!!");

        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("An error Occured!!!");

        }

    }

    private void csvToOrganizationUnits(InputStream is) throws Exception {
        try ( BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));  CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<OrganizationUnit> counties = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            OrganizationUnit kenya = new OrganizationUnit("Kenya", "KenyaSSV", "CLmPcJmOc90", 1, null, null, null);
            orgDao.save(kenya);

            log.info("***************************  ADDING COUNTIES  ******************************************");
            csvRecords.forEach(csvRecord -> {
                String facilityType = csvRecord.get("description");
                if (facilityType.equalsIgnoreCase("County")) {
                    OrganizationUnit county = new OrganizationUnit(csvRecord.get("name"), csvRecord.get("code"), csvRecord.get("id"), 1, null, null, kenya);
                    counties.add(county);
                }

            });
            OrganizationUnit kenya_update = orgDao.findByUid("CLmPcJmOc90");
            kenya_update.addChildren(counties);
            orgDao.save(kenya_update);

            log.info("***************************  ADDING SUB COUNTIES  ******************************************");
            csvRecords.forEach(csvRecord -> {
                String facilityType = csvRecord.get("description");
                if (facilityType.equalsIgnoreCase("Sub County")) {
                    String[] uidPath = csvRecord.get("path").split("/");
                    String parentUID = uidPath[uidPath.length - 2];
                    OrganizationUnit countyParent = orgDao.findByUid(parentUID);
                    OrganizationUnit subCounty = new OrganizationUnit(csvRecord.get("name"), csvRecord.get("code"), csvRecord.get("id"), 1, countyParent.getId(), null, null);
                    countyParent.addChild(subCounty);
                    try {
                        orgDao.save(countyParent);
                    } catch (Exception ex) {
                        Logger.getLogger(OrganizationUnitController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });

            log.info("***************************  ADDING FACILITIES (THIS MIGHT TAKE A FEW MINUTES, BE PATIENT)  ******************************************");
            csvRecords.forEach(csvRecord -> {
                String facilityType = csvRecord.get("description");
                if (facilityType.equalsIgnoreCase("Facility")) {
                    String[] uidPath = csvRecord.get("path").split("/");
                    String parentUID = uidPath[uidPath.length - 2];
                    OrganizationUnit subCountyParent = orgDao.findByUid(parentUID);
                    OrganizationUnit facility = new OrganizationUnit(csvRecord.get("name"), csvRecord.get("code"), csvRecord.get("id"), 1, subCountyParent.getParent().getId(), subCountyParent.getId(), null);
                    subCountyParent.addChild(facility);
                    try {
                        orgDao.save(subCountyParent);
                    } catch (Exception ex) {
                        Logger.getLogger(OrganizationUnitController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
