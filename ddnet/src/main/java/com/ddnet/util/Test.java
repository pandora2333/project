package com.ddnet.util;
import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Test {

	public static void main(String[] args) {
//		List<Integer> list1 = new ArrayList<>();
//		List<Integer> list2 = new ArrayList<>();
//		for(int i = 0 ;i<5;++i){
//			list1.add(i);
//		}
//		for(int i = 5 ;i<8;++i){
//			list2.add(i);
//		}
//		StringBuilder sb = new StringBuilder("");
//		int index = 0;
//		for(;index< min(list1.size(),list2.size());++index){
////			if(!visited1[index])
//				sb.append(list1.get(index)+" ");
////			if(!visited2[index])
//				sb.append(list2.get(index)+" ");
//		}
//		for(int next = index;next < list1.size();++next)
////			if(!visited1[index])
//				sb.append(list1.get(next)+" ");
//		for(int next = index;next < list2.size();++next)
////			if(!visited2[index])
//				sb.append(list2.get(next)+" ");
//		System.out.println(sb);
		String test="1x2 3x4 5x6";
		Pattern pattern = Pattern.compile("(\\d)x(\\d)",Pattern.DOTALL);
		Matcher matcher = pattern.matcher(test);
		while(matcher.find())
			System.out.println("index:"+matcher.start()+"->"+matcher.group(2));
	}
	
}
