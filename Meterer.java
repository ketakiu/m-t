package bmc;



import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;


import org.apache.activemq.ActiveMQConnectionFactory;

import dbHandler.ConfigParam;
import dbHandler.DatabaseAdmin;

public class Meterer implements Meter 
{
	
	private static ConfigParam confParam=new ConfigParam();
	private static DatabaseAdmin dbAdmn=new DatabaseAdmin();
	
	private static int limit=Integer.valueOf(confParam.getReqCountLimit());
	
	
	
	
	private static final String SUB1234 = "SUB1234";
//	private static String jdbcDRIVER = " ";
//	private static String dbURL = " ";
//	private static String dbUSER = " ";
//	private static String dbUserPASS = " ";
//	private static Connection MYSQLcon = null;



	private static Session JMSsession = null;
	private static Topic JMStopic = null;
	private static MessageProducer msgProducer=null;
	UserCountMap reqCountmap = UserCountMap.getInstance();
	Topic Throttlertopic = null;


//	public Meterer(String JDBC_Driver, String DB_Url, String User_Name, String User_Password, String msgqurl,
//			String msgqport, String Topic_Name) 
//	{
//		// TODO Auto-generated constructor stub
////		this.jdbcDRIVER = JDBC_Driver;
////		this.dbURL = DB_Url;
////		// Database credentials
////		this.dbUSER = User_Name;
////		this.dbUserPASS = User_Password;
////		try {
////			// Register JDBC driver
////			Class.forName(JDBC_Driver);
////
////			// Open a connection
////			this.MYSQLcon = DriverManager.getConnection(dbURL, dbUSER, dbUserPASS);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//
//	}

	

	public void doMessage(Message msg) {
		try {
			// TODO Auto-generated method stub

			//System.out.println("MSg...");
			
			TextMessage textMessage = (TextMessage) msg;

			String msgtxt = textMessage.getText();
			
			String[] info = msgtxt.split(" ");
			updatecount(info[0], info[1]);
			getcount(info[0], info[1]);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Meterer() 
	{
		
		
		String msgQurl=confParam.getMsgQaddr()+confParam.getMsgQport();
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(msgQurl);
		javax.jms.Connection connection;
		try {
			connection = connectionFactory.createConnection();

			// need to setClientID value, any string value you wish
			connection.setClientID("M13");

			try {
				connection.start();
			} catch (Exception e) {
				System.err.println("NOT CONNECTED!!!");
			}
			JMSsession= connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			 Throttlertopic=JMSsession.createTopic(confParam.getThrottlingTopic());
		     msgProducer=JMSsession.createProducer(Throttlertopic);
		     //ThrottlingManager throttlingManager=new ThrottlingManager();
		    //throttlingManager.subscribe("Throttler", msgQurl);
		     
		     
		     
			JMStopic = JMSsession.createTopic(confParam.getMsgTopic());

			// need to use createDurableSubscriber() method instead of
			// createConsumer() for topic
			// MessageConsumer consumer = session.createConsumer(topic);
			MessageConsumer consumer = JMSsession.createDurableSubscriber(JMStopic, SUB1234);
           MessageConsumer con=JMSsession.createConsumer(JMStopic);
			MessageListener listner = new MessageListener() 
			{
				public void onMessage(Message message) {

					if (message instanceof TextMessage) 
					{
						//System.out.println("hello");
						TextMessage textMessage = (TextMessage) message;
						//System.out.println(textMessage);
						doMessage(textMessage);
					}

				}
			};

			//consumer.setMessageListener(listner);
			con.setMessageListener(listner);
			
			
			
			ThrottlingManager thrManager=new ThrottlingManager();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void updatecount(String tid, String uid) 
	{
//		Statement stmt;
//		if (tid != null && uid != null) {
//			try {
//				Thread.sleep(1);
//				stmt = this.MYSQLcon.createStatement();
//				stmt.execute("use BMC");
//				String sql;
//				sql = sql = "update try set Count=Count+1 where tid=" + tid + " and uid=" + uid + ";";
//				stmt.executeUpdate(sql);
//
//			} catch (SQLException | InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
dbAdmn.setcount(tid, uid);
	}

	@Override
	public void getcount(String tid, String uid) 
	{
		
		
	
		int cnt=dbAdmn.getcount(tid, uid);
		//System.out.println(tid+"  "+cnt+"  ");
		//reqCountmap.put(uid, cnt);
		checklimit(cnt, tid, uid);
//		Statement stmt;
//		try {
//			stmt = MYSQLcon.createStatement();
//			stmt.execute("use BMC");
//			String sql;
//			sql = "SELECT * FROM try where tid=" + tid + " and uid=" + uid + ";";
//			ResultSet rs = stmt.executeQuery(sql);
//			rs.next();
//			int cnt = rs.getInt("Count");
//			// System.out.println("Putting:"+uid+"\t"+cnt);
//			reqCountmap.put(uid, cnt);
//			return cnt;
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0;
	}
	@Override
	public void checklimit(int c,String tid,String uid)
	{
		// TODO Auto-generated method stub

		
		
		int percent = (c *100/ limit);
		
		if (percent >= 100)
		{
			
		} 
		else if (percent >= 90 && percent<100)
		{
			TextMessage msg;
			try {
				msg = JMSsession.createTextMessage(tid+" "+uid+" "+"90");
				System.out.println("90 reached.............");
				msgProducer.send(msg);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} else if (percent >= 60) {
			System.out.println("60 reached.............");
			TextMessage msg;
			try {
				msg = JMSsession.createTextMessage(tid+" "+uid+" "+"60");
				msgProducer.send(msg);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		} 
	}
	public static void main(String[] args)
	{
		
		//should be a separate program
		//TODO this should create a connection and should wait for the messages to arrive
		//Meterer meterer = new ME
		Meterer meteringUnit=new Meterer();
		
		
		
	}

}
