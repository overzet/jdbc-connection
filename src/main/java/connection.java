import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class connection {

    public static void main(String[] args) throws SQLException {

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        // Create a connection
        try {   // The fancy pancy way
            Properties props = new Properties();
            props.load(new FileInputStream("/Users/overzet/IdeaProjects/JdbcTest/sql/demo.properties"));

            String theUser = props.getProperty("user");
            String thePassword = props.getProperty("password");
            String theDburl = props.getProperty("dburl");

            System.out.println("Connecting to database......");
            System.out.println("Database URL " + theDburl);
            System.out.println("User " + theUser);

            myConn = DriverManager.getConnection(theDburl, theUser, thePassword);

            // Create a connection old school cool
            //myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo?user=student&password=student&serverTimezone=UTC&useSSL=false");

            System.out.println("");
            System.out.println("");
            System.out.println("Database connection successful!\n");

            DatabaseMetaData databaseMetadata = myConn.getMetaData();

            System.out.println("Product name " + databaseMetadata.getDatabaseProductName());
            System.out.println("Product version " + databaseMetadata.getDatabaseProductVersion());
            System.out.println();
            System.out.println("JDBC driver name " + databaseMetadata.getDriverName());
            System.out.println("JDBC driver version " + databaseMetadata.getDriverVersion());
            System.out.println();

            // 2. Create a statement
            myStmt = myConn.createStatement();

            // 3. Execute SQL query
            myRs = myStmt.executeQuery("select * from employees");

            PreparedStatement myPrepStmt = myConn.prepareStatement("DELETE FROM employees WHERE salary > ? AND department = ?");

            myPrepStmt.setInt(1, 200000);
            myPrepStmt.setString(2, "HR");
            //ResultSet myPrepRes = myPrepStmt.executeQuery();
            int rowsAffected = myPrepStmt.executeUpdate();
            System.out.println("Rows effected " + rowsAffected);


            while (myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
            }

            // 3a. Insert a new employee if it not exists
            System.out.println("");
            System.out.println("");
            System.out.println("Inserting a new employee to database\n");

            rowsAffected = myStmt.executeUpdate(
                    "INSERT INTO employees (last_name, first_name, email, department, salary) " +
                            "SELECT * FROM(SELECT 'Wright', 'Eric', 'eric.wright@foo.com', 'HR', 33000.00) AS tmp " +
                            "WHERE NOT EXISTS(SELECT email FROM employees WHERE email='eric.wright@foo.com')");


            myRs = myStmt.executeQuery("select * from employees");

            // 4. Process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name") + ", " + myRs.getString("salary"));
            }
            System.out.println("Rows effected " + rowsAffected);


            System.out.println("");
            System.out.println("");
            System.out.println("Update salary employee to database\n");

            rowsAffected = myStmt.executeUpdate((
                    "UPDATE employees " +
                            "SET salary = 63000 " +
                            "WHERE last_name = 'Wright' AND first_name = 'Eric'"));


            myRs = myStmt.executeQuery("select * from employees");

            // 4. Process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name") + ", " + myRs.getString("email") + ", " + myRs.getString("salary"));
            }
            System.out.println("Rows effected " + rowsAffected);


            System.out.println("");
            System.out.println("");
            System.out.println("Fire employee to database\n");

            rowsAffected = myStmt.executeUpdate((
                    "DELETE FROM employees " +
                            "WHERE last_name = 'Wright' AND first_name = 'Eric'"));


            myRs = myStmt.executeQuery("select * from employees");

            // 4. Process the result set
            while (myRs.next()) {
                System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name") + ", " + myRs.getString("email") + ", " + myRs.getString("salary"));
            }
            System.out.println("Rows effected " + rowsAffected);


        } catch (Exception exc) {
            exc.printStackTrace();
        } finally {
            if (myRs != null) {
                myRs.close();
            }

            if (myStmt != null) {
                myStmt.close();
            }

            if (myConn != null) {
                myConn.close();
            }
        }
    }

    private static void display(ResultSet myPrepRes) throws SQLException {
        while (myPrepRes.next()) {
            System.out.println(myPrepRes.getString("last_name") + ", " + myPrepRes.getString("first_name"));
            System.out.println("");
        }
    }

}
