package hu.latzkoo.organization.model;

import java.util.HashMap;

public class Organization {

    private int identifier;
    private boolean active;
    private String type;
    private String name;
    private String alias;
    private String telecom;
    private String address;
    private Organization partOf;
    private HashMap<String, String> contact;
    private String endpoint;

    public Organization() {
    }

    public Organization(int identifier, boolean active, String type, String name, String alias, String telecom,
                        String address, Organization partOf, HashMap<String, String> contact, String endpoint) {
        this.identifier = identifier;
        this.active = active;
        this.type = type;
        this.name = name;
        this.alias = alias;
        this.telecom = telecom;
        this.address = address;
        this.partOf = partOf;
        this.contact = contact;
        this.endpoint = endpoint;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTelecom() {
        return telecom;
    }

    public void setTelecom(String telecom) {
        this.telecom = telecom;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Organization getPartOf() {
        return partOf;
    }

    public void setPartOf(Organization partOf) {
        this.partOf = partOf;
    }

    public HashMap<String, String> getContact() {
        return contact;
    }

    public void setContact(HashMap<String, String> contact) {
        this.contact = contact;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
