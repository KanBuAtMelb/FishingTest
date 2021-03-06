package com.example.fishingtest.Model;
/**
 *
 * Project: Fishing Competition
 * Author: Ziqi Zhang
 * Date: 8/06/2019
 * The class is object of Post
 *
 */
import java.io.Serializable;
import java.sql.Timestamp;

public class Post implements Serializable {
    public String postId;
    public String userId;
    public String compId;
    public String oriDownloadUrl;
    public String meaDownloadUrl;
    public String measuredData;
    public String fishingName;
    public String timeStamp;
    public double longitude;
    public double latitude;

    public Post() {
    }



    // constructing the post object
    public Post(String postId, String userId, String compId, String oriDownloadUrl, String meaDownloadUrl, String measuredData, String fishingName, String timeStamp, double longitude, double latitude) {
        this.postId = postId;
        this.userId = userId;
        this.compId = compId;
        this.oriDownloadUrl = oriDownloadUrl;
        this.meaDownloadUrl = meaDownloadUrl;
        this.measuredData = measuredData;
        this.fishingName = fishingName;
        this.timeStamp = timeStamp;
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public String getUserId() { return userId; }

    public String getMeasuredData() {
        return measuredData;
    }

    public String getOriDownloadUrl() {
        return oriDownloadUrl;
    }

    public String getMeaDownloadUrl() {
        return meaDownloadUrl;
    }

    public String getCompId() {
        return compId;
    }

    public String getPostId() {
        return postId;
    }

    public String getTimeStamp() { return timeStamp; }

    public double getLongitude() { return longitude; }

    public double getLatitude() { return latitude; }

    public String getFishingName() {
        return fishingName;
    }
}
