package com.yunyou.modules.interfaces.edi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yunyou.common.utils.time.DateFormatUtil;
import com.yunyou.core.persistence.DataEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 接口日志Entity
 * @author zyf
 * @version 2021-05-06
 */
public class EdiLog extends DataEntity<EdiLog> {
	private static final Logger logger = LoggerFactory.getLogger(EdiLog.class);
	private static final long serialVersionUID = 1L;
	private String ediType;			// 接口类型
	@JsonFormat(pattern = DateFormatUtil.PATTERN_DEFAULT_ON_SECOND)
	private Date time;				// 发生时间
	private String sContent;		// 发送报文内容
	private String rContent;		// 接收报文内容
	private String handleStatus;	// 处理状态
	private String handleMsg;		// 处理信息
	private String def1;
	private String def2;
	private String def3;
	private String def4;
	private String def5;

	private Date occurrenceTimeFm;
	private Date occurrenceTimeTo;

	public EdiLog() {
		super();
	}

	public EdiLog(String id){
		super(id);
	}

	public static EdiLog success(String ediType, String sContent, String rContent, String... def) {
		EdiLog apiLog = new EdiLog();
		apiLog.setEdiType(ediType);
		apiLog.setTime(new Date());
		apiLog.setsContent(sContent);
		apiLog.setrContent(rContent);
		apiLog.setHandleStatus("99");
		apiLog.setHandleMsg("成功");
		if (def != null && def.length > 0) {
			apiLog.setDef1(def[0]);
			apiLog.setDef2(def.length > 1 ? def[1] : null);
			apiLog.setDef3(def.length > 2 ? def[2] : null);
			apiLog.setDef4(def.length > 3 ? def[3] : null);
			apiLog.setDef5(def.length > 4 ? def[4] : null);
		}
		return apiLog;
	}

	public static EdiLog failure(String ediType, String sContent, String rContent, Exception e, String... def) {
		EdiLog apiLog = new EdiLog();
		apiLog.setEdiType(ediType);
		apiLog.setTime(new Date());
		apiLog.setsContent(sContent);
		apiLog.setrContent(rContent);
		apiLog.setHandleStatus("90");
		apiLog.setHandleMsg(e.getClass().getSimpleName() + "：" + e.getMessage());
		if (def != null && def.length > 0) {
			apiLog.setDef1(def[0]);
			apiLog.setDef2(def.length > 1 ? def[1] : null);
			apiLog.setDef3(def.length > 2 ? def[2] : null);
			apiLog.setDef4(def.length > 3 ? def[3] : null);
			apiLog.setDef5(def.length > 4 ? def[4] : null);
		}
		if (logger.isErrorEnabled()) {
			logger.error("EDI接口 [{}] 发生异常", ediType, e);
		}
		return apiLog;
	}

	public String getEdiType() {
		return ediType;
	}

	public void setEdiType(String ediType) {
		this.ediType = ediType;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getsContent() {
		return sContent;
	}

	public void setsContent(String sContent) {
		this.sContent = sContent;
	}

	public String getrContent() {
		return rContent;
	}

	public void setrContent(String rContent) {
		this.rContent = rContent;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

	public String getHandleMsg() {
		return handleMsg;
	}

	public void setHandleMsg(String handleMsg) {
		this.handleMsg = handleMsg;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getDef2() {
		return def2;
	}

	public void setDef2(String def2) {
		this.def2 = def2;
	}

	public String getDef3() {
		return def3;
	}

	public void setDef3(String def3) {
		this.def3 = def3;
	}

	public String getDef4() {
		return def4;
	}

	public void setDef4(String def4) {
		this.def4 = def4;
	}

	public String getDef5() {
		return def5;
	}

	public void setDef5(String def5) {
		this.def5 = def5;
	}

	public Date getOccurrenceTimeFm() {
		return occurrenceTimeFm;
	}

	public void setOccurrenceTimeFm(Date occurrenceTimeFm) {
		this.occurrenceTimeFm = occurrenceTimeFm;
	}

	public Date getOccurrenceTimeTo() {
		return occurrenceTimeTo;
	}

	public void setOccurrenceTimeTo(Date occurrenceTimeTo) {
		this.occurrenceTimeTo = occurrenceTimeTo;
	}
}