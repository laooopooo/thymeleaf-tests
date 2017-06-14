/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2016, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.spring5.reactive.exchange;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.PathSegment;
import org.springframework.http.server.reactive.PathSegmentContainer;
import org.springframework.http.server.reactive.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;

public final class TestingServerHttpRequest implements ServerHttpRequest {

    private final String contextPath;
    private final String path;
    private final URI uri;
    private final HttpHeaders headers;
    private final Flux<DataBuffer> body;
    private final MultiValueMap<String, String> queryParams;
    private final MultiValueMap<String, HttpCookie> cookies;


    public TestingServerHttpRequest(final String path, final Map<String,List<String>> queryParams) {
        super();
        this.contextPath = "/testing";
        this.path = path;
        try {
            this.uri = new URI(this.contextPath + "/" + path);
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
        this.headers = new HttpHeaders();
        this.body = Flux.empty();
        this.queryParams = new LinkedMultiValueMap<String,String>(queryParams);
        this.cookies = new LinkedMultiValueMap<>();
    }



    @Override
    public RequestPath getPath() {

        return new RequestPath() {
            @Override
            public PathSegmentContainer contextPath() {
                return new PathSegmentContainer() {
                    @Override
                    public String value() {
                        return TestingServerHttpRequest.this.contextPath;
                    }

                    @Override
                    public List<PathSegment> pathSegments() {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override
            public PathSegmentContainer pathWithinApplication() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String value() {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<PathSegment> pathSegments() {
                throw new UnsupportedOperationException();
            }
        };

    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        return this.queryParams;
    }

    @Override
    public MultiValueMap<String, HttpCookie> getCookies() {
        return this.cookies;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getMethodValue() {
        return HttpMethod.GET.name();
    }

    @Override
    public URI getURI() {
        return this.uri;
    }

    @Override
    public Flux<DataBuffer> getBody() {
        return Flux.empty();
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }

}
