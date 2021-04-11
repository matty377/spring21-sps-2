package com.google.sps.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet("/image-analysis")
public class ImageAnalysisServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    // Get the file chosen by the user.
    Part filePart = request.getPart("image");
    String fileName = filePart.getSubmittedFileName();
    InputStream fileInputStream = filePart.getInputStream();
    byte[] imageBytes = fileInputStream.readAllBytes();

    // Upload the file to Cloud Storage and get its URL.
    //String imageUrl = uploadToCloudStorage(fileName, imageBytes);

    // Get the labels of the image that the user uploaded.
    List<EntityAnnotation> imageLabels = getImageLabels(imageBytes);

    System.out.println(imageLabels);

    // Output some HTML that shows the data the user entered.
    // You could also store these in Datastore instead.
    //response.setContentType("text/html");
    //PrintWriter out = response.getWriter();
    //out.println("<p>Here's the image you uploaded:</p>");
    //out.println("<a href=\"" + imageUrl + "\">");
    //out.println("<img src=\"" + imageUrl + "\" />");
    //out.println("</a>");
    //out.println("<p>Here are the labels we extracted:</p>");
    //out.println("<ul>");
    //for (EntityAnnotation label : imageLabels) {
    //  out.println("<li>" + label.getDescription() + " " + label.getScore());
    //}
    //out.println("</ul>");
  }

  /**
   * Generate a list of labels that apply to the image
   * represented by the binary data stored in imgBytes.
   */
  private List<EntityAnnotation> getImageLabels(byte[] imageBytes) throws IOException {
    ByteString byteString = ByteString.copyFrom(imageBytes);
    Image image = Image.newBuilder().setContent(byteString).build();

    Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
    AnnotateImageRequest request =
        AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
    List<AnnotateImageRequest> requests = new ArrayList<>();
    requests.add(request);

    ImageAnnotatorClient client = ImageAnnotatorClient.create();
    BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
    client.close();
    List<AnnotateImageResponse> imageResponses = batchResponse.getResponsesList();
    AnnotateImageResponse imageResponse = imageResponses.get(0);

    if (imageResponse.hasError()) {
      System.err.println("Error getting image labels: " + imageResponse.getError().getMessage());
      return null;
    }

    return imageResponse.getLabelAnnotationsList();
  }

}