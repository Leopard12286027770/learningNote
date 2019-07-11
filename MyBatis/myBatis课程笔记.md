动MySQL:cmd:net start mysql

Eclipse 创建Bean、构造函数等的快捷键是shift+alt+s

myBatis依赖于SqlSsssionFactory
mybatis-config.xml是全局配置文件，也可以不用xml，直接全部new出对象
影射的SQL通过mapper.xml配置,mapper要注册在config.xml中
mapper的不同方法要有不同的名称标识，用namespace.id（老版本方法）
从Factory获取openSession就可以执行已经影射的SQL语句
一个sqlSession就是一次和DB的会话
session用完之后要close()

Bean的命名需要与mysql中的属性一致，否则需要在SQL影射中命名
SELECT last_name(mysql名) lastName(Bean名),id,gender

xml配置文件在conf文件夹里需要把conf文件夹build - > use as source folder
jar包全部add into build path

使用接口操作数据库
在mapper.xml中把方法的namespace指定为接口的全类名copy qualifiedName （带有包名的），再把影射id改为接口方法名，就可以绑定接口
获取接口对象，获取到的是proxy对象，自动创建的代理对象

SqlSession是非线程安全的

xml文件中的dtd文件为xml文件的标签约束，在preference中xml->xml catalog设置路径映射文件之后，可以关联出alt+/有提示，
看到标签种类和属性

properties标签：引入外部properties配置文件 resource引入类路径下资源，url引入网络或磁盘资源
类路径就是java工程内路径
properties文件可以配置config中参数等
资源整合基本交给spring做，properties标签不常用

settings标签：参考官方手册
mapUnderscoreToCamelCase： 下划线到驼峰命名映射，数据库是下划线，java bean是驼峰
typeAliases: 设置type全类名的别名，就是返回类，在xml绑定接口时用，默认别名就是类名，别名不区分大小写
里面package标签可以批量起默认alias
也可以在bean上@Alias（“abc”）来起别名
typeHandler: 数据库数据格式和Java格式转换

environments：配置环境，environment id= 标识，例如开发和测试使用的环境不同，可以直接改environments的default切换
environment必须有transectionManager和dataSource：使用的连接池

databaseIdProvider：根据不同的厂商发送不同的SQL语句，在property标签里起别名
在mapper.xml里 select标签里加databaseId="mysql" ，实现同一个接口匹配不同数据库

mappers：把映射文件注册到全局配置中 用resouce和url注册文件，class注册接口（接口必须与mapper在同文件夹下，名称一致）
mybatis支持所有sql语句写在注解里的方式，注册接口，@select写在接口内方法上（不推荐，不重要的，简单的可以这么用）
批量注册用package，xml和接口要在同一文件夹下，名字一致

eclipse中，工程下的不同文件夹下所有文件会被统一编译到bin文件夹下，所以外侧目录下的子目录名称一样，
就可以实现最终存放在同一目录下

Mapper.xml里mapper的方法标签中可以用parameterType指定参数类型，可以省略
SQL语句里直接用#｛｝就可以获得传入参数，直接写封装BEAN中的属性名

用openSession获得的session不会主动提交数据，需要手动openSession.commit()
从factory获取openSession时传入openSession(true) 就可以自动提交
Bean使用有参的构造函数前一定要加一个无参的构造函数，否则很多框架会无法工作，框架通过无参构造器反射
增删改可以定义返回值Integer，Long，Boolean 返回的是影响了多少行，只需要在接口改返回值，在xml里没有resultType，
只有查找有
JDBC里statment有getGeneratedKeys()获取自增主键
myBatis一样，用statement   在insert标签里注入getGeneratedKeys()   
keyProperty=employee.id，键值封装给JavaBean，添加后就可以在对象里查到
oracle不支持自增，使用序列来模拟自增
oracle先取序列的.nextval，再插入新记录，用取到的值作key
mybatis用selectKey标签取序列中的key    order=“BEFORE”  resultType=“Integer”
也可以把employees_seq.nextval直接放在id的位置，然后用selectKey    order=“AFTER” .currval封装给Bean 

传入参数的处理：
单个参数： #｛任意字符｝，mybatis不做特殊处理    不能有@Param
多个参数： #｛param1｝  #｛param2｝，多个参数会封装成一个map，#｛｝是从map获取key
		或者#｛0｝， #｛1｝
可以命名参数，明确封装参数时的key，在接口里  
getEmployeeByIdAndLastName(@Param("id")Integer id, @Param("lastName")String lastName);
多个参数是业务逻辑的封装模型，可以直接传入pojo    取出：#｛pojo属性名｝
也可以传入map  取出：#｛key｝
如果这个map经常使用，可以编写一个Transfer Object （TO）  ，如Page

