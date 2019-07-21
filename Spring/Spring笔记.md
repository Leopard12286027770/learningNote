IOC invertiont of control =  DI  dependency injection

Spring整合JavaEE的各种功能

在eclipse里安装 spring tool suite插件，辅助
只选择带有spring IDE的项目安装       网盘集合：https://blog.csdn.net/qq_37872792/article/details/81149261

创建Spring Bean Configuration File   放在src目录下
<bean id="起名字" calss="自建类的全类名">
	<property name="属性名" value="幅值"></property>
</bean>
自动调用构造函数（无参）创建类的容器，用setter方法设置内部属性值
创建spring IOC容器的对象：   创建容器的时候就调用了构造函数和setter
ApplicationContext ctx = new ClassPathXmlApplicationContext("刚才创建的xml文件");  
还可以用FileSystemXmlApplicationContext();
从IOC容器中获取Bean实例
id获取一定要强制类型转换   eclipse快捷键  类型修正，全类名补全、导入 ctrl+1   1   23456
HelloWorld helloWorld =(HelloWorld ) ctx.getBean("起的id名")；
或者                  = ctx.getBean(HelloWorld.class)； 要求这个类只定义了一个bean

传统：组件向容器请求资源  IOC：容器推送资源给组件，组件选择接收方式

要求必须有无参构造器，IOC在使用<bean class="全类名">时使用无参构造器进行反射
不写无参直接写有参 就不会默认生成无参构造器
bean的id要唯一
默认生成的是单例的Bean，可以不是单例

注入方法（bean设置属性值）
属性注入 setter方法，使用<property>   最常用
构造器注入   用<constructor-arg>注入   按照构造器的参数顺序传入 多个标签
	<constructor-arg value="abc" type="java.lang.String"></constructor-arg>
	多个同数量构造器，用type进行匹配区分重载
	还可以在constructor里面用value标签进行赋值
	如果有特殊字符，可以用<![CDATA[]]>封装字符   比如<> 
	<value><![CDATA[<SHANGHAI^>]]> </value>
	bean里封装bean可以用property标签，name=内部的属性名，ref=其他的bean的id    可以自动关联
	也可以在property里面的ref标签写
	还可以直接在property标签里面写一个内部bean  可以没有id
	带参构造，constructor-arg标签，用ref指向bean也一样
	constructor-arg赋null，需要 <constructor-arg><null/></constructor-arg>
	
	级联属性赋值（少用）    property标签里name 可以是级联的 如person里封装car，person的bean直接设置car.speed
	调用的car的speed的setter    注入要对应方法名，不是属性名如setSpeed2 函数里speed赋值，xml name写speed2
	但级联属性赋值需要先把内部bean配置好，否则为null，无法获取setter
	因为是单例，原来的car 的bean的属性也被更改
	
	如果有list的属性，property标签里写list标签，list标签里面再写<ref bean="car"/> <ref bean="car2"/>
	list标签里面同样可以写内部bean
	类似的有<set> <map>
	<map>里用<entry key="AA" value-ref="car2"></entry> 添加成员
	
	Properties是HashTable的子类，实现Map接口
	给properties赋值要用property->props->prop标签，  <prop key="password">123456</prop>
	
	可以配置独立的集合bean，供多个bean引用
	把namespace标签里的util勾选，添加util:lis标签
	里面可以配置内部bean，也可以加ref
	其他bean引用的时候<property name="cars" ref="cars"></property>  用ref就行
	
	p命名空间：为bean属性赋值   更简洁
	把namespace标签里的p勾选  
	<bean id="person5" class="com.abc.spring.bean.collections.Person" p:age="30"
	p:name="Perry" p:age-ref="cars"
	></bean>
	
工厂方法注入（很少用，不推荐）

自动装配：在<bean>的autowire属性里指定模式  byType要求类型唯一    byName目标bean的id    constructor（不推荐使用）
	<bean id="person" class="com.abc.spring.bean.autowire.Person"
	p:name="Jeson" autowire="byName"></bean>
	要求其他bean的配置id与自动配置的bean的setter风格的属性名一致  id= "car2"  setCar2()｛.....｝
	byType 则是按照属性类型，要求该类型配置的bean唯一
自动装配不够灵活，用的少     但应用第三方框架工具时用的多

继承：
先定义好address 然后，要改的属性重新配置    用parent属性指定继承  autowire和 abstract属性不继承
<bean id="address2"  p:street="Buzhidao" parent="address"></bean>
如果配置的bean是一个模版，没有实例，需要abstract=“true”， 只能被其他bean继承
如果bean的class没指定，则必须是abstract

