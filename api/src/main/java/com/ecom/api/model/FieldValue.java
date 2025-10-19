package com.ecom.api.model;

import com.ecom.api.type.FieldsNFile;

import java.io.Serializable;

public class FieldValue implements Serializable {
    private FieldsNFile field;
    private String value;

    public FieldValue(FieldsNFile field, String value) {
        this.field = field;
        this.value = value;
    }

    public FieldsNFile getField() {
        return field;
    }
    
    public String getValue() {
        return value;
    }
}
