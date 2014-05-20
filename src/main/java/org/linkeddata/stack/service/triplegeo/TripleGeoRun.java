package org.linkeddata.stack.service.triplegeo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.geoknow.athenarc.triplegeo.*;
import eu.geoknow.athenarc.triplegeo.wkt.*;

/**
 * Servlet implementation class TripleGeoRun
 */
public class TripleGeoRun extends HttpServlet {
        
        private static final long serialVersionUID = 1L;
        private static String filePath;
        
        // Options for writing the config file
        static String configFile;
        
        static String[] outputParams = {"", "", "", "", "", ""};
        static String[] dataParams = {"", "", "", "", "", ""};
        static String[] dbParams = {"", "", "", "", "", "", "", "", "", "", "", "", "", ""};
        static String[] NSParams = {"", "", "", ""};
        static String[] RSParams = {"", "", "", ""};
        static String defaultLang;
        static String fileEXT;

        public void init( ){
        	  filePath = getServletContext().getRealPath(File.separator);
        	  filePath = filePath.replace("\\", "/");
              configFile = filePath+"config/config.conf";
              outputParams[0] = configFile;
              outputParams[1] = filePath+"tmp";
              
              File resultDir = new File(filePath + "result");
              if (!resultDir.exists()) {
            	  resultDir.mkdirs();
              }
              
              File configDir = new File(filePath + "config");
              if (!configDir.exists()) {
            	  configDir.mkdirs();
              }
        }

