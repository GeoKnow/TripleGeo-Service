package org.linkeddata.triplegeo.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
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

    // String candidateName = null;
    File dir = getWorkingDir(context);
    List<String> uploaded = new ArrayList<String>();

    log.debug(request.getContentType());

    // checks whether there is a file upload request or not
    if (ServletFileUpload.isMultipartContent(request)) {
      final FileItemFactory factory = new DiskFileItemFactory();
      final ServletFileUpload fileUpload = new ServletFileUpload(factory);
      try {
        /*
         * parseRequest returns a list of FileItem but in old (pre-java5) style
         */
        // final List items = fileUpload.parseRequest(request);

        FileItemIterator iter = fileUpload.getItemIterator(request);

        // if (items != null) {
        // final Iterator iter = items.iterator();
        while (iter.hasNext()) {
          final FileItemStream item = iter.next();
          final String itemName = item.getName();
          final String fieldName = item.getFieldName();
          // final String fieldValue = item.getString();

          InputStream stream = item.openStream();

          if (item.isFormField()) {
            // candidateName = stream.asString(stream);
            System.out.println("Field Name: " + fieldName + ", andidate Name: "
                + Streams.asString(stream));
          } else {
            System.out.println("File field " + fieldName + " with file name " + item.getName()
                + " detected.");

            final File targetFile =
                new File(dir.getPath() + File.separator + itemName.toLowerCase());
            // System.out.println("Saving the file: " + savedFile.getName());
            // item.write(savedFile);

            OutputStream outStream = new FileOutputStream(targetFile);

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
              outStream.write(buffer, 0, bytesRead);
            }
            IOUtils.closeQuietly(stream);
            IOUtils.closeQuietly(outStream);

            uploaded.add(dir.getPath() + File.separator + itemName.toLowerCase());
          }

        }
        // }
      } catch (FileUploadException e) {
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

    Gson gson = new Gson();
    String json = gson.toJson(uploaded);

    return Response.ok().entity(json).build();
  }
}
