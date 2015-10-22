package org.linkeddata.triplegeo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Configuration {

  private static final Logger log = Logger.getLogger(Configuration.class);

  private String job;

  private String targetEndpoint = "";
  private String targetGraph = "";

  private String tmpDir = "";
  private String outputFile = "";
  private String format = "";
  private String targetStore = "";
  private String dbType = "";
  private String dbName = "";
  private String dbUserName = "";
  private String dbPassword = "";
  private String dbHost = "";
  private String dbPort = "";
  private String resourceName = "";
  private String tableName = "";
  private String condition = "";
  private String labelColumnName = "";
  private String nameColumnName = "";
  private String classColumnName = "";
  private String geometryColumnName = "";
  private String ignore = "";
  private String nsPrefix = "";
  private String nsURI = "";
  private String ontologyNSPrefix = "";
  private String ontologyNS = "";
  private String sourceRS = "";
  private String targetRS = "";
  private String defaultLang = "en";

  private String inputFile;
  private String featureString;
  private String attribute;
  private String type;
  private String name;

  @Override
  public String toString() {
    Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
    return gson.toJson(this);
  }

  public String getInputFile() {
    return inputFile;
  }

  public void setInputFile(String inputFile) {
    this.inputFile = inputFile;
  }

  public String getFeatureString() {
    return featureString;
  }

  public void setFeatureString(String featureString) {
    this.featureString = featureString;
  }

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUclass() {
    return uclass;
  }

  public void setUclass(String uclass) {
    this.uclass = uclass;
  }

  private String uclass;

  public String getTmpDir() {
    return tmpDir;
  }

  public void setTmpDir(String tmpDir) {
    this.tmpDir = tmpDir;
  }

  public String getOutputFile() {
    return outputFile;
  }

  public void setOutputFile(String outputFile) {
    this.outputFile = outputFile;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getTargetStore() {
    return targetStore;
  }

  public void setTargetStore(String targetStore) {
    this.targetStore = targetStore;
  }

  public String getDbType() {
    return dbType;
  }

  public void setDbType(String dbType) {
    this.dbType = dbType;
  }

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public String getDbUserName() {
    return dbUserName;
  }

  public void setDbUserName(String dbUserName) {
    this.dbUserName = dbUserName;
  }

  public String getDbPassword() {
    return dbPassword;
  }

  public void setDbPassword(String dbPassword) {
    this.dbPassword = dbPassword;
  }

  public String getDbHost() {
    return dbHost;
  }

  public void setDbHost(String dbHost) {
    this.dbHost = dbHost;
  }

  public String getDbPort() {
    return dbPort;
  }

  public void setDbPort(String dbPort) {
    this.dbPort = dbPort;
  }

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getCondition() {
    return condition;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public String getLabelColumnName() {
    return labelColumnName;
  }

  public void setLabelColumnName(String labelColumnName) {
    this.labelColumnName = labelColumnName;
  }

  public String getNameColumnName() {
    return nameColumnName;
  }

  public void setNameColumnName(String nameColumnName) {
    this.nameColumnName = nameColumnName;
  }

  public String getClassColumnName() {
    return classColumnName;
  }

  public void setClassColumnName(String classColumnName) {
    this.classColumnName = classColumnName;
  }

  public String getGeometryColumnName() {
    return geometryColumnName;
  }

  public void setGeometryColumnName(String geometryColumnName) {
    this.geometryColumnName = geometryColumnName;
  }

  public String getIgnore() {
    return ignore;
  }

  public void setIgnore(String ignore) {
    this.ignore = ignore;
  }

  public String getNsPrefix() {
    return nsPrefix;
  }

  public void setNsPrefix(String nsPrefix) {
    this.nsPrefix = nsPrefix;
  }

  public String getNsURI() {
    return nsURI;
  }

  public void setNsURI(String nsURI) {
    this.nsURI = nsURI;
  }

  public String getOntologyNSPrefix() {
    return ontologyNSPrefix;
  }

  public void setOntologyNSPrefix(String ontologyNSPrefix) {
    this.ontologyNSPrefix = ontologyNSPrefix;
  }

  public String getOntologyNS() {
    return ontologyNS;
  }

  public void setOntologyNS(String ontologyNS) {
    this.ontologyNS = ontologyNS;
  }

  public String getSourceRS() {
    return sourceRS;
  }

  public void setSourceRS(String sourceRS) {
    this.sourceRS = sourceRS;
  }

  public String getTargetRS() {
    return targetRS;
  }

  public void setTargetRS(String targetRS) {
    this.targetRS = targetRS;
  }

  public String getDefaultLang() {
    return defaultLang;
  }

  public void setDefaultLang(String defaultLang) {
    this.defaultLang = defaultLang;
  }

  public String getJob() {
    return job;
  }

  public void setJob(String job) {
    this.job = job;
  }

  public void writeConfigDb(String configFile) throws IOException {
    String content =
        "tmpDir = " + tmpDir + "\n" + "outputFile = " + outputFile + "\n" + "format = " + format
            + "\n" + "targetStore = " + targetStore + "\n" + "dbType = " + dbType + "\n"
            + "dbName = " + dbName + "\n" + "dbUserName = " + dbUserName + "\n" + "dbPassword = "
            + dbPassword + "\n" + "dbHost = " + dbHost + "\n" + "dbPort = " + dbPort + "\n"
            + "resourceName = " + resourceName + "\n" + "tableName = " + tableName + "\n"
            + "condition = " + condition + "\n" + "labelColumnName = " + labelColumnName + "\n"
            + "nameColumnName = " + nameColumnName + "\n" + "classColumnName = " + classColumnName
            + "\n" + "geometryColumnName = " + geometryColumnName + "\n" + "ignore  = " + ignore
            + "\n" + "nsPrefix = " + nsPrefix + "\n" + "nsURI = " + nsURI + "\n"
            + "ontologyNSPrefix = " + ontologyNSPrefix + "\n" + "ontologyNS = " + ontologyNS + "\n"
            + "sourceRS = " + sourceRS + "\n" + "targetRS = " + targetRS + "\n" + "defaultLang = "
            + defaultLang + "\n";

    wrtieFile(configFile, content);
  }

  public void writeConfigShape(String configFile) throws IOException {
    String content =
        "tmpDir = " + tmpDir + "\n" + "inputFile = " + inputFile + "\n" + "outputFile = "
            + outputFile + "\n" + "format = " + format + "\n" + "targetStore = " + targetStore
            + "\n" + "featureString = " + featureString + "\n" + "attribute = " + attribute + "\n"
            + "ignore = " + ignore + "\n" + "type = " + type + "\n" + "name = " + name + "\n"
            + "class = " + uclass + "\n" + "nsPrefix = " + nsPrefix + "\n" + "nsURI = " + nsURI
            + "\n" + "ontologyNSPrefix = " + ontologyNSPrefix + "\n" + "ontologyNS = " + ontologyNS
            + "\n" + "sourceRS = " + sourceRS + "\n" + "targetRS = " + targetRS + "\n"
            + "defaultLang = " + defaultLang;

    wrtieFile(configFile, content);
  }

  private void wrtieFile(String configFile, String content) throws IOException {

    log.info(content);
    File file = new File(configFile);
    if (!file.exists()) {
      file.createNewFile();
    }

    FileWriter fw = new FileWriter(file.getAbsoluteFile());
    BufferedWriter bw = new BufferedWriter(fw);

    String[] lines = content.split("\n");
    for (String line : lines) {
      if (!line.toLowerCase().contains("null")) {
        bw.write(line);
        bw.newLine();
      }
    }
    bw.close();

  }

  public String getTargetEndpoint() {
    return targetEndpoint;
  }

  public void setTargetEndpoint(String targetEndpoint) {
    this.targetEndpoint = targetEndpoint;
  }

  public String getTargetGraph() {
    return targetGraph;
  }

  public void setTargetGraph(String targetGraph) {
    this.targetGraph = targetGraph;
  }
}
