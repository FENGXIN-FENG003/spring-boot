# WebMvcConfigurer
## 生效条件
```java
@AutoConfiguration(after = { DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class,
		ValidationAutoConfiguration.class })//在这些配置之后生效
@ConditionalOnWebApplication(type = Type.SERVLET)//web应用生效 type=SERVLET REACTIVE
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@ImportRuntimeHints(WebResourcesRuntimeHints.class)
public class WebMvcAutoConfiguration {
```
## WebMvcAutoConfigurationAdapter
### 效果
```java
// 表单提交Rest请求 默认只有GET,POST，使用此filter,可以提交PUT,DELETE请求
@Bean
@ConditionalOnMissingBean(HiddenHttpMethodFilter.class)
@ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = "enabled")
public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
    return new OrderedHiddenHttpMethodFilter();
}
// 默认只有GET,POST携带数据，允许PUT,DELETE携带数据
@Bean
@ConditionalOnMissingBean(FormContentFilter.class)
@ConditionalOnProperty(prefix = "spring.mvc.formcontent.filter", name = "enabled", matchIfMissing = true)
public OrderedFormContentFilter formContentFilter() {
    return new OrderedFormContentFilter();
}
```

```java
@Configuration(proxyBeanMethods = false)
@Import(EnableWebMvcConfiguration.class)
@EnableConfigurationProperties({ WebMvcProperties.class, WebProperties.class })
@Order(0)
public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ServletContextAware {
```
给容器放入`WebMvcConfigurer`可以自己定制SpringMvc各种功能<br>
`WebProperties`绑定配置：`spring.web`
`WebMvcProperties`绑定配置：`spring.mvc`
### WebMvcConfigurer

1. `getMessageCodesResolver`：获取消息代码解析器。用于将错误代码转换为相应的错误消息。
2. `getValidator`：获取验证器。用于验证请求数据是否符合预期的规则。
3. `extendHandlerExceptionResolvers`：扩展异常处理器。用于处理请求处理过程中的异常。
4. `configureHandlerExceptionResolvers`：配置异常处理器。用于配置异常处理器的顺序和处理规则。
5. `extendMessageConverters`：扩展消息转换器。用于将请求数据转换为Java对象，或将Java对象转换为响应数据。
6. `configureMessageConverters`：配置消息转换器。用于配置消息转换器的顺序和处理规则。
7. `addArgumentResolvers`：添加参数解析器。用于解析请求中的参数，并将其转换为Java对象。
8. `addReturnValueHandlers`：添加返回值处理器。用于处理方法返回值，并将其转换为响应数据。
9. `configureViewResolvers`：配置视图解析器。用于将视图名称转换为实际的视图对象。
10. `addViewControllers`：添加视图控制器。用于根据请求路径直接返回视图，而不需要经过Controller层。
11. `addCorsMappings`：添加跨域映射。用于配置跨域请求的处理规则。
12. `addResourceHandlers`：添加资源处理器。用于处理静态资源（如HTML、CSS、JavaScript等）的请求。
13. `addInterceptors`：添加拦截器。用于拦截请求和响应，并对它们进行处理。
14. `addFormatters`：添加格式化器。用于格式化请求和响应数据，例如将字符串转换为日期、数字等。
15. `configureDefaultServletHandling`：配置默认Servlet处理。用于处理无法匹配任何Controller的请求。
16. `configureAsyncSupport`：配置异步支持。用于配置异步请求的处理规则。
17. `configureContentNegotiation`：配置内容协商。用于根据请求的Accept头确定要返回的响应内容类型。
18. `configurePathMatch`：配置路径匹配。用于配置路径匹配规则，例如是否启用路径参数的通配符匹配。

