/*$("#goButton").click(function() {
	$.ajax({


		type: "GET",
		url: "http://localhost:9000/listProjects",

		success: function () {
			alert("Value: " + $("#searchTerm").val())}
	});
});*/


function search(length){

if(length>=10){
 alert("You can only search upto 10 results")
 return false;
 }
	//alert("i did reach here ");
		//console.log($("#searchTerm").val())
let keyword =$("#searchTerm").val();

if (!keyword){
	alert("Please enter some search term to proceed")
	return false;
}

	$.ajax({


		type: "get",
		url: "/"+ $("#searchTerm").val(),

		success: function () {
			//alert("Control was here actually");
			window.location.replace("/listResults");
			// this.url=url;
			//$("testData").append("<p>HelloHere</p>");
			},
		error : function(url){
			console.log(url);
		}
	});
}

function displaySkillResults(id){
    console.log("Hi there")
	console.log(id)

	
	$.ajax({


		type: "get",
		url: "/skills/"+ id,

		success: function () {
			//alert("Fetching skills from backend");
			window.location.replace("/forSkills");
			// this.url=url;
			//$("testData").append("<p>HelloHere</p>");
		},
		error : function(url){
			alert("Failure ....");
			console.log(url);
		}
	});
}