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
package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ${package.Assembler}.${entity?uncap_first}ssembler;
import com.fuhouyu.framework.database.base.PageResultDTO;
import ${package.DTO}.${entity}PageQueryDTO;
import ${package.DTO}.${entity}DTO;
import ${package.Mapper}.${table.mapperName};
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

<#if generateService>
import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>()<#if generateService>, ${table.serviceName}</#if> {

}
<#else>
@Slf4j
@RequiredArgsConstructor
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}><#if generateService> implements ${table.serviceName}</#if> {

    private final ${entity?uncap_first}ssembler ${entity?uncap_first}ssembler;

    @Override
    public Long save${entity}(${entity}DTO dto){
        ${entity} entity = ${entity?uncap_first}ssembler.toEntity(dto);
        this.baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public Boolean update${entity}(${entity}DTO dto){
        return this.baseMapper.updateById(${entity?uncap_first}ssembler.toEntity(dto)) > 0;
    }

    @Override
    public ${entity}DTO getById(Long id){
        return ${entity?uncap_first}ssembler.toDTO(this.baseMapper.selectById(id));
    }

    @Override
    public PageResultDTO<${entity}DTO> page(${entity}PageQueryDTO pageQuery){
        LambdaQueryWrapper<${entity}> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        return PageResultDTO.buildPageResult(this.baseMapper.selectPage(pageQuery, lambdaQueryWrapper), ${entity?uncap_first}ssembler::toDTO);
    }
}
</#if>
