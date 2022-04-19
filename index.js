
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
		var http = new XMLHttpRequest();
		http.open('POST', fullurl, true);
		http.onreadystatechange = function()
		{
		    if(this.readyState == 4 && this.status == 200) {
		        alert(this.responseText);
		    }
		}
		http.send(params);
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