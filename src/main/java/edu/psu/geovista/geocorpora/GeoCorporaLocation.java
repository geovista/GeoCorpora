/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.psu.geovista.geocorpora;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author scottpez
 */
public class GeoCorporaLocation {

    public static String FEATURE_ID_FIELD = "feature_id";
    public static String TWEET_ID_FIELD = "tweet_id_str";
    public static String TWEET_TEXT_FIELD = "tweet_text";
    public static String TWEET_CREATED_AT_FIELD = "tweet_created_at";
    public static String TWEET_USER_LOCATION_FIELD = "tweet_user_location";
    public static String TWEET_USER_TIMEZONE_FIELD = "tweet_user_timezone";
    public static String TWEET_COORDINATES_LATITUDE_FIELD = "tweet_coordinates_latitude";
    public static String TWEET_COORDINATES_LONGITUDE_FIELD = "tweet_coordinates_longitude";
    public static String TWEET_PLACEID_FIELD = "tweet_placeid";
    public static String CHAR_POSITION_FIELD = "char_position";
    public static String TEXT_FIELD = "text";
    public static String GEONAMEID_FIELD = "geoNameId";
    public static String TOPONYM_FIELD = "toponym";
    public static String COUNTRY_CODE_FIELD = "country_code";
    public static String LONGITUDE_FIELD = "longitude";
    public static String LATITUDE_FIELD = "latitude";
    public static String SURROGATE_GEONAMEID_FIELD = "surrogate_geoNameId";
    public static String SURROGATE_NAME_FIELD = "surrogate_name";
    public static String SURROGATE_COUNTRY_CODE_FIELD = "surrogate_country_code";
    public static String SURROGATE_GEOJSON_FIELD = "surrogate_geojson";
    public static String IS_UNCERTAIN_SEMANTICS_FIELD = "uncertain_semantics";
    public static String IS_NON_OVERLAPPING_AMGIGUOUS_FIELD = "non_overlapping_ambiguous";
    public static String IS_OVERLAPPING_AMBIGUOUS_FIELD = "overlapping_ambiguous";
    public static String IS_VAGUE_FIELD = "vague";
    public static String IS_NOT_IN_GEONAMES_FIELD = "not_in_geonames";

    private int featureId;
    private String tweetId;
    private String tweetText;
    private String tweetCreatedAt;
    private String tweetUserLocation;
    private String tweetUserTimeZone;
    private double tweetCoordinatesLatitude;
    private double tweetCoordinatesLongitude;
    private int tweetPlaceId;
    private int charPosition;
    private String text;
    private String geoNameId;
    private String toponym;
    private String countryCode;
    private double longitude;
    private double latitude;
    private ArrayList<GeoCorporaSurrogate> geoCorporaSurrogates;
    private boolean uncertainSemantics;
    private boolean nonOverlappingAmbiguous;
    private boolean overlappingAmbiguous;
    private boolean vague;
    private boolean notInGeoNames;

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTweetCreatedAt() {
        return tweetCreatedAt;
    }

    public void setTweetCreatedAt(String tweetCreatedAt) {
        this.tweetCreatedAt = tweetCreatedAt;
    }

    public String getTweetUserLocation() {
        return tweetUserLocation;
    }

    public void setTweetUserLocation(String tweetUserLocation) {
        this.tweetUserLocation = tweetUserLocation;
    }

    public String getTweetUserTimeZone() {
        return tweetUserTimeZone;
    }

    public void setTweetUserTimeZone(String tweetUserTimeZone) {
        this.tweetUserTimeZone = tweetUserTimeZone;
    }

    public double getTweetCoordinatesLatitude() {
        return tweetCoordinatesLatitude;
    }

    public void setTweetCoordinatesLatitude(double tweetCoordinatesLatitude) {
        this.tweetCoordinatesLatitude = tweetCoordinatesLatitude;
    }

    public double getTweetCoordinatesLongitude() {
        return tweetCoordinatesLongitude;
    }