如果参数是map，  取出：#{param1.id}
如果参数是Collection（List、Set），或者数组    取出#｛list[0]｝或者array[0],或者collection[0]
${}取出的值直接放在SQL语句中
，#｛｝以预编译的方式设置SQL语句，PreparedStatement，用？占位，与JDBC相同
推荐使用#｛｝，有分表，写表名、排序情况使用$   原生JDBC，SQL 不支持占位符的时候   字符连接
select * from ${year}_salary where xxx
............order by ${name}  ${asc}

取值#｛｝
可以指定java类型和JDBC类型，保留小数、处理器等
jdbcType：数据为null的时候，有些数据库不能识别mybatis对null的默认处理（JdbcType: OTHER），如oracle
使用#{email,jdbcType=NULL}
也可以在全局配置里，改jdbcTypeForNull=NULL

select如果返回集合，resultType要写集合中的类型
SQL语句中用like可以使用通配符， 如“%王%”    包含 王 的数据
也可以返回map key是属性名，值是对应值， resultType 就写map
返回多条记录的map：Map<Integer,Employee>：xml的resultType为Employee，在写接口的时候用@MapKey("key")

resultMap：与resultType不同时存在
resultMap指定自定义映射规则
在xml的mapper标签里有resultMap标签
type是JavaBean类型名，id是map的标签
map里面写列对应的映射名   column是DB列名，property是JavaBean属性名
主键是id，其他是result
推荐写所有列的映射写全

联合查询
在bean里封装另一个bean：
用resultMap，直接在map里写property=另一个bean.对应的属性名     级联属性封装

还可以使用association标签 指定联合javaBean对象
property是自己类Employee里的属性名（private dept），javaType是联合的全类名
association里面和resultMap一样写，这里的property就是联合的类的属性

association还能进行分步查询
先查一个表，找到另一个的id，再查另一张表
association里面加select属性，里面写要调用的另一张表的查询方法的xml的namespace.id  column属性就是传入的参数
就是用select的方法，传入column的值，封装给property

association也可以延迟加载，在使用关联表信息的时候再去查询
分步查询的基础上，在全局配置文件里加lazyLoadingEnable true，  aggressiveLazyLoading true

关联一个List：
在resultMap里加collection标签  property是自己类里的属性名， ofType定义每一个list成员的类型
分步查询也用select
association、collection如果要传多个标签，就封装成map    column="{key1=column1，key2=column2}"
fetchType可以配置是否延迟加载  lazy/eager

resultMap里可以加discriminator 鉴别某列的值，根据值改变封装行为  如是女则查询部门
column指定判断的列，javaType列值对应的java类型
用case处理不同分支 case里的resultType是封装的类型，把association 或者 result放在case内


动态SQL
if	choose(when,otherwise)	trim(where,set)	foreach
在select标签里加if标签  test就是判断表达式（OGNL）， 里面传的参数不加#{}
调用静态方法加@     @java.lang.Math@PI
特殊符号要用HTML的实体名称，不能直接转义 " = &quot;    !=  \"
条件拼装，where后面加1=1 所有的条件前面都加and
也可以用where标签，动态拼装，自动去掉多出来的and  or，所有if放在where标签里    and  or 必须放在条件前面

推荐用where标签，不手写where

trim就可以把写在前后的and   or截掉  
prefix给结果加前缀比如加where  prefixOverride 去掉前面的多余字符    
suffix加后缀   suffixOverride删后缀

choose(when,otherwise)  就是带break的switch-case   选择其中一个分支

set标签是封装修改条件 更新数据库用
用if更新多个属性值，会多出最后的  ,    用set标签+if标签自动去掉
也可以用trim

foreach用于遍历传入list   在foreach标签 collection是传入名等，
list类型的参数会自动封装进map，key名就是list，类似的还有collection ， array
item是每个成员的代名item="it"，
separator是每个元素间的分隔符如  ，   open是遍历开始时加的前缀，如(，close是后缀，如)
index="i"是索引代名，遍历map时是key代名
标签里#{it}  就能取出每个值   #{i}取索引
多个参数时封装一个map，collection可以是key

foreach也用于批量保存
MySQL多条插入可以在一个insert里values用，连接多个（）
也可以封装多个SQL语句，语句之间用;隔开，需要打开MySQL的连机器属性，允许执行多个指令
在连接mySQL的配置文件中，jdbc.url后面加？allowMultiQueries=true
oracle不支持()，()封装多个插入值   可以用begin  之后多个insert语句，;隔开   end
也可以利用中间表dual批量插入，select a,b,c,d from中间表，中间表用union连接多条数据

内置参数：
1._parameter：整个参数， 多个参数传入是会封装map，_parameter就是这个map   比如判断_parameter!=null 传入参数判断
2._databaseId：如果配置了DatabasePIdrovide标签

