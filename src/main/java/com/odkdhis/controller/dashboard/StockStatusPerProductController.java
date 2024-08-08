/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller.dashboard;

import com.odkdhis.model.dashboard.FacilityStock;
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
public class StockStatusPerProductController extends CommonUtil {
    
    @GetMapping(value = {"/stockstatusperproduct"})
    public ModelAndView dashboard() throws ParseException {
    
            Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-01");
            Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-31");
            String county="Kitui";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("stockstatusperproductsearch", new FacilityStock());
        modelAndView.addObject("stockstatusperproductdata", inventorymgtDao.getFacilityStock(county, dateFrom, dateTo));
        //modelAndView.addObject("summaryOrgUnit",  "NOT PROVIDED" );
        modelAndView.addObject("summaryOrgUnit", "stock status analysis per product : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        modelAndView.setViewName("stockstatusperproduct");
        return modelAndView;
    }

    
        @GetMapping(value = {"/stockstatusperproductfilter"})
    public ModelAndView dashboardfiltered(@RequestParam(name = "county", required = false) String county, @RequestParam(name = "fromDate", required = false) String fromDate, @RequestParam(name = "toDate", required = false) String toDate) throws ParseException {
    
            Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        List<FacilityStock> lst = new ArrayList<>();
        if (county != null && fromDate != null && toDate != null) {
            lst = inventorymgtDao.getFacilityStock(county, dateFrom, dateTo);
        }
        
        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("stockstatusperproductsearch", new FacilityStock());
        modelAndView.addObject("stockstatusperproductdata", lst);
        modelAndView.addObject("summaryOrgUnit", "stock status analysis per product : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        //modelAndView.addObject("summaryOrgUnit", (county == null) ? "NOT PROVIDED" : "Stock Status - Report per Product at County level : " + county +" Between "+format.format(dateFrom)+ " and "+format.format(dateTo));
        modelAndView.setViewName("stockstatusperproduct");
        return modelAndView;
    }
}
