package com.bamboo.blog.web.admin;

import com.bamboo.blog.entity.Tag;
import com.bamboo.blog.service.TagService;
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
public class TagController {

    @Autowired
    private TagService tagService;

    //分页显示分类列表
    @GetMapping("/tags")
    public String list(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        model.addAttribute("page", tagService.listTag(pageable));
        return "admin/tags";
    }

    //点击新增分类按钮跳转到新增分类页面
    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/new_tag";

    }

    //
    @Transactional
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {

        model.addAttribute("tag", tagService.getTag(id));
        return "admin/new_tag";
    }

    //保存标签
    @PostMapping("/tags")
    public String post(Tag tag, BindingResult result, RedirectAttributes attributes) {

        //不允许添加重名的tag
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","不能添加重复的标签");
        }

        if (result.hasErrors()) {
            return "admin/new_tag";
        }


        if (tag.getName().length() < 1) {
            attributes.addFlashAttribute("message", "新增标签失败");

        } else {

            tagService.saveTag(tag);
            attributes.addFlashAttribute("message", "新增标签成功");
        }
        return "redirect:/admin/tags";
    }

    //编辑分类
    @PostMapping("/tags/{id}")
    public String editPost(Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Tag tag2 = tagService.getTagByName(tag.getName());
        if (tag2 != null) {
            result.rejectValue("name","nameError","不能添加重复的标签");
        }

        if (result.hasErrors()) {
            return "admin/new_tag";
        }

        if (tag.getName().length() < 1) {
            attributes.addFlashAttribute("message", "更新失败");

        } else {

            tagService.updateTag(id, tag);
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }


    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
