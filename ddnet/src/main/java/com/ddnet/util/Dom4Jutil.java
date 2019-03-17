package com.ddnet.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author pandora
 *
 */
@Slf4j
public class Dom4Jutil {

	public static Document getDocument(String path){
		SAXReader reader = new SAXReader();
		try {
//System.out.println(new File(path).getAbsolutePath());
			log.info("配置文件加载:{}",new File(path).getAbsolutePath());
			return reader.read(new File(path));
		} catch (DocumentException e) {
			log.info("error in getDocument:{}",e);
		}
		return null;
	}
	public static boolean update(Document doc,String path){
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		try {
			XMLWriter writer = new XMLWriter(new FileWriter(path), format);
			writer.write(doc);
			writer.close();
			return true;
		} catch (IOException e) {
			log.info("error in update for Dom4J:{}",e);
		}
		return false;
	}
}
