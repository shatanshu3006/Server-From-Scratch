package com.coderfromscratch.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpParser {

    // note

//    	HTTP requests are text-based, so parsing them at the character level (via InputStreamReader)
//      is more appropriate than handling raw bytes.

    private final static Logger LOGGER= LoggerFactory.getLogger(HttpParser.class);

    //for the table of ascii codes for parsing the requests
    private static final int SP = 0x20;   //32        //SP is space
    private static final int CR = 0x0D;   //13        //carriage return
    private static final int LF = 0x0A;   //10        //line feed


    public HttpRequest parseHttpRequest(InputStream inputStream) throws IOException, HttpParsingException {
        //reader is used for conversion from bytes to characters on inputStream
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request=new HttpRequest();

        try {
            parseRequestLine(reader,request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parseHeaders(reader,request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseBody(reader,request);

        return request;
    }


    private void parseRequestLine(InputStreamReader reader,HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder();

        boolean methodParsed=false;
        boolean requestTargetParsed=false;

        int _byte;
        while((_byte= reader.read())>=0){
            if(_byte == CR){
                // log the last chunk before exiting
                _byte = reader.read();

                if(_byte == LF){
                    LOGGER.debug("Request Line VERSION to Process: {}", processingDataBuffer.toString());
                    if(!methodParsed || !requestTargetParsed){
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    try {
                        request.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    return;
                }
                else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if(_byte == SP){
                // TODO process the previous data
                if(!methodParsed) {
                    LOGGER.debug("Request Line METHOD to Process: {}", processingDataBuffer.toString());
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed=true;
                }
                else if (!requestTargetParsed){
                    LOGGER.debug("Request Line REQ TARGET to Process : {}", processingDataBuffer.toString());
                    request.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed=true;
                }
                else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
                processingDataBuffer.delete(0,processingDataBuffer.length());       //clearing the buffer
            }
            else{
                processingDataBuffer.append((char)_byte);

                //handling if the incoming method is a million characters long, then we have to parse it and take it into memory and then validate if the method is correct or not so we have to perform a check:
                if(!methodParsed){
                    if(processingDataBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader reader,HttpRequest request) throws IOException, HttpParsingException {

        StringBuilder processingDataBuffer = new StringBuilder();
        boolean crlfFound=false;

        int _byte;
        while((_byte= reader.read())>=0){
            if(_byte==CR){
                _byte= reader.read();
                if(_byte==LF){
                    if(!crlfFound){
                        crlfFound=true;

                        //do the things like processing
                        processSingleHeaderField(processingDataBuffer,request);

                        //clear the buffer
                        processingDataBuffer.delete(0,processingDataBuffer.length());

                    }

                }
                else{
                    throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            else{
                //TODO Append to buffer
                crlfFound=false;
                processingDataBuffer.append((char)_byte);
            }
        }
    }

    private void processSingleHeaderField(StringBuilder processingDataBuffer, HttpRequest request) throws HttpParsingException {
        String rawHeaderField = processingDataBuffer.toString();
        Pattern pattern = Pattern.compile("^(?<fieldName>[!#$%&’*+\\-./^_‘|˜\\dA-Za-z]+):\\s?(?<fieldValue>[!#$%&’*+\\-./^_‘|˜(),:;<=>?@[\\\\]{}\" \\dA-Za-z]+)\\s?$");

        Matcher matcher = pattern.matcher(rawHeaderField);
        if (matcher.matches()) {
            // We found a proper header
            String fieldName = matcher.group("fieldName");
            String fieldValue = matcher.group("fieldValue");
            request.addHeader(fieldName, fieldValue);
        } else{
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private void parseBody(InputStreamReader reader,HttpRequest request){

    }

}
