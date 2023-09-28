import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class JdbcQuery {

	private static Properties getConnectionData() {

        Properties props = new Properties();

        String fileName = "src/db.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            props.load(fis);
        } catch (IOException ioe) {
            Logger lgr = Logger.getLogger(JdbcQuery.class.getName());
            lgr.log(Level.SEVERE, ioe.getMessage(), ioe);
        }

        return props;
    }
	
	public static void main(String[] args) {
		
		Properties props = getConnectionData();
		
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String password = props.getProperty("db.password");
		
		String template = "SELECT * FROM Employee WHERE (joiningDate > ?) AND ( ((YEAR(NOW()) - YEAR(dateOfBirth) = ?) AND ( (MONTH(NOW()) > MONTH(dateOfBirth)) OR ( (MONTH(NOW()) = MONTH(dateOfBirth)) AND (DATE(NOW()) >= DATE(dateOfBirth)) ) )) OR (YEAR(NOW()) - YEAR(dateOfBirth) > ?) )";
		
		try(Connection conn = DriverManager.getConnection(url, user, password);){
			
			Scanner scan = new Scanner(System.in);
			
			Date joiningDate;
			int age;
			
			PreparedStatement inserter = conn.prepareStatement(template);
			
			System.out.println("To display all the employees who joined AFTER a certain 'date' and are OVER 'n' years old.\n\nEnter the following:");
			
			System.out.print("Joining Date(YYYY-MM-DD): ");
			String dateString = scan.nextLine();
			joiningDate = java.sql.Date.valueOf(dateString);
			inserter.setDate(1, joiningDate);
			
			System.out.print("Age(in years): ");
			age = scan.nextInt();
			inserter.setInt(2, age);
			inserter.setInt(3, age);
			
			ResultSet rs = inserter.executeQuery();
			
			System.out.println();
			
			while(rs.next()) {
				System.out.printf("Employee ID: %-5d, Employee Name: %-20s, Job Title: %-15s, Birth Date: %-11s, Joining Date: %-11s, Salary: %-10.2f, Department ID: %-5d \n",
						rs.getInt("employeeId"), rs.getString("employeeName"), rs.getString("jobTitle"), (rs.getDate("dateOfBirth")).toString(), (rs.getDate("joiningDate")).toString(), rs.getDouble("salary"), rs.getInt("departmentId"));
			}
			
			scan.close();
			
		} catch(IllegalArgumentException iae) { // for input of type 'date'
			System.out.println("Illegal Argument Error: " + iae + "\nTryAgain!");
		} catch(InputMismatchException ime) {
			System.out.println("Input Type Mismatch Error: " + ime + "\nTryAgain!");
		} catch(SQLException sqle) {
			Logger lgr = Logger.getLogger(JdbcQuery.class.getName());
			lgr.log(Level.SEVERE, sqle.getMessage(), sqle);
		}
		
	}

}
