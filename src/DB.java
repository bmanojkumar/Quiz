import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class DB {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/cnfquiz";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "manoj";

	static Connection conn = null;
	static Statement stmt = null;

	DB() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		//System.out.println("Connecting to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	}
}