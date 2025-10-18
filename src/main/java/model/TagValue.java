package model;

import type.Tags;

import java.io.Serializable;

public class TagValue implements Serializable {
    private Tags tag;
    private String value;

    public TagValue(Tags tag, String value) {
        this.tag = tag;
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
