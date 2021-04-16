package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Value;
import com.google.gson.Gson;
import com.google.sps.data.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/post-comment")
public class PostCommentServlet extends HttpServlet {
    private Query<Entity> query;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        Message givenLink = gson.fromJson(reader.readLine(), Message.class);
        String linkAddress = givenLink.getUrl();
        String msg = givenLink.getMsg();
        System.out.println("Link: " + linkAddress);
        System.out.println("Message: " + msg);
        query =
        Query.newEntityQueryBuilder().setKind("Image").setFilter(PropertyFilter.eq("Url",linkAddress))
        .build();

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        QueryResults<Entity> results = datastore.run(query);
        Entity result = results.next();
        List<Value<String>> comments = result.getList("comments");
        List<Value<String>> updatedComments = addToValueList(comments, msg);
        datastore.update(Entity.newBuilder(result).set("comments", updatedComments).build());
        System.out.println("Updated datastore entity");
        response.sendRedirect("/comments.html");
    }

    /**
     * Creates a new List and adds new value
     * @param oldComments List of old comments
     * @param newComment new Comment
     * @return updated List
     */
    private List<Value<String>> addToValueList(List<Value<String>> oldComments, String newComment) {
        List<Value<String>> newComments = new ArrayList<Value<String>>();
        for (Value<String> s : oldComments) {
            newComments.add(s);
        }
        newComments.add(StringValue.of(newComment));

        return newComments;
    }
}
