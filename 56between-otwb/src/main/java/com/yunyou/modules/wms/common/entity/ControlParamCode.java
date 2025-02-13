package com.yunyou.modules.wms.common.entity;

/**
 * 描述： 系统控制参数代码类，维护所有的控制参数代码
 *
 * @author Jianhua on 2019/1/25
 */
public enum ControlParamCode {

    /***************************************************************************
     * 公共基础 *
     **************************************************************************/

    /**
     * 仓库级参数-数量小数位数（数值）
     */
    WH_QTY_DEC("QTY_DEC"),
    /**
     * 仓库级参数-长宽高小数位数（数值）
     */
    WH_LENGTH_DEC("LEN_DEC"),
    /**
     * 仓库级参数-体积小数位数（数值）
     */
    WH_CUBIC_DEC("CUB_DEC"),
    /**
     * 仓库级参数-金额小数位数（数值）
     */
    WH_PRICE_DEC("PRI_DEC"),
    /**
     * 仓库级参数-重量小数位数（数值）
     */
    WH_WEIGHT_DEC("WGT_DEC"),

    /***************************************************************************
     * 入库管理 *
     **************************************************************************/
    /**
     * PO参数：PO单是否需要做审核（Y：需要审核；N：不用审核）
     */
    PO_AUDIT("PO_AUDIT"),
    /**
     * ASN参数：ASN单是否需要做审核（Y：需要审核；N：不用审核）
     */
    ASN_AUDIT("ASN_AUDIT"),
    /**
     * QC参数：QC单是否需要做审核（Y：需要审核；N：不用审核）
     */
    QC_AUDIT("QC_AUDIT"),
    /**
     * ASN参数：ASN明细中计划收货库位的默认值（空：不默认；非空：格式如code||name）
     */
    ASN_DEF_LOC("ASN_DEF_LOC"),
    /**
     * ASN参数：创建ASN单和收货时是否根据生产日期、保质期自动计算失效日期（Y:自动计算；N：不自动计算）
     */
    ASN_CALC_EXP_DATE("ASN_CALC_EXP_DATE"),
    /**
     * ASN参数：是否只有ASN完全收货才可以关闭ASN（Y：必须是完全收货；N：部分收货或完全收货）
     */
    ASN_ONLY_FULL_RCV_CLOSE("ASN_ONLY_FULL_RCV_CLOSE"),
    /**
     * 收货上架参数：完全收货状态，并且不存在未完成的上架任务，是否自动关闭ASN（Y：自动关闭；N：不自动关闭）
     */
    RCV_AUTO_CLOSE_ASN("RCV_AUTO_CLOSE_ASN"),
    /**
     * ASN参数：是否要求按照计划入库的序列号做扫描收货（Y：需要先录入序列号，实际商品要和计划序列号做校验；N：无计划，以实际到货为准）
     */
    ASN_IS_PLAN_SERIAL("ASN_IS_PLAN_SERIAL"),

    /**
     * 收货上架参数：生成上架任务是否进行单进程操作（Y：单进程；N：不是单进程）
     */
    RCV_PA_TASK_QUEUE("RCV_PA_TASK_QUEUE"),

    /***************************************************************************
     * 出库管理 *
     **************************************************************************/
    /**
     * SALE参数：SALE单是否需要做审核（Y：需要审核；N：不用审核）
     */
    SALE_AUDIT("SALE_AUDIT"),
    /**
     * SO参数：SO单是否需要做审核（Y：需要审核；N：不用审核）
     */
    SO_AUDIT("SO_AUDIT"),
    /**
     * SO参数：生成波次计划是否需要选择波次规则（Y：需要选择；N：不需要选择）
     */
    SO_WAVE_BY_RULE("SO_WAVE_BY_RULE"),
    /**
     * SO参数：是否只有SO完全发运才可以关闭SO（Y：必须是完全发运；N：部分发运或完全发运）
     */
    SO_ONLY_FULL_SHIP_CLOSE("SO_ONLY_FULL_SHIP_CLOSE"),
    /**
     * SO参数：订单关闭后是否同步更新面单号（Y：同步；N：不同步）
     */
    SO_SYNC_MAIL_NO("SO_SYNC_MAIL_NO"),
    /**
     * 分配参数：分配参数：是否启用两步分配，即系统必须经过预配，然后才可以做分配（Y：启用两步分配；N：不启用，而是采用一步分配）
     */
    ALLOC_TWO_STEP("ALLOC_TWO_STEP"),

