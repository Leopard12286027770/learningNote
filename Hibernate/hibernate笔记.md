hibernate  是Java领域的ORM框架    Object Relational Mapping 可以操作Object来直接操作数据库
低侵入式：不要求Bean继承任何父类或实现接口，就不污染代码
ORM是对JDBC的封装
myBatis需要熟悉SQL语句，hibernate不用，myBatis运行效率高

OID  Object id， 加载：根据OID把一条记录放入内存

元数据meta data 描述对象-关系映射的数据，通常放在xml文件中

hibernate配置文件  .cfg.xml
在首选项里关联hibernate的dtd
连接信息
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">123456</property>
driver_class    url     
基本信息
数据库方言：mySQL或oracle等   在hibernate包里，project/etc/hibernate.property文件里有常用属性值
<property name="hibernate.dialect">org.hibernate.dialect.MySQLInnoDBDialect</property>
根据不同配置生成不同的SQL语句，包括分页写法等
在控制台打印SQL
<property name="hibernate.show_sql">true</property>
是否对SQL格式化   分行显示
<property name="hibernate.format_sql">true</property>
生成数据表的策略  自动创建表
<property name="hbm2ddl.auto">update</property>
指定关联的.hbm.xml
<mapping resource="com/abc/hibernate/News.hbm.xml">   注意用的是目录结构a/b/c 不是包名a.b.c/

创建对象-关系映射文件  .hbm.xml
自动根据创建的bean 类生成映射的xml
<id name=“id” type=“java.lang.Integer”>是bean的属性名  <colunm name="ID">是SQL的列名
其他列名是<property name = "title">

访问数据库的代码：
创建SessionFactory
Configuration config = new Configuration().configure();   默认关联/hibernate.cfg.xml
////////SessionFactory sessionFactory = config.buildSessionFactory()    4.0之前的方法
4.0之后的新方法
创建ServiceRegistry
ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties())
															  .buildSessionFactory();
SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry );
SessionFactory线程安全   一般只创建一个实例

创建Session
Session session = sessionFactory.openSession();

开启事务
Transaction transaction = session.beginTransaction();

执行保存操作
News news = new News("Java",new Date(new java.util.Date().getTime()));  前一个Date 是sql.Date 里面的是 util.Date
session.save(news);

提交事务
transaction.commit();

关闭Session
session.close();
关闭SessionFactory
sessionFactory.close();

读数据就是session.get(News.class,id);

session相当于JDBC的Connection，实际是对Connection的包装
事务Transaction代表一次原子操作，没开启事务不能进行操作
hbm2ddl.auto配置：
create每次生成新的数据表，删除旧表
create-drop当SessionFactory关闭时删除创建的表
update 更新数据表，如果表结构（对象关系映射）改变，也不删除原有数据  最常用
validate  如果.hbm.xml和数据库里的表的列不一致，则抛出异常，不修改

Session是操作数据库的主要接口，实现加载功能，不是直接查询
Session缓存为一级缓存，同步更新数据库需要flush过程
对象四种状态：持久化、临时、游离、删除

JUnit可以自动执行@Before init() 和 @After destroy()方法， 在@Test前后
Session缓存：Session不结束，不执行清理缓存，缓存就不被清理
缓存操作

flush() 强制使数据库与缓存同步，修改数据库  在transaction.commit()时自动执行flush()
		flush()会发送SQL语句
		实际在commit之后，数据库才会真正执行的SQL语句     
		或者直接调用session.flush()
		或者执行HQL或QBC查询之前，会先执行flush()，保证查询到的是最新的
		或者记录ID是使用数据库的自增方法生成，则save()方法后立即发送insert语句，保证save后对象id存在
refresh() 强制使缓存与数据库同步，发送SELECT语句
		要调用session.refresh(entity)刷新需要的缓存对象
		需要修改mySQl的事务隔离级别，默认是可重复读，否则在读的过程中不能修改记录
		在.cfg.xml里修改 <property name="connection.islolation"> 2 <>    //Read Commited
clear() 调用session.clear()清理缓存


临时对象Transient OID为null，不在session缓存中，数据库没有记录   		new出来的对象
持久化对象，托管对象，在session缓存中，与数据库对应，flush时会同步更新数据库，
	一个session实例缓存中，数据库的一个记录只能对应唯一的持久化对象   save merge get
