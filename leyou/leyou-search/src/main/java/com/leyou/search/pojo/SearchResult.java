package com.leyou.search.pojo;

import com.leyou.common.vo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;



public class SearchResult extends PageResult<Goods> {
    //为了保证网络的高效性，我们不需要category中所有字段的值，只需要获取id和name即可
    //那么就不用Category作为泛型进行封装，而是用Map，以String作为字段名类型，以Object作为字段值类型
    private List<Map<String,Object>> categories;
    //brand对象基本上所有的字段都需要，所以直接封装
    private List<Brand> brands;

    public SearchResult() {
    }

    public SearchResult(Long total, List<Goods> items) {
        super(total, items);
    }

    public SearchResult(Long total, Long totalPage, List<Goods> items) {
        super(total, totalPage, items);
    }

    public SearchResult(List<Map<String, Object>> categories, List<Brand> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}
