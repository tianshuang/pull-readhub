# pull-readhub.cn

### 简介
无码科技近日发布了 [Readhub](https://readhub.cn/)，由于不想每天手动登录站点去看，所以随手写了一个小工具，每天早上定时通过钉钉机器人推送到群里。

效果如图：
![dingding](https://storage.tianshuang.me/pull-readhub/dingtalk.jpg)

环境要求：
JDK8 +

使用方法：

下载 [pull-readhub-1.0.0.jar](https://storage.tianshuang.me/pull-readhub/pull-readhub-1.0.0.jar)

下载 [pull-readhub.properties](https://storage.tianshuang.me/pull-readhub/pull-readhub.properties)

修改配置文件中的日志路径及钉钉机器人 URL 及 cron 配置并将以上两个文件放置于同一目录下。

```Bash
chmod u+x pull-readhub-1.0.0.jar
./pull-readhub-1.0.0.jar &
```

如有建议，敬请指出。
