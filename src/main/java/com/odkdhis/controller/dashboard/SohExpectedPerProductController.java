/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller.dashboard;

import com.odkdhis.model.FormName;
import com.odkdhis.model.ServerConfig;
import com.odkdhis.model.dashboard.ProductName;
import com.odkdhis.model.dashboard.ProductsMeetingSohExpectation;
import com.odkdhis.model.dashboard.SummaryData;
import com.odkdhis.util.CommonUtil;
import com.odkdhis.util.SampleDataPull;
//import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
public class SohExpectedPerProductController extends CommonUtil {
    
    @GetMapping(value = {"/sohexpectedperproduct"})
    public ModelAndView dashboard() throws ParseException {
        
            Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-01");
            Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-31");
            String county="Kitui";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("expecteddataproductsearch", new ProductsMeetingSohExpectation());
        modelAndView.addObject("expecteddataproduct", accountabilityDao.getSohExpectedData(county, dateFrom, dateTo));
        //modelAndView.addObject("summaryOrgUnit",  "NOT PROVIDED" );
        modelAndView.addObject("summaryOrgUnit", "% of Items with actual soh=expected soh : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        modelAndView.setViewName("sohexpectedperproduct");
        return modelAndView;
    }

    
        @GetMapping(value = {"/sohexpectedperproductfilter"})
    public ModelAndView dashboardfiltered(@RequestParam(name = "county", required = false) String county, @RequestParam(name = "fromDate", required = false) String fromDate, @RequestParam(name = "toDate", required = false) String toDate) throws ParseException {

            Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            
        List<ProductsMeetingSohExpectation> lst = new ArrayList<>();
        if (county != null && fromDate != null && toDate != null) {
            lst = accountabilityDao.getSohExpectedData(county, dateFrom, dateTo);
        }

        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("expecteddataproductsearch", new ProductsMeetingSohExpectation());
        modelAndView.addObject("expecteddataproduct", lst);
        modelAndView.addObject("summaryOrgUnit", "% of Items with actual soh=expected soh : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        //modelAndView.addObject("summaryOrgUnit", (county == null) ? "NOT PROVIDED" : "Facilities meeting Expectation for : " + county +" Between "+dateFrom+ " and "+dateTo);
        modelAndView.setViewName("sohexpectedperproduct");
        return modelAndView;
    }
}
