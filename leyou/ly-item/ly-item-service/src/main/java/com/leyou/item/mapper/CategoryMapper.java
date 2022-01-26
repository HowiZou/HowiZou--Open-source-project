package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
@Repository
public interface CategoryMapper extends Mapper<Category>,SelectByIdListMapper<Category,Long> {
}
