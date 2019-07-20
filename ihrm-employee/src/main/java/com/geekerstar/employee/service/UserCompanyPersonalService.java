package com.geekerstar.employee.service;

import com.geekerstar.common.utils.IdWorker;
import com.geekerstar.domain.employee.UserCompanyPersonal;
import com.geekerstar.domain.employee.response.EmployeeReportResult;
import com.geekerstar.employee.dao.UserCompanyPersonalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UserCompanyPersonalService {
    @Autowired
    private UserCompanyPersonalDao userCompanyPersonalDao;

    public void save(UserCompanyPersonal personalInfo) {
        userCompanyPersonalDao.save(personalInfo);
    }

    public UserCompanyPersonal findById(String userId) {
        return userCompanyPersonalDao.findByUserId(userId);
    }
}
