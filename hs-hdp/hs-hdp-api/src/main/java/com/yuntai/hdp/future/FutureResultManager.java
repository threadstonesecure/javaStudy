package com.yuntai.hdp.future;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 管理FutureResult，即等待队列管理 Created by denglt on 2015/12/14.
 */
public class FutureResultManager<T> {

	private ConcurrentMap<String, FutureResult<T>> resultMap = new ConcurrentHashMap<String, FutureResult<T>>();

	/**
	 * 创建FutureResult对象
	 * 
	 * @param unique
	 * @return
	 */
	public FutureResult<T> newFutureResult(String unique) {
		FutureResult<T> futureResult = new FutureResult<T>(unique, this);
		resultMap.put(unique, futureResult);
		return futureResult;
	}

	/**
	 * 设置结果数据
	 * 
	 * @param unique
	 * @param pack
	 */
	public void setResult(String unique, T pack) {
		FutureResult<T> futureResult = resultMap.remove(unique);
		if (futureResult != null) {
			futureResult.setResult(pack);
		}
	}

	/**
	 * 删除FutureResult
	 * 
	 * @param unique
	 * @return
	 */
	public FutureResult<T> remove(String unique) {
		return resultMap.remove(unique);
	}


	public boolean contain(String unique){
		return resultMap.containsKey(unique);
	}
}