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


@WebServlet("/comments")
public class ImageLinkServlet extends HttpServlet
{
 
    
    public void getUrl(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        String projectId = "spring21-sps-2";
        String bucketName = "spring21-sps-2";

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        String linkAddress = "https://storage.googleapis.com/download/storage/v1/b/spring21-sps-2/o/1562598348321.jpg?generation=1618447547073684&alt=media";
        Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Image").setFilter(PropertyFilter.eq("Url",linkAddress))
        .build();
        
        QueryResults<Entity> results = datastore.run(query); 
        List<Images> imagesUrl = new ArrayList<>();

        
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

