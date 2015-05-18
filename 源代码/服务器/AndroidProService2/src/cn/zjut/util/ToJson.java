package cn.zjut.util;

import java.util.List;

import cn.zjut.entity.Location;
import cn.zjut.entity.User;

public interface ToJson {
	String toJson(int i,List<User> l,List<Integer> score,List<String> pt);
	String toLocationJson(List<Location> lo1,List<Location> lo2);
}
