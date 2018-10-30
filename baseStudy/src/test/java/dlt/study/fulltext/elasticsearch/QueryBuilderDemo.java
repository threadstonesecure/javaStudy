package dlt.study.fulltext.elasticsearch;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Test;

/**
 * @Description:
 * @Package: dlt.study.fulltext.elasticsearch
 * @Author: denglt
 * @Date: 2018/10/16 5:49 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class QueryBuilderDemo {

    @Test
    public void boolQuery() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchAllQuery())
                .must(QueryBuilders.termQuery("_type", "_doc"))
                .must(QueryBuilders.matchQuery("address", "广州").analyzer("ik_max_word"));
        System.out.println(Strings.toString(boolQueryBuilder));

    }

    @Test
    public void test() {
        QueryStringQueryBuilder stringQueryBuilder = QueryBuilders.queryStringQuery("denglt");
        System.out.println(Strings.toString(stringQueryBuilder));

    }

    @Test
    public void nestedQuery(){
        NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("name", QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("name.en", "denglt")), ScoreMode.None);
        System.out.println(Strings.toString(nestedQueryBuilder));
       // SortBuilders.fieldSort("age").getNestedSort().setFilter()



    }
}
