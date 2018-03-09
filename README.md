# spring cloud学习笔记

1.springCloud概要

1.1 springCloud说明

Spring Cloud为开发人员提供了快速构建分布式系统中一些常见模式的工具，例如配置管理，服务发现，断路器，智能路由，微代理，控制总线。(参考:https://springcloud.cc/spring-cloud-dalston.html)

1.2 springCloud特点

- 约定优于配置
- 开箱即用，快速启动
- 使用环境广泛，如pcServer/docker/云等
- 轻量级组件，组件支持丰富齐全
- 多种选型，如zk/Eureka等
- 负载均衡
- 断路器
- 分布式消息传递

2.服务注册与发现

2.1 服务发现组件功能

- 服务注册表:记录当前可用服务实例网络信息的数据库，是服务发现的核心机制
- 服务注册:服务消费者和服务提供者通过心跳机制注册到服务注册中心(springCloud推荐使用Eureka创建服务注册中心)
- 健康检查:服务发现组件长时间接收不到心跳便会删除该服务的注册信息，对外不在提供该服务的ip和端口

2.2 服务发现方式

- 客户端Eureka或zk
- 服务端Consul+nginx

2.3 创建服务注册中心(spring-cloud-eureka-server)

eureka是一个高可用的服务发现组件，无后端缓存；

eureka是一个基于REST的服务，主要用于定位运行在AWS域中的中间层服务，以达到负载均衡和中间层服务故障转移的目的；

eureka集成在spring-cloud-netflix项目中，以实现Spring Cloud的服务发现功能；

默认情况eureka server就是一个eureka client，需要指定一个server；

eureka.client.register-with-eureka:false和fetch-registry:false指明该服务是一个eureka server；

2.3.1 pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka-server</artifactId>
    </dependency>

2.3.2 application.yml

    eureka:
      instance:
        hostname: localhost
      client:
        register-with-eureka: false
        fetch-registry: false
        service-url:
          defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka

2.3.3 在启动类添加@EnableEurekaServer注解

    @SpringBootApplication
    @EnableEurekaServer
    public class SpringCloudEurekaServerApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaServerApplication.class, args);
    	}
    }

访问http://localhost:8081进入eureka server界面

2.4 创建服务提供者(spring-cloud-eureka-provider)

2.4.1 pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>

2.4.2 application.yml

    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8081/eureka/
    server:
      port: 8082
    spring:
      application:
        name: eureka-provider

2.4.3 启动类添加@EnableEurekaClient，指明是一个Eureka Client

    @SpringBootApplication
    @EnableEurekaClient
    public class SpringCloudEurekaProviderApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaProviderApplication.class, args);
    	}
    }

3.服务消费者

3.1 Ribbon基本用法

ribbon是负责客户端负载均衡

ribbon可以通过在客户端经过一系列算法来均衡调用服务，Ribbon工作是分为两步：

第一步：优先选择在同一个Zone且负载较少的eureka server

第二步：根据用户指定的策略，从Server取到的服务注册列表中选择一个地址，Ribbon提供了包括轮询/随机/根据响应时间加权等多种策略

3.1.1 在服务提供者(spring-cloud-eureka-provider)新增一个IndexController作为服务提供接口

    /**
    * com.spring.cloud.eureka.ribbon.controller.IndexController
    */
    @RestController
    public class IndexController {

        @RequestMapping(value = "/ribbon")
        public String ribbon(String name) {
            return "hello "  + name + ", welcome to ribbon world! ";
        }

    }

3.1.2 创建一个服务消费者(spring-cloud-eureka-consumer-ribbon)

pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-ribbon</artifactId>
    </dependency>

application.yml

    server:
      port: 8083
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8081/eureka/
    spring:
      application:
        name: eureka-ribbon

在启动类中添加@EnableDiscoveryClient向服务注册中心注册，并向程序的IOC注入一个RestTemplate，添加@LoadBalanced
注解表明restTemplate开启负载均衡功能。

    @SpringBootApplication
    @EnableDiscoveryClient
    public class SpringCloudEurekaConsumerApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaConsumerApplication.class, args);
    	}

    	@Bean
    	@LoadBalanced
    	RestTemplate restTemplate() {
    		return new RestTemplate();
    	}
    }

