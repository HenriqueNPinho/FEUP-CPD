package Membership;

import java.io.Serializable;

public class MembershipInfo implements Serializable {
    private String[] nodes;
    private String[] recentLogs;

    public MembershipInfo(){}

    public MembershipInfo(String[] nodes, String[] recentLogs) {
        this.nodes = nodes;
        this.recentLogs = recentLogs;
    }

    public String[] getNodes() {
        return this.nodes;
    }

    public String[] getRecentLogs() {
        return this.recentLogs;
    }
    
}
