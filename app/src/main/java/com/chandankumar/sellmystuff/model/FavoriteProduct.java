package com.chandankumar.sellmystuff.model;

public class FavoriteProduct {
    private String productId;

    public FavoriteProduct() {
    }

    public FavoriteProduct(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    @Override
    public String toString() {
        return "FavoriteProduct{" +
                "productId='" + productId + '\'' +
                '}';
    }
}
