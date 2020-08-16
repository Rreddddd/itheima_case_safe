package lc.test.case_fase.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Encrypt {

    static String md5(String str){
        if(str!=null){
            try {
                MessageDigest digest=MessageDigest.getInstance("MD5");
                byte[] bytes = digest.digest(str.getBytes());
                StringBuilder sb=new StringBuilder();
                String hex;
                for(byte _byte : bytes){
                    hex=Integer.toHexString(_byte & 0xff);
                    if(hex.length()==1){
                        hex="0"+hex;
                    }
                    sb.append(hex);
                }
                return sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
