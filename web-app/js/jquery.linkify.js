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

function linkify(string, buildHashtagUrl, includeW3, target) {
    string = string.replace(/((http|https|ftp)\:\/\/|\bw{3}\.)[a-z0-9\-\.]+\.[a-z]{2,3}(:[a-z0-9]*)?\/?([a-z\u00C0-\u017F0-9\-\._\?\,\'\/\\\+&amp;%\$#\=~])*/gi, function (captured) {
        var uri;
        if (captured.toLowerCase().indexOf("www.") == 0) {
            if (!includeW3) {
                return captured;
            }
            uri = "http://" + captured;
        } else {
            uri = captured;
        }
        return "<a href=\"" + uri + "\" target=\"" + target + "\">" + captured + "</a>";
        ;
    });

    if (buildHashtagUrl) {
        string = string.replace(/\B#(\w+)/g, "<a href=" + buildHashtagUrl("$1") + " target=\"" + target + "\">#$1</a>");
    }
    return string;
}

(function ($) {
    $.fn.linkify = function (opts) {
        return this.each(function () {
            var $this = $(this);
            var buildHashtagUrl;
            var includeW3 = true;
            var target = '_self';
            if (opts) {
                if (typeof opts == "function") {
                    buildHashtagUrl = opts;
                } else {
                    if (typeof opts.hashtagUrlBuilder == "function") {
                        buildHashtagUrl = opts.hashtagUrlBuilder;
                    }
                    if (typeof opts.includeW3 == "boolean") {
                        includeW3 = opts.includeW3;
                    }
                    if (typeof opts.target == "string") {
                        target = opts.target;
                    }
                }
            }
            $this.html(
                $.map(
                    $this.contents(),
                    function (n, i) {
                        if (n.nodeType == 3) {
                            return linkify(n.data, buildHashtagUrl, includeW3, target);
                        } else {
                            return n.outerHTML;
                        }
                    }
                ).join("")
            );
        });
    }
})(jQuery);