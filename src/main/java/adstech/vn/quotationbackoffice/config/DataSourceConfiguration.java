package adstech.vn.quotationbackoffice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataSourceConfiguration {
	@Bean(name = "dataWarehouseDatasource")
    @ConfigurationProperties("spring.warehouse-datasource")
    public DataSource dataWarehouseDatasource(){
        return DataSourceBuilder.create().build();
    }
	
	@Bean(name = "erpDatasource")
    @ConfigurationProperties("spring.datasource")
    public DataSource reportMetaDatasource(){
        return DataSourceBuilder.create().build();
    }
	
	@Bean(name = "dataWarehouseJdbcTemplate")
	@Autowired
	public NamedParameterJdbcTemplate dataWarehouseJdbcTemplate(@Qualifier("dataWarehouseDatasource") DataSource dataWarehouseDatasource) {
	    return new NamedParameterJdbcTemplate(dataWarehouseDatasource);
	}
	
	@Bean(name = "erpJdbcTemplate")
	@Autowired
	public NamedParameterJdbcTemplate reportMetaJdbcTemplate(@Qualifier("erpDatasource") DataSource erpDatasource) {
	    return new NamedParameterJdbcTemplate(erpDatasource);
	}
}
