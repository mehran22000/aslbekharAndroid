package com.example.n55.jsonandvally.model;

/**
 * Created by N55 on 7/14/2017.
 */
public class Login {


    private String UserName, Password , Url;

    public Login(){

    }

    public Login(String UserName , String Password , String Url){

        this.UserName = UserName;
        this.Password = Password ;
        this.Url = Url;
    }


    //UserName

    public String getusername() {
        return UserName;
    }

    public void setusername(String username) {
        this.UserName = username;
    }

    //Password

    public String getpassword() {
        return Password;
    }

    public void setpassword(String password) {
        this.Password = password;
    }

    //URL
    public String geturl() {
        return Url;
    }

    public void seturl(String url) {
        this.Url = url;
    }

}