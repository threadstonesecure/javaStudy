package com.yutai.hdp.gateway.accesshdpserver;

import com.alibaba.dubbo.rpc.cluster.Merger;

/**
 * Created by denglt on 2016/1/27.
 */
public class BooleanMerger implements Merger<Boolean> {
	@Override
	public Boolean merge(Boolean... items) {
		System.out.println("coming in com.yuntai.hdp.access.BooleanMerger");
		if (items.length == 0)
			return false;
		if (items.length == 1)
			return items[0];

		for (Boolean item : items) {
			if (item.booleanValue())
				return true;
		}

		return false;
	}
}
