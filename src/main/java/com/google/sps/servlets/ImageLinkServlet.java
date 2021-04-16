package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Value;
import com.google.gson.Gson;
import com.google.sps.data.Images;
import com.google.sps.data.RequestText;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/comment")
public class ImageLinkServlet extends HttpServlet
{
    private Query<Entity> query;
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String projectId = "spring21-sps-2";
        String bucketName = "spring21-sps-2";


        System.out.println(request.getContentType());
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        RequestText givenLink = gson.fromJson(reader.readLine(), RequestText.class);
        String linkAddress = givenLink.getText();
        System.out.println("Link Address:" + linkAddress);
        query = Query.newEntityQueryBuilder().setKind("Image").setFilter(PropertyFilter.eq("Url",linkAddress))
        .build();

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException 
    {
        String projectId = "spring21-sps-2";
        String bucketName = "spring21-sps-2";
        
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query); 
        List<Images> imagesUrl = new ArrayList<>();

        
        while (results.hasNext())
        {
            Entity entity = results.next();
            String message = entity.getString("message");
            String link = entity.getString("Url");
            List<Value<String>> tags = entity.getList("tags");
            List<Value<String>> comments = entity.getList("comments");
            Images img = new Images(message, link, tags, comments); 
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

