package com.yuntai.hdp.gateway.service;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.service.DowndataHandler;
import org.springframework.stereotype.Service;

@Service("downdataHandler")
public class GateWayDowndataHandler implements DowndataHandler {

    @Override
    public Boolean downData(RequestPack request) {
        // TODO: 2018/6/20 转发到hdpServer
        return null;
    }
}
