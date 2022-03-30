
function login()
{
    //Get the modal
    var modal = document.getElementById('login');
    var modal_login = document.getElementById('login_btn');
    const user = document.getElementById("uname").value;
    const pw = document.getElementById("psw").value;
    modal.style.display = "none";
    if(user != "" && pw != "" )
    {
        // alert("juhu");
        // const user = document.getElementById("uname").innerHTML;
        // const pw = document.getElementById("psw").innerHTML;
        const data = {'get':'admin', 'user':'user', 'pw':'pw'};
        const result = encodeQueryData(data);
        let xhr = new XMLHttpRequest();
        xhr.open('GET', result, true);
        xhr.send();
        xhr.onreadystatechange = processRequest;
        function processRequest(e) {
            if (xhr.readyState == 4 && xhr.status == 200) {
                console.log("juhu");
            }
        }

        // IPxhr.open('GET', "?get=admin&user="+user+"&password="+pw, true);
        // IPxhr.send();
        // IPxhr.onreadystatechange = processRequest;
        // function processRequest(e)
        // {
        // 	if (IPxhr.readyState == 4 && IPxhr.status == 200)
        // 	{
        // 		// let response = JSON.parse(IPxhr.responseText);
        // 		// document.querySelector("#ipAddress").innerHTML = response.ip;
        // 		// alert("yea");
        //         alert("got it");
        // 	}
        // }
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