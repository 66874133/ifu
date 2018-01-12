package com.sieve.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sieve.core.analyse.ConditionAnalyzer;
import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.condition.UndefinedConditionAnalyzerException;
import com.sieve.policy.Policy;
import com.sieve.policy.PolicyService;

public class Test2 {

	private static Map<String, ConditionAnalyzer> map = new HashMap<String, ConditionAnalyzer>();

	private static String BUSINESS = "01";
	private static String INVERT = "02";
	private static String MANUAL = "03";
	private final static Logger logger = Logger.getLogger(Test2.class);

	/**
	 * @param args
	 * @throws UndefinedConditionAnalyzerException
	 */
	public static void main(String[] args)
			throws UndefinedConditionAnalyzerException {
		long s = System.currentTimeMillis();
		Map<String, String> input = new HashMap<String, String>();
		input.put("$name", "儿童玩具小汽车【人工添加】");
		input.put("$source", BUSINESS);
		input.put("$price", "26");
		input.put("$date", "2016-06-01");

		Map<String, String> input2 = new HashMap<String, String>();
		input2.put("$name", "乐高益智拼装积木城市军事火警系列消防入门套装60106儿童玩具");
		input2.put("$source", INVERT);
		input2.put("$price", "18");
		input2.put("$date", "2016-06-02");

		Map<String, String> input3 = new HashMap<String, String>();
		input3.put("$name", "儿童电动车四轮可坐人沙滩车男女宝宝电动摩托车小孩电瓶玩具汽车");
		input3.put("$source", INVERT);
		input3.put("$price", "13");
		input3.put("$date", "2016-06-03");

		Map<String, String> input4 = new HashMap<String, String>();
		input4.put("$name", "韩国进口PORORO扭扭车摇摆车宝宝滑行玩具溜溜车静音轮带音乐");
		input4.put("$source", BUSINESS);
		input4.put("$price", "30");
		input4.put("$date", "2016-06-03");

		Map<String, String> input5 = new HashMap<String, String>();
		input5.put("$name", "遥控飞机2.4G高清实时传输航拍四轴飞行器无人机直升机 儿童玩具");
		input5.put("$source", BUSINESS);
		input5.put("$price", "15");
		input5.put("$date", "2016-05-22");

		Map<String, String> input6 = new HashMap<String, String>();
		input6.put("$name", "正品disney迪士尼公主儿童游泳圈坐男女童小孩加厚宝宝腋下圈包邮");
		input6.put("$source", BUSINESS);
		input6.put("$price", "15");
		input6.put("$date", "2016-05-22");

		Map<String, String> input7 = new HashMap<String, String>();
		input7.put("$name", "韩版幼儿园双肩包1-2-3岁宝宝小书包可爱背包卡通包包男女儿童");
		input7.put("$source", BUSINESS);
		input7.put("$price", "15");
		input7.put("$date", "2016-05-22");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(input);
		list.add(input2);
		list.add(input3);
		list.add(input4);
		list.add(input5);
		list.add(input6);
		list.add(input7);

		Policy policy = new Policy();
		policy.setName("P1");
		String condition = "((($random randomorderby #random) ♀♀ ($source desorderby $source)) && (($date desorderby $date) && ($price desorderby $price))) && ($price desorderby $price)";
		policy.setCondition(condition);
		// policy.setConf("{\"name\":\"儿童\",\"price\":\"23\",\"volume\":\"10\",\"review\":\"100\"}");
		policy.setConf("{\"name\":\"儿童\",\"price\":\"23\",\"volume\":\"10\",\"review\":\"100\",\"random\":\"\"}");

		for (int j = 0; j < 100; j++) {
			ItemPolicyAnalyseResults analyseResult = PolicyService.caculate(
					list, policy);

			int count = analyseResult.getList().size();
			System.out
					.println(policy.getName() + "结果-------------------------");
			for (int i = 0; i < count; i++) {

				System.out.println(analyseResult.getList().get(i));

			}

		}

		logger.error("time = " + (System.currentTimeMillis() - s) + "ms");
	}

}
