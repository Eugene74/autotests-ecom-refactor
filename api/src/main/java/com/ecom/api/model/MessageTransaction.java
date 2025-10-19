package com.ecom.api.model;

import com.ecom.api.type.Mtid;

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
