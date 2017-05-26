package bmc;


import java.net.URI;



import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;



import javax.jms.*;

public class MessageSender {
	BrokerService msgbroker = null;
	Connection msgBRKRconn = null;
	ConnectionFactory msgBRKRconnFactory = null;
	Session msgSession = null;
	Topic msgTopic = null;
	MessageProducer msgProducer = null;

	public MessageSender(String msgbrkraddr,String msgbrkrport,String msgqurl,String msgqport,String Topic_Name) {

	try {
		
		this.msgbroker = BrokerFactory.createBroker(new URI(

				msgbrkraddr+msgbrkrport+")"));
	

		this.msgbroker.start();

		this.msgBRKRconn = null;

		// Producer
       
		
		this.msgBRKRconnFactory = new ActiveMQConnectionFactory(

				msgqurl+msgqport);

		this.msgBRKRconn = this.msgBRKRconnFactory.createConnection();

		this.msgSession = this.msgBRKRconn.createSession(false,

				Session.AUTO_ACKNOWLEDGE);
     
		this.msgTopic = this.msgSession.createTopic(Topic_Name);

		this.msgProducer = this.msgSession.createProducer(this.msgTopic);

		this.msgBRKRconn.start();

	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}

	public void sendmsg(String msgtxt) {

		Message msg;
		try {

			msg = this.msgSession.createTextMessage(msgtxt);

			//System.out.println("Sending text '" + msgtxt + "'");
			this.msgProducer.send(msg);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}