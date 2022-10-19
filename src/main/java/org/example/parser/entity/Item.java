package org.example.parser.entity;

public class Item {
    private String urlPhoto,name,status,price;

    public Item(String urlPhoto, String name, String status, String price) {
        this.urlPhoto = urlPhoto;
        this.name = name;
        this.status = status;
        this.price = price;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
