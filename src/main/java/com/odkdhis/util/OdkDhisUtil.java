/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.model.DatasetValue;
import com.odkdhis.model.OdkForm;
import com.odkdhis.model.SingleForm;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author ssvmoh
 */
@Component
public class OdkDhisUtil extends CommonUtil {
/*
	public static int INDENTATION = 4;

	private List<DatasetValue> getDataSetValuesListNew(String projectId) {
		List<DatasetValue> dataValues = new ArrayList<>();
		try {
			for (SingleForm form : SingleForm.values()) {
				log.info("processing ********************************************************************>"
						+ form.name());
				final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + projectId
						+ "/forms/" + form.name() + "/submissions";
				HttpEntity<String> entity = new HttpEntity<String>(getODKHttpHeaders("JSON"));
				JSONArray jArray = new JSONArray(
						restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());

				if (!jArray.isEmpty()) {
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject instance = jArray.getJSONObject(i);
						String instanceId = instance.getString("instanceId");
						String instanceCreatedDate = instance.getString("createdAt");

						if (dateEqualToYesterday(instanceCreatedDate, "GMT+03")) {
							String xmlString = getSubmissionInstanceXml(projectId, form.name(), instanceId);
							if (xmlString != null) {
								JSONObject jsonObj = XML.toJSONObject(xmlString);
								String json = jsonObj.toString(INDENTATION);
								JSONObject instancejson = new JSONObject(json);
								JSONObject datajson = instancejson.getJSONObject("data");
								String instanceCollectedDate = datajson.getString("today");

								String num = String
										.valueOf(datajson.getInt(getDenominatorOrNumeratorFieldName(form.name(), "N")));
								String den = String
										.valueOf(datajson.getInt(getDenominatorOrNumeratorFieldName(form.name(), "D")));
								String score = String.valueOf(
										getScore(datajson.getInt(getDenominatorOrNumeratorFieldName(form.name(), "N")),
												datajson.getInt(getDenominatorOrNumeratorFieldName(form.name(), "D")),
												form.getMaxScore()));

								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "DENOMINATOR"),
										getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson.getInt("mflcode"))),
										den));
								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "NUMERATOR"),
										getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson.getInt("mflcode"))),
										num));
								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "FORMNAME"),
										getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson.getInt("mflcode"))),
										form.getName()));
								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "SCORE"),
										getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson.getInt("mflcode"))),
										score));
								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "MAXSCORE"),
										getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson.getInt("mflcode"))),
										String.valueOf(form.getMaxScore())));
							}
						}
					}
				}
			}
			// Create the whole datavalues listing
			List<DatasetValue> finalDValues = new ArrayList<>();
			finalDValues.addAll(dataValues);
			List<DatasetValue> storageDValues = getDataSetValuesListFromTheDoubleForms(projectId,
					"storage_area_assesment2", "storage_area_assesment_org", "Storage of Health Products", 20);
			finalDValues.addAll(storageDValues);
			List<DatasetValue> inventoryDValues = getDataSetValuesListFromTheDoubleForms(projectId,
					"Inventory_Management", "Inventory_Management_Addenda", "Inventory Management", 30);
			finalDValues.addAll(inventoryDValues);
			List<DatasetValue> supplyDValues = getDataSetValuesListFromTheDoubleForms(projectId,
					"supply_chain_audit_v2", "supply_chain_audit_Addenda", "Accountability for Commodities", 15);
			finalDValues.addAll(supplyDValues);
			return finalDValues;
		} catch (Exception e) {
			log.error("Error Occured While Getting Submission ==>" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private List<DatasetValue> getDataSetValuesListFromTheDoubleForms(String projectId, String formId1, String formId2,
			String formName, Integer maxScore) {
		List<DatasetValue> dataValues = new ArrayList<>();
		log.info("processing ********************************************************************>" + formId1
				+ "  And  " + formId2);
		try {
			HttpEntity<String> odkHeaderEntity = new HttpEntity<String>(getODKHttpHeaders("JSON"));
			final String submissionsUri1 = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + projectId
					+ "/forms/" + formId1 + "/submissions";
			final String submissionsUri2 = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + projectId
					+ "/forms/" + formId2 + "/submissions";
			JSONArray jArrayStorage1 = new JSONArray(
					restTemplate.exchange(submissionsUri1, HttpMethod.GET, odkHeaderEntity, String.class).getBody());
			JSONArray jArrayStorage2 = new JSONArray(
					restTemplate.exchange(submissionsUri2, HttpMethod.GET, odkHeaderEntity, String.class).getBody());

			JSONArray jArray1 = new JSONArray();
			JSONArray jArray2 = new JSONArray();

			if (!jArrayStorage1.isEmpty()) {
				for (int i = 0; i < jArrayStorage1.length(); i++) {
					JSONObject instance = jArrayStorage1.getJSONObject(i);
					String instanceCreatedDate = instance.getString("createdAt");
					if (dateEqualToYesterday(instanceCreatedDate, "GMT+03")) {
						jArray1.put(instance);
					}
				}
			}

			if (!jArrayStorage2.isEmpty()) {
				for (int i = 0; i < jArrayStorage2.length(); i++) {
					JSONObject instance = jArrayStorage2.getJSONObject(i);
					String instanceCreatedDate = instance.getString("createdAt");
					if (dateEqualToYesterday(instanceCreatedDate, "GMT+03")) {
						jArray2.put(instance);
					}
				}
			}

			List<String> jArrayProcessed1 = new ArrayList<>();
			List<String> jArrayProcessed2 = new ArrayList<>();
			if (!jArray1.isEmpty() && !jArray2.isEmpty()) {
				for (int i = 0; i < jArray1.length(); i++) {
					for (int j = 0; j < jArray2.length(); j++) {

						JSONObject instance1 = jArray1.getJSONObject(i);
						String instanceId1 = instance1.getString("instanceId");
						String xmlString1 = getSubmissionInstanceXml(projectId, formId1, instanceId1);
						JSONObject instance2 = jArray1.getJSONObject(j);
						String instanceId2 = instance2.getString("instanceId");
						String xmlString2 = getSubmissionInstanceXml(projectId, formId2, instanceId2);

						if (xmlString1 != null && xmlString2 != null) {
							// Instance 1
							JSONObject jsonObj1 = XML.toJSONObject(xmlString1);
							String json1 = jsonObj1.toString(INDENTATION);
							JSONObject instancejson1 = new JSONObject(json1);
							JSONObject datajson1 = instancejson1.getJSONObject("data");
							String instanceCollectedDate1 = datajson1.getString("today");
							Integer instanceCollectedfacility1 = datajson1.getInt("mflcode");

							// Instance 2
							JSONObject jsonObj2 = XML.toJSONObject(xmlString2);
							String json2 = jsonObj2.toString(INDENTATION);
							JSONObject instancejson2 = new JSONObject(json2);
							JSONObject datajson2 = instancejson2.getJSONObject("data");
							String instanceCollectedDate2 = datajson2.getString("today");
							Integer instanceCollectedfacility2 = datajson2.getInt("mflcode");

							if (instanceCollectedDate1.equalsIgnoreCase(instanceCollectedDate2)
									&& instanceCollectedfacility1.compareTo(instanceCollectedfacility2) == 0) {
								Integer num1 = (formId1.equals("storage_area_assesment2"))
										? datajson1.getJSONObject("Storage_Assessment")
												.getInt(getDenominatorOrNumeratorFieldName(formId1, "N"))
										: datajson1.getInt(getDenominatorOrNumeratorFieldName(formId1, "N"));
								Integer num2 = (formId2.equals("storage_area_assesment_org"))
										? datajson2.getJSONObject("Storage_Assessment")
												.getInt(getDenominatorOrNumeratorFieldName(formId2, "N"))
										: datajson2.getInt(getDenominatorOrNumeratorFieldName(formId2, "N"));
								Integer den1 = (formId1.equals("storage_area_assesment2"))
										? datajson1.getJSONObject("Storage_Assessment")
												.getInt(getDenominatorOrNumeratorFieldName(formId1, "D"))
										: datajson1.getInt(getDenominatorOrNumeratorFieldName(formId1, "D"));
								Integer den2 = (formId2.equals("storage_area_assesment_org"))
										? datajson2.getJSONObject("Storage_Assessment")
												.getInt(getDenominatorOrNumeratorFieldName(formId2, "D"))
										: datajson2.getInt(getDenominatorOrNumeratorFieldName(formId2, "D"));

								Integer num = num1 + num2;
								Integer den = den1 + den2;

								dataValues
										.add(new DatasetValue(getEntityIdFromCode("dataElements", "DENOMINATOR"),
												getDHISDailyPeriodFromString(instanceCollectedDate1, "GMT+03"),
												getEntityIdFromCode("organisationUnits",
														String.valueOf(datajson1.getInt("mflcode"))),
												String.valueOf(den)));
								dataValues
										.add(new DatasetValue(getEntityIdFromCode("dataElements", "NUMERATOR"),
												getDHISDailyPeriodFromString(instanceCollectedDate1, "GMT+03"),
												getEntityIdFromCode("organisationUnits",
														String.valueOf(datajson1.getInt("mflcode"))),
												String.valueOf(num)));
								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "FORMNAME"),
										getDHISDailyPeriodFromString(instanceCollectedDate1, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson1.getInt("mflcode"))),
										formName));
								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "SCORE"),
										getDHISDailyPeriodFromString(instanceCollectedDate1, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson1.getInt("mflcode"))),
										String.valueOf(getScore(num, den, maxScore))));
								dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "MAXSCORE"),
										getDHISDailyPeriodFromString(instanceCollectedDate1, "GMT+03"),
										getEntityIdFromCode("organisationUnits",
												String.valueOf(datajson1.getInt("mflcode"))),
										String.valueOf(maxScore)));

								jArrayProcessed1.add(xmlString1);
								jArrayProcessed2.add(instanceId2);
							}
						}

					}

				}

				for (int i = 0; i < jArray1.length(); i++) {
					JSONObject instance = jArray1.getJSONObject(i);
					String instanceId = instance.getString("instanceId");
					if (!jArrayProcessed1.contains(instanceId)) {
						String xmlString = getSubmissionInstanceXml(projectId, formId1, instanceId);
						if (xmlString != null) {
							JSONObject jsonObj = XML.toJSONObject(xmlString);
							String json = jsonObj.toString(INDENTATION);
							JSONObject instancejson = new JSONObject(json);
							JSONObject datajson = instancejson.getJSONObject("data");
							String instanceCollectedDate = datajson.getString("today");

							Integer num = (formId1.equals("storage_area_assesment2"))
									? datajson.getJSONObject("Storage_Assessment")
											.getInt(getDenominatorOrNumeratorFieldName(formId1, "N"))
									: datajson.getInt(getDenominatorOrNumeratorFieldName(formId1, "N"));
							Integer den = (formId1.equals("storage_area_assesment2"))
									? datajson.getJSONObject("Storage_Assessment")
											.getInt(getDenominatorOrNumeratorFieldName(formId1, "D"))
									: datajson.getInt(getDenominatorOrNumeratorFieldName(formId1, "D"));
							BigDecimal score = getScore(num, den, maxScore);

							dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "DENOMINATOR"),
									getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
									getEntityIdFromCode("organisationUnits",
											String.valueOf(datajson.getInt("mflcode"))),
									String.valueOf(den)));
							dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "NUMERATOR"),
									getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
									getEntityIdFromCode("organisationUnits",
											String.valueOf(datajson.getInt("mflcode"))),
									String.valueOf(num)));
							dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "FORMNAME"),
									getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
									getEntityIdFromCode("organisationUnits",
											String.valueOf(datajson.getInt("mflcode"))),
									formName));
							dataValues
									.add(new DatasetValue(getEntityIdFromCode("dataElements", "SCORE"),
											getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
											getEntityIdFromCode("organisationUnits",
													String.valueOf(datajson.getInt("mflcode"))),
											String.valueOf(score)));
							dataValues
									.add(new DatasetValue(getEntityIdFromCode("dataElements", "MAXSCORE"),
											getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
											getEntityIdFromCode("organisationUnits",
													String.valueOf(datajson.getInt("mflcode"))),
											String.valueOf(maxScore)));
						}
					}
				}
				for (int i = 0; i < jArray2.length(); i++) {
					JSONObject instance = jArray2.getJSONObject(i);
					String instanceId = instance.getString("instanceId");
					if (!jArrayProcessed2.contains(instanceId)) {
						String xmlString = getSubmissionInstanceXml(projectId, formId2, instanceId);
						if (xmlString != null) {
							JSONObject jsonObj = XML.toJSONObject(xmlString);
							String json = jsonObj.toString(INDENTATION);
							JSONObject instancejson = new JSONObject(json);
							JSONObject datajson = instancejson.getJSONObject("data");
							String instanceCollectedDate = datajson.getString("today");

							Integer num = (formId2.equals("storage_area_assesment_org"))
									? datajson.getJSONObject("Storage_Assessment")
											.getInt(getDenominatorOrNumeratorFieldName(formId2, "N"))
									: datajson.getInt(getDenominatorOrNumeratorFieldName(formId2, "N"));
							Integer den = (formId2.equals("storage_area_assesment_org"))
									? datajson.getJSONObject("Storage_Assessment")
											.getInt(getDenominatorOrNumeratorFieldName(formId2, "D"))
									: datajson.getInt(getDenominatorOrNumeratorFieldName(formId2, "D"));
							BigDecimal score = getScore(num, den, maxScore);

							dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "DENOMINATOR"),
									getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
									getEntityIdFromCode("organisationUnits",
											String.valueOf(datajson.getInt("mflcode"))),
									String.valueOf(den)));
							dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "NUMERATOR"),
									getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
									getEntityIdFromCode("organisationUnits",
											String.valueOf(datajson.getInt("mflcode"))),
									String.valueOf(num)));
							dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "FORMNAME"),
									getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
									getEntityIdFromCode("organisationUnits",
											String.valueOf(datajson.getInt("mflcode"))),
									formName));
							dataValues
									.add(new DatasetValue(getEntityIdFromCode("dataElements", "SCORE"),
											getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
											getEntityIdFromCode("organisationUnits",
													String.valueOf(datajson.getInt("mflcode"))),
											String.valueOf(score)));
							dataValues
									.add(new DatasetValue(getEntityIdFromCode("dataElements", "MAXSCORE"),
											getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
											getEntityIdFromCode("organisationUnits",
													String.valueOf(datajson.getInt("mflcode"))),
											String.valueOf(maxScore)));
						}
					}
				}

			} else if (!jArray1.isEmpty() && jArray2.isEmpty()) {
				for (int i = 0; i < jArray1.length(); i++) {
					JSONObject instance = jArray1.getJSONObject(i);
					String instanceId = instance.getString("instanceId");

					String xmlString = getSubmissionInstanceXml(projectId, formId1, instanceId);
					if (xmlString != null) {
						JSONObject jsonObj = XML.toJSONObject(xmlString);
						String json = jsonObj.toString(INDENTATION);
						JSONObject instancejson = new JSONObject(json);
						JSONObject datajson = instancejson.getJSONObject("data");
						String instanceCollectedDate = datajson.getString("today");

						Integer num = (formId1.equals("storage_area_assesment2"))
								? datajson.getJSONObject("Storage_Assessment")
										.getInt(getDenominatorOrNumeratorFieldName(formId1, "N"))
								: datajson.getInt(getDenominatorOrNumeratorFieldName(formId1, "N"));
						Integer den = (formId1.equals("storage_area_assesment2"))
								? datajson.getJSONObject("Storage_Assessment")
										.getInt(getDenominatorOrNumeratorFieldName(formId1, "D"))
								: datajson.getInt(getDenominatorOrNumeratorFieldName(formId1, "D"));
						BigDecimal score = getScore(num, den, maxScore);

						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "DENOMINATOR"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(den)));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "NUMERATOR"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(num)));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "FORMNAME"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								formName));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "SCORE"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(score)));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "MAXSCORE"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(maxScore)));
					}
				}

			} else if (jArray1.isEmpty() && !jArray2.isEmpty()) {
				for (int i = 0; i < jArray2.length(); i++) {
					JSONObject instance = jArray2.getJSONObject(i);
					String instanceId = instance.getString("instanceId");

					String xmlString = getSubmissionInstanceXml(projectId, formId2, instanceId);
					if (xmlString != null) {
						JSONObject jsonObj = XML.toJSONObject(xmlString);
						String json = jsonObj.toString(INDENTATION);
						JSONObject instancejson = new JSONObject(json);
						JSONObject datajson = instancejson.getJSONObject("data");
						String instanceCollectedDate = datajson.getString("today");

						Integer num = (formId2.equals("storage_area_assesment_org"))
								? datajson.getJSONObject("Storage_Assessment")
										.getInt(getDenominatorOrNumeratorFieldName(formId2, "N"))
								: datajson.getInt(getDenominatorOrNumeratorFieldName(formId2, "N"));
						Integer den = (formId2.equals("storage_area_assesment_org"))
								? datajson.getJSONObject("Storage_Assessment")
										.getInt(getDenominatorOrNumeratorFieldName(formId2, "D"))
								: datajson.getInt(getDenominatorOrNumeratorFieldName(formId2, "D"));
						BigDecimal score = getScore(num, den, maxScore);

						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "DENOMINATOR"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(den)));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "NUMERATOR"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(num)));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "FORMNAME"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								formName));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "SCORE"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(score)));
						dataValues.add(new DatasetValue(getEntityIdFromCode("dataElements", "MAXSCORE"),
								getDHISDailyPeriodFromString(instanceCollectedDate, "GMT+03"),
								getEntityIdFromCode("organisationUnits", String.valueOf(datajson.getInt("mflcode"))),
								String.valueOf(maxScore)));
					}
				}

			} else {
				log.info("Nothing To Process for the 2 forms !!!!!!!!!!!!!");
			}

			return dataValues;
		} catch (Exception e) {
			log.error("Error Occured While Getting Submission ==>" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private JSONArray getODKCentralSubmissions(String projectId, String xmlFormId) {
		try {
			final String submissionsUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + projectId
					+ "/forms/" + xmlFormId + "/submissions";
			HttpEntity<String> entity = new HttpEntity<String>(getODKHttpHeaders("JSON"));
			return new JSONArray(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());
		} catch (Exception e) {
			log.error("Error Occured While Getting Submission ==>" + e.getMessage());
			return null;
		}
	}

	private String removeAllOrganisationUnits() {
		try {
			for (int j = 0; j < 2; j++) {
				log.info("Processing =======================>Page " + j + "/148");

				final String submissionsUri = "http://ssvdhis.crabdance.com:8080/api/organisationUnitGroupSets.json?page="
						+ j;
				HttpEntity<String> entity = new HttpEntity<String>(getDHISHttpHeaders());
				JSONObject js = new JSONObject(
						restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());
				JSONArray arr = js.getJSONArray("organisationUnitGroupSets");

				for (int i = 0; i < arr.length(); i++) {
					String delUri = "http://ssvdhis.crabdance.com:8080/api/organisationUnitGroupSets/"
							+ arr.getJSONObject(i).getString("id");
					log.info("Removing ==" + arr.getJSONObject(i).getString("id"));
					restTemplate.exchange(delUri, HttpMethod.DELETE, entity, String.class);

				}
			}
			return null;
		} catch (Exception e) {
			log.error("Error Occured While Getting dataValueSets ==>" + e.getMessage());
			return null;
		}
	}

	// @Scheduled(fixedDelay = 1000 * 60 * 5) //5 minutes
	@Scheduled(cron = "20 58 15 * * ?", zone = "Africa/Nairobi")
	/*
	 * The fields read from left to right are interpreted as follows. second minute
	 * hour day of month month day of week
	 */
	/*public void postDataFromODKTODHIS() throws Exception {
		try {
			if (internetConnectionIsAvailable()) {
				checkODKDHISServerHeartBeat();
				if (connDao.getConnectionStatus() != null
						&& connDao.getConnectionStatus().getOdkConnection().equals("YES")
						&& connDao.getConnectionStatus().getDhisConnection().equals("YES")) {
					// JSONArray odkDataIds = getODKCentralSubmissions("2",
					// "Facility_Profile_Information");
					// List<DatasetValue> datavalues = getDataSetValuesList(odkDataIds, "2",
					// "Facility_Profile_Information");
					// List<DatasetValue> datavalues = getDataSetValuesListNew("2");
					// if (datavalues != null && !datavalues.isEmpty()) {
					// String jsonStringTobePostedToDHIS =
					// getDataSetValuesForDHISPostRequest(datavalues);
					// log.info("JSON DATA===" + jsonStringTobePostedToDHIS);
					// postDatasetValuesToDHIS(jsonStringTobePostedToDHIS);
					// }
					deleteODKSubmissionByInstanceId("2", "Inventory_Management",
							"uuid:7d8cc771-c1a7-4425-af14-7aeb4e35d059");
				}
			}

		} catch (JSONException e) {
			log.error("Error Occured ============>" + e.getMessage());
		}

	}

	private void deleteODKSubmissionByInstanceId(String projectId, String xmlFormId, String instanceId) {
		final String instanceUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + projectId + "/forms/"
				+ xmlFormId + "/submissions/" + instanceId;
		HttpEntity<String> entity = new HttpEntity<String>(getODKHttpHeaders("XML"));
		ResponseEntity<String> result = restTemplate.exchange(instanceUri, HttpMethod.DELETE, entity, String.class);
		log.info("Form XML ***************" + getSubmissionInstanceXml(projectId, xmlFormId, instanceId));
		log.info("Delete Response Code ***************" + result.getStatusCodeValue());
		log.info("Delete Response ***************" + result.getBody());
	}

	

	private String getSubmissionInstanceXml(String projectId, String xmlFormId, String instanceId) {
		try {
			final String instanceUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + projectId + "/forms/"
					+ xmlFormId + "/submissions/" + instanceId + ".xml";
			HttpEntity<String> entity = new HttpEntity<String>(getODKHttpHeaders("XML"));
			ResponseEntity<String> result = restTemplate.exchange(instanceUri, HttpMethod.GET, entity, String.class);
			log.info("XML CODE *************************************************************"
					+ result.getStatusCodeValue());
			log.info("XML STATUS CODE *************************************************************"
					+ result.getStatusCode());

			if (result.getStatusCode() == HttpStatus.OK) {
				String xmlString = restTemplate.exchange(instanceUri, HttpMethod.GET, entity, String.class).getBody();// Check
																														// 200
																														// response
				return xmlString;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private List<DatasetValue> getDataSetValuesList(JSONArray instancesArray, String projectId, String xmlFormId) {
		List<DatasetValue> dataValues = new ArrayList<>();
		if (!instancesArray.isEmpty()) {
			for (int i = 0; i < instancesArray.length(); i++) {
				JSONObject instance = instancesArray.getJSONObject(i);
				String instanceId = instance.getString("instanceId");
				String xmlString = getSubmissionInstanceXml(projectId, xmlFormId, instanceId);
				JSONObject jsonObj = XML.toJSONObject(xmlString);
				String json = jsonObj.toString(INDENTATION);
				JSONObject instancejson = new JSONObject(json);
				JSONObject datajson = instancejson.getJSONObject("data");

				dataValues.add(new DatasetValue("a8yOuedpiAM", "20200929", "Objgoqcihmn",
						String.valueOf(datajson.getInt("fDenominator"))));

			}
		}

		return dataValues;
	}*/

	


}
