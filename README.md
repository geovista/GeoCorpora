# GeoCorpora
The GeoCorpora project aims at creating corpora of fully geo-annotated texts (in particular microblog texts) and developing tools to support the corpus building process using crowd-sourcing and visual analytics approaches. The created corpora is publicly available in this repository. A first corpus of ~6000 geo-annotated tweets has been published in geocorpora_1506879947339.tsv along with Java code to help parsing the corpora and acquiring the tweet text.

### Related Work
The GeoCorpora is discussed in depth in the following publication:

Wallgrün, J. O., Karimzadeh, M., Pezanowski, S., & MacEachren, A. M. (2017). GeoCorpora: Building a Corpus to Test and Train Microblog Geoparsers. International Journal of Geographical Information Science. https://doi.org/10.1080/13658816.2017.1368523

If you find the GeoCorpora valuable in your research. Please cite this publication.

Another publication on related work using GeoCorpora can be found here:

Karimzadeh, M., Pezanowski, S., Wallgrün, J. O., MacEachren, A. M., & Wallgrün, J. O. (2019). GeoTxt: A scalable geoparsing system for unstructured text geolocation. Transactions in GIS, 23(1), 118–136. https://doi.org/10.1111/tgis.12510

### Tutorial
Begin by adding your Twitter API credentials to the resources file resources/edu/psu/geovista/geocorpora/geocorpora.properties

Next, you may build the project using Maven.

The entry class is edu.psu.geovista.geocorpora.CorpusFromCSV. This class will read the tab delimited GeoCorpora file, parse the GeoCorpora into GeoCorporaTweet objects, and query the Twitter API for the tweet text. Finally, the GeoCorpora will be save to a tab delimited file with the tweet text for your records so that you do not need to do this in the future.

The absolute path to the GeoCorpora tab delimited file can be specified in the pathToCSVFile variable in the main method of CorpusFromCSV or added as an argument when running this method.

### Iterating over tweets
Tweets can be iterated through using code similar to that in the main method of CorpusFromCSV. The next tweets containing locations having any of the location types specified in the nextTweet() method, will be returned.

### Iterating over locations
Next, the individual locations in each of these tweets can be iterated over as seen in the inner loop using the location types specified in the nextLocation() method.
