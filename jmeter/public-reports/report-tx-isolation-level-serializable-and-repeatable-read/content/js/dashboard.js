/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 0.0, "KoPercent": 100.0};
    var dataset = [
        {
            "label" : "FAIL",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "PASS",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.0, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.0, 500, 1500, "Create dragon with new name"], "isController": false}, {"data": [0.0, 500, 1500, "Import too many treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Update cave to normal treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Create cave with normal treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Import dragons with names intersections"], "isController": false}, {"data": [0.0, 500, 1500, "Import unique dragons"], "isController": false}, {"data": [0.0, 500, 1500, "Login"], "isController": false}, {"data": [0.0, 500, 1500, "Update cave to too many treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Import normal treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Update to existing dragon"], "isController": false}, {"data": [0.0, 500, 1500, "Delete existing dragon"], "isController": false}, {"data": [0.0, 500, 1500, "Update to new dragon"], "isController": false}, {"data": [0.0, 500, 1500, "Create dragon with existing name"], "isController": false}, {"data": [0.0, 500, 1500, "Create cave with too many treasures"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 48, 48, 100.0, 5009.270833333334, 5001, 5033, 5006.0, 5019.3, 5033.0, 5033.0, 1.3473684210526315, 3.2710526315789474, 0.0], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Create dragon with new name", 3, 3, 100.0, 5006.666666666667, 5005, 5009, 5006.0, 5009.0, 5009.0, 5009.0, 0.5461496450027307, 1.3259062670671764, 0.0], "isController": false}, {"data": ["Import too many treasures", 3, 3, 100.0, 5010.666666666667, 5009, 5012, 5011.0, 5012.0, 5012.0, 5012.0, 0.5463485703879075, 1.32638920506283, 0.0], "isController": false}, {"data": ["Update cave to normal treasures", 3, 3, 100.0, 5005.666666666667, 5005, 5006, 5006.0, 5006.0, 5006.0, 5006.0, 0.547645125958379, 1.3295368975903614, 0.0], "isController": false}, {"data": ["Create cave with normal treasures", 3, 3, 100.0, 5006.666666666667, 5005, 5009, 5006.0, 5009.0, 5009.0, 5009.0, 0.5458515283842794, 1.3251825191048034, 0.0], "isController": false}, {"data": ["Import dragons with names intersections", 3, 3, 100.0, 5011.0, 5009, 5013, 5011.0, 5013.0, 5013.0, 5013.0, 0.5460502366217692, 1.325664929923553, 0.0], "isController": false}, {"data": ["Import unique dragons", 3, 3, 100.0, 5014.0, 5009, 5019, 5014.0, 5019.0, 5019.0, 5019.0, 0.5454545454545455, 1.32421875, 0.0], "isController": false}, {"data": ["Login", 9, 9, 100.0, 5014.666666666667, 5001, 5033, 5007.0, 5033.0, 5033.0, 5033.0, 1.6157989228007181, 3.9227305879712744, 0.0], "isController": false}, {"data": ["Update cave to too many treasures", 3, 3, 100.0, 5007.333333333333, 5005, 5011, 5006.0, 5011.0, 5011.0, 5011.0, 0.5470459518599562, 1.328082262035011, 0.0], "isController": false}, {"data": ["Import normal treasures", 3, 3, 100.0, 5013.666666666667, 5009, 5022, 5010.0, 5022.0, 5022.0, 5022.0, 0.5454545454545455, 1.32421875, 0.0], "isController": false}, {"data": ["Update to existing dragon", 3, 3, 100.0, 5006.0, 5005, 5007, 5006.0, 5007.0, 5007.0, 5007.0, 0.5461496450027307, 1.3259062670671764, 0.0], "isController": false}, {"data": ["Delete existing dragon", 3, 3, 100.0, 5005.0, 5003, 5006, 5006.0, 5006.0, 5006.0, 5006.0, 0.5456529647144416, 1.324700459257912, 0.0], "isController": false}, {"data": ["Update to new dragon", 3, 3, 100.0, 5005.666666666667, 5005, 5006, 5006.0, 5006.0, 5006.0, 5006.0, 0.5462490895848506, 1.3261476920975965, 0.0], "isController": false}, {"data": ["Create dragon with existing name", 3, 3, 100.0, 5006.0, 5005, 5007, 5006.0, 5007.0, 5007.0, 5007.0, 0.5463485703879075, 1.32638920506283, 0.0], "isController": false}, {"data": ["Create cave with too many treasures", 3, 3, 100.0, 5006.0, 5005, 5007, 5006.0, 5007.0, 5007.0, 5007.0, 0.5460502366217692, 1.325664929923553, 0.0], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Median
            case 8:
            // Percentile 1
            case 9:
            // Percentile 2
            case 10:
            // Percentile 3
            case 11:
            // Throughput
            case 12:
            // Kbytes/s
            case 13:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 48, 100.0, 100.0], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 48, 48, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 48, "", "", "", "", "", "", "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["Create dragon with new name", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import too many treasures", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Update cave to normal treasures", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Create cave with normal treasures", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import dragons with names intersections", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import unique dragons", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Login", 9, 9, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 9, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Update cave to too many treasures", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import normal treasures", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Update to existing dragon", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Delete existing dragon", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Update to new dragon", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Create dragon with existing name", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Create cave with too many treasures", 3, 3, "Non HTTP response code: org.apache.http.conn.ConnectTimeoutException/Non HTTP response message: Connect to localhost:18123 [localhost/127.0.0.1] failed: Connect timed out", 3, "", "", "", "", "", "", "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
