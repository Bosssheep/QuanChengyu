
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.security.*;


/**
 * 区块链
 */
public class BlockChain {
    private List<Block> blockChain = new ArrayList<>();

    public BlockChain(){

    }
    public String blockChainString(){
        return blockChain.toString();
    }
    /**
     * 当一个用户按照成语接龙的规则，对上上一个成语，并且系统验证这成语正确（工作量被证明）。
     * 这个时候我们就可以创建一个新块，并且加到链里面。
     * @return 新块
     */
    public Block newBlock(int i, String proof, String hash, Timestamp createTime, String sSender, String recipient){
        Block bRet=null;
        //在这里创建一个新块
        bRet = new Block(i,proof,hash,createTime,sSender,recipient);

        return bRet;
    }

    /**
     *  创始块的创建，创世块是一个块，必须是固定的信息
     *  逻辑上来说，只有在区块链产品的第一个用户第一次启动的时候，才会需要创建创世块
     * @return 创世块
     */
    public Block createFirstBlock(){
        try{
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            return newBlock(0,"海阔天空","",ts,"","");
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成块的Hash字符串
     * @param block
     * @return
     */
    public String Hash(Block block){
        String bHash = null;
        //在这里Hash 一个块
        String s=block.sPreviousHash+block.sProof+block.sRecipient+block.sSender+block.tsCreateTime.toString();
        bHash = MD5(s);
        return bHash;
    }
    private static String MD5(String key){
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try{
            byte[]  byteInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = java.security.MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(byteInput);
            // 获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 成语有效证明
     * @param pre
     * @param cur
     * @return
     */
    public static boolean validProof(String pre,String cur){
        //验证这个成语的头一个字是不是上一个成语的最后一个字
        if(cur.charAt(0)!=pre.charAt(pre.length()-1)){
            return false;
        }

        //通过接口验证是否是成语，也就是工作量证明
        int resultCode=HttpRequestUtil.getRequest1(cur);
        if(resultCode==0){
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] arsgs){
        validProof("七上下八","八面玲珑");

    }

}
