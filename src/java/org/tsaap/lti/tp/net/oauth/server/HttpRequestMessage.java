/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap.lti.tp.net.oauth.server;

import net.oauth.OAuth;
import net.oauth.OAuthMessage;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * An HttpServletRequest, encapsulated as an OAuthMessage.
 *
 * @author John Kristian
 */
public class HttpRequestMessage extends OAuthMessage {

    public HttpRequestMessage(HttpServletRequest request, String URL) {
        super(request.getMethod(), URL, getParameters(request));
        this.request = request;
        copyHeaders(request, getHeaders());
    }

    private final HttpServletRequest request;

    @Override
    public InputStream getBodyAsStream() throws IOException {
        return request.getInputStream();
    }

    @Override
    public String getBodyEncoding() {
        return request.getCharacterEncoding();
    }

    private static void copyHeaders(HttpServletRequest request, Collection<Map.Entry<String, String>> into) {
        Enumeration<String> names = request.getHeaderNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                Enumeration<String> values = request.getHeaders(name);
                if (values != null) {
                    while (values.hasMoreElements()) {
                        into.add(new OAuth.Parameter(name, values.nextElement()));
                    }
                }
            }
        }
    }

    public static List<OAuth.Parameter> getParameters(HttpServletRequest request) {
        List<OAuth.Parameter> list = new ArrayList<OAuth.Parameter>();
        for (Enumeration<String> headers = request.getHeaders("Authorization"); headers != null
                && headers.hasMoreElements(); ) {
            String header = headers.nextElement();
            for (OAuth.Parameter parameter : OAuthMessage
                    .decodeAuthorization(header)) {
                if (!"realm".equalsIgnoreCase(parameter.getKey())) {
                    list.add(parameter);
                }
            }
        }
        for (Object e : request.getParameterMap().entrySet()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                list.add(new OAuth.Parameter(name, value));
            }
        }
        return list;
    }

}