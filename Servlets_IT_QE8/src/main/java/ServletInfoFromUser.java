import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/formServlet1")
public class ServletInfoFromUser extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Properties getConnectionData() {
        
    	Properties props = new Properties();

        String fileName = "/home/sidhartha/eclipse-workspace/Servlets_IT_QE8/src/main/java/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(ServletQuery.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        Properties props = getConnectionData();
        
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException cnfe) {
        	System.out.println(cnfe);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        String templateForEmployee = "INSERT INTO Employee VALUES(?, ?, ?, ?, ?, ?, ?)";
        String templateForDepartment = "INSERT INTO Department VALUES(?, ?, ?)";
        
        int departmentId;
        String departmentName;
        String departmentLocation;
        int employeeId;
        String employeeName;
        String jobTitle;
        String dateOfBirthString;
        String joiningDateString;
        double salary;
        
        PreparedStatement inserter;
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(url, user, password);) {
        	
        	response.setContentType("text/html");
        	
            String whichTable = request.getParameter("whichTable");

            if ("Department".equals(whichTable)) { // if Department option is selected
                departmentId = Integer.parseInt(request.getParameter("departmentId1"));
                departmentName = request.getParameter("departmentName");
                departmentLocation = request.getParameter("departmentLocation");

                inserter = conn.prepareStatement(templateForDepartment);
                inserter.setInt(1, departmentId);
                inserter.setString(2, departmentName);
                inserter.setString(3, departmentLocation);

                inserter.executeUpdate();
            } 
            else if ("Employee".equals(whichTable)) { // if Employee option is selected
                employeeId = Integer.parseInt(request.getParameter("employeeId"));
                employeeName = request.getParameter("employeeName");
                jobTitle = request.getParameter("jobTitle");
                dateOfBirthString = request.getParameter("dateOfBirth");
                joiningDateString = request.getParameter("joiningDate");
                salary = Double.parseDouble(request.getParameter("salary"));
                departmentId = Integer.parseInt(request.getParameter("departmentId2"));

                java.sql.Date dateOfBirth = java.sql.Date.valueOf(dateOfBirthString);
                java.sql.Date joiningDate = java.sql.Date.valueOf(joiningDateString);

                inserter = conn.prepareStatement(templateForEmployee);
                inserter.setInt(1, employeeId);
                inserter.setString(2, employeeName);
                inserter.setString(3, jobTitle);
                inserter.setDate(4, dateOfBirth);
                inserter.setDate(5, joiningDate);
                inserter.setDouble(6, salary);
                inserter.setInt(7, departmentId);

                inserter.executeUpdate();
            }
            
            out.println("<html><body>");
            out.println("<h1>Data Inserted Successfully</h1>");
            out.println("</body></html>");

        } catch (SQLException sqle) {
            Logger lgr = Logger.getLogger(ServletQuery.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
            out.println("<html><body><h1>" + sqle.getMessage() + "</h1></body></html>");
        }
    }
}
