package com.leyou.item.web;


import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid")Long pid){
        return ResponseEntity.ok(categoryService.findCategoryByPid(pid));
    }
    @GetMapping
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        // 响应200
        return ResponseEntity.ok(names);
    }

}
