package com.baizhi.lht.controller;

import com.baizhi.lht.entity.Category;
import com.baizhi.lht.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;

@Controller
@RequestMapping("category")
public class CategoryController {

    //依赖注入
    @Resource
    private CategoryService categoryService;

    //展示所有
    @RequestMapping("showOneCategory")
    @ResponseBody
    public HashMap<String, Object> showOneCategory(Integer page, Integer rows) {
        HashMap<String, Object> map = categoryService.showOneCategory(page, rows);
        return map;
    }

    //展示所有
    @RequestMapping("showTwoCategory")
    @ResponseBody
    public HashMap<String, Object> showTwoCategory(Integer page, Integer rows, String parentId) {
        HashMap<String, Object> map = categoryService.showTwoCategory(page, rows, parentId);
        return map;
    }

    //更新状态
    @RequestMapping("edit")
    @ResponseBody
    public Object edit(Category category, String oper) {
        if (oper.equals("add")) {
            //添加
            HashMap<String, Object> add = categoryService.add(category);
            return add;
        } else if (oper.equals("edit")) {
            HashMap<String, Object> update = categoryService.update(category);
            return update;
        } else if (oper.equals("del")) {

            HashMap<String, Object> delete = categoryService.delete(category);
            return delete;
        }
        return null;
    }
}
