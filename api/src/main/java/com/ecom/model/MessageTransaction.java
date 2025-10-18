package com.ecom.model;

import com.ecom.type.Mtid;

import java.io.Serializable;

public abstract class MessageTransaction implements Serializable {
    private Mtid messageType;

    public Mtid getMessageType() {
        return messageType;
    }

    public void setMessageType(Mtid messageType) {
        this.messageType = messageType;
    }
}