创建一个Service请求服务提供者提供的接口

    /**
    * com.spring.cloud.eureka.ribbon.service.IndexService
    */
    @Service
    public class IndexService {
        private static final String PATH = "http://EUREKA-PROVIDER/ribbon";
        @Autowired
        private RestTemplate restTemplate;
        public String hello(String name) {
            //该处做负载均衡
            return restTemplate.getForObject(PATH + "?name=" + name, String.class);
        }
    }

3.2 Ribbon负载均衡策略

- 轮询负载均衡策略:按照i=(i+1)%n的轮询调度策略调用第i台服务器
- 随机加载负载均衡策略
- 加权响应时间负载均衡:根据响应时间分配一个weight;响应时间越长,weight越小,选中概率越低
- 区域感知轮询负载均衡

3.2.1 对单一服务指定特定负载均衡策略 @RibbonClient(name = "eureka-provider", configuration = FooConfiguration.class)

com.spring.cloud.eureka.ribbon.config.TestConfiguration

    @Configuration
    @RibbonClient(name = "eureka-provider", configuration = FooConfiguration.class)
    public class TestConfiguration {
    }

com.spring.cloud.config.ribbon.FooConfiguration

    @Configuration
    public class FooConfiguration {

        @Bean
        public IRule ribbonRule(IClientConfig config) {
    //        return new BestAvailableRule(); //最小的并发请求的server
    //        return new WeightedResponseTimeRule(); //加权响应时间负载均衡
    //        return new RetryRule(); //在选定的负载均衡策略机上重试机制
    //        return new RoundRobinRule(); //roundRobin方式轮询
    //        return new ZoneAvoidanceRule(); //区域感知轮询负载均衡
            return new RandomRule();//随机加载
        }
    }

注:FooConfiguration类不应该在主应用程序可以扫描(@ComponentScan)到包及其子包中,否则该配置类被所有的@RibbonClients共享;

3.2.2 对所有服务指定特定负载均衡策略

    @RibbonClients(defaultConfiguration = {FooConfiguration.class})
    public class SpringCloudEurekaConsumerApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaConsumerApplication.class, args);
    	}

    	@Bean
    	@LoadBalanced
    	RestTemplate restTemplate() {
    		return new RestTemplate();
    	}
    }

3.3 配置文件自定义Ribbon客户端

配置文件配置格式:clientName.ribbon.前缀

- NFLoadBalancerClassName:配置com.netflix.loadbalancer.ILoadBalancer接口的实现
- NFLoadBalancerRuleClassName:配置com.netflix.loadbalancer.IRule接口的实现
- NFLoadBalancerPingClassName:配置com.netflix.loadbalancer.IPing接口的实现
- NIWSServerListClassName:配置com.netflix.loadbalancer.ServerList接口的实现
- NIWSServerListFilterClassName:配置com.netflix.loadbalancer.ServerListFilter接口的实现

application.yml配置

    eureka-provider:
      ribbon:
        NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule

3.4 Feign基本用法

> 声明式web服务客户端，具有可插入注释支持(包括Feign注释和JAX-RS注释);
> 支持可插拔编码器和解码器
> 默认集成ribbon和eureka，实现负载均衡

**_`feign服务创建`_**

- pom.xml添加配置

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-feign</artifactId>
        </dependency>

- application.yml添加配置

        server:
          port: 8085
        spring:
          application:
            name: eureka-feign
        eureka:
          client:
            service-url:
              defaultZone: http://localhost:8081/eureka/

- 启动类增加@EnableFeignClients注解开启Feign功能

        @SpringBootApplication
        @EnableEurekaClient
        @EnableFeignClients
        public class SpringCloudEurekaConsumerFeignApplication {

            public static void main(String[] args) {
                SpringApplication.run(SpringCloudEurekaConsumerFeignApplication.class, args);
            }
        }


- 创建一个接口调用服务接口,通过添加@FeignClient("serviceName")注解来指定调用哪个服务

        @FeignClient(value = "EUREKA-PROVIDER")
        public interface FeignService {

            @RequestMapping(value = "/feign", method = RequestMethod.GET)
            String feign(@RequestParam(value = "name") String name);
        }

