/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller.dashboard;

import com.odkdhis.model.FormName;
import com.odkdhis.model.ServerConfig;
import com.odkdhis.model.dashboard.ProductAccountability;
import com.odkdhis.model.dashboard.ProductName;
import com.odkdhis.model.dashboard.ProductsMeetingSohExpectation;
import com.odkdhis.model.dashboard.SummaryData;
import com.odkdhis.util.CommonUtil;
import com.odkdhis.util.SampleDataPull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class ProductAccountabilityController extends CommonUtil {

    @GetMapping(value = {"/accountability"})
    public ModelAndView dashboard(@RequestParam(name = "product", required = false) String product, @RequestParam(name = "county", required = false) String county, @RequestParam(name = "fromDate", required = false) String fromDate, @RequestParam(name = "toDate", required = false) String toDate) throws ParseException {

        // Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-01");
        //Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31");
        LocalDate dateFrom = LocalDate.parse("2020-10-01");
        LocalDate dateTo = LocalDate.parse("2020-12-31");

        String defaultCounty = "Kitui";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if (county != null) {
            defaultCounty = county;
            //dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
            //dateTo = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
            dateFrom = LocalDate.parse(fromDate);
            dateTo = LocalDate.parse(toDate);
        }

        List<ProductAccountability> productsAccountability = accountabilityDao.getProductAccountabilities(null, product, defaultCounty, dateFrom, dateTo);

        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("accountabilityData", productsAccountability);
        SummaryData summaryData = new SummaryData();
        modelAndView.addObject("summaryData", summaryData);
        modelAndView.addObject("expectedData", new ProductAccountability());
        ServerConfig config = new ServerConfig();
        modelAndView.addObject("config", config);
        modelAndView.addObject("summaryOrgUnit", (defaultCounty == null || orgDao.findByOrgunitCode(defaultCounty) == null) ? "NOT PROVIDED" : "SUMMARY SCORES FOR : " + orgDao.findByOrgunitCode(defaultCounty).getName() + " Between " + format.format(dateFrom) + " And " + format.format(dateTo));
        modelAndView.setViewName("accountabilityRevised");
        return modelAndView;
    }

}
