package com.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.entity.CangkuxinxiEntity;

import com.service.CangkuxinxiService;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 快递仓库信息表
 * 后端接口
 * @author
 * @email
 * @date 2021-02-22
*/
@RestController
@Controller
@RequestMapping("/cangkuxinxi")
public class CangkuxinxiController {
    private static final Logger logger = LoggerFactory.getLogger(CangkuxinxiController.class);

    @Autowired
    private CangkuxinxiService cangkuxinxiService;

    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("Controller:"+this.getClass().getName()+",page方法");
        Object role = request.getSession().getAttribute("role");
        PageUtils page = null;
        if(role.equals("用户")){
            params.put("expressTypes","2");
            page = cangkuxinxiService.queryPage(params);
        }else{
            page = cangkuxinxiService.queryPage(params);
        }
        return R.ok().put("data", page);
    }
    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        logger.debug("Controller:"+this.getClass().getName()+",info方法");
        CangkuxinxiEntity cangkuxinxi = cangkuxinxiService.selectById(id);
        if(cangkuxinxi!=null){
            return R.ok().put("data", cangkuxinxi);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody CangkuxinxiEntity cangkuxinxi, HttpServletRequest request){
        logger.debug("Controller:"+this.getClass().getName()+",save");
        Wrapper<CangkuxinxiEntity> queryWrapper = new EntityWrapper<CangkuxinxiEntity>()
            .eq("logistics", cangkuxinxi.getLogistics());
        cangkuxinxi.setLogistics(String.valueOf(new Date().getTime()));
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        CangkuxinxiEntity cangkuxinxiEntity = cangkuxinxiService.selectOne(queryWrapper);
            cangkuxinxi.setWarehouseTime(new Date());
        cangkuxinxi.setExpressTypes(2);
        if(cangkuxinxiEntity==null){
            cangkuxinxiService.insert(cangkuxinxi);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody CangkuxinxiEntity cangkuxinxi, HttpServletRequest request){
        logger.debug("Controller:"+this.getClass().getName()+",update");
        //根据字段查询是否有相同数据
        Wrapper<CangkuxinxiEntity> queryWrapper = new EntityWrapper<CangkuxinxiEntity>()
            .notIn("id",cangkuxinxi.getId())
            .eq("logistics", cangkuxinxi.getLogistics())
            .eq("name", cangkuxinxi.getName())
            .eq("kd_types", cangkuxinxi.getKdTypes())
            .eq("courier", cangkuxinxi.getCourier())
            .eq("cmobile", cangkuxinxi.getCmobile())
            .eq("recipients", cangkuxinxi.getRecipients())
            .eq("rmobile", cangkuxinxi.getRmobile())
            .eq("consigneeaddress", cangkuxinxi.getConsigneeaddress())
            .eq("express_types", cangkuxinxi.getExpressTypes())
            .eq("notice_content", cangkuxinxi.getNoticeContent())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        CangkuxinxiEntity cangkuxinxiEntity = cangkuxinxiService.selectOne(queryWrapper);
                cangkuxinxi.setWarehouseTime(new Date());
        if(cangkuxinxiEntity==null){
            cangkuxinxiService.updateById(cangkuxinxi);//根据id更新
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }


    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        logger.debug("Controller:"+this.getClass().getName()+",delete");
        cangkuxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}

