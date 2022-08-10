
$.ajax({
  url: "?get=use_json",
  context: document.body
}).done(function(data) {
    $('#table').remove();
});

function login()
{
    //Get the modal
    var modal = document.getElementById('login');
    var modal_login = document.getElementById('login_btn');
    const user = document.getElementById("uname").value;
    const pw = document.getElementById("psw").value;
    const remember = document.getElementById("remember").value;
    modal.style.display = "none";
    if(user != "" && pw != "" )
    {
		
		var url = "localhost:4000/?";
		const data = {'get':'admin', 'user':user, 'pw':pw, 'remember':remember};
        var params = '?'+encodeQueryData(data);
        params = encodeQueryData(data);
		/*
		var http = new XMLHttpRequest();
		http.open("GET", url+params, true);
		http.onreadystatechange = function()
		{
		    if(http.readyState == 4 && http.status == 200) {
		        alert(http.responseText);
		    }
		}
		http.send(null);
		*/
		//var params = "somevariable=somevalue&anothervariable=anothervalue";
		const fullurl = url+params;
		console.log(fullurl);
		let http = new XMLHttpRequest();
		//http.open('POST', fullurl, true);
		http.open('GET', fullurl, true);
		http.send(params);
		http.onload = function() {
		  if (http.status != 200) { // analyze HTTP status of the response
		    alert("Error "+http.status+ http.statusText);
		  } else { // show the result
		    alert("Done, got "+http.response.length+" bytes");
		  }
		};
		http.onreadystatechange = function()
		{
		    if(this.readyState == 4 && this.status == 200) {
		        alert(this.responseText);
		    }
		}
    }
}

function encodeQueryData(data) {
  const ret = [];
  for (let d in data)
  {
    ret.push(encodeURIComponent(d) + '=' + encodeURIComponent(data[d]));
    }
    return ret.join('&');
}
function getData()
{
  $.ajax({
    url: "?get=data",
    context: document.body
  }).done(function(data) {
      $('#h1').empty();
      $('#h1').append('<h1 style="font-size:50px;"><marquee>User view</marquee></h1></p>');
      $('#table').remove();
      let sizeOfData = data.length;
      // create table with data
      var table = $('<table>').addClass('table').addClass('table-striped');
      table.attr('id', 'table');
      // table.append('<tr>');
      // table.append('<th>ID</th>');
      // table.append('<th>FIRSTNAME</th>');
      // table.append('<th>LASTNAME</th>');
      // table.append('<th>STATE</th>');
      // table.append('<th>AGE</th>');
      // table.append('<th>PW</th>');
      table.append('<tr>');
      // table.append('<th>'+data[0].header_firstName+'</th>');
      // table.append('<th>'+data[0].header_lastName+'</th>');
      table.append('<th>FIRSTNAME</th>');
      table.append('<th>LASTNAME</th>');
      table.append('</tr>');
      for(let i=1; i<data.length; i++){
        table.append('<tr>');
        table.append('<th>'+data[i].firstName+'</th>');
        table.append('<th>'+data[i].lastName+'</th>');
        table.append('</tr>');
      }
      // $.each(data, function(key, val)
      // {
      //     table.append('<tr>');
      //     table.append('<td>'+val.id+'</td>');
      //     table.append('<td>'+val.firstname+'</td>');
      //     table.append('<td>'+val.lastname+'</td>');
      //     table.append('<td>'+val.state+'</td>');
      //     table.append('<td>'+val.age+'</td>');
      //     table.append('<td>'+val.pw+'</td>');
      //     table.append('</tr>');
      // });
      table.append('</table>');
      $('body').append('<p></p>');
      $('body').append(table);
      // $('#table').append('<p></p>');
      // $('#table').append(table);
      let text;
      if(sizeOfData == undefined)
      {
        text='';
      }else{
        text=' ('+sizeOfData+')';
      }
      // $("#input").html('Got data'+text).addClass('btn btn-success');
    });
}
function getWeather()
{
  $.ajax({
    url: "?get=weather",
    context: document.body
  }).done(function(data) {
    });
}
function getAllData()
{
  $.ajax({
    url: "?get=admin&user=admin&pw=secret",
    context: document.body
  }).done(function(data) {
      $('#table').remove();
      let sizeOfData = data.length;
      // create table with data
      $('#h1').empty();
      $('#h1').append('<h1 style="font-size:50px;"><marquee>Admin view</marquee></h1></p>');
      var table = $('<table>').addClass('table').addClass('table-striped');
      table.attr('id', 'table');
      // table.append('<tr>');
      // table.append('<th>ID</th>');
      // table.append('<th>FIRSTNAME</th>');
      // table.append('<th>LASTNAME</th>');
      // table.append('<th>STATE</th>');
      // table.append('<th>AGE</th>');
      // table.append('<th>PW</th>');
      // table.append('</tr>');
      // $.each(data, function(key, val)
      // {
      //     table.append('<tr>');
      //     table.append('<td>'+val.id+'</td>');
      //     table.append('<td>'+val.firstname+'</td>');
      //     table.append('<td>'+val.lastname+'</td>');
      //     table.append('<td>'+val.state+'</td>');
      //     table.append('<td>'+val.age+'</td>');
      //     table.append('<td>'+val.pw+'</td>');
      //     table.append('</tr>');
      // });
      /*
      table.append('<tr>');
      table.append('<th>'+data[0][0]+'</th>');
      table.append('<th>'+data[0][1]+'</th>');
      table.append('</tr>');
      for(let i=1; i<data.length; i++){
        table.append('<tr>');
        table.append('<th>'+data[i][0]+'</th>');
        table.append('<th>'+data[i][1]+'</th>');
        table.append('<th>'+data[i][2]+'</th>');
        table.append('<th>'+data[i][3]+'</th>');
        if(data[i][4] == 0){
          table.append('<th>no</th>');
        } else {
          table.append('<th>yes</th>');
        }
        */
        table.append('<tr>');
      table.append('<th>ID</th>');
      table.append('<th>FIRSTNAME</th>');
      table.append('<th>LASTNAME</th>');
      table.append('<th>PASSWORD</th>');
      table.append('<th>IS_ADMIN</th>');
      table.append('</tr>');
      for(let i=1; i<data.length; i++){
        table.append('<tr>');
        table.append('<th>'+data[i].id+'</th>');
        table.append('<th>'+data[i].firstName+'</th>');
        table.append('<th>'+data[i].lastName+'</th>');
        table.append('<th>'+data[i].password+'</th>');
        table.append('<th>'+data[i].isAdmin+'</th>');
        table.append('</tr>');
      }
        table.append('</tr>');
      table.append('</table>');
      $('body').append('<p></p>');
      $('body').append(table);
      // $('#table').append('<p></p>');
      // $('#table').append(table);
      let text;
      if(sizeOfData == undefined)
      {
        text='';
      }else{
        text=' ('+sizeOfData+')';
      }
      // $("#input").html('Got data'+text).addClass('btn btn-success');
    });
}
function insertData()
{
  $.ajax({
    url: "?get=insert",
    context: document.body
  }).done(function(data) {
    alert("Done");
  });
}
