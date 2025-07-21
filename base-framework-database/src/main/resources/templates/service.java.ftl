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
package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.fuhouyu.framework.database.base.PageResultDTO;
import ${package.DTO}.${entity}DTO;
import ${package.DTO}.${entity}PageQueryDTO;

/**
* <p>
* ${table.comment!} 服务类
* </p>
*
* @author ${author}
* @since ${date}
*/
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
    * 保存 ${table.comment!}
    *
    * @param dto ${table.comment!} DTO对象
    * @return id
    */
    Long save${entity}(${entity}DTO dto);

    /**
    * 修改 ${table.comment!}
    *
    * @param dto ${table.comment!} DTO对象
    * @return id
    */
    Boolean update${entity}(${entity}DTO dto);

    /**
    * 通过id查询${table.comment!}
    *
    * @param id 主键id
    * @return ${table.comment!} DTO 对象
    */
    ${entity}DTO getById(Long id);

    /**
    * 分页查询${table.comment!}
    *
    * @param pageQuery 分页信息
    * @return 分页的${table.comment!}
    */
    PageResultDTO<${entity}DTO> page(${entity}PageQueryDTO pageQuery);
}
</#if>
