package com.vuxiii.compiler.Error;

public class Error {

    String title;
    String body;

    public Error( String title, String body ) {
        this.title = title;
        this.body = body;
    }


    public String toString() {
        return "\u001B[41m\u001B[37m--[[ " + title + " ]]--\u001B[0m\n" + body;
    }
}
