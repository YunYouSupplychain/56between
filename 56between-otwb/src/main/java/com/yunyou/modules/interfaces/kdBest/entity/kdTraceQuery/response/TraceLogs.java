package com.yunyou.modules.interfaces.kdBest.entity.kdTraceQuery.response;

public class TraceLogs {
	private Problems problems;
    private boolean check;
	private String mailNo;
	private Traces traces;

    public Problems getProblems()
    {
        return this.problems;
    }

    public void setProblems(Problems value)
    {
        this.problems = value;
    }

    public boolean getCheck()
    {
        return this.check;
    }

    public void setCheck(boolean value)
    {
        this.check = value;
    }

    public String getMailNo()
    {
        return this.mailNo;
    }

    public void setMailNo(String value)
    {
        this.mailNo = value;
    }

    public Traces getTraces()
    {
        return this.traces;
    }

    public void setTraces(Traces value)
    {
        this.traces = value;
    }


}
