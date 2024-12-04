package com.nft.nftsite.utils;

public class RegexPattern {

    public static final String EMAIL = "^([A-Z|a-z|0-9](\\.|_){0,1})+[A-Z|a-z|0-9]\\@([A-Z|a-z|0-9])+((\\.){0,1}[A-Z|a-z|0-9]){2}\\.[a-z]{2,3}$";

    public static final String PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$";

    public static final String OLD_PASSWORD_PATTERN = "^((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,64})$";

    public static final String TAG = "^([A-Za-z0-9_]{4,15})$";

    public static final String SLUG = "^([A-Za-z0-9-]{2,50})$";

}
