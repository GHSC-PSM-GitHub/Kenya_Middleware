/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author ssvmoh
 */
@Entity
@Table(name = "products_tbl")

public class ProductAccountability implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "sub_id")
    private String instanceId;

    @Column(name = "date_collected")
    private LocalDate dateCollected;

    @Column(name = "product_name")
    private String product;

    @Column(name = "org_unit")
    private String orgUnit;

    private int openSOH;
    private int receivedFromSCA;
    private int totalSOH;
    private String dnote;
    private int qtyDispensed;
    private int negativeAdjust;
    private int positiveAdjust;
    private int actualCount;
    private BigDecimal perAccounted;
    private String sohExpected;
    private String county;
    private String subcounty;
    private String facility;
    private String deliveryAvailable;
    private String deliveryNumber;
    private String productReceived;
    private Integer qtyReceivedFromSupply;
    private Integer qtyDelivered;
    private Integer qtyOnBincard;
    private String deliveryDate;
    @Column(name = "numerator")
    private int numerator;
    @Column(name = "denominator")
    private int denominator;
    @Column(name = "score")
    private int score;


    @Transient
    private String fromDate;
    @Transient
    private String toDate;

    public ProductAccountability() {

    }

    public ProductAccountability(ProductAccountabilityBuilder builder) {
        this.dateCollected = builder.dateCollected;
        this.product = builder.product;
        this.orgUnit = builder.orgUnit;
        this.openSOH = builder.openSOH;
        this.dnote = builder.dnote;
        this.receivedFromSCA = builder.receivedFromSCA;
        this.totalSOH = builder.totalSOH;
        this.qtyDispensed = builder.qtyDispensed;
        this.negativeAdjust = builder.negativeAdjust;
        this.positiveAdjust = builder.positiveAdjust;
        this.actualCount = builder.actualCount;
        this.perAccounted = builder.perAccounted;
        this.sohExpected = builder.sohExpected;
        this.instanceId = builder.instanceId;
        this.county = builder.county;
        this.subcounty = builder.subcounty;
        this.facility = builder.facility;
        this.deliveryAvailable = builder.deliveryAvailable;
        this.deliveryNumber = builder.deliveryNumber;
        this.productReceived = builder.productReceived;
        this.qtyReceivedFromSupply = builder.qtyReceivedFromSupply;
        this.qtyDelivered = builder.qtyDelivered;
        this.qtyOnBincard = builder.qtyOnBincard;
        this.deliveryDate = builder.deliveryDate;
        this.numerator=builder.numerator;
        this.denominator=builder.denominator;
        this.score=builder.score;

    }

    public String getId() {
        return id;
    }

    public LocalDate getDateCollected() {
        return dateCollected;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public int getOpenSOH() {
        return openSOH;
    }

    public String getDnote() {
        return dnote;
    }

    public int getReceivedFromSCA() {
        return receivedFromSCA;
    }

    public int getTotalSOH() {
        return totalSOH;
    }

    public int getQtyDispensed() {
        return qtyDispensed;
    }

    public int getNegativeAdjust() {
        return negativeAdjust;
    }

    public int getPositiveAdjust() {
        return positiveAdjust;
    }

    /**
     * @return the sohExpected
     */
    public String getSohExpected() {
        return sohExpected;
    }

    /**
     * @return the perAccounted
     */
    public BigDecimal getPerAccounted() {
        return perAccounted;
    }

    /**
     * @param perAccounted the perAccounted to set
     */
    public void setPerAccounted(BigDecimal perAccounted) {
        this.perAccounted = perAccounted;
    }

    /**
     * @param sohExpected the sohExpected to set
     */
    public void setSohExpected(String sohExpected) {
        this.sohExpected = sohExpected;
    }

    public int getActualCount() {
        return actualCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDateCollected(LocalDate dateCollected) {
        this.dateCollected = dateCollected;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public void setOpenSOH(int openSOH) {
        this.openSOH = openSOH;
    }

    public void setDnote(String dnote) {
        this.dnote = dnote;
    }

    public void setReceivedFromSCA(int receivedFromSCA) {
        this.receivedFromSCA = receivedFromSCA;
    }

    public void setTotalSOH(int totalSOH) {
        this.totalSOH = totalSOH;
    }

    public void setQtyDispensed(int qtyDispensed) {
        this.qtyDispensed = qtyDispensed;
    }

    public void setNegativeAdjust(int negativeAdjust) {
        this.negativeAdjust = negativeAdjust;
    }

    public void setPositiveAdjust(int positiveAdjust) {
        this.positiveAdjust = positiveAdjust;
    }

    public void setActualCount(int actualCount) {
        this.actualCount = actualCount;
    }

    /**
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the submissionId
     */
    public String getSubmissionId() {
        return instanceId;
    }

    /**
     * @param submissionId the submissionId to set
     */
    public void setSubmissionId(String submissionId) {
        this.instanceId = submissionId;
    }

    /**
     * @return the county
     */
    public String getCounty() {
        return county;
    }

    /**
     * @param county the county to set
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * @return the facility
     */
    public String getFacility() {
        return facility;
    }

    /**
     * @param facility the facility to set
     */
    public void setFacility(String facility) {
        this.facility = facility;
    }

    /**
     * @return the fromDate
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;

    }

    /**
     * @return the subcounty
     */
    public String getSubcounty() {
        return subcounty;
    }

    /**
     * @param subcounty the subcounty to set
     */
    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    /**
     * @return the deliveryAvailable
     */
    public String getDeliveryAvailable() {
        return deliveryAvailable;
    }

    /**
     * @param deliveryAvailable the deliveryAvailable to set
     */
    public void setDeliveryAvailable(String deliveryAvailable) {
        this.deliveryAvailable = deliveryAvailable;
    }

    /**
     * @return the deliveryNumber
     */
    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    /**
     * @param deliveryNumber the deliveryNumber to set
     */
    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    /**
     * @return the productReceived
     */
    public String getProductReceived() {
        return productReceived;
    }

    /**
     * @param productReceived the productReceived to set
     */
    public void setProductReceived(String productReceived) {
        this.productReceived = productReceived;
    }

    /**
     * @return the qtyReceivedFromSupply
     */
    public Integer getQtyReceivedFromSupply() {
        return qtyReceivedFromSupply;
    }

    /**
     * @param qtyReceivedFromSupply the qtyReceivedFromSupply to set
     */
    public void setQtyReceivedFromSupply(Integer qtyReceivedFromSupply) {
        this.qtyReceivedFromSupply = qtyReceivedFromSupply;
    }

    /**
     * @return the qtyDelivered
     */
    public Integer getQtyDelivered() {
        return qtyDelivered;
    }

    /**
     * @param qtyDelivered the qtyDelivered to set
     */
    public void setQtyDelivered(Integer qtyDelivered) {
        this.qtyDelivered = qtyDelivered;
    }

    /**
     * @return the qtyOnBincard
     */
    public Integer getQtyOnBincard() {
        return qtyOnBincard;
    }

    /**
     * @param qtyOnBincard the qtyOnBincard to set
     */
    public void setQtyOnBincard(Integer qtyOnBincard) {
        this.qtyOnBincard = qtyOnBincard;
    }

    /**
     * @return the deliveryDate
     */
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public static class ProductAccountabilityBuilder {

        private LocalDate dateCollected;

        private String product;

        private String orgUnit;

        private int openSOH;
        private String dnote;
        private int receivedFromSCA;
        private int totalSOH;
        private int qtyDispensed;
        private int negativeAdjust;
        private int positiveAdjust;
        private int actualCount;
        private BigDecimal perAccounted;
        private String sohExpected;
        private String instanceId;
        private String county;
        private String subcounty;
        private String facility;
        private String deliveryAvailable;
        private String deliveryNumber;
        private String productReceived;
        private Integer qtyReceivedFromSupply;
        private Integer qtyDelivered;
        private Integer qtyOnBincard;
        private String deliveryDate;
        private int numerator;
        private int denominator;
        private int score;
        


        public ProductAccountabilityBuilder dateCollected(LocalDate dateCollected) {
            this.dateCollected = dateCollected;
            return this;
        }

        public ProductAccountabilityBuilder product(String product) {
            this.product = product;
            return this;
        }

        public ProductAccountabilityBuilder dnote(String dnote) {
            this.dnote = dnote;
            return this;
        }

        public ProductAccountabilityBuilder orgUnit(String orgUnit) {
            this.orgUnit = orgUnit;
            return this;
        }

        public ProductAccountabilityBuilder openSOH(int openSOH) {
            this.openSOH = openSOH;
            return this;
        }

        public ProductAccountabilityBuilder receivedFromSCA(int receivedFromSCA) {
            this.receivedFromSCA = receivedFromSCA;
            return this;
        }

        public ProductAccountabilityBuilder totalSOH(int totalSOH) {
            this.totalSOH = totalSOH;
            return this;
        }

        public ProductAccountabilityBuilder qtyDispensed(int qtyDispensed) {
            this.qtyDispensed = qtyDispensed;
            return this;
        }

        public ProductAccountabilityBuilder negativeAdjust(int negativeAdjust) {
            this.negativeAdjust = negativeAdjust;
            return this;
        }

        public ProductAccountabilityBuilder positiveAdjust(int positiveAdjust) {
            this.positiveAdjust = positiveAdjust;
            return this;
        }

        public ProductAccountabilityBuilder actualCount(int actualCount) {
            this.actualCount = actualCount;
            return this;
        }
        public ProductAccountabilityBuilder numerator(int numerator) {
            this.numerator = numerator;
            return this;
        }        
        public ProductAccountabilityBuilder denominator(int denominator) {
            this.denominator = denominator;
            return this;
        }
        public ProductAccountabilityBuilder score(int score) {
            this.score = score;
            return this;
        }        
        public ProductAccountabilityBuilder county(String county) {
            this.county = county;
            return this;
        }

        public ProductAccountabilityBuilder subcounty(String subcounty) {
            this.subcounty = subcounty;
            return this;
        }

        public ProductAccountabilityBuilder facility(String facility) {
            this.facility = facility;
            return this;
        }

        public ProductAccountabilityBuilder deliveryAvailable(String deliveryAvailable) {
            this.deliveryAvailable = deliveryAvailable;
            return this;
        }

        public ProductAccountabilityBuilder deliveryNumber(String deliveryNumber) {
            this.deliveryNumber = deliveryNumber;
            return this;
        }

        public ProductAccountabilityBuilder productReceived(String productReceived) {
            this.productReceived = productReceived;
            return this;
        }

        public ProductAccountabilityBuilder deliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public ProductAccountabilityBuilder qtyReceivedFromSupply(int qtyReceivedFromSupply) {
            this.qtyReceivedFromSupply = qtyReceivedFromSupply;
            return this;
        }

        public ProductAccountabilityBuilder qtyDelivered(int qtyDelivered) {
            this.qtyDelivered = qtyDelivered;
            return this;
        }

        public ProductAccountabilityBuilder qtyOnBincard(int qtyOnBincard) {
            this.qtyOnBincard = qtyOnBincard;
            return this;
        }


        public ProductAccountabilityBuilder perAccounted() {
            BigDecimal denominator = new BigDecimal(this.openSOH + this.receivedFromSCA + this.positiveAdjust);
            BigDecimal numerator = new BigDecimal(this.qtyDispensed + this.actualCount + this.negativeAdjust);

            BigDecimal percentageAcc = BigDecimal.ZERO;
            if (denominator.compareTo(BigDecimal.ZERO) > 0) {
                percentageAcc = (numerator.divide(denominator, 10, RoundingMode.HALF_EVEN)).multiply(new BigDecimal(100)).setScale(0, RoundingMode.HALF_EVEN);
            }

            this.perAccounted = percentageAcc;
            return this;
        }

        public ProductAccountabilityBuilder sohExpected(String sohExpected) {
            this.sohExpected = sohExpected;
            return this;
        }

        public ProductAccountabilityBuilder instanceId(String instanceId) {
            this.instanceId = instanceId;
            return this;
        }

        public ProductAccountability build() {
            ProductAccountability prod = new ProductAccountability(this);
            return prod;
        }
    }

    /**
     * @return the numerator
     */
    public int getNumerator() {
        return numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public int getDenominator() {
        return denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }
}
