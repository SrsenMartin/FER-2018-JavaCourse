$(function(){
	
	$.getJSON("rest/data/tags", listTags);
	
});

function listTags(tags){
	$.each(tags, function(i, tag){
		if(i%5 == 0 && i != 0){
			$("#tagArea").append("<br>");
		}
		
		$("#tagArea").append("<button class='tag' id=" + tag + " onclick='clickFunction(this.id)'>" + tag + "</button>");
	});
}

function clickFunction(selectedTag){
	$("#thumbnailsArea").html("");
	$("#imageArea").html("");
	
	$.getJSON("rest/data/urls/" + selectedTag, addThumbnails);
}

function addThumbnails(urls){	
	$.each(urls, function(i, url){
		if(i%5 == 0 && i != 0){
			$("#thumbnailsArea").append("<br>");
		}	
	
		$("#thumbnailsArea").append("<img class='thumbnail' id=" + url + 
				" src='thumbnails?url=" + url + "' onclick='imageClick(this.id)'/>");
	});
}

function imageClick(url){
	$("#imageArea").html("");
	
	$.getJSON("rest/data/imageData/" + url, showImageData);
}

function showImageData(image){
	$("#imageArea").append("<img src='getImage?url=" + image.path + "'/>");
	$("#imageArea").append("<h1 class='data'>" + image.name + "</h1>");
	$("#imageArea").append("<h3 class='data'>[" + image.tags + "]</h3>");
	
	setTimeout(function(){
		$(window).scrollTop($('#imageArea').offset().top);
		}, 300);
}