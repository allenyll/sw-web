package com.sw.cache.util;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串 工具类
 * @Author: yu.leilei
 * @Date: 下午 3:01 2018/3/6 0006
 */
public class StringUtil {

    /**
     * <p>
     * 安全的做字符串替换
     * </p>
     *
     * 将<b>源字符串</b>中的<b>目标字符串</b>全部替换成<b>替换字符串</b> 规则如下：
     * <ol>
     * <li>若source为null,则结果亦 为null</li>
     * <li>若target为null,则结果为source</li>
     * <li>若replacement为null,则结果为source中的target全部被剔除后的新字符串</li>
     * </ol>
     *
     * @param source
     *            源字符串
     * @param target
     *            目标字符串
     * @param replacement
     *            替换字符串
     * @return 替换过的字符串
     */
    public static String safeReplace(String source, String target,
                                     String replacement) {
        if (source == null || source.isEmpty() || target == null
                || target.isEmpty() || target.equals(replacement)) {
            return source;
        }

        List<Integer> offsets = new ArrayList<Integer>();
        int targetLen = target.length();
        int offset = 0;
        while (true) {
            offset = source.indexOf(target, offset);
            if (offset == -1) {
                break;
            }

            offsets.add(offset);
            offset += targetLen;
        }

        String result = source;
        if (!offsets.isEmpty()) {
            // 计算结果字符串数组长度
            int sourceLen = source.length();
            if (replacement == null) {
                replacement = "";
            }

            int replacementLen = replacement.length();

            int offsetsSize = offsets.size();
            int resultLen = sourceLen + (replacementLen - targetLen)
                    * offsetsSize;

            // 源/目标字符数组
            char[] sourceCharArr = source.toCharArray();
            char[] replacementCharArr = replacement.toCharArray();
            char[] destCharArr = new char[resultLen];

            // 做第一轮替换
            int firstOffset = offsets.get(0);
            System.arraycopy(sourceCharArr, 0, destCharArr, 0, firstOffset);
            if (replacementLen > 0) {
                System.arraycopy(replacementCharArr, 0, destCharArr,
                        firstOffset, replacementCharArr.length);
            }

            // 中间替换 // 前一个偏移量
            int preOffset = firstOffset;
            // 目标char数组目前的有效长度(即已经填入的字符数量)
            int destPos = firstOffset + replacementCharArr.length;
            for (int i = 1; i < offsetsSize; i++) {
                // 当前偏移量
                offset = offsets.get(i);
                int fragmentLen = offset - preOffset - targetLen;
                System.arraycopy(sourceCharArr, preOffset + targetLen,
                        destCharArr, destPos, fragmentLen);
                destPos += fragmentLen;
                if (replacementLen > 0) {
                    System.arraycopy(replacementCharArr, 0, destCharArr,
                            destPos, replacementCharArr.length);
                }
                preOffset = offset;
                destPos += replacementCharArr.length;
            }

            // 做末轮替换
            int lastFragmentLen = sourceLen - preOffset - targetLen;
            System.arraycopy(sourceCharArr, preOffset + targetLen, destCharArr,
                    destPos, lastFragmentLen);

            result = new String(destCharArr);
        }

        return result;
    }

    /**
     * 对象是否为空
     * @param o String,List,Map,Object[],int[],long[]
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            if (o.toString().trim().equals("")) {
                return true;
            }
        } else if (o instanceof List) {
            if (((List) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Map) {
            if (((Map) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Set) {
            if (((Set) o).size() == 0) {
                return true;
            }
        } else if (o instanceof Object[]) {
            if (((Object[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof int[]) {
            if (((int[]) o).length == 0) {
                return true;
            }
        } else if (o instanceof long[]) {
            if (((long[]) o).length == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     * @param strVal
     * @return true 不为空 false 为空
     */
    public static boolean isNotEmpty(Object strVal) {
        return !isEmpty(strVal);
    }

    public static String getOrderNo(String id, SimpleDateFormat format){
        StringBuilder sb = new StringBuilder();
        //日期
        sb.append(format.format(new Date()));
        //四位主键id
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(id);
        String numStr = m.replaceAll("").trim();
        if (numStr.length() > 19){
            numStr = numStr.substring(0, 18);
        }
        String _id = String.format("%04d", Long.parseLong(numStr)).substring(0,4);
        sb.append(_id);
        //三位随机数
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result += random.nextInt(10);
        }
        sb.append(result);
        return sb.toString();
    }

    /**
     * 获取随机位数的字符串
     *
     * @Date 2017/8/24 14:09
     */
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成32位uuid
     * @return
     */
    public static String getUUID32(){
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     *
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap   要排序的Map对象
     * @param urlEncode   是否需要URLENCODE
     * @param keyToLower    是否需要将Key转换为全小写
     *            true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower){
        String buff;
        Map<String, String> tmpMap = paraMap;
        try
        {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, Comparator.comparing(o -> (o.getKey())));
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds)
            {
                if (isNotEmpty(item.getKey()))
                {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode)
                    {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower)
                    {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else
                    {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false)
            {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e)
        {
            return null;
        }
        return buff;
    }

}
