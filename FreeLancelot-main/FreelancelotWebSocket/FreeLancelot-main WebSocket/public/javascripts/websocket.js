var $projectsResult = $("#projectsResult");
 var $goButton = $("#goButton");
 var $searchTerm = $("#searchTerm");
 var connection = new WebSocket("ws://localhost:9000/websocket");
var globalWordStatisticsUrl = "@routes.WebSocketController.globalWordStatistics("").url";
var averageFleschIndexUrl = "@routes.WebSocketController.averageFleschIndex("").url";
var singleReadabilityScoreUrl = "@routes.WebSocketController.singleReadabilityScore("").url";
var individualWordStatisticsUrl = "@routes.WebSocketController.individualWordStatistics("","").url";
var searchCount = 0;
$goButton.prop("disabled", true);

var goButton = function () {
    var text = $searchTerm.val();
    if(searchCount >=10){
        alert("You can only search upto 10 results");
        return;
    }
    searchCount++;
    console.log("text"+text);
    if(text == ""){
        alert("Please enter some search term to proceed");
        return;
    }
     var search = {
            searchKey : ''
        };
        search.searchKey = text;
        connection.send(JSON.stringify(search));
        $searchTerm.val("");
};

connection.onopen = function () {
    console.log("Connection open");
    $goButton.prop("disabled", false);
    $goButton.on('click', goButton);
    $searchTerm.keypress(function (event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13') {
            goButton();
        }
    });
};

connection.onerror = function (error) {
    console.log('WebSocket Error ', error);
};

connection.onmessage = function (event) {
    var listOfResult = JSON.parse(event.data);
    // iterate array of results
    for (let i = 0; i < Object.keys(listOfResult).length; i++) {
    var globalPreviewDescription ="";
    //console.log("globalPreviewDescription1:"+globalPreviewDescription);
    var keywordId = listOfResult[i]["keyword"].split(' ').join('-');
        //iterate 250 projects
        for(let j = 0; j < Object.keys(listOfResult[i]["projectInfo"]).length; j++){
            globalPreviewDescription = globalPreviewDescription +listOfResult[i]["projectInfo"][j]["preview_description"];
        }
        //console.log("globalPreviewDescription2:"+globalPreviewDescription);
        var toAppend = "";
        //iterate first 10 projects
        toAppend =toAppend+"<div id=\'" + keywordId + "\' class=\'row justify-content-md-center \'><div class=\'col-md-8\'>"
        var keyword = listOfResult[i]['keyword'];
       //console.log("@routes.WebSocketController.globalWordStatistics(_)"+{keyword});
        toAppend =toAppend+"<p><b>Search terms : "+keyword+" </b> &nbsp;<a href="+globalWordStatisticsUrl+ keyword + " target=\'_blank\'>Global Stats</a>"
        toAppend =toAppend+"<b>&nbsp;</b><a href="+averageFleschIndexUrl+keyword+" target=\'_blank\'> Global Readability</a>  </p>"

        for(let j = 0; j < Object.keys(listOfResult[i]["firstTenProjectInfo"]).length; j++){
            var ownerId = listOfResult[i]['firstTenProjectInfo'][j]['owner_id'];
            toAppend =toAppend+"<p><b>Owner ID: </b> <a href=\'\' target=\'_blank\'> "+ownerId+" </a> &nbsp; </b>"+listOfResult[i]['firstTenProjectInfo'][j]['time_submitted']+"</b>&nbsp;<b>Title: </b>"+listOfResult[i]['firstTenProjectInfo'][j]['title']+" </b>&nbsp;<b>Type: </b>"+listOfResult[i]['firstTenProjectInfo'][j]['type']+" </b>&nbsp;<b>Skills :</b>";
            for(let k = 0; k < Object.keys(listOfResult[i]["firstTenProjectInfo"][j]["jobs"]).length && k<5; k++){
                var skillName = listOfResult[i]["firstTenProjectInfo"][j]["jobs"][k]["name"];
                var skillId = listOfResult[i]["firstTenProjectInfo"][j]["jobs"][k]["id"];
                toAppend =toAppend+"<a href=\'\' onclick=displaySkillResults("+skillId+")>"+skillName+"</a>&nbsp;";
            }
            var previewDescription = listOfResult[i]['firstTenProjectInfo'][j]['preview_description'];
            toAppend =toAppend+"&nbsp;| <a href="+individualWordStatisticsUrl.slice(0, -1)+keywordId+"/"+encodeURI(previewDescription.trim())+" target=\'_blank\'> Stats</a>"
            toAppend =toAppend+"&nbsp;| <a href="+singleReadabilityScoreUrl+encodeURI(previewDescription.trim())+" target=\'_blank\'> Readability</a></p>"
        }


        toAppend+="</br></div></div>"
        //toAppend.replace(/keywordVar/g,keyword);
        //console.log(toAppend);
        if (document.getElementById(keywordId) == null) {
            $projectsResult.prepend($(toAppend));
        }else{
            document.getElementById(keywordId).innerHTML = toAppend;
        }
    }


}
