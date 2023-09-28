import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/formServlet2")
public class ServletQuery extends HttpServlet {

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

        String template = "SELECT * FROM Employee WHERE (joiningDate > ?) AND ( ((YEAR(NOW()) - YEAR(dateOfBirth) = ?) AND ( (MONTH(NOW()) > MONTH(dateOfBirth)) OR ( (MONTH(NOW()) = MONTH(dateOfBirth)) AND (DATE(NOW()) >= DATE(dateOfBirth)) ) )) OR (YEAR(NOW()) - YEAR(dateOfBirth) > ?) )";
        
        Date joiningDate;
        int age;
        
        PreparedStatement inserter;
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(url, user, password);) {
        	
        	response.setContentType("text/html");
        	
        	String joiningDateString = request.getParameter("joiningDate");
        	joiningDate = java.sql.Date.valueOf(joiningDateString);
        	age = Integer.parseInt(request.getParameter("age"));
        	
        	inserter = conn.prepareStatement(template);
        	
        	inserter.setDate(1, joiningDate);
        	inserter.setInt(2, age);
        	inserter.setInt(3, age);
        	
        	ResultSet rs = inserter.executeQuery();
        	
        	out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Query Database</title>");
            out.println("<style>");
            out.println("body, input {");
            out.println("    font-size: 20px;");
            out.println("    text-align: center;");
            out.println("}");
            out.println("#input1, #input2 {");
            out.println("    display: none;");
            out.println("}");
            out.println(".error{");
            out.println("    font-size: 18px;");
            out.println("    color: red;");
            out.println("}");
            out.println("table {");
            out.println("    border-style: double;");
            out.println("    text-align: center;");
            out.println("}");
            out.println("th, td {");
            out.println("    border-style: solid;");
            out.println("}");
            out.println("</style>");
            out.println("</head>");
            
            out.println("<body>");
            out.println("<h1>Query Database</h1>");
            
            out.println("<form name=\"queryForm\" id=\"queryForm\" action=\"formServlet2\" method=\"post\">");
            out.println("<h2>Enter the following:</h2>");
            out.println("<h4>To get the details of all the employees who joined after a 'certain date' and are over 'n' years age...</h4><br>");
            out.println("<label for=\"joiningDate\">Joining Date: </label>");
            out.println("<input type=\"date\" id=\"joiningDate\" name=\"joiningDate\" min=\"1950-12-31\">");
            out.println("<span id=\"joiningDateError\" class=\"error\"> </span><br><br>");
            out.println("<label for=\"age\">Age(in years): </label>");
            out.println("<input type=\"number\" id=\"age\" name=\"age\" min=\"18\" max=\"100\">");
            out.println("<span id=\"ageError\" class=\"error\"> </span><br><br>");
            out.println("<input type=\"submit\" id=\"submit\" name=\"submit\" value=\"Get Result\">");
            out.println("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;");
            out.println("<input type=\"reset\" id=\"reset\" name=\"reset\" value=\"Reset\">");
            out.println("</form> <br> <br> <br>");
            
            // query result
            out.println("<h2>Query Results</h2>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Employee ID</th>");
            out.println("<th>Employee Name</th>");
            out.println("<th>Job Title</th>");
            out.println("<th>Date of Birth</th>");
            out.println("<th>Joining Date</th>");
            out.println("<th>Salary</th>");
            out.println("<th>Department ID</th>");
            out.println("</tr>");
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("employeeId") + "</td>");
                out.println("<td>" + rs.getString("employeeName") + "</td>");
                out.println("<td>" + rs.getString("jobTitle") + "</td>");
                out.println("<td>" + rs.getDate("dateOfBirth") + "</td>");
                out.println("<td>" + rs.getDate("joiningDate") + "</td>");
                out.println("<td>" + rs.getDouble("salary") + "</td>");
                out.println("<td>" + rs.getInt("departmentId") + "</td>");
                out.println("</tr>\n");
            }
            
            out.println("</table>");
            out.println("</body>");
            out.println("<script src=\"littleValidation.js\"></script>");
            out.println("</html>");

        } catch (SQLException sqle) {
            Logger lgr = Logger.getLogger(ServletInfoFromUser.class.getName());
            lgr.log(Level.SEVERE, sqle.getMessage(), "");
            out.println("<html><body><h1>" + sqle.getMessage() + "</h1></body></html>");
        }
    }
}
