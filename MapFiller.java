package bmc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import dbHandler.ConfigParam;
import dbHandler.DatabaseAdmin;


public class MapFiller extends Thread
{
     
	private static String JDBCdriver = null;
	private static String dbURL = null;
	private static String dbUSER = null;
	private static String dbUSERpassword = null;
	private static String dbNAME = null;
	private static Connection dbConnection = null;
	private static ConfigParam configparameter = ConfigParam.getinstance();
	ResultSet rs;
	UserCountMap usercntmap=UserCountMap.getInstance();
	DatabaseAdmin dbadmin=new DatabaseAdmin();
	

	public MapFiller() {

//		JDBCdriver = configparameter.getJDBCdriver();
//		dbURL = configparameter.getDBurl();
//		dbUSER = configparameter.getDBuser();
//		dbNAME = configparameter.getDBname();
//		dbUSERpassword = "password";
//
//		try {
//			// Register JDBC driver
//			Class.forName(JDBCdriver);
//
//			// Open a connection
//			dbConnection = DriverManager.getConnection(dbURL, dbUSER, dbUSERpassword);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	public void FillMap() {
		// TODO Auto-generated method stub
		Statement dbStmt;

		try {

//			dbStmt = dbConnection.createStatement();
//			dbStmt.execute("use" +" "+ dbNAME);
//			String sqlQuery;
//			sqlQuery = "select COUNT(*) as Count from try ;";
//			rs = dbStmt.executeQuery(sqlQuery);
//			rs.next();
//			//int totalCount = rs.getInt("Count");
//			sqlQuery = "select tid,uid,Count,datediff(curdate()) from try ;";
			rs = dbadmin.getuserinfo();
		
			
//			int Count = rs.getInt("Count");
//			String uid = rs.getString("uid");
//			String tid = rs.getString("tid");
//			System.out.println(tid+" "+Count);
			Thread []thrd=new Thread[50];
			int i=0;
			while(rs.next())
			{
				int Count = rs.getInt("Count");
				String uid = rs.getString("uid");
				String tid = rs.getString("tid");
				int dateDiff=rs.getInt("diff");
				
				thrd[i]=new Thread(new Runnable() {
					
					@Override
					public void run() 
					{
						if(dateDiff>=0)
						    usercntmap.put(tid+uid, Count);				
					}
				});
				thrd[i].start();
				i++;
			}
		
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//usercntmap.show();
	
	}
	public static void main(String[]args)
	{
		MapFiller mp =new MapFiller();
		mp.FillMap();
		
	}

}