package com.geekerstar.system.service;

import com.geekerstar.common.service.BaseService;
import com.geekerstar.common.utils.IdWorker;
import com.geekerstar.common.utils.QiniuUploadUtil;
import com.geekerstar.domain.company.Department;
import com.geekerstar.domain.system.Role;
import com.geekerstar.domain.system.User;
import com.geekerstar.system.client.DepartmentFeignClient;
import com.geekerstar.system.dao.RoleDao;
import com.geekerstar.system.dao.UserDao;
import com.geekerstar.system.utils.BaiduAiUtil;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.*;

@Service
public class UserService extends BaseService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdWorker idWorker;


    /**
     * 根据mobile查询用户
     */
    public User findByMobile(String mobile) {
        return userDao.findByMobile(mobile);
    }


    @Autowired
    private DepartmentFeignClient departmentFeignClient;

    /**
     * 批量保存用户
     */
    @Transactional
    public void saveAll(List<User> list, String companyId, String companyName) {
        for (User user : list) {
            //默认密码
            user.setPassword(new Md5Hash("123456", user.getMobile(), 3).toString());
            //id
            user.setId(idWorker.nextId() + "");
            //基本属性
            user.setCompanyId(companyId);
            user.setCompanyName(companyName);
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setLevel("user");

            //填充部门的属性
            Department department = departmentFeignClient.findByCode(user.getDepartmentId(), companyId);
            if (department != null) {
                user.setDepartmentId(department.getId());
                user.setDepartmentName(department.getName());
            }

            userDao.save(user);
        }
    }

    /**
     * 1.保存用户
     */
    public void save(User user) {
        //设置主键的值
        String id = idWorker.nextId() + "";
        String password = new Md5Hash("123456", user.getMobile(), 3).toString();
        user.setLevel("user");
        user.setPassword(password);//设置初始密码
        user.setEnableState(1);
        user.setId(id);
        //调用dao保存部门
        userDao.save(user);
    }

    /**
     * 2.更新用户
     */
    public void update(User user) {
        //1.根据id查询部门
        User target = userDao.findById(user.getId()).get();
        //2.设置部门属性
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        //3.更新部门
        userDao.save(target);
    }

    /**
     * 3.根据id查询用户
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    public List<User> findAll(String companyId) {
        return userDao.findAll(super.getSpec(companyId));
    }

    /**
     * 4.查询全部用户列表
     * 参数：map集合的形式
     * hasDept
     * departmentId
     * companyId
     */
    public Page findAll(Map<String, Object> map, int page, int size) {
        //1.需要查询条件
        Specification<User> spec = new Specification<User>() {
            /**
             * 动态拼接查询条件
             * @return
             */
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class), (String) map.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class), (String) map.get("departmentId")));
                }
                if (!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if ("0".equals((String) map.get("hasDept"))) {
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    } else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };

        //2.分页
        Page<User> pageUser = userDao.findAll(spec, new PageRequest(page - 1, size));
        return pageUser;
    }

    /**
     * 5.根据id删除用户
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 分配角色
     */
    public void assignRoles(String userId, List<String> roleIds) {
        //1.根据id查询用户
        User user = userDao.findById(userId).get();
        //2.设置用户的角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //3.更新用户
        userDao.save(user);
    }

    /**
     * 完成图片处理
     *
     * @param id        ：用户id
     * @param file      ：用户上传的头像文件
     * @return ：请求路径
     */
//    public String uploadImage(String id, MultipartFile file) throws IOException {
//        //1.根据id查询用户
//        User user = userDao.findById(id).get();
//        //2.使用DataURL的形式存储图片（对图片byte数组进行base64编码）
//        String encode = "data:image/png;base64,"+Base64.encode(file.getBytes());
//        System.out.println(encode);
//        //3.更新用户头像地址
//        user.setStaffPhoto(encode);
//        userDao.save(user);
//        //4.返回
//        return encode;
//    }

    @Autowired
    private BaiduAiUtil baiduAiUtil;

    /**
     * 上传到七牛云存储
     * 注册到百度云AI人脸库
     * 1.调用百度云接口，判断当前用户是否已经注册
     * 2.已注册，更新
     * 3.未注册，注册
     */
    public String uploadImage(String id, MultipartFile file) throws IOException {
        //1.根据id查询用户
        User user = userDao.findById(id).get();
        //2.将图片上传到七牛云存储，获取请求路径
        String imgUrl = new QiniuUploadUtil().upload(user.getId(), file.getBytes());//上传图片名，图片的byte数组
        //3.更新用户头像地址
        user.setStaffPhoto(imgUrl);
        userDao.save(user);

        //判断是否已经注册面部信息
        Boolean aBoolean = baiduAiUtil.faceExist(id);
        String imgBase64 = Base64.encode(file.getBytes());
        if (aBoolean) {
            //更新
            baiduAiUtil.faceUpdate(id, imgBase64);
        } else {
            //注册
            baiduAiUtil.faceRegister(id, imgBase64);
        }
        //4.返回
        return imgUrl;
    }
}
