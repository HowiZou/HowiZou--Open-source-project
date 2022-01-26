package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BrandMapper extends Mapper<Brand>{
    @Insert("insert into tb_category_brand (category_id,brand_id) values(#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid")Long cid, @Param("bid")Long bid);

    @Insert("SELECT * FROM tb_brand b INNER JOIN tb_category_brand cb ON b.id = cb.brand_id WHERE cb.category_id = #{cid}")
    List<Brand> queryByCategoryId(@Param("cid") Long cid);
}
