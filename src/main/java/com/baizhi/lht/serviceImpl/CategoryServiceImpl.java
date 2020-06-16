package com.baizhi.lht.serviceImpl;

import com.baizhi.lht.annotation.AddCache;
import com.baizhi.lht.annotation.AddLog;
import com.baizhi.lht.annotation.DelCache;
import com.baizhi.lht.dao.CategoryMapper;
import com.baizhi.lht.entity.Category;
import com.baizhi.lht.entity.CategoryExample;
import com.baizhi.lht.service.CategoryService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    //依赖注入
    @Resource
    private CategoryMapper categoryMapper;

    //查询一级类别
    @AddCache
    @Override
    public HashMap<String, Object> showOneCategory(Integer page, Integer rows) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //存储当前页
        map.put("page", page);

        //获取总条数
        CategoryExample example = new CategoryExample();
        example.createCriteria().andLevelsEqualTo(1);
        int i = categoryMapper.selectCountByExample(example);
        //存储总条数
        map.put("records", i);

        //获取总页数
        int total = i % rows == 0 ? i / rows : i / rows + 1;
        map.put("total", total);

        //查询展示i数据
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", categories);

        return map;
    }

    //查询二级类别
    @AddCache
    @Override
    public HashMap<String, Object> showTwoCategory(Integer page, Integer rows, String prentId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //存储当前页
        map.put("page", page);

        //获取总条数
        CategoryExample example = new CategoryExample();
        example.createCriteria().andParentIdEqualTo(prentId);
        int i = categoryMapper.selectCountByExample(example);
        //存储总条数
        map.put("records", i);

        //获取总页数
        int total = i % rows == 0 ? i / rows : i / rows + 1;
        map.put("total", total);

        //查询展示i数据
        RowBounds rowBounds = new RowBounds((page - 1) * rows, rows);
        List<Category> categories = categoryMapper.selectByExampleAndRowBounds(example, rowBounds);
        map.put("rows", categories);
        return map;
    }

    //添加类别
    @DelCache
    @AddLog(description = "添加类别")
    @Override
    public HashMap<String, Object> add(Category category) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        CategoryExample example = new CategoryExample();
        example.createCriteria().andCateNameEqualTo(category.getCateName());
        Category category1 = categoryMapper.selectOneByExample(example);
        if (category1 == null) {
            if (category.getParentId() == null) {

                category.setLevels(1);


            } else {
                category.setLevels(2);

            }
            category.setId(UUID.randomUUID().toString());
            categoryMapper.insertSelective(category);
            map.put("status", "101");
            map.put("message", "添加成功");
        } else {
            map.put("status", "101");
            map.put("message", "添加失败，类别名已存在");
        }
        return map;
    }

    //修改类别
    @DelCache
    @AddLog(description = "修改类别")
    @Override
    public HashMap<String, Object> update(Category category) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        CategoryExample example = new CategoryExample();
        example.createCriteria().andCateNameEqualTo(category.getCateName());
        Category category1 = categoryMapper.selectOneByExample(example);
        if (category1 == null) {
            categoryMapper.updateByPrimaryKeySelective(category);
        } else {
            map.put("status", "101");
            map.put("message", "修改失败，类别名已存在");
        }
        return map;
    }

    //删除类别
    @DelCache
    @AddLog(description = "删除类别")
    @Override
    public HashMap<String, Object> delete(Category category) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        //通过id查询数据
        Category category1 = categoryMapper.selectByPrimaryKey(category);
        //判断是一级还是二级
        if (category1.getLevels() == 1) {
            //查询一级类别有无子类别
            CategoryExample example = new CategoryExample();
            example.createCriteria().andParentIdEqualTo(category1.getId());
            int i = categoryMapper.selectCountByExample(example);
            //通过数量判断是否有字类别
            if (i == 0) {
                //没有子类别，直接删除
                categoryMapper.deleteByPrimaryKey(category);
                map.put("status", "100");
                map.put("message", "删除成功！");

            } else {
                //有子类别
                map.put("status", "100");
                map.put("message", "删除失败,该类别下有子类别！");
            }
        } else {
            //没有二级类别，直接删除
            categoryMapper.deleteByPrimaryKey(category);
            map.put("status", "100");
            map.put("message", "删除成功！");
        }
        return map;
    }

    //app类别展示

    @AddCache
    @Override
    public HashMap<String, Object> queryAllCategory() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            //获取所有类别
            List<Category> categories = categoryMapper.queryAllCategory();
            List<Category> list = new ArrayList<>();
            //清除重复二级类别
            for (Category category : categories) {
                if (category.getLevels() == 1) {
                    list.add(category);
                }
            }
            map.put("data", list);
            map.put("message", "查询成功");
            map.put("status", "100");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("data", new ArrayList<>());
            map.put("message", "查询失败");
            map.put("status", "104");
        }
        return map;
    }
}
