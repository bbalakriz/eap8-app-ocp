/*
 * Copyright 2022 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bala.eap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Optional;
import org.junit.Test;

/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
public class HelloWorldServletIT {

    protected URI getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080/eap8-simple";
        }
        try {
            return new URI(host + "/HelloWorld");
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }

    @Test
    public void testHelloWorld() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(Redirect.ALWAYS)
                .connectTimeout(Duration.ofMinutes(1))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getHTTPEndpoint())
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Optional<String> contentType = response.headers().firstValue("Content-Type");
        assertTrue(contentType.isPresent());
        String[] content = response.body().split(getLineSeparator());
        System.out.println(content.toString());
        System.out.println(content.length);
    }

    protected String getLineSeparator() {
        return"\n";
    }
}
