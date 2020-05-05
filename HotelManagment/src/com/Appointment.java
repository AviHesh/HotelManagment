package com;

import java.sql.*;

public class Appointment {
    private Connection connect() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/apphealth", "root", "");

        } catch (Exception e) {

            e.printStackTrace();
        }
        return con;
    }

    public String readAppointments(){

        String output = "";

        try
        {
            Connection con = connect();
            if (con == null)
            {
                return "Error while connecting to the database for reading.";
            }

            // Prepare the html table to be displayed
            output = "<table border='3'> "
                    + " <tr><th>Person UserID</th >"
                    + "<th >  Doctor ID  </th > " + "<th>  Appointment Date  </th>"
                    + "<th>  Appointment Time </th>"
                    + "<th> Update </th ><th> Remove</th></tr> ";

            String query = "select * from appointment";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // iterate through the rows in the result set
            while (rs.next())
            {
                String appointmentID = Integer.toString(rs.getInt("appointmentID"));
                String userID = rs.getString("userID");
                String doctorID = rs.getString("doctorID");
                String appointmentDate = rs.getString("appointmentDate");
                String appointmentTime = rs.getString("appointmentTime");

                // Add into the html table
                output += "<tr><td><input id='hidAppointmentIDUpdate'"
                        + "name='hidAppointmentIDUpdate'"
                        + "type='hidden' value='"
                        + appointmentID + "'>"
                        + userID + "</td>";
                output += "<td>" + doctorID + "</td>";
                output += "<td>" + appointmentDate + "</td>";
                output += "<td>" + appointmentTime + "</td>";

                // buttons
                output += "<td><input name='btnUpdate' type='button' value='Update'class='btnUpdate btn btn-secondary'></td>"
                        + "<td><input name='btnRemove'type='button' value='Remove'class='btnRemove btn btn-danger' data-appointmentid='"
                        + appointmentID + "'>" + "</td></tr>";
            }

            con.close();

            // Complete the html table

            output += "</table>";

        }

        catch (Exception e)
        {
            output = "Error while reading the appointments.";
            System.err.println(e.getMessage());
        }

        return output;

    }

    public String insertAppointment(String userID, String doctorID,
                                    String appointmentDate, String appointmentTime){

        String output = "";

        try
        {
            Connection con = connect();

            if (con == null)
            {
                return "Error while connecting to the database for inserting.";
            }

            // create a prepared statement
            String query = " insert into appointment(appointmentID,userID,doctorID,appointmentDate,appointmentTime)"+ " values (?, ?, ?, ?, ?)";
            PreparedStatement preparedStmt = con.prepareStatement(query);

            // binding values
            preparedStmt.setInt(1, 0);
            preparedStmt.setString(2, userID);
            preparedStmt.setString(3, doctorID);
            preparedStmt.setString(4, appointmentDate);
            preparedStmt.setString(5, appointmentTime);

            // execute the statement
            preparedStmt.execute();
            con.close();

            String newAppointments = readAppointments();
            output = "{\"status\":\"success\", \"data\": \"" + newAppointments + "\"}";

        }catch (Exception e){

            output = "{\"status\":\"error\", \"data\": \"Error while inserting the appointment.\"}";
            System.err.println(e.getMessage());
        }

        return output;
    }

    public String updateAppointment(String ID, String userID, String doctorID,
                                    String appointmentDate, String appointmentTime)
    {
        String output = "";

        try
        {
            Connection con = connect();

            if (con == null)
            {
                return "Error while connecting to the database for updating.";
            }

            // create a prepared statement
            String query = "UPDATE appointment SET userID=?,doctorID=?,appointmentDate=?,appointmentTime=? WHERE appointmentID=?";

            PreparedStatement preparedStmt = con.prepareStatement(query);

            // binding values
            preparedStmt.setString(1, userID);
            preparedStmt.setString(2, doctorID);
            preparedStmt.setString(3, appointmentDate);
            preparedStmt.setString(4, appointmentTime);
            preparedStmt.setInt(5, Integer.parseInt(ID));

            // execute the statement
            preparedStmt.execute();
            con.close();

            String newAppointments = readAppointments();
            output = "{\"status\":\"success\", \"data\": \"" + newAppointments + "\"}";

        }catch (Exception e){

            output = "{\"status\":\"error\", \"data\":\"Error while updating the appointment.\"}";
            System.err.println(e.getMessage());
        }
        return output;
    }

    public String deleteAppointment(String appointmentID)
    {
        String output = "";

        try
        {

            Connection con = connect();

            if (con == null){

                return "Error while connecting to the database for deleting.";
            }

            // create a prepared statement
            String query = "delete from appointment where appointmentID=?";
            PreparedStatement preparedStmt = con.prepareStatement(query);

            // binding values
            preparedStmt.setInt(1, Integer.parseInt(appointmentID));

            // execute the statement
            preparedStmt.execute();
            con.close();

            String newAppointments = readAppointments();
            output = "{\"status\":\"success\", \"data\": \"" + newAppointments + "\"}";

        }catch (Exception e){

            output = "{\"status\":\"error\", \"data\":\"Error while deleting the appointment.\"}";
            System.err.println(e.getMessage());
        }

        return output;
    }

}
