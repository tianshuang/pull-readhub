# pull-readhub.me

### 简介
无码科技近日发布了 [Readhub](https://readhub.me/)，由于不想每天手动登录站点去看，所以随手写了一个小工具，每天早上九点通过钉钉机器人推送到群里。

效果如图：
![dingding](https://storage.tianshuang.me/pull-readhubme/dingtalk.jpg)

环境要求：
JDK8 +

使用方法：

下载 [pull-readhubme-0.1.1-SNAPSHOT.jar](https://storage.tianshuang.me/pull-readhubme/pull-readhubme-0.1.1-SNAPSHOT.jar)

下载 [pull-readhubme.properties](https://storage.tianshuang.me/pull-readhubme/pull-readhubme.properties)

修改配置文件中的日志路径及钉钉机器人 URL 并将以上两个文件放置于同一目录下。

```Bash
chmod u+x pull-readhubme-0.1.1-SNAPSHOT.jar
./pull-readhubme-0.1.1-SNAPSHOT.jar &
```

如有建议，敬请指出。
