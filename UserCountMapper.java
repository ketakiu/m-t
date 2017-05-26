package bmc;


public interface UserCountMapper<K,V>
{


public void put(String key, Integer value);
public Integer get(String key);
  
}
