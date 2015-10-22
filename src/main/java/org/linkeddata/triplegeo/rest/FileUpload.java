package org.linkeddata.triplegeo.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

@Path("/upload")
public class FileUpload {

  private static final Logger log = Logger.getLogger(FileUpload.class);

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

  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.APPLICATION_JSON)
  public Response uploadFiles(@Context HttpServletRequest request, @Context ServletContext context) {

    String candidateName = null;
    File dir = getWorkingDir(context);
    List<String> uploaded = new ArrayList<String>();

    // checks whether there is a file upload request or not
    if (ServletFileUpload.isMultipartContent(request)) {
      final FileItemFactory factory = new DiskFileItemFactory();
      final ServletFileUpload fileUpload = new ServletFileUpload(factory);
      try {
        /*
         * parseRequest returns a list of FileItem but in old (pre-java5) style
         */
        final List items = fileUpload.parseRequest(request);

        if (items != null) {
          final Iterator iter = items.iterator();
          while (iter.hasNext()) {
            final FileItem item = (FileItem) iter.next();
            final String itemName = item.getName();
            final String fieldName = item.getFieldName();
            final String fieldValue = item.getString();

            if (item.isFormField()) {
              candidateName = fieldValue;
              System.out.println("Field Name: " + fieldName + ", Field Value: " + fieldValue);
              System.out.println("Candidate Name: " + candidateName);
            } else {
              final File savedFile =
                  new File(dir.getPath() + File.separator + itemName.toLowerCase());
              System.out.println("Saving the file: " + savedFile.getName());
              item.write(savedFile);
              uploaded.add(dir.getPath() + File.separator + itemName.toLowerCase());
            }

          }
        }
      } catch (FileUploadException e) {
        log.error(e);
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
            .build();
      } catch (Exception e) {
        log.error(e);
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage())
            .build();
      }
    }

    Gson gson = new Gson();
    String json = gson.toJson(uploaded);

    return Response.ok().entity(json).build();
  }
}
