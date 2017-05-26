package bmc;

import javax.servlet.http.HttpServletRequest;

public interface BillingUnitProvider {
	
	BillingUnit getBillingUnit(HttpServletRequest req); 
}