依赖：
在生成一个bean前必须先生成另一个bean，没有就报错，而不是默认null
depends-on="car"  必须有car的bean 但可以不用    多个依赖可以逗号，空格

bean的作用域   scope
bean默认是单例，只有一个实例，所有参考指向同一个
xml写bean的时候配置属性 scope=""  默认singleton  改为prototype就不是单例，每次获取产生新的bean
singleton模式，在创建容器（ApplicationContext）时已经调用无参构造函数，生成实例
prototype模式，获取bean的时候才调用构造函数    懒加载

调入外部文件：
C3P0是一个开源的JDBC连接池，它实现了数据源和JNDI绑定，支持JDBC3规范和JDBC2的标准扩展
jdbc:mysql:///company等同于 jdbc:mysql://localhost:3306/company 
company指的是数据库名称也就是说第三个'/'代表 'localhost:3306/'

spring配置xml里： dataSource的Bean
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
	<property name="driverClass" value="com.mysql.jdbc.Driver"></property>
	<property name="jdbcUrl" value="jdbc:mysql:///test"></property></bean>
独立配置文件：  db.properties
user=root
password=123456
driverClass=com.mysql.jdbc.Driver
jdbcUrl=jdbc:mysql:///test

使用PropertyPlaceholderConfigurer的BeanFactory后置处理器，用${var}引用外部文件属性
勾选namespace的context   使用<context:property-placeholder location="classpath:db.properties"/>   后缀名不必须
就可以调用外部文件中的属性值
<property name="user" value="${user}"> </property>

SpEL：#{}界定，为bean动态赋值
赋字面值， #{12.536} #{1e5}  #{'another word'}字符串用单引号   #{false}   
引用其他对象，value="#{对象id}"    
<property name="car" value="#{car}"></property>    之前用ref="car"
应用对象属性 value="#{id.property}"   ref做不到
方法链式操作   value="#{abcGenerator2.toString().toUpperCase()}"
计算与静态方法<property name="tyrePerimeter" value="#{T(java.lang.Math).PI * 80}"></property> 
三元表达式<property name="info" value="#{car.price>300000?'GOLD':'white'}"></property>

写bean的时候可以指定初始化方法init-method与销毁方法destroy-method    指定的是类内部方法名
ApplicationContext是接口，没有close()方法，实现类ClassPathXmlApplicationContext 有close()
执行顺序：调用构造器->调用setters->调用init   close()调用destroy
Bean的后置处理器，在调用初始化方法前后，处理bean，处理的是所有bean生成对象，非单例
应用：检查属性值合法，按标准更改bean属性
自定义类实现BeanPostProcessor接口   xml里配置processor的bean只需要class
postProcessBeforeInitialization 在setter调用之后，init方法之前   传入原始bean，String bean的id返回一个bean Object
postProcessAfterInitialization   在init方法之后   传入原始bean，String bean的id返回一个bean Object
可以先判断beanName是不是需要改的bean的id

静态工厂方法获取bean:     整合其他架构时用
用静态函数，返回目标实例   相当于从静态生成的Map里通过key取各个bean
构建静态方法，然后实现单例，然后在xml文件里配置bean   设置factory-method
constructor-arg value传入构造器参数

实例工厂方法：			整合其他架构时用
先创建工厂本身的bean id="carFactory"  ，再创建bean调用实例方法factory-bean="carFactory"  
设置factory-method   constructor-arg value传入构造器参数

FactoryBean：
类实现implements FactoryBean<E>   重写getObject()等方法，返回实际对象
xml写法和实例获取方法和最基本的一样  返回实际对象为E类型， 不是FactoryBean类型

基于注解：
组件扫描，自动扫描@Component @Repository  @Service @Controller
组件默认命名把第一个字母小写，也可以在注解里用value起名  @Repository("userRepo")  value为默认属性
<context:component-scan basepackage=扫描包 和子包 >   多个包用逗号隔开
resource-pattern="repository/*.class"    指定扫描包的路径与名称格式
子标签有<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Repository"/> 过滤注解 
		<context:include-filter type="assignable" expression="接口名"/> 过滤类名接口名
		component-scan有默认user-default-filter=true， 是上面四个标签，false之后就要用include-filter
