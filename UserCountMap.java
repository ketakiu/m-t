package bmc;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserCountMap implements UserCountMapper
{

	
	
	private Map<String,Integer>map;
	private static UserCountMap mp=new UserCountMap();
	
	
	public static UserCountMap getInstance() 
	{
		// TODO Auto-generated constructor stub
		return mp;
	}
	
	
	private UserCountMap()
	{
		map=new ConcurrentHashMap<String,Integer>();
	}
	
	
	@Override
	public void put(String key,Integer value)
	{
		// TODO Auto-generated method stub
		map.put(key, value);
		
	}
	public void show()
	{
		System.out.println("showing cache info");
		Iterator it = map.entrySet().iterator();
	    for(Map.Entry e:map.entrySet())
	    {
	    	System.out.println(".");
	    	System.out.println(e.getKey()+"  "+e.getValue());
	    }
	}

	@Override
	public Integer get(String key) {
		// TODO Auto-generated method stub
		return map.get(key);
	}
	public static void main(String[]args)
	{
		
		
		UserCountMap mp=UserCountMap.getInstance();
		mp.put("0",5);
		mp.show();
		
	}

	
}
