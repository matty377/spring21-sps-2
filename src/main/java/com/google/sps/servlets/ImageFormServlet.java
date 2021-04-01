package com.google.sps.servlets;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/upload")
@MultipartConfig()
public class ImageFormServlet extends HttpServlet {
    
    @Override
    /**
     * @param request Received request
     * @param response Outgoing response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
            //UUID uuid = UUID.randomUUID(); //This is a randomly generated ID for the image

            String message = request.getParameter("message");
            Part filePart = request.getPart("image");
            //String fileName = uuid.toString(); //This could be changed later if we have a system for it
            String fileName = filePart.getSubmittedFileName();
            
            
            InputStream fileInputStream = filePart.getInputStream();

            

            String uploadedImageUrl = uploadToCloudStorage(fileName, fileInputStream);
            PrintWriter out = response.getWriter();
            
            
            //This method will upload to cloudstorage and then get a blob, preferably to use with the message.
            System.out.println("Upload confirmed.");
            response.sendRedirect("/index.html"); //Redirects back home for now, will change to list of images later.
    }

    private static String uploadToCloudStorage(String fileName, InputStream fileInputStream){

        String projectId = "spring21-sps-2";
        String bucketName = "spring21-sps-2";
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();


        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        Blob blob = storage.create(blobInfo, fileInputStream);

        return blob.getMediaLink();
    }
}
