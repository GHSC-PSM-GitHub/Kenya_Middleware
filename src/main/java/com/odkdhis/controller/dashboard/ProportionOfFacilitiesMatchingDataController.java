/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller.dashboard;

import com.odkdhis.model.dashboard.FacilitiesWithMatchingData;
import com.odkdhis.util.CommonUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Paul Omboi
 */
@Controller
public class ProportionOfFacilitiesMatchingDataController extends CommonUtil {
    
    @GetMapping(value = {"/proportionoffacilitiesmatchingdata"})
    public ModelAndView dashboard() throws ParseException {
    
            Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-01");
            Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-31");
            String county="Kitui";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("facilitieswithmatchingdatasearch", new FacilitiesWithMatchingData());
        modelAndView.addObject("inventoryMatchingData", inventorymgtDao.getFacilitiesWithMatchingData(county, dateFrom, dateTo));
        //modelAndView.addObject("summaryOrgUnit",  "NOT PROVIDED" );
        modelAndView.addObject("summaryOrgUnit", "% of Facilities with matching AMC and Stock card balance : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        modelAndView.setViewName("proportionoffacilitiesmatchingdata");
        return modelAndView;
    }

    
        @GetMapping(value = {"/facilitieswithmatchingdatafilter"})
    public ModelAndView dashboardfiltered(@RequestParam(name = "county", required = false) String county, @RequestParam(name = "fromDate", required = false) String fromDate, @RequestParam(name = "toDate", required = false) String toDate) throws ParseException {
    
            Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        List<FacilitiesWithMatchingData> lst = new ArrayList<>();
        if (county != null && fromDate != null && toDate != null) {
            lst = inventorymgtDao.getFacilitiesWithMatchingData(county, dateFrom, dateTo);
        }
        
        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("facilitieswithmatchingdatasearch", new FacilitiesWithMatchingData());
        modelAndView.addObject("inventoryMatchingData", lst);
        modelAndView.addObject("summaryOrgUnit", "% Facilities with matching AMC and Stock Balance : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        //modelAndView.addObject("summaryOrgUnit", (county == null) ? "NOT PROVIDED" : "Proportion of facilities with matching data : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        modelAndView.setViewName("proportionoffacilitiesmatchingdata");
        return modelAndView;
    }
}
