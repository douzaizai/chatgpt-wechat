package cn.ricky.demo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 日志切面类
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

  private static final String TRACE_ID = "traceId";

  @Pointcut("execution(* cn..*.*Controller.*(..)) || @annotation(cn.ricky.demo.annotation.Logging)")
  public void pointcut() {
  }

  @Around("pointcut()")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    String traceId = UUID.randomUUID().toString();
    MDC.put(TRACE_ID, traceId);
    Object[] args = pjp.getArgs();
    String[] argNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
    Map<String, Object> paramsMap = new HashMap<>();
    StringBuilder sb = new StringBuilder();
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        paramsMap.put(argNames[i], args[i] != null ? args[i].toString() : "");
      }

      for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
        sb.append(entry.getKey()).append(":").append(entry.getValue()).append(";");
      }
    }
    log.info("method:{}, params:{}", pjp.getSignature().toShortString(), sb);
    try {
      return pjp.proceed();
    } catch (Throwable e) {
      log.error("pjp.proceed failed:", e);
      throw e;
    } finally {
      MDC.remove(TRACE_ID);
    }
  }
}