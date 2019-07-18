package com.geekerstar.company.service;

import com.geekerstar.common.utils.IdWorker;
import com.geekerstar.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author geekerstar
 * date: 2019-07-18 20:24
 * description:
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 保存企业
     * 1、配置Idwork到工程中
     * 2、在service中注入idwork
     * 3、通过idwork生成id
     * 4、保存企业
     */
    public void add(Company company){
        //基本属性的设置
        String id  = String.valueOf(idWorker.nextId());
        company.setId(id);
        //默认的状态
        company.setAuditState("0"); //0未审核，1已审核
        company.setState(1); //0未激活，1已激活
        companyDao.save(company);
    }


    /**
     * 更新企业
     * 1、参数，Company
     * 2、根据id查询企业对象
     * 3、设置修改的属性值
     * 4、调用dao完成更新
     */
    public void update(Company company){
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(temp);
    }

    /**
     * 删除企业
     */
    public void deleteById(String id){
        companyDao.deleteById(id);
    }

    /**
     * 根据id查询企业
     */
    public Company findById(String id){
        return companyDao.findById(id).get();
    }

    /**
     * 查询全部企业列表
     */
    public List<Company> findAll(){
        return companyDao.findAll();
    }
}
