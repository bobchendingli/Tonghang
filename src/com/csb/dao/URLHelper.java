package com.csb.dao;

/**
 * 服务器请求URL
 * @author bobo
 *
 */
public class URLHelper {
    //base url
    private static final String URL_TH = "http://t.iappbee.com:7700/Wap/";;//"http://i.college-cardio.org/Wap/"
//    private static final String URL_TH = "http://124.254.5.211:7700/Wap/";
    //服务器文件存放目录
    public static final String URL_SERVER_UPLOAD = "http://t.iappbee.com:7700/Uploads/";;//"http://i.college-cardio.org/Uploads/"
    public static final String URL_SERVER = "http://t.iappbee.com:7700";//"http://i.college-cardio.org";
    //注册
    public static final String URL_REGISTER = URL_TH + "Register/index";
    //短信验证码
    public static final String URL_REQUEST_SMS_VERIFICATION = URL_TH + "Captcha/index";
    public static final String URL_REQUEST_SMS_VERIFICATION_1 = URL_TH + "Captcha/identify";
    //登录接口
    public static final String URL_LOGIN = URL_TH + "Login/index";
    //头像上传接口
    public static final String URL_UPLOAD_HEAD_PIC = URL_TH + "UploadImage/index";
    //医师执照上传接口
    public static final String URL_UPLOAD_DACTOR_LICENSE = URL_TH + "UploadLicense/index";
    //用户信息修改接口
    public static final String URL_UPDATE_USER = URL_TH +"UpdateUserInfo/index";
    //获得会议列表接口
    public static final String URL_GET_MEETINGLIST = URL_TH + "Meeting/getMeetingList";
    //获得会议详细信息接口
    public static final String URL_GET_MEETINGINFO = URL_TH + "Meeting/getMeetingInfo";
    //更新会议状态接口
    public static final String URL_UPDATEMEETINGSTATUS = URL_TH + "Meeting/updateMeetingStatus";
    //获得会议提问内容接口
    public static final String URL_GETMEETINGQUESTION = URL_TH + "Meeting/getMeetingQuestion";
    //上传会议提问内容接口
    public static final String URL_UPDATEMEETINGQUESTION = URL_TH + "Meeting/updateMeetingQuestion";
    //获取会议问卷内容接口
    public static final String URL_GETMEETINGQUESTIONNAIRE = URL_TH + "Meeting/getQuestionnaire";
    //上传会议问卷答案接口
    public static final String URL_UPDATEMEETINGQUESTIONNAIREANSWER = URL_TH + "Meeting/updateQuestionnaireAnwser";
    //会议文件下载接口
    public static final String URL_DOWNLOADFILELIST = URL_TH + "Meeting/downloadFile";
    //升级版本接口
    public static final String URL_UPDATEVERSION = URL_TH + "UpdateVersion/index";
    //意见反馈接口
    public static final String URL_FEEDBACK = URL_TH + "Feedback/index";
}
