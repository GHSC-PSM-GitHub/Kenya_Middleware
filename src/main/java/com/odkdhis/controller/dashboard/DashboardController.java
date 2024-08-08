/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller.dashboard;

import com.odkdhis.model.ServerConfig;
import com.odkdhis.model.dashboard.PeriodType;
import static com.odkdhis.model.dashboard.PeriodType.THIS_QUARTER;
import com.odkdhis.model.dashboard.SummaryData;
import com.odkdhis.util.CommonUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ssvmoh
 */
@Controller
public class DashboardController extends CommonUtil {

    @GetMapping(value = {"/filtered"})
    public ModelAndView dashboard(@RequestParam(name = "orgUnitCode", required = true) String orgUnitCode, @RequestParam(name = "fromDate", required = true) String fromDate, @RequestParam(name = "toDate", required = true) String toDate) throws ParseException {
  
        Date dateFrom=new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);  
        Date dateTo=new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        
        ModelAndView modelAndView = getModelAndView();
        modelAndView.addObject("availableData", dataDao.getDisplayDataList(orgUnitCode, dateFrom, dateTo));
        SummaryData summaryData = new SummaryData();
        modelAndView.addObject("summaryData", summaryData);
        ServerConfig config = new ServerConfig();
        modelAndView.addObject("config", config);
        modelAndView.addObject("summaryOrgUnit", (orgUnitCode == null || orgDao.findByOrgunitCode(orgUnitCode) == null) ? "NOT PROVIDED" : "SUMMARY SCORES FOR : "+orgDao.findByOrgunitCode(orgUnitCode).getName()+" Between "+format.format(dateFrom)+" And "+format.format(dateTo));
        modelAndView.setViewName("dashboard");
        return modelAndView;
    }

    private List<Date> getDates(PeriodType type) {
        // Assume the week starts on Sunday
        List<Date> dates = new ArrayList<>();

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        if (type == THIS_QUARTER) {
            if (month < 3) {
                Calendar start = Calendar.getInstance();
                start.set(year, Calendar.JANUARY, 01);
                dates.add(start.getTime());
                Calendar end = Calendar.getInstance();
                end.set(year, Calendar.MARCH, 31);
                dates.add(end.getTime());
            } else if (month > 2 && month < 6) {
                Calendar start = Calendar.getInstance();
                start.set(year, Calendar.APRIL, 01);
                dates.add(start.getTime());
                Calendar end = Calendar.getInstance();
                end.set(year, Calendar.JUNE, 30);
                dates.add(end.getTime());
            } else if (month > 5 && month < 9) {
                Calendar start = Calendar.getInstance();
                start.set(year, Calendar.JULY, 01);
                dates.add(start.getTime());
                Calendar end = Calendar.getInstance();
                end.set(year, Calendar.SEPTEMBER, 30);
                dates.add(end.getTime());
            } else {
                Calendar start = Calendar.getInstance();
                start.set(year, Calendar.OCTOBER, 01);
                dates.add(start.getTime());
                Calendar end = Calendar.getInstance();
                end.set(year, Calendar.DECEMBER, 31);
                dates.add(end.getTime());
            }
        }
        return dates;
    }
}
