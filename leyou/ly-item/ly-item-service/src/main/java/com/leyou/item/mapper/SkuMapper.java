package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
@Component
public interface SkuMapper extends Mapper<Sku>, InsertListMapper<Sku> {
}
