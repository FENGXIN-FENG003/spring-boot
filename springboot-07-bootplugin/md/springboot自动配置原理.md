# boot自动配置
## boot的pom文件
### 依赖不用添加版本号原因
每个boot项目都有父工程
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.2</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```
父工程spring-boot-starter-parent有父工程spring-boot-dependencies依赖版本管理 其中配置好了所有依赖版本
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>3.3.2</version>
</parent>
```
### 自动配置原理
1. 导入场景启动器 每个场景启动器都有spring-boot-starter
```xml
   <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
```
2. spring-boot-starter导入所有配置类
```xml
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-autoconfigure</artifactId>
      <version>3.3.2</version>
      <scope>compile</scope>
    </dependency>
```
3. 此包中包含开发的所有整合配置 但并不是全部生效，是按需生效的
在App中，`@SpringBootApplication`有`@EnableAutoConfiguration`
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
```
`@EnableAutoConfiguration`中有注解`@Import(AutoConfigurationImportSelector.class)`进行批量导入`AutoConfigure`
```java
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
```
`AutoConfigurationImportSelector.java`中有`selectImports`方法 调用`getAutoConfigurationEntry`方法
```java
@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return NO_IMPORTS;
		}
		AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
		return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
	}
```
`getAutoConfigurationEntry`调用`getCandidateConfigurations`，最终得到所有的`AutoConfigure` 只有导入相应的场景时才会生效
```java
protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
		if (!isEnabled(annotationMetadata)) {
			return EMPTY_ENTRY;
		}
		AnnotationAttributes attributes = getAttributes(annotationMetadata);
		List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
```

4. 核心参数
在`AutoConfigure`中会有条件注解`@ConditionalOnClass(Xxx.class)`，当引入场景启动器会导入其中的类，条件成立bean组件生效
其中核心参数会从`xxxProperties`类对象提取 且使用`@EnableConfigurationProperties(xxxProperties.class)`属性绑定
```java
@EnableConfigurationProperties({ServerProperties.class})
@Import({BeanPostProcessorsRegistrar.class, ServletWebServerFactoryConfiguration.EmbeddedTomcat.class, ServletWebServerFactoryConfiguration.EmbeddedJetty.class, ServletWebServerFactoryConfiguration.EmbeddedUndertow.class})
public class ServletWebServerFactoryAutoConfiguration {
}
```
```java
@Bean
    @ConditionalOnClass(
        name = {"org.apache.catalina.startup.Tomcat"}
    )
    public TomcatServletWebServerFactoryCustomizer tomcatServletWebServerFactoryCustomizer(ServerProperties serverProperties) {
        return new TomcatServletWebServerFactoryCustomizer(serverProperties);
    }
```
`xxxProperties`中使用`@ConfigurationProperties`进行属性绑定 从配置文件中绑定核心参数
```java
@ConfigurationProperties(
    prefix = "server",
    ignoreUnknownFields = true
)
public class ServerProperties {
```
