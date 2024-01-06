package com.example.truyenchu._class;

public class UserClass
{
    // Fields representing user attributes
    private String username;
    private String name;
    private String email;

    private String profile;
    private String password;
    private String font;
    private int fontSize;
    private String backgroundColor;

    // Constructor to initialize the User object
    public UserClass(String username, String name, String email, String profile, String password, String font, int fontSize, String backgroundColor)
    {
        this.username = username;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.password = password;
        this.font = font;
        this.fontSize = fontSize;
        this.backgroundColor = backgroundColor;
    }

    public UserClass() {

    }

    // Getters and setters for each field
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFont()
    {
        return font;
    }

    public void setFont(String font)
    {
        this.font = font;
    }

    public int getFontSize()
    {
        return fontSize;
    }

    public void setFontSize(int fontSize)
    {
        this.fontSize = fontSize;
    }

    public String getBackgroundColor()
    {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}

