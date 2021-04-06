package com.google.sps.servlets;


import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

//datastore imports...
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
            String message = getParameter(request, "message", ""); 
            Part filePart = request.getPart("image");
            String fileName = filePart.getSubmittedFileName();
            
            InputStream fileInputStream = filePart.getInputStream();

            String uploadedImageUrl = uploadToCloudStorage(fileName, fileInputStream);
            PrintWriter out = response.getWriter();
            
            Datastore datastore = DatastoreOptions.getDefaultInstance().getService(); //get the instance of the Datastore class
            KeyFactory keyFactory = datastore.newKeyFactory().setKind("Image"); //creates a keyFactory with a kind called "Task" and the name keyFactory
            FullEntity imgEntity =
                Entity.newBuilder(keyFactory.newKey()) //give a key to the Entity
                .set("message", message)//
                .set("Url", uploadedImageUrl)
                .build();
            datastore.put(imgEntity);//store this entity in datastore

            //This method will upload to cloudstorage and then get a blob, preferably to use with the message.
            System.out.println("Upload confirmed.");
            response.sendRedirect("/index.html"); //Redirects back home for now, will change to list of images later.
    }

    //helper function for getParameter
    private String getParameter(HttpServletRequest request, String name, String defaultValue)
    {
    String value = request.getParameter(name);
    if (value == null) 
    {
        return defaultValue;
    }
        return value;
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
