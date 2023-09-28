import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.Thread;

public class JdbcTransaction {

	private static Properties getConnectionData() {

        Properties props = new Properties();

        String fileName = "src/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(JdbcTransaction.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	public static void main(String[] args) {

		Properties props = getConnectionData();
		
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String password = props.getProperty("db.password");
		
		String newDepartmentQuery = "INSERT INTO Department VALUES (400, \"Management\", \"Mumbai\")";
		String employeeQuery = "SELECT * FROM Employee ORDER BY joiningDate";
		String departmentQuery = "SELECT * FROM Department";
		
		String updateEmployeeTemplate = "UPDATE Employee SET DepartmentId = ?, EmployeeId = ? WHERE EmployeeId = ?";
		
		try(Connection conn = DriverManager.getConnection(url, user, password);) {
			
			try(Statement st = conn.createStatement()){
			
				conn.setAutoCommit(false);
			
				st.execute(newDepartmentQuery);
				
				ResultSet rs = st.executeQuery(employeeQuery);
//				while(rs.next()) {
//					System.out.println(rs.getDate(1));
//				}
				
				PreparedStatement inserter = conn.prepareStatement(updateEmployeeTemplate);
				
				System.out.println("Moving 3 senior most employees to Management department...\n\n");
				
				for(int i=0; i<3; i++) {
					if(rs.next()) {
						int empID = 40000 + (i + 1);
						inserter.setInt(1, 400);
						inserter.setInt(2, empID);
						inserter.setInt(3, rs.getInt("employeeId"));
						inserter.executeUpdate();
						Thread.sleep(3000);
						System.out.println("Employee moved to Management department.\n");
						// employee ID also updated since it is based on department, according to my assumption
					}
				}
			
				conn.commit();
				
				Thread.sleep(3000);
				
				System.out.println("\n\nUpdated Data:");
				
				rs = st.executeQuery(employeeQuery);
				
				System.out.println("\nEmployee Table:");
				while(rs.next()) {
					System.out.printf("Employee ID: %-5d, Employee Name: %-20s, Job Title: %-15s, Birth Date: %-11s, Joining Date: %-11s, Salary: %-10.2f, Department ID: %-5d \n",
							rs.getInt("employeeId"), rs.getString("employeeName"), rs.getString("jobTitle"), (rs.getDate("dateOfBirth")).toString(), (rs.getDate("joiningDate")).toString(), rs.getDouble("salary"), rs.getInt("departmentId"));
				}
				
				rs = st.executeQuery(departmentQuery);
				
				System.out.println("\nDepartment Table:");
				while(rs.next()) {
					System.out.printf("Department ID: %-5d, Department Name: %-20s, Department Location: %-20s \n",
							rs.getInt("departmentId"), rs.getString("departmentName"), rs.getString("departmentLocation"));
				}
			
			} catch(SQLException sqle) {
				
				try {
					System.out.println(sqle);
					System.out.println("Moving failed!");
					conn.rollback();
				} catch(SQLException sqle1) {
					Logger lgr = Logger.getLogger(JdbcTransaction.class.getName());
					lgr.log(Level.SEVERE, sqle1.getMessage(), sqle1);
				}
				
			} catch(InterruptedException ie) {
				Thread.currentThread().interrupt();
				System.out.println("Interrupted Error: " + ie);
			}
			
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(JdbcTransaction.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
	}

}
