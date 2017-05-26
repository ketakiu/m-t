package bmc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnectionFactory;


import dbHandler.ConfigParam;
import dbHandler.DatabaseAdmin;



public class ThrottlingManager
{

	  Connection con = null;
	  
	   javax.jms.Connection connection = null;
		ConnectionFactory connectionFactory = null;
		Session session = null;
		Topic topic = null;
		MessageConsumer consumer=null;
		MessageListener listener;
	UserCountMap map=UserCountMap.getInstance();
    DatabaseAdmin d=new DatabaseAdmin();
 
    
    private static String JDBCdriver = null;
	private static String dbURL = null;
	private static String dbUSER = null;
	private static String dbUSERpassword = null;
	private static String dbNAME = null;
	private static Connection dbConnection = null;
	private static ConfigParam configparameter = ConfigParam.getinstance();

	public ThrottlingManager() {

		JDBCdriver = configparameter.getJDBCdriver();
		dbURL = configparameter.getDBurl();
		dbUSER = configparameter.getDBuser();
		dbNAME = configparameter.getDBname();
		dbUSERpassword = "password";

		try {
			// Register JDBC driver
			Class.forName(JDBCdriver);

			// Open a connection
			dbConnection = DriverManager.getConnection(dbURL, dbUSER, dbUSERpassword);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		this.subscribe(configparameter.getThrottlingTopic(),configparameter.getMsgQaddr()+configparameter.getMsgQport());
	}
		
		
	
	
	public void doMessage(Message msg) {
		try {
			// TODO Auto-generated method stub
			
			TextMessage textMessage = (TextMessage) msg;

			String msgtxt = textMessage.getText();
			
			String[] info = msgtxt.split(" ");
			
			if(info[2].equals("90"))
			{
				do90(info[0],info[1]);
			}
			else if(info[2].equals("60"))
			{
				do60(info[0],info[1]);
			}
			
			

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
public void subscribe(String topicname,String url)
{

	
	
	
	
	ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    javax.jms.Connection connection;
	try {
		connection = connectionFactory.createConnection();
	
	

    // need to setClientID value, any string value you wish
    connection.setClientID("M1234567");

    try{
    connection.start();
    }catch(Exception e){
        System.err.println("NOT CONNECTED!!!");
    }
    Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);

    Topic topic = session.createTopic(topicname);

    //need to use createDurableSubscriber() method instead of createConsumer() for topic
    // MessageConsumer consumer = session.createConsumer(topic);
    MessageConsumer consumer = session.createDurableSubscriber(topic,
            "SUB12345");
    MessageConsumer con=session.createConsumer(topic);
    MessageListener listner = new MessageListener() {
    	public void onMessage(Message message) {
            
                if (message instanceof TextMessage)
                {
                    TextMessage textMessage = (TextMessage) message;
                  doMessage(textMessage);
                }
                
            }
        };
    
    consumer.setMessageListener(listner);
    con.setMessageListener(listner);
	}
catch (JMSException e1) {
// TODO Auto-generated catch block
          e1.printStackTrace();
      }
   

}
	
	

	
	public void do90(String tid, String uid) {
		
//		System.out.println("90         "+tid+"   "+uid);
//
//		Statement stmt;
//if(tid!=null && uid!=null)
//{
//		try {
//		
//			stmt = this.con.createStatement();
//			stmt.execute("use BMC");
//			String sql;
//			sql = sql = "select Name,Last_Name from try where tid="+tid+" and uid="+uid+";";
//			ResultSet rs = stmt.executeQuery(sql);
//            rs.next();
//            String name=rs.getString("Name");
//            String Last_name=rs.getString("Last_Name");
            
            
            System.out.println(d.getname(tid, uid)+" "+d.getlastname(tid, uid)+" "+"Limit Breach level 2");
            

//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//}

	



	}

	
	public void do60(String tid, String uid) 
	{
		
		
//		System.out.println(d.getname(tid,uid));
//		
//		System.out.println("60         "+tid+"   "+uid);
//		Statement stmt;
//if(tid!=null && uid!=null)
//{
//		try {
//
//			stmt = this.con.createStatement();
//			stmt.execute("use BMC");
//			String sql;
//			sql = sql = "select Name,Last_Name from try where tid="+tid+" and uid="+uid+";";
//			ResultSet rs = stmt.executeQuery(sql);
//            rs.next();
//            String name=rs.getString("Name");
//            String Last_name=rs.getString("Last_Name");
            
            
        System.out.println(d.getname(tid, uid)+" "+d.getlastname(tid, uid)+" "+"Limit Breach level 1");

            
			
			
//
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//}

	



	}


	
}

