package com.cwt.app.controller.api;

public class MySettingsApi {
    public static final String MYSETTINGS_CONTROLLER_API = "我的设置控制器";

    public static class ListAcceptMessage {
        public static final String METHOD_API_NAME = "获取接受消息列表";
        public static final String METHOD_API_NOTE = "获取接受消息列表接口";
    }

    public static class ReadAcceptMessage {
        public static final String METHOD_API_NAME = "已读该消息";
        public static final String METHOD_API_NOTE = "已读该消息接口";
    }

    public static class CountNoReadAcceptMessage {
        public static final String METHOD_API_NAME = "获取未读接受消息数量";
        public static final String METHOD_API_NOTE = "获取未读接受消息数量接口";
    }

    public static class ListAllCommunityBulletin {
        public static final String METHOD_API_NAME = "获取所有社区公告列表";
        public static final String METHOD_API_NOTE = "获取所有社区公告列表接口";
    }

    public static class InsertComplaintProposal {
        public static final String METHOD_API_NAME = "提交投诉建议";
        public static final String METHOD_API_NOTE = "提交投诉建议接口";
        public static final String METHOD_API_CPCONTENT = "投诉建议内容";
    }

    public static class ListAllAboutUs {
        public static final String METHOD_API_NAME = "获取关于我们列表";
        public static final String METHOD_API_NOTE = "获取关于我们列表接口";
    }

    public static class ListAdSliderForApp {
        public static final String METHOD_API_NAME = "获取app首页轮播图列表";
        public static final String METHOD_API_NOTE = "获取app首页轮播图列表接口";
    }

}