可以用默认名或value名getBean
用context:component-scan还会自动注册 AutowiredAnnotationBeanPostProcessor实例
可以用@Autowired 和 @Resource   @Inject自动装配
@Autowired可以放在构造器、非public字段、一切有参数的方法上     
@Autowired(required=false) 默认true，如果没有注册的bean报错或者不报错
类型匹配，如果类型是接口，有多个实现类的bean，如果有bean的id value起的是接口同名，则用这个，没有则报错
可以再加上@Qualifier("实现类名首字母小写") 指定名称进行匹配
如果@Autowired加在数组上 Collection<E>，会把所有类型匹配的bean全装进去
在Map<String,V>上，与V类型兼容的bean装进去，Bean名为key
@Resource必须提供bean名，不给则用变量名作为bean名   没有required属性

泛型依赖注入<T>:
根据bean所继承的泛型，自动匹配拥有同样泛型的bean
a继承 A<User>  (定义为A<T>)   b继承 B<User> (定义B<T>)
A<T> 里@Autowired B<T>   在获取a的时候，自动匹配继承了<T>的b，如果b不是<User> 则找不到匹配的bean报错

AOP:
代理是AOP的实现
使用动态代理对象把原始对象封装起来，调用都要通过代理进行处理，如产生日志等重复性代码，不用每个方法都写一遍
用的是java.lang.reflect.Proxy
Calculator proxy = null;
ClassLoader loader = target.getClass().getClassLoader();
Class[] interfaces = new Class[] {Calculator.class};
InvocationHandler h= new InvocationHandler() {
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("Inside Proxy, invoking....."+" method:"
			+ method.getName()+"  args: "+ Arrays.asList(args));
		Object result = method.invoke(target, args);
		System.out.println("result:  "+result);
		return null;
	}
};
proxy=(Calculator) Proxy.newProxyInstance(loader, interfaces, h);
return proxy;
使用的时候  Calculator proxy = new LogginProxy(target).getLoggingProxy();    
不可在里面调用proxy的方法，会造成死循环

不建议直接用动态代理
用Spring的AOP    AOP底层就是动态代理   AOP抽取横切关注点，就是业务逻辑都要经过的层，如日志、参数验证
连接点：方法执行前或者执行后的点，或者抛出异常时的点    AOP通过切点定位到特定的连接点，类似查询条件
导入aspectjweaver-1.7.1.jar（版本低了可能不兼容）     com.springsource.org.aopalliance-1.0.0.jar
spring-aspects-4.0.0.RELEASE.jar    spring-aop-4.0.0.RELEASE.jar
把自定义类  @Component放在IOC容器中   @Aspect声明为切面
类的方法里 @Before就是前置   @After后置   @AfterReturning 返回结果之后执行    @AfterThrowing 抛出异常后  @Around
@Before("execution(int com.abc.spring.aop.impl.Calculator.add(int, int))")指明目标函数，要有返回类型
作用于所有方法int com.abc.spring.aop.impl.Calculator.*(int, int)    返回类型和参数类型必须一致
任意修饰符用 *         execution(* com.abc.spring.aop.impl.*.*(..))
还要在xml里注册<aop:aspectj-autoproxy></aop:aspectj-autoproxy>
Before下的方法的参数里加Joinpoint joinpoint获取对象信息（org.aspectj.lang.JoinPoint，还有另一个Joinpoint不能用）
方法名：String name = joinPoint.getSignature().getName();
参数：List<Object> args = Arrays.asList(joinPoint.getArgs());
@After的通知，后置通知，无论方法是否出现异常，都会执行，后置通知无法访问目标方法的执行结果
@AfterRreturning  返回之后才通知，报错不通知      标签里加returning，方法参数里带着返回的变量，就是执行结果
	@AfterReturning(value="execution(* com.abc.spring.aop.impl.Calculator.*(..))",
			returning="result")
	public void afterReturning(JoinPoint joinPoint, Object result) {
		System.out.println("method finished");
		System.out.println("result is : "+result);
	}
