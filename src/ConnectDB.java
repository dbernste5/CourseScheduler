

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConnectDB {

	private Connection conn;
	private String dbURL;
	
	//constructor
	public ConnectDB(String dbURL) throws SQLException{
		
		this.dbURL =dbURL;
		conn = DriverManager.getConnection(dbURL);
		if (conn == null) 
		{
			throw new UnableToConnectException();
		}

	}
	
	public Connection getConnection()
	{
		return conn;
	}
	
}	
	
	

