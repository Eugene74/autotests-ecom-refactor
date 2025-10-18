package model;

import java.util.Set;

import static type.Mtid.Mtid10;

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
