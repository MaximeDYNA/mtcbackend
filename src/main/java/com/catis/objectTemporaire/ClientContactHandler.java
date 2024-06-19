package com.catis.objectTemporaire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientContactHandler {

    private Long clientId;
    private Long contactId;

    public ClientContactHandler(Long clientId, Long contactId) {
        super();
        this.clientId = clientId;
        this.contactId = contactId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

}
