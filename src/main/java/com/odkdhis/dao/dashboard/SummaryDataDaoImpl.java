/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.dao.dashboard;

import com.odkdhis.commondao.GenericDaoImpl;
import com.odkdhis.model.FormName;
import com.odkdhis.model.dashboard.DisplayData;
import com.odkdhis.model.dashboard.SummaryData;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ssvmoh
 */
@Repository
@Transactional
public class SummaryDataDaoImpl extends GenericDaoImpl<SummaryData, String> implements SummaryDataDao {

    @Override
    public void save(SummaryData data) throws Exception {
        saveBean(data);
    }

    @Override
    public SummaryData findById(String Id) {
        return findBeanById(Id);
    }

    @Override
    public List<DisplayData> getDisplayDataList(String orgCode, Date from, Date to) {
        List<SummaryData> dataList = new ArrayList<>();
        List<DisplayData> displayDataList = new ArrayList<>();
        if (orgCode != null && from != null && to != null) {
            int count = 0;
            for (FormName formName : FormName.values()) {
                dataList = this.getFacilitySummaryData(formName, orgCode, from, to);
                displayDataList.add(getDisplayDataFromSummaryData(dataList, formName));
            }
            BigDecimal denTot = BigDecimal.ZERO;
            BigDecimal numTot = BigDecimal.ZERO;
            BigDecimal scoreTot = BigDecimal.ZERO;
            for (DisplayData d : displayDataList) {
                denTot = denTot.add((d.getDenominator() == null || d.getDenominator() == "") ? BigDecimal.ZERO : new BigDecimal(d.getDenominator().replaceAll(",", "")));
                numTot = numTot.add((d.getNumerator() == null || d.getNumerator() == "") ? BigDecimal.ZERO : new BigDecimal(d.getNumerator().replaceAll(",", "")));
                scoreTot = scoreTot.add((d.getScore() == null || d.getScore() == "") ? BigDecimal.ZERO : new BigDecimal(d.getScore().replaceAll(",", "").replaceAll(" %", "")));
            }
            BigDecimal scoreAv = getScore(numTot, denTot) ;
            displayDataList.add(new DisplayData("Total Score", formatter2.format(100), formatter.format(numTot), formatter2.format(denTot), formatter2.format(scoreAv)+" %", scoreComment(scoreAv.intValue())));
        } else {
            return new ArrayList<>();
        }
        return displayDataList;
    }

    private DisplayData getDisplayDataFromSummaryData(List<SummaryData> summary, FormName formName) {

        // String maxScore = String.valueOf(formName.getMaxScore());
        BigDecimal num = BigDecimal.ZERO;
        BigDecimal den = BigDecimal.ZERO;
        for (SummaryData sd : summary) {
            num = num.add(sd.getNumerator());
            den = den.add(sd.getDenominator());
        }
        num = summary.size() > 1 ? num.divide(new BigDecimal(summary.size()), 2, RoundingMode.CEILING) : num;
        den = summary.size() > 1 ? den.divide(new BigDecimal(summary.size()), 2, RoundingMode.CEILING) : den;
        BigDecimal score = getScore(num, new BigDecimal(formName.getMaxScore()));
        if(summary.size() < 1){
            return new DisplayData(formName.getName(), formatter.format(formName.getMaxScore()), "", "", "", "");
        }
        return new DisplayData(formName.getName(), formatter2.format(formName.getMaxScore()), formatter.format(num), formatter2.format(formName.getMaxScore()), formatter2.format(score)+" %", scoreComment(score.intValue()));

    }

    private String scoreComment(Integer score) {
        String comment = "";
        if (score > 79 && score < 101) {
            comment = "Excellent";
        }
        if (score > 64 && score < 80) {
            comment = "Good";
        }
        if (score > 49 && score < 65) {
            comment = "Average";
        }
        if (score < 50) {
            comment = "Poor";
        }
        return comment;
    }

    @Override
    public List<SummaryData> getFacilitySummaryData(FormName formName, String orgCode, Date from, Date to) {

        List<SummaryData> sdata = em.createQuery("select s from SummaryData s where s.formName = :fname and s.orgUnit = :code and s.dateCollected between :from and :to", SummaryData.class)
                .setParameter("fname", formName).setParameter("code", orgCode).setParameter("from", from).setParameter("to", to)
                .getResultList();
        return sdata;
    }

    @Override
    public void delete(String Id) throws Exception {
        deleteBeanById(Id);
    }

    private BigDecimal getScore(BigDecimal numerator, BigDecimal maxScore) {
        BigDecimal f =BigDecimal.ZERO;
        if (maxScore.compareTo(f)>0){
        f = numerator.divide(maxScore, 10, RoundingMode.CEILING);
        }
        
        return (f.multiply(new BigDecimal(100))).setScale(0, RoundingMode.HALF_UP);

    }
}
