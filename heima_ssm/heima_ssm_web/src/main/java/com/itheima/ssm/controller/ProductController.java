package com.itheima.ssm.controller;

import com.itheima.ssm.dao.IProductDao;
import com.itheima.ssm.domain.Orders;
import com.itheima.ssm.domain.Product;
import com.itheima.ssm.service.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService productService;
    @RequestMapping("/save.do")
    public String save(Product product) throws Exception {
    productService.save(product);
    return "redirect:findAll.do";
    }

    //查询全部产品
    @RequestMapping("/findAll.do")
    @RolesAllowed("ADMIN")
    public ModelAndView findAll() throws Exception {
        ModelAndView mv = new ModelAndView();
        List<Product> ps = productService.findAll();
        mv.addObject("productList",ps);
        mv.setViewName("product-list1");
        return mv;
    }



}
