package org.jms.eureka.demo.client.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 提供者Controller层
 */
@RestController
@Slf4j
public class ProducerController {

    @GetMapping("/producer")
    public String producer(@RequestParam String param){
        log.info("生成者—/producer 成功执行，传入参数：{}",param);
        return "producer_"+param;
    }
}
