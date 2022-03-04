# ot-client-helper

Ot-Client-Helper 是一套手动埋点生成 Trace 的入口 Span 的 API 脚手架工程，它**不具备追踪上下文传递的能力**，也**不具备自动关闭 Span，需要手动管理 Span**，
适用于 Android 只生成入口 Span 的场景

Ot-Client-Helper 生成的数据遵循 OT 格式，追踪数据会上传到 SLS Trace 实例中。

## 快速开始

buid.gradle 文件

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

| 变量                 | 说明                                                                                                                                                                                                                                                                                            | 示例                                 |
| -------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------ |
| ${endpoint}          | 日志服务的接入地址，格式为`https://${region-endpoint}`，其中： <br/> `${region-enpoint}` ：Project 访问域名，支持公网和阿里云内网（经典网络、VPC）。更多信息，请参见[服务入口](https://help.aliyun.com/document_detail/29008.htm?spm=a2c4g.11186623.0.0.3b4812faModpH5#reference-wgx-pwq-zdb)。 | https://cn-hangzhou.log.aliyuncs.com |
| ${project}           | 日志服务 Project 名称。                                                                                                                                                                                                                                                                         | test-project                         |
| ${instance}          | Trace 服务实例 ID。                                                                                                                                                                                                                                                                             | test-traces                          |
| ${access-key-id}     | 阿里云账号 AccessKey ID。<br/> 建议您使用只具备日志服务 Project 写入权限的 RAM 用户的 AccessKey（包括 AccessKey ID 和 AccessKey Secret）。授予 RAM 用户向指定 Project 写入数据权限的具体操作，请参见授权。如何获取 AccessKey 的具体操作，请参见访问密钥。                                       | 无                                   |
| ${access-key-secret} | 阿里云账号 AccessKey Secret。<br/> 建议您使用只具备日志服务 Project 写入权限的 RAM 用户的 AccessKey。                                                                                                                                                                                           | 无                                   |
| ${service}           | 服务名。根据您的实际场景配置。                                                                                                                                                                                                                                                                  | Android                              |

## FAQ

- 如何获取追踪上下文

通过`Span.TraceContext()`方法可以获取追踪上下文
**注：** `Span.TraceContext()`只具备生成 Trace 上下文能力，并不具备传递上下文的能力，上下文传递需要手动完成

- 如何设置 Span 异常

通过`Span.occurError()`方法可以设置当前 Span 为异常状态

- Android 时间和 Server 端时间不一致问题
  前提： 后端服务在 Response 中返回当前服务端当前时间（以下以 Ok HttpClient 为例）

1. 利用 OKHttp 的 Interceptor 获取 Reponse 中的时间
2. 利用`SystemClock.elapsedRealtime()`记录手机系统开机时间
3. 现在服务器时间 = 以前服务器时间 + 现在手机开机时间 - 以前服务器时间的获取时刻的手机开机时间

```java
Request request = chain.request();
long startTime = System.nanoTime();
Response proceed = chain.proceed(request);
// 以前服务器时间的获取时刻的手机开机时间
long lastTime = System.nanoTime() - startTime;

//
diffTime = lastTime - SystemClock.elapsedRealtime();

//当前服务器时间
serverTime = diffTime + SystemClock.elapsedRealtime()

```
