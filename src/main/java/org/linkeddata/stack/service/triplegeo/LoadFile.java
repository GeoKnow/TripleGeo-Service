package org.linkeddata.stack.service.triplegeo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class LoadFile
 */
public class LoadFile extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// Options for writing the config file
	static String configFile;
	static String[] shpFile = new String[1];
	static String configTemplate;
	static String outputFormat;
	static String execType;
	ArrayList<String> configList = new ArrayList<String>();
	ArrayList<String[]> config = new ArrayList<String[]>();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	 String filePath = request.getSession().getServletContext().getRealPath("/");
    	 filePath = filePath.replace("TripleGeoServlet\\", "");
    	 configFile = filePath+"generator\\uploads\\"+request.getParameter("file");
    	 shpFile[0] = filePath+"generator\\uploads\\"+request.getParameter("shp");
    	 shpFile[0] = shpFile[0].replace("\\", "/");
    	 
    	 configList.clear();
    	 config.clear();
    	 config.add(shpFile);
    	 readConfig(configFile);
    	 
    	 Gson gson = new Gson();
	     String json = gson.toJson(config);
	     response.setContentType("application/json");
	     response.setCharacterEncoding("UTF-8");
	     response.getWriter().write(json);
    	}
    
    private void readConfig(String configFile){
    	
    	BufferedReader br = null;
    	 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(configFile));
 
			while ((sCurrentLine = br.readLine()) != null) {
				if ( sCurrentLine.trim().length() != 0 ) {  
						configList.add(sCurrentLine);
				}
			}
			
			for(int i=0; i<configList.size(); i++){
				if(!configList.get(i).toString().substring(0, 1).equals("#")){
					String[]parts = configList.get(i).split("=");
					parts[0] = parts[0].trim();
					parts[1] = parts[1].trim();
					config.add(parts);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
    	 
		}
    }
    
}
