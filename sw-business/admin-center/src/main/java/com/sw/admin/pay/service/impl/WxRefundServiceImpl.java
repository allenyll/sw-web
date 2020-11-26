package com.sw.admin.pay.service.impl;

import com.sw.admin.pay.properties.WxProperties;
import com.sw.admin.pay.service.IWxRefundService;
import com.sw.admin.pay.utils.HttpUtils;
import com.sw.common.constants.WxConstants;
import com.sw.common.dto.TransactionRefundDto;
import com.sw.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description:  微信小程序退款业务
 * @Author:       allenyll
 * @Date:         2020/11/23 上f午11:55
 * @Version:      1.0
 */
@Slf4j
@Service("wxRefundService")
public class WxRefundServiceImpl implements IWxRefundService {

    @Autowired
    WxProperties wxProperties;

    @Override
    public Result<TransactionRefundDto> payRefund(String transactionNo, BigDecimal amount, BigDecimal refundAmount, String aftersaleReason) {
        Result<TransactionRefundDto> result = new Result<>();
        Map<String, String> dataMap = createData(transactionNo, amount, refundAmount, aftersaleReason);
        String signStr = createLinkString(dataMap);
        String sign = MD5Util.md5Password(signStr + "&key=" + wxProperties.getKey()).toUpperCase();
        dataMap.put("sign", sign);
        String xmlData = XmlUtil.GetMapToXML(dataMap);
        String xmlRes = HttpUtils.postData(wxProperties.getRefundUrl(), xmlData, wxProperties.getMchId());
        try {
            Map<String,String> map = XmlUtil.doXMLParse(xmlRes);
            if (map != null) {
                String resCode = MapUtil.getString(map, "result_code");
                if(WxConstants.SUCCESS.equals(resCode)) {
                    //退款成功的操作 记录退款日志
                    //返回的预付单信息
                    TransactionRefundDto refundDto = new TransactionRefundDto();
                    refundDto.setAmount(refundAmount);
                    refundDto.setRefundNo(MapUtil.getString(map, "out_refund_no"));
                    result.setObject(refundDto);
                }else{
                    log.info("退款失败原因: " + map.get("return_msg"));
                    result.setCode("1000001");
                    result.fail(map.get("return_msg"));
                    return result;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("退款失败原因: " + e.getMessage());
            result.setCode("1000001");
            result.fail(e.getMessage());
            return result;
        }
        return result;
    }


    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           商户平台设置的密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * @param content 加密内容
     * @param charset 字符类型
     * @return 加密返回字节数组
     */
    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

        /**
         * 封装退款数据
         * @param transactionNo 商户订单号
         * @param amount 订单金额
         * @param refundAmount 退款进金额
         * @return
         */
    private Map<String, String> createData(String transactionNo, BigDecimal amount, BigDecimal refundAmount, String aftersaleReason) {

        Map<String, String> data = new HashMap<>();

        //随机字符串
        String nonceStr = StringUtil.getRandomString(32);
        //商户订单号
        String outTradeNo = transactionNo;
        //商户退款订单号
        String outRefundNo = StringUtil.getOrderNo();
        //订单总金额 微信支付提交的金额是不能带小数点的，且是以分为单位,这边需要转成字符串类型，否则后面的签名会失败
        String totalFee = String.valueOf(amount.intValue());
        //退款总金额
        String refundFee = String.valueOf(refundAmount.multiply(new BigDecimal(100)).intValue());

        data.put("appid", wxProperties.getAppId());
        data.put("mch_id", wxProperties.getMchId());
        data.put("nonce_str", nonceStr);
        data.put("sign_type", "MD5");
        data.put("out_trade_no", outTradeNo);
        data.put("out_refund_no", outRefundNo);
        data.put("total_fee", totalFee);
        data.put("refund_fee", refundFee);
        data.put("notify_url", WxConstants.REFUND_NOTIFY_URL);
        data.put("refund_desc", aftersaleReason);

        return data;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {
                // 拼接时，不包括最后一个&字符
                sb.append(key).append("=").append(value);
            } else {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        return sb.toString();
    }


}
