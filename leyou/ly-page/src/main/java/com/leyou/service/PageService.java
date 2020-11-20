package com.leyou.service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecificationClient;
import com.leyou.item.pojo.*;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
@Service
public class PageService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private TemplateEngine templateEngine;

    public Map<String, Object> loadModel(Long spuId) {

        Map<String, Object> model = new HashMap<>();

        //查询spu 1
        Spu spu = goodsClient.querySpuById(spuId);
        //查询skus 1
        List<Sku> skus = spu.getSkus();
        //查询详情 1.
        SpuDetail spuDetail = spu.getSpuDetail();
        //查询品牌 1
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        // 查询商品分类 1
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //查询规格参数
        List<SpecGroup> specs = specClient.queryGroupByCid(spu.getCid3());

        //查询规格参数组
        List<SpecGroup> groups = specClient.queryGroupByCid(spu.getCid3());

        //查询特殊的规格参数
        List<SpecParam> params = specClient.queryParamList(null, spu.getCid3(), null);
        //初始化特殊规格参数的Map
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(), param.getName());
        });


        model.put("spu", spu);
        model.put("spuDetail", spuDetail);
        model.put("categories", categories);
        model.put("brand", brand);
        model.put("skus", skus);
        model.put("groups", groups);
        model.put("paramMap", paramMap);
        return model;


    }


    public void creatHtml(Long spuId) {
        //上下文
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        //输出流
        File dest = new File("G:\\JavaProject\\yun6\\upload", spuId + ".html");
        try (PrintWriter writer = new PrintWriter(dest, "UTF-8")) {
            //生成HTML
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.error("[静态页服务]生成静态页异常", e);
        }


    }


    public void deleteHtml(Long id) {
        File file = new File("G:\\nginx 1.17.9.1 Unicorn\\html\\item", id + ".html");
        file.deleteOnExit();

    }
}
