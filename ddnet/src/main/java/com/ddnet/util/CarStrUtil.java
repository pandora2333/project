package com.ddnet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

import com.ddnet.pojo.Car;
import com.ddnet.pojo.Order;
import com.mysql.cj.util.StringUtils;

/**
 * 解析CarStr字符串
 * @author pandora
 *
 */
@Slf4j
public class CarStrUtil {
	
	public static Car parseStr(String carStr,String username){//1x2 2x3 3x4 -> Car
		
		Car car = new Car();
		car.setUser_name(username);
		List<Order> list = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(carStr, " ");//[1x2,2x3,3x4]
		while(st.hasMoreTokens()){
			Order order = new Order();
			String[] strs = st.nextToken().split("x");
			order.setGoods_id(Integer.parseInt(strs[0]));
			order.setBuy_count(Integer.parseInt(strs[1]));
			list.add(order);
		}
		car.setOrders(list);
		return car;
	}
	
	
	public static String parseCar(Car car){//  Car -> 1x2 2x3 3x4
		if(car==null) return null;
		StringBuilder sb = new StringBuilder("");
		for(Order order:car.getOrders())
			sb.append(order.getGoods_id()+"x"+order.getBuy_count()+" ");
		return sb.toString().trim();
	}
	//o(n*n),o(n),使用字符串拆分模式
	public static String fixedStr(String originStr,int code,String destStr){//1x2 2x3 3x4 -> 0 增加数量，1 减少数量
		if(StringUtils.isNullOrEmpty(originStr)) return destStr;
		StringBuilder sb = new StringBuilder("");
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		StringTokenizer st1 = new StringTokenizer(originStr, " ");
		StringTokenizer st2 = new StringTokenizer(destStr, " ");
		while(st1.hasMoreTokens()&& st2.hasMoreElements()){
			list1.add(st1.nextToken());
			list2.add(st2.nextToken());
		}
		while(st1.hasMoreTokens()){
			list1.add(st1.nextToken());
		}
		while(st2.hasMoreTokens()){
			list2.add(st2.nextToken());
		}
		boolean visited1[] = new boolean[list1.size()];//list1中元素访问记录
		boolean visited2[] = new boolean[list2.size()];//list2中元素访问记录
		for(int index =0;index < list1.size();++index){
			String str1 = list1.get(index);
			String[] tmp1 = null;
			String[] tmp2 = null;
			if(index > list2.size()) break;
			for(int cur = 0;cur<list2.size();++cur){
				tmp1 = str1.split("x");
				tmp2 = list2.get(cur).split("x");
				if(visited2[cur])continue;//访问过就不进行下面操作
				if(tmp1[0].equals(tmp2[0])){
					switch(code){
						case 0:
							sb.append(tmp1[0]+"x"+(Integer.valueOf(tmp1[1])+Integer.valueOf(tmp2[1]))+" ");
							visited2[cur]=true;;
							visited1[index]=true;
							break;
						case 1:
							int num = Integer.valueOf(tmp1[1])-Integer.valueOf(tmp2[1]);
							if(num>0)
								sb.append(tmp1[0]+"x"+num+" ");
							visited2[cur]=true;
							visited1[index]=true;
							break;
						default:throw new RuntimeException("非法carStr in  carstrutil");
					}
					break;
				}
				}

		}
		
		if(code==0)
			for(int index=0;index<list2.size();++index)
				if(!visited2[index])
					sb.append(list2.get(index)+" ");
		for(int index=0;index<list1.size();++index)
			if(!visited1[index])
				sb.append(list1.get(index)+" ");
		return sb.toString().trim() ;
	}
	//删除购物车中的标记商品，使用正则快速匹配 o(n*n) o(n)
	public static String delStr(String originStr,String destStr){
		Pattern pattern = Pattern.compile("(\\d)x(\\d)",Pattern.DOTALL);
		Matcher matcher = pattern.matcher(destStr);
		List<String> list1 = new ArrayList<String>();
		List<String> count = new ArrayList<String>();//存储购物车中商品数量
		List<String> list2 = new ArrayList<String>();//存储删除商品id
		StringBuilder sb = new StringBuilder("");
		while(matcher.find())
			list2.add(matcher.group(1));
		matcher  = pattern.matcher(originStr);
		while(matcher.find()){
			list1.add(matcher.group(1));
			count.add(matcher.group(2));
		}
		for(int index = 0;index < list1.size();++index)
			if(!list2.contains(list1.get(index)))
				sb.append(list1.get(index)+"x"+count.get(index)+" ");
//		System.out.println(sb);
		log.info("del {}",sb);
		return sb.toString().trim();
	}

}