4.断路器:Hystrix(spring-cloud-eureka-consumer-hystrix)

如果底层服务出现故障有可能会导致整个服务的连锁故障，当对特定的服务调用不可用达到一个阀值(Hystric是5秒20次)断路器将会被打开，从而避免连锁反应。

4.1 Hystrix使用

4.1.1 ribbon使用hystrix

- pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix</artifactId>
    </dependency>

- 启动类添加@EnableHystrix注解开启Hystrix

    @SpringBootApplication
    @EnableEurekaClient
    @EnableHystrix
    public class SpringCloudEurekaConsumerHystrixApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaConsumerHystrixApplication.class, args);
    	}

    	@Bean
    	@LoadBalanced
    	public RestTemplate restTemplate() {
    		return new RestTemplate();
    	}
    }

- 在service类中添加@HystrixCommand(fallbackMethod = "fallback")创建熔断器功能并指定了熔断返回的内容

    @Service
    public class HystrixRibbonService {

        @Autowired
        private RestTemplate restTemplate;

        @HystrixCommand(fallbackMethod = "fallback")
        public String index(String name) {
            return restTemplate.getForObject("http://EUREKA-PROVIDER/ribbon?name=" + name, String.class);
        }

        public String fallback(String name) {
            return "sorry, " + name + " system request error!";
        }
    }

4.1.2 断路器仪表盘

Hystrix的主要优点之一是它收集关于每个HystrixCommand的一套指标, Hystrix仪表板以有效的方式显示每个断路器的运行状况.

当使用Ribbon客户端的Hystrix命令时需要确保Hystrix超时配置长于Ribbon的超时配置, 包括可能进行的任何潜在的重试.
例如: 如果Ribbon连接超时为一秒钟, 并且Ribbon客户端可能会重试该请求三次, 这时Hystrix超时需要略超过三秒钟。

- 集成断路器仪表盘

pom.xml:

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-hystrix-dashboard</artifactId>
    </dependency>

在启动类中添加加@EnableHystrixDashboard

    @SpringBootApplication
    @EnableEurekaClient
    @EnableHystrix
    @EnableHystrixDashboard
    public class SpringCloudEurekaConsumerHystrixApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaConsumerHystrixApplication.class, args);
    	}

    	@Bean
    	@LoadBalanced
    	public RestTemplate restTemplate() {
    		return new RestTemplate();
    	}
    }

访问http://ip:port/hystrix访问仪表盘界面, 并将仪表板指向Hystrix客户端应用程序中的单个实例/hystrix.stream端点.

4.1.3 feign集成Hystrix

启动类添加@EnableFeignClients注解

    @SpringBootApplication
    @EnableEurekaClient
    @EnableHystrix
    @EnableHystrixDashboard
    @EnableFeignClients
    public class SpringCloudEurekaConsumerHystrixApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaConsumerHystrixApplication.class, args);
    	}

    	@Bean
    	@LoadBalanced
    	public RestTemplate restTemplate() {
    		return new RestTemplate();
    	}
    }

application.yml增加feign.hystrix.enabled=true表示打开断路器

    feign:
      hystrix:
        enabled: true

service:fallback指定断路器返回的异常信息类

    @FeignClient(value = "EUREKA-PROVIDER", fallback = HystrixFeignServiceError.class)
    public interface HystrixFeignService {

        @RequestMapping(value = "/feign", method = RequestMethod.GET)
        String feign(@RequestParam(value = "name") String name);
    }

异常信息类需要实现service类并且注入ioc容器类

    @Component
    public class HystrixFeignServiceError implements HystrixFeignService {
        @Override
        public String feign(String name) {
            return "sorry " + name + ", system error!";
        }
    }

5.路由器和过滤器(Zuul)

5.1 Zuul概要

Zuul是Netflix的基于JVM的路由器和服务器端负载均衡器，主要功能是用于路由转发和过滤器;

Zuul的作用:
- 认证
- 洞察
- 压力测试
- 金丝雀测试
- 动态路由
- 服务迁移
- 负载脱落
- 安全
- 静态响应处理
- 主动/主动流量管理

