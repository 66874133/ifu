package com.funnel.smarker;

import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Template;

public class TestString {

	/**
	 * @param args
	 */
	 public static void main(String[] args) throws Exception{ 
         //����һ��ģ����� 
         Template t = new Template(null, new StringReader("�û�����${user};URL��    ${url};������ ��${name}"), null); 
         //������ֵ��Map 
         Map map = new HashMap(); 
         map.put("user", "lavasoft"); 
         map.put("url", "http://www.baidu.com/"); 
         map.put("name", "�ٶ�"); 
         //ִ�в�ֵ���������ָ����������� 
         t.process(map, new OutputStreamWriter(System.out)); 
 } 

}
