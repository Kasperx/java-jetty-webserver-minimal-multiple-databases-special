
// $(document).ready(function() {

  $.ajax({
    url: "?get=use_json",
    context: document.body
  }).done(function(data) {
    // if(data){
      $('#table').remove();
    // }
  });

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
      // Manage header
      $('#h1').empty();
      $('#h1').append('<h1 style="font-size:50px;"><marquee>User view</marquee></h1></p>');
      //////////////////////////////////////////////////////
      // Manage table for data input
      //////////////////////////////////////////////////////
      // $('#table_input').remove();
      // let table_input = $('<table>');
      // table_input.addClass("table");
      // // table.addClass("table-striped");
      // table_input.addClass("table-hover");
      // table_input.attr('id', 'table_input');
      // table_input.append('<tr>');
      // // table_input.append('<td style="max-width:70px;"><input placeholder="input s" id="input_name"></td>');
      // table_input.append('<td style="max-width:70px;"><input placeholder="Input name" id="input_name"></td>');
      // // table_input.append('<td style="max-width:70px;"><input placeholder="input v" id="input_action"></td>');
      // table_input.append('<td style="max-width:70px;"><input placeholder="Input activity" id="input_action"></td>');
      // // table_input.append('<td style="max-width:70px;"><input placeholder="input o" id="input_action_name"></td>');
      // table_input.append('<td style="max-width:70px;"><input placeholder="Input activity name" id="input_action_name"></td>');
      // // let button = $('#button');
      // // button.attr('id', 'btn_send');
      // // button.on('click', function(sendDataToSystem){});
      // // button.addClass("btn");
      // // button.addClass("btn-success");
      // // button.append('send');
      // // table_input.append('<td>');
      // // table_input.append('<td><button id="btn_send" class="btn btn-success" onclick="javascript:sendDataToSystem()">send</button></td>');
      // table_input.append(
      //   '<td>'
      //   +'<input type="button" id="btn_send" class="btn btn-success" onclick="javascript:sendDataToSystem()" value="send"></input>'
      //   +'</td>'
      // );
      // // table_input.append(button);
      // table_input.append('</td>');
      // table_input.append('</tr>');
      // table_input.append('</table>');
      // $('body').append('<p></p>');
      // $('body').append(table_input);
      //////////////////////////////////////////////////////
      // table input
      //////////////////////////////////////////////////////
      // Manage table for data
      // $('#table').empty();
      // let sizeOfData = data.length;
      // create table with data
      let table = $('#table');
      table.remove();
      // table = $('<table>');
      table = $('<table>');
      table.addClass("table");
      table.addClass("table-striped");
      table.addClass("table-hover");
      // table.append('<table class="table table-hover table-hover">');
      // table.append('<thead>');
      table.attr('id', 'table');
      // table.append('<tr>');
      // table.append('<th>ID</th>');
      // table.append('<th>FIRSTNAME</th>');
      // table.append('<th>LASTNAME</th>');
      // table.append('<th>STATE</th>');
      // table.append('<th>AGE</th>');
      // table.append('<th>PW</th>');
      // table.append('<tr>');
      // table.append('<th>'+data[0].header_firstName+'</th>');
      // table.append('<th>'+data[0].header_lastName+'</th>');
      // table.append('<th class="thead-dark">n</th>');
      // table.append('<th class="thead-dark">s</th>');
      // table.append('<th class="thead-dark">v</th>');
      // table.append('<th class="thead-dark">o</th>');
      table.append('<thead class="thead-dark">'
        +'<tr>'
        +'<th style="max-width:200px;">Position</th>'
        +'<th style="max-width:200px;">Name</th>'
        +'<th style="max-width:200px;">Activity</th>'
        +'<th style="max-width:200px;">Activity name</th>'
        +'<th></th>'
        +'</tr>'
        +'</thead>'
      );
      // table.addClass("thead-dark");
      table.append('<tr>');
      // Does not work with separated cmds, only inline like above :/
      // table.append('<th class="thead-dark">Position</th>');
      // table.append('<th class="thead-dark">Name</th>');
      // table.append('<th class="thead-dark">Activity</th>');
      // table.append('<th class="thead-dark">Activity name</th>');
      // table.append('<th>Position</th>');
      // table.append('<th>Name</th>');
      // table.append('<th>Activity</th>');
      // table.append('<th>Activity name</th>');
      // table.append('</tr>');
      // table.append('</thead>');
      table.append('<tbody>');
      // table.append('</thead>');
      // table.append('<tbody>');
      let tabledata = '';
      for(let i=0; i<data.length; i++){
        // Does not work with separated cmds, only inline like above :/
        // table.append('<tr>');
        // table.append('<td>'+data[i].n+'</td>');
        // table.append('<td>'+data[i].o+'</td>');
        // table.append('<td>'+data[i].s+'</td>');
        // table.append('<td>'+data[i].v+'</td>');
        // table.append('</tr>');
        tabledata += '<tr>';
        tabledata += '<td>'+data[i].n+'</td>';
        tabledata += '<td>'+data[i].o+'</td>';
        tabledata += '<td>'+data[i].s+'</td>';
        tabledata += '<td>'+data[i].v+'</td>';
        tabledata += '</tr>';
      }
      table.append(tabledata);

      // table_input.append('<td style="max-width:70px;"><input placeholder="input s" id="input_name"></td>');
      table.append('<td></td>');
      // table.append('<td style="max-width:70px;"><input placeholder="Input name" id="input_name"></input></td>');
      // table.append('<td style="max-width:70px;"><input placeholder="Input activity" id="input_action"></input></td>');
      // table.append('<td style="max-width:70px;"><input placeholder="Input activity name" id="input_action_name"></input></td>');
      table.append('<td><input placeholder="Input name" id="input_name"></input></td>');
      table.append('<td><input placeholder="Input activity" id="input_action"></input></td>');
      table.append('<td><input placeholder="Input activity name" id="input_action_name"></input></td>');
      table.append(
        '<td>'
        +'<input type="button" id="btn_send" class="btn btn-success" onclick="javascript:sendDataToSystem()" value="send"></input>'
        +'</td>'
      );

      table.append('</tbody>');
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
      // table.append('</tbody>');
      table.append('</table>');
      $('body').append('<p></p>');
      $('body').append(table);
      // $('#table').append('<p></p>');
      // $('#table').append(table);
      // let text;
      // if(sizeOfData == undefined)
      // {
      //   text='';
      // }else{
      //   text=' ('+sizeOfData+')';
      // }
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
  function insertData()
  {
    $.ajax({
      url: "?get=insert",
      context: document.body
    }).done(function(data) {
      alert("Inserted new data.");
    }).fail(function ( jqXHR, textStatus, errorThrown ) {
      // console.log(jqXHR);
      // console.log(textStatus);
      // console.log(errorThrown);
      alert('Failed');
    });;
  }
  function sendDataToSystem()
  {
    // let input_position_value = $('#input_position').val();
    let input_name_value = $('#input_name').val();
    let input_action_value = $('#input_action').val();
    let input_action_name_value = $('#input_action_name').val();
    if(input_name_value == "" && input_action_value == "" && input_action_name_value == "")
    {}
    else {
      $.ajax({
        url: "?get=adduser"
        // +"&position="+input_position_value
        +"&name="+input_name_value
        +"&action="+input_action_value
        +"&action_name="+input_action_name_value,
        context: document.body
      }).done(function(data) {
        alert("Inserted "
        // +input_position_value+" "
        +input_name_value+" ",
        +input_action_value+" ",
        +input_action_name_value,
        +"' to system.");
        // call new data after insert
      //   $.ajax({
      //     url: "?get=data",
      //     context: document.body
      //   }).done(function() {
          
      //   });
      // }).done(function() {
      //   alert('OK');
        getData();
      }).fail(function ( jqXHR, textStatus, errorThrown ) {
        // console.log(jqXHR);
        // console.log(textStatus);
        // console.log(errorThrown);
        alert('Failed');
      });
      // insertDataTest();
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