    /**
     * 分配参数：分配的时候，同一笔批次库位库存是否根据OT的单位换算，1个OT生成一条分配明细（Y：是；N：多个OT生成一条分配明细）
     */
    ALLOC_SPLIT_OT("ALLOC_SPLIT_OT"),
    /**
     * 分配参数：分配的时候，同一笔批次库位库存是否根据PL的单位换算，1个PL生成一条分配明细（Y：是；N：多个PL生成一条分配明细）
     */
    ALLOC_SPLIT_PL("ALLOC_SPLIT_PL"),
    /**
     * 分配参数：分配的时候，同一笔批次库位库存是否根据CS的单位换算，1个CS生成一条分配明细（Y：是；N：多个CS生成一条分配明细）
     */
    ALLOC_SPLIT_CS("ALLOC_SPLIT_CS"),
    /**
     * 分配参数：分配的时候，同一笔批次库位库存是否根据IP的单位换算，1个IP生成一条分配明细（Y：是；N：多个IP生成一条分配明细）
     */
    ALLOC_SPLIT_IP("ALLOC_SPLIT_IP"),
    /**
     * 分配参数：分配时理货库位的默认值（空：不默认；非空：格式为Code，如SORTATION）
     */
    ALLOC_DEF_LOC("ALLOC_DEF_LOC"),
    /**
     * 分配参数：分配时是否自动产生跟踪号（Y：自动产生；N：默认为*）
     */
    ALLOC_TRACE_ID("ALLOC_TRACE_ID"),

    /**
     * 复核打包参数：复核打包重量是否必填（Y：必填；N：非必填）
     */
    CHECK_WEIGHT_NULL("CHECK_WEIGHT_NULL"),
    /**
     * 复核打包参数：复核打包是否自动产生新箱号（Y：自动产生；N：不自动产生）
     */
    CHECK_CARTON_NO("CHECK_CARTON_NO"),
    /**
     * 复核打包参数：复核打包箱号指定的生成规则名，当CHECK_CARTON_NO=Y时，查找CHECK_CARTON_NO_RULE配置的编号生成规则
     * (默认值是系统的生成箱号的编号规则) WM_TRACE_ID
     */
    CHECK_CARTON_NO_RULE("CHECK_CARTON_NO_RULE"),
    /**
     * 复核打包参数：是否一定要复核才可以发运（Y：需要复核；N：不用复核）
     */
    SHIP_NEED_CHECK("SHIP_NEED_CHECK"),
    /**
     * 复核参数：复核后是否自动做发货确认（Y:复核后自动发货，N:复核后不自动发货）
     */
    CHECK_AUTO_SHIP("CHECK_AUTO_SHIP"),
    /**
     * 打包参数：打包后是否自动做发货确认（Y:打包后自动发货，N:打包后不自动发货）
     */
    PACK_AUTO_SHIP("PACK_AUTO_SHIP"),
    /**
     * 发货参数：完全发货后是否自动关闭SO（Y：自动关闭；N：不自动关闭）
     */
    SHIP_AUTO_CLOSE_SO("SHIP_AUTO_CLOSE_SO"),
    /**
     * 复核打包参数：装箱完成是否自动打印表单（Y：自动打印；N：不自动打印）
     */
    CHECK_AUTO_PRINT_LIST("CHECK_AUTO_PRINT_LIST"),
    /**
     * 复核打包参数：装箱自动打印表单的模板，当CHECK_AUTO_PRINT=Y时，查找 CHECK_PRINT_TEMPLATE配置的报表模板
     */
    CHECK_PRINT_LIST_TEMPLATE("CHECK_PRINT_LIST_TEMPLATE"),
    /**
     * 复核打包参数：装箱完成是否自动打印装箱标签（Y：自动打印；N：不自动打印）
     */
    CHECK_AUTO_PRINT_LABEL("CHECK_AUTO_PRINT_LABEL"),
    /**
     * 复核打包参数：装箱自动打印装箱标签的模板，当CHECK_AUTO_PRINT_LABEL=Y时，查找
     * CHECK_PRINT_LABEL_TEMPLATE配置的报表模板
     */
    CHECK_PRINT_LABEL_TEMPLATE("CHECK_PRINT_LABEL_TEMPLATE"),
    /**
     * 复核打包参数：出库单复核打包时，是否允许部分复核
     */
    CHECK_ALLOW_PART("CHECK_ALLOW_PART"),
    /**
     * 装车单参数：拣货明细在何种状态下可以进行装车（PK: 完全拣货；SP: 完全发运）
     */
    LOAD_ALLOW_STATUS("LOAD_ALLOW_STATUS"),
    /**
     * 判断装车单装载数量是否可输 （N：不可输入； Y：可输入）
     */
    LOAD_IS_MODIFY_QTY("LOAD_IS_MODIFY_QTY"),

