/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.psu.geovista.geocorpora;

import java.util.ArrayList;

/**
 *
 * @author scottpez
 */
public class GeoCorporaTweet {

    private String tweetId;
    private String tweetText;
    private final ArrayList<GeoCorporaLocation> geoCorporaLocations;
    private boolean containsUncertainSemantics;
    private boolean containsNonOverlappingAmbiguous;
    private boolean containsOverlappingAmbiguous;
    private boolean containsVague;
    private boolean containsNotInGeonames;
    private int index = 0;

    public GeoCorporaTweet() {
        this.geoCorporaLocations = new ArrayList<>();
        this.containsUncertainSemantics = false;
        this.containsNonOverlappingAmbiguous = false;
        this.containsOverlappingAmbiguous = false;
        this.containsVague = false;
        this.containsNotInGeonames = false;
    }

    public void addGeoCorporaLocation(GeoCorporaLocation geoCorporaLocation) {
        this.geoCorporaLocations.add(geoCorporaLocation);
        if (geoCorporaLocation.isUncertainSemantics()) {
            this.containsUncertainSemantics = true;
        }
        if (geoCorporaLocation.isNonOverlappingAmbiguous()) {
            this.containsNonOverlappingAmbiguous = true;
        }
        if (geoCorporaLocation.isOverlappingAmbiguous()) {
            this.containsOverlappingAmbiguous = true;
        }
        if (geoCorporaLocation.isVague()) {
            this.containsVague = true;
        }
        if (geoCorporaLocation.isNotInGeoNames()) {
            this.containsNotInGeonames = true;
        }
    }

    GeoCorporaLocation nextLocation(boolean containsUncertainSemantics, boolean containsNonOverlappingAmbiguous,
            boolean containsOverlappingAmbiguous, boolean containsVague, boolean containsNotInGeonames) {
        index++;
        GeoCorporaLocation location = null;
        if (index < this.geoCorporaLocations.size()) {
            location = this.geoCorporaLocations.get(index - 1);
            while (!testLocation(location, containsUncertainSemantics, containsNonOverlappingAmbiguous,
                    containsOverlappingAmbiguous, containsVague, containsNotInGeonames) 
                    && index < this.geoCorporaLocations.size()) {
                index++;
                location = this.geoCorporaLocations.get(index - 1);
            }
        }
        return location;
    }

    private boolean testLocation(GeoCorporaLocation location, boolean containsUncertainSemantics, boolean containsNonOverlappingAmbiguous,
            boolean containsOverlappingAmbiguous, boolean containsVague, boolean containsNotInGeonames) {

        if (containsUncertainSemantics && location.isUncertainSemantics()) {
            return true;
        }
        if (containsNonOverlappingAmbiguous && location.isNonOverlappingAmbiguous()) {
            return true;
        }
        if (containsOverlappingAmbiguous && location.isOverlappingAmbiguous()) {
            return true;
        }
        if (containsVague && location.isVague()) {
            return true;
        }
        if (containsNotInGeonames && location.isNotInGeoNames()) {
            return true;
        }

        return false;
    }

    public String getTweetId() {
        return tweetId;
    }

    public void setTweetId(String tweetId) {
        this.tweetId = tweetId;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTweetText() {
        return tweetText;
    }

    public ArrayList<GeoCorporaLocation> getGeoCorporaLocations() {
        return geoCorporaLocations;
    }

    public boolean containsUncertainSemantics() {
        return containsUncertainSemantics;
    }

    public void setContainsUncertainSemantics(boolean containsUncertainSemantics) {
        this.containsUncertainSemantics = containsUncertainSemantics;
    }

    public boolean containsNonOverlappingAmbiguous() {
        return containsNonOverlappingAmbiguous;
    }

    public void setContainsNonOverlappingAmbiguous(boolean containsNonOverlappingAmbiguous) {
        this.containsNonOverlappingAmbiguous = containsNonOverlappingAmbiguous;
    }

    public boolean containsOverlappingAmbiguous() {
        return containsOverlappingAmbiguous;
    }

    public void setContainsOverlappingAmbiguous(boolean containOverlappingAmbiguous) {
        this.containsOverlappingAmbiguous = containOverlappingAmbiguous;
    }

    public boolean containsVague() {
        return containsVague;
    }

    public void setContainsVague(boolean containsVague) {
        this.containsVague = containsVague;
    }

    public boolean containsNotInGeonames() {
        return containsNotInGeonames;
    }

    public void setContainsNotInGeonames(boolean containsNotInGeonames) {
        this.containsNotInGeonames = containsNotInGeonames;
    }

}
