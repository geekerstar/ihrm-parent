package com.geekerstar.company;

import com.geekerstar.company.dao.CompanyDao;
import com.geekerstar.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author geekerstar
 * date: 2019-07-18 20:18
 * description:
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyDaoTest {

    @Autowired
    private CompanyDao companyDao;

    @Test
    public void test() {
        Company company = companyDao.findById("1").get();
        System.out.println(company);

    }
}
