/*
    LTIToolProvider - Classes to handle connections with an LTI 1 compliant tool consumer
    Copyright (C) 2013  Stephen P Vickers

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    Contact: stephen@spvsoftwareproducts.com

    Version history:
      1.0.00   2-Jan-13  Initial release
      1.1.00  13-Apr-13
      1.1.01  18-Jun-13
*/
package org.tsaap.lti.tp;

import org.apache.commons.httpclient.NameValuePair;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Class to represent miscellaneous utility methods.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public class Utils {

    /**
     * Converts a String value to a float value
     *
     * @param value string to be converted
     * @return numeric value, or null if not a numeric string
     */
    public static Float stringToFloat(String value) {

        Float fValue = null;
        try {
            fValue = Float.valueOf(value);
        } catch (NumberFormatException e) {
        }

        return fValue;

    }

    /**
     * Converts a float value to a String value
     *
     * @param fValue to be converted
     * @return converted value
     */
    public static String floatToString(float fValue) {

        String value = String.valueOf(fValue);
        value = value.replaceFirst("\\.*0*$", "");

        return value;

    }

    /**
     * Generates a random string.
     * <p>
     * The generated string will only comprise letters (upper- and lower-case) and digits.
     *
     * @param length Length of string to be generated
     * @return random string
     */
    protected static String getRandomString(int length) {

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random rand = new Random();
        StringBuilder value = new StringBuilder();
        int charsLength = chars.length() - 1;

        for (int i = 0; i < length; i++) {
            value.append(chars.charAt(rand.nextInt(charsLength)));
        }

        return value.toString();

    }

    /**
     * Returns a string in quotes for use in a database query.
     * <p>
     * Any single quotes in the value passed will be replaced with two single quotes.  If a null value is passed, a string
     * of 'NULL' is returned (which will never be enclosed in quotes irrespective of the value of the $addQuotes parameter.
     *
     * @param value     value to be quoted
     * @param addQuotes <code>true</code> if the returned string is to be enclosed in single quotes
     * @return <code>true</code> if the user object was successfully deleted
     */
    protected static String quoted(String value, boolean addQuotes) {

        if (value == null) {
            value = "NULL";
        } else {
            value = value.replaceAll("'", "''");
            if (addQuotes) {
                value = "'" + value + "'";
            }
        }

        return value;

    }

    /**
     * Returns a URL-encoded string.
     *
     * @param value value to be encoded
     * @return encoded string
     */
    protected static String urlEncode(String value) {

        String encoded;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encoded = URLEncoder.encode(value);
        }

        return encoded;

    }

    /**
     * Returns an array of name/value pair objects representing values in a map.
     *
     * @param params map of String values
     * @return array of name/value pairs
     */
    protected static NameValuePair[] getHTTPParams(Map<String, String> params) {

        NameValuePair[] nvPairs = null;
        if (params != null) {
            nvPairs = new NameValuePair[params.size()];
            int i = 0;
            for (Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry<String, String> entry = iter.next();
                nvPairs[i] = new NameValuePair(entry.getKey(), entry.getValue());
                i++;
            }
        }

        return nvPairs;

    }

    /**
     * Returns an array of name/value pair objects representing map entry values in a list.
     *
     * @param params list of map entry values
     * @return array of name/value pairs
     */
    protected static NameValuePair[] getHTTPParams(List<Map.Entry<String, String>> params) {

        NameValuePair[] nvPairs = null;
        if (params != null) {
            nvPairs = new NameValuePair[params.size()];
            int i = 0;
            for (Iterator<Map.Entry<String, String>> iter = params.iterator(); iter.hasNext(); ) {
                Map.Entry<String, String> entry = iter.next();
                nvPairs[i] = new NameValuePair(entry.getKey(), entry.getValue());
                i++;
            }
        }

        return nvPairs;

    }

    /**
     * Returns an XML document from an XML String
     *
     * @param xml String containing XML
     * @return XML document, null if not XML
     */
    protected static Document getXMLDoc(String xml) {

        Document xmlDoc = null;

// Remove any garbage from the top of the XML response
        int pos = xml.indexOf("<?xml ");
        if (pos > 0) {
            xml = xml.substring(pos);
        }
        try {
            SAXBuilder sb = new SAXBuilder();
            xmlDoc = sb.build(new ByteArrayInputStream(xml.getBytes()));
        } catch (JDOMException e) {
        } catch (IOException e) {
        }

        return xmlDoc;

    }

    /**
     * Returns a named XML child element given a parent element.
     * <p>
     * The first element will be returned if more than one exists with the given name.
     * The first child element will be returned if the name is null.
     *
     * @param root parent element
     * @param name name of child element
     * @return child element, null if not found
     */
    protected static Element getXmlChild(Element root, String name) {

        Element child = null;
        if (name != null) {
            ElementFilter elementFilter = new ElementFilter(name);
            Iterator<Element> iter = (Iterator<Element>) root.getDescendants(elementFilter);
            if (iter.hasNext()) {
                child = iter.next();
            }
        } else {
            List<Element> elements = (List<Element>) root.getChildren();
            if (elements.size() >= 1) {
                child = elements.get(0);
            }
        }

        return child;

    }

    /**
     * Returns the value of a named XML child element given a parent element.
     * <p>
     * The value of the first element will be returned if more than one exists with the given name.
     * The value of the first child element will be returned if the name is null.
     *
     * @param root parent element
     * @param name name of child element
     * @return element value, null if not found
     */
    protected static String getXmlChildValue(Element root, String name) {

        String value = null;
        Element child = getXmlChild(root, name);
        if (child != null) {
            value = child.getText();
        }

        return value;

    }

}
