package com.cwt.app.controller.api;

/**
 * \* <p>Desciption:</p>
 * \* CreateTime: 2018/7/21 11:13
 * \* User: XianChaoWei
 * \* Version: V1.0
 * \
 */
public class WalletApi {
    public static final String WALLET_CONTROLLER_API = "钱包信息控制器";

    public static class selectByUserId {
        public static final String METHOD_API_NAME = "根据用户Id查询钱包";
        public static final String METHOD_API_NOTE = "根据用户ID查询钱包信息，返回钱包对象";
        public static final String METHOD_API_USERID = "用户ID";
    }

    public static class selectByWalletId {
        public static final String METHOD_API_NAME = "根据钱包Id查询钱包";
        public static final String METHOD_API_NOTE = "根据用户ID查询钱包信息，返回钱包对象";
        public static final String METHOD_API_WALLETID = "钱包ID";
    }

    public static class selectTransferBill {
        public static final String METHOD_API_NAME = "查询转出信息";
        public static final String METHOD_API_NOTE = "根据转账人id查询转账记录信息";
        public static final String METHOD_API_NOTE_PAGENUM = "第几页";
        public static final String METHOD_API_NOTE_PAGESIZE = "一页几条";
    }

    public static class selectCollectingBill {
        public static final String METHOD_API_NAME = "查询收款信息";
        public static final String METHOD_API_NOTE = "根据转账人id查询收款记录信息";
        public static final String METHOD_API_NOTE_PAGENUM = "第几页";
        public static final String METHOD_API_NOTE_PAGESIZE = "一页几条";
    }

    public static class selectRecommendAwardBill {
        public static final String METHOD_API_NAME = "查询推荐奖记录信息";
        public static final String METHOD_API_NOTE = "根据转账人id查询推荐奖记录信息";
    }

    public static class selectAutoUpdateBalanceBill {
        public static final String METHOD_API_NAME = "查询智能释放记录信息";
        public static final String METHOD_API_NOTE = "根据转账人id查询智能释放记录信息";
    }

    public static class selectTransBill {
        public static final String METHOD_API_NAME = "查询余额兑换信息";
        public static final String METHOD_API_NOTE = "根据转账人id查询余额兑换记录信息";
        public static final String METHOD_API_NOTE_PAGENUM = "第几页";
        public static final String METHOD_API_NOTE_PAGESIZE = "一页几条";
    }

    public static class selectGameCoinTransBill {
        public static final String METHOD_API_NAME = "查询游戏币兑换信息";
        public static final String METHOD_API_NOTE = "根据转账人id查询游戏币兑换记录信息";
        public static final String METHOD_API_NOTE_PAGENUM = "第几页";
        public static final String METHOD_API_NOTE_PAGESIZE = "一页几条";
    }

    public static class selectByDateName {
        public static final String METHOD_API_NAME = "根据数据名获取数据";
        public static final String METHOD_API_NOTE = "查询信息表，根据数据名查询，返回数据";
        public static final String METHOD_API_DATENAME = "数据名";
    }

    public static class selectGradeByUserId {
        public static final String METHOD_API_NAME = "根据用户id查询等级信息";
        public static final String METHOD_API_NOTE = "查询等级信息";

    }

    public static class updateWallet {
        public static final String METHOD_API_NAME = "更新钱包";
        public static final String METHOD_API_NOTE = "更新钱包信息";
        public static final String METHOD_API_WALLET = "更新的钱包";
    }

    public static class saveByUserId {
        public static final String METHOD_API_NAME = "保存钱包";
        public static final String METHOD_API_NOTE = "保存钱包信息";
        public static final String METHOD_API_USERID = "用户ID";
    }

    public static class transformHashrate {
        public static final String METHOD_API_NAME = "转换算力";
        public static final String METHOD_API_NOTE = "余额转换算力功能";
        public static final String METHOD_API_TRANSBALANCE = "转换余额";
        public static final String METHOD_API_PASSWORD = "交易密码";
    }

    public static class gameCoinTransformHashrate {
        public static final String METHOD_API_NAME = "转换算力";
        public static final String METHOD_API_NOTE = "游戏币转换算力功能";
        public static final String METHOD_API_TRANSGAMECOIN = "转换游戏币";
        public static final String METHOD_API_PASSWORD = "交易密码";
    }

    public static class transferAccounts {
        public static final String METHOD_API_NAME = "节点转账(转出)";
        public static final String METHOD_API_NOTE = "余额转账";
        public static final String METHOD_API_TRANSBALANCE = "交易余额";
        public static final String METHOD_API_TELEPHONE = "收款人账号";
        public static final String METHOD_API_PASSWORD = "交易密码";
    }

    public static class listAllForce {
        public static final String METHOD_API_NAME = "查询当前账号所有算力更改记录";
        public static final String METHOD_API_NOTE = "查询当前账号所有算力更改记录";
        public static final String METHOD_API_PAGENUM = "起始行号";
        public static final String METHOD_API_PAGESIZE = "总行数";
    }

    public static class listAllBalance {
        public static final String METHOD_API_NAME = "查询当前账号所有余额更改记录";
        public static final String METHOD_API_NOTE = "查询当前账号所有余额更改记录";
        public static final String METHOD_API_PAGENUM = "起始行号";
        public static final String METHOD_API_PAGESIZE = "总行数";
    }

    public static class headerUpdateBalance {
        public static final String METHOD_API_NAME = "获取每天的智能释放";
        public static final String METHOD_API_NOTE = "获取每天的智能释放";
    }

    public static class selectUpdateBalance {
        public static final String METHOD_API_NAME = "查询每天的智能释放的值";
        public static final String METHOD_API_NOTE = "查询每天的智能释放的值";
    }

}