5.1.1 zuul创建(spring-cloud-eureka-zuul)

pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-zuul</artifactId>
    </dependency>

启动类添加@EnableZuulProxy注解开启zuul功能

    @SpringBootApplication
    @EnableEurekaClient
    @EnableZuulProxy
    public class SpringCloudEurekaZuulApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaZuulApplication.class, args);
    	}
    }

application.yml

    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8081/eureka/
    server:
      port: 9091
    spring:
      application:
        name: eureka-zuul
    zuul:
      routes:
        feign-url:
          path: /feign-url/**
          serviceId: eureka-feign
        ribbon-url:
          path: /ribbon-url/**
          serviceId: eureka-ribbon


说明：/feign-url开头的请求转发到eureka-feign服务，/ribbon-url开头的请求转发到eureka-ribbon服务

5.1.2 服务过滤

zuul可以做服务过滤，做一下安全验证。

代码示例：

    @Component
    public class IndexFilter extends ZuulFilter {
        /**
         * filterType返回一个字符串代表过滤器的类型，返回的字符串及表示的含义如下：
         * pre：在请求被路由之前调用
         * route：在请求被路由之时调用
         * post：在请求被路由之后被调用
         * error：处理请求发送错误时被调用
         *
         * @return
         */
        @Override
        public String filterType() {
            return "pre";
        }

        /**
         * 定义过滤器的执行顺序，数值越大优先级越低
         *
         * @return
         */
        @Override
        public int filterOrder() {
            return 0;
        }

        /**
         * 判断过滤器是否要过滤，true表示过滤器总是生效(这里可以进行相应的逻辑判断过滤器是否生效)
         *
         * @return
         */
        @Override
        public boolean shouldFilter() {
            return true;
        }

        /**
         * 过滤器具体的逻辑实现
         *
         * @return
         */
        @Override
        public Object run() {
            RequestContext context = RequestContext.getCurrentContext();
            HttpServletRequest request = context.getRequest();

            String name = request.getParameter("name");

            System.out.println("[IndexFilter] >>> name = " + name);

            if ("jack".equals(name)) {
                context.setResponseStatusCode(200);
                context.setSendZuulResponse(true);//true表示对该请求进行路由，false表示过滤该请求，不进行路由
                context.set("isSuccess", true);//告诉下一个Filter上一个Filter的状态
            } else {
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(1001);
                context.setResponseBody("name is error! name = " + name);//返回错误的内容
                context.set("isSuccess", false);
            }

            return null;
        }
    }

6.文件配置

6.1 创建文件配置服务端(spring-cloud-eureka-config-server)

> pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>

> application.yml

    spring:
      application:
        name: config-server
      cloud:
        config:
          server:
            git:
              uri: https://github.com/chasdream/spring-cloud-config.git
              search-paths: spring-cloud-config
          label: master
    server:
      port: 7060

> 启动类增加@EnableConfigServer, 开启配置服务器功能

    @SpringBootApplication
    @EnableConfigServer
    public class SpringCloudEurekaConfigServerApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SpringCloudEurekaConfigServerApplication.class, args);
    	}
    }

注: application.yml文件配置说明：

    spring.cloud.config.server.git.uri 配置git仓库地址
    spring.cloud.config.server.git.search-paths 配置仓库路径
    spring.cloud.config.label 配置仓库分支
    spring.cloud.config.server.git.username 配置git仓库用户名(公有仓库可省略)
    spring.cloud.config.server.git.password 配置git仓库用户密码(公有仓库可省略)

启动服务，访问http://localhost:7060/config-server/dev/master验证配置服务中心是否可以从远程仓库获取配置文件配置信息，验证信息如下:

    {
        "name":"config-server",
        "profiles":[
            "dev"
        ],
        "label":"master",
        "version":"2bd741fa42e2a7bb70a5816f8ddef3796c11737a",
        "state":null,
        "propertySources":[
            {
                "name":"https://github.com/chasdream/spring-cloud-config.git/application-dev.properties",
                "source":{
                    "name":"chasdream-dev"
                }
            }
        ]
    }

