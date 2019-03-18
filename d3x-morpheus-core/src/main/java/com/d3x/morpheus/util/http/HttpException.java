/*
 * Copyright (C) 2014-2018 D3X Systems - All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.d3x.morpheus.util.http;

/**
 * A runtime exception generated by the Morpheus http client adapter
 *
 * @author Xavier Witdouck
 *
 * <p><strong>This is open source software released under the <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache 2.0 License</a></strong></p>
 */
public class HttpException extends RuntimeException {

    private HttpRequest request;

    /**
     * Constructor
     * @param request       the request associated with this exception
     * @param cause         the root cause, null permitted
     */
    public HttpException(HttpRequest request, String message, Throwable cause) {
        super(message, cause);
        this.request = request;
    }

    /**
     * The http request associated with this exception
     * @return  the http request
     */
    public HttpRequest getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "HttpException{" +  "request=" + request + '}';
    }
}