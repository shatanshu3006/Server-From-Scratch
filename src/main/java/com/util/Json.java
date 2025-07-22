/**  This class acts as a central helper to easily read, write, and transform JSON data—crucial for processing HTTP requests and responses in a server. **/

package com.util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class Json {

    private static ObjectMapper myObjectMapper=defaultObjectMapper();

    // Creates and returns a default ObjectMapper with some basic configuration
    private static ObjectMapper defaultObjectMapper(){
        ObjectMapper om= new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return om;
    }

    // Converts a JSON string into a JsonNode object
    public static JsonNode parse(String jsonSrc) throws IOException {
        return myObjectMapper.readTree(jsonSrc);
    }

    // Converts a JsonNode into a Java object of the given class
    public static <A> A fromJson(JsonNode node,Class<A>clazz) throws JsonProcessingException {
        return myObjectMapper.treeToValue(node,clazz);
    }

    // Converts a Java object into a JsonNode
    public static JsonNode toJson(Object obj){
        return myObjectMapper.valueToTree(obj);
    }

    // Converts a JsonNode to a compact JSON string
    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJson(node,false);
    }

    // Converts a JsonNode to a pretty-printed JSON string
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJson(node,true);
    }

    // Converts an object to JSON string, with optional pretty printing
    private static String generateJson(Object o,boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = myObjectMapper.writer();

        if(pretty){
            objectWriter= objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }

        return objectWriter.writeValueAsString(o);
    }


}
