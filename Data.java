package bmc;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class Data
 */
@WebServlet("/Data")
public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Data() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Got request");
		String s = request.getRequestURI();
		System.out.println(s);
		String[] s2 = s.split("Data/");
		
		response.setContentType("application/json; charset=UTF-8");

		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BMC", "root",
					"password");
			Statement st = con.createStatement();
			ResultSet rs;

			if (s2[1].equals("Tenant")) {
				rs = st.executeQuery("select tid, sum(Count) from BMC GROUP BY tid;");
				JSONObject item1 = new JSONObject();
				JSONArray TenantId = new JSONArray();

				while (rs.next()) {

					item1 = new JSONObject();
					item1.put("TenantID", rs.getInt(1));
					item1.put("Count", rs.getInt(2));
					TenantId.put(item1);

				}
				PrintWriter out=	response.getWriter();
				out.write(TenantId.toString());out.flush();
				
				out.flush();
			}
		else
			{
			rs = st.executeQuery("select uid,Count from BMC where tid="+s2[1]+";");
			JSONObject item1 = new JSONObject();
			JSONArray UserID = new JSONArray();

			while (rs.next()) {

				item1 = new JSONObject();
				item1.put("UserID", rs.getInt(1));
				item1.put("Count", rs.getInt(2));
				UserID.put(item1);

			}
			PrintWriter out=	response.getWriter();
			out.write(UserID.toString());out.flush();
			
			out.flush();
			}
			
			
			
		} catch (ClassNotFoundException | SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Hello");

		System.out.println(request.getParameter("name"));
		System.out.println(request.getParameter("name2"));
	}

}
