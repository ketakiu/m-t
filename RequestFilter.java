package bmc;

//Import required java libraries
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import dbHandler.ConfigParam;


//Implements Filter class
public class RequestFilter implements Filter 

{
	private static ConfigParam confparam=new ConfigParam();
	
	
	
	
	private static String msgBRKRport="";
	private static String MSGBRKRADDR = "";
	private static String msgQaddr = "";
	private static String msgQport= "";
	private static String msgTOPICname = "";
//	private static String JDBCdriver= "";
//	private static String dbURL = "";
//	private static String dbUSER = "";
//	private static String dbUSERpassword = "";
//	private static String dbNAME = "";
//	private static String dbTABLEname = "";
	private static int reqCOUNTlimit = 0;
	

	private static MessageSender messageSender;
	//private static Throttler throttler;
	//private static Meterer meterer;
	private static DefaultBillingUnitProvider billingUnitProvider;
	private static final String Server_id="BMCserver1234";

	public void init(FilterConfig config) throws ServletException 
	{

		MapFiller mapfill=new MapFiller();
		mapfill.FillMap();
		System.out.println("got request");
		billingUnitProvider = new DefaultBillingUnitProvider();
		
//		RequestFilter.MSGBRKRADDR = config.getInitParameter("MSGBRKRADDR");
//		RequestFilter.msgBRKRport= config.getInitParameter("MSGBRKRPORT");
//        RequestFilter.msgQaddr= config.getInitParameter("MSGQADDR");
//		RequestFilter.msgQport = config.getInitParameter("MSGQPORT");
//		RequestFilter.msgTOPICname = config.getInitParameter("MsgTopic");
//		
		
		RequestFilter.MSGBRKRADDR = confparam.getMsgBRKRaddr();
		RequestFilter.msgBRKRport= confparam.getMsgBRKRport();
        RequestFilter.msgQaddr= confparam.getMsgQaddr();
		RequestFilter.msgQport = confparam.getMsgQport();
		RequestFilter.msgTOPICname = confparam.getMsgTopic();
		RequestFilter.reqCOUNTlimit = Integer.valueOf(confparam.getReqCountLimit());
		
		
		
		
        RequestFilter.messageSender = new MessageSender(MSGBRKRADDR, msgBRKRport, msgQaddr, msgQport, msgTOPICname);

//		RequestFilter.JDBCdriver = config.getInitParameter("JDBC_Driver");
//		RequestFilter.dbURL= config.getInitParameter("DB_Url");
//		RequestFilter.dbUSER = config.getInitParameter("User_Name");
//		RequestFilter.dbUSERpassword = config.getInitParameter("User_Password");
//		RequestFilter.dbNAME = config.getInitParameter("DB_Name");
//		RequestFilter.dbTABLEname = config.getInitParameter("DB_Table");

		

		//meterer = new Meterer(RequestFilter.JDBCdriver, RequestFilter.dbURL, RequestFilter.dbUSER, RequestFilter.dbUSERpassword, RequestFilter.msgQaddr,
				//RequestFilter.msgBRKRport, RequestFilter.msgTOPICname);

		//meterer=new Meterer(RequestFilter.msgTOPICname, RequestFilter.msgQaddr + RequestFilter.msgQport);

		//throttler = new Throttler(RequestFilter.reqCOUNTlimit, RequestFilter.JDBCdriver, RequestFilter.dbURL, RequestFilter.dbUSER, RequestFilter.dbUSERpassword,
				//RequestFilter.msgQaddr, RequestFilter.msgBRKRport, RequestFilter.msgTOPICname);

		//throttler.subscribe(RequestFilter.msgTOPICname, RequestFilter.msgQaddr + RequestFilter.msgQport);

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws java.io.IOException, ServletException {

		
		
		
		
		//String ipAddress = request.getRemoteAddr();

		// Log the IP address and current timestamp.
		HttpServletRequest req = (HttpServletRequest) request;
		//String path = req.getRequestURI();

		// No need for this filter based on admin
	

			BillingUnit billingUnit = billingUnitProvider.getBillingUnit(req);

			// get this into interfaces
			String msg = billingUnit.getMsg();
			String tnt = billingUnit.getTnt();
			String usr = billingUnit.getUsr();

			

			String key = tnt+usr;
			UserCountMap map = UserCountMap.getInstance();
//			do {
//               // System.out.println("===================");
//			} while (map.get(key) == null);
       	 Integer val;
       	 
       	 
       	 //System.out.println(usr+"  "+map.get(key));
			val = map.get(key);

			
			
		//System.out.println(key+"  "+val+"      "+reqCOUNTlimit );	
			
		if(val==null)
		{
			response.setContentType("text/html");// setting the content type
			PrintWriter pw = response.getWriter();// get the stream to write
													// the data
			// writing html in the stream
			pw.println("<html><body>");

			pw.print("TenantID:" + tnt);
			pw.print("\tUserID:" + usr);
			pw.println("Your Subsciption period has been Expired....please contact to your Admin.");
			pw.println("</body></html>");

			pw.close();// closing the stream	
		}
		else 
			{
			System.out.println(val+"\t"+reqCOUNTlimit);
			if ((int) val >= reqCOUNTlimit) 
			
			{
				
				response.setContentType("text/html");// setting the content type
				PrintWriter pw = response.getWriter();// get the stream to write
														// the data
				// writing html in the stream
				pw.println("<html><body>");

				pw.print("TenantID:" + tnt);
				pw.print("\tUserID:" + usr);
				pw.println("You have exceeded your limit....please contact to your Admin.");
				pw.println("</body></html>");

				pw.close();// closing the stream
			} 
			else {
				
				// System.out.println(val);
				RequestFilter.messageSender.sendmsg(tnt + " " + usr + " " + msg+" "+Server_id);
				map.put(key, (int) val + 1);
				// Pass request back down the filter chain
				chain.doFilter(request, response);
			    }
	        }
		}

	

//	public void updatelog(String ip, String date) throws IOException {
//		BufferedWriter bw = null;
//		FileWriter fw = null;
//		String path = "C:\\Users\\srathod\\workspace\\Try1\\mylog.txt";
//		path = path.replace("\\", "/");
//		File file = new File(path);
//
//		// if file doesnt exists, then create it
//		if (!file.exists()) {
//			file.createNewFile();
//			System.out.println("File Created");
//		}
//
//		// true = append file
//		fw = new FileWriter(file.getAbsoluteFile(), true);
//		bw = new BufferedWriter(fw);
//
//		bw.write(ip + ',' + date + '\n');
//		bw.close();
//		fw.close();
//
//	}

	public void destroy() {
		/*
		 * Called before the Filter instance is removed from service by the web
		 * container
		 */
	}
}
