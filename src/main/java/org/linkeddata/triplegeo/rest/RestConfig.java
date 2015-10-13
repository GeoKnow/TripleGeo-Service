package org.linkeddata.triplegeo.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main configuration for Jersey REST
 * 
 * @author alejandragarciarojas
 *
 */

@ApplicationPath("/*")
public class RestConfig extends ResourceConfig {

  public RestConfig() {
    packages("org.linkeddata.triplegeo.rest");
  }

}
