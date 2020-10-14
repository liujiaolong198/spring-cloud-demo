package org.jms.eureka.demo.client.consumer.rpac.foeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * foeign 方式调用服务
 */
@FeignClient(name = "client-producer")
public interface ProducerService {

    @GetMapping("/producer")
    public String producer(@RequestParam String param);
}
