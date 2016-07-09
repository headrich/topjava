/**
 * Created by Montana on 05.07.2016.
 */
var controllerUrl = "top/meals";

$(".editRow").click(function(){
    $("#editRowModal").modal();

});

$(".removeRow").click(function(){
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