删除对象 数据库没有OID的对应记录，缓存也没有							delete
游离对象， 脱管对象，不在session缓存，由持久化对象转来，数据库可以有  evict close clear

session.save(entity)   临时->持久化 为对象分配ID  flush()时发送INSERT
在save()之前的id是无效的，持久化的id是不能改的

session.persist(entity)	也是INSERT，区别是persist之前有ID则抛异常，save忽略Id

session.get(Entity.class,id)   发送SELECT  立即加载  如果数据表没有该记录，返回null
如果先session.close()没关系

session.load(entity.class,id) 如果不使用对象，则不立即SELECT，而返回一个代理对象，  是延迟加载， 如果没有该记录，抛异常
如果先session.close()，之后再用对象，可能会抛出LazyInitializationException

session.update(entity)  游离->持久化 get后改了对象属性之后可以不用，commit之前的flush就能更新
			但如果session关了，再重新开session，缓存里没有该对象，就变成了游离对象
			这时就需要显式调用update()，不管和数据库一不一样，都发UPDATE语句
			如果是持久化对象，没变属性，调用update()也不发UPDATE
			如果一样不要发送UPDATE，需要改.hbm.xml  select-before-update="true"
			如果数据库里没有对应记录，抛异常
			如果游离对象和session里的持久化对象id相同，则报错
			
session.saveOrUpdate() 判断OID是不是null null则save 非null则update
						id非空，数据库没有对应记录，则抛异常
						也可以设.hnm.xml   unsaved-value   OID等于这个值也判断为null

session.merge()   判断是游离还是临时，session缓存里有没有，数据库里有没有
					数据库有则get， 缓存有则更新缓存并update，数据库没有或者临时对象则save，
					hibernate用的不多，JPA用的多

session.delete()  只要OID和数据库记录对应，则DELETE，实际是在commit里执行SQL语句
					如果没有对应记录，抛异常
					可以设置.cfg.xml  <property name="hibernate.use.identifier.rollback">true<  >
					使删除对象后，把其OID置为null

session.evict()    从缓存中把指定的持久化对象移除

session.doWork(Work work)   Work是接口，需要重写execute(Connection connection)方法
							这样就能获得底层jdbc的connection

触发器工作： update：如果一样不要发送UPDATE，需要改.hbm.xml  select-before-update="true"
				避免触发器盲目触发（update一样的对象，不应该触发）
			 触发器改变对象，使session缓存不一致--->  在执行完session操作后，调用flush()和refresh()同步数据

配置文件：
hibernate.properties    或者 hibernate.cfg.xml （推荐）
可以配置C3P0连接池属性   hibernate.c3p0.max_size    min_size   timeout等
acquire_increment 连接耗尽时，一次获取n个连接
timeout 		  多长时间没使用则销毁连接
idle_test_period  超时的检测间隔
max_statements    缓存Statement数
hibernate.jdbc.fetch_size  调用Statement.setFetchSize()方法，设置每次读取的记录数，
												size越大读取次数越少，越快，服务器消耗越大 默认10，设置100
hibernate.jdbc.batch_size	批量操作的批次大小，size越大，发SQL的次数越少，越快

*.hbm.xml
属性和数据库列映射    推荐 一个文件定义一个class

hibernate-mapping标签 
package属性指定包名之后，下面的class标签的name可以不带包名

-class节点  name、table对应类和数据表
 dynamic-update (insert) =true 动态更新、插入语句，  update语句里只包含改变的属性字段，否则所有字段都要update

---id节点
   hibernate使用OID建立内存和数据库对应关系，OID和主键对应   hibernate通过标识符生成器为主键赋值
   推荐使用代理主键（不具有业务意义的整型，即id）
   
---...generator节点 id生成器   class=....
	increment  由hibernate递增生成，先读数据表的id最大值，再+1，有并发问题
	identity   数据库负责生成，要求数据库自增，oracle不行  OID必须是long int short， 不能是 byte
	sequence   用数据库提供的序列生成id， DB2，oracle等    OID必须是long int short， 不能是 byte
	hilo 	   按照high/low算法生成，不依赖数据库，从特定表hibernate_unique_key的字段获取next_high值，并修改
	native     按照数据库自动选择identity sequence或 hilo   （使用的最多）

