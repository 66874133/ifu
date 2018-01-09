package com.duplex.crawler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行依赖关系
 * 
 * @author Jerry
 * 
 */
public class ExcuteDepend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 962897763761848466L;

	/**
	 * 需要处理完的Excutor
	 */
	private List<String> allExcutorKey = new ArrayList<String>();

	private Map<String, IExcuteable> keyToExcutor = new HashMap<String, IExcuteable>();

	private Map<String, List<String>> targetToSource = new HashMap<String, List<String>>();

	public IExcuteable getExcutor(String key) {
		return keyToExcutor.get(key);
	}

	public void putExcutor(String key, IExcuteable excuteable) {
		keyToExcutor.put(key, excuteable);
	}

	/**
	 * 获取可以可以处理的Excutor
	 * 
	 * @param progress
	 * @return
	 */
	public List<String> nextExcutorKeys(List<String> progress) {

		List<String> keys = getDependLessExcutorKeys();

		List<String> undo = getUndoExcutorKeys(progress);

		List<String> ret = new ArrayList<String>();

		for (String key : keys) {
			if (!progress.contains(key)) {
				ret.add(key);
			}
		}

		for (String key : undo) {
			if (!progress.contains(key)) {
				ret.add(key);
			}
		}

		return ret;

	}

	public void addDepend(String source, String target) {
		if (!allExcutorKey.contains(source)) {
			allExcutorKey.add(source);
		}

		if (!allExcutorKey.contains(target)) {
			allExcutorKey.add(target);
		}

		if (!targetToSource.containsKey(target)) {
			targetToSource.put(target, new ArrayList<String>());
		}
		targetToSource.get(target).add(source);
	}

	/**
	 * 获取没有依赖关系的执行器
	 * 
	 * @param executing
	 * @return
	 */
	public List<String> getDependLessExcutorKeys() {

		List<String> list = new ArrayList<String>();
		for (String str : allExcutorKey) {

			if (!targetToSource.containsKey(str)) {
				list.add(str);
			}
		}
		return list;
	}

	/**
	 * 获取有依赖关系的执行器
	 * 
	 * @param progress
	 * @return
	 */
	private List<String> getUndoExcutorKeys(List<String> progress) {

		List<String> undoList = new ArrayList<String>();
		for (String str : allExcutorKey) {

			// 是未处理的ExcutorKey
			if (!progress.contains(str)) {

				// 判断该Excutor是否可以进行处理 检查依赖的Excutor是否已经完成
				List<String> list = targetToSource.get(str);
				if (null != list && progress.containsAll(list)) {
					undoList.add(str);
				}

			}
		}

		return undoList;

	}

	public IExcuteable selectExcutor(List<String> excutorKey, List<String> list) {

		// Random random = new Random();

		for (String key : excutorKey) {
			if (!list.contains(key)) {
				return keyToExcutor.get(key);
			}
		}
		return null;

	}

	public boolean isFinished(List<String> strs) {
		for (String key : allExcutorKey) {
			if (!strs.contains(key)) {
				return false;
			}
		}
		return true;
	}

	public List<String> getAllExcutorKey() {
		return allExcutorKey;
	}

	public ExecuteStep getNextStep(int i) {

		for (ExecuteStep step : getSteps()) {
			if (step.index == i + 1) {
				return step;
			}
		}
		return new ExecuteStep(new ArrayList<String>());

	}

	public List<ExecuteStep> getSteps() {

		List<String> list = new ArrayList<String>();

		List<ExecuteStep> executeSteps = new ArrayList<ExecuteStep>();
		int i = 0;
		while (!list.containsAll(allExcutorKey)) {

			List<String> temp = nextExcutorKeys(list);

			ExecuteStep step = new ExecuteStep(temp);
			step.index = i++;
			executeSteps.add(step);

			list.addAll(temp);

		}
		return executeSteps;

		// {3=[2], 2=[0, 1], 5=[4], 4=[2]}
	}

	public void setAllExcutorKey(List<String> allExcutorKey) {
		this.allExcutorKey = allExcutorKey;
	}

	public static void main(String[] args) {

		ExcuteDepend depend = ExcuteDependConfig.fromFile("depend.xml")
				.getExcuteDepend();

		depend.getSteps();

		List<String> progress = new ArrayList<String>();
		progress.add("1");
		progress.add("2");

		List<String> list = new ArrayList<String>();
		progress.add("2");

		System.out.println(depend.nextExcutorKeys(progress));

		System.out.println(depend.selectExcutor(depend
				.nextExcutorKeys(progress), list));

	}
}
