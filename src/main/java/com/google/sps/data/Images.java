package com.google.sps.data;

import java.util.List;

import com.google.cloud.datastore.Value;

/*Image POJO (Plain Old Java Object)*/
public final class Images
{
    private final String message;
    private final String Url;
    private final List<Value<String>> tags;

    public Images (String message, String Url, List<Value<String>> tags)
    {
        this.message = message;
        this.Url = Url;
        this.tags = tags;
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
}