package com.leyou.search.Demo;

import com.leyou.search.Demo.pojo.Item;
import com.leyou.search.Demo.repositry.ItemRepositry;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticSearchTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepositry itemRepositry;

    @Test
    public void testIndex(){
        this.elasticsearchTemplate.createIndex(Item.class);
        this.elasticsearchTemplate.putMapping(Item.class);

    }

    @Test
    public void deleteIndex(){
        elasticsearchTemplate.deleteIndex(Item.class);
    }

    @Test
    public void testCreate(){
        Item item = new Item(1L,"小米手机8","手机","小米", (long) 3499,"http://image.leyou.com/13123.jpg");
        this.itemRepositry.save(item);
    }
    @Test
    public void testCreatel(){
        ArrayList<Item> list = new ArrayList<>();
        list.add(new Item(2L,"坚果8","手机","坚果", (long) 3499,"http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L,"华为8","手机","华为", (long) 3499,"http://image.leyou.com/13123.jpg"));
        this.itemRepositry.saveAll(list);
    }

    @Test
    public void testFind(){
        Optional<Item> item = this.itemRepositry.findById(1l);
        System.out.println(item.get());
    }
    @Test
    public void testAgg(){
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加聚合查询
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("brand"));
        //添加结果集过滤，普通结果集不包含任何字段，相当于size=0
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));//(包含字段，排除字段)
        //执行聚合查询，获取聚合结果集，需要强转
        AggregatedPage<Item> itemPage = (AggregatedPage<Item>)this.itemRepositry.search(queryBuilder.build());
        //获取聚合结果集中的聚合对象，根据聚合名获取。需要强转成LongTerms StringTerms DoubleTerms
        Aggregation brandsAgg = itemPage.getAggregation("brands");
        StringTerms terms = (StringTerms)brandsAgg;
        //获取聚合中的桶
        terms.getBuckets().forEach(bucket -> {
            //获取桶中的key
            System.out.println(bucket.getKeyAsString());
            //获取桶中的条数
            System.out.println(bucket.getDocCount());
        });

    }
}
