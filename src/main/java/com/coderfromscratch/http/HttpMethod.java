package com.coderfromscratch.http;

public enum HttpMethod {
     GET, HEAD;
     public static final int MAX_LENGTH;

     static {
         int tempMaxLength=-1;
         for(HttpMethod method:values()){
             if(method.name().length()>tempMaxLength){
                 tempMaxLength=method.name().length();
             }
         }
         MAX_LENGTH=tempMaxLength;
     }
}

/*
        -------    HANDLING LONGER METHODS   --------
    1.	Purpose of MAX_LENGTH: Instead of hardcoding a fixed value, the MAX_LENGTH in HttpMethod is dynamically computed using a static block to always reflect the length of the longest HTTP method name (e.g., GET, HEAD, or longer ones like DELETE).
	2.	Flexibility Benefit: This makes the code future-proof — if you later add methods like DELETE or OPTIONS, which are longer than GET or HEAD, the MAX_LENGTH will automatically adjust without requiring manual changes.
 */
