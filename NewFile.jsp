<%@ page import ="java.sql.*" %>
<%@ page import ="javax.sql.*" %>
<html>
<head>
<script type="text/JavaScript">
               <!--
               function Autoref(t)
               {
            	   setTimeout("location.reload(true);",t);
					
			    }
               </script>             



</head>
<body onload="JavaScript:Autoref(9000);"> 
<!--Div that will hold the pie chart-->
<div id="chart_div" style="width:65%"><canvas id="mycanvas">></canvas></div>

<%
String tenantId=new String(request.getParameter("tenant_id"));
System.out.print(tenantId);
String Query=new String(request.getParameter("Query"));
if(Query.equals("Query1"))
{            
         
         

%>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.js"></script>
	<script type="text/javascript">
   
	$(document).ready(function(){
	
	    	 
           $.ajax({
			url: "http://localhost:8080/BEProject/Data/Tenant",
			
			method: "GET",
			success: function(data) {
				console.log(data);
				var Tenant = [];
				var Count = [];
				for(var i in data) 
				{
					Tenant.push("Tenant " + data[i].TenantID);
					Count.push(data[i].Count);
					
				}

				var chartdata = {
					labels: Tenant,
					datasets : [
						{
							label: 'Tenant wise Request Count',
							backgroundColor: 'rgba(150, 200, 100, 0.75)',
							borderColor: 'rgba(150, 200, 100, 0.75)',
							hoverBackgroundColor: 'rgba(200, 200, 200, 1)',
							hoverBorderColor: 'rgba(200, 200, 200, 1)',
							data: Count
						}
					]
				};

				var ctx = $("#mycanvas");

				var barGraph = new Chart(ctx, {
					type: 'bar',
					data: chartdata
				});
			},
			error: function(data) {
				console.log(data);
			}
		});
	});
	</script>
  
<% 
}
%>

<% 
if(Query.equals("Query3"))
{            
         
         

%>

<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.js"></script>
	<script type="text/javascript">
   
    var urlx="http://localhost:8080/BEProject/Data/"
    var var1="<%=tenantId%>";
    urlx=urlx+var1;
    urlx=urlx.replace(/ /g,"");
	$(document).ready(function(){
	
	    	 
           $.ajax({
			url:urlx,
			
			method: "GET",
			success: function(data) {
				console.log(data);
				var User = [];
				var Count = [];
				for(var i in data) 
				{
					User.push("User " + data[i].UserID);
					Count.push(data[i].Count);
					
				}

				var chartdata = {
					labels: User,
					datasets : [
						{
							label: 'User wise Request Count for Tenant '+var1,
							backgroundColor: 'rgba(150, 0, 220, 0.75)',
							borderColor: 'rgba(150, 0, 220, 0.75)',
							hoverBackgroundColor: 'rgba(150, 0, 220, 1)',
							hoverBorderColor: 'rgba(150, 0, 220, 1)',
							data: Count
						}
					]
				};

				var ctx = $("#mycanvas");

				var barGraph = new Chart(ctx, {
					type: 'bar',
					options:{
						scales:{
							yAxes:[{ticks:{beginAtZero:true}}]
						}
							
					},
					data: chartdata
				});
			},
			error: function(data) {
				console.log(data);
			}
		});
	});
	</script>
  
<% 
}
%>
























<table width=90%, border=1>
<%

if(Query.equals("Query2") || Query.equals("Query3"))
{ 
        
	

	
	Class.forName("com.mysql.jdbc.Driver"); 
	java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/BMC","root","password"); 
	Statement st= con.createStatement(); 
	ResultSet rs;
	//response.setIntHeader("Refresh", 5);
           int ten_id=Integer.parseInt(request.getParameter("tenant_id"));
           rs=st.executeQuery("select * from BMC where tid="+ten_id);        
           out.println( "<tr>" );
           out.println("<th>User id</th>");
           out.println("<th>Tenant id</th>");
           out.println("<th>Count</th>");
           out.println("<th>Comments</th>");

while(rs.next())
 {         
           
           out.println( "</tr>" );

           out.println( "<tr>" ); 
	   out.println("<td>" + rs.getString("uid") + "</td>");	

           out.println( "<td>" + rs.getString("tid") + "</td>");
           out.println( "<td>" + rs.getString("Count") + "</td>");
           //if(rs.getString("count").equals("90"))
        	   if(Integer.valueOf(rs.getString("Count"))>=135 && Integer.valueOf(rs.getString("Count"))<150 )
           {
            out.println("<td>ALERT 90% limit exceeded</td>");
           }
        	   else if(Integer.valueOf(rs.getString("Count"))>=150 )
               {
                   out.println("<td>ALERT limit exceeded</td>");
                  }
           else
           {
            out.println("<td cellpadding=10>Usage below 90%</td>");
           }
           out.println( "</tr>" );
     
 }
}

%>
</table>
</body>
</html>

