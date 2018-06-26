package com.yuntai.hdp.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class NodeConfig {

    private static Log log = LogFactory.getLog(NodeConfig.class);

    @Value("${hdpserver.node.type}")
    private String nodeType;

    @Value("${hdpserver.node.position}")
    private String nodePosition;

    @PostConstruct
    public void init() {
        log.info(String.format("hdpServer节点配置-> %s %s ", nodeType, nodePosition));

        switch (NodeType.getEnum(nodeType)) {
            case NORMAL:
                isCascade = false;
                break;
            case CASCASDE:
                isCascade = true;
                break;
            default:
                isCascade = false;
        }
        if (isCascade) {
            switch (NodePosition.getEnum(nodePosition)) {
                case NEAR_YUN:
                    isNearYun = true;
                    break;
                case NEAR_HOS:
                    isNearHosp = true;
                    break;
                default:
                    throw new IllegalArgumentException("hdpServer级联模式下参数{hdpserver.node.position}配置错误！");
            }
        }
    }

    public boolean isToHosByCascade() {
        return isCascade && isNearYun;
    }

    public boolean isToYunServiceByCascade() {
        return isCascade && isNearHosp;
    }

    private boolean isCascade = false;

    private boolean isNearYun = false;

    private boolean isNearHosp = false;

    private enum NodeType {
        NORMAL("normal"), CASCASDE("cascade");
        private static final Map<String, NodeType> code_map = new HashMap<String, NodeType>();

        static {
            for (NodeType typeEnum : NodeType.values()) {
                code_map.put(typeEnum.nodeType, typeEnum);
            }
        }

        NodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public static NodeType getEnum(String code) {
            return code_map.get(code);
        }

        private String nodeType;
    }

    private enum NodePosition {
        NEAR_YUN("near_yunservice"), NEAR_HOS("near_hospital");
        private static final Map<String, NodePosition> code_map = new HashMap<String, NodePosition>();

        static {
            for (NodePosition typeEnum : NodePosition.values()) {
                code_map.put(typeEnum.position, typeEnum);
            }
        }

        NodePosition(String position) {
            this.position = position;
        }

        public static NodePosition getEnum(String code) {
            return code_map.get(code);
        }

        private String position;
    }
}
