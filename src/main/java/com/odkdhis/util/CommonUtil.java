/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.util;

import com.odkdhis.dao.ConnectionStatusDaoImpl;
import com.odkdhis.dao.DataValueGroupDaoImpl;
import com.odkdhis.dao.DatasetValueDaoImpl;
import com.odkdhis.dao.ServerConfigDaoImpl;
import com.odkdhis.dao.SubmissionDaoImpl;
import com.odkdhis.dao.dashboard.ActionPointsReviewDaoImpl;
import com.odkdhis.dao.dashboard.AdministratorReportsDaoImpl;
import com.odkdhis.dao.dashboard.CurrentActionPointsDaoImpl;
import com.odkdhis.dao.dashboard.InventoryManagementDaoImpl;
import com.odkdhis.dao.dashboard.LlinsDaoImpl;
import com.odkdhis.dao.dashboard.OnJobTrainingDaoImpl;
import com.odkdhis.dao.dashboard.OrganizationUnitDaoImpl;
import com.odkdhis.dao.dashboard.ProductAccountabilityDaoImpl;
import com.odkdhis.dao.dashboard.RenalSectionDaoImpl;
import com.odkdhis.dao.dashboard.StorageAssessmentDaoImple;
import com.odkdhis.dao.dashboard.SummaryDataDaoImpl;
import com.odkdhis.dao.dashboard.SupervisionTeamDaoImpl;
import com.odkdhis.dao.dashboard.VerificationDataDaoImpl;
import com.odkdhis.model.ConnectionStatus;
import com.odkdhis.model.DataElement;
import com.odkdhis.model.DataValueGroup;
import com.odkdhis.model.DatasetValue;
import com.odkdhis.model.ApiUser;
import com.odkdhis.model.User;
import com.odkdhis.model.dashboard.OrganizationUnit;
import com.odkdhis.model.dashboard.SummaryData;
import com.odkdhis.security.UserDaoImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.ModelAndView;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ssvmoh
 */
public abstract class CommonUtil implements ApplicationContextAware {

    protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    protected static DecimalFormat formatter = new DecimalFormat("#,##0.00"); //$NON-NLS-1$
    protected SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MMM-dd"); //$NON-NLS-1$
    protected String ENCRYPTION_SECRET = "ssvODK@DhisData";
    protected BigDecimal zero = BigDecimal.ZERO;
    
    protected static int INDENTATION = 4;
    protected final String PROJECTID = "2";
    protected final String PROJECTID3 = "3";
    protected final String PROJECTID7 = "7";
    

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    protected ConnectionStatusDaoImpl connDao;

    @Autowired
    protected ServerConfigDaoImpl configDao;

    @Autowired
    protected SubmissionDaoImpl submissionDao;

    @Autowired
    protected DatasetValueDaoImpl dataValueDao;

    @Autowired
    protected DataValueGroupDaoImpl dataValueGroupDao;

    @Autowired
    protected OrganizationUnitDaoImpl orgDao;

    @Autowired
    protected SummaryDataDaoImpl dataDao;
    
    @Autowired
    protected UserDaoImpl userDao;
    
    @Autowired
    protected InventoryManagementDaoImpl inventorymgtDao;
    
    @Autowired
    protected StorageAssessmentDaoImple StorageAssessmentDao;
    
    @Autowired
    protected VerificationDataDaoImpl verificationDataDao;
    
    @Autowired
    protected AdministratorReportsDaoImpl adminReportsDao;
    
    @Autowired
    protected OnJobTrainingDaoImpl onJobTrainingDao;
    
    @Autowired
    protected ActionPointsReviewDaoImpl actionPointsReviewDao;
    
    @Autowired
    protected CurrentActionPointsDaoImpl currentactionPointsDao;
    
    @Autowired
    protected SupervisionTeamDaoImpl supervisionTeamDao;
    
    @Autowired
    protected LlinsDaoImpl llinsDao;
      
    @Autowired
    protected ProductAccountabilityDaoImpl accountabilityDao;
    
    @Autowired
    protected RenalSectionDaoImpl renalSectionDao;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private ApplicationContext applicationContext;
    
