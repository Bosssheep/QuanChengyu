import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Server {
    static final String  sIPPre="192.168.253.";
    //本地存储路径
    static final String  sDataFileDir="c://blockchain";

    static BlockChain blockChain = new BlockChain();

    /**
     * 网络读取区块链数据保存到本地文件
     */
    public static void DowloadData(){
        File localFileDir = new File(sDataFileDir);
        //不存在数据，则初始化区块链，并写入主机
        if(!localFileDir.exists()){
            localFileDir.mkdir();
            FileOutputStream out = null;
            try{
                out = new FileOutputStream(new File(localFileDir+"//data.txt"));
                out.write((blockChain.createFirstBlock().toString()+"\r\n").getBytes());

            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //扫描局域网内的节点，找到最长的链，下载到本地
        int maxLen=0;
        String maxLenChain = "";
        for(int i=0;i<255;i+=1){
            String sThisURL="http://"+sIPPre+i+":8080/blockchain/chain.jsp";
            System.out.println(sThisURL);
            String sChain=HttpRequestUtil.net(sThisURL);
            if(sChain!=""){
                System.out.println(sChain);
                String sTemp[]=sChain.split("##");
                if(sTemp.length>maxLen){
                    maxLen=sTemp.length;
                    maxLenChain=sChain;
                }
            }
        }

        try{
            if(maxLenChain!=""){
                FileOutputStream fOS = new FileOutputStream(new File(localFileDir+"//data.txt"));
                maxLenChain=maxLenChain.replace("##", "\r\n");
                fOS.write((maxLenChain+"\r\n").getBytes());
                fOS.close();
            }
        }catch (FileNotFoundException e){

        }catch (IOException e){

        }

        /*//单机
        String sThisURL="http://"+sIPPre+":8080/blockchain/chain.jsp";
        System.out.println(sThisURL);
        String sChain=HttpRequestUtil.net(sThisURL);
        if(sChain!=""){
            System.out.println(sChain);
            String sTemp[]=sChain.split("##");
        }*/

    }
}
