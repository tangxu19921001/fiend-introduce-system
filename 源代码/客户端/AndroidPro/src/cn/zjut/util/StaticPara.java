package cn.zjut.util;

public  class StaticPara {
	public static String SERVICE_URL="http://192.168.1.102:8080/AndroidProService2/";
	public static int doregister(String name,String pa,String pa2){
		if(name.trim().equals("")||pa.trim().equals("")||pa2.trim().equals("")){
			return 0;//请输入完整
		}
		else{
			if(!pa.equals(pa2)){
				return -1;//两次输入不同
			}
			return 1;
		}
	}
}