    public void setTweetCoordinatesLongitude(double tweetCoordinatesLongitude) {
        this.tweetCoordinatesLongitude = tweetCoordinatesLongitude;
    }

    public int getTweetPlaceId() {
        return tweetPlaceId;
    }

    public void setTweetPlaceId(int tweetPlaceId) {
        this.tweetPlaceId = tweetPlaceId;
    }

    public int getCharPosition() {
        return charPosition;
    }

    public void setCharPosition(int charPosition) {
        this.charPosition = charPosition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGeoNameId() {
        return geoNameId;
    }

    public void setGeoNameId(String geoNameId) {
        this.geoNameId = geoNameId;
    }

    public String getToponym() {
        return toponym;
    }

    public void setToponym(String toponym) {
        this.toponym = toponym;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ArrayList<GeoCorporaSurrogate> getGeoCorporaSurrogates() {
        return geoCorporaSurrogates;
    }

    public void parseSurrogateGeoJSON(JSONObject surrogateGeoJSON) {
        this.geoCorporaSurrogates = new ArrayList<>();
        JSONArray arr = (JSONArray) surrogateGeoJSON.get("features");
        for (int i = 0; i < arr.size(); i++) {
            JSONObject surrogateFeature = (JSONObject) arr.get(i);
            JSONObject surrogateProperties = (JSONObject) surrogateFeature.get("properties");
            JSONArray tempArray = (JSONArray) surrogateProperties.get("positions");
            int[] surrogatePositions = new int[tempArray.size()];
            for (int j = 0; j < tempArray.size(); j++) {
                surrogatePositions[j] = Integer.valueOf(tempArray.get(j).toString());
            }
            JSONObject surrogateGeometry = (JSONObject) surrogateFeature.get("geometry");
            String surrogateGeoNameId = String.valueOf((Long) surrogateProperties.get("geoNameId"));
            String surrogateToponym = (String) surrogateProperties.get("toponym");
            String surrogateCountryCode = (String) surrogateProperties.get("countryCode");
            JSONArray surrogateCoordinates = (JSONArray) surrogateGeometry.get("coordinates");
            GeoCorporaSurrogate geoCorporaSurrogate = new GeoCorporaSurrogate();
            geoCorporaSurrogate.setPositions(surrogatePositions);
            geoCorporaSurrogate.setGeoNameId(surrogateGeoNameId);
            geoCorporaSurrogate.setToponym(surrogateToponym);
            geoCorporaSurrogate.setCountryCode(surrogateCountryCode);
            geoCorporaSurrogate.setLongitude(Double.valueOf(surrogateCoordinates.get(0).toString()));
            geoCorporaSurrogate.setLatitude(Double.valueOf(surrogateCoordinates.get(1).toString()));
            this.geoCorporaSurrogates.add(geoCorporaSurrogate);
        }
    }

    public boolean isUncertainSemantics() {
        return uncertainSemantics;
    }

    public void setUncertainSemantics(boolean uncertainSemantics) {
        this.uncertainSemantics = uncertainSemantics;
    }

    public boolean isNonOverlappingAmbiguous() {
        return nonOverlappingAmbiguous;
    }

    public void setNonOverlappingAmbiguous(boolean nonOverlappingAmbiguous) {
        this.nonOverlappingAmbiguous = nonOverlappingAmbiguous;
    }

    public boolean isOverlappingAmbiguous() {
        return overlappingAmbiguous;
    }

    public void setOverlappingAmbiguous(boolean overlappingAmbiguous) {
        this.overlappingAmbiguous = overlappingAmbiguous;
    }

    public boolean isVague() {
        return vague;
    }

    public void setVague(boolean vague) {
        this.vague = vague;
    }

    public boolean isNotInGeoNames() {
        return notInGeoNames;
    }

    public void setNotInGeoNames(boolean notInGeoNames) {
        this.notInGeoNames = notInGeoNames;
    }
    

}
