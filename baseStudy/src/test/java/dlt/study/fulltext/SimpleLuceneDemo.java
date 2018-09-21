package dlt.study.fulltext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denglt on 2017/4/10.
 */
public class SimpleLuceneDemo {
    private Path path;
    private Directory directory;
    private Analyzer analyzer;

    @Before
    public void init() throws Exception {
        // 不同的分词影响后面的查询
       // analyzer = new CJKAnalyzer(); // 二元分词
       // analyzer = new StandardAnalyzer(); // 一元分词
        analyzer = new IKAnalyzer(true);

        path = FileSystems.getDefault().getPath("/tmp/testindex", "");
        directory = FSDirectory.open(path);
        // Directory directory = new RAMDirectory();
    }

    @Test
    public void deleteIndex() throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter iwriter = new IndexWriter(directory, config);
        iwriter.deleteAll();
        iwriter.close();
    }

    @Test
    public void createIndex() throws Exception {

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter iwriter = new IndexWriter(directory, config);

        Document doc = new Document();

        String text = "This is the text to be indexed. by 邓隆通 Denglt";
        doc.add(new Field("id", "1", TextField.TYPE_STORED));  // id 字段最好用StringField（即setTokenized(false)）
        doc.add(new StringField("name", "邓隆通", Field.Store.NO));
        doc.add(new TextField("text", text, Field.Store.YES));
        doc.add(new Field("text2", text, CUSTOMIZE_STORED));
        doc.add(new TextField("author", "denglt", Field.Store.YES)); // index ,but not store
        //iwriter.addDocument(doc);
        iwriter.updateDocument(new Term("id", "1"), doc);

        doc = new Document();
        text = "This is the text to be indexed. by 老王";
        doc.add(new Field("id", "22", TextField.TYPE_STORED));
        doc.add(new StringField("name", "邓隆", Field.Store.NO));
        doc.add(new Field("text", text, TextField.TYPE_STORED));
        doc.add(new Field("text2", text, CUSTOMIZE_STORED));
        doc.add(new TextField("author", "老王", Field.Store.YES)); // index ,but not store
        iwriter.updateDocument(new Term("id", "22"), doc);

        doc = new Document();
        text = "This is the text to be indexed. by 老王2";
        doc.add(new Field("id", "2", TextField.TYPE_STORED));
        doc.add(new StringField("name", "邓隆", Field.Store.NO));
        doc.add(new Field("text", text, TextField.TYPE_STORED));
        doc.add(new Field("text2", text, CUSTOMIZE_STORED));
        doc.add(new TextField("author", "老王", Field.Store.YES)); // index ,but not store
        iwriter.updateDocument(new Term("id", "2"), doc);


        doc = new Document();
        text = "This is the text to be indexed. by 王邓";
        doc.add(new Field("id", "3", TextField.TYPE_STORED));
        doc.add(new StringField("name", "邓隆", Field.Store.NO));
        doc.add(new Field("text", text, TextField.TYPE_STORED));
        doc.add(new Field("text2", text, CUSTOMIZE_STORED));
        doc.add(new TextField("author", "老王", Field.Store.YES)); // index ,but not store
        iwriter.updateDocument(new Term("id", "3"), doc);

        doc = new Document();
        text = "This is the text to be indexed. by 邓";
        doc.add(new Field("id", "4", TextField.TYPE_STORED));
        doc.add(new StringField("name", "邓隆", Field.Store.NO));
        doc.add(new Field("text", text, TextField.TYPE_STORED));
        doc.add(new Field("text2", text, CUSTOMIZE_STORED));
        doc.add(new TextField("author", "老王", Field.Store.YES)); // index ,but not store
        iwriter.updateDocument(new Term("id", "4"), doc);

        doc = new Document();
        text = "This is the text to be indexed. by 隆通";
        doc.add(new Field("id", "5", TextField.TYPE_STORED));
        doc.add(new StringField("name", "邓隆", Field.Store.NO));
        doc.add(new Field("text", text, TextField.TYPE_STORED));
        doc.add(new Field("text2", text, CUSTOMIZE_STORED));
        doc.add(new TextField("author", "老王", Field.Store.YES)); // index ,but not store
        iwriter.updateDocument(new Term("id", "5"), doc);

        iwriter.flush();
        iwriter.commit();
        iwriter.close();
    }

    @Test
    public void query() throws Exception {
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        // QueryParser parser = new QueryParser("author", analyzer);  //
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"text"}, analyzer);
        Query query = parser.parse("邓隆通");
        TopDocs topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);
    }

    @Test
    public void queryAnd() throws Exception {
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);
        QueryParser parser = new QueryParser("text", analyzer);
        Query query1 = parser.parse("Denglt");
        Query query2 = parser.parse("邓隆通");
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(query1, BooleanClause.Occur.MUST);
      //  builder.add(query2, BooleanClause.Occur.MUST);
        Query query = builder.build();

        TopDocs topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);
    }

    /**
     * 分词
     */
    @Test
    public void analyzerToken() throws IOException {
        // analyzer = new CJKAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("content", "邓隆通");
        tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.end();
        tokenStream.close();
    }

    /**
     * 按照分词结果，使用and 查询
     */
    @Test
    public void searchWord() throws Exception {
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        List<String> tokens = new ArrayList<>();
        TokenStream tokenStream = analyzer.tokenStream("token", "邓隆");
        tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
            tokens.add(charTermAttribute.toString());
        }
        tokenStream.end();
        tokenStream.close();

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String token : tokens) {
            TermQuery query = new TermQuery(new Term("text", token));
            builder.add(query, BooleanClause.Occur.MUST);
        }
        Query query = builder.build();

        TopDocs topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);
    }

    @Test
    public void termQuery() throws Exception {
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        TermQuery query = new TermQuery(new Term("text", "denglt"));
        TopDocs topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);

        query = new TermQuery(new Term("text", "Denglt")); // 无法找到 大写问题
        topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);

        query = new TermQuery(new Term("text", "邓隆")); //
        topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);
    }

    /**
     * wildcard 通配符
     * @throws Exception
     */
    @Test
    public void wildcardQuery() throws Exception {
        DirectoryReader ireader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(ireader);

        Query query = new WildcardQuery(new Term("text", "*邓隆通*")); //
        TopDocs topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);

        System.out.println("===============");
        query = new TermQuery(new Term("text", "邓隆通")); //
        topDocs = isearcher.search(query, 100);
        printDocs(topDocs, isearcher);

    }


    private void printDocs(TopDocs topDocs, IndexSearcher isearcher) throws Exception {
        ScoreDoc[] hits = topDocs.scoreDocs;
        if (hits.length == 0) {
            System.out.println("no found !");
            return;
        }
        System.out.println(topDocs.totalHits);
        System.out.println(hits.length);
        for (ScoreDoc hit : hits) {
            Document d = isearcher.doc(hit.doc);
            System.out.print("doc=" + hit.doc + " score=" + hit.score);
            System.out.print(", id:" + d.getField("id").stringValue());
            System.out.print(", text:" + d.getField("text").stringValue());
            System.out.println(", author:" + d.get("author"));
        }
    }

    public static final FieldType CUSTOMIZE_STORED = new FieldType();

    static {
        CUSTOMIZE_STORED.setOmitNorms(true);
        CUSTOMIZE_STORED.setIndexOptions(IndexOptions.DOCS);
        CUSTOMIZE_STORED.setTokenized(false); // 不分词
        CUSTOMIZE_STORED.freeze();

    }
}


