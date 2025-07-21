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
package ${package.Assembler};

import com.fuhouyu.framework.database.base.BaseAssembler;
import ${package.Entity}.${entity};
import ${package.DTO}.${entity}DTO;
import org.mapstruct.Mapper;

/**
* <p>
* ${table.comment!} 转换器
* </p>
*
* @author ${author}
* @since ${date}
*/
@Mapper(componentModel = "spring")
public interface ${entity}Assembler extends BaseAssembler<${entity}, ${entity}DTO> {

}
