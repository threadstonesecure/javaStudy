package dlt.study.fulltext.elasticsearch;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
                .must(QueryBuilders.termQuery("_type", "_doc"));
        System.out.println(Strings.toString(boolQueryBuilder));

    }
}