6.2 创建配置文件服务器客户端(spring-cloud-eureka-config-client)

> pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-config</artifactId>
    </dependency>

> bootstrap.yml

    spring:
      application:
        name: config-client
      cloud:
        config:
          label: master
          profile: dev
          uri: http://localhost:7060/
    server:
      port: 7061

注：文件配置客户端配置文件名必须是bootstrap命名，不能是application命名;

bootstrap.yml配置文件说明:

    spring.cloud.config.label 配置远程仓库分支
    spring.cloud.config.profile 配置选择环境的配置文件
    spring.cloud.config.uri 配置服务中心的地址

6.3 利用Eureka实现高可用服务配置中心

> 配置文件服务端及客户端pom.xml文件增加Eureka

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
    </dependency>

> 配置文件服务器application.yml配置文件修改

    spring:
      application:
        name: config-server
      cloud:
        config:
          server:
            git:
              uri: https://github.com/chasdream/spring-cloud-config.git
              search-paths: spring-cloud-config
          label: master
    server:
      port: 7060
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8081/eureka/

> 配置文件客户端bootstrap.yml配置文件修改

    spring:
      application:
        name: config-client
      cloud:
        config:
          label: master
          profile: dev
          discovery:
            service-id: config-server 文件配置中心服务名，用于替换uri
            enabled: true  表示从配置中心读取文件
          #uri: http://localhost:7060/ 用service-id替换
    server:
      port: 7061
    eureka:
        client:
          service-url:
            defaultZone: http://localhost:8081/eureka/

7.消息总线(spring cloud bus)

7.1 spring cloud bus概述

- Spring Cloud Bus将分布式系统的节点与轻量级消息代理链接，可以用于广播配置文件的更改或其他管理指令，也可用于监控和应用程序之间的通信通道;
- Spring Cloud Bus唯一的实现是通过使用AMQP代理作为传输，默认支持rabbitmq和kafka

7.2 通过配置kafka实现消息总线服务(spring-cloud-eureka-config-client)

7.2.1 config-server连接到kafka服务器

> 配置文件服务端pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-kafka</artifactId>
    </dependency>

> 配置文件服务端application.yml

    spring:
      application:
        name: config-server
      cloud:
        config:
          server:
            git:
              uri: https://github.com/chasdream/spring-cloud-config.git
              search-paths: spring-cloud-config
          label: master
        stream:
          kafka:
            binder:
              brokers: 127.0.0.1:9092
              zk-nodes: 127.0.0.1:2181
    server:
      port: 7060
    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8081/eureka/

注：
- spring.cloud.stream.kafka.binder.brokers 配置kafka服务列表
- spring.cloud.stream.kafka.binder.defaultBrokerPort 配置kafka服务默认端口，如果brokers没有配置端口这使用默认值9092
- spring.cloud.stream.kafka.binder.zk-nodes 配置zk节点列表
- spring.cloud.stream.kafka.binder.defaultZkPort 配置zk节点默认端口，如果zk-nodes没有配置端口这使用默认值2181

7.2.2 config-client连接kafka服务器

> 配置文件客户端pom.xml

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bus-kafka</artifactId>
    </dependency>

> 配置文件客户端bootstrap.yml

    spring:
      application:
        name: config-client
      cloud:
        config:
          label: master
          profile: dev
          discovery:
            service-id: config-server
            enabled: true
          #uri: http://localhost:7060/
        stream:
          kafka:
            binder:
              brokers: 127.0.0.1:9092
              zk-nodes: 127.0.0.1:2181
    server:
      port: 7061
    eureka:
        client:
          service-url:
            defaultZone: http://localhost:8081/eureka/
    management:
      security:
        enabled: false

注：
- management.security.enabled false:禁用安全管理策略
- @RefreshScope 允许动态刷新配置
- 文件配置客户端发post请求http://localhost:7061/bus/refresh 重新读取git配置文件信息

@RefreshScope示例：

    @Controller
    @RefreshScope
    public class ConfigController {

        @Value("${name}")
        public String name;

        @ResponseBody
        @RequestMapping(value = "/config")
        public String config() {
            return name;
        }
    }

