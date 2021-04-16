package com.google.sps.data;

import java.util.List;

import com.google.cloud.datastore.Value;

/*Image POJO (Plain Old Java Object)*/
public final class Images
{
    private final String message;
    private final String Url;
    private final List<Value<String>> tags;
    private List<Value<String>> replies;

    public Images (String message, String Url, List<Value<String>> tags)
    {
        this.message = message;
        this.Url = Url;
        this.tags = tags;
    }

    public Images (String message, String Url, List<Value<String>> tags, List<Value<String>> replies)
    {
        this.message = message;
        this.Url = Url;
        this.tags = tags;
        this.replies = replies;
    }

    public String getMessage ()
    {
        return message;
    }

    public String getUrl ()
    {
        return Url;
    }
    
    public List<Value<String>> getTags() {
        return tags;
    }
//Don't actually need these. go back and add, but this shouldn't do things
    public List<Value<String>> getReplies() {
        return replies;
    }

    public void setReplies(List<Value<String>> replies) {
        this.replies = replies;
    }
}