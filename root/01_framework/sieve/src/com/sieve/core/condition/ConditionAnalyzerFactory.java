package com.sieve.core.condition;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ConditionAnalyzerFactory {
	private final static Logger logger = Logger
			.getLogger(ConditionAnalyzerFactory.class);

	private static Map<String, IOneAnalyzer> operatorToOneAnalyzer = new HashMap<String, IOneAnalyzer>();

	static {
		operatorToOneAnalyzer.put(">", new GreaterConditionAnalyzer());
		operatorToOneAnalyzer.put("<", new LessConditionAnalyzer());
		operatorToOneAnalyzer.put("contains", new ContainsConditionAnalyzer());
		operatorToOneAnalyzer.put("leftfaraway",
				new LeftFarAwayConditionAnalyzer());
		operatorToOneAnalyzer.put("desorderby",
				new DesOrderbyConditionAnalyzer());
		operatorToOneAnalyzer.put("ascorderby",
				new AscOrderbyConditionAnalyzer());
		operatorToOneAnalyzer.put("randomorderby",
				new RandomOrderbyConditionAnalyzer());
		operatorToOneAnalyzer.put("♀♀", new AddingConditionAnalyzer());
		operatorToOneAnalyzer.put("××", new MultipleConditionAnalyzer());
		operatorToOneAnalyzer.put("//", new DivisionConditionAnalyzer());
		operatorToOneAnalyzer.put("&&", new ConnetConditionAnalyzer());
		operatorToOneAnalyzer.put("LOG", new LogarithmConditionAnalyzer());
		operatorToOneAnalyzer.put("cddistance",
				new CurrentDateDistanceConditionAnalyzer());
	}

	public static IOneAnalyzer getOneAnalyzer(String operator)
			throws UndefinedConditionAnalyzerException {

		if (null == operatorToOneAnalyzer.get(operator)) {
			logger.error("operator=" + operator);
			throw new UndefinedConditionAnalyzerException();
		}
		return operatorToOneAnalyzer.get(operator);
	}
}