    /***************************************************************************
     * 库内管理 *
     **************************************************************************/
    /**
     * 调整单参数：调整单是否需要做审核（Y：需要审核；N：不用审核）
     */
    AD_AUDIT("AD_AUDIT"),
    /**
     * 转移单参数：转移单是否需要做审核（Y：需要审核；N：不用审核）
     */
    TF_AUDIT("TF_AUDIT"),

    /***************************************************************************
     * 操作锁(悲观锁) *
     **************************************************************************/
    /**
     * 是否开启库存操悲观锁（Y：开启；N：不开启）
     */
    WM_INV_LOCK("WM_INV_LOCK"),
    /**
     * 库存操作锁最大等待时间，单位：秒（需为正整数，不设置或设置值不合法，系统将采用默认值20秒）
     */
    WM_LOCK_MAX_WAIT("WM_LOCK_MAX_WAIT"),

    /***************************************************************************
     * 加工管理 *
     **************************************************************************/
    /**
     * 加工参数：加工单是否需要做审核（Y：需要审核；N：不用审核）
     */
    KIT_AUDIT("KIT_AUDIT"),
    /**
     * 加工参数：加工库位的默认值（空：不默认；非空：格式为Code，如WORKBENCH）
     */
    KIT_DEF_LOC("KIT_DEF_LOC"),
    /**
     * 加工参数：是否只有完全加工才可以关闭加工单 （Y：必须是完全加工；N：部分加工或完全加工，但是不能存在分配中或拣货中的情况）
     */
    KIT_ONLY_FULL_KIT_CLOSE("KIT_ONLY_FULL_KIT_CLOSE"),

    /***************************************************************************
     * 越库 *
     **************************************************************************/
    /**
     * ASN参数：越库匹配之后，码盘时，同一笔越库收货明细是否根据OT的单位换算， 1个OT生成一条收货明细（Y：是；N：多个OT生成一条收货明细）
     */
    ASN_CD_SPLIT_OT("ASN_CD_SPLIT_OT"),
    /**
     * ASN参数：越库匹配之后，码盘时，同一笔越库收货明细是否根据PL的单位换算， 1个PL生成一条收货明细（Y：是；N：多个PL生成一条收货明细）
     */
    ASN_CD_SPLIT_PL("ASN_CD_SPLIT_PL"),
    /**
     * ASN参数：越库匹配之后，码盘时，同一笔越库收货明细是否根据CS的单位换算， 1个CS生成一条收货明细（Y：是；N：多个CS生成一条收货明细）
     */
    ASN_CD_SPLIT_CS("ASN_CD_SPLIT_CS"),
    /**
     * ASN参数：越库匹配之后，码盘时，同一笔越库收货明细是否根据IP的单位换算， 1个IP生成一条收货明细（Y：是；N：多个IP生成一条收货明细）
     */
    ASN_CD_SPLIT_IP("ASN_CD_SPLIT_IP"),
    /**
     * 收货参数：当越库类型是直接越库，越库收货时候，出库操作同时进行到哪个状态 （ALLOC：分配；PK：拣货；SP：发货）
     */
    RCV_CD_OUT_STATUS("RCV_CD_OUT_STATUS"),

    /***************************************************************************
     * EDI for Integrator *
     **************************************************************************/
    /**
     * WMS系统参数：是否调用外部接口（Y：调用；N：不调用）
     */
    WM_CALL_EDI("WM_CALL_EDI"),
    /**
     * WMS系统参数：Integrator URL地址
     */
    WM_INTEGRATOR_URL("WM_INTEGRATOR_URL"),
    /**
     * WMS系统参数：采购单的Integrator配置 （格式：登录用户code.密码.报文类型，用"."隔开的）
     */
    WM_PO_INTEGRATOR_CONFIG("WM_PO_INTEGRATOR_CONFIG"),
    /**
     * WMS系统参数：入库单的Integrator配置 （格式：登录用户code.密码.报文类型，用"."隔开的）
     */
    WM_ASN_INTEGRATOR_CONFIG("WM_ASN_INTEGRATOR_CONFIG"),

    /**
     * WMS系统参数：出库单的Integrator配置 （格式：登录用户code.密码.报文类型，用"."隔开的）
     */
    WM_SO_INTEGRATOR_CONFIG("WM_SO_INTEGRATOR_CONFIG"),

    /**
     * WMS系统参数：库存的Integrator配置 （格式：登录用户code.密码.报文类型，用"."隔开的）
     */
    WM_INV_INTEGRATOR_CONFIG("WM_INV_INTEGRATOR_CONFIG");

    private String code;

    public String getCode() {
        return this.code;
    }

    ControlParamCode(String code) {
        this.code = code;
    }
}
