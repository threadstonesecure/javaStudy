package dlt.study.fulltext.elasticsearch;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @Description:
 * @Package: dlt.study.fulltext.elasticsearch
 * @Author: denglt
 * @Date: 2018/10/24 11:34 AM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class RoutingDemo {
    /**
     *  1、put 的时候指定了routing, delete , get , update 也要指定routing，否则找不到数据
     *  2、不同routing，_id可以重复
     *
     */
    @Test
    public void insert() throws Exception{
        XContentBuilder xContentBuilder = jsonBuilder()
                .startObject()
                .field("name.cn", "邓隆通")
                .field("name.en", "deng long tong")
                .field("title", "java架构师、oracleDBA、oracle数据库专家")
                .field("age", 42)
                .field("birthday", "1976-04-16")
                .field("message", "测试elasticsearch 2")
                .field("address", "广州市天河区华观1398号A7-402房路")
                .field("address_no", "广州市天河区华观路1398号A7-402房")
                .endObject();
        IndexResponse indexResponse = ClientHelper.getClient().prepareIndex("test", "_doc", "99")
                .setRouting("1").setSource(xContentBuilder).get();
        System.out.println(indexResponse);
        indexResponse = ClientHelper.getClient().prepareIndex("test", "_doc", "99")
                .setRouting("2").setSource(xContentBuilder).get();
        System.out.println(indexResponse);

        /*
IndexResponse[index=test,type=_doc,id=99,version=1,result=created,seqNo=9,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
IndexResponse[index=test,type=_doc,id=99,version=1,result=created,seqNo=8,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]
         */
    }

    @Test
    public void get() throws Exception {
        GetResponse doc = ClientHelper.getClient().prepareGet("test", "_doc", "99").setRouting("1").get();
        System.out.println(doc);
        doc = ClientHelper.getClient().prepareGet("test", "_doc", "99").setRouting("2").get();
        System.out.println(doc);
        doc = ClientHelper.getClient().prepareGet("test", "_doc", "99").setRouting("3").get();
        System.out.println(doc);
    }

    @Test
    public void delete() throws Exception {
        DeleteRequest deleteIndex = new DeleteRequest("test", "_doc", "99").routing("1");
        ActionFuture<DeleteResponse> actionFuture = ClientHelper.getClient().delete(deleteIndex);
        System.out.println(actionFuture.get());
    }

    @Test
    public void update() throws Exception {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("test");
        updateRequest.type("_doc");
        updateRequest.id("99");
        updateRequest.routing("1"); // 必须
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .field("name.cn", "")
                .endObject());
        UpdateResponse updateResponse = ClientHelper.getClient().update(updateRequest).get();
        System.out.println(updateResponse);
    }
}
