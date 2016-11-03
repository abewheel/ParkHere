import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.User;

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
	
	public User createUser(User user) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("INSERT INTO "+DBConstants.USER_TB+" ("+
				DBConstants.USER_EMAIL_COL+", "+DBConstants.USER_NAME_COL+", "+DBConstants.PASSWORD_COL+") VALUES ('"+
				user.getEmail()+"', '"+user.getName()+"', '"+user.getPassword()+"')", Statement.RETURN_GENERATED_KEYS);
	
		System.out.println("INSERT INTO "+DBConstants.USER_TB+" ("+
				DBConstants.USER_EMAIL_COL+", "+DBConstants.USER_NAME_COL+", "+DBConstants.PASSWORD_COL+") VALUES ("+
				user.getEmail()+", "+user.getName()+", "+user.getPassword()+")");
		
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		System.out.println("about to print id");
		if (rs.next()){
			System.out.println(rs.getLong(1));
			user.setUser_id(rs.getLong(1));
		}
		
		return user;
	}
	
	public Boolean checkUserPasssword(String email, String password) throws SQLException{
		PreparedStatement ps = conn.prepareStatement("SELECT * FROM"+DBConstants.USER_TB+" WHERE "+DBConstants.USER_EMAIL_COL+" = "+email
				+" AND "+DBConstants.PASSWORD_COL+" = "+password);
		ResultSet rs = ps.executeQuery();
		return rs.next();
	}

}