---property节点
	access属性  默认property，使用getter setter， 改成field则用反射方式，直接访问
	column   和column子节点一样
	type    Java的bean的属性类型，不写就反射获取，然后判定数据库字段的类型
	unique   是否唯一约束
	update   是否可修改
	index    索引名称，为数据列创建索引，加速查询
	length   指定字段长度
	formula  设置SQL表达式，hibernate计算派生属性：持久化类的属性通过运行得出，不存储在数据库
		     formula="(SELECT concat(author,':',title) FROM NEWS n WHERE n.id=id)"
	scale    指定小数保留位数
	
时间：
Java: util.Date   util.Calendar
JDBC API: sql.Date  sql.Time  sql.Timestamp	   都是util.Date的子类

util.Date是父类，所以其可以对应DATE，TIME，TIMESTEMP类型    持久化类推荐使用java.util.Date
可以通过property的type	进行映射
<property name="date" type="timestep">  其中timestep是hibernate映射类型，不是java或标准SQL类型
	<column name="TIMESTEP" />    sql-type=""可以指定数据库中的字段类型
</property>
	
大对象映射：
长度超过255	，byte[]可用于存放图片或文件的二进制数据， JDBC有java.sql.Clob Blob 
对应标准SQL的CLOB （Character Large Object） BLOB （Binary Large Object）
MySql不支持CLOB、BLOB 用TEXT、MEDIUMTEXT、LONGTEXT表示
java持久化类可以选择byte[]或者java.sql.Blob，字符串 String或者Clob

映射组成关系：
bean里带bean    将一张表装进两个bean（级联）里
用<component name="pay">
	<property name="monthlyPay" column="MONTHLY_PAY"></property>    指定组件的属性
	<property name="yearlyPay" column="YEARLY_PAY"></property>    指定组件的属性
	<parent name="worker"/>		指定子bean中的父bean引用（子bean中有Worker worker指向父bean）
</component>
	
映射关联关系：
bean里指向另一个bean  两张表，外键关联
一对多：
一个Customer 多个Order， 一个Order一个Customer
多对一：
<class name ="Order">
	<many-to-one name="customer" class="a.b.c.Customer" column="CUST_ID"></many-to-one>
	column即是order表的外键名，会自动关联到customer的主键
如果插入的时候先插入order，后再插入对应的customer，则先3个insert，外键为空，再2个update关联外键
查询order，则只查出order， 延迟加载，在用到关联对象customer的时候再查询
默认情况下关联的customer对象和load()一样是代理对象，使用才查询
如果懒加载，延迟加载之前把session关闭，则会抛出异常
在不设定级联关系的情况下，不能直接删除有被引用关系的customer
	
一对多：
用一个Set作Order参考的集合  
hbm.xml里
<class name= "Customer">
	<set name="orders" table="Orders"> 要指明关联的表名   n的一端
		<key column="CUST_ID"></key> 关联的n的一端的表的外键名
		<one-to-many class="Order"/>	n的一端的映射类
数据库里并没有对应order的外键，是order外键连接customer
实际的SQL里，两端都要用update维护关联关系	
可以在<set>里设置inverse， false的一端主动维护关系，true被动不动。 没有=true设置，则两端都维护
1-n关系中，n的一方作为主控方有助于性能改善
建议1的一端， set里设置inverse="true"  先插入1的一端，后插入n的一端，这样避免多余的update维护动作
	
get 1的一端，	对n的一端会使用延迟加载，返回n的一端是hibernate的内置集合类型，
有延迟加载和存放代理对象的功能
所以在声明1的一端的集合类型的时候需要使用接口类型 Set（不能HashSet），
hibernate好能获取集合类型，返回hibernate内置类型
同样可能会抛出懒加载异常    如果只是获取set.size()仍然可能不进行加载

修改 customer.getOrders().iterator().next().setOrderName("AAA"); 用iterator().next()获取set成员
删除同样，不能有级联属性
	
<set> 还可以设定 cascade=delete   删一个可以把关联的双方行都删除
				 cascade=delete-orphan  customer.getOrders().clear()清空set后
						               删除所有和当前customer解除关联的order
				 cascade=save-update   save(customer)的时候就把关联的order一起保存了  (不建议使用)
				 order-by="ORDER_NAME DESC" 查询时对集合排序，参数是表的字段名，不是entity属性
				 
