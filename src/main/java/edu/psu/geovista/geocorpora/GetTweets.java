/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.psu.geovista.geocorpora;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author scottpez
 */
public class GetTweets {

    private final Twitter twitter;
    private RateLimitStatus rateLimitStatus;

    public static void main(String args[]) throws IOException, SQLException, TwitterException {

        GetTweets getTweets = new GetTweets();
        Status status = getTweets.getTweetStatusById("557916969433718785");
        System.out.println(status.getText());
    }

    public GetTweets() {

        Properties prop = new Properties();
        InputStream input = null;

        String aConsumerKey = "";
        String aConsumerSecret = "";
        String aRequestTokenURL = "";
        String aAuthorizeURL = "";
        String aAccessTokenURL = "";
        String aAccessToken = "";
        String aAccessTokenSecret = "";

        try {

            input = GetTweets.class.getResourceAsStream("geocorpora.properties");
            prop.load(input);
            aConsumerKey = prop.getProperty("aConsumerKey");
            aConsumerSecret = prop.getProperty("aConsumerSecret");
            aRequestTokenURL = prop.getProperty("aRequestTokenURL");
            aAuthorizeURL = prop.getProperty("aAuthorizeURL");
            aAccessTokenURL = prop.getProperty("aAccessTokenURL");
            aAccessToken = prop.getProperty("aAccessToken");
            aAccessTokenSecret = prop.getProperty("aAccessTokenSecret");

        } catch (IOException ex) {
            Logger.getLogger(GetTweets.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(GetTweets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(aConsumerKey);
        cb.setOAuthConsumerSecret(aConsumerSecret);
        cb.setOAuthRequestTokenURL(aRequestTokenURL);
        cb.setOAuthAuthorizationURL(aAuthorizeURL);
        cb.setOAuthAccessTokenURL(aAccessTokenURL);
        cb.setOAuthAccessToken(aAccessToken);
        cb.setOAuthAccessTokenSecret(aAccessTokenSecret);

        twitter = new TwitterFactory(cb.build()).getInstance();

    }

    public Status getTweetStatusById(String id_str) {
        Status status = null;
        try {
            status = twitter.showStatus(Long.parseLong(id_str));
            rateLimitStatus = status.getRateLimitStatus();
        } catch (TwitterException ex) {
            int statusCode = ex.getStatusCode();
            if (statusCode == 404) {
                Logger.getLogger(GetTweets.class.getName()).log(Level.INFO,
                        "Tweet with ID " + id_str + " has been deleted by the user.");
            } else {
                Logger.getLogger(GetTweets.class.getName()).log(Level.INFO,
                        "The status may exist but something else went wrong with Twitter.");
                Logger.getLogger(GetTweets.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
    }

    public void handleRateLimit() {
        if (rateLimitStatus != null) {
            int remaining = rateLimitStatus.getRemaining();
            if (remaining == 0) {
                Logger.getLogger(GetTweets.class.getName()).log(Level.INFO,
                        "Twitter rate limit hit, sleeping for 5 minutes.");
                int resetTime = rateLimitStatus.getSecondsUntilReset() + 5;
                int sleep = (resetTime * 1000);
                try {
                    Thread.sleep(sleep > 0 ? sleep : 0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GetTweets.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
