import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Properties;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.IllegalArgumentException;

public class JdbcInfoFromUser {
	
	private static Properties getConnectionData() {

        Properties props = new Properties();

        String fileName = "src/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(JdbcInfoFromUser.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	public static void main(String[] args) {
		
		Properties props = getConnectionData();
		
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String password = props.getProperty("db.password");
		
		String templateForEmployee = "INSERT INTO Employee VALUES(?, ?, ?, ?, ?, ?, ?)";
		String templateForDepartment = "INSERT INTO Department VALUES(?, ?, ?)";
		
		String queryEmployee = "SELECT * FROM Employee";
		String queryDepartment = "SELECT * FROM Department";
		
		try (Connection conn = DriverManager.getConnection(url, user, password);) {
						
			int option;
			int employeeId;
			String employeeName;
			String jobTitle;
			Date dateOfBirth;
			Date joiningDate;
			double salary;
			int departmentId;
			String departmentName;
			String departmentLocation;
			
			Scanner scan = new Scanner(System.in);
			
			PreparedStatement inserter = conn.prepareStatement(templateForDepartment);
			// Populating Department table
			System.out.println("---Department Data---");
			
			while(true) {
				System.out.print("\nEnter 0 to exit and 1 to insert department data: ");
				option = scan.nextInt();
				if(option == 0) {
					break;
				}
				else if(option == 1) {
					System.out.print("Department ID(positive integer): ");
					departmentId = scan.nextInt();
					scan.nextLine();
					inserter.setInt(1, departmentId);
					
					System.out.print("Department Name: ");
					departmentName = scan.nextLine();
					inserter.setString(2, departmentName);
					
					System.out.print("Department Location(city): ");
					departmentLocation = scan.nextLine();
					inserter.setString(3, departmentLocation);
					
					inserter.executeUpdate();
				}
			}
			
			inserter = conn.prepareStatement(templateForEmployee);
			// Populating Employee table
			System.out.println("\n\n---Employee Data---");
			
			while(true) {
				System.out.print("\nEnter 0 to exit and 1 to insert employee data: ");
				option = scan.nextInt();
				if(option == 0) {
					break;
				}
				else if(option == 1) {
					System.out.print("Employee ID(positive integer): " );
					employeeId = scan.nextInt();
					scan.nextLine();
					inserter.setInt(1, employeeId);
					
					System.out.print("Employee Name: ");
					employeeName = scan.nextLine();
					inserter.setString(2, employeeName);
					
					System.out.print("Job Title: ");
					jobTitle = scan.nextLine();
					inserter.setString(3, jobTitle);
					
					System.out.print("Date of Birth(YYYY-MM-DD): ");
					String dateString = scan.nextLine();
					dateOfBirth = java.sql.Date.valueOf(dateString);
					inserter.setDate(4, dateOfBirth);
					
					System.out.print("Joining Date(YYYY-MM-DD): ");
					dateString = scan.nextLine();
					joiningDate = java.sql.Date.valueOf(dateString);
					inserter.setDate(5, joiningDate);
					
					System.out.print("Salary: ");
					salary = scan.nextDouble();
					inserter.setDouble(6, salary);
					
					System.out.print("Department ID(positive integer): ");
					departmentId = scan.nextInt();
					inserter.setInt(7, departmentId);
					
					inserter.executeUpdate();
				}
			}
			
			scan.close();
			
			System.out.println("\n\nInserted Data:");
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(queryEmployee);
			
			System.out.println("\nEmployee Table:");
			while(rs.next()) {
				System.out.printf("Employee ID: %-5d, Employee Name: %-20s, Job Title: %-15s, Birth Date: %-11s, Joining Date: %-11s, Salary: %-10.2f, Department ID: %-5d \n",
						rs.getInt("employeeId"), rs.getString("employeeName"), rs.getString("jobTitle"), (rs.getDate("dateOfBirth")).toString(), (rs.getDate("joiningDate")).toString(), rs.getDouble("salary"), rs.getInt("departmentId"));
			}
			
			rs = st.executeQuery(queryDepartment);
			
			System.out.println("\nDepartment Table:");
			while(rs.next()) {
				System.out.printf("Department ID: %-5d, Department Name: %-20s, Department Location: %-20s \n",
						rs.getInt("departmentId"), rs.getString("departmentName"), rs.getString("departmentLocation"));
			}
			
		} catch (InputMismatchException ime) {
			System.out.println("Input Type Mismatch Error: " + ime + "\nTry Again!");
		} catch (IllegalArgumentException iae) { // for input of type 'date'
			System.out.println("Illegal Argument Error: " + iae + "\nTry Again!");
		} catch (SQLException sqle) {
			Logger lgr = Logger.getLogger(JdbcInfoFromUser.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}

	}

}