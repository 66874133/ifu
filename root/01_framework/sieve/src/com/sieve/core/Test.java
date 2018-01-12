package com.sieve.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sieve.core.analyse.result.ItemPolicyAnalyseResults;
import com.sieve.policy.Policy;
import com.sieve.policy.PolicyService;

public class Test {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		long s = System.currentTimeMillis();
		Map<String, String> input = new HashMap<String, String>();
		input.put("$name", "儿童玩具小汽车【人工添加】");
		input.put("$price", "20");
		input.put("$volume", "14265");
		input.put("$review", "796");
		input.put("$date", "2016-06-01");
		input.put("$sourcelevel", "99");

		Map<String, String> input2 = new HashMap<String, String>();
		input2.put("$name", "乐高益智拼装积木城市军事火警系列消防入门套装60106儿童玩具");
		input2.put("$price", "15");
		input2.put("$volume", "1870");
		input2.put("$review", "347");
		input2.put("$date", "2016-06-02");
		input2.put("$sourcelevel", "01");

		Map<String, String> input3 = new HashMap<String, String>();
		input3.put("$name", "儿童电动车四轮可坐人沙滩车男女宝宝电动摩托车小孩电瓶玩具汽车");
		input3.put("$price", "220");
		input3.put("$volume", "130");
		input3.put("$review", "122");
		input3.put("$date", "2016-06-03");
		input3.put("$sourcelevel", "11");

		Map<String, String> input4 = new HashMap<String, String>();
		input4.put("$name", "韩国进口PORORO扭扭车摇摆车宝宝滑行玩具溜溜车静音轮带音乐");
		input4.put("$price", "120");
		input4.put("$volume", "30");
		input4.put("$review", "118");
		input4.put("$date", "2016-06-03");
		input4.put("$sourcelevel", "23");

		Map<String, String> input5 = new HashMap<String, String>();
		input5.put("$name", "遥控飞机2.4G高清实时传输航拍四轴飞行器无人机直升机 儿童玩具");
		input5.put("$price", "119");
		input5.put("$volume", "15");
		input5.put("$review", "155");
		input5.put("$date", "2016-05-22");
		input5.put("$sourcelevel", "66");

		Map<String, String> input6 = new HashMap<String, String>();
		input6.put("$name", "正品disney迪士尼公主儿童游泳圈坐男女童小孩加厚宝宝腋下圈包邮");
		input6.put("$price", "200");
		input6.put("$volume", "15");
		input6.put("$review", "997");
		input6.put("$date", "2016-05-22");
		input6.put("$sourcelevel", "66");

		Map<String, String> input7 = new HashMap<String, String>();
		input7.put("$name", "韩版幼儿园双肩包1-2-3岁宝宝小书包可爱背包卡通包包男女儿童");
		input7.put("$price", "235");
		input7.put("$volume", "15");
		input7.put("$review", "32654");
		input7.put("$date", "2016-05-22");
		input7.put("$sourcelevel", "66");

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
		String condition = "($review > #review && $sourcelevel desorderby $sourcelevel) && ($name contains #name&&$price < #price && $volume > #volume)";
		policy.setCondition(condition);
		policy.setConf("{\"name\":\"儿童\",\"price\":\"23\",\"volume\":\"10\",\"review\":\"100\",\"sourcelevelweight\":\"3\"}");

		Policy policy2 = new Policy();
		policy2.setName("P2");
		String condition2 = "($review > #review) && ($price leftfaraway #price && $date desorderby $date)";
		policy2.setCondition(condition2);
		policy2.setConf("{\"name\":\"儿童\",\"price\":\"299\",\"volume\":\"10\",\"review\":\"100\"}");

		List<Policy> policys = new ArrayList<Policy>();
		policys.add(policy);
		policys.add(policy2);

		String combineExpression = "P1 ∪ P2";
		// String condition =
		// "d==2||a==2&&(b<1||(c>1&&d>1||e!=1))&&e<0&&(b<1||(c>1&&d>1))&&e<1";

		ItemPolicyAnalyseResults analyseResult = PolicyService.caculate(list,
				policy);

		int count = analyseResult.getList().size();
		System.out.println(policy.getName() + "结果-------------------------");
		for (int i = 0; i < count; i++) {

			System.out.println(analyseResult.getList().get(i));

		}

		ItemPolicyAnalyseResults analyseResult2 = PolicyService
				.combineCaculate(list, policys, combineExpression);

		System.out.println("analyseResult2=" + analyseResult2);

		int count2 = analyseResult2.getList().size();
		System.out.println("-----合并结果-------------------------");
		for (int i = 0; i < count2; i++) {

			System.out.println(analyseResult2.getList().get(i));

		}

		System.out.println("time = " + (System.currentTimeMillis() - s));
	}

}
