package com.example.iclock;

import java.io.Serializable;

public class CreateUserBook implements Serializable {
    private String bookOwner;
    private String description;
    private String userId;
    private String bookName;
    private String price;
    private String publishingYear;
    private String otherDetailOptional;
    private String imageUrl;


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    private String contact;

    private void CreateUserBook(){
        otherDetailOptional = "No Other Details Available";
    }

    public String getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(String bookOwner) {
        this.bookOwner = bookOwner;
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

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getPublishingYear() {
        return publishingYear;
    }

    public void setPublishingYear(String publishingYear) {
        this.publishingYear = publishingYear;
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
