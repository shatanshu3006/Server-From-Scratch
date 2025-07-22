package com.coderfromscratch.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1",1,1);           //this is like an enum object

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    //constructor for the enum
    HttpVersion(String LITERAL,int MAJOR,int MINOR){
        this.LITERAL=LITERAL;
        this.MAJOR=MAJOR;
        this.MINOR=MINOR;
    }

    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHttpVersionException{
        Matcher matcher =httpVersionRegexPattern.matcher((literalVersion));
        if(!matcher.find() || matcher.groupCount()!=2){
            //TODO check if this is the exception we want to send later on (RFC has to be referred)
            //so instead we used a newer class called BadHttpVersionException and handle it separately
            throw new BadHttpVersionException();
        }
        int major=Integer.parseInt(matcher.group("major"));
        int minor=Integer.parseInt(matcher.group("minor"));

        HttpVersion temporaryBestCompatible=null;
        for(HttpVersion version:HttpVersion.values()){
            if(version.LITERAL.equals(literalVersion)){
                return version;
            }
            else{
                if(version.MAJOR==major){
                    if(version.MINOR<minor){
                        temporaryBestCompatible=version;
                    }
                }
            }
        }
        return temporaryBestCompatible;
    }
}
