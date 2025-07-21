/*
 * Copyright 2024-2025 fuhouyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ${package.Controller};

import com.fuhouyu.framework.database.base.PageResultDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fuhouyu.framework.common.response.BaseResponse;
import ${package.DTO}.${entity}DTO;
import ${package.DTO}.${entity}PageQueryDTO;
import ${package.Service}.${table.serviceName};
import com.fuhouyu.framework.common.response.ResponseHelper;
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.log.enums.RiskTypeEnum;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotEmpty;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

import java.util.List;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/api/v1<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
@Tag(name = "${table.comment!}")
@Validated
@Slf4j
@RequiredArgsConstructor
@LogModule("${table.comment!}")
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${table.serviceName} ${table.serviceName?uncap_first};

    /**
    * 保存 ${table.comment!}
    * @param vo ${table.comment!} DTO对象
    * @return id
    */
    @PostMapping
    @Operation(summary = "保存${table.comment!}")
    @LogRecord(operator = OperatorEnum.INSERT, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Long> save${entity}(@RequestBody @Valid ${entity}DTO vo){
        return ResponseHelper.success(this.${table.serviceName?uncap_first}.save${entity}(vo));
    }

    /**
    * 修改 ${table.comment!}
    *
    * @param id           主键id
    * @param vo ${table.comment!} DTO对象
    * @return true 成功 false 失败
    */
    @PutMapping("/{id}")
    @Operation(summary = "修改${table.comment!}")
    @LogRecord(operator = OperatorEnum.UPDATE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Boolean> update(
        @PathVariable("id") Long id, @RequestBody @Valid ${entity}DTO vo) {
        vo.setId(id);
        return ResponseHelper.success(this.${table.serviceName?uncap_first}.update${entity}(vo));
    }

    /**
    * 通过id查询${table.comment!}
    *
    * @param id 主键id
    * @return ${table.comment!}
    */
    @GetMapping("/{id}")
    @Operation(summary = "通过id查询${table.comment!}")
    public BaseResponse<${entity}DTO> getById(@PathVariable("id") Long id) {
        return ResponseHelper.success(this.${table.serviceName?uncap_first}.getById(id));
    }

    /**
    * 分页查询${table.comment!}
    *
    * @param pageQueryVo 分页信息
    * @return 分页的${table.comment!}
    */
    @GetMapping("/list")
    @Operation(summary = "分页查询${table.comment!}")
    public BaseResponse<PageResultDTO<${entity}DTO>> page(${entity}PageQueryDTO pageQueryVo) {
        return ResponseHelper.success(this.${table.serviceName?uncap_first}.page(pageQueryVo));
    }

    /**
    * 批量删除${table.comment!}
    *
    * @param idsList id集合
    * @return void
    */
    @DeleteMapping
    @Operation(summary = "批量删除${table.comment!}")
    @LogRecord(operator = OperatorEnum.DELETE, riskType = RiskTypeEnum.HIGH_LEVEL)
    public BaseResponse<Void> deleteByIds(@RequestBody
    @NotEmpty(message = "请选择需要删除的${table.comment!}") List<Long> idsList) {
        this.${table.serviceName?uncap_first}.removeBatchByIds(idsList);
        return ResponseHelper.success();
    }

}
</#if>