一对一关系：
外键映射，外键加unique ， 实现一对一
使用<many-to-one name class column unique=“true”>  <one-to-one name class property-ref="mgr"> 
		一个生成外键，另一个就可以one-to-one   property-ref是有外键的一方的外键属性名，如果没配，就用对方主键
先保存没有外键的一方，不产生额外的update
查有外键的一侧仍然是懒加载，先session.close()仍然会抛异常

如果查的是没有外键的一侧，则直接把双方都查询出来  用的是left outer join
				 
基于主键映射：
双方主键完全一样， 一方的主键生成策略用 foreign  param子标签指定自己entity里指向对方的属性名	
 而且要加 one-to-one constained="true"
另一方用one-to-one
不论保存顺序怎样，都先插入不用foreign的一方，获取主键值				 
查询是两个一起查出来，用left outer join						


多对多关系：
需要中间表映射  联合主键
建表联合主键：PRIMARY KEY (ID1,ID2) 就行
<class name="Category">
<set name="items" table="CATEGORIES_ITEMS">    中间表表名
	<key>
		<column name="C_ID">					自己在中间表对应的外键列
	<many-to-many class="Item" column="I_ID">	对方的类和中间表的外键列

查询的时候仍然懒加载，加载时用中间表 inner join 另一张表
另一张表加set->many-to-many时 必须<set  inverse="true">
否则双方在插入记录的时候，都要在中间表插入关联记录，会造成主键冲突

继承映射：
subclass   可以实现父类子类公用一张表  多一个discriminator列，用于记录type    （不推荐）
所有子类的字段不能有非空约束
在父类的hbm.xml里， <class name="PERSON" table="PERSONS" discriminator-value="PERSON">标签里，定义
<discriminator column="DISC" type="string">   加辨别列
<subclass name="Student" discriminator-value="STU">
	<property name="school" type column="SCHOOL"> 子类独有的属性
discriminator列由hibernate自动维护
查询只用查一个表  session.createQuery("FROM Person").list()  
				 session.createQuery("FROM Student").list()
如果继承层次深，数据表的字段会多  需要鉴别列

joined-subclass
每个子类一个表，只放子类独有的属性，主键关联父类的表（主键相同）
子类属性可以加非空约束
在<class> 下定义
<joined-subclass name="Student" table="STUDENTS">
	<key column="STU_ID">
	<property name="school" type column="SCHOOL"> 子类独有的属性

子类插入，至少需要插入两张表
查询子类需要查询父子表，没有冗余字段，不需要鉴别列  能加非空约束

union-subclass
每个entity一个独立的数据表
在<class> 下定义    主键生成策略需要hilo
<union-subclass name="Student" table="STUS">
	<property name="school" type column="SCHOOL"> 子类独有的属性
插入之后的子类的表是完整的，带有父类所有列
插入性能好，子类只插入子表
查询父类，需要查父和子表， 用union合并父子表   查询性能稍差  能加非空约束
存在冗余字段（子表重复父表列）  更新父表时效率很低，子表也要查

检索策略：
<class lazy=“false”>可以调节类对象是否延迟加载，仅对load()生效，不是关联属性的懒加载设置  通常用默认值true
一对多、多对多的<set>标签
lazy属性 true（默认）  false  决定orders集合初始化时机   （不建议设置false）
		 extra 增强的延迟加载  尽可能延迟集合初始化的时间  如调用orders.size() 发送SELECT count(*)
								只查询集合中的一个元素，就只select那一个，不把集合整个初始化
							如果需要初始化，extra反而会多发一次SQL，大多数情况下默认true
		Hibernate.initialize(customer.getOrders())可以进行显式初始化
fetch=  select（默认）    决定orders的查询语句形式
		subselect   需要初始化一个对象集合的时候把内存里的set全部初始化   、
					通过带子查询的select中 WHERE  IN(...)的条件出现，
					读取所有同类型的n的一段的集合，lazy 有效， batch-size失效
		join决定orders集合初始化时机   同时lazy属性失效，会直接用迫切左外连接，查询关联的n的一端的集合
						Query的list()方法会忽略join，依旧使用延迟加载
