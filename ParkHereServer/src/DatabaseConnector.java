import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
	private Connection conn;
	public DatabaseConnector(){
		conn = null;
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://db4free.net/park_here?user=elautz&password=elautz&useSSL=false");
			System.out.println("connected!");
			//st = conn.createStatement();
			
			ps = conn.prepareStatement("SELECT * FROM cancellation_policy");
			//ps.setString(1, name); // set first variable in prepared statement
			rs = ps.executeQuery();
			//System.out.println(rs.);
			while (rs.next()) {
				Integer fname = rs.getInt("cancellation_policy_id");
				String lname = rs.getString("cancellation_policy");
				//int studentID = rs.getInt("studentID");
				System.out.println ("id = " + fname);
				System.out.println ("policy = " + lname);
				//System.out.println ("studentID = " + studentID);
			}
		}
		catch (SQLException sqle){
			System.out.println("sql excep: "+sqle.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("class not found excep: "+e.getMessage());
		}
		
	}

}
