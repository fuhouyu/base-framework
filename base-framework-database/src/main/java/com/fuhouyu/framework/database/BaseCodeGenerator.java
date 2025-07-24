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
package com.fuhouyu.framework.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.fuhouyu.framework.database.base.BaseEntity;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 代码生成器的基类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/9 19:50
 */
public abstract class BaseCodeGenerator {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    private static final String PARENT_PATH = String.format("%s/%s", Paths.get(System.getProperty("user.dir")),
            "src/main/java");


    public boolean initCodeGenerator() {
        String currentPackage = this.getClass().getPackage().getName();

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder
                        .author("code generator")
                        .outputDir(PARENT_PATH)
                        .commentDate("yyyy-MM-dd")
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()
                )
                .packageConfig(builder -> builder
                        .entity("domain")
                        .mapper("mapper")
                        .service("service")
                        .parent(currentPackage)
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper"))
                )
                .strategyConfig(builder -> builder
                        .addInclude(this.getIncludeTableList())
                        .entityBuilder()
                        .enableLombok()
                        .addIgnoreColumns("created_by", "updated_by", "created_at", "updated_at", "is_deleted")
                        .superClass(BaseEntity.class)
                        .idType(IdType.ASSIGN_ID)
                        .enableSerialAnnotation()
                        // mapper
                        .mapperBuilder()
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                        // controller 层
                        .controllerBuilder()
                        .enableRestStyle()
                ).injectionConfig(cfg -> {

                    // vo
                    cfg.customFile(fileBuilder -> fileBuilder
                            .fileName("DTO.java")
                            .packageName("dto")
                            .templatePath("/templates/dto.java.ftl"));
                    // assembler
                    cfg.customFile(fileBuilder -> fileBuilder
                            .fileName("Assembler.java")
                            .packageName("assembler")
                            .templatePath("/templates/assembler.java.ftl"));

                    // pageQueryVO
                    cfg.customFile(fileBuilder -> fileBuilder
                            .fileName("PageQueryDTO.java")
                            .packageName("dto")
                            .templatePath("/templates/pageQueryDTO.java.ftl"));

                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
        return true;
    }

    /**
     * 需要生成的表
     *
     * @return 表集合
     */
    public abstract List<String> getIncludeTableList();
}
