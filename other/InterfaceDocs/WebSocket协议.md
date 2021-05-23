# 网络协议Websocket

## 请求包格式
command=test@jjj&requestId=1&name=wukai&num=10

- command：   代表指令，必须要有
- requestId： 代表请求Id，响应数据会将requestId回传
- 其他：      参数列表



## 接口返回数据json
### state 类型枚举
- EXCEPTION(0) : 异常
- SUCCESS(1) : 成功
- FAIL(2)   : 失败
- PUSH(3)    :  推送

## 调用成功测试接口
command=user@getUser&requestId=1&userId=1
```json
{
    "state":1,
    "requestId":1,
    "data":{
        "lastLogin":1589619048296,
        "password":"1234",
        "user_id":1,
        "pic":"1",
        "username":"wukai"
    }
}
```

## 调用失败测试接口
command=user@getFail&requestId=1&userId=1
```json
{
    "state":2,
    "requestId":1,
    "msg":"No user 1"
}
```

## 产生推送测试接口
command=test@push&requestId=1&msg=wulala
```json
{
	"state":3,
    "cmd":"push@test",
    "data":{
        "lastLogin":1589619256653,
        "password":"1234",
        "user_id":1,
        "pic":"1",
        "username":"wukai"
    }
}
```
