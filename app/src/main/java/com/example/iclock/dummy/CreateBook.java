package com.example.iclock.dummy;

import java.io.Serializable;

public class CreateBook implements Serializable {
    private String bookName;
    private String bookDescription;
    private String bookPrice;
    private String bookImageUrl;
    private String bookForSemester;
    private String bookSubject;
    private String bookForBranch;
    private String bookOwner;
    private String userId;
    private String publishingYear;
    private String contact;

    public String getBookOwner() {
        return bookOwner;
    }
    public String getContact(){
        return  contact;
    }
    public void setContact(String contact){
        this.contact = contact;
    }

    public void setBookOwner(String bookOwner) {
        this.bookOwner = bookOwner;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    private String otherDetailOptional;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public String getBookForSemester() {
        return bookForSemester;
    }

    public void setBookForSemester(String bookForSemester) {
        this.bookForSemester = bookForSemester;
    }

    public String getBookSubject() {
        return bookSubject;
    }

    public void setBookSubject(String bookSubject) {
        this.bookSubject = bookSubject;
    }

    public String getBookForBranch() {
        return bookForBranch;
    }

    public void setBookForBranch(String bookForBranch) {
        this.bookForBranch = bookForBranch;
    }
}
