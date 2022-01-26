package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;

@FeignClient("item-service")
@Repository
public interface GoodsClient extends GoodsApi{

}
