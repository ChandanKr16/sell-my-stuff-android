package com.chandankumar.sellmystuff.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Product implements Parcelable {
    private String id;
    private String name;
    private String category;
    private Double price;
    private Double weight;
    private String description;
    private List<String> imageList;
    private String userId;
    private String createdAt;
    private String paymentId;
    private Boolean isPaymentDone;
    private Boolean isFavorite;

    public Product(){}

    public Product(String name, String category, Double price, Double weight, String description, List<String> imageList, String userId, String createdAt, String paymentId, Boolean isPaymentDone, Boolean isFavorite) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.imageList = imageList;
        this.userId = userId;
        this.createdAt = createdAt;
        this.paymentId = paymentId;
        this.isPaymentDone = isPaymentDone;
        this.isFavorite = isFavorite;
    }

    public Product(String id, String name, String category, Double price, Double weight, String description, List<String> imageList, String userId, String createdAt, String paymentId, Boolean isPaymentDone, Boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.imageList = imageList;
        this.userId = userId;
        this.createdAt = createdAt;
        this.paymentId = paymentId;
        this.isPaymentDone = isPaymentDone;
        this.isFavorite = isFavorite;
    }

    public Product(String id, Product product){
        this(id, product.getName(), product.getCategory(), product.getPrice(), product.getWeight(), product.getDescription(),
                product.getImageList(), product.getUserId(), product.getCreatedAt(), product.getPaymentId(), product.getPaymentDone(), product.getFavorite());
    }


    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            weight = null;
        } else {
            weight = in.readDouble();
        }
        description = in.readString();
        imageList = in.createStringArrayList();
        userId = in.readString();
        createdAt = in.readString();
        paymentId = in.readString();
        byte tmpIsPaymentDone = in.readByte();
        isPaymentDone = tmpIsPaymentDone == 0 ? null : tmpIsPaymentDone == 1;
        byte tmpIsFavorite = in.readByte();
        isFavorite = tmpIsFavorite == 0 ? null : tmpIsFavorite == 1;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Boolean getPaymentDone() {
        return isPaymentDone;
    }

    public void setPaymentDone(Boolean paymentDone) {
        isPaymentDone = paymentDone;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(category);
        if (price == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(price);
        }
        if (weight == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(weight);
        }
        parcel.writeString(description);
        parcel.writeStringList(imageList);
        parcel.writeString(userId);
        parcel.writeString(createdAt);
        parcel.writeString(paymentId);
        parcel.writeByte((byte) (isPaymentDone == null ? 0 : isPaymentDone ? 1 : 2));
        parcel.writeByte((byte) (isFavorite == null ? 0 : isFavorite ? 1 : 2));
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", weight=" + weight +
                ", description='" + description + '\'' +
                ", imageList=" + imageList +
                ", userId='" + userId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", isPaymentDone=" + isPaymentDone +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
