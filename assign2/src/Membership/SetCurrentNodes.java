package Membership;

import Main.Store;

public class SetCurrentNodes implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        for(String logLine : Store.log) {
            String[] logLineSplit = logLine.split(" ");
            
            int counter = Integer.parseInt(logLineSplit[1]);
            if(counter % 2 == 0) {
                boolean n = false;
                for(String node : Store.currentNodes) {
                    if(node.equals(logLineSplit[0])) {
                        n = true;
                        break;
                    }
                }
                if(!n) 
                Store.currentNodes.add(logLineSplit[0]);

            }
            else {
                for(int i = 0; i < Store.currentNodes.size(); i++) {
                    if(Store.currentNodes.get(i).equals(logLineSplit[0])) {
                        Store.currentNodes.remove(i);
                    }
                }
            }
        }   
        System.out.println("Current nodes updated");
    }
    
    
}
