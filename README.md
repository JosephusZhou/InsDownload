# InsDownload

Instagram 图片视频下载工具

使用 Kotlin 开发，使用了协程进行异步操作

另外正在尝试 JetPack 版，其中包含 DataBinding、LiveData、ViewModel 等组件

[传送门](https://github.com/JosephusZhou/InsDownload/tree/jetpack)

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