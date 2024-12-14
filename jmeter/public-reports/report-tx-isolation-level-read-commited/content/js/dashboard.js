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

    var data = {"OkPercent": 47.916666666666664, "KoPercent": 52.083333333333336};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.4791666666666667, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.6666666666666666, 500, 1500, "Create dragon with new name"], "isController": false}, {"data": [0.0, 500, 1500, "Import too many treasures"], "isController": false}, {"data": [1.0, 500, 1500, "Update cave to normal treasures"], "isController": false}, {"data": [1.0, 500, 1500, "Create cave with normal treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Import dragons with names intersections"], "isController": false}, {"data": [0.3333333333333333, 500, 1500, "Import unique dragons"], "isController": false}, {"data": [1.0, 500, 1500, "Login"], "isController": false}, {"data": [0.0, 500, 1500, "Update cave to too many treasures"], "isController": false}, {"data": [0.3333333333333333, 500, 1500, "Import normal treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Update to existing dragon"], "isController": false}, {"data": [0.3333333333333333, 500, 1500, "Delete existing dragon"], "isController": false}, {"data": [1.0, 500, 1500, "Update to new dragon"], "isController": false}, {"data": [0.0, 500, 1500, "Create dragon with existing name"], "isController": false}, {"data": [0.0, 500, 1500, "Create cave with too many treasures"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 48, 25, 52.083333333333336, 97.06250000000003, 17, 281, 75.0, 221.5, 248.29999999999993, 281.0, 44.6927374301676, 25.40608269320298, 46.57766672486033], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Create dragon with new name", 3, 1, 33.333333333333336, 99.0, 69, 114, 114.0, 114.0, 114.0, 114.0, 9.46372239747634, 6.65109917192429, 8.696643335962145], "isController": false}, {"data": ["Import too many treasures", 3, 3, 100.0, 176.33333333333334, 151, 209, 169.0, 209.0, 209.0, 209.0, 10.273972602739725, 5.1670858304794525, 22.494381421232877], "isController": false}, {"data": ["Update cave to normal treasures", 3, 0, 0.0, 53.0, 36, 64, 59.0, 64.0, 64.0, 64.0, 12.0, 8.6796875, 7.2890625], "isController": false}, {"data": ["Create cave with normal treasures", 3, 0, 0.0, 97.66666666666667, 81, 107, 105.0, 107.0, 107.0, 107.0, 9.404388714733543, 7.8431132445141065, 8.69722276645768], "isController": false}, {"data": ["Import dragons with names intersections", 3, 3, 100.0, 186.0, 159, 200, 199.0, 200.0, 200.0, 200.0, 11.029411764705882, 5.547018612132352, 24.051441865808822], "isController": false}, {"data": ["Import unique dragons", 3, 2, 66.66666666666667, 210.0, 149, 260, 221.0, 260.0, 260.0, 260.0, 8.849557522123893, 4.442063053097344, 19.40161780973451], "isController": false}, {"data": ["Login", 9, 0, 0.0, 45.44444444444444, 17, 94, 22.0, 94.0, 94.0, 94.0, 15.358361774744028, 9.538982508532424, 6.614294475255973], "isController": false}, {"data": ["Update cave to too many treasures", 3, 3, 100.0, 39.666666666666664, 30, 46, 43.0, 46.0, 46.0, 46.0, 13.698630136986301, 6.367722602739726, 8.320847602739725], "isController": false}, {"data": ["Import normal treasures", 3, 2, 66.66666666666667, 207.66666666666666, 158, 281, 184.0, 281.0, 281.0, 281.0, 8.955223880597016, 4.498017723880597, 19.39715485074627], "isController": false}, {"data": ["Update to existing dragon", 3, 3, 100.0, 34.666666666666664, 32, 36, 36.0, 36.0, 36.0, 36.0, 12.76595744680851, 5.74717420212766, 7.5673204787234045], "isController": false}, {"data": ["Delete existing dragon", 3, 2, 66.66666666666667, 26.333333333333332, 19, 34, 26.0, 34.0, 34.0, 34.0, 6.224066390041493, 2.4981360217842323, 3.519271913900415], "isController": false}, {"data": ["Update to new dragon", 3, 0, 0.0, 33.0, 26, 38, 35.0, 38.0, 38.0, 38.0, 13.274336283185841, 9.540929203539823, 7.829784292035398], "isController": false}, {"data": ["Create dragon with existing name", 3, 3, 100.0, 125.33333333333334, 34, 226, 116.0, 226.0, 226.0, 226.0, 6.329113924050633, 2.867879746835443, 5.822290348101266], "isController": false}, {"data": ["Create cave with too many treasures", 3, 3, 100.0, 128.0, 27, 234, 123.0, 234.0, 234.0, 234.0, 6.3965884861407245, 3.029634195095949, 5.934335021321962], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 9, 36.0, 18.75], "isController": false}, {"data": ["404", 2, 8.0, 4.166666666666667], "isController": false}, {"data": ["Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 10, 40.0, 20.833333333333332], "isController": false}, {"data": ["409", 4, 16.0, 8.333333333333334], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 48, 25, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 10, "500", 9, "409", 4, "404", 2, "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["Create dragon with new name", 3, 1, "409", 1, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import too many treasures", 3, 3, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["Import dragons with names intersections", 3, 3, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import unique dragons", 3, 2, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["Update cave to too many treasures", 3, 3, "500", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import normal treasures", 3, 2, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Update to existing dragon", 3, 3, "500", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Delete existing dragon", 3, 2, "404", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["Create dragon with existing name", 3, 3, "409", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Create cave with too many treasures", 3, 3, "500", 3, "", "", "", "", "", "", "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
