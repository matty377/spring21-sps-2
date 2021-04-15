package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.Gson;
import com.google.sps.data.Images;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class ImageLinkServlet extends HttpServlet
{
    // public void doPost(HttpServletRequest request, HttpServletResponse response) 
    //     throws ServletException, IOException {
    //         //UUID uuid = UUID.randomUUID(); //This is a randomly generated ID for the image

    //         String message = getParameter(request, "message", ""); 
    //         Part filePart = request.getPart("image");
    //         //String fileName = uuid.toString(); //This could be changed later if we have a system for it
    //         String fileName = filePart.getSubmittedFileName();
            
    //         InputStream fileInputStream = filePart.getInputStream();

    //         String uploadedImageUrl = uploadToCloudStorage(fileName, fileInputStream);
    //         PrintWriter out = response.getWriter();
            
    //         Datastore datastore = DatastoreOptions.getDefaultInstance().getService(); //get the instance of the Datastore class
    //         KeyFactory keyFactory = datastore.newKeyFactory().setKind("Image"); //creates a keyFactory with a kind called "Task" and the name keyFactory
    //         FullEntity imgEntity =
    //             Entity.newBuilder(keyFactory.newKey()) //give a key to the Entity
    //             .set("message", message)//
    //             .set("Url", uploadedImageUrl)
    //             .build();
    //         datastore.put(imgEntity);//store this entity in datastore

    //         //This method will upload to cloudstorage and then get a blob, preferably to use with the message.
    //         System.out.println("Upload confirmed.");
    //         response.sendRedirect("/index.html"); //Redirects back home for now, will change to list of images later.
    //}
    
    public void getUrl(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        String projectId = "spring21-sps-2";
        String bucketName = "spring21-sps-2";
        

        /* Store images in datastore */
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Url").setFilter(PropertyFilter.eq("Url", EQUAL))
        .build();
        
        QueryResults<Entity> results = datastore.run(query); //creates a query as well as results
        List<Images> imagesUrl = new ArrayList<>();//..a list to store the images url

        //traverse the query results
        while (results.hasNext())
        {
            Entity entity = results.next();
            String message = entity.getString("message");
            String link = entity.getString("Url");
            Images img = new Images(message, link); 
            imagesUrl.add(img);
        }

        //converting to json here...
        String json = convertToJson(imagesUrl);
        response.setContentType("application/json;"); 
        response.getWriter().println(json);


    }
    
    private String convertToJson(List<Images> stuff)
    {
        Gson gson = new Gson();
        String json = gson.toJson(stuff);
        return json;
    }

}

