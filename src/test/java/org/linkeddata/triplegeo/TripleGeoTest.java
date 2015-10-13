package org.linkeddata.triplegeo;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class TripleGeoTest {

  private static final Logger log = Logger.getLogger(TripleGeoTest.class);
  private File exDir;
  private File xsltDir;
  private File tmpDir;
  private File resultDir;


  @Before
  public void getDirs() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();

    exDir = new File(classLoader.getResource("examples").getPath());
    log.debug(exDir.exists());
    if (!(exDir.exists() && exDir.isDirectory())) {
      log.error("Examples dir is not present");
      return;
    }

    xsltDir = new File(classLoader.getResource("xslt").getPath());
    log.debug(xsltDir.exists());
    if (!(xsltDir.exists() && xsltDir.isDirectory())) {
      log.error("xslt dir is not present");
      return;
    }

    log.info(exDir.getParent());
    tmpDir = new File(exDir.getParent() + "/tmp");
    if (!tmpDir.exists())
      tmpDir.mkdirs();
    log.info("Tmp dir: " + tmpDir.getPath());

    resultDir = new File(exDir.getParent() + "/result");
    if (!resultDir.exists())
      resultDir.mkdirs();
    log.info("resultDir: " + resultDir.getPath());

  }

  // @After
  // public void deleteDirs() {
  // // resultDir.delete();
  // tmpDir.delete();
  // }


  @Test
  public void testKml() throws Exception {

    String fileXML = exDir.getPath() + File.separator + "sample.kml";
    String fileRDF = resultDir.getPath() + File.separator + "sample.rdf";

    TripleGeoApi tg = new TripleGeoApi();
    tg.KmlToRdf(fileXML, fileRDF);

    log.info(fileRDF);
    File res = new File(fileRDF);

    assertTrue(res.exists());

  }

  @Test
  public void testGml() throws Exception {

    String fileXML = exDir.getPath() + File.separator + "airports.gml";
    String fileRDF = resultDir.getPath() + File.separator + "airports.rdf";

    TripleGeoApi tg = new TripleGeoApi();
    tg.GmlToRdf(fileXML, fileRDF);

    log.info(fileRDF);
    File res = new File(fileRDF);

    assertTrue(res.exists());

  }

  @Test
  public void testShapeToRdf() throws Exception {

    String configFile = resultDir.getPath() + "/points.conf";
    String testFile = exDir.getPath() + "/points.shp";
    String resultFile = resultDir.getPath() + "/points.rdf";

    log.info(testFile);

    // {
    // "job": "example",
    // "format": "RDF/XML",
    // "targetStore": "GeoSPARQL",
    // "inputFile": "points.shp",
    // "featureString": "points",
    // "attribute": "osm_id",
    // "ignore": "UNK",
    // "type": "points",
    // "name": "name",
    // "dclass": "type",
    // "nsPrefix": "georesource",
    // "nsURI": "http://geoknow.eu/geodata#",
    // "ontologyNSPrefix": "geo",
    // "ontologyNS": "http://www.opengis.net/ont/geosparql#"
    // }

    Configuration conf = new Configuration();

    conf.setTmpDir(tmpDir.getPath());
    conf.setInputFile(testFile);
    conf.setOutputFile(resultFile);
    conf.setFormat("RDF/XML");
    conf.setTargetStore("GeoSPARQL");

    conf.setResourceName("");
    conf.setTableName("");
    conf.setCondition("");
    conf.setLabelColumnName("");
    conf.setNameColumnName("");
    conf.setClassColumnName("");
    conf.setGeometryColumnName("");
    conf.setIgnore("UNK");
    conf.setNsPrefix("georesource");
    conf.setNsURI("http://geoknow.eu/geodata#");
    conf.setOntologyNSPrefix("geo");
    conf.setOntologyNS("http://www.opengis.net/ont/geosparql#");
    conf.setSourceRS("");
    conf.setTargetRS("");
    conf.setDefaultLang("en");

    conf.setFeatureString("points");
    conf.setAttribute("osm_id");
    conf.setType("points");
    conf.setName("name");
    conf.setUclass("type");

    TripleGeoApi tg = new TripleGeoApi();
    tg.EsriToRdf(conf, configFile);

    File res = new File(resultFile);

    assertTrue(res.exists());
  }

  @Test
  public void testDbToRdf() throws Exception {

    Configuration conf = new Configuration();
    conf.setDbType("");
    conf.setDbName("");
    conf.setDbUserName("");
    conf.setDbPassword("");
    conf.setDbHost("");
    conf.setDbPort("");

  }
}
