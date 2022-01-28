package com.itheima.ssm.dao;


import com.itheima.ssm.domain.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


import java.util.List;
@Repository
public interface IProductDao {
    //根据id查询产品
    @Select("select * from product where id=#{id}")
    Product findById(String id) throws Exception;

    //查询所有的商品信息
    @Select("select * from product")
    List<Product> findAll() throws Exception;

     @Insert("insert into product(productNum,productName,cityName,departureTime,productPrice,productDesc,productStatus)values(#{productNum},#{productName},#{cityName},#{departureTime},#{productPrice},#{productDesc},#{productStatus})")
    void save(Product product);
}
