package com.odkdhis.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Response {

	/**
	 * Generates modelMap to return in the modelAndView
	 * @param contacts
	 * @return
	 */
	public static Map<String,Object> listResponse(List records){
		
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("totalCount", records.size());
		modelMap.put("rows", records);
		modelMap.put("success", true);
		
		return modelMap;
	}
        
        public static Map<String,Object> listResponse2(List records, String rec){
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("totalCount", records.size());
		modelMap.put("rows", rec);
		modelMap.put("success", true);
		
		return modelMap;
	}
        
        public static Map<String,Object> mapOKCal(List records){
		
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("events", records);
		
		return modelMap;
	}
	
	/**
	 * Generates modelMap to return in the modelAndView
	 * @param contacts
	 * @return
	 */
	public static Map<String,Object> mapOK(List files, int total){
		
		Map<String,Object> modelMap = new HashMap<String,Object>(3);
		modelMap.put("total", total);
		modelMap.put("data", files);
		modelMap.put("success", true);
		
		return modelMap;
	}
        
	/**
	 * Generates modelMap to return in the modelAndView in case
	 * of exception
	 * @param message message
	 * @return
	 */
	public static Map<String,Object> failure(String message){

		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", message);
		modelMap.put("success", false);

		return modelMap;
	} 
        
        public static Map<String,Object> success(String message){

		Map<String,Object> modelMap = new HashMap<String,Object>(2);
		modelMap.put("message", message);
		modelMap.put("success", true);

		return modelMap;
	} 
}
