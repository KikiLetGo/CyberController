### 重要事项
- 请把自己在HMS上申请的agconnect-services.json文件替换掉本项目的，并把build.gradle中的applicationId改成你自己的
- 把CyberApplication中配置的apiKey(setApiKey)改成你自己的

### 手机设置
- 使用前先让手机和电脑蓝牙配对和连接
- 把本App加入到电池白名单（电池优化选项中找到本App，选择不优化），否则息屏一段时间后，系统将断开socket连接，并且早Doze Mode下无法重连，电脑三连翻译快捷键将无法唤醒屏幕
- 在手机旋转设置中打开“旋转锁屏界面”，否则每次解锁（或点亮屏幕）的横竖屏切换体验不佳
- 最好关闭手机的锁屏界面，否则每次息屏后要解锁，体验不佳

### 关键代码思路提示
电脑上要执行的功能实际上是本App通过TCP连接注入的，这些功能脚本在`assets/command_scripts`下，比如`key_click.py`是在电脑上执行按键点击的功能脚本模板
```python
def process(params):
    keys = params.split(',')
    for key in keys:
        key_down(key)
        key_up(key)
process(params)
```
在向电脑发送命令的时候只需要把这段脚本的文本，以及需要传入的`params`，然后发送给电脑就可以了，电脑上的python程序会自动解析并执行。

> 也就是说，如果有新的对电脑的控制，不需要修改电脑上的python程序，直接在客户端编写新的脚本发送过去注入执行就好。

### 电脑端代码
https://github.com/KikiLetGo/CyberControllerServer
