package dlt.study.fulltext.elasticsearch;

import com.google.common.collect.Maps;
import org.apache.lucene.index.Terms;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.*;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * @Description:
 * @Package: dlt.study.fulltext.elasticsearch
 * @Author: denglt
 * @Date: 2018/10/12 7:23 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class TransportClientDemo {

    private String host1 = "47.106.93.69";
    private int port = 9300;

    private String indexName = "test";
    private TransportClient client;

    @Before
    public void connect() throws Exception {
        // on startup
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host1), port));
        //  .addTransportAddress(new TransportAddress(InetAddress.getByName("host2"), 9300)); // cluster

    }

    @After
    public void close() {
        client.close();
    }

    @Test
    public void recreateIndex() {
        try {
            deleteIndex();
            System.out.println("delete index ok!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            createIndex();
            System.out.println("create index ok!");
            modifyMapping();
            System.out.println("modify mapping ok!");
            insertDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createIndex() throws Exception {
        Map<String, Object> mappings = Maps.newHashMap();
        mappings.put("message", "type=text,analyzer=ik_max_word");
        mappings.put("name", "type=text,analyzer=standard");

        XContentBuilder xContentBuilder = jsonBuilder();
        xContentBuilder.startObject()
                .field("dynamic", "false")
                .startObject("properties")
                .startObject("message").field("type", "text").field("analyzer", "DP").endObject()
                .startObject("name.en").field("type", "text").field("analyzer", "standard").endObject()
                .startObject("age").field("type", "integer")/*.field("analyzer", "standard")*/.endObject()
                .startObject("title").field("type", "text").endObject()
                .endObject()
                .endObject();
        System.out.println(Strings.toString(xContentBuilder));
        CreateIndexResponse createIndexResponse = client.admin().indices().prepareCreate(indexName)
                // .addMapping("_doc", "message", "type=text,analyzer=ik_max_word") // ok
                //.addMapping("_doc", mappings) error
                .addMapping("_doc", xContentBuilder)  // ok
                .setSettings(Settings.builder().put("index.analysis.analyzer.default.type", "ik_max_word")
                        .put("index.number_of_replicas", 1))
                //.addAlias()
                //.addCustom()
                .get();

        System.out.println(createIndexResponse);

        CreateIndexRequest request = new CreateIndexRequest(indexName);
        client.admin().indices().create(request);
    }

    /**
     * .
     * The PUT mapping API also allows to specify the mapping of a type after index creation
     */
    @Test
    public void modifyMapping() throws Exception {
        XContentBuilder xContentBuilder = jsonBuilder();
        xContentBuilder.startObject()
                .startObject("properties")
                .startObject("name.cn").field("type", "text").field("analyzer", "ik_max_word").endObject()
                .startObject("sex").field("type", "integer")/*.field("analyzer", "standard")*/.endObject()
                .startObject("address").field("type", "text").field("index", true).endObject()
                .startObject("address_no").field("type", "text").field("index", false).endObject() // 不能进行查询
                .startObject("birthday").field("type", "date").endObject()
                .endObject()
                .endObject();
        //System.out.println(Strings.toString(xContentBuilder));
        client.admin().indices().preparePutMapping(indexName)
                .setType("_doc")
                .setSource(xContentBuilder)
                .get();
    }

    @Test
    public void deleteIndex() throws Exception {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        AdminClient adminClient = client.admin();
        ActionFuture<DeleteIndexResponse> actionFuture = adminClient.indices().delete(deleteIndexRequest);
        System.out.println(actionFuture.get());

    }


    /**
     * 可以反复执行，达到修改数据目的( before delete after insert)
     *
     * @throws Exception
     */
    @Test
    public void insertDocument() throws Exception {
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
        IndexResponse indexResponse = client.prepareIndex(indexName, "_doc", "1").setSource(xContentBuilder).get();
        System.out.println(indexResponse);
        xContentBuilder = jsonBuilder()
                .startObject()
                .field("name.cn", "张三")
                .field("name.en", "zhang san")
                .field("title", "java架构师、oracleDBA、oracle数据库专家")
                .field("age", 30)
                .field("birthday", "1976-04-16")
                .field("message", "测试elasticsearch 2")
                .field("address", "天河区华观路1398号A7-402房")
                .field("address_no", "广州市天河区华观路1398号A7-402房")
                .endObject();
        indexResponse = client.prepareIndex(indexName, "_doc", "2").setSource(xContentBuilder).get();

        System.out.println(indexResponse);

        xContentBuilder = jsonBuilder()
                .startObject()
                .field("name.cn", "李四")
                .field("name.en", "li si")
                .field("title", "java架构师、oracleDBA、oracle数据库专家")
                .field("age", 30)
                .field("birthday", "1976-04-16")
                .field("message", "测试elasticsearch 2")
                .field("address", "天河区华观路1398号A7-402房")
                .field("address_no", "广州市天河区华观路1398号A7-402房")
                .endObject();
        indexResponse = client.prepareIndex(indexName, "_doc", "3").
                setSource(Strings.toString(xContentBuilder), XContentType.JSON).get();
        System.out.println(indexResponse);

        //client.index(IndexRequset)
    }

    /**
     * 更新个别字段
     */
    @Test
    public void updateDocument() throws Exception {
        // client.prepareUpdate((indexName, "_doc", "1").setDoc()

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);
        updateRequest.type("_doc");
        updateRequest.id("1");
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .field("name.cn", "")
                .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();
        System.out.println(updateResponse);
    }

    /**
     * 查询更新
     *
     * @throws Exception
     */
    @Test
    public void updateByQuery() throws Exception {
        UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        BulkByScrollResponse bulkByScrollResponse = updateByQuery.source(indexName)
                .filter(QueryBuilders.termQuery("address", "华观"))
                .size(1000)
                .script(new Script(ScriptType.INLINE, "painless", "ctx._source.age = ctx._source.age * 2", Collections.emptyMap()))
                .get();
        System.out.println(bulkByScrollResponse);
    }


    @Test
    public void deleteDocument() {
        DeleteRequest deleteIndex = new DeleteRequest(indexName, "_doc", "1");
        ActionFuture<DeleteResponse> actionFuture = client.delete(deleteIndex);
        DeleteResponse deleteResponse = actionFuture.actionGet();
        System.out.println(deleteResponse);

        DeleteResponse deleteResponse2 = client.prepareDelete("test", "_doc", "1").get();
        System.out.println(deleteResponse2);
    }

    @Test
    public void deleteByQuery() {
        BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .source(indexName)
                .filter(QueryBuilders.matchQuery("address", "广州华观路"))
                .get();
        System.out.println(bulkByScrollResponse);

    }

    @Test
    public void getDocument() {
        GetResponse doc = client.prepareGet(indexName, "_doc", "1") //  client.prepareMultiGet()
                .get();
        System.out.println(doc);
        // Object value = doc.getField("_source");

        System.out.println(doc.getVersion());
        System.out.println(doc.getSource().get("birthday").getClass());
        System.out.println(doc.getSource().get("age").getClass());

    }



    @Test
    public void bulkApi() throws Exception {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequestBuilder.add(client.prepareIndex("twitter", "tweet", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
        );

        bulkRequestBuilder.add(client.prepareIndex("twitter", "tweet", "2")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "another post")
                        .endObject()
                )
        );

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);
        updateRequest.type("_doc");
        updateRequest.id("4");
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .field("name.cn", "")
                .endObject());
        bulkRequestBuilder.add(updateRequest);

        BulkResponse bulkResponse = bulkRequestBuilder.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            bulkResponse.forEach(r -> {
                if (r.isFailed()) System.out.println(r.getFailure());
            });
        }
    }


    @Test
    public void bulkProcessor() throws Exception {
        BulkProcessor bulkProcessor = getBulkProcessor(client);
        IndexRequest indexRequest = new IndexRequest("twitter", "tweet", "1");
        indexRequest.source(jsonBuilder()
                .startObject()
                .field("user", "kimchy")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch")
                .endObject()
        );
        bulkProcessor.add(indexRequest);

        indexRequest = new IndexRequest("twitter", "tweet", "2");
        indexRequest.source(jsonBuilder()
                .startObject()
                .field("user", "kimchy")
                .field("postDate", new Date())
                .field("message", "another post")
                .endObject());
        bulkProcessor.add(indexRequest);

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(indexName);
        updateRequest.type("_doc");
        updateRequest.id("4");
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("gender", "male")
                .field("name.cn", "")
                .endObject());
        bulkProcessor.add(updateRequest);
        bulkProcessor.flush();
        // bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
        bulkProcessor.close();

        // Refresh your indices
        client.admin().indices().prepareRefresh().get();
    }

    private static BulkProcessor getBulkProcessor(Client client) {
        return BulkProcessor.builder(
                client,
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                        System.out.println(String.format("BulkProcessor [%d] begin ,  number of actions -> %d", executionId, request.numberOfActions()));
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                        System.out.println("BulkProcessor end;");
                        if (response.hasFailures()) {
                            System.out.println("follow actions is failure:");
                            response.forEach(r -> {
                                if (r.isFailed()) System.out.println(r.getFailure());
                            });
                        }
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        System.out.println("BulkProcessor end  because of Throwable : " + failure);
                    }
                })
                .setBulkActions(10000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    @Test
    public void reindx() {
        BulkByScrollResponse response = ReindexAction.INSTANCE.newRequestBuilder(client)
                .source(indexName)
                .destination("target_test")
                //.abortOnVersionConflict()
                .filter(QueryBuilders.matchQuery("address", "华观路"))
                .get();
        System.out.println(response);
    }

    @Test
    public void search() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("address", "华观路"));  // and
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("age").from(30).to(100));
        // boolQueryBuilder.should()  or
        // boolQueryBuilder.mustNot() not
        //  boolQueryBuilder.must(QueryBuilders.termQuery("age",42));
        // QueryBuilders.matchPhraseQuery() // 短语
        System.out.println(Strings.toString(boolQueryBuilder));

        SearchResponse response = client.prepareSearch(indexName)
                .setTypes("_doc")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)                 // Query
                //  .setPostFilter(QueryBuilders.rangeQuery("age").from(40).to(100))     // Filter
                .setFrom(0).setSize(60).setExplain(true)
                //.highlighter()  高亮
                .addSort("age", SortOrder.DESC)
                .get();
        System.out.println(response);
        System.out.println("count -> " + response.getHits().getTotalHits());
        SearchHit[] searchHits = response.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            System.out.println(searchHit.getSourceAsMap());
        }
    }

    @Test
    public void scrollSearch() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("address", "华观路"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("age").from(30).to(100));
        //  boolQueryBuilder.must(QueryBuilders.termQuery("age",42));

        System.out.println(Strings.toString(boolQueryBuilder));

        SearchResponse scrollResp = client.prepareSearch(indexName)
                .setTypes("_doc")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .setScroll(new TimeValue(60000))
                .setFrom(0).setSize(1).setExplain(true)
                //.highlighter()  高亮
                .addSort("age", SortOrder.DESC)
                .get();
        do {
            for (SearchHit searchHit : scrollResp.getHits().getHits()) {
                System.out.println(searchHit.getSourceAsMap());
            }

            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(60000))
                    .execute().actionGet();
        } while (scrollResp.getHits().getHits().length != 0);
    }

    /**
     * 聚合
     */
    @Test
    public void aggregations() {
        SearchResponse sr = client.prepareSearch(indexName)
                .setTypes("_doc")
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(
                        AggregationBuilders.terms("agg1").field("age")
                )
                .addAggregation(
                        AggregationBuilders.dateHistogram("agg2")
                                .field("birthday")
                                .dateHistogramInterval(DateHistogramInterval.YEAR)
                )
                .get();

        System.out.println(sr);
        LongTerms agg1 = sr.getAggregations().get("agg1");
        System.out.println(agg1);
        Histogram agg2 = sr.getAggregations().get("agg2");
        System.out.println(agg2);

    }

    @Test
    public void joinQuery() {

    }
}
