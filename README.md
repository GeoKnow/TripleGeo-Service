#TripleGeo-Service

A web service for running TripleGeo

##Building

Tested in Tomcat7

To deploy the service to your Tomcat server run:

	mvn tomcat:deploy
	
or to redeploy:

	mvn tomcat:redeploy
	
## REST API

The rest services available are:

|Path   |Method | accepts | produces | Body       |Description|
|-----  |-------|---------|------------|----------|----------|
|/      | GET   |         |application/json |     | Get all performed transformations|
|/{uuid}| GET   |         |application/json |     | Get transformations description|
|/      | POST  | application/json   | application/json | config*  | New transformation|
|/upload| POST  | multipart/form-data| application/json |          | Upload a file to be used in a transformation|

The configuration for a new transformation needs the following JSON:

        {
            "job": "[esri|gml|kml] ",
            "format": "[RDF/XML|N3|N-TRIPLES]",
            "targetStore": "[GeoSPARQL|Virtuoso]",
            "inputFile": "path/to/shape/file.shp",
            "featureString": "points",
            "attribute": "osm_id",
            "ignore": "UNK",
            "type": "points",
            "name": "name",
            "uclass": "type",
            "nsPrefix": "georesource",
            "nsURI": "http://geoknow.eu/geodata#",
            "ontologyNSPrefix": "geo",
            "ontologyNS": "http://www.opengis.net/ont/geosparql#",
            "sourceRS": "",
            "targetRS": "",
            "defaultLang": "en",
            "targetEndpoint": "http://localhost:8080/sparql",
            "targetGraph": "http://example/results"
        }
        
For a better description for the parameters check [TripleGeo](https://github.com/GeoKnow/TripleGeo). 