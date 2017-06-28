package com.linzaixian.util.string;

/**
 * @author linzaixian
 * @since 2017-06-26 20:00:15 
 */
public class StringUtil {
    /**
     * 首字母大写
     * @param str
     * @return
     */
    public static String firstUp(String str){
        char[] ch = str.toCharArray();  
        if (ch[0] >= 'a' && ch[0] <= 'z') {  
            ch[0] = (char) (ch[0] - 32);  
        }  
        return new String(ch);
    }
    /**
     * 首字母小写
     * @param str
     * @return
     */
    public static String firstLow(String str){
        char[] ch = str.toCharArray();  
        if (ch[0] >= 'A' && ch[0] <= 'Z') {  
            ch[0] = (char) (ch[0] + 32);  
        }  
        return new String(ch);
    }
    /**
     * 对字符串根据分隔符进行单词化
     * @param str
     * @param separator
     * @param isFirstUp
     * @return
     */
    public static String toWord(String str,String separator,Boolean isFirstUp){
        String[] array=str.split(separator);
        StringBuilder sb=new StringBuilder();
        for(String one:array){
            one=one.toLowerCase();
            if(isFirstUp==null){
                sb.append(one);
            }else if(isFirstUp==true){
                sb.append(firstUp(one));
            }else if(isFirstUp==false){
                sb.append(firstLow(one));
            }
        }
        return sb.toString();
        
    }
    
    
    public static void main(String[] args) {
        System.out.println(firstUp("A"));
        System.out.println(firstLow("A"));
        System.out.println(toWord("abC", "_",true));
    }
}
