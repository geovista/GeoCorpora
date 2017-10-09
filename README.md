# GeoCorpora
The GeoCorpora project aims at creating corpora of fully geo-annotated texts (in particular microblog texts) and developing tools to support the corpus building process using crowd-sourcing and visual analytics approaches. Created corpora will be made publicly available in this repository. A first corpus of ~6000 geo-annotated tweets will be published here in the near future.

### Tutorial
Begin by adding your Twitter API credentials to the resources file resources/edu/psu/geovista/geocorpora/geocorpora.properties

Next, you may build the project using Maven.

The entry class is edu.psu.geovista.geocorpora.CorpusFromCSV. This class will read the tab delimited GeoCorpora file, parse the GeoCorpora into GeoCorporaTweet objects, and query the Twitter API for the tweet text. Finally, the GeoCorpora will be save to a tab delimited file with the tweet text for your records so that you do not need to do this in the future.

The absolute path to the GeoCorpora tab delimited file can be specified in the pathToCSVFile variable in the main method of CorpusFromCSV or added as an argument when running this method.

### Iterating over tweets
Tweets can be iterated through using code similar to that in the main method of CorpusFromCSV. The next tweets containing locations having any of the location types specified in the nextTweet() method, will be returned.

### Iterating over locations
Next, the individual locations in each of these tweets can be iterated over as seen in the inner loop using the location types specified in the nextLocation() method.
