package com.leyou.item.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.vo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: taft
 * @Date:
 */
@Service
public class GoodsService {

    //首先查询的一定是spu表
    //得到的结果，有两个值是不能直接使用（cid，bid）
    //需要把查询到的spu转为spobo，
    //根据spu中记录的category的信息去查分类的名称
    //根据spu中记录brand的信息去查询品牌名称

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;



    private Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page,Integer rows) {

        //开启分页

        PageHelper.startPage(page,rows);

        //构建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (saleable != null){
            criteria.orEqualTo("saleable",saleable);
        }

        if (StringUtils.isNotBlank(key)){
            //根据tile模糊查询
            criteria.andLike("title","%"+key+"%");
        }

        List<Spu> spus = spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);
        //把这其中得spu变成spubo


        //以下操作就是把spu转为SpuBO
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            // 把spu所有的属性值copy给spuBo
            BeanUtils.copyProperties(spu, spuBo);
            // 设置品牌名称和分类名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            List<String> names = this.categoryService.queryCategoryNameByCids(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "-"));
            return spuBo;
        }).collect(Collectors.toList());

        // 返回分页结果集
        return new PageResult<>(pageInfo.getTotal(), spuBos);
    }




    @Transactional
    public void saveGoods(SpuBo spuBO) {

        // 保存spu
        spuBO.setId(null);
        spuBO.setSaleable(true);
        spuBO.setValid(true);
        spuBO.setCreateTime(new Date());
        spuBO.setLastUpdateTime(spuBO.getCreateTime());
        int count = spuMapper.insertSelective(spuBO);
        if(count == 1){
            throw new LyException(ExceptionEnum.GOODS_SAVE_ERROR);
        }

        //保存spuDetail信息
        spuBO.getSpuDetail().setSpuId(spuBO.getId());

        this.spuDetailMapper.insertSelective(spuBO.getSpuDetail());

        // 保存sku和库存信息
        saveSkuAndStock(spuBO.getSkus(), spuBO.getId());

        sendMsg("insert",spuBO.getId());

    }

    private void sendMsg(String type,Long spuId) {
        try {
            this.amqpTemplate.convertAndSend("item." + type, spuId);//（routingkey,传输的信息）
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    private void saveSkuAndStock(List<Sku> skus, Long spuId) {

        for (Sku sku : skus) {
            if (!sku.getEnable()){
                continue;
            }
            //保存sku
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());

            this.skuMapper.insert(sku);

            //保存库存信息
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());

            this.stockMapper.insert(stock);
        }
    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku sku = new Sku();
        sku.setSpuId(id);
        return skuMapper.select(sku);
    }

    public SpuDetail querySpuDetailById(Long id) {

        return this.spuDetailMapper.selectByPrimaryKey(id);
    }

    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }



    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }

    /*
    根据spuId查询sku
     */
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        //查询每个sku对应的库存
        skus.forEach(sku -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        });
        return skus;
    }

    /*
    更新商品
     */
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //先删除sku和stock
        //搜集所有的skuId
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);
        skus.forEach(sku -> {
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        });

        //直接删除sku
        this.skuMapper.delete(record);

        //再新增sku和stock
        saveSkuAndStock(spuBo.getSkus(),spuBo.getId());
        //更新spu和spuDetail
        spuBo.setSaleable(null);
        spuBo.setValid(null);
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());
        sendMsg("update",spuBo.getId());
    }
}
