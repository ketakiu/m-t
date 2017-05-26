package bmc;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class RequestServer extends HttpServlet
{

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		// String s=(String) req.getAttribute("Name");
		// ServletContext sv=req.getSession().getServletContext();
		String st = req.getHeader("Name");
		String tnt = req.getParameter("tnt");
		String usr = req.getParameter("usr");
		
		
		
		
		res.setContentType("text/html");// setting the content type
		PrintWriter pw = res.getWriter();// get the stream to write the data
		// writing html in the stream
		pw.println("<html><body>");
		pw.println("Welcome..!!");
		// pw.println(s);
		pw.print(st);
		pw.print("TenantID:" + tnt);
		pw.print("\tUserID:" + usr);
		// pw.println(sv.getAttribute("Name"));
		pw.println("</body></html>");

		pw.close();// closing the stream

		

		
	}

	

}
