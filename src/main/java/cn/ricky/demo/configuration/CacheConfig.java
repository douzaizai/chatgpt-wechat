package cn.ricky.demo.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Configuration
@ConfigurationProperties("cache-config")
public class CacheConfig {

  private Integer expireTime;
  private Integer maxSize;

  @Bean("caffeineCacheManager")
  public CacheManager cacheManager() {
    CaffeineCacheManager cacheManager = new CaffeineCacheManager();
    cacheManager.setCaffeine(Caffeine.newBuilder()
            // 设置最后一次写入或访问后经过固定时间过期
            .expireAfterAccess(expireTime, TimeUnit.SECONDS)
            // 缓存的最大条数
            .maximumSize(maxSize));
    return cacheManager;
  }
}