### 静态资源
![img_1.png](image/img_1.png)
```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    if (!this.resourceProperties.isAddMappings()) {
        logger.debug("Default resource handling disabled");
        return;
    }
    // 当以`private String webjarsPathPattern = "/webjars/**"`请求时 将进入classpath:/META-INF/resources/webjars/ 寻找资源（this.mvcProperties.getWebjarsPathPattern()）
    // 导入webjar依赖 请求路径 == 进入webjar类路径访问资源
    addResourceHandler(registry, this.mvcProperties.getWebjarsPathPattern(),
    "classpath:/META-INF/resources/webjars/");
    // 当以`private String staticPathPattern = "/**";`请求时 将进入四个类路径寻找资源（this.mvcProperties.getStaticPathPattern())
   /*  
   this.resourceProperties.getStaticLocations()
   
   private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
    "classpath:/META-INF/resources/",
    "classpath:/resources/",
    "classpath:/static/",
    "classpath:/public/" };
    
    private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS; 
    */
    /*
     * 1. classpath:/META-INF/resources/
     * 2.classpath:/resources/
     * 3.classpath:/static/
     * 4.classpath:/public/
     */
    addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
        registration.addResourceLocations(this.resourceProperties.getStaticLocations());
        if (this.servletContext != null) {
            ServletContextResource resource = new ServletContextResource(this.servletContext, SERVLET_LOCATION);
            registration.addResourceLocations(resource);
        }
    });
}
```
```java
private void addResourceHandler(ResourceHandlerRegistry registry, String pattern, Consumer<ResourceHandlerRegistration> customizer) {
if (registry.hasMappingForPattern(pattern)) {
return;
}
ResourceHandlerRegistration registration = registry.addResourceHandler(pattern);
customizer.accept(registration);
    /* 
    1.this.resourceProperties:
    在WebMvcAutoConfiguration中：
    2.private final Resources resourceProperties;
    Resources属于配置类：
    @ConfigurationProperties("spring.web")
    public class WebProperties { 
    由此在spring.web配置文件进行缓存策略配置
    */
    // 设置缓存生命周期 默认 0 
    registration.setCachePeriod(getSeconds(this.resourceProperties.getCache().getPeriod()));
    // HTTP缓存控制（前端）
    registration.setCacheControl(this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl());
    // 最后一次修改 如果浏览器缓存资源last time != 服务器资源 last time 则重新获取资源 否则使用缓存资源 减少向服务器的请求
    registration.setUseLastModified(this.resourceProperties.getCache().isUseLastModified());
    customizeResourceHandlerRegistration(registration);
}
```
```properties
# 设置资源缓存周期 单位：s
spring.web.resources.cache.period=3600
# 更详细的缓存策略控制 设置此属性，将重写spring.web.resources.cache.period
spring.web.resources.cache.cachecontrol.max-age=7200
# 设置是否开启最新修改时间
spring.web.resources.cache.use-last-modified=true
```
## EnableWebMvcConfiguration
### 欢迎页
1. 当没有`WebMvcConfigurationSupport`时组件生效 生效之后 developer自己可以使用`WebMvcConfigurationSupport`
```java
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)

public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration implements ResourceLoaderAware { 
    
public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {
}
```
2. `welcomePageHandlerMapping`找到对应的handler方法 
```java
@Bean
public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext, FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
    // 返回欢迎页
    return createWelcomePageHandlerMapping(applicationContext, mvcConversionService, mvcResourceUrlProvider, WelcomePageHandlerMapping::new);
}
```
```java
private <T extends AbstractUrlHandlerMapping> T createWelcomePageHandlerMapping(ApplicationContext applicationContext, FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider, WelcomePageHandlerMappingFactory<T> factory) {TemplateAvailabilityProviders templateAvailabilityProviders = new TemplateAvailabilityProviders(applicationContext);
    // 根据private String staticPathPattern = "/** 获取静态资源
    String staticPathPattern = this.mvcProperties.getStaticPathPattern();
    // 获取html欢迎页 getIndexHtmlResource()
    T handlerMapping = factory.create(templateAvailabilityProviders, applicationContext, getIndexHtmlResource(), staticPathPattern);
    
    handlerMapping.setInterceptors(getInterceptors(mvcConversionService, mvcResourceUrlProvider));
    
    handlerMapping.setCorsConfigurations(getCorsConfigurations());
    
    return handlerMapping;
}
```
```java
private Resource getIndexHtmlResource() {
    for (String location : this.resourceProperties.getStaticLocations()) {
        /* 根据 
        1. classpath:/META-INF/resources/
        2.classpath:/resources/
        3.classpath:/static/
        4.classpath:/public/ 遍历获取Resource欢迎页 */
        
        Resource indexHtml = getIndexHtmlResource(location);
        if (indexHtml != null) {
            return indexHtml;
        }
    }
    ServletContext servletContext = getServletContext();
    if (servletContext != null) {
        return getIndexHtmlResource(new ServletContextResource(servletContext, SERVLET_LOCATION));
    }
    return null;
}
// 调用
private Resource getIndexHtmlResource(String location) {
    return getIndexHtmlResource(this.resourceLoader.getResource(location));
}

private Resource getIndexHtmlResource(Resource location) {
    try {
        // 获取欢迎页index.html 浏览器默认寻找此资源加载
        Resource resource = location.createRelative("index.html");
        if (resource.exists() && (resource.getURL() != null)) {
            return resource;
        }
    }
    catch (Exception ex) {
        // Ignore
    }
    return null;
}
```
3. favicon.ico browser默认规则 访问index.html会请求favicon.ico资源加载 和springboot没有太大关系<br>
![img.png](image/img.png)