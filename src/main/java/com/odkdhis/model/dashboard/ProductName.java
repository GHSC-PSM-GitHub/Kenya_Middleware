/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.odkdhis.model.dashboard;

/**
 *
 * @author ssvmoh
 */
public enum ProductName {
   
    AL_24_S("al24s"),						
    DMPA_INJ("dmpainj"),						
    TDF_3TC_DTG_ADULT("tdf3tcdtgadult"),						
    AMOXICILLIN_250MG_DT("amoxicillin250mgdt"),						
    HIV_SCREENING_TEST("hiv_screeningtest"),						
    AL_6("AL61"),					
    AL_12("AL121"),						
    AL_18("AL181"),						
    ARTESUNATE_INJ("Artesunateinj60mgvial1"),						
    MALARIA_RDTS("malariaRDTs1"),						
    NVP_LIQUID_50MG("NVliquid50mg/5mL1"),						
    ROD_IMPLANT1("RodImplant"),						
    ROD_IMPLANT2("RodImplant"),						
    AMOXYCILLIN_DT_250MG("amoxycillindt250mg1"),						
    AMOXYCILLIN_CAPS_250MG("amoxycillincaps250mg1"),						
    CEFIXIME_TABS_400MG("cefiximetabs400mg1"),						
    CEFTRIAXONE_INJ("ceftriaxoneinj1"),						
    CHLORHEXIDINE_GEL("Chlorhexidine"),				
    FLUCLOXACILLIN_CAPS("flucloxacillincaps1"),						
    ORS_ZINC_COPACK("ORS/ZincCoPack1"),						
    OXYTOCIN_INJECTION("Oxytocininjection10"),						
    PARACETAMOL_TABS_500MG("paracetamoltabs500mg1"),						
    COTTON_WOOL_ABSORBENT("cottonwoolabsorbent1"),						
    LATEX_EXAMINATION("latexexamination1"),						
    SURGICAL_GLOVES("surgicalgloves1"),
    ABC3TC120mg60mg("ABC3TC120mg60mg1");
    


    private ProductName(String name) {
        this.name = name;
    }
    
    private String name;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
