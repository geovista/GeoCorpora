/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.psu.geovista.geocorpora;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import twitter4j.Status;

/**
 *
 * @author scottpez
 */
public final class CorpusFromCSV {

    private HashMap<String, GeoCorporaTweet> corpus;

    private int index = 0;
    private List<CSVRecord> myRecords;
    private List<String> keys;

    /**
     * @param args the command line arguments
     * @throws java.io.UnsupportedEncodingException
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {

        String pathToCSVFile = "geocorpora_1506879947339.tsv";

        if (args != null && args.length > 0 && !args[0].equals("")) {
            pathToCSVFile = args[0];
        }

        CorpusFromCSV corpus = new CorpusFromCSV(pathToCSVFile);

        GeoCorporaTweet geoCorporaTweet = null;
        while ((geoCorporaTweet = corpus.nextTweet(true, true, true, true, true)) != null) {
            // code for processing a tweet goes here
            Logger.getLogger(CorpusFromCSV.class.getName()).log(Level.INFO, 
                    "Tweet with ID " + geoCorporaTweet.getTweetId() + " has a location with desired type.");

            GeoCorporaLocation geoCorporaLocation = null;
            while ((geoCorporaLocation = geoCorporaTweet.nextLocation(true, true, true, true, true)) != null) {
                // code for processing individual locations in the tweet goes here
                Logger.getLogger(CorpusFromCSV.class.getName()).log(Level.INFO,
                        "Location " + geoCorporaLocation.getToponym() + " in tweet with ID " 
                                + geoCorporaTweet.getTweetId() + " has the desired type.");
            }
        }

        String pathToCSVFileWithTweetText = FilenameUtils.getFullPath(pathToCSVFile) + 
                FilenameUtils.getBaseName(pathToCSVFile) + "_withTweetText." 
                + FilenameUtils.getExtension(pathToCSVFile);
        corpus.writeCorpusToCSV(pathToCSVFileWithTweetText);

    }

    public CorpusFromCSV(String pathToCSVFile) throws FileNotFoundException, IOException {
        this.corpus = readCorpusFromCSV(pathToCSVFile);
        this.keys = new ArrayList<>(this.corpus.keySet());
    }

    private GeoCorporaTweet nextTweet(boolean containsUncertainSemantics, boolean containsNonOverlappingAmbiguous,
            boolean containsOverlappingAmbiguous, boolean containsVague, boolean containsNotInGeonames) {
        index++;
        GeoCorporaTweet tweet = null;
        if (index < this.keys.size()) {
            tweet = this.corpus.get(this.keys.get(index - 1));
            while (!testTweet(tweet, containsUncertainSemantics, containsNonOverlappingAmbiguous,
                    containsOverlappingAmbiguous, containsVague, containsNotInGeonames) && index < this.keys.size()) {
                index++;
                tweet = this.corpus.get(this.keys.get(index - 1));
            }
        }
        return tweet;
    }

    private boolean testTweet(GeoCorporaTweet tweet, boolean containsUncertainSemantics, boolean containsNonOverlappingAmbiguous,
            boolean containsOverlappingAmbiguous, boolean containsVague, boolean containsNotInGeonames) {

        if (containsUncertainSemantics && tweet.containsUncertainSemantics()) {
            return true;
        }
        if (containsNonOverlappingAmbiguous && tweet.containsNonOverlappingAmbiguous()) {
            return true;
        }
        if (containsOverlappingAmbiguous && tweet.containsOverlappingAmbiguous()) {
            return true;
        }
        if (containsVague && tweet.containsVague()) {
            return true;
        }
        if (containsNotInGeonames && tweet.containsNotInGeonames()) {
            return true;
        }

        return false;
    }

    public HashMap<String, GeoCorporaTweet> readCorpusFromCSV(String filePath) throws UnsupportedEncodingException, 
            FileNotFoundException, IOException {
        Reader in = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
        Iterable<CSVRecord> records = CSVFormat.TDF.withFirstRecordAsHeader().withEscape('\\').parse(in);

        HashMap<String, GeoCorporaTweet> geoCorporaTweets = new HashMap<>();

        this.myRecords = new ArrayList<>();
        for (CSVRecord record : records) {
            this.myRecords.add(record);
            int featureId = Integer.parseInt(record.get(GeoCorporaLocation.FEATURE_ID_FIELD));
            String tweetId = record.get(GeoCorporaLocation.TWEET_ID_FIELD);
            int charPosition = Integer.parseInt(record.get(GeoCorporaLocation.CHAR_POSITION_FIELD));
            String text = record.get(GeoCorporaLocation.TEXT_FIELD);
            String geoNameId = record.get(GeoCorporaLocation.GEONAMEID_FIELD);
            String toponym = record.get(GeoCorporaLocation.TOPONYM_FIELD);
            String countryCode = record.get(GeoCorporaLocation.COUNTRY_CODE_FIELD);
            String tempValue = record.get(GeoCorporaLocation.LONGITUDE_FIELD);
            double longitude = -999.99;
            if (!tempValue.equals("")) {
                longitude = Double.parseDouble(tempValue);
            }
            tempValue = record.get(GeoCorporaLocation.LATITUDE_FIELD);
            double latitude = -999.99;
            if (!tempValue.equals("")) {
                latitude = Double.parseDouble(tempValue);
            }
            tempValue = record.get(GeoCorporaLocation.SURROGATE_GEOJSON_FIELD);
            JSONObject surrogateGeoJSON = null;
            if (!tempValue.equals("")) {
                surrogateGeoJSON = (JSONObject) JSONValue.parse(tempValue);
            }
            boolean uncertainSemantics = false;
            tempValue = record.get(GeoCorporaLocation.IS_UNCERTAIN_SEMANTICS_FIELD);
            if (!tempValue.equals("")) {
                uncertainSemantics = Boolean.parseBoolean(tempValue);
            }
            boolean nonOverlappingAmbiguous = false;
            tempValue = record.get(GeoCorporaLocation.IS_NON_OVERLAPPING_AMGIGUOUS_FIELD);
            if (!tempValue.equals("")) {
                nonOverlappingAmbiguous = Boolean.parseBoolean(tempValue);
            }
            boolean overlappingAmbiguous = false;
            tempValue = record.get(GeoCorporaLocation.IS_OVERLAPPING_AMBIGUOUS_FIELD);
            if (!tempValue.equals("")) {
                overlappingAmbiguous = Boolean.parseBoolean(tempValue);
            }
            boolean vague = false;
            tempValue = record.get(GeoCorporaLocation.IS_VAGUE_FIELD);
            if (!tempValue.equals("")) {
                vague = Boolean.parseBoolean(tempValue);
            }
            boolean notInGeoNames = false;
            tempValue = record.get(GeoCorporaLocation.IS_NOT_IN_GEONAMES_FIELD);
            if (!tempValue.equals("")) {
                notInGeoNames = Boolean.parseBoolean(tempValue);
            }
            GeoCorporaLocation geoCorporaLocation = new GeoCorporaLocation();
            geoCorporaLocation.setFeatureId(featureId);
            geoCorporaLocation.setTweetId(tweetId);
            geoCorporaLocation.setCharPosition(charPosition);
            geoCorporaLocation.setGeoNameId(geoNameId);
            geoCorporaLocation.setToponym(toponym);
            geoCorporaLocation.setCountryCode(countryCode);
            geoCorporaLocation.setLongitude(longitude);
            geoCorporaLocation.setLatitude(latitude);
            if (surrogateGeoJSON != null) {
                geoCorporaLocation.parseSurrogateGeoJSON(surrogateGeoJSON);
            }
            geoCorporaLocation.setUncertainSemantics(uncertainSemantics);
            geoCorporaLocation.setNonOverlappingAmbiguous(nonOverlappingAmbiguous);
            geoCorporaLocation.setOverlappingAmbiguous(overlappingAmbiguous);
            geoCorporaLocation.setVague(vague);
            geoCorporaLocation.setNotInGeoNames(notInGeoNames);
            
            if (geoCorporaTweets.get(tweetId) != null) {
                geoCorporaTweets.get(tweetId).addGeoCorporaLocation(geoCorporaLocation);
            } else {
                GeoCorporaTweet geoCorporaTweet = new GeoCorporaTweet();
                geoCorporaTweet.setTweetId(tweetId);
                geoCorporaTweet.addGeoCorporaLocation(geoCorporaLocation);
                geoCorporaTweets.put(tweetId, geoCorporaTweet);
            }
        }

        // Use the Twitter API to get the tweet text from the tweet ID.
        GetTweets getTweetById = new GetTweets();
        Set<String> tweetIds = geoCorporaTweets.keySet();
        for (String tweetId : tweetIds) {
            Status status = getTweetById.getTweetStatusById(tweetId);
            if (status != null) {
                String tweetText = status.getText();
                geoCorporaTweets.get(tweetId).setTweetText(tweetText);

                // If you have hit your Twitter rate limit, we will sleep for 15 minutes then resume. Please be patient.
                getTweetById.handleRateLimit();
            }
        }

        return geoCorporaTweets;
    }

    public void writeCorpusToCSV(String filePath) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        FileWriter fileWriter = new FileWriter(filePath);
        CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, CSVFormat.TDF.withEscape('\\').withQuoteMode(QuoteMode.NONE));

        // print header
        csvFilePrinter.printRecord(GeoCorporaLocation.FEATURE_ID_FIELD,
                GeoCorporaLocation.TWEET_ID_FIELD,
                GeoCorporaLocation.CHAR_POSITION_FIELD,
                GeoCorporaLocation.TEXT_FIELD,
                GeoCorporaLocation.GEONAMEID_FIELD,
                GeoCorporaLocation.TOPONYM_FIELD,
                GeoCorporaLocation.COUNTRY_CODE_FIELD,
                GeoCorporaLocation.LONGITUDE_FIELD,
                GeoCorporaLocation.LATITUDE_FIELD,
                GeoCorporaLocation.SURROGATE_GEOJSON_FIELD,
                GeoCorporaLocation.IS_UNCERTAIN_SEMANTICS_FIELD,
                GeoCorporaLocation.IS_VAGUE_FIELD,
                GeoCorporaLocation.IS_OVERLAPPING_AMBIGUOUS_FIELD,
                GeoCorporaLocation.IS_NON_OVERLAPPING_AMGIGUOUS_FIELD,
                GeoCorporaLocation.IS_NOT_IN_GEONAMES_FIELD,
                GeoCorporaLocation.TWEET_TEXT_FIELD);
        for (CSVRecord record : this.myRecords) {
            Object[] obj = new Object[record.size() + 1];
            for (int i = 0; i < record.size(); i++) {
                obj[0] = record.get(i);
            }
            String tweetId = record.get(GeoCorporaLocation.TWEET_ID_FIELD);
            obj[obj.length - 1] = this.corpus.get(tweetId).getTweetText();
            csvFilePrinter.printRecord(obj);
        }

        csvFilePrinter.close();
        fileWriter.close();
    }
}
