package com.leyou.goods.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service

public class GoodsHtmlService {
    @Autowired
    private TemplateEngine templateEngine;

   @Autowired
   private GoodsService goodsService;

    public void createHtml(Long spuId){
        //初始化上下文对象
        Context context = new Context();
        //设置数据模型给上下文
        context.setVariables(this.goodsService.loadData(spuId));
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new File("E:\\tools\\nginx\\nginx-1.12.2\\html\\item\\" + spuId + ".html"));
            this.templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(printWriter != null){
                printWriter.close();
            }
        }
    }

    public void deleteHtml(Long spuId) {
        File file = new File("E:\\tools\\nginx\\nginx-1.12.2\\html\\item\\" + spuId + ".html");
        file.deleteOnExit();//如果有的话删除
    }
}
