package com.coderfromscratch.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)     // this declaration gives flexibility to not declare the beforeClass() method static
public class HttpHeadersParserTest {
    private HttpParser httpParser;

    private Method parserHeadersMethod;

    @BeforeAll
    public void beforeClass() throws NoSuchMethodException {
        // Create the parser instance once for all tests
        httpParser = new HttpParser();
        // Get the Class object for HttpParser
        Class<HttpParser> cls = HttpParser.class;
        // Get the declared method `parseHeaders(InputStreamReader, HttpRequest)`
        parserHeadersMethod = cls.getDeclaredMethod("parseHeaders", InputStreamReader.class, HttpRequest.class);
        // Make it accessible even if it's private or protected
        parserHeadersMethod.setAccessible(true);
    }

    @Test
    public void testPrivateMethod() throws InvocationTargetException, IllegalAccessException {
        parserHeadersMethod.invoke(httpParser, new InputStreamReader(new ByteArrayInputStream("".getBytes())), new HttpRequest());
    }

    @Test
    public void testSimpleSingleHeader() throws InvocationTargetException, IllegalAccessException {
        HttpRequest request = new HttpRequest();
        parserHeadersMethod.invoke(
                httpParser,
                generateSimpleSingleHeaderMethod(),
                request);
        assertEquals(1, request.getHeaderNames().size());
        assertEquals("localhost:8081", request.getHeader("host"));
    }

    @Test
    public void testMultipleHeaders() throws InvocationTargetException, IllegalAccessException {
        HttpRequest request = new HttpRequest();
        parserHeadersMethod.invoke(
                httpParser,
                generateMultipleHeadersMessage(),
                request);
        assertEquals(10, request.getHeaderNames().size());
        assertEquals("localhost:8081", request.getHeader("host"));
    }

    @Test
    public void testErrorSpaceBeforeColonHeader() throws InvocationTargetException, IllegalAccessException {
        HttpRequest request = new HttpRequest();

        try {
            parserHeadersMethod.invoke(
                    httpParser,
                    generateSpaceBeforeColonErrorHeaderMessage(),
                    request);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof HttpParsingException) {
                assertEquals(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST, ((HttpParsingException) e.getCause()).getErrorCode());
            }
        }

    }

    private InputStreamReader generateSimpleSingleHeaderMethod() {
        String rawData = "Host: localhost:8081\r\n";
//                "Host: localhost:8081\r\n" +
//                "User-Agent: curl/8.7.1\r\n" +
//                "Accept: */*\r\n" +
//                "\r\n";
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        return reader;
    }

    private InputStreamReader generateMultipleHeadersMessage() {
        String rawData = "Host: localhost:8080\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        return reader;

    }

    private InputStreamReader generateSpaceBeforeColonErrorHeaderMessage() {
        String rawData = "Host : localhost:8080\r\n\r\n" ;

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        return reader;
    }
}
