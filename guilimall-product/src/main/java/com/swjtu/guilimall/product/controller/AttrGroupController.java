package com.swjtu.guilimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.swjtu.guilimall.product.entity.AttrEntity;
import com.swjtu.guilimall.product.service.AttrAttrgroupRelationService;
import com.swjtu.guilimall.product.service.AttrService;
import com.swjtu.guilimall.product.service.CategoryService;
import com.swjtu.guilimall.product.vo.AttrGroupRelationVo;
import com.swjtu.guilimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.swjtu.guilimall.product.entity.AttrGroupEntity;
import com.swjtu.guilimall.product.service.AttrGroupService;
import com.swjtu.common.utils.PageUtils;
import com.swjtu.common.utils.R;


/**
 * 属性分组
 *
 * @author Lil_Boat
 * @email GuodanYang_nn@163.com
 * @date 2022-08-14 10:14:03
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;


    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
        // PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除关联属性信息
     *
     * @param vos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos) {
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
