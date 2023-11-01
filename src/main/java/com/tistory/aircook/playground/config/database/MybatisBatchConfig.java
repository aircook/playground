package com.tistory.aircook.playground.config.database;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;

//@Configuration
//@MapperScan(
//        basePackages = "com.tistory.aircook.playground.repository",
//        annotationClass = Mapper.class
//)
public class MybatisBatchConfig {

    /**
     * SqlSessionTemplate 객체 생성(Batch 전용)
     * @param sqlSessionFactory
     * @return
     * @throws Exception
     */
    @Bean(name = "batchSqlSessionTemplate")
    public SqlSessionTemplate batchSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    }

}
