package com.sg.web.base;

public class ViewObjectBase {
	
	String typeId; //类型ID
	String type;//类型
	float  left;  //左上角x坐标
	float  top;    //左上角y坐标
	float  wight;  //宽度
    float  heght;  //长度
    int    ZIndex;  //显示顺序  越大越显示在后层；
    float  fromWight;//初始宽度
    float  fromHeight;//初始高度
    String value;
    String cmd;

   
    
 

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public float getFromWight() {
		return fromWight;
	}

	public void setFromWight(float fromWight) {
		this.fromWight = fromWight;
	}

	public float getFromHeight() {
		return fromHeight;
	}

	public void setFromHeight(float fromHeight) {
		this.fromHeight = fromHeight;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ViewObjectBase(String typeId,float  left,float  top,float  wight,float  heght,int    ZIndex)
    {
    	this.typeId=typeId;
    	this.left=left;
    	this.top=top;
    	this.wight=wight;
    	this.heght=heght;
    	this.ZIndex=ZIndex;
    }
    
    public ViewObjectBase()
    {
    	
    }
    
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public float getLeft() {
		return left;
	}
	public void setLeft(float left) {
		this.left = left;
	}
	public float getTop() {
		return top;
	}
	public void setTop(float top) {
		this.top = top;
	}
	public float getWight() {
		return wight;
	}
	public void setWight(float wight) {
		this.wight = wight;
	}
	public float getHeght() {
		return heght;
	}
	public void setHeght(float heght) {
		this.heght = heght;
	}
	public int getZIndex() {
		return ZIndex;
	}
	public void setZIndex(int zIndex) {
		ZIndex = zIndex;
	}
    
    
}
