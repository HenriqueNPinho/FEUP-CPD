package Membership;

import java.io.Serializable;
import java.util.ArrayList;

public class MembershipInfo implements Serializable {
    private ArrayList<String> nodes;
    private ArrayList<String> recentLogs;

    public MembershipInfo(){}

    public MembershipInfo(ArrayList<String> nodes, ArrayList<String> recentLogs) {
        this.nodes = nodes;
        this.recentLogs = recentLogs;
    }

    public ArrayList<String> getNodes() {
        return this.nodes;
    }

    public ArrayList<String> getRecentLogs() {
        return this.recentLogs;
    }
    
}