        /**
         * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
         */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<HTML><HEAD><TITLE>Hello World!</TITLE>"
                + "</HEAD><BODY>Hello World!!!</BODY></HTML>");
        }

        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            
            if(request.getParameter("format").equals("RDF/XML") || request.getParameter("format").equals("RDF/XML-ABBREV")){
                    fileEXT = "rdf";
            }
            if(request.getParameter("format").equals("N3")){
                    fileEXT = "n3";
            }
            if(request.getParameter("format").equals("N-TRIPLES")){
                    fileEXT = "nt";
            }
            if(request.getParameter("format").equals("TURTLE") || request.getParameter("format").equals("TTL")){
                    fileEXT = "ttl";
            }
            
            NSParams[0] = request.getParameter("nsPrefix");
            NSParams[1] = request.getParameter("nsURI");
            NSParams[2] = request.getParameter("ontologyNSPrefix");
            NSParams[3] = request.getParameter("ontologyNS");
            
            RSParams[0] = request.getParameter("sourceRS");
            RSParams[1] = request.getParameter("targetRS");
            
            defaultLang = request.getParameter("defaultLang");
            
            System.out.println("Starting job: "+request.getParameter("job"));
            
            if(request.getParameter("job").equals("example")){
            	System.out.println(request.getParameter("inputFile"));
                    outputParams[2] = filePath+"examples/"+request.getParameter("inputFile");
                    outputParams[3] = filePath+"result/result."+fileEXT;
                    outputParams[4] = request.getParameter("format");
                    outputParams[5] = request.getParameter("targetStore");
                    
                    dataParams[0] = request.getParameter("featureString");
                    dataParams[1] = request.getParameter("attribute");
                    dataParams[2] = request.getParameter("ignore");
                    dataParams[3] = request.getParameter("type");
                    dataParams[4] = request.getParameter("name");
                    dataParams[5] = request.getParameter("dclass");
                    writeShpConfig();
                    ExecuteShpExtraction();
                    response.getWriter().write(fileEXT);
            }
            
            if(request.getParameter("job").equals("file")){
                    outputParams[2] = request.getParameter("inputFile");
                    outputParams[3] = filePath+"result/result."+fileEXT;
                    outputParams[4] = request.getParameter("format");
                    outputParams[5] = request.getParameter("targetStore");
                    
                    dataParams[0] = request.getParameter("featureString");
                    dataParams[1] = request.getParameter("attribute");
                    dataParams[2] = request.getParameter("ignore");
                    dataParams[3] = request.getParameter("type");
                    dataParams[4] = request.getParameter("name");
                    dataParams[5] = request.getParameter("dclass");
                    writeShpConfig();
                    ExecuteShpExtraction();
                    response.getWriter().write(fileEXT);
            }
            
            if(request.getParameter("job").equals("db")){
                    System.out.println(request.getParameter("dbType"));
                    outputParams[3] = filePath+"result/result."+fileEXT;
                    outputParams[4] = request.getParameter("format");
                    outputParams[5] = request.getParameter("targetStore");
                    
                    dbParams[0] = request.getParameter("dbType");
                    dbParams[1] = request.getParameter("dbName");
                    dbParams[2] = request.getParameter("dbUserName");
                    dbParams[3] = request.getParameter("dbPassword");
                    dbParams[4] = request.getParameter("dbHost");
                    dbParams[5] = request.getParameter("dbPort");
                    dbParams[6] = request.getParameter("resourceName");
                    dbParams[7] = request.getParameter("tableName");
                    dbParams[8] = request.getParameter("condition");
                    dbParams[9] = request.getParameter("labelColumnName");
                    dbParams[10] = request.getParameter("nameColumnName");
                    dbParams[11] = request.getParameter("classColumnName");
                    dbParams[12] = request.getParameter("geometryColumnName");
                    dbParams[13] = request.getParameter("ignore");
                    
                    writeDBConfig();
                    try {
                                ExecuteDBExtraction();
                                response.getWriter().write(fileEXT);
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
            }

        }
    
    public static void writeShpConfig(){
                            try {
                                        String content = 
                                                                     "tmpDir = "+outputParams[1]+"/n"
                                                                   + "inputFile = "+outputParams[2]+"/n"
                                                                   + "outputFile = "+outputParams[3]+"/n"
                                                                   + "format = "+outputParams[4]+"/n"
                                                                   + "targetStore = "+outputParams[5]+"/n"
                                                                   + "featureString = "+dataParams[0]+"/n"
                                                                   + "attribute = "+dataParams[1]+"/n"
                                                                   + "ignore = "+dataParams[2]+"/n"
                                                                   + "type = "+dataParams[3]+"/n"
                                                                   + "name = "+dataParams[4]+"/n"
                                                                   + "class = "+dataParams[5]+"/n"
                                                                   + "nsPrefix = "+NSParams[0]+"/n"
                                                                   + "nsURI = "+NSParams[1]+"/n"
                                                                   + "ontologyNSPrefix = "+NSParams[2]+"/n"
                                                                   + "ontologyNS = "+NSParams[3]+"/n"
                                                                   + "sourceRS = "+RSParams[0]+"/n"
                                                                   + "targetRS = "+RSParams[1]+"/n"
                                                                   + "defaultLang = "+defaultLang;
                 
                                        File file = new File(configFile);
                                        
                                        if (!file.exists()) {
                                                file.createNewFile();
                                        }
                 
                                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                                        BufferedWriter bw = new BufferedWriter(fw);
                                        
                                        String[] lines = content.split("/n");
                                    for (String line: lines) {
                                            if(!line.toLowerCase().contains("null")){
                                                    bw.write(line);
                                                bw.newLine();
                                            }
                                    }
                                        bw.close();
                 
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
    
    public static void writeDBConfig(){
            try {
                     
                        String content = 
                                                     "tmpDir = "+outputParams[1]+"/n"
                                                   + "outputFile = "+outputParams[3]+"/n"
                                                   + "format = "+outputParams[4]+"/n"
                                                   + "targetStore = "+outputParams[5]+"/n"
                                                   + "dbType = "+dbParams[0]+"/n"
                                                   + "dbName = "+dbParams[1]+"/n"
                                                   + "dbUserName = "+dbParams[2]+"/n"
                                                   + "dbPassword = "+dbParams[3]+"/n"
                                                   + "dbHost = "+dbParams[4]+"/n"
                                                   + "dbPort = "+dbParams[5]+"/n"
                                                   + "resourceName = "+dbParams[6]+"/n"
                                                   + "tableName = "+dbParams[7]+"/n"
                                                   + "condition = "+dbParams[8]+"/n"
                                                   + "labelColumnName = "+dbParams[9]+"/n"
                                                   + "nameColumnName = "+dbParams[10]+"/n"
                                                   + "classColumnName = "+dbParams[11]+"/n"
                                                   + "geometryColumnName = "+dbParams[12]+"/n"
                                                   + "ignore = "+dbParams[13]+"/n"
                                                   + "nsPrefix = "+NSParams[0]+"/n"
                                                   + "nsURI = "+NSParams[1]+"/n"
                                                   + "ontologyNSPrefix = "+NSParams[2]+"/n"
                                                   + "ontologyNS = "+NSParams[3]+"/n"
                                                   + "sourceRS = "+RSParams[0]+"/n"
                                                   + "targetRS = "+RSParams[1]+"/n"
                                                   + "defaultLang = "+defaultLang;
 
                        File file = new File(configFile);
                        
                        if (!file.exists()) {
                                file.createNewFile();
                        }
 
                        FileWriter fw = new FileWriter(file.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);
                        
                        String[] lines = content.split("/n");
                    for (String line: lines) {
                            if(!line.toLowerCase().contains("null")){
                                    bw.write(line);
                                bw.newLine();
                            }
                    }
                        bw.close();
 
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
    
    // Start LIMES with the configfile
         public static void ExecuteShpExtraction() throws IOException{
        	 for(int i=0; i<outputParams.length; i++){
        		 System.out.println(outputParams[i]);
        	 }
                 ShpToRdf.main(outputParams);
         }
         
         public static void ExecuteDBExtraction() throws Exception{
                 RdbToRdf.main(outputParams);
         }

}