batch-size  =“1” 2 3 4 5 6 批量检索，减少select语句数量
			设置一次初始化内存里set集合的数量（不是集合内成员的数量），延迟和立即都有效，提高查询效率
			实现方法是Where id IN(1, 2 , 3)

多对一，一对一  <many-to-one>  <one-to-one>
lazy  proxy	延迟检索
	  FALSE 立即检索
	  join  迫切左外连接

fetch   select （默认）
		join   忽略lazy属性，
			   Query的list()方法会忽略join，依旧使用延迟加载

batch-size (在class节点上) 同set标签里的batch-size

查询：
导航对象图   根据已经加载的对象，获取其他对象   1对n n对1等
OID    根据OID获取   get  load
HQL    Hibernate Query Language
QBC    Query By Criteria
本地SQL    使用SQL语句

HQL    面对对象的     hibernate负责翻译成SQL语句
创建Query对象
String hql = "FROM Employee e WHERE e.salary > ? AND e.email LIKE ? ORDER BY e.salary";
Query query = session.createQuery(hql);

按位置绑定参数
query.setFloat(0,6000).setString(1,"%a%");
按照参数名绑定参数    hql里要以:开始
String hql = "FROM Employee e WHERE e.salary > :sal AND e.email LIKE :em ORDER BY e.salary";
query.setFloat("sal",6000).setString("em","%a%");

参数也可以是实体类
String hql = "FROM Employee e WHERE e.dept= ?";
query.setEntity(0,dept);

执行查询
List<Employee> emps = query.list();

分页查询：
setFirstResult(int firstResult) 设定从哪一个开始 默认0
setMaxResults(int maxResults)   设定最多查出的数量

int pageNo = 3;
int pageSize = 5;
query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize).list()

命名查询
hbm.xml里 和 class节点并列
<query name="salaryEmps"><![CDATA[FROM Employee e WHERE e.salary > :minSal AND  e.salary< :maxSal]]></query>
有大于小于号，和标签混淆，需要放在CDATA里 <![CDATA[]]>
Query query = session.getNamedQuery("salaryEmps");
query.setFloat("minSal",6000).setFloat("maxSal",7100);

投影查询  SELECT 部分列而不是全部
正常的查询方式
String hql = "SELECT e.email, e.salary FROM Employee e WHERE e.dept=:dept";
Query query = session.createQuery(hql);
List<Object[]> result = query.setEntity("dept",dept).list();

投影查询是返回对象，可以直接在hql语句里封装对象
 String hql = "SELECT new Employee(e.email, e.salary) FROM Employee e WHERE e.dept=:dept";
前提是Employee有对应的构造器  
List<Employee> result = query.setEntity("dept",dept).list();

报表查询
hql里可以用min() max() GROUP BY HAVING 

迫切左外连接   一般要left join 都会加fetch
hql里LEFT JOIN FETCH，表示迫切左外连接
SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.emps
查询List<Department>  而且emp的set已经初始化好
如果不用DISTINCT， 会出现员工数量的Department，可以用Set进行去重

不用FETCH，直接左外连接，则根据配置文件，（如果lazy）emp的set会延迟加载

迫切内连接   迫切作用一样
INNER JOIN FETCH  或者 JOIN FETCH 

删除   更新
String hql = "DELETE FROM DEPARTMENT d WHERE d.id= :id";
session.createQuery(hql).setInteger("id",20).executeUpdate();

QBC
SQL动态拼装
Criteria criteria = session.createCriteria(Employee.class);
criteria.add(Restrictions.eq("email","abc@123.com")); equal 通过Restrictions的静态方法添加查询条件
criteria.add(Restrictions.gt("salary",1000F)); greater than   Float
Employee emp = (Employee) criteria.uniqueResult();

AND
Conjunction conjunction =  Restrictions.conjunction();   Conjunction自己就是Criteria对象
conjunction.add(Restrictions.like("name","a",MatchMode.ANYWHERE))
conjunction.add(Restrictions.eqProperty("dept",dept))     一个属性为另一个entity的实例

OR
Disjunction disjunction = Restrictions.disjunction();
disjunction.add(Restrictions.ge("salary"),5000F)  greater or equal Float

统计查询
Projection
criteria.setProjection(Projections.max("salary"));

排序
criteria.addOrder(Order.asc("salary"));

