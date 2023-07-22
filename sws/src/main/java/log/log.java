package log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Aspect
public class log {
    @Around("@annotation(action)")
    public Object before(ProceedingJoinPoint point, Action action) throws Throwable {
        String classname = point.getTarget().getClass().getName();
        String methonname = point.getSignature().getName();
        String description = action.description();

        log.info("类名：{}， 方法名：{}，此方法描述：{}", classname, methonname, description);
        long time = System.currentTimeMillis();
        Object proceed = point.proceed();
        long time2 = System.currentTimeMillis();
        long spendtime = time2 - time;
        log.info("方法：{}，执行耗时：{}", methonname, spendtime);

        return proceed;



    }
}
