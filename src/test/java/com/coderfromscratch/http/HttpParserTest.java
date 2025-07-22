//Read RFC-7231 (Section 4.1 - Request Methods)

package com.coderfromscratch.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)         //This tells JUnit to use the same instance for all test methods, so @BeforeAll no longer needs to be static.
class HttpParserTest {
    private HttpParser httpParser;

    @BeforeAll
    public void beforeClass(){
        httpParser=new HttpParser();
    }

    // test method for "GOOD" input
    @Test
   // @org.junit.jupiter.api.Test
    void parseHttpRequest() throws IOException {
        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(
                    generateValidGETTestCase()
            );
        } catch (HttpParsingException e) {
            fail(e);
        }

        // Ensure the request object was successfully parsed and is not null
        assertNotNull(request);
        // Check that the HTTP method was correctly parsed as GET
        assertEquals(request.getMethod(),HttpMethod.GET);
        // Verify the request target is correctly parsed as "/"
        assertEquals(request.getRequestTarget(),"/");
        // Confirm the original HTTP version string is "HTTP/1.1"
        assertEquals(request.getOriginalHttpVersion(),"HTTP/1.1");
        // Ensure the best compatible HTTP version is HTTP/1.1
        assertEquals(request.getBestCompatibleVersion(),HttpVersion.HTTP_1_1);
    }

    //tes method for "BAD" input
    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestBadMethod() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseMethodName()
            );
            fail();
        } catch (HttpParsingException e) {
            // Verify that the error code indicates a 501 Not Implemented status for unsupported method
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestBadMethod1() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseMethodName1()
            );
            fail();
        } catch (HttpParsingException e) {
            // Verify that the error code indicates a 501 Not Implemented status for unsupported method
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestInvalidNumberOfItems() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseRequestLineInvalidNumberOfItems()
            );
            fail();
        } catch (HttpParsingException e) {
            // Confirm that the error code indicates a 400 Bad Request due to invalid request line format
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            //e.printStackTrace();
        }
    }

    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestEmptyRequestLine() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseEmptyRequestLine()
            );
            fail();
        } catch (HttpParsingException e) {
            // Confirm that the error code indicates a 400 Bad Request due to empty request line
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            //e.printStackTrace();
        }
    }

    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestLineCRnoLF() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseRequestLineOnlyCRnoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            // Confirm that the error code indicates a 400 Bad Request due to malformed line endings (CR without LF)
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
            //e.printStackTrace();
        }
    }

    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestBadHttpVersion() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseBadHttpVersion()
            );
            fail();
        } catch (HttpParsingException e) {
            // Verify that the error code indicates a 400 Bad Request due to invalid HTTP version format
            assertEquals(e.getErrorCode(),HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestUnsupportedHttpVersion() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseUnsupportedHttpversion()
            );
            fail();
        } catch (HttpParsingException e) {
            // Verify that the error code indicates a 505 HTTP Version Not Supported status
            assertEquals(e.getErrorCode(),HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    @Test
        // @org.junit.jupiter.api.Test
    void parseHttpRequestSupportedHttpVersion() throws IOException {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateSupportedHttpversion()
            );
            // Ensure the request object was successfully parsed and is not null
            assertNotNull(request);
            // Check that the best compatible HTTP version defaults to HTTP/1.1
            assertEquals(request.getBestCompatibleVersion(),HttpVersion.HTTP_1_1);
            // Confirm the original HTTP version string is "HTTP/1.2"
            assertEquals(request.getOriginalHttpVersion(),"HTTP/1.2");
        } catch (HttpParsingException e) {
            fail();
        }
    }

    //mock "GOOD" input
    private InputStream generateValidGETTestCase(){
        String rawData= "GET / HTTP/1.1\r\n" +
                "Host: localhost:8081\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "Accept: */*\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    //mock "BAD" input
    private InputStream generateBadTestCaseMethodName(){
        String rawData= "GeT / HTTP/1.1\r\n" +
                 "User-Agent: curl/8.7.1\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    //mock "BAD" input for longer METHOD name than the existing ones
    private InputStream generateBadTestCaseMethodName1(){
        String rawData= "GEETTTTTT / HTTP/1.1\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseRequestLineInvalidNumberOfItems(){
        String rawData= "GET / AAAAAAAAA HTTP/1.1\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseEmptyRequestLine(){
        String rawData= "\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseRequestLineOnlyCRnoLF() {
        String rawData= "GET / HTTP/1.1\r" +    // <-- no LF
                "Host: localhost:8081\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "Accept: */*\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseBadHttpVersion() {
        String rawData= "GET / HTP/1.1\r" +    // <-- no LF
                "Host: localhost:8081\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "Accept: */*\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseUnsupportedHttpversion() {
        String rawData= "GET / HTTP/2.1\r\n" +    // <-- no LF
                "Host: localhost:8081\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "Accept: */*\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }

    private InputStream generateSupportedHttpversion() {
        String rawData= "GET / HTTP/1.2\r\n" +    // <-- no LF
                "Host: localhost:8081\r\n" +
                "User-Agent: curl/8.7.1\r\n" +
                "Accept: */*\r\n" +
                "\r\n";

        InputStream inputStream=new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        return inputStream;
    }
}
