package com.leyou.search.client;

import com.leyou.common.vo.PageResult;
import com.leyou.item.api.GoodsApi;
import com.leyou.item.bo.SpuBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("item-service")
@Repository
public interface GoodsClient extends GoodsApi{

}
