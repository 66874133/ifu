package com.sieve.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sieve.core.analyse.ConditionAnalyzer;
import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.core.condition.UndefinedConditionAnalyzerException;
import com.sieve.policy.Policy;
import com.sieve.policy.PolicyService;

public class Test3 {

	private static Map<String, ConditionAnalyzer> map = new HashMap<String, ConditionAnalyzer>();

	private static String BUSINESS = "01";
	private static String INVERT = "02";
	private static String MANUAL = "03";

	/**
	 * @param args
	 * @throws UndefinedConditionAnalyzerException
	 */
	public static void main(String[] args)
			throws UndefinedConditionAnalyzerException {
		long s = System.currentTimeMillis();
		Map<String, String> input = new HashMap<String, String>();
		input.put("$name", "01 儿童玩具小汽车【人工添加】");
		input.put("$source", BUSINESS);
		input.put("$price", "19.9");
		input.put("$saleCount", "2612");
		input.put("$cidSaleCount", "0");
		input.put("$allSaleCount", "536");
		input.put("$commentCount", "9974");
		input.put("$date", "2016-07-01 01:22:33");

		Map<String, String> input2 = new HashMap<String, String>();
		input2.put("$name", "02 乐高益智拼装积木城市军事火警系列消防入门套装60106儿童玩具");
		input2.put("$source", INVERT);
		input2.put("$price", "39.9");
		input2.put("$saleCount", "18333");
		input2.put("$commentCount", "974");
		input2.put("$date", "2016-07-01 01:22:33");
		input2.put("$cidSaleCount", "188");
		input2.put("$allSaleCount", "536");

		Map<String, String> input3 = new HashMap<String, String>();
		input3.put("$name", "03 儿童电动车四轮可坐人沙滩车男女宝宝电动摩托车小孩电瓶玩具汽车");
		input3.put("$source", INVERT);
		input3.put("$price", "673");
		input3.put("$saleCount", "133333");
		input3.put("$commentCount", "84");
		input3.put("$date", "2016-07-01 01:22:33");
		input3.put("$cidSaleCount", "49");
		input3.put("$allSaleCount", "536");

		Map<String, String> input4 = new HashMap<String, String>();
		input4.put("$name", "04 韩国进口PORORO扭扭车摇摆车宝宝滑行玩具溜溜车静音轮带音乐");
		input4.put("$source", BUSINESS);
		input4.put("$price", "3873");
		input4.put("$saleCount", "300");
		input4.put("$commentCount", "184");
		input4.put("$date", "2016-07-22 01:22:33");
		input4.put("$cidSaleCount", "49");
		input4.put("$allSaleCount", "536");

		Map<String, String> input5 = new HashMap<String, String>();
		input5.put("$name", "05 遥控飞机2.4G高清实时传输航拍四轴飞行器无人机直升机 儿童玩具");
		input5.put("$source", BUSINESS);
		input5.put("$price", "873");
		input5.put("$saleCount", "15");
		input5.put("$commentCount", "4");
		input5.put("$date", "2016-07-22 01:22:33");
		input5.put("$cidSaleCount", "132");
		input5.put("$allSaleCount", "536");

		Map<String, String> input6 = new HashMap<String, String>();
		input6.put("$name", "06 正品disney迪士尼公主儿童游泳圈坐男女童小孩加厚宝宝腋下圈包邮");
		input6.put("$source", BUSINESS);
		input6.put("$saleCount", "874");
		input6.put("$price", "158");
		input6.put("$commentCount", "291");
		input6.put("$date", "2016-07-22 01:22:33");
		input6.put("$cidSaleCount", "132");
		input6.put("$allSaleCount", "536");

		Map<String, String> input7 = new HashMap<String, String>();
		input7.put("$name", "07 韩版幼儿园双肩包1-2-3岁宝宝小书包可爱背包卡通包包男女儿童");
		input7.put("$source", BUSINESS);
		input7.put("$price", "15");
		input7.put("$saleCount", "2274");
		input7.put("$commentCount", "1291");
		input7.put("$date", "2016-07-22 01:22:33");
		input7.put("$cidSaleCount", "72");
		input7.put("$allSaleCount", "536");

		Map<String, String> input8 = new HashMap<String, String>();
		input8.put("$name", "08 卡通包包韩版幼儿园");
		input8.put("$source", BUSINESS);
		input8.put("$price", "151");
		input8.put("$saleCount", "674");
		input8.put("$commentCount", "191");
		input8.put("$date", "2016-07-22 01:22:33");
		input8.put("$cidSaleCount", "0");
		input8.put("$allSaleCount", "536");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		list.add(input);
		list.add(input2);
		list.add(input3);
		list.add(input4);
		list.add(input5);
		list.add(input6);
		list.add(input7);
		list.add(input8);

		Policy policy = new Policy();
		policy.setName("P1");
		// String condition =
		// "($date cddistance #ddistance) && (($saleCount LOG #Xb) ×× ($commentCount LOG #Xa))";
		// String condition =
		// "($date cddistance #ddistance) && (($saleCount ♀♀ #Xb) LOG ($commentCount LOG #Xa))";
		String condition = "($date cddistance #ddistance) && (((($saleCount ♀♀ #Xb) LOG (($commentCount ♀♀ #Xb) LOG #Xa)) ×× #Xd) ×× (($cidSaleCount // ($allSaleCount ♀♀ #Xb)) ♀♀ #defaultRate))";
		policy.setCondition(condition);
		// policy.setConf("{\"name\":\"儿童\",\"price\":\"23\",\"volume\":\"10\",\"review\":\"100\"}");
		policy.setConf("{\"ddistance\":\"72\",\"random\":\"\",\"pweighting\":\"0.5\",\"Xa\":\"2\",\"Xb\":\"10\",\"Xc\":\"1\",\"Xd\":\"100\",\"defaultRate\":\"0.20\"}");

		for (int j = 0; j < 10; j++) {
			ItemPolicyAnalyseResults analyseResult = PolicyService.caculate(
					list, policy);

			int count = analyseResult.getList().size();
			System.out
					.println(policy.getName() + "结果-------------------------");
			for (int i = 0; i < count; i++) {

				System.out.println(analyseResult.getList().get(i));

			}

		}

		System.out.println("time = " + (System.currentTimeMillis() - s) + "ms");
	}

}
