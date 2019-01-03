package com.mgrid.data.oid;

public class MyOID {

	String oid;
	String name;
	String value;
	
	public MyOID(String oid,String name,String value)
	{
		this.oid=oid;
		this.name=name;
		this.value=value;
	}
	
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
