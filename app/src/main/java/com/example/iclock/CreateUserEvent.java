package com.example.iclock;

public class CreateUserEvent {
    private String eventOwner;
    private String description;
    private String userId;
    private String eventName;
    private String eventStartdate;
    private String eventEndDate;
    private String isCertificationAvailable;
    private String otherDetailOptional;
    private String imageUrl;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    private String contact;

    private void CreateUserEvent(){
        otherDetailOptional = "No Other Details Available";
    }

    public String getEventOwner() {
        return eventOwner;
    }

    public void setEventOwner(String eventOwner) {
        this.eventOwner = eventOwner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventStartdate() {
        return eventStartdate;
    }

    public void setEventStartdate(String eventStartdate) {
        this.eventStartdate = eventStartdate;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getIsCertificationAvailable() {
        return isCertificationAvailable;
    }

    public void setIsCertificationAvailable(String isCertificationAvailable) {
        this.isCertificationAvailable = isCertificationAvailable;
    }

    public String getOtherDetailOptional() {
        return otherDetailOptional;
    }

    public void setOtherDetailOptional(String otherDetailOptional) {
        this.otherDetailOptional = otherDetailOptional;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
