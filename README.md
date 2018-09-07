# sdchain-wallet-android

> A app for users to manage wallet

## Configuration your signatures 
configuration your signatures in ***local.properties***
```
keystore.password=xxx
keystore.alias_password=xxx
keystore.alias=xxx
keystore.path=xxx/xxx.jks
```



## Usage

* [`Step 1: Set a key with server`](#set-a-key-with-server)
* [`Step 2: Use your app signatures encrypt the key`](#Use-your-app-signatures-encrypt-the-key)


### Set a key with server

[^_^]:和服务器约定一个Key,用于部分接口加密，发送请求的时候，增加加密字段"validStr"

>A key is agreed with the server for partial interface encryption. When sending a request, the encrypted field "validStr" is added.
```java
String validStr=AesUtil2.encryptPOST("need encrypt field")
```
or
```java
String validStr=AesUtil2.encryptGET("need encrypt field")
```


### Use your app signatures encrypt the key

[^_^]:用App的签名对约定的Key进行再加密，并保存在本地，使用时解密使用

>Re-encrypt the agreed Key with the signature of the App, save it locally, and decrypt it when using it.

encrypt
```java
String SIGN=EncryptUtil.encrypt(SecurityUtils.getSign(), Key)
```
>keep the key int project file : ***gradle.properties***

decrypt
```java
String Key=EncryptUtil.decrypt(SecurityUtils.getSign(), SIGN)
```


## Thanks

[zBarLibary][1]  
[PickView][2]  
[RxJava][3]  
[RxAndroid][4]  
[glide][5]  
[bottom-navigation-bar][6]  
[logger][7]  
[SmartRefreshLayout][8]  
[PictureSelector][9]  
[volley][10]  
[gson][11]  
[EvenBus][12]  
[greendao][13]  
[AndPermission][14]  
[systembartint][15]  
[butterknife][16]  
[arouter][17]  
[pinyin4j][18]  

--------------------------------
[1]:https://github.com/bertsir/zBarLibary
[2]:https://github.com/brucetoo/PickView
[3]:https://github.com/ReactiveX/RxJava/
[4]:https://github.com/ReactiveX/RxAndroid
[5]:https://github.com/bumptech/glide
[6]:https://github.com/Ashok-Varma/BottomNavigation
[7]:https://github.com/orhanobut/logger
[8]:https://github.com/scwang90/SmartRefreshLayout
[9]:https://github.com/LuckSiege/PictureSelector
[10]:https://github.com/mcxiaoke/android-volley
[11]:https://github.com/google/gson/
[12]:https://github.com/greenrobot/EventBus
[13]:https://github.com/greenrobot/greenDAO
[14]:https://github.com/yanzhenjie/AndPermission
[15]:https://github.com/hexiaochun/SystemBarTint
[16]:https://github.com/JakeWharton/butterknife
[17]:https://github.com/alibaba/ARouter
[18]:http://pinyin4j.sourceforge.net/


## License

SD-Wallet is based on the [GNU General Public License v3.0](http://www.gnu.org/licenses/quick-guide-gplv3.html)  protocol.


