# ot-client-helper
Ot-Client-Helper是一套手动埋点生成Trace的入口Span的API脚手架工程，它**不具备追踪上下文传递的能力**，也**不具备自动关闭Span，需要手动管理Span**，
适用于Android只生成入口Span的场景

Ot-Client-Helper生成的数据遵循OT格式，追踪数据会上传到SLS Trace实例中。

## 快速开始
buid.gradle文件
```gradle
 implementation 'com.aliyun.openservices:ot-clien-helper:1.0.1'
```

测试代码
```java

// Context appContext=InstrumentationRegistry.getInstrumentation().getTargetContext();
Init(Configuration.Builder.newBuilder(appContext,"${access-key-id}","${access-key-secret}","${endpoint}","${project}","${instance}", "${service}").withResources(Collections.singletonMap("test","test1")));

Span span=createSpan("test").withResource("rest","rest").withAttribute("test1","test2").start();

span=span.withAttribute("test3","test5").occurError();

System.out.println(String.format("%s  %s",span.traceContext().contextKey(),span.traceContext().contextValue()));
// 创建的Span一定要调用Stop方法
span.stop();
```
参数介绍

| 变量 | 说明 | 示例 |
|---|---|---|
|${endpoint}	|日志服务的接入地址，格式为`https://${region-endpoint}`，其中： <br/> `${region-enpoint}` ：Project访问域名，支持公网和阿里云内网（经典网络、VPC）。更多信息，请参见[服务入口](https://help.aliyun.com/document_detail/29008.htm?spm=a2c4g.11186623.0.0.3b4812faModpH5#reference-wgx-pwq-zdb)。 | https://cn-hangzhou.log.aliyuncs.com|
|${project}|	日志服务Project名称。	| test-project |
|${instance}|	Trace服务实例ID。	|test-traces|
|${access-key-id}|	阿里云账号AccessKey ID。<br/> 建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey（包括AccessKey ID和AccessKey Secret）。授予RAM用户向指定Project写入数据权限的具体操作，请参见授权。如何获取AccessKey的具体操作，请参见访问密钥。| 无|
|${access-key-secret} | 阿里云账号AccessKey Secret。<br/> 建议您使用只具备日志服务Project写入权限的RAM用户的AccessKey。 | 无|
|${service}	| 服务名。根据您的实际场景配置。|	Android|

## FAQ
* 如何获取追踪上下文

通过`Span.TraceContext()`方法可以获取追踪上下文
**注：** `Span.TraceContext()`只具备生成Trace上下文能力，并不具备传递上下文的能力，上下文传递需要手动完成
   
* 如何设置Span异常

通过`Span.occurError()`方法可以设置当前Span为异常状态