原理是动态代理的代码段
Object result = method.invoke(target, args);
用try-catch-finally包裹，在不同位置插入切面方法函数
@AfterThrowing就是异常抛出时，在catch里执行，
@AfterReturning(value="execution(* com.abc.spring.aop.impl.Calculator.*(..))"，throwing="e")
获取异常，e，在方法参数里加   Exception e， Exception类型可以指定
相当于 catch(NullPointerException e)只会捕捉指定的Exception类型
@Around   需要参数  public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint)
环绕通知类似动态代理的全过程！！！！！！！！
proceedingJoinPoint参数可以决定是否执行目标方法，而且必须有返回值
返回值就是目标方法的返回值       执行过程要用try-catch
try {
	return proceedingJoinPoint.proceed();
} catch (Throwable e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return null;

切面优先级设置：在@Aspect上加@Order(2) 值越小，优先级越高
切入点表达式"execution(* com.abc.spring.aop.impl.Calculator.*(..))"可以复用，定义一个方法，没有内容
@Pointcut("execution(* com.abc.spring.aop.impl.Calculator.*(..))")
public void declareJoinPointExpression() {}
其他方法  	@Before("declareJoinPointExpression()")就可以引用     weaver版本仍然要与JDK兼容否则报错
包外的方法也可以AAAA.declareJoinPointExpression()引用

基于xml配置切面：
<bean id="loggingAspect" class="a.b.c.d.e.LoggingAspect"></bean>
<aop:config>
	<aop:pointcut expression="execution(*  a.c.b.d.e.*.*(..))"  id="pointcut">
	<aop:aspect ref="loggingAspect" order="1" >
		<aop:before method="beforeMethod" point-ref="pointcut"/> 如果是返回和异常通知，可以加throwing、returning属性
	</aop:aspect>
</aop:config>

JdbcTemplate
update(String sql,Object args)增、改   batchUpdate(String sql, List<Object[]> batchArgs) 批量执行
public <T> T queryForObject(String sql, PrameterizedRowMapper<T> rm, Object args)
public <T> List<T> query(同上)  查多行

先配置C3P0数据源
<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
	user   password.....
	<property name="driverClass" value="${jdbc.driverClass}"></property>
	<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property></bean>
连接数据库的url参数要加jdbc.jdbcUrl=jdbc:mysql:///spring4?useUnicode=true&characterEncoding=UTF8&Timezone=GMT
sql.DataSource类获取连接
配置Spring的JdbcTemplate   获取jdbcTemplate只能通过ctx.getBean  UnitTest和main里不能用@Autowired
	<bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
		<property name="dataSource" ref="dataSource"></property></bean>
取多列的值要用
RowMapper<Employee> rowMapper= new BeanPropertyRowMapper<>(Employee.class); 定义结果映射
sql里要用别名转换驼峰命名属性名    参数占位符用  ？
public <T> T queryForObject(String sql, PrameterizedRowMapper<T> rm, Object args)
不支持级联属性映射，pojo里封装pojo，不能直接赋值    jdbcTemplate只是一个小工具，复杂功能要用ORM框架
查多行时query(String sql, PrameterizedRowMapper<T> rm, Object args)，不用queryForList的，ForList只能查一个列
参数里有ElementType的就是查询单个属性值，或者一个count()
jdbcTemplate是线程安全的
同样的方法，可以写一个类继承JdbcDaoSupport，再注入dataSource，再getJdbcTemplate    不推荐

NamedParameterJdbcTemplate
	<bean id="namedParameterTemplate" class="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate">
		<constructor-arg ref="dataSource"></constructor-arg></bean>
传参数可以用:lastName, :email 替代？ 需要传入一个Map     类似myBatis
也可以直接传入一个pojo对象  封装参数类型是SqlParameterSource
SqlParameterSource parameterSource=new BeanPropertySqlParameterSource(employee);

事务就是一系列动作，要么全完成，要么全不做
Spring里的事务管理器是通用的，hibernate、mybatis和jdbc一样的用法
核心：Interface PlatformTransactionManager   
不同的工具都有对应的TransectionManager实现类 如DataSourceTransectionManager（JDBC）

配置事务管理器
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"></property></bean>
开启tx命名空间，加<tx:annotation-driven transaction-manager="transactionManager"/>
之后就可以在事务函数上加@Transactional
例：事务包含A、B执行，B报错，但A已经执行，@Transactional就可以自动把A回滚掉
@Transactional(propagation=Propagation.REQUIRED)事务传播行为，事务调用事务时起作用   加在被调事务上
Propagation.REQUIRED（默认），使用父事务， Propagation.REQUIRES_NEW，挂起父事务，开启新事务处理
例： 买两本书，钱够第一本，不够第二本   REQUIRED两本都不能买   REQUIRES_NEW第一本能买

事务隔离级别，针对脏读，不可重复读，幻读
@Transactional(isolation=)
（1）Read uncommitted(未授权读取、读未提交)：
1）其他事务读未提交数据，出现脏读；
2）如果一个事务已经开始写数据，则另外一个事务则不允许同时进行写操作，
但允许其他事务读此行数据。该隔离级别可以通过“排他写锁”实现。
3）避免了更新丢失，却可能出现脏读。也就是说事务B读取到了事务A未提交的数据。
（读未提交：一个事务写数据时，只允许其他事务对这行数据进行读，所以会出现脏读，事务T1读取T2未提交的数据）
（2）Read committed（授权读取、读提交）：   最常用
1）允许写事务，所以会出现不可重复读
2）读取数据的事务允许其他事务继续访问该行数据，但是未提交的写事务将会禁止其他事务访问该行。
3）该隔离级别避免了脏读，但是却可能出现不可重复读。事务A事先读取了数据，事务B紧接了更新了数据，
并提交了事务，而事务A再次读取该数据时，数据已经发生了改变。
（读已提交：读取数据的事务允许其他事务进行操作，避免了脏读，但是会出现不可重复读，
事务T1读取数据，T2紧接着更新数据并提交数据，事务T1再次读取数据的时候，和第一次读的不一样。即虚读）
（3）Repeatable read（可重复读取）：
1）禁止写事务；
2）读取数据的事务将会禁止写事务（但允许读事务），写事务则禁止任何其他事务。
3）避免了不可重复读取和脏读，但是有时可能出现幻读。这可以通过“共享读锁”和“排他写锁”实现。
（可重复读：读事务会禁止所有的写事务，但是允许读事务，避免了不可重复读和脏读，但是会出现幻读，
即第二次查询数据时会包含第一次查询中未出现的数据）
（4）Serializable（序列化）：
1）禁止任何事务，一个一个进行；
2）提供严格的事务隔离。它要求事务序列化执行，事务只能一个接着一个地执行，但不能并发执行。
如果仅仅通过“行级锁”是无法实现事务序列化的，必须通过其他机制保证新插入的数据不会被刚执行查询操作的事务访问到。
3）序列化是最高的事务隔离级别，同时代价也花费最高，性能很低，一般很少使用，在该级别下，
事务顺序执行，不仅可以避免脏读、不可重复读，还避免了幻读。

还有回滚属性，默认为所有运行异常都回滚    rollbackFor=｛class｝，noRollbackFor=｛class｝
readOnly=false属性：表明事务只读，不改，帮助数据库引擎优化事务
timeOut属性，超时强制回滚之前事务的占用时间，单位s， Thread.sleep(xxxx)单位ms

基于xml配置事务：
@Autowired的都要去掉@Autowired加setter，就可以在xml里生成bean的时候用property关联原来需要@Autowired的其他bean
xml里就是bean的写法添加事务类
配置事务管理器
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"></property></bean>
配置事务属性
	<tx:advice id="txAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<tx:method name="get*" read-only="true"/>    方法名和对应的配置属性   name  可以用get*模糊匹配
			<tx:method name="*"/>     其他使用默认值
		</tx:attributes>
	</tx:advice>
配置切入点：
	<aop:config>
		<aop:pointcut expression="execution(* com.abc.jdbc.tx.*.*(..))" id="txPoint"/>
		<aop:advisor advice-ref="txAdvice" pointcut-ref="txPoint"/>
	</aop:config>
	
整合Hibernate
1.IOC容器生成Hibernate的SessionFactory
2.让hibernate使用spring的事务	

配置hibernate
导入jar包，创建hibernate.cfg.xml	数据源配置在IOC里面，不在hibernate里  
关联的.hbm.xml也在IOC配SessionFactory时配置   要配置基本属性，方言、SQL、显示、格式化、生成表策略、二级缓存
hibernate用的也是mapper，一个entity对应一个mapper（hbm.xml），里面配置映射

加入spring
导入jar，配置数据源
配SessionFactory    使用Spring提供的LocalSessionFactoryBean   class=LocalSessionFactoryBean
property里加 dataSource，configLocation， mappingLocations    value=“classpath:a.b.*.xml”
hibernate连接后如果数据库里没有entity的表，会自动生成（需要提前配置自动生成属性）
配置hibernateTransactionManager  <property name="sessionFactory" ref=".....">
配置aop，和上面xml配事务一样
	
在sessionFactory的bean里，可以用property name=“hibernateProperty”->props->prop的标签逐个配置hibernate属性
就可以省略hibernate.cfg.xml   不推荐
	
Spring在web中：
非web应用在main中创建
web应用在其被服务器加载时创建IOC容器：ServletContextListener---contextInitialized方法中创建IOC容器
