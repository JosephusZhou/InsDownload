# InsDownload

Instagram 下载器

*该项目起源于小狗强子想要在手机上下载 Instagram 图片*

使用 Kotlin 开发，使用了协程进行异步操作

### 功能

* 通过帖子链接获取帖子的图片和视频
* 下载帖子图片和视频

### 编译事项

* 在项目根目录创建`keystore`目录，存放签名密钥

* 在项目根目录创建`keystore.properties`文件，内容示例如下：
```
STORE_FILE=../keystore/key
STORE_PASSWORD=######
kEY_ALIAS=@@@@@
KEY_PASSWORD=######
```