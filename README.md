#TripleGeo-Service

A web service for running TripleGeo

##Building

Tested in Tomcat7

To deploy the service to your Tomcat server run:

	mvn tomcat:deploy
	
or to redeploy:

	mvn tomcat:redeploy
	
##Directories
	
The TripleGeo-Service webapp folder should have the following subdirectories:

	/config
	/result
	/examples

##HTTP Calls

###Running TripleGeo

####Load the TripleGeo configuration and shp file

POST a params array to /Loadfile

	params: {
									file : filename, (the config file)
									shp: inputFileName (the name of the shp files)
									}

This class will read the files from the GeoKnow Generator (https://github.com/GeoKnow/GeoKnowGeneratorUI) upload 
folder, which should be in the same Tomcat server:

	/webapps/generator/uploads
	
If a different behaviour is desired this class must be edited.

The class will respond with the parameters read from the config file.
				 	
####Start the extraction process

POST a params array to /TripleGeoRun

	params = {
					 job: (file or database job)
					
					 format: 
					 targetStore: 
					 inputFile : 
					 
					 featureString: 
					 attribute: 
					 ignore: 
					 type: 
					 name: 
					 dclass: 
					 
					 nsPrefix: 
					 nsURI: 
					 ontologyNSPrefix: 
					 ontologyNS: 
					 
					 sourceRS: 
					 targetRS: 
					 
					 defaultLang: 
				   };
	
The class will output the results to the webapp/result folder
	
####Open the output from the extraction process

POST a params array to /TripleGeoReview

  params = { filetype : filetype };

The class will automatically open the files created by TripleGeoRun and return the models.

	
####Save the output to a SPARQL endpoint
	
POST a params array to /ImportRDF

	var parameters = {
		        rdfFile: "result."+fileType,
		        fileType: fileType,
		        endpoint: 
		        graph:  
		        uriBase:
		      	};
		
Will save the results to the endpoint in the specified graphs using the URI base.

##Licence

The source code of this repo is published under the Apache License Version 2.0

