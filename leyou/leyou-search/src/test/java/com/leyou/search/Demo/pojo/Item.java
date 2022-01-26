package com.leyou.search.Demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "goods", type = "docs", shards = 1, replicas = 0)
public class Item {
    @Id
    Long id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    String title;
    @Field(type = FieldType.Keyword)
    String category;
    @Field(type = FieldType.Keyword)
    String brand;
    @Field(type = FieldType.Long)
    Long price;
    @Field(type = FieldType.Keyword,index = false)
    String images;

}