bind可以把表达式绑定到变量里
在select标签内 用bind标签<bind name="_lastName" value="'%'+lastName+'%'"/>拼接
之后#{_lastName}就可以调用拼接
模糊查询%是字符串，_是单个字符
如果是模糊查询，推荐查的时候传入参数就带%等，不用绑定

sql标签：抽取可重用的片段
用include标签导入
可以在include里用property标签定义变量和值，在sql标签里用${name}取出

有两级缓存，默认只有一级开启
一级：本地缓存  与数据库同一次会话的数据，同一个openSession，而且从缓存取出的是同一个对象，不是新创建的bean对象
也成为sqlSession级别缓存，每个sqlSession对象有一个缓存
在两次查询之间进行增删改操作后，缓存失效
sqlSession.clearCache()可以清空缓存 

二级：全局缓存
基于namespace的缓存，xml的namespace
一个会话关闭（或者提交commit）之后一级缓存的数据存到二级缓存中！！！！    不关不提交，查不到

在全局设定中配置setting cacheEnabled = true（默认）      一级缓存无法关闭
在mapper的xml中也要配置<cache></cache>
其中的属性：eviction：缓存回收策略，容量不够时处理   LRU（default）  FIFO  SOFT  WEAK
flushInterval： 缓存刷新（清空）间隔   默认不清
reaOnly 只读   默认从缓存读的数据不做修改，直接把缓存引用交出去，不安全
				非只读，用反序列化clone一个  （默认）
size: 缓存数据数
type 指定自定义缓存全类名，自定缓存要实现mybatis的cache接口

POJO需要实现序列化接口

如果在select标签里 useCache设置false，关闭的也是二级缓存，   一级缓存无法关闭
增删改标签里有flushCache默认true，所以会默认清空缓存，一二级缓存都清空
sqlSession.clearCache() 清的是一级缓存
localCacheScope   SESSION就是当前会话保存，STATEMENT不存数据

缓存顺序：先二级，后一级，最后数据库
缓存就是个HashMap

Ehcache 缓存框架
导入ehcache的jar包，在mybaits的github页面，有spring整合、各种缓存整合代码，下载jar包
导入jar包之后在mapper.xml里写cache 的type=ehcache全类名，就行，参考官方文档
还要在类路径下放xml配置文件

在别的mapper下可以用cache-ref标签，指定和另一个namespace下的缓存一样配置



Spring整合：
在官网有spring适配器
导入spring的jar包， Spring IoC，jdbc，webmvc，aop，JSTL
mybatis包，mysql驱动包，mybatis-spring适配包

spring配置：WebContent/WEB-INF/lib/web.xml   写入contextloaderlistener  指定spring配置文件
就是conf录下创建applicationContext.xml   是springBeanDefinition file
WebContent/WEB-INF/lib/web.xml里面再加上 SpringMVC配置    前端控制器--dispatcherservelet
servlet名：spring     同目录下创建spring-servlet.xml 作为springMVC配置文件
加context   component-scan  扫描annotation
加mvc：annotation-driven     mvc：default-servlet-handler
视图解析器   bean class=.........

SpringMVC控制网站跳转逻辑    Spring管理业务逻辑组件、数据源     AOP为Aspect Oriented Programming
数据源配置，需要<bean dataSource> 种类，如ComboPooledDataSource
需要jdbcUrl，driverClass，user，password    </bean>

用context：property-patchloader标签引入外部配置文件，${}填写用户、url，pwd等

Spring事务管理器
<bean  DataSourceTransectionManager>
	<property name="dataSource"  ref="dataSource的名字"></property>
</bean>

开启基于注解的事务
<tx:annotation-driven transection-manager=".....">

整合JDBC就用jdbcTemplate

整合myBatis
spring管理mybatis之后，可以用@Autowired自动注入mapper，不用获取sessionfactory，opensession
spring官网有jpetstore，是整合spring，mybatis的例程，可以参考配置文件

自动创建sqlSessionFactory对象
<bean sqlSessionFactoryBean>   <property dataSource> </bean>
就可以用这个bean里的property代替原来的全局配置文件的一部分，再configLocation关联全局配置文件，里面放常改动的配置
property也指定mapperLocation
mybatis-spring:scan   标签扫描dao接口文件，可以autowired
在service里
@Autowired
private EmployeeMapper employeeMapper;


根据数据库表，MyBatis Generator  （MBG）自动生成Bean 配置文件等，类似代码生成器
github的官网下有generator
写一个xml做起始配置，用官方文档的例子改
官方文档运行方法，可以在test里写一个方法，运行@Test
会生成 CRUD方法 CREATE  RETRIEVE UPDATE DELETE
是QBC风格查询方法(模仿hibernate) query by criteria   每个条件是xxxxxxExample对象    内部mapper.xml实现是动态sql嵌套
EmployeeExample  example = new EmployeeExample ;
Criteria criteria = example.createCriteria();
criteria.andLastNameLike("%e%");用条件连接实现多条件   没有or操作
如果要用，创建多个criteria，同一个example
example.or(criteria2);//实现  or   相当于where (criteria1 and  criteria1) or (criteria2)


