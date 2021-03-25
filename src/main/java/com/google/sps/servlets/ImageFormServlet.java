package com.google.sps.servlets;

import java.io.IOException;
import java.io.InputStream;
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
            UUID uuid = UUID.randomUUID(); //This is a randomly generated ID for the image

            String message = request.getParameter("message");
            Part filePart = request.getPart("image");
            String fileName = uuid.toString(); //This could be changed later if we have a system for it
            InputStream fileInputStream = filePart.getInputStream();

            //String uploadFileUrl = uploadToStorage(fileName, fileInputStream, message); 
            //This method will upload to cloudstorage and then get a blob, preferably to use with the message.
            System.out.println("Upload confirmed.");
            response.sendRedirect("/index.html"); //Redirects back home for now, will change to list of images later.
    }
}