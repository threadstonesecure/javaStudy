package dlt.study.fulltext.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.List;

/**
 * @Description: 测试嵌套对象 search的差别
 * @Package: dlt.study.fulltext.elasticsearch
 * @Author: denglt
 * @Date: 2018/10/23 5:31 PM
 * @Copyright: 版权归 HSYUNTAI 所有
 */
public class NestedTypeDemo {

    private String host1 = "47.106.93.69";
    private int port = 9300;

    private TransportClient client;

    @Before
    public void connect() throws Exception {
        // on startup
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName(host1), port));
        //  .addTransportAddress(new TransportAddress(InetAddress.getByName("host2"), 9300));

    }

    /**
     * curl -X PUT "47.106.93.69:9200/my_index/blogpost/1?pretty" -H 'Content-Type: application/json' -d'
     * {
     *   "title": "Nest eggs",
     *   "body":  "Making your money work...",
     *   "tags":  [ "cash", "shares" ],
     *   "comments": [
     *     {
     *       "name":    "John Smith",
     *       "comment": "Great article",
     *       "age":     28,
     *       "stars":   4,
     *       "date":    "2014-09-01"
     *     },
     *     {
     *       "name":    "Alice White",
     *       "comment": "More like this please",
     *       "age":     31,
     *       "stars":   5,
     *       "date":    "2014-10-22"
     *     }
     *   ]
     * }
     * '
     * 这儿的comments 是Object type，不是 nested type
     *
     * 虽然 object 类型在存储 单一对象 时非常有用,但对于对象数组的搜索而言,毫无用处。
     *
     * {
     *   "query": {
     *     "bool": {
     *       "must": [
     *         {
     *           "match": {
     *             "comments.name": "Alice"
     *           }
     *         },
     *         {
     *           "match": {
     *             "comments.age": 28
     *           }
     *         }
     *       ]
     *     }
     *   }
     * }   有记录
     */
    @Test
    public void getDocument() {
        GetResponse doc = client.prepareGet("my_index", "blogpost", "1") //  client.prepareMultiGet()
                .get();
        System.out.println(doc);
        // Object value = doc.getField("_source");
        System.out.println(doc.getVersion());
        System.out.println(doc.getSource().get("tags").getClass());

        System.out.println(doc.getSource().get("comments").getClass());

        List<Object> tags = (List<Object>) doc.getSource().get("tags");
        tags.forEach(t -> {
            System.out.println(t.getClass());
            System.out.println(t);
        });

        List<Object> comments = (List<Object> ) doc.getSource().get("comments");
        comments.forEach(t -> {
            System.out.println(t.getClass());
            System.out.println(t);
        });
    }



    /**
     * curl -X PUT "47.106.93.69:9200/nested_index" -H 'Content-Type: application/json' -d'
     * {
     *   "mappings": {
     *     "blogpost": {
     *       "properties": {
     *         "comments": {
     *           "type": "nested",
     *           "properties": {
     *             "name":    { "type": "text"  },
     *             "comment": { "type": "text"  },
     *             "age":     { "type": "short"   },
     *             "stars":   { "type": "short"   },
     *             "date":    { "type": "date"    }
     *           }
     *         }
     *       }
     *     }
     *   }
     * }
     * '
     * curl -X PUT "47.106.93.69:9200/nested_index/blogpost/1?pretty" -H 'Content-Type: application/json' -d'
     * {
     *   "title": "Nest eggs",
     *   "body":  "Making your money work...",
     *   "tags":  [ "cash", "shares" ],
     *   "comments": [
     *     {
     *       "name":    "John Smith",
     *       "comment": "Great article",
     *       "age":     28,
     *       "stars":   4,
     *       "date":    "2014-09-01"
     *     },
     *     {
     *       "name":    "Alice White",
     *       "comment": "More like this please",
     *       "age":     31,
     *       "stars":   5,
     *       "date":    "2014-10-22"
     *     }
     *   ]
     * }
     * '
     *
     * {
     *   "query": {
     *     "nested": {
     *       "path": "comments",
     *       "query": {
     *         "bool": {
     *           "must": [
     *             {
     *               "match": {
     *                 "comments.name": "Alice"
     *               }
     *             },
     *             {
     *               "match": {
     *                 "comments.age": 28
     *               }
     *             }
     *           ]
     *         }
     *       }
     *     }
     *   }
     * }   // 无记录
     *
     * {
     *   "query": {
     *     "nested": {
     *       "path": "comments",
     *       "query": {
     *         "bool": {
     *           "must": [
     *             {
     *               "match": {
     *                 "comments.name": "John"
     *               }
     *             },
     *             {
     *               "match": {
     *                 "comments.age": 28
     *               }
     *             }
     *           ]
     *         }
     *       }
     *     }
     *   }
     * }  // 有记录
     */

    @Test
    public void getDocument2() {
        GetResponse doc = client.prepareGet("nested_index", "blogpost", "1") //  client.prepareMultiGet()
                .get();
        System.out.println(doc);
        // Object value = doc.getField("_source");
        System.out.println(doc.getVersion());
        System.out.println(doc.getSource().get("tags").getClass());

        System.out.println(doc.getSource().get("comments").getClass());

        List<Object> tags = (List<Object>) doc.getSource().get("tags");
        tags.forEach(t -> {
            System.out.println(t.getClass());
            System.out.println(t);
        });

        List<Object> comments = (List<Object> ) doc.getSource().get("comments");
        comments.forEach(t -> {
            System.out.println(t.getClass());
            System.out.println(t);
        });
    }

}
