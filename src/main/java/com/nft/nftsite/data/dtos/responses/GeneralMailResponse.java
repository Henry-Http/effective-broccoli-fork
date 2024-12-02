package com.nft.nftsite.data.dtos.responses;

public class GeneralMailResponse {

    private String email;

    public GeneralMailResponse() {
    }

    public GeneralMailResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "GeneralMailResponse{" +
                "email='" + email + '\'' +
                '}';
    }
}
