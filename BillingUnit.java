package bmc;


public class BillingUnit {

	private String usr;
	private String tnt;
	private String msg;

	public BillingUnit(String msg, String tnt, String usr) {
		this.msg = msg;
		this.tnt = tnt;
		this.usr = usr;
	}

	public String getUsr() {
		return usr;
	}

	public String getTnt() {
		return tnt;
	}

	public String getMsg() {
		return msg;
	}

}

