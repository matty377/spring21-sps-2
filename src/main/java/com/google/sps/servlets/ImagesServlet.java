package com.google.sps.servlets;

//getting the images.java class...
import com.google.sps.data.Images;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.api.client.util.Data;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.Gson;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;


@WebServlet("/images")
public class ImagesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        String projectId = "spring21-sps-2";
        String bucketName = "spring21-sps-2";
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        Bucket bucket = storage.get(bucketName);
        Page<Blob> blobs = bucket.list();
        

        for (Blob blob : blobs.iterateAll()){
            String imgTag = String.format("<img src=\"%s\" />", blob.getMediaLink());
            response.getWriter().println(imgTag);
            response.getWriter().println("<br>");
        }

        /* Store images in datastore */
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();


        Query<Entity> query =
            Query.newEntityQueryBuilder().setKind("Images").setOrderBy(OrderBy.desc("message")).build();
        QueryResults<Entity> results = datastore.run(query); //creates a query as well as results
        List<Images> images = new ArrayList<>();//..a list to store the images

        //traverse the query results
        while (results.hasNext())
        {
            Entity entity = results.next();
            String message = entity.getString("message");
            String link = entity.getString("Url");
            Images img = new Images(message, link); 
            images.add(img);
        }

        //converting to json here...
        String json = convertToJson(images);
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
