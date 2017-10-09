/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.psu.geovista.geocorpora;

import java.util.ArrayList;

public class GeoCorporaTweets extends ArrayList<GeoCorporaTweet> {
    public GeoCorporaTweet getGeocorporaTweet(String tweetId) {
        for (GeoCorporaTweet geoCorporaTweet : this) {
            if (geoCorporaTweet.getTweetId().equals(tweetId)) {
                return geoCorporaTweet;
            }
        }
        return null;
    }
}
