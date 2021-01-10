package com.bamboo.blog.web.admin;


import com.bamboo.blog.entity.Type;
import com.bamboo.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;


@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    //分页显示分类列表
    @GetMapping("/types")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        model.addAttribute("page", typeService.listType(pageable));
        return "admin/types";
    }

    //点击新增分类按钮跳转到新增分类页面
    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type", new Type());
        return "admin/new_type";

    }

    //编辑分类1，先获取原始的type
    @Transactional
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        System.out.println("进来了！！！！");
        model.addAttribute("type", typeService.getType(id));
        return "admin/new_type";
    }

    //保存分类
    @PostMapping("/types")
    public String post(Type type, BindingResult result, RedirectAttributes attributes) {

        //不允许添加重名的type
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        if (result.hasErrors()) {
            return "admin/new_type";
        }


        if (type.getName().length() < 1) {
            attributes.addFlashAttribute("message", "新增分类失败");

        } else {

            typeService.saveType(type);
            attributes.addFlashAttribute("message", "新增分类成功");
        }
        return "redirect:/admin/types";
    }

    //编辑分类2，提交时检查正确性
    @Transactional
    @PostMapping("/types/{id}")
    public String editPost(Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Type type2 = typeService.getTypeByName(type.getName());
        if (type2 != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }

        if (result.hasErrors()) {
            return "admin/new_type";
        }

        if (type.getName().length() < 1) {
            attributes.addFlashAttribute("message", "更新失败");

        } else {

            typeService.updateType(id, type);
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/types";
    }

}
