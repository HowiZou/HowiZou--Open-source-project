package com.leyou.search.Demo.repositry;

import com.leyou.search.Demo.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepositry extends ElasticsearchRepository<Item,Long>{
}
