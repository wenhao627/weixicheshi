微信公众号定时推送天气预报的项目

在application.yml文件中配置自己微信公众号的appID和appsecret

如：
        #公众号配置
wechat:
  appid:  wxc3581a57171d33af # 公众号的appid
  appkey: 0280b753eb98a9fb013e14ce5716098c # 公众号的appsecret
  messageId: zV4ud6VMbaiyI2uvgxEveUwd_naNhK3p6wMPZ9i4r3Q # 公众号的模板id(早上发送用的)
  messageId2: hzT5PJzxtjMrSF7RVvUi4gbYDxcjnhTxdJpWpbMmqxI # 公众号的模板id(下午发送用的)
  messageId3: AtGZW5gsAnCIy4KEdwLe2kSg1nEzN3rQu5IoJLloOBY # 公众号的模板id(晚上发送用的)目前只用的
  
  
  然后在src/main/java/com/github/binarywang/demo/wx/mp/servise/test3.java文件中修改天气api的url
  
                    
   @Scheduled(cron = "0 30 8 * * ?")//设置定时执行任务 //代表早上8.30执行任务
