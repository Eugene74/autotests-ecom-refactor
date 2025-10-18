package com.ecom.type;

public enum Tags {
    Mtid15_ARF1("ARF1"),
    Mtid15_ARF2("ARF2"),
    Mtid15_ARF3("ARF3"),
    Mtid15_ETID("ETID"),
    Mtid15_PFID("PFID"),
    Mtid15_SBMR("SBMR"),
    Mtid15_INDX("INDX"),
    Mtid15_STRT("STRT"),
    Mtid15_DCCD("DCCD"),
    Mtid15_PSVT("PSVT"),
    Mtid15_WPRD("WPRD"),
    Mtid15_POSE("POSE"),
    Mtid15_QUAS("QUAS"),
    Mtid15_NADA("NADA"),
    Mtid15_DCCE("DCCE"),
    Mtid15_MSRV("MSRV"),
    Mtid16_SAAV("SAAV"),
    Mtid16_CAVV("CAVV"),
    Mtid16_3DSV("3DSV"),
    Mtid16_3DID("3DID");

    private final String value;
    
    Tags(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
}
