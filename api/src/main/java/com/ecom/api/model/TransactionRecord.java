package com.ecom.api.model;

import java.util.Set;

import static com.ecom.api.type.Mtid.Mtid10;

public class TransactionRecord extends MessageTransaction {
    
    private Set<FieldValue> records;

    public TransactionRecord(Set<FieldValue> records) {
        setMessageType(Mtid10);
        this.records = records;
    }

    public void addRecord(FieldValue record) {
        this.records.add(record);
    }
    
}
