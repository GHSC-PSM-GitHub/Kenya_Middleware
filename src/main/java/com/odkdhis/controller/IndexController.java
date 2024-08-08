/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.controller;

import com.odkdhis.model.ServerConfig;
import com.odkdhis.model.dashboard.OrganizationUnit;
import com.odkdhis.model.dashboard.SummaryData;
import com.odkdhis.util.CommonUtil;
import com.odkdhis.util.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author simiyu
 */
@Controller
public class IndexController extends CommonUtil {

    @GetMapping(value = {"/","/home"})
    public ModelAndView home() throws ParseException {
        
        Date dateFrom=new SimpleDateFormat("yyyy-MM-dd").parse("2020-10-01");  
        Date dateTo=new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-31");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String orgUnitCode="16860";
        
        
        ModelAndView modelAndView = getModelAndView();
        ServerConfig config = new ServerConfig();
        modelAndView.addObject("config", config);
        modelAndView.addObject("availableData", dataDao.getDisplayDataList(orgUnitCode, dateFrom, dateTo));
        SummaryData summaryData = new SummaryData();
        modelAndView.addObject("summaryData", summaryData);
        OrganizationUnit org = orgDao.findByOrgunitCode(orgUnitCode);
        modelAndView.addObject("summaryOrgUnit", "SUMMARY SCORES FOR : "+(Objects.isNull(org) ? "" : org.getName())+" Between "+format.format(dateFrom)+" And "+format.format(dateTo));
       //modelAndView.addObject("summaryOrgUnit", "SUMMARY SCORES FOR : "); 
       modelAndView.setViewName("dashboard");
        return modelAndView;
    }


    @PostMapping("/serverconfig")
    public String saveServerConfig(ServerConfig config) throws Exception {
        for (ServerConfig currentconf : configDao.getServerConfigs()) {
            configDao.delete(currentconf);
        }
        config.setDhisAuthPass(encrypt(config.getDhisAuthPass(), ENCRYPTION_SECRET));
        config.setOdkAuthPass(encrypt(config.getOdkAuthPass(), ENCRYPTION_SECRET));
        configDao.save(config);
        checkODKDHISServerHeartBeat();
        return "redirect:/";
    }

    @PostMapping("/sendData")
    public @ResponseBody
    Map<String, ? extends Object> sendDataToDHIS() throws Exception {

        if (!internetConnectionIsAvailable()) {
            return Response.failure("There's no internet Connection !!");
        }
        checkODKDHISServerHeartBeat();
        if (connDao.getConnectionStatus() != null
                && connDao.getConnectionStatus().getOdkConnection().equals("YES")
                && connDao.getConnectionStatus().getDhisConnection().equals("YES")) {
            SendDataFromODKToDHIS2();
        } else {
            return Response.failure("There's no Server Connection !!");
        }
        return Response.success("Successfully Done !!");
    }

}
