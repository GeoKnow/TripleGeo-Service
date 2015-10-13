package org.linkeddata.triplegeo.rest;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.linkeddata.triplegeo.Configuration;
import org.linkeddata.triplegeo.TripleGeoApi;
import org.linkeddata.utils.QueryChunks;

import com.google.gson.Gson;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;
import com.hp.hpl.jena.rdf.model.ResourceRequiredException;


/**
 * 
 * @author alejandragarciarojas
 *
 */
@Path("")
public class TripleGeoService {
  private static final Logger log = Logger.getLogger(TripleGeoService.class);

  @Context
  ServletContext context;

  /**
   * Returns the working directory where config and output files are saved
   * 
   * @param context
   * @return path
   */
  private File getWorkingDir(@Context ServletContext context) {

    ClassLoader classLoader = getClass().getClassLoader();
    File exDir = new File(classLoader.getResource("examples").getPath());

    File resultDir = new File(exDir.getParent() + File.separator + "results");
    if (!resultDir.exists()) {
      log.warn("working directory " + resultDir.getAbsolutePath()
          + " doesnt exist, attempt to create it... ");
      resultDir.mkdirs();
    }

    return resultDir;
  }

  /**
   * Provides a list of all configuration files processed by the service.
   * 
   * @param context
   * @return a JSON array of UUIDs
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllConfigurations(@Context ServletContext context) {

    File workingDir = getWorkingDir(context);

    String[] ext = {"json"};
    Collection<File> fileIterator = FileUtils.listFiles(workingDir, ext, false);
    List<String> results = new ArrayList<String>();
    for (File f : fileIterator)
      results.add(f.getName().replace(".json", ""));

    Gson gson = new Gson();
    String json = gson.toJson(results);

    return Response.ok().entity(json).header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Methods", "GET").build();
  }

  /**
   * Provides the content of a specific configuration file reading the uuid.json file produced with
   * the results
   * 
   * @param uuid
   * @param context
   * @return JSON object with the configuration parameters
   */
  @GET
  @Path("{uuid}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getConfiguration(@PathParam("uuid") String uuid, @Context ServletContext context) {

    String workingPath = getWorkingDir(context).getAbsolutePath();

    String configFile = workingPath + File.separator + uuid + ".json";
    log.debug(configFile);
    try {

      String config = FileUtils.readFileToString(new File(configFile), "UTF-8");
      return Response.ok().header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "GET").type(MediaType.TEXT_PLAIN).entity(config)
          .build();

    } catch (IOException e) {
      e.printStackTrace();
      return Response.status(Response.Status.NOT_FOUND).header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "GET").build();
    }

  }

  /**
   * Submit a configuration script in and execute Deer.
   * 
   * @param configuration string in text/turtle format
   * @return Response wit a JSON object with the output results
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response run(Configuration config) {


    String fileEXT = "";
    if (config.getFormat().equals("RDF/XML") || config.getFormat().equals("RDF/XML-ABBREV")) {
      fileEXT = "rdf";
    } else if (config.getFormat().equals("N3")) {
      fileEXT = "n3";
    } else if (config.getFormat().equals("N-TRIPLES")) {
      fileEXT = "nt";
    } else if (config.getFormat().equals("TURTLE") || config.getFormat().equals("TTL")) {
      fileEXT = "ttl";
    } else {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("format not supported: " + config.getFormat())
          .header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "POST").build();
    }

    String uuid = UUID.randomUUID().toString();

    File wDir = getWorkingDir(context);
    String resultDir = wDir.getAbsolutePath();

    String configFile = resultDir + File.separator + uuid + ".conf";
    // create a temporary dir for the output
    File tmpDir = new File(resultDir + File.separator + "tmp-" + uuid);
    tmpDir.mkdirs();

    config.setTmpDir(tmpDir.getPath());
    config.setOutputFile(resultDir + File.separator + uuid + "." + fileEXT);

    try {

      TripleGeoApi tg = new TripleGeoApi();

      if (config.getJob().equals("esri-example")) {
        String f =
            wDir.getParent() + File.separator + "examples" + File.separator + config.getInputFile();
        log.info(f);
        config.setInputFile(f);
        tg.EsriToRdf(config, configFile);

      } else if (config.getJob().equals("esri")) {
        tg.EsriToRdf(config, configFile);
      }

      else if (config.getJob().equals("gml-example")) {
        String f =
            wDir.getParent() + File.separator + "examples" + File.separator + config.getInputFile();
        log.info(f);
        tg.GmlToRdf(f, config.getOutputFile());

      } else if (config.getJob().equals("gml")) {
        tg.GmlToRdf(config.getInputFile(), config.getOutputFile());
      }

      else if (config.getJob().equals("kml-example")) {
        String f =
            wDir.getParent() + File.separator + "examples" + File.separator + config.getInputFile();
        log.info(f);
        tg.KmlToRdf(f, config.getOutputFile());

      } else if (config.getJob().equals("kml")) {
        tg.KmlToRdf(config.getInputFile(), config.getOutputFile());
      }

      else if (config.getJob().equals("db")) {
        tg.DbToRdf(config, configFile);

      }
      // delete temp dir
      tmpDir.delete();

      FileWriter jsonFile = new FileWriter(configFile.replace(".conf", ".json"), false);
      jsonFile.append(config.toString());
      jsonFile.close();

      if (!config.getTargetEndpoint().equals("")) {
        try {
          saveResults(config);
        } catch (Exception e) {
          log.error(e);
          e.printStackTrace();
          return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
              .header("Access-Control-Allow-Origin", "*")
              .header("Access-Control-Allow-Methods", "POST").build();
        }
      }

      // output resulting file/endpoint/graph
      Gson gson = new Gson();
      String json = gson.toJson(config);

      return Response.ok().header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "POST").type(MediaType.APPLICATION_JSON)
          .entity(json).build();

    } catch (ResourceRequiredException e) {
      log.error(e);
      return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage())
          .header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "POST").build();
    } catch (IOException e) {
      log.error(e);
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
          .header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "POST").build();
    } catch (Exception e) {
      log.error(e);
      e.printStackTrace();
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
          .header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "POST").build();
    }

  }

  private static void saveResults(Configuration config) throws Exception {

    log.info("Saving results to " + config.getTargetEndpoint());

    if (config.getTargetEndpoint() == null || config.getTargetEndpoint() == "")
      throw new NullPointerException("Undefined endpoint for saving results");

    String output = config.getOutputFile();

    // read the file created with the first output format
    Model model = ModelFactory.createDefaultModel();
    RDFReader reader = model.getReader(config.getFormat());
    reader.read(model, new File(output).toURI().toURL().toString());

    if (!model.isEmpty())
      insertResults(config.getTargetEndpoint(), config.getTargetGraph(), config.getNsURI(), model);

  }

  private static void insertResults(String endpoint, String graph, String uriBase, Model model)
      throws Exception {
    List<String> insertqueries = QueryChunks.generateInsertChunks(graph, model, uriBase);
    Iterator<String> it = insertqueries.iterator();
    log.info(" into " + graph);

    CloseableHttpClient httpClient = HttpClients.createDefault();

    while (it.hasNext()) {
      String q = it.next();

      HttpPost proxyMethod = new HttpPost(endpoint);

      ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
      postParameters.add(new BasicNameValuePair("query", q));
      postParameters.add(new BasicNameValuePair("format", "application/sparql-results+json"));
      proxyMethod.setEntity(new UrlEncodedFormEntity(postParameters));

      final CloseableHttpResponse response = httpClient.execute(proxyMethod);

      BufferedReader in =
          new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        log.debug(inputLine);
      }
      in.close();

      if (response.getStatusLine().getStatusCode() != 200) {
        throw new IOException("Could not insert data: " + endpoint + " "
            + response.getStatusLine().getReasonPhrase());
      }

    }
    httpClient.close();
  }

}
