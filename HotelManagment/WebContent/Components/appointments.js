$(document).ready(function()
{

    $("#alertSuccess").hide();
    $("#alertError").hide();
});

// SAVE ============================================
$(document).on("click", "#btnSave", function(event)
{
// Clear alerts---------------------
    $("#alertSuccess").text("");
    $("#alertSuccess").hide();
    $("#alertError").text("");
    $("#alertError").hide();

// Form validation-------------------
    var status = validateAppointmentForm();
    if (status != true)
    {
        $("#alertError").text(status);
        $("#alertError").show();
        return;
    }

// If valid------------------------
   // $("#formAppointment").submit();

var type = ($("#hidAppointmentIDSave").val() == "") ? "POST" : "PUT";

$.ajax(
    {
        url : "AppointmentsAPI",
        type : type,
        data : $("#formAppointment").serialize(),
        dataType : "text",
        complete : function(response, status)
        {
            onAppointmentSaveComplete(response.responseText, status);
        }
    });
});


function onAppointmentSaveComplete(response, status) {

    if (status == "success") {

        var resultSet = JSON.parse(response);

        if (resultSet.status.trim() == "success") {

            $("#alertSuccess").text("Successfully saved.");
            $("#alertSuccess").show();

            $("#divAppointmentsGrid").html(resultSet.data);

        } else if (resultSet.status.trim() == "error") {

            $("#alertError").text(resultSet.data);
            $("#alertError").show();

        }
    } else if (status == "error") {

        $("#alertError").text("Error while saving.");
        $("#alertError").show();

    } else {

        $("#alertError").text("Unknown error while saving..");
        $("#alertError").show();

    }

    $("#hidAppointmentIDSave").val("");
    $("#formAppointment")[0].reset();


}

//UPDATE==========================================
$(document).on("click", ".btnUpdate", function(event)
{
    $("#hidAppointmentIDSave").val($(this).closest("tr").find('#hidAppointmentIDUpdate').val());
    $("#userID").val($(this).closest("tr").find('td:eq(0)').text());
    $("#doctorID").val($(this).closest("tr").find('td:eq(1)').text());
    $("#appointmentDate").val($(this).closest("tr").find('td:eq(2)').text());
    $("#appointmentTime").val($(this).closest("tr").find('td:eq(3)').text());
});

//Delete
$(document).on("click", ".btnRemove", function(event)
{
    $.ajax(
        {
            url : "AppointmentsAPI",
            type : "DELETE",
            data : "appointmentID=" + $(this).data("appointmentid"),
            dataType : "text",
            complete : function(response, status)
            {
                onAppointmentDeleteComplete(response.responseText, status);
            }
        });
});

function onAppointmentDeleteComplete(response, status) {

    if (status == "success") {

        var resultSet = JSON.parse(response);

        if (resultSet.status.trim() == "success") {

            $("#alertSuccess").text("Successfully deleted.");
            $("#alertSuccess").show();

            $("#divAppointmentsGrid").html(resultSet.data);

        } else if (resultSet.status.trim() == "error") {

            $("#alertError").text(resultSet.data);
            $("#alertError").show();

        }

    } else if (status == "error") {

        $("#alertError").text("Error while deleting.");
        $("#alertError").show();

    } else {

        $("#alertError").text("Unknown error while deleting..");
        $("#alertError").show();

    }

}



// CLIENTMODEL=========================================================================
function validateAppointmentForm()
{
// USER ID
    if ($("#userID").val().trim() == "")
    {
        return "Insert User ID.";
    }

// DOCTOR ID
    if ($("#doctorID").val().trim() == "")
    {
        return "Insert Doctor ID.";
    }

// APPOINTMENT DATE-------------------------------
    if ($("#appointmentDate").val().trim() == "")
    {
        return "Insert Appointment Date.";
    }

// APPOINTMENT TIME------------------------
    if ($("#appointmentTime").val().trim() == "")
    {
        return "Insert Appointment Time.";
    }
    return true;
}
