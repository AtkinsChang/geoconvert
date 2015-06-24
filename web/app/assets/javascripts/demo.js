/*
 * Licensed to the Programming Language and Software Methodology Lab (PLSM)
 * under one or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership.
 * The PLSM licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function() {
    $(document).ready(
        function() {
            var compile = function() {
                logger.yellow("Compiling...");
                $.ajax({
                    data: JSON.stringify({
                        "code": editor.getSession().getValue()
                    }),
                    type: "POST",
                    url: "/compile",
                    contentType: 'application/json; charset=utf-8',
                    dataType: 'json',
                    cache: true,
                    success: function (response) {
                        if (response.result.success) {
                            logger.green("Success, time elapsed: " + response.result.time + " ms");
                            logger.blue("Result:");
                            window._gmap.cleanPoints();
                            response.result.results.map(function(result){
                                logger.blue(result.log);
                                window._gmap.addPoint(result.location.lat, result.location.lng, result.name, result.html);
                                var bounds = new google.maps.LatLngBounds();
                                window._gmap_marker.forEach(function(marker) {
                                    bounds.extend(marker.getPosition());
                                });
                                window._gmap.fitBounds(bounds);
                                if(window._gmap.getZoom() > 15) {
                                    window._gmap.setZoom(15);
                                }
                            });
                        } else {
                            logger.red("Failed, time elapsed: " + response.result.time + " ms");
                            response.result.error.map(function(msg){logger.red(msg);});
                        }
                        logger.newLine();
                    }
                });
            };
            var logger = {
                "logArea": $("#log"),
                "scrollBottom": function() {
                    this.logArea[0].scrollTop = Number.MAX_VALUE;
                },
                "newLine": function() {
                    this.logArea.append($("<br />"));
                    this.scrollBottom();
                },
                "log": function(msg, color) {
                    this.logArea.append($("<div>").html(msg).css({"color": color}));
                    this.scrollBottom();
                },
                "orange": function (msg) {
                    this.log(msg, "#E57255");
                },
                "blue": function(msg) {
                    this.log(msg, "#46BDDF");
                },
                "green": function(msg) {
                    this.log(msg, "#52D273");
                },
                "yellow": function(msg) {
                    this.log(msg, "#E5C453");
                },
                "red": function(msg) {
                    this.log(msg, "#E95065");
                }
            };

            var editor = ace.edit("editor");
            editor.setTheme("ace/theme/twilight");
            editor.renderer.setShowGutter(false);
            editor.setShowPrintMargin(false);
            editor.getSession().setMode("ace/mode/scala");
            editor.commands.addCommand({
                "name": "Compile",
                "bindKey": {
                    "win": "Ctrl-Enter|Cmd-Enter",
                    "mac": "Ctrl-Enter|Cmd-Enter",
                    "sender":"editor|cli"
                },
                "exec": compile
            });
            editor.getSession().setTabSize(2);
            $.ajax({
                type: "GET",
                url: "/buildinfo",
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                cache: true,
                success: function (json) {
                    var url = "https://github.com/AtkinsChang/geoconvert";
                    logger.logArea.append(
                        $("<div>").append("Enter ").append(
                            $("<span>").html("Cmd/Ctrl-Enter").css({"color": "#E5C453"})
                        ).append(" to compile")
                    );
                    logger.logArea.append(
                        $("<div>").append("Source code: ")
                            .append(
                            $("<a>")
                                .attr('href', url)
                                .html(url)
                        )
                    );
                    for(var k in json) {
                        logger.logArea.append(
                            $("<div>").append("- ").append(
                                $("<span>").html(k).css({"color": "#46BDDF"})
                            ).append(": ").append(
                                $("<span>").html(json[k]).css({"color": "#52D273"})
                            )
                        );
                    }
                    logger.newLine();
                }
            });
            $.ajax({
                type: "GET",
                url: "/sample",
                cache: true,
                success: function (response) {
                    editor.getSession().setValue(response);
                }
            });

            window._gmap_init = function () {
                var mapOptions = {
                    zoom: 17,
                    center: new google.maps.LatLng(24.9862332, 121.575843),
                    panControl: true,
                    zoomControl: true,
                    mapTypeControl: true,
                    scaleControl: true,
                    streetViewControl: true,
                    overviewMapControl: true
                };
                google.maps.Map.prototype.cleanPoints = function() {
                    if (typeof window._gmap_marker != 'undefined') {
                        window._gmap_marker.map(
                            function(marker){
                                google.maps.event.clearListeners(marker, 'click');
                                if(typeof marker != 'undefined' && typeof marker.setMap != 'undefined') {
                                    marker.setMap(null);
                                }
                            }
                        );
                        window._gmap_marker.length = 0;
                    }
                };
                google.maps.Map.prototype.addPoint = function(lat, lng, name, info) {
                    if (typeof window._gmap_marker != 'undefined') {
                        var marker = new google.maps.Marker({
                            position: new google.maps.LatLng(lat, lng),
                            map: window._gmap,
                            title: name
                        });
                        window._gmap_marker.push(marker);
                        var infowindow = new google.maps.InfoWindow({
                            content: info
                        });
                        google.maps.event.addListener(marker, 'click', function () {
                            infowindow.open(window._gmap, marker);
                        });
                    }
                };
                window._gmap = new google.maps.Map($('#map-canvas')[0], mapOptions);
                window._gmap_marker = [];
                delete window._gmap_init;
            };

            var script = document.createElement("script");
            script.type = "text/javascript";
            script.src = "//maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&signed_in=true&callback=_gmap_init";
            $("body").append(script);
        }
    );
})();