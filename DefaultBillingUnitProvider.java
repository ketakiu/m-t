package bmc;
import javax.servlet.http.HttpServletRequest;

public class DefaultBillingUnitProvider implements BillingUnitProvider {

	@Override
	public BillingUnit getBillingUnit(HttpServletRequest req) {
		// get this into interfaces
		String msg = req.getHeader("Name");
		String tnt = req.getParameter("tnt");
		String usr = req.getParameter("usr");
		
		// TODO Auto-generated method stub
		return new BillingUnit(msg, tnt, usr);
	}

}
