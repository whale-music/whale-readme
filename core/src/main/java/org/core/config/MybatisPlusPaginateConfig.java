package org.core.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
// mybatis
@MapperScan("org.core.mybatis.mapper") // 指定要扫描的Mapper接口所在的包路径
public class MybatisPlusPaginateConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // todo 添加多数据源 mysql h2 sqlite
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}