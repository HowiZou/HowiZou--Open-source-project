package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptror.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;
    public void addCart(Cart cart) {

        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //先查询,hashOperations是Map<String ：userId,Map<String ：SkuId,String>>的第二层map表
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());
        //判断是否有
        String skuId = cart.getSkuId().toString();
        Integer num = cart.getNum();

        if(hashOperations.hasKey(skuId)){
            String cartJson = hashOperations.get(skuId).toString();
            cart = JsonUtils.parse(cartJson, Cart.class);
            cart.setNum(num + cart.getNum());
        }else {
            //没有，新增
            cart.setUserId(userInfo.getId());
            //查询商品信息
            Sku sku = this.goodsClient.querySkuById(cart.getSkuId());
            cart.setPrice(sku.getPrice());
            cart.setImage(StringUtils.isBlank(sku.getImages())? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setTitle(sku.getTitle());
            cart.setNum(num);
        }
        hashOperations.put(skuId, JsonUtils.serialize(cart));

    }

    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //判断hash操作对象是否存在
        if(!this.redisTemplate.hasKey(userInfo.getId().toString())){
            return null;
        }
        //先查询
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(userInfo.getId().toString());

        List<Object> cartJsons = hashOperations.values();

        return cartJsons.stream().map(cartJson -> JsonUtils.parse(cartJsons.toString(), Cart.class)).collect(Collectors.toList());
    }
}
