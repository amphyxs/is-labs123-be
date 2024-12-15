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

    var data = {"OkPercent": 45.833333333333336, "KoPercent": 54.166666666666664};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.4583333333333333, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.3333333333333333, 500, 1500, "Create dragon with new name"], "isController": false}, {"data": [0.0, 500, 1500, "Import too many treasures"], "isController": false}, {"data": [1.0, 500, 1500, "Update cave to normal treasures"], "isController": false}, {"data": [1.0, 500, 1500, "Create cave with normal treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Import dragons with names intersections"], "isController": false}, {"data": [0.3333333333333333, 500, 1500, "Import unique dragons"], "isController": false}, {"data": [1.0, 500, 1500, "Login"], "isController": false}, {"data": [0.0, 500, 1500, "Update cave to too many treasures"], "isController": false}, {"data": [0.3333333333333333, 500, 1500, "Import normal treasures"], "isController": false}, {"data": [0.0, 500, 1500, "Update to existing dragon"], "isController": false}, {"data": [0.3333333333333333, 500, 1500, "Delete existing dragon"], "isController": false}, {"data": [1.0, 500, 1500, "Update to new dragon"], "isController": false}, {"data": [0.0, 500, 1500, "Create dragon with existing name"], "isController": false}, {"data": [0.0, 500, 1500, "Create cave with too many treasures"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 48, 26, 54.166666666666664, 59.20833333333333, 7, 202, 38.5, 151.20000000000005, 192.84999999999997, 202.0, 54.857142857142854, 30.672991071428573, 57.1171875], "isController": false}, "titles": ["Label", "#Samples", "FAIL", "Error %", "Average", "Min", "Max", "Median", "90th pct", "95th pct", "99th pct", "Transactions/s", "Received", "Sent"], "items": [{"data": ["Create dragon with new name", 3, 2, 66.66666666666667, 33.0, 23, 47, 29.0, 47.0, 47.0, 47.0, 5.836575875486381, 3.370470573929961, 5.363494041828794], "isController": false}, {"data": ["Import too many treasures", 3, 3, 100.0, 155.0, 89, 196, 180.0, 196.0, 196.0, 196.0, 5.825242718446602, 2.9296875, 12.731341019417476], "isController": false}, {"data": ["Update cave to normal treasures", 3, 0, 0.0, 24.666666666666668, 22, 26, 26.0, 26.0, 26.0, 26.0, 6.0728744939271255, 4.364878542510121, 3.688796811740891], "isController": false}, {"data": ["Create cave with normal treasures", 3, 0, 0.0, 32.0, 22, 39, 35.0, 39.0, 39.0, 39.0, 5.964214711729622, 4.9740618787276345, 5.515733722664016], "isController": false}, {"data": ["Import dragons with names intersections", 3, 3, 100.0, 156.66666666666666, 79, 202, 189.0, 202.0, 202.0, 202.0, 5.671077504725898, 2.8503071833648392, 12.294718809073723], "isController": false}, {"data": ["Import unique dragons", 3, 2, 66.66666666666667, 107.66666666666667, 85, 130, 108.0, 130.0, 130.0, 130.0, 5.163511187607574, 2.593521407056799, 11.375860585197936], "isController": false}, {"data": ["Login", 9, 0, 0.0, 33.55555555555556, 7, 56, 27.0, 56.0, 56.0, 56.0, 16.363636363636363, 10.163352272727272, 7.047230113636363], "isController": false}, {"data": ["Update cave to too many treasures", 3, 3, 100.0, 49.0, 30, 72, 45.0, 72.0, 72.0, 72.0, 6.024096385542169, 2.8002635542168677, 3.659167921686747], "isController": false}, {"data": ["Import normal treasures", 3, 2, 66.66666666666667, 126.33333333333333, 115, 148, 116.0, 148.0, 148.0, 148.0, 5.025125628140704, 2.5240132956448913, 10.889407977386934], "isController": false}, {"data": ["Update to existing dragon", 3, 3, 100.0, 38.0, 36, 41, 37.0, 41.0, 41.0, 41.0, 5.639097744360902, 2.5386953712406015, 3.342707354323308], "isController": false}, {"data": ["Delete existing dragon", 3, 2, 66.66666666666667, 23.0, 17, 32, 20.0, 32.0, 32.0, 32.0, 6.012024048096192, 2.4130291833667337, 3.399376878757515], "isController": false}, {"data": ["Update to new dragon", 3, 0, 0.0, 38.0, 25, 51, 38.0, 51.0, 51.0, 51.0, 6.211180124223602, 4.464285714285714, 3.6636257763975157], "isController": false}, {"data": ["Create dragon with existing name", 3, 3, 100.0, 31.0, 19, 46, 28.0, 46.0, 46.0, 46.0, 6.172839506172839, 2.797067901234568, 5.678530092592593], "isController": false}, {"data": ["Create cave with too many treasures", 3, 3, 100.0, 32.333333333333336, 26, 42, 29.0, 42.0, 42.0, 42.0, 6.122448979591836, 2.7822066326530615, 5.614237882653061], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 7, 26.923076923076923, 14.583333333333334], "isController": false}, {"data": ["404", 2, 7.6923076923076925, 4.166666666666667], "isController": false}, {"data": ["Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 10, 38.46153846153846, 20.833333333333332], "isController": false}, {"data": ["409", 7, 26.923076923076923, 14.583333333333334], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 48, 26, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 10, "500", 7, "409", 7, "404", 2, "", ""], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["Create dragon with new name", 3, 2, "409", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import too many treasures", 3, 3, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["Import dragons with names intersections", 3, 3, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import unique dragons", 3, 2, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["Update cave to too many treasures", 3, 3, "500", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Import normal treasures", 3, 2, "Value in json path '$.status' expected to match regexp 'COMPLETED', but it did not match: 'FAILED'", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Update to existing dragon", 3, 3, "500", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Delete existing dragon", 3, 2, "404", 2, "", "", "", "", "", "", "", ""], "isController": false}, {"data": [], "isController": false}, {"data": ["Create dragon with existing name", 3, 3, "409", 3, "", "", "", "", "", "", "", ""], "isController": false}, {"data": ["Create cave with too many treasures", 3, 3, "409", 2, "500", 1, "", "", "", "", "", ""], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
