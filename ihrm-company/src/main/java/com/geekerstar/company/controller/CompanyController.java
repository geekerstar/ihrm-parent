package com.geekerstar.company.controller;

import com.geekerstar.common.entity.Result;
import com.geekerstar.common.entity.ResultCode;
import com.geekerstar.company.service.CompanyService;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author geekerstar
 * date: 2019-07-18 20:36
 * description:
 */
@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 保存企业
     */
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result save(@RequestBody Company company){
        companyService.add(company);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id更新
     * 1、方法
     * 2、请求参数
     * 3、响应
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(value = "id") String id,@RequestBody Company company){
        company.setId(id);
        companyService.update(company);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除企业
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable(value = "id") String id){
        companyService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id查询企业
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable(value = "id") String id){
        Company company = companyService.findById(id);
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(company);
        return result;
    }

    /**
     * 查询企业全部列表
     */
    @GetMapping(value = "")
    public Result findAll(){
        List<Company> list = companyService.findAll();
        Result result = new Result(ResultCode.SUCCESS);
        result.setData(list);
        return result;
    }

}
