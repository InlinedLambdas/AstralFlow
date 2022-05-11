# 配置文件

配置文件用于规定星流的各种参数，在 `plugins/AstralFlow` 下即可找到 `config.conf`，这是星流的配置文件。  
一般来说，星流是开箱即用的，所以配置文件不调也没关系。

> 注意：扩展的配置文件在他们自己的插件数据文件夹里，不是在 `plugins/AstralFlow` 下。

我们推荐您使用 [VSCode](https://code.visualstudio.com/) + [HOCON 高亮 by ustc_zzzz](https://www.mcbbs.net/thread-819921-1-1.html)
或者别的支持 HOCON 语法高亮的编辑器来编辑配置文件，这可以使得您编辑文件的效率大大提升。

```hocon
# 保存数据的时间间隔，单位为 tick 。1 tick 相当于 1/20 秒
# 设置为 -1 则表示不自动保存数据。
data-save-intervals = 300
# 插件对玩家使用的语言
locale = "zh_CN"
# 一些优化设定
optimization {
  # 是否启用区块缓存扩容 (HashMap-resizing)
  # 当加载的区块数量到达一定程度时候，将会自动扩容缓存。这个操作会复制原先缓存中所有的数据到新的缓存内，因此非常耗时。
  # 如果你不确定是否要开启，请保持默认值: false
  allow-chunk-map-resizing = false
  # 是否启用机器缓存扩容 (WeakHashMap-resizing)
  # 当加载的机器数量到达一定程度时候，将会自动扩容缓存。这个操作会复制原先缓存中所有的数据到新的缓存内，因此非常耗时。
  # 但是机器数量一般较少，所以我们建议开启。默认值: true
  # 如果你的服务器内机器数量增长速度较快，建议关闭。
  allow-machine-map-resizing = true
  # 区块缓存槽位数，一个槽位一个区块，槽位满了可能导致速度稍微变慢，影响不大。
  # 这项数值决定了区块缓存的起始大小，越高的数值往往带来的性能提升越大，但是可能会增加内存使用。
  # 如果你的服务器加载的区块非常多，请考虑设置为较大的数值，如 1024。数值可以通过这个公式计算出来：常驻全服区块数目 * 1.25
  # 如果你不知道应该怎么调，请保持默认: 512
  chunk-map-capacity = 512
  # 默认的机器数据储存格式，请不要动。
  default-machine-storage-type = JSON
  # 机器缓存槽位数，一个槽位一个机器，槽位满了可能导致速度稍微变慢，影响不大。
  # 这项数值决定了机器缓存的起始大小，越高的数值往往带来的性能提升越大，但是可能会增加内存使用。
  # 如果你的服务器加载的机器非常多，请考虑设置为较大的数值，如 128。
  initial-machine-capacity = 32
  # 当一个机器抛出多少个错误的时候我们需要把它停止
  # 注意，抛出错误是从服务器开始算到关服的。如果你想关闭频繁报错的机器，请把它设置的稍微高一点，如 20
  # 如果你想停止掉所有出错过的机器，请把它设置为 0
  machine-tick-exception-limit = 4
}
# 关于合成的设置
recipe-setting {
  # 是否将原版物品纳入矿物辞典中，这可能对扩展的合成配方会很有用（如果他们接受原版物品）
  add-vanilla-ore-dict = true
  # 是否注入原版的工作台，这样你就可以在工作台里合成自定义的物品了
  # P.S. 我们不会真的往原版配方表里面加配方，合成过程是模拟的，所以可能有一些合成相关的插件会找不到配方来合成。
  inject-vanilla-crafting-table = true
  # 当匹配到自定义配方的时候且原版也有对应配方时，是否覆盖掉原版的合成结果
  # 这项仅当 inject-vanilla-crafting-table 为 true 时有效
  override-vanilla-recipe = true
}
# 安全相关
security-setting {
  # 是否允许玩家在插件加载完毕之前进入服务器，这可能会引起安全问题
  allow-player-join-before-init = false
  # 多少个 tick 给潜在的内存泄漏对象记一次周期，有20的倍数个周期的对象会被标记为内存泄露并且在控制台中输出
  # 设置为 -1 禁用内存泄漏检测。此项用于寻找潜在的内存泄漏
  # 如果你不知道怎么调，就把它设置为 100
  leak-check-interval = 100
}
# 配置文件版本号，请不要自己修改，否则会引起数据丢失。
version = 1

```