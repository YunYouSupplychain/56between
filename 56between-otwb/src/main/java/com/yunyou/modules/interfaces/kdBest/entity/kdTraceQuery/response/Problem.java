package com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response;

public class Problem {
	private long seqNum;
	private String problemType;
	private String registerMan;
	private String registerDate;
	private String registerSite;
	private String problemCause;
	private String noticeSite;
	private String replyMan;
	private String replyDate;
	private String replyContent;

    public long getSeqNum()
    {
        return this.seqNum;
    }

    public void setSeqNum(long value)
    {
        this.seqNum = value;
    }

    public String getProblemType()
    {
        return this.problemType;
    }

    public void setProblemType(String value)
    {
        this.problemType = value;
    }

    public String getRegisterMan()
    {
        return this.registerMan;
    }

    public void setRegisterMan(String value)
    {
        this.registerMan = value;
    }

    public String getRegisterDate()
    {
        return this.registerDate;
    }

    public void setRegisterDate(String value)
    {
        this.registerDate = value;
    }

    public String getRegisterSite()
    {
        return this.registerSite;
    }

    public void setRegisterSite(String value)
    {
        this.registerSite = value;
    }

    public String getProblemCause()
    {
        return this.problemCause;
    }

    public void setProblemCause(String value)
    {
        this.problemCause = value;
    }

    public String getNoticeSite()
    {
        return this.noticeSite;
    }

    public void setNoticeSite(String value)
    {
        this.noticeSite = value;
    }

    public String getReplyMan()
    {
        return this.replyMan;
    }

    public void setReplyMan(String value)
    {
        this.replyMan = value;
    }

    public String getReplyDate()
    {
        return this.replyDate;
    }

    public void setReplyDate(String value)
    {
        this.replyDate = value;
    }

    public String getReplyContent()
    {
        return this.replyContent;
    }

    public void setReplyContent(String value)
    {
        this.replyContent = value;
    }

}
