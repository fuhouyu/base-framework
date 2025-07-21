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
