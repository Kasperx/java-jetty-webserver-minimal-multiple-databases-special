
$(document).ready(function(){
  moment.locale('de');
//  $("#input_date").attr("placeholder", moment().format('DD.MM.YYYY'));
  $("#input_date").val(moment().format('DD.MM.YYYY'));
//  $("#input_until_time").attr("placeholder", moment().format('LT'));
  $("#input_until_time").val(moment().format('LT'));
//  $("#input_from_time").attr("placeholder", moment().format('LT'));
  $("#input_from_time").val(moment().format('LT'));
});

function getData()
{
  $.ajax({
    url: "api/?get",
    context: document.body
  }).done(function(data) {
      $('#table').remove();
      let sizeOfData = data.length;
      // create table with data
      var table = $('<table>').addClass('table');
      table.attr('id', 'table');
      table.append('<tr>');
      table.append('<th>ID</th>');
      table.append('<th>FIRSTNAME</th>');
      table.append('<th>LASTNAME</th>');
      table.append('<th>STATE</th>');
      table.append('<th>AGE</th>');
      table.append('<th>PW</th>');
      table.append('</tr>');
      $.each(data, function(key, val)
      {
          table.append('<tr>');
          table.append('<td>'+val.id+'</td>');
          table.append('<td>'+val.firstname+'</td>');
          table.append('<td>'+val.lastname+'</td>');
          table.append('<td>'+val.state+'</td>');
          table.append('<td>'+val.age+'</td>');
          table.append('<td>'+val.pw+'</td>');
          table.append('</tr>');
      });
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
      $("#input").html('Got data'+text).addClass('btn btn-success');
    });
}
function getTimes()
{
  $.ajax({
    url: "api/?opt=gettimes",
    context: document.body
  }).done(function(data) {
      $('#table').remove();
      let sizeOfData = data.length;
      // create table with data
      var table = $('<table>').addClass('table');
      table.attr('id', 'table');
      table.append('<tr>');
      table.append('<th>id</th>');
      table.append('<th>person id</th>');
      table.append('<th>date</th>');
      table.append('<th>from time</th>');
      table.append('<th>until time</th>');
      table.append('</tr>');
      $.each(data, function(key, val)
      {
          table.append('<tr>');
          table.append('<td>'+val.id+'</td>');
          table.append('<td>'+val.person_id+'</td>');
          table.append('<td>'+val.date+'</td>');
          table.append('<td>'+val.from_time+'</td>');
          table.append('<td>'+val.until_time+'</td>');
          table.append('</tr>');
      });
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
      $("#times").html('Got data'+text).addClass('btn btn-success');
    });
}
function createData()
{
  $.ajax({
    url: "api/?opt=insert",
    context: document.body
  }).done(function(data){
    // if(data != null){
    //   console.log(data);
    // }else{
    //   console.log(data);
    // }
  });
}
function createTimes()
{
  $.ajax({
    url: "api/?opt=createtimes",
    context: document.body
  }).done(function(data){
    // if(data != null){
    //   console.log(data);
    // }else{
    //   console.log(data);
    // }
  });
}
