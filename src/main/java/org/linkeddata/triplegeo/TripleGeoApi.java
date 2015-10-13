package org.linkeddata.triplegeo;

import java.io.File;
import java.io.IOException;

import eu.geoknow.athenarc.triplegeo.ShpToRdf;
import eu.geoknow.athenarc.triplegeo.utils.UtilsLib;
import eu.geoknow.athenarc.triplegeo.wkt.RdbToRdf;

public class TripleGeoApi {

  private File xsltDir;

  public TripleGeoApi() throws Exception {
    ClassLoader classLoader = getClass().getClassLoader();
    xsltDir = new File(classLoader.getResource("xslt").getPath());
    if (!xsltDir.exists())
      throw new Exception("XSLT files not found" + xsltDir.getPath());
  }

  public void DbToRdf(Configuration config, String configFile) throws Exception {
    config.writeConfigDb(configFile);
    String[] args = {configFile};
    RdbToRdf.main(args);
  }

  public void EsriToRdf(Configuration config, String configFile) throws IOException {
    config.writeConfigShape(configFile);
    String[] args = {configFile};
    ShpToRdf.main(args);

  }

  public void KmlToRdf(String input, String output) {

    String fileXSLT = xsltDir.getPath() + File.separator + "KML2RDF.xsl"; // Predefined XSLT
                                                                          // stylesheet to be
                                                                          // applied in
    // transformation
    // Set saxon as transformer.
    System.setProperty("javax.xml.transform.TransformerFactory",
        "net.sf.saxon.TransformerFactoryImpl");

    // simpleTransform("./data/samples.kml", "./xslt/KML2RDF.xsl", "./data/test1.rdf");
    UtilsLib.saxonTransform(input, fileXSLT, output);
  }

  public void GmlToRdf(String input, String output) {

    String fileXSLT = xsltDir.getPath() + File.separator + "GML2RDF.xsl"; // Predefined XSLT
                                                                          // stylesheet to be
                                                                          // applied in
                                                                          // transformation

    // Set saxon as transformer.
    System.setProperty("javax.xml.transform.TransformerFactory",
        "net.sf.saxon.TransformerFactoryImpl");

    // simpleTransform("./data/blocks.gml", "./xslt/GML2RDF.xsl", "./data/test1.rdf");
    UtilsLib.saxonTransform(input, fileXSLT, output);
  }
}
