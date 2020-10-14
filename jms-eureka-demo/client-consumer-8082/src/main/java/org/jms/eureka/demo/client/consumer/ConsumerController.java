package org.jms.eureka.demo.client.consumer;

import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.tomcat.util.http.RequestUtil;
import org.jms.eureka.demo.client.consumer.rpac.foeign.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * 消费者Controller
 */
@RestController
@Slf4j
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ProducerService producerService;

    @GetMapping("/restTemplate")
    public String restTemplateMethod(@RequestParam Map<String, String> param) {
        List<ServiceInstance> serviceInstances = this.common();
        // 因为现在是单体服务，并不是集群模式，所以只可能有一个服务，直接调用第一个即可
        ServiceInstance serviceInstance = serviceInstances.get(0);
        String paramStr = URLUtil.buildQuery(param, Charset.defaultCharset());
        String url = "%s/producer?%s";
        url = url.formatted(serviceInstance.getUri().toString(), paramStr);
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/feign")
    public String feignMethod(@RequestParam String param) {
        this.common();
        return producerService.producer(param);
    }

    /**
     * 公共部分
     */
    private List<ServiceInstance> common() {
        // 获取所有远程服务的调用地址
        List<String> services = discoveryClient.getServices();
        log.info("注册中心上服务列表有: " + services);

        // 通过服务名获取调用的服务信息
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances("client-producer");
        for (ServiceInstance serviceInstance : serviceInstances) {
            log.info("服务实例：{}\t{}\t{}\t{}",
                    serviceInstance.getServiceId(),
                    serviceInstance.getHost(),
                    serviceInstance.getPort(),
                    serviceInstance.getUri());
        }
        return serviceInstances;
    }
}