    public User getCurrentLoggedUser() {
        User u = null;
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            u = userDao.findByUsername(currentUserName);
        }
        return u;
    }

    protected ModelAndView getModelAndView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("bothconnected", (connDao.getConnectionStatus() != null && connDao.getConnectionStatus().getOdkConnection().equals("YES") && connDao.getConnectionStatus().getDhisConnection().equals("YES")) ? true : false);
        modelAndView.addObject("dataSend", dataValueGroupDao.getDatasetValueGroups(Boolean.TRUE, null).size());
        modelAndView.addObject("dataNOTSend", dataValueGroupDao.getDatasetValueGroups(Boolean.FALSE, null).size());
        //modelAndView.addObject("availableData", dataDao.getDisplayDataList(null, null, null));
        return modelAndView;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getContext() {
        return applicationContext;
    }

    protected static boolean internetConnectionIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    private static SecretKeySpec secretKey;
    private static byte[] key;

    protected void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    protected String encrypt(String strToEncrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    protected String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    protected static Date getDateFromString(String date, String timeZone) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone(timeZone)); // example "GMT+08" for timezone
        String[] str_date = date.split(Pattern.quote("."));

        return format.parse(str_date[0].replace("T", " "));
    }

    protected static Date getDateFromStringNoTime(String date, String timeZone) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        return format.parse(date);
    }

    protected String getDHISDailyPeriodFromString(String date, String timeZone) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        String[] str_date = date.split(Pattern.quote("."));
        String[] saidDate = (str_date[0].replace("T", " ").replace("-", "")).split(" ");
        return saidDate[0];
    }

    protected boolean dateEqualToYesterday(String date, String timeZone) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        String[] str_date = date.split(Pattern.quote("."));
        Date data_date = format.parse(str_date[0].replace("T", " "));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        // today.setTime(today.getTime() - 24 * 60 * 60 * 1000); // Subtract
        // 24*60*60*1000 milliseconds
        today.setTime(today.getTime() - 24 * 60 * 60 * 1000); // Subtract 24*60*60*1000 milliseconds
        Date yesterday = format.parse(dateFormat.format(today));

        return data_date.equals(yesterday);
    }

    protected boolean dateEqualToToday(Date data_date, String timeZone) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Date todayFormatted = format.parse(dateFormat.format(today));
        return data_date.equals(todayFormatted);
    }

    protected String getODKAuthToken() {
        final String loginUri = configDao.getServerConfig().getOdkUrl() + "/v1/sessions";
        ApiUser user = new ApiUser(configDao.getServerConfig().getOdkAuthUser(), decrypt(configDao.getServerConfig().getOdkAuthPass(), ENCRYPTION_SECRET));
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ApiUser> entity = new HttpEntity<>(user, headers);
        JSONObject jsonfromString = new JSONObject(restTemplate.exchange(loginUri, HttpMethod.POST, entity, String.class).getBody());
        return jsonfromString.getString("token");
    }

    protected HttpHeaders getODKHttpHeaders(String contentType, String odkToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType.equals("JSON") ? MediaType.APPLICATION_JSON : MediaType.APPLICATION_XML);
        headers.set("Authorization", "Bearer " + odkToken);
        return headers;
    }

    protected String getDHISBasicToken() {
        String auth = configDao.getServerConfig().getDhisAuthUser() + ":" + decrypt(configDao.getServerConfig().getDhisAuthPass(), ENCRYPTION_SECRET);
        byte[] encodedAuth = org.apache.tomcat.util.codec.binary.Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }

    protected HttpHeaders getDHISHttpHeaders(String authHeader) {
        return new HttpHeaders() {
            {
                set("Authorization", authHeader);
                setContentType(MediaType.APPLICATION_JSON);
            }
        };
    }

    protected HttpHeaders getDHISHttpHeaders_Original() {
        return new HttpHeaders() {
            {
                String auth = configDao.getServerConfig().getDhisAuthUser() + ":" + decrypt(configDao.getServerConfig().getDhisAuthPass(), ENCRYPTION_SECRET);
                byte[] encodedAuth = org.apache.tomcat.util.codec.binary.Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
                setContentType(MediaType.APPLICATION_JSON);
            }
        };
    }

    protected void checkODKDHISServerHeartBeat() throws Exception {
        if (connDao.getConnectionStatusses().size() > 1) {
            for (ConnectionStatus current : connDao.getConnectionStatusses()) {
                connDao.delete(current);
            }
        }
        try {
            if (configDao.getServerConfig() != null) {
                final String OdkUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects";
                HttpEntity<String> odkentity = new HttpEntity<>(getODKHttpHeaders("JSON", getODKAuthToken()));
                Integer odkStatus = restTemplate.exchange(OdkUri, HttpMethod.GET, odkentity, String.class).getStatusCodeValue();

                final String dhisUri = configDao.getServerConfig().getDhisUrl() + "/api/dataSets.json";
                HttpEntity<String> dhisentity = new HttpEntity<>(getDHISHttpHeaders(getDHISBasicToken()));
                Integer dhisStatus = restTemplate.exchange(dhisUri, HttpMethod.GET, dhisentity, String.class).getStatusCodeValue();

                ConnectionStatus st = new ConnectionStatus();
                st.setOdkConnection(odkStatus == 200 ? "YES" : "NO");
                st.setDhisConnection(dhisStatus == 200 ? "YES" : "NO");
                connDao.save(st);
            }

        } catch (Exception e) {
            ConnectionStatus st = new ConnectionStatus();
            st.setOdkConnection("NO");
            st.setDhisConnection("NO");
            connDao.save(st);
        }

    }

    protected String getDenominatorOrNumeratorFieldName(String formId, String fieldName) {
        
        if (fieldName.equals("D")) {
            switch (formId) {
                case "Facility_Profile_Information":
                //case "supply_chain_audit_Addenda":
                    return "fDenominator";
                case "storage_area_assesment2":
                //case "storage_area_assesment_org":
                    return "cdenominator";
                //case "Inventory_Management_Addenda":
                //case "Inventory_Management":
                case "Inventory_Management_combined":                    
                //case "supply_chain_audit_v2":
                //case "supply_chain_audit_v3":
                case "supply_chain_audit_slim":
                    return "totalDenominator";                                        
                case "Commodity_Data_and_Reporting_Tools":
                    return "fdenominator";
                case "Verification_of_commodity_data":
                    return "tdenominator";
                case "LLINs":
                    return "lDenominator";
                case "Job_Aids_for_commodity_management":
                    return "denominator";
                     
                default:
                    return "";
            }
        } else {
            switch (formId) {
                case "Facility_Profile_Information":
                //case "supply_chain_audit_Addenda":
                    return "fNumerator";                     
                case "storage_area_assesment2":
                //case "storage_area_assesment_org":
                    return "storageAssessmentTotal";
                //case "Inventory_Management_Addenda":    
                //case "Inventory_Management":
                case "Inventory_Management_combined":
                    return "totalNumerator";                    
                case "Commodity_Data_and_Reporting_Tools":
                    return "fnumerator";
                case "Verification_of_commodity_data":
                    return "tnumerator";
                //case "supply_chain_audit_v2":
                //case "supply_chain_audit_v3":
                case "supply_chain_audit_slim": 
                    return "finalNumerator";                    
                case "Job_Aids_for_commodity_management":
                    return "numerator";
                case "LLINs":
                    return "lNumerator";                                         
                default:
                    return "";
            }
        }
    }

    protected JSONObject postDatasetValuesToDHIS(String jsonString, DataValueGroup dvg) throws Exception {
        try {
            final String submissionsUri = configDao.getServerConfig().getDhisUrl() + "/api/dataValueSets";//?importStrategy=CREATE_AND_UPDATE&dryRun=true
            HttpEntity<String> entity = new HttpEntity<>(jsonString, getDHISHttpHeaders(getDHISBasicToken()));
            ResponseEntity<String> result = restTemplate.exchange(submissionsUri, HttpMethod.POST, entity, String.class);
            log.info("Data Post Response ************************************************************" + result.getStatusCode());
            if (result.getStatusCode() == HttpStatus.OK) {
                dvg.setRetries(dvg.getRetries() + 1);
                if (dvg.getRetries() > 3) {
                    dvg.setDataSend(Boolean.TRUE);
                }
                dataValueGroupDao.save(dvg);
            }
            return new JSONObject(result.getBody());
        } catch (JSONException | RestClientException e) {
            log.error("Error Occured While Getting dataValueSets ==>" + e.getMessage());
            return null;
        }
    }

    protected JSONObject refreshDHISAnalyticsTables() throws Exception {
        try {
            final String submissionsUri = configDao.getServerConfig().getDhisUrl() + "/api/resourceTables/analytics";
            HttpEntity<String> entity = new HttpEntity<>(getDHISHttpHeaders(getDHISBasicToken()));
            ResponseEntity<String> result = restTemplate.exchange(submissionsUri, HttpMethod.POST, entity, String.class);
            log.info("Analytics Tables Refreshed ************************************************************" + result.getStatusCode());
            return new JSONObject(result.getBody());
        } catch (JSONException | RestClientException e) {
            log.error("Error Occured While Refreshing Tables ==>" + e.getMessage());
            return null;
        }
    }

    protected String getEntityIdFromCode(String entityName, String entityCode, String dhisBasicToken) {
        try {
            final String submissionsUri = configDao.getServerConfig().getDhisUrl() + "/api/" + entityName + ".json?filter=code:eq:" + entityCode;
            HttpEntity<String> entity = new HttpEntity<>(getDHISHttpHeaders(dhisBasicToken));
            JSONObject resp = new JSONObject(restTemplate.exchange(submissionsUri, HttpMethod.GET, entity, String.class).getBody());
            return new JSONObject(resp.getJSONArray(entityName).get(0).toString()).getString("id");
        } catch (JSONException | RestClientException e) {
            log.error("Error Occured While Getting dataValueSets ==>" + e.getMessage());
            return null;
        }
    }

    protected BigDecimal getScore(Integer numerator, Integer denominator, Integer maxScore) {
        BigDecimal num = getNumeratorScore(numerator, denominator, maxScore);
        BigDecimal max = new BigDecimal(maxScore);
        BigDecimal f = num.divide(max, 10, RoundingMode.CEILING);
        BigDecimal score = (f.multiply(new BigDecimal(100))).setScale(0, RoundingMode.HALF_UP);
        return score;

    }

    protected BigDecimal getNumeratorScore(Integer numerator, Integer denominator, Integer maxScore) {
        BigDecimal num = new BigDecimal(numerator);
        BigDecimal den = new BigDecimal(denominator);
        BigDecimal max = new BigDecimal(maxScore);
        if (den.compareTo(zero) > 0) {
            BigDecimal f = num.divide(den, 10, RoundingMode.CEILING);
            return (f.multiply(max)).setScale(1, RoundingMode.HALF_UP);
        }
        return zero;

    }

    protected String getDataSetValuesForDHISPostRequest_old(List<DatasetValue> dataValues) throws Exception {
        StringBuffer sb = new StringBuffer(1000);
        sb.append("{\"dataValues\":[");
        dataValues.forEach(dv -> {
            sb.append(dv.toGridJson2());
        });
        sb.append("]}");
        final String resp = sb.toString();
        return resp.replace(",\n" + "]}", "]}");
    }

    protected String getDataSetValuesForDHISPostRequest(List<DatasetValue> dataValues) throws Exception {
        StringBuffer sb = new StringBuffer(1000);
        DataValueGroup dv1 = dataValues.get(0).getDataGroup();
        sb.append("{\"dataSet\": \"" + dv1.getDataSet() + "\", \"completeDate\": \"" + dv1.getDateCollected().toString() + "\", \"period\": \"" + dv1.getPeriod() + "\", \"orgUnit\": \"" + dv1.getOrgUnit() + "\", \"dataValues\":[");
        dataValues.forEach(dv -> {
            sb.append(dv.toGridJson());
        });
        sb.append("]}");
        final String resp = sb.toString();
        return resp.replace(",\n" + "]}", "]}");
    }

    protected void SendDataFromODKToDHIS2() throws Exception {
        try {
            List<DataValueGroup> datavalueGroups = dataValueGroupDao.getDatasetValueGroups(Boolean.FALSE, null);
            //List<DataValueGroup> datavalueGroupsToBeSend = datavalueGroups.stream().limit(3).collect(Collectors.toList());
            datavalueGroups.forEach(dvg -> {
                String jsonStringTobePostedToDHIS = null;
                try {
                    TimeUnit.SECONDS.sleep(1);
                    jsonStringTobePostedToDHIS = getDataSetValuesForDHISPostRequest(dvg.getDatavalues());
                    log.info(jsonStringTobePostedToDHIS);
                    postDatasetValuesToDHIS(jsonStringTobePostedToDHIS, dvg);
                } catch (Exception ex) {
                    Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
            refreshDHISAnalyticsTables();
            log.info("RELEASE SERVER ==============>" + dataValueGroupDao.getDatasetValueGroups(Boolean.FALSE, null).size());
        } catch (JSONException e) {
            log.error("Error Occured ============>" + e.getMessage());
        }
    }

    protected String getSubmissionInstanceXml(String projectId, String xmlFormId, String instanceId, String odkToken) {
        try {
            final String instanceUri = configDao.getServerConfig().getOdkUrl() + "/v1/projects/" + projectId + "/forms/"
                    + xmlFormId + "/submissions/" + instanceId + ".xml";
            HttpEntity<String> entity = new HttpEntity<>(getODKHttpHeaders("XML", odkToken));
            ResponseEntity<String> result = restTemplate.exchange(instanceUri, HttpMethod.GET, entity, String.class);

            if (result.getStatusCode() == HttpStatus.OK) {
                String xmlString = restTemplate.exchange(instanceUri, HttpMethod.GET, entity, String.class).getBody();
                return xmlString;
            } else {
                return null;
            }
        } catch (RestClientException e) {
            log.error("Error Occured ============>" + e.getMessage());
            return null;
        }

    }

    protected void postMetaDataToDHIS2() {
        StringBuffer sb = new StringBuffer(1000);
        sb.append("{\"dataElements\":[");

        for (DataElement form : DataElement.values()) {
            sb.append("{\"name\":").append("\"").append("Denominator (" + form.getName() + ")").append("\",")
                    .append("\"shortName\":\"").append("Denominator (" + form.getName() + ")").append("\",")
                    .append("\"code\":\"").append("DENO-" + form.getDataElementSuffix()).append("\",")
                    .append("\"aggregationType\":\"").append("SUM").append("\",")
                    .append("\"domainType\":\"").append("AGGREGATE").append("\",")
                    .append("\"valueType\":\"").append("INTEGER_ZERO_OR_POSITIVE").append("\"},")
                    .append("\n");

            sb.append("{\"name\":").append("\"").append("Numerator (" + form.getName() + ")").append("\",")
                    .append("\"shortName\":\"").append("Numerator (" + form.getName() + ")").append("\",")
                    .append("\"code\":\"").append("NUME-" + form.getDataElementSuffix()).append("\",")
                    .append("\"aggregationType\":\"").append("SUM").append("\",")
                    .append("\"domainType\":\"").append("AGGREGATE").append("\",")
                    .append("\"valueType\":\"").append("INTEGER_ZERO_OR_POSITIVE").append("\"},")
                    .append("\n");

            sb.append("{\"name\":").append("\"").append("Max Score (" + form.getName() + ")").append("\",")
                    .append("\"shortName\":\"").append("Max Score (" + form.getName() + ")").append("\",")
                    .append("\"code\":\"").append("MAX-" + form.getDataElementSuffix()).append("\",")
                    .append("\"aggregationType\":\"").append("SUM").append("\",")
                    .append("\"domainType\":\"").append("AGGREGATE").append("\",")
                    .append("\"valueType\":\"").append("INTEGER_ZERO_OR_POSITIVE").append("\"},")
                    .append("\n");

            sb.append("{\"name\":").append("\"").append("Form Name (" + form.getName() + ")").append("\",")
                    .append("\"shortName\":\"").append("Form Name (" + form.getName() + ")").append("\",")
                    .append("\"code\":\"").append("FORM-" + form.getDataElementSuffix()).append("\",")
                    //.append("\"aggregationType\":\"").append("SUM").append("\",")
                    .append("\"domainType\":\"").append("AGGREGATE").append("\",")
                    .append("\"valueType\":\"").append("LONG_TEXT").append("\"},")
                    .append("\n");

            sb.append("{\"name\":").append("\"").append("Score (" + form.getName() + ")").append("\",")
                    .append("\"shortName\":\"").append("Score (" + form.getName() + ")").append("\",")
                    .append("\"code\":\"").append("SCORE-" + form.getDataElementSuffix()).append("\",")
                    .append("\"aggregationType\":\"").append("SUM").append("\",")
                    .append("\"domainType\":\"").append("AGGREGATE").append("\",")
                    .append("\"valueType\":\"").append("INTEGER_ZERO_OR_POSITIVE").append("\"},")
                    .append("\n");

        }
        sb.append("]}");
        final String resp = sb.toString();

        log.info(resp);

        try {
            final String submissionsUri = configDao.getServerConfig().getDhisUrl() + "/api/metadata";
            HttpEntity<String> entity = new HttpEntity<>(resp.replace(",\n" + "]}", "]}"), getDHISHttpHeaders(getDHISBasicToken()));
            ResponseEntity<String> result = restTemplate.exchange(submissionsUri, HttpMethod.POST, entity, String.class);
            log.info("Data Post Response ************************************************************" + result.getStatusCode());

        } catch (JSONException | RestClientException e) {
            log.error("Error Occured While Getting dataValueSets ==>" + e.getMessage());
        }

    }

    protected File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    protected File getFileFromResourceFolder(String folderName, String fileName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + folderName + "/" + fileName);
            File reportfile = resource.getFile();
            return reportfile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void saveDataSummary() {

        log.info("**************************** SAVING SUMMARY ****************************************");
        try {
            List<DataValueGroup> datavalueGroups = dataValueGroupDao.getDatasetValueGroups(null, Boolean.FALSE);
            datavalueGroups.forEach(dvg -> {
                SummaryData sData = new SummaryData(dvg.getOrgUnitCode(), dvg.getFormName(), dvg.getDateCollected(), dvg.getNumerator(), dvg.getDenominator(), dvg.getScore());
                dvg.setDataSendTosummary(Boolean.TRUE);
                try {
                    dataDao.save(sData);
                    dataValueGroupDao.save(dvg);
                } catch (Exception ex) {
                    Logger.getLogger(CommonUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            log.info("**************************** DONE SAVING SUMMARY ****************************************");
        } catch (Exception e) {
            log.error("Error Occured While Getting Submission ==>" + e.getMessage());
        }
    }

    protected List<String> getOrgunitFacilitiesCodes(String orgUnitId, String orgUnitCode) {
        OrganizationUnit org = null;
        if (orgUnitId != null) {
            org = orgDao.findById(orgUnitId);
        }
        if (orgUnitCode != null) {
            org = orgDao.findByOrgunitCode(orgUnitCode);
        }
        List<String> unitCodes = new ArrayList<>();
        if (org == null) {
            return unitCodes;
        } else if (org.getParent() == null) {
            List<OrganizationUnit> counties = org.getChildren();
            counties.forEach(county -> {
                List<OrganizationUnit> subcounties = county.getChildren();
                subcounties.forEach(subcounty -> {
                    List<OrganizationUnit> fac = subcounty.getChildren();
                    fac.forEach(f -> {
                        unitCodes.add(f.getCode());
                    });
                });
            });
        } else if (org.getCountParent() == null && org.getSubCountParent() == null) {
            List<OrganizationUnit> subcounties = org.getChildren();
            subcounties.forEach(subcounty -> {
                List<OrganizationUnit> fac = subcounty.getChildren();
                fac.forEach(f -> {
                    unitCodes.add(f.getCode());
                });
            });

        } else if (org.getCountParent() != null && org.getSubCountParent() == null) {
            List<OrganizationUnit> fac = org.getChildren();
            fac.forEach(f -> {
                unitCodes.add(f.getCode());
            });

        } else if (org.getChildren().size() < 1) {
            unitCodes.add(org.getCode());
        }

        return unitCodes;
    }

    protected LocalDate getDateFromString(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime = LocalDate.parse(dateStr, formatter);
        return dateTime;
    }
    
    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    return dateToConvert.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
}

}
