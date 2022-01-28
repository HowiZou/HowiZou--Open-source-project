package com.itheima.ssm.service;

import com.itheima.ssm.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IProductService {

    List<Product> findAll() throws Exception;

    void save(Product product) throws Exception;
}
