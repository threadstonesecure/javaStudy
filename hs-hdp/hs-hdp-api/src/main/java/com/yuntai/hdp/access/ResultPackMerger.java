package com.yuntai.hdp.access;

import com.alibaba.dubbo.rpc.cluster.Merger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by denglt on 2016/1/26.
 */
public class ResultPackMerger implements Merger<ResultPack> {
	@Override
	public ResultPack merge(ResultPack... items) {
        System.out.println("coming in com.yuntai.hdp.access.ResultPackMerger");
        if (items.length == 0) {
            return null;
        }
		if (items.length == 1)
			return items[0];
		Map<String, ResultPack> mapResult = new HashMap<>();
		for (ResultPack item : items) {
			mapResult.put(item.getKind(), item);
		}
		ResultPack result;
		result = mapResult.remove(ResultKind.OK.getKind());
		if (result != null) {
			return result;
		}
		result = mapResult.remove(ResultKind.ERROR_NOEXISTS_HOSP.getKind());
		if (mapResult.isEmpty()) {
			return result;
		}
		return mapResult.values().toArray(new ResultPack[0])[0];
	}
}
