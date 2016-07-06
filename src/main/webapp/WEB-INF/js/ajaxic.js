/**
 * Created by Montana on 05.07.2016.
 */
var controllerUrl = "top/meals";

$(".editRow").onclick(function(){
    $("#editRowModal").modal();

});

$(".removeRow").onclick(function(){
    $("#editRowModal").modal();

});

function updateTable(){
    $.ajax({
        type:"POST",
        url:controllerUrl,
        success:updateTableData()
    });
}
function updateTableData() {
    //$(".table")
}