mybatis用户使用接口层， sql语句解析通过数据处理层    配置文件：框架支持层
引导层生成SqlSessionFactory ，可以用XML也可以用java API生成
sqlSessionFactory初始化：创建sqlSessionFactoryBuilder对象.build(传入文件流)
里面创建XMLConfigBuilder  parse()    XPathParser解析器，解析XML文件，解析全局配置文件configuration节点
解析每个标签节点  解析结果放在Configuration对象
解析mapper，所有解析都有各自的解析器
statementParser解析SQL语句   一个增删改查标签，构造一个MappedStatement
然后所有配置，包括全局和mapper都放在Configuration对象中
再用这个configuration做build，new 一个DeafultSqlSEssionFactory返回

原理跳过一部分

SqlSessionFactory生成SqlSession，再用Executor执行每一个MappedStatement 增删改查
StatementHandler执行，ParameterHandler借助TypeHandler设置参数，用ResultSetHandler借助TypeHandler封装结果

每个创建出Handler对象，都要调用interceptorChain.pluginAll(Object target)     获取所有interceptor，调用plugin()方法
返回包装后的target
插件机制：为target创建代理对象，在对象运行前后可运行  和spring一样是aop
插件要实现Interceptor接口  plugin方法里用Plugin.wrap方法包装目标对象
@Intercepts({@Signature(type=Handler的class,method="handler的方法"，args=方法的参数.class))
插件签名，设置拦截的handler和方法
再把插件注册在全局配置文件里 的plugins标签，还可以加内部的property值
代理对象里面有一个属性保存原目标对象
实际执行，执行代理对象的方法intercept，代理对象里面执行原方法 要在intercepor的intercept方法里写invocation.proceed()
多个plugin按照全局配置文件顺序执行包装，实际执行的时候就是逆序解包装执行

拿StatementHandler里的ParameterHandler的值，就可以改sql的参数
用SystemMetaObject.forObject(target)  拿到元数据
metaObject.getValue("key")拿参数， metaObject.setValue("key",value)设置参数   插件可以改参数

分页插件 PageHelper
分页就是OFFSET LIMIT ， 返回有偏置的单页的记录数的信息，拿到的就是选择的页的内容
插件 page对象可以读总页数、当前页数等，   也可以把返回的list包装成一个PageInfo对象， 
属性更多，可以连续显示n个页码，不用额外判断    12_3_45    34_5_67

oracle的分页很复杂，不能用limit需要借助rownum，使用子查询，先查<=hi 再查>=lo
创建一个Page 的bean
oracle创建存储过程， mapper 里statementType=CALLABLE，调用存储过程  固定语法

批量操作：
全局配置中defaultExecutorType = BATCH 批量执行器，但这样所有都是批量了
实际操作，在获取openSession时，传入executorType.BATCH
用for循环执行大量操作，参数操作很多，SQL语句预编译只有一次，效率提高
数据库预编译好SQL，直接接收大量参数

在Spring整合里，可以配置一个
<bean id="sqlSession" class="SqlSessionTemplate">
	<constructor-arg name = "sqlSessionFactory" ref="之前的默认Factory"></constructor-arg>
	<constructor-arg name = "executorType" value="BATCH" ></constructor-arg>
</bean>
@Autowired
private SqlSession sqlSession;就取得批量session

自定义TypeHandler处理枚举（Enumerate） 如状态n选一   Java类名 enum
bean里定义了enum之后保存在数据库中是 枚举的名字 如LOGOUT、  MALE
enum.ordinal() 是索引 0 1 2 3     enum.name()  是名字 MALE FEMALE
利用的是TypeHandler 的EnumTypeHandler类，默认取name()
可以在全局配置中设置TypeHandlers  typeHandler handler=EnumOrdinalTypeHandler<EmpStatus>  写了<E>可以针对某个class
枚举状态可能对应状态码 400 502等，还有msg
在enum 类中，构造函数带参数,希望数据库保存状态码
public enum EmpStatus{
	LOGIN(100,“用户登录”),LOUGOUT(200,"用户退出");
	private Integer code;
	private String msg;
	private EmpStatus(Integer code, String msg){
			this......
	}
}
自己写一个类实现TypeHandler接口或者继承BaseTypeHandler类
覆写 setParameter（） 的  preparedStatement.setString()；  getResult（）
在全局配置中改用自己写的TypeHandler  加上javaType=EmpStatus指定
也可以在mapper里的增SQL中取参数时 #｛empStatus, typeHandler=xxxxx｝
查询用resultMap里面的result映射标签加 typeHandler
