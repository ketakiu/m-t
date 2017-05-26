package bmc;

import javax.jms.JMSException;

public interface Meter  
{
  void getcount(String a,String b);
  void updatecount(String a,String b);
  public void checklimit(int c,String tid,String uid) throws JMSException;
  
}

