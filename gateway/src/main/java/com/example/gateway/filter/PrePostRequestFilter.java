package com.example.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *  filter order global
 *  {
 *   "org.springframework.cloud.gateway.filter.LoadBalancerClientFilter@77856cc5": 10100,
 *   "org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter@4f6fd101": 10000,
 *   "org.springframework.cloud.gateway.filter.NettyWriteResponseFilter@32d22650": -1,
 *   "org.springframework.cloud.gateway.filter.ForwardRoutingFilter@106459d9": 2147483647,
 *   "org.springframework.cloud.gateway.filter.NettyRoutingFilter@1fbd5e0": 2147483647,
 *   "org.springframework.cloud.gateway.filter.ForwardPathFilter@33a71d23": 0,
 *   "org.springframework.cloud.gateway.filter.AdaptCachedBodyGlobalFilter@135064ea": 2147483637,
 *   "org.springframework.cloud.gateway.filter.WebsocketRoutingFilter@23c05889": 2147483646
 * }
 */
@Component
public class PrePostRequestFilter implements GlobalFilter, Ordered {

    final Logger logger = LoggerFactory.getLogger(PrePostRequestFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("pre log");
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();

        if(method.equals(HttpMethod.GET.toString())) {
            Map map = request.getQueryParams();
            HttpHeaders httpHeaders = HttpHeaders.writableHttpHeaders(request.getHeaders());
            httpHeaders.add("uuid", UUID.randomUUID().toString().replace("-", ""));
            logTrace(exchange, map.toString());
        }

        return chain
                .filter(exchange)
                .then(Mono.fromRunnable(() -> logger.info("post log")));
    }

    private void logTrace(ServerWebExchange exchange, String param) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = serverHttpRequest.getURI().getPath();
        String method = serverHttpRequest.getMethodValue();
        String headers = serverHttpRequest.getHeaders().entrySet()
                .stream()
                .map(entry -> "            " + entry.getKey() + ": [" + String.join(";", entry.getValue()) + "]")
                .collect(Collectors.joining("\n"));
        logger.info("\n" + "----------------             ----------------             ---------------->>\n" +
                        "HttpMethod : {}\n" +
                        "Uri        : {}\n" +
                        "Param      : {}\n" +
                        "Headers    : \n" +
                        "{}\n" +
                        "\"<<----------------             ----------------             ----------------"
                , method, path, param, headers);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
