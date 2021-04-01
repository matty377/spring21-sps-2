package com.google.sps.data;

/*Image POJO (Plain Old Java Object)*/
public final class Images
{
    private final String message;
    private final String Url;

    public Images (String message, String Url)
    {
        this.message = message;
        this.Url = Url;
    }

    public String getMessage ()
    {
        return message;
    }

    public String getUrl ()
    {
        return Url;
    }
}