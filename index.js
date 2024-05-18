
  function insertDataTest()
  {
    let table = $('#table');
    table.append('<tr id="tableinput">'
    +'<td>'
    +'<input placeholder="Input name">'
    +'</td>'
    +'<td>'
    +'<input placeholder="Input action">'
    +'</td>'
    +'<td>'
    +'<input placeholder="Input action name">'
    +'</td>'
    +'<td>'
    +'<input type="button" id="btn_send" class="btn btn-success" onclick="javascript:sendDataToSystem()" value="send"></input>'
    +'</td>'
    );
    table.remove('#tableinput');
  }

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
      url: "?get=data&format=json",
      context: document.body
    }).done(function(data) {
      // Manage header
      $('#h1').empty();
      $('#h1').append('<h1 style="font-size:50px;"><marquee>User view</marquee></h1></p>');
      let table = $('#table');
      table.remove();
      table = $('<table>');
      table.addClass("table");
      table.addClass("table-striped");
      table.addClass("table-hover");
      table.attr('id', 'table');
      table.append('<thead class="thead-dark">'
        +'<tr>'
        +'<th style="max-width:200px;">#</th>'
        +'<th style="max-width:200px;">Vorname</th>'
        +'<th style="max-width:200px;">Nachname</th>'
        +'<th style="max-width:200px;">Activity</th>'
        +'<th style="max-width:200px;">Activity name</th>'
        +'<th></th>'
        +'</tr>'
        +'</thead>'
      );
      table.append('<tr>');
      // Does not work with separated cmds, only inline like above :/
      table.append('<tbody>');
      let tabledata = '';
      for(let i=0; i<data.length; i++){
        // Does not work with separated cmds, only inline like above :/
        tabledata += '<tr>';
        tabledata += '<td>'+data[i].position+'</td>';
        tabledata += '<td>'+data[i].vorname+'</td>';
        tabledata += '<td>'+data[i].nachname+'</td>';
        tabledata += '<td>'+data[i].activity+'</td>';
        tabledata += '<td>'+data[i].activity_name+'</td>';
        tabledata += '</tr>';
      }
      table.append(tabledata);

      table.append('<td><input style="max-width:200px;" placeholder="Input position" id="input_position"></input></td>');
      table.append('<td><input style="max-width:200px;" placeholder="Input vorname" id="input_vorname"></input></td>');
      table.append('<td><input style="max-width:200px;" placeholder="Input nachname" id="input_nachname"></input></td>');
      table.append('<td><input style="max-width:200px;" placeholder="Input activity" id="input_action"></input></td>');
      table.append('<td><input style="max-width:200px;" placeholder="Input activity name" id="input_action_name"></input></td>');
      table.append(
        '<td>'
        +'<input type="button" id="btn_send" class="btn btn-success" onclick="javascript:sendDataToSystem()" value="send"></input>'
        +'</td>'
      );

      table.append('</tbody>');
      table.append('</table>');
      $('body').append('<p></p>');
      $('body').append(table);
    });
  }
  function insertData()
  {
    $.ajax({
      url: "?get=insert&format=json",
      context: document.body
    }).done(function(data) {
      alert("Inserted new data.");
    }).fail(function ( jqXHR, textStatus, errorThrown ) {
      alert('Failed');
    });;
  }
  function sendDataToSystem()
  {
    let input_name_position_value = $('#input_position').val();
    let input_name_value = $('#input_name').val();
    let input_action_value = $('#input_action').val();
    let input_action_name_value = $('#input_action_name').val();
    if(input_name_position_value && input_name_value == "" && input_action_value == "" && input_action_name_value == "")
    {}
    else {
      $.ajax({
        url: "?get=add_user"
        +"&format=json"
        +"&position="+input_name_position_value
        +"&vorname="+input_name_value
        +"&nachname="+input_name_value
        +"&action="+input_action_value
        +"&action_name="+input_action_name_value,
        context: document.body
      }).done(function(data) {
        alert("Inserted "
        +input_name_position_value+" ",
        +input_name_value+" ",
        +input_action_value+" ",
        +input_action_name_value,
        +"' to system.");
        getData();
      }).fail(function ( jqXHR, textStatus, errorThrown ) {
        console.log(textStatus);
        alert('Failed');
      });
    }
}
$(function() {
  $('#table').on('editable-save.bs.table', function(e, field, row, oldValue){
      console.log("1 "+ field);
      console.log("2 "+ row[field]);
      console.log("3 "+ row.lot);
      console.log("4 "+ oldValue);
  });    
});
