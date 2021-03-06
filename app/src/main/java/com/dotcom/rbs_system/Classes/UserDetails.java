package com.dotcom.rbs_system.Classes;

public class UserDetails {
    private static UserDetails userDetailsObj = new UserDetails();

    Boolean isShopkeeper;
    String shopNmae,shopLogo,shopBanner;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Boolean getShopkeeper() {
        return isShopkeeper;
    }

    public void setShopkeeper(Boolean shopkeeper) {
        isShopkeeper = shopkeeper;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public String getShopNmae() {
        return shopNmae;
    }

    public void setShopNmae(String shopNmae) {
        this.shopNmae = shopNmae;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopBanner() {
        return shopBanner;
    }

    public void setShopBanner(String shopBanner) {
        this.shopBanner = shopBanner;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private UserDetails() {}

    public static UserDetails getInstance() {
        return userDetailsObj;
    }
}
