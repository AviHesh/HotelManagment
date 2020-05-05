package com;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.Appointment;


@WebServlet("/AppointmentsAPI")
public class AppointmentsAPI extends HttpServlet {
    private static final long serialVersionUID = 1L;


    Appointment appObj = new Appointment();

    public AppointmentsAPI() {

        super();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String output = appObj.insertAppointment(

                request.getParameter("userID"),
                request.getParameter("doctorID"),
                request.getParameter("appointmentDate"),
                request.getParameter("appointmentTime"));

        response.getWriter().write(output);
    }


    private static Map getParasMap(HttpServletRequest request) {

        Map<String, String> map = new HashMap<String, String>();
        try {
            Scanner scanner = new Scanner(request.getInputStream(), "UTF-8");

            String queryString = scanner.hasNext() ?
                    scanner.useDelimiter("\\A").next() : "";
            scanner.close();

            String[] params = queryString.split("&");
            for (String param : params) {

                String[] p = param.split("=");
                map.put(p[0], p[1]);
            }
        } catch (Exception e) {
        }
        return map;
    }


    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map paras = getParasMap(request);

        String output = appObj.updateAppointment(paras.get("hidAppointmentIDSave").toString(),
                paras.get("userID").toString(),
                paras.get("doctorID").toString(),
                paras.get("appointmentDate").toString(),
                paras.get("appointmentTime").toString());

        response.getWriter().write(output);

    }


    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map paras = getParasMap(request);

        String output = appObj.deleteAppointment(paras.get("appointmentID").toString());

        response.getWriter().write(output);
    }

}
