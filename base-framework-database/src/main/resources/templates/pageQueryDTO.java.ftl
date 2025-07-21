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
package ${package.DTO};

import ${package.Entity}.${entity};
import com.fuhouyu.framework.database.base.PageQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* <p>
* ${table.comment!} DTO
* </p>
*
* @author ${author}
* @since ${date}
*/
@Getter
@Setter
@ToString
@Schema(name = "${entity}PageQueryDTO", description = "${table.comment!} 分页查询的dto对象")
@EqualsAndHashCode(callSuper = false)
public class ${entity}PageQueryDTO extends PageQueryDTO<${entity}>  {

}
