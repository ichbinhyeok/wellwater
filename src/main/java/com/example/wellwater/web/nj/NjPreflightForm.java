package com.example.wellwater.web.nj;

import com.example.wellwater.nj.NjPreflightRequest;

import java.util.ArrayList;
import java.util.List;

public class NjPreflightForm {

    private String transactionType = "sale";
    private String waterSource = "private_well";
    private String address;
    private String municipalitySlug;
    private List<String> extraContexts = new ArrayList<>();
    private String channel = "direct";
    private String source = "main";
    private String partnerSlug;

    public NjPreflightRequest toRequest() {
        return new NjPreflightRequest(
                transactionType, waterSource, address, municipalitySlug, extraContexts, channel, source, partnerSlug
        );
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(String waterSource) {
        this.waterSource = waterSource;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMunicipalitySlug() {
        return municipalitySlug;
    }

    public void setMunicipalitySlug(String municipalitySlug) {
        this.municipalitySlug = municipalitySlug;
    }

    public List<String> getExtraContexts() {
        return extraContexts;
    }

    public void setExtraContexts(List<String> extraContexts) {
        this.extraContexts = extraContexts == null ? new ArrayList<>() : new ArrayList<>(extraContexts);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPartnerSlug() {
        return partnerSlug;
    }

    public void setPartnerSlug(String partnerSlug) {
        this.partnerSlug = partnerSlug;
    }
}
