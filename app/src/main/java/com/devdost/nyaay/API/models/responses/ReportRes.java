package com.devdost.nyaay.API.models.responses;

import com.google.gson.annotations.SerializedName;

public class ReportRes {

    @SerializedName("status")
    private String status;

    @SerializedName("weapon")
    private double weapon;

    @SerializedName("alcohol")
    private double alcohol;

    @SerializedName("drugs")
    private double drugs;

    @SerializedName("nudity")
    private NudityRes nudity;

    class NudityRes {
        @SerializedName("raw")
        private double raw;

        @SerializedName("partial")
        private double partial;

        @SerializedName("safe")
        private double safe;
    }

    @SerializedName("offensive")
    private OffensiveRes offensive;

    class OffensiveRes {
        @SerializedName("prob")
        private double prob;
    }


    public String getStatus() {
        return status;
    }

    public double getWeapon() {
        return weapon;
    }

    public double getAlcohol() {
        return alcohol;
    }

    public double getDrugs() {
        return drugs;
    }

    public double getRaw() {
        return nudity.raw;
    }

    public double getPartial() {
        return nudity.partial;
    }

    public double getSafe() {
        return nudity.safe;
    }

    public double getProb() {
        return offensive.prob;
    }

}