分页
criteria.setFirstResult((pageNo-1)*pageSize);
criteria.setMaxResults(pageSize);
criteria.list();

也可以直接criteria.a().b()连续操作


原生SQL语句
String sql = "INSERT INTO　department VALUES(?,?)";
Query query = session.createSQLQuery(sql);
query.setInteger(0,100).setString(1,"dev").executeUpdate();

	
二级缓存
一级缓存是session级，二级缓存是SessionFactory级缓存
SessionFactory级缓存分内置和外置
内置存放 预设SQL语句和hbm.xml的数据    只读
外置缓存是常规所指的二级缓存    是可配置的缓存插件
少修改的，不很重要，允许出现并发问题的数据，可以放入二级缓存
二级缓存并没有被hibernate实现，需要第三方插件
并发策略：
四种隔离级别都有对应策略
Read-Write-----Read Commited隔离级别   推荐

允许使用的缓存插件：
EHCache
OSCache
SwarmCache   不支持Readwrite
JBossCache	 不支持Readwrite

加入EHCache的包，xml配置文件
在cfg.xml里 <property name="cache.usze_second_level_cache">true</>启用二级缓存

<property name="hibernate.cache.region.factory_class">org.EHCache<> 配置使用的产品
在hibernate的 hibernate.properties里找，如果不对，在jar里找

<class-cache usage="read-write" class="Employee全类名">配置使用二级缓存的类
也可以在.hbm.xml里配置  <cache usage="read-write"/>

集合级别的二级缓存
如果有延迟加载的set，父类使用二级缓存，初始化set，session关闭再开启后父类不需要再查询，set仍然需要重新延迟加载
在cfg.xml <collection-cache usage="read-write" collection="Department全类名.其集合的属性名">
如果此时集合内的子类没有使用二级缓存，那么集合保留OID，初始化时再一个个查询
所以集合内的类也需要使用二级缓存

EHCache的配置xml
磁盘存储位置 <diskStore>   超过缓存设定储存限制时放在磁盘上
<defaultCache  >   默认缓存策略
<cache name="aaa"> 一个缓存区域的命名和缓存策略
hibernate根据区域名配置缓存
类就是全类名， 集合就是全类名.属性名

maxElementsInMemory="10000" 内存中最大的对象数目
eternal="false"    是否是永久储存
timeToIdleSeconds  允许空闲状态的最长时间     0为无限长
timeToLiveSeconds  允许存在的最长时间         0为无限长
overflowToDisk="true"   允许内存装到限制值时往磁盘上写   SessionFactory关闭之后会自动清空硬盘缓存数据

查询缓存   依赖于二级缓存
用hql查询的话，缓存并不能起作用，因为不是OID查询
需要设置query.setCacheable(true);  或者criteria.setCacheable(true);
cfg.xml设置<property name="cache.use_query_cache">true   开启查询缓存

时间戳缓存
hibernate用时间戳缓存来判断缓存里的数据是否过期
如果有插入更新或者删除，会加时间戳

query.iterate遍历   效率提升
query查询一个Employee的list
Iterator<Employee> it = query.iterate();
会只查询Employee的ID，再根据ID从内存里查，查不到再SQL
如果不在内存里，反而会发送更多的SQL，不推荐使用

获取和当前线程绑定的session对象
不需要从外部传入session，多个DAO方法可以使用一个事务
建立单例HibernateUtils类，
private构造函数， static HibernateUtils instatnce=new HibernateUtils();
public HibernateUtils getInstance() return instance
提供getSessionFactory()方法
如果sessionFactory==null则 创建新的
getSession()方法
return getSessionFactory().getCurrentSession();

cfg.xml配置管理session的方式
<property name="current_session_context_class">thread
如果session是由thread来管理的，那么在transaction.commit()或失败回滚时，session会关闭

批量操作，使用JDBC API是最快的
session.doWork(new Work(){
	  public void execute(Connection connection) throws SQLException{
	  }
	}
)
如果用session的 save()或者update()做批量操作，对象会被存放在缓存中，太多缓存溢出
需要做一定次数后session.flush() session.clear()
HQL 不支持 INSERT INTO .....   VALUES 所以不能批量添加，但可以批量修改和删除
StatelessSession没有缓存，可以批量操作，但底层还是翻译成JDBC语句


