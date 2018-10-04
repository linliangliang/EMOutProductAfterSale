package com.zhengyuan.emoutproductaftersale;

import com.zhengyuan.baselib.constants.Constants;
import com.zhengyuan.baselib.listener.NetworkCallbacks;
import com.zhengyuan.baselib.listener.NetworkCallbacks.SimpleDataCallback;
import com.zhengyuan.baselib.utils.xml.Element;
import com.zhengyuan.baselib.xmpp.ChatUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by zy on 2018/9/5.
 */

public enum DataObtainer {
    INSTANCE;
    private final String LOG_TAG = "DataObtainer";
    //获取选项的值
    public void  getSelectInfomation (final NetworkCallbacks.SimpleDataCallback callback){
        Element element = new Element("mybody");
        element.addProperty("type", "requestSelectInformation");
        ChatUtils.INSTANCE.sendMessage(Constants.CHAT_TO_USER, element.toString(),"returnSelectInformation",
            new NetworkCallbacks.MessageListenerThinner() {
        @Override
          public void processMessage(Element element, Message message, Chat chat) {
            boolean isSuccess = element.getBody() != null &&
                    !element.getBody().equals("");
              callback.onFinish(isSuccess, "", element.getProperty("result"));
           }
    });
    }
    //获取质量信息的详细信息
    public void  getQulityInfo(String info ,final NetworkCallbacks.SimpleDataCallback callback){
        Element element = new Element("mybody");
        element.addProperty("type", "requestGetQulityInfo");
        element.addProperty("info",info);
        ChatUtils.INSTANCE.sendMessage(Constants.CHAT_TO_USER, element.toString(),"returnGetQulityInfo",
                new NetworkCallbacks.MessageListenerThinner() {
                    @Override
                    public void processMessage(Element element, Message message, Chat chat) {
                        boolean isSuccess = element.getBody() != null &&
                                !element.getBody().equals("");
                        callback.onFinish(isSuccess, "", element.getProperty("result"));
                    }
                });
    }
    //获取局，段信息
    public void  getJDInfo(String info ,final NetworkCallbacks.SimpleDataCallback callback){
        Element element = new Element("mybody");
        element.addProperty("type", "requestGetJuDuanInfo");
        element.addProperty("info",info);
        ChatUtils.INSTANCE.sendMessage(Constants.CHAT_TO_USER, element.toString(),"returnGetJuDuanInfo",
                new NetworkCallbacks.MessageListenerThinner() {
                    @Override
                    public void processMessage(Element element, Message message, Chat chat) {
                        boolean isSuccess = element.getBody() != null &&
                                !element.getBody().equals("");
                        callback.onFinish(isSuccess, "", element.getProperty("result"));
                    }
                });
    }
    //获取质量信息的详细信息 通过车型 或者车号
    public void  getQulityInfoByCars(String carId,String carType,final NetworkCallbacks.SimpleDataCallback callback){
        Element element = new Element("mybody");
        element.addProperty("type", "requestGetQulityInfoByCar");
        element.addProperty("carId",carId);
        element.addProperty("carType",carType);
        ChatUtils.INSTANCE.sendMessage(Constants.CHAT_TO_USER, element.toString(),"returnGetQulityInfoByCar",
                new NetworkCallbacks.MessageListenerThinner() {
                    @Override
                    public void processMessage(Element element, Message message, Chat chat) {
                        boolean isSuccess = element.getBody() != null &&
                                !element.getBody().equals("");
                        callback.onFinish(isSuccess, "", element.getProperty("result"));
                    }
                });
    }

    public void  getMaterialInfo(String serialNumber ,final NetworkCallbacks.SimpleDataCallback callback){
        Element element = new Element("mybody");
        element.addProperty("type", "requestGetMaterialInfo");
        element.addProperty("info",serialNumber);
        ChatUtils.INSTANCE.sendMessage(Constants.CHAT_TO_USER, element.toString(),"returnGetMaterialInfo",
                new NetworkCallbacks.MessageListenerThinner() {
                    @Override
                    public void processMessage(Element element, Message message, Chat chat) {
                        boolean isSuccess = element.getBody() != null &&
                                !element.getBody().equals("");
                        callback.onFinish(isSuccess, "", element.getProperty("result"));
                    }
                });
    }
    public void  sendInfoByFour(String s1 ,String s2 ,String snameId,String username,String GZCLType,String pathFor,final NetworkCallbacks.SimpleDataCallback callback){
        Element element = new Element("mybody");
        element.addProperty("type", "requestInsertFourInfo");
        element.addProperty("info",s1);
        element.addProperty("info2",s2);
        element.addProperty("snameId",snameId);
        element.addProperty("userName",username);
        element.addProperty("GZCLType",GZCLType);
        element.addProperty("pathFor",pathFor);
        ChatUtils.INSTANCE.sendMessage(Constants.CHAT_TO_USER, element.toString(),"returnInsertFourInfo",
                new NetworkCallbacks.MessageListenerThinner() {
                    @Override
                    public void processMessage(Element element, Message message, Chat chat) {
                        boolean isSuccess = element.getBody() != null &&
                                !element.getBody().equals("");
                        callback.onFinish(isSuccess, "", element.getProperty("result"));
                    }
                });
    }
    //提交前三个页面的信息
    public void  sendInfoByAll(String s1 ,String s2 ,String snameId,String username,String GZCLType,String ImagePath,String HsHxInfo,final NetworkCallbacks.SimpleDataCallback callback){
        Element element = new Element("mybody");
        element.addProperty("type", "requestInsertAllInfo");
        element.addProperty("info",s1);
        element.addProperty("info2",s2);
        element.addProperty("snameId",snameId);
        element.addProperty("userName",username);
        element.addProperty("GZCLType",GZCLType);
        element.addProperty("ImagePath",ImagePath);
        element.addProperty("hshxInfo",HsHxInfo);
        ChatUtils.INSTANCE.sendMessage(Constants.CHAT_TO_USER, element.toString(),"returnInsertAllInfo",
                new NetworkCallbacks.MessageListenerThinner() {
                    @Override
                    public void processMessage(Element element, Message message, Chat chat) {
                        boolean isSuccess = element.getBody() != null &&
                                !element.getBody().equals("");
                        callback.onFinish(isSuccess, "", element.getProperty("result"));
                    }
                });
    }
}
