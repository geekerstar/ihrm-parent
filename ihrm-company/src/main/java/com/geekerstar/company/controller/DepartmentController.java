package com.geekerstar.company.controller;

import com.geekerstar.common.controller.BaseController;
import com.geekerstar.common.entity.Result;
import com.geekerstar.common.entity.ResultCode;
import com.geekerstar.company.service.CompanyService;
import com.geekerstar.company.service.DepartmentService;
import com.geekerstar.domain.company.Company;
import com.geekerstar.domain.company.Department;
import com.geekerstar.domain.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/company")   //  company/deparment
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CompanyService companyService;
    /**
     * 保存
     */
    @RequestMapping(value="/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department department) {
        //1.设置保存的企业id
        /**
         * 企业id：目前使用固定值1，以后会解决
         */
        department.setCompanyId(companyId);
        //2.调用service完成保存企业
        departmentService.save(department);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     * 指定企业id
     */
    @RequestMapping(value="/department",method = RequestMethod.GET)
    public Result findAll() {
        //1.指定企业id
        Company company = companyService.findById(companyId);
        //2.完成查询
        List<Department> list = departmentService.findAll(companyId);
        //3.构造返回结果
        DeptListResult deptListResult = new DeptListResult(company,list);
        return new Result(ResultCode.SUCCESS,deptListResult);
    }

    /**
     * 根据ID查询department
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }

    /**
     * 修改Department
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id,@RequestBody Department department) {
        //1.设置修改的部门id
        department.setId(id);
        //2.调用service更新
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }


}
