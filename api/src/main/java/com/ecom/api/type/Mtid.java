package com.ecom.api.type;

public enum Mtid {
    Mtid00(00),
    Mtid10(10),
    Mtid15(15),
    Mtid16(16),
    Mtid99(99);

    private final int value;
    
    Mtid(int value) {
        this.value = value;
    }
    
    public int getValue() { 
        return this.value;
    }
}
