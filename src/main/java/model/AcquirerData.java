package model;

import type.Mtid;

import java.util.Set;

public class AcquirerData extends MessageTransaction {
    private Set<TagValue> acquireRecords;

    public AcquirerData(Mtid messageType, Set<TagValue> acquireRecords) {
        setMessageType(messageType);
        this.acquireRecords = acquireRecords;
    }

    public void addAcquireRecord(TagValue record) {
        this.acquireRecords.add(record);
    }
    
}
