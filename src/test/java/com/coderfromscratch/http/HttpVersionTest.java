package com.coderfromscratch.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {

    // Tests exact version match; should return HTTP/1.1 when "HTTP/1.1" is provided.
    // No fallback is needed in this case.
    @Test
    void getBestCompatibleVersionExactMatch(){
        HttpVersion version=null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        } catch (BadHttpVersionException e) {
            fail();
        }

        assertNotNull(version);
        assertEquals(version,HttpVersion.HTTP_1_1);
    }

    // Tests invalid format input like lowercase "http/1.1".
    // The fallback mechanism does not apply here, so it should throw BadHttpVersionException.
    @Test
    void getBestCompatibleVersionBadFormat(){
        HttpVersion version=null;
        try {
            version = HttpVersion.getBestCompatibleVersion("http/1.1");
            fail();
        } catch (BadHttpVersionException e) {

        }
    }

    // Tests higher version input like "HTTP/1.2".
    // Since "HTTP/1.2" is unsupported but still compatible, it falls back to the highest known supported version, which is HTTP/1.1.
    @Test
    void getBestCompatibleVersionHigherVersion(){
        HttpVersion version=null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
            assertNotNull(version);
            assertEquals(version,HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            fail();
        }
    }
}
