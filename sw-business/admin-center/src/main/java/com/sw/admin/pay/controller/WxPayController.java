package com.sw.admin.pay.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.order.service.impl.OrderDetailServiceImpl;
import com.sw.admin.order.service.impl.OrderServiceImpl;
import com.sw.admin.pay.utils.HttpUtils;
import com.sw.cache.util.CacheUtil;
import com.sw.client.annotion.CurrentUser;
import com.sw.common.constants.CacheKeys;
import com.sw.common.constants.dict.OrderStatusDict;
import com.sw.common.constants.dict.OrderTradeDict;
import com.sw.common.constants.dict.PayTypeDict;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.order.OrderDetail;
import com.sw.common.entity.pay.Transaction;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.pay.properties.WxProperties;
import com.sw.admin.pay.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Description:  微信支付接口
 * @Author:       allenyll
 * @Date:         2019/4/4 3:40 PM
 * @Version:      1.0
 */
@Controller
@RequestMapping("/pay")
public class WxPayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayController.class);

    private static final String NOTIFY_URL = "https://www.allenyll.com/system-web/pay/payCallback";

    @Autowired
    WxProperties wxProperties;

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    OrderServiceImpl orderService;
    
    @Autowired
    OrderDetailServiceImpl orderDetailService;

    @Autowired
    CacheUtil cacheUtil;
    
//    @Autowired
//    WxPayService wxPayService;

    @Transactional
    @ResponseBody
    @RequestMapping(value = "createUnifiedOrder", method = RequestMethod.POST)
    public Result<Map<String, Object>> createUnifiedOrder(@CurrentUser(isFull = true) User user, HttpServletRequest request, HttpServletResponse response) {
        //设置最终返回对象
        Result<Map<String, Object>> result = new Result<>();
        Map<String, Object> data = new HashMap<>();
        //接受参数(openid)
        String openid = request.getParameter("openid");
        String mode = request.getParameter("mode");
        String currentOpenId = cacheUtil.get(CacheKeys.WX_CURRENT_OPENID + "_" + mode + "_" + openid, String.class);
        if (!openid.equals(currentOpenId)) {
            result.fail("当前用户与登录用户不匹配");
            return result;
        }

        Long orderId = Long.parseLong(request.getParameter("orderId"));
        
        Order order = orderService.getById(orderId);
        if (order == null || order.getId() == null) {
            result.fail("订单关联失败，请联系管理员");
            return result;
        }
        
        if (OrderStatusDict.CANCEL.getCode() == order.getOrderStatus() || OrderStatusDict.CLOSE.getCode() == order.getOrderStatus() ||
         OrderStatusDict.UN_EFFECT.getCode() == order.getOrderStatus()
        ) {
            result.fail("订单已取消或者已失效或者已关闭");
            return result;
        }
        
        if (OrderStatusDict.PAY.getCode() == order.getOrderStatus()) {
            result.fail("订单已支付，请不要重复操作");
            return result;
        }

        
        //接受参数(金额)
        String amount = request.getParameter("amount");
        //用户ID
        Long customerId = Long.parseLong(request.getParameter("customerId"));
        //支付来源
        String payName = request.getParameter("payName");
        //支付备注
        String remark = request.getParameter("remark");
        
        //接口调用总金额单位为分换算一下(测试金额改成1,单位为分则是0.01,根据自己业务场景判断是转换成float类型还是int类型)
        BigDecimal bigAmount = new BigDecimal("0.02").multiply(new BigDecimal("100"));
        int amountFen = bigAmount.intValue();
        //创建hashmap(用户获得签名)
        SortedMap<String, String> paraMap = new TreeMap<>();
        //设置body变量 (支付成功显示在微信支付 商品详情中)
        String body = "SNU";
        //设置随机字符串
        String nonceStr = StringUtil.getRandomString(11);
        //设置商户订单号
        String outTradeNo =  StringUtil.getRandomString(11);


        //设置请求参数(小程序ID)
        paraMap.put("appid", wxProperties.getAppId());
        //设置请求参数(商户号)
        paraMap.put("mch_id", wxProperties.getMchId());
        //设置请求参数(随机字符串)
        paraMap.put("nonce_str", nonceStr);

        //订单的商品
        data.put("orderId", orderId);
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailList(data);
        if (CollectionUtil.isNotEmpty(orderDetails)) {
            for (OrderDetail goodsVo : orderDetails) {
                body = body + goodsVo.getGoodsName() + "、";
            }
            if (body.length() > 0) {
                body = body.substring(0, body.length() - 1);
            }
        }
        
        //设置请求参数(商品描述)
        paraMap.put("body", body);
        //设置请求参数(商户订单号)
        paraMap.put("out_trade_no", outTradeNo);
        //设置请求参数(总金额)
        paraMap.put("total_fee", String.valueOf(amountFen));
        //设置请求参数(终端IP)
        paraMap.put("spbill_create_ip", IPUtil.getIpAddr(request, response));
        //设置请求参数(通知地址)
        paraMap.put("notify_url", NOTIFY_URL);
        //设置请求参数(交易类型)
        paraMap.put("trade_type", "JSAPI");
        //设置请求参数(openid)(在接口文档中 该参数 是否必填项 但是一定要注意 如果交易类型设置成'JSAPI'则必须传入openid)
        paraMap.put("openid", openid);
        //调用逻辑传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        String stringA = StringUtil.formatUrlMap(paraMap, false, false);
        //第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。(签名)
        String sign = MD5Util.md5Password(stringA+"&key="+wxProperties.getKey()).toUpperCase();
        paraMap.put("sign", sign);
        //将参数 编写XML格式
        String xmlData = XmlUtil.GetMapToXML(paraMap);
        try {
            //发送请求(POST)(获得数据包ID)(这有个注意的地方 如果不转码成ISO8859-1则会告诉你body不是UTF8编码 就算你改成UTF8编码也一样不好使 所以修改成ISO8859-1)
            Map<String,String> resultUn = XmlUtil.doXMLParse(HttpUtils.getRemotePortData(wxProperties.getOrderUrl(), new String(xmlData.getBytes(), "ISO8859-1")));

            if (resultUn == null) {
                result.fail("统一下单失败");
                return result;
            }
            
            // 响应报文
            String returnCode = MapUtil.getString(resultUn, "return_code");
            String returnMsg = MapUtil.getString(resultUn, "return_msg");

            LOGGER.info("微信统一下单返回状态：{},返回消息：{}", returnCode, returnMsg);
            
            if (returnCode.equalsIgnoreCase("FAIL")) {
                result.fail("支付失败: " + returnMsg);
                return result;
            }
            
            //应该创建 支付表数据
           
            //创建支付信息对象
            Transaction transaction = new Transaction();
            transaction.setId(SnowflakeIdWorker.generateId());
            transaction.setTransactionNo(outTradeNo);
            transaction.setCustomerId(customerId);
            transaction.setIntegral(0);
            transaction.setOrderId(orderId);
            transaction.setSource(payName);
            transaction.setTransactionTime(DateUtil.getCurrentDateTime());
            transaction.setAmount(new BigDecimal(amount));
            transaction.setStatus(OrderTradeDict.START.getCode());
            transaction.setPayChannel(PayTypeDict.WECHAT.getCode());
            transaction.setRemark(remark);
            transaction.setIsDelete(0);
            transaction.setAddTime(DateUtil.getCurrentDateTime());
            transaction.setAddUser(customerId);
            transaction.setUpdateTime(DateUtil.getCurrentDateTime());
            transaction.setUpdateUser(customerId);
            //插入Dao
            transactionService.save(transaction);

            LOGGER.info("微信 统一下单 接口调用成功 并且新增支付信息成功");
            data.put("transactionId", transaction.getId());
            data.put("prepayId", resultUn.get("prepay_id"));
            data.put("outTradeNo", paraMap.get("out_trade_no"));
            
            sign(data);
            result.setObject(data);
            return result;

        } catch (UnsupportedEncodingException e) {
            LOGGER.info("微信 统一下单 异常："+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.info("微信 统一下单 异常："+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 签名
     * @param result
     */
    private void sign(Map<String, Object> result) {
        LOGGER.info("微信 支付接口生成签名 方法开始");
        //获得参数(微信统一下单接口生成的prepay_id )
        String prepayId = MapUtil.getString(result, "prepayId");
        //创建 时间戳
        String timeStamp = Long.valueOf(System.currentTimeMillis()).toString();
        //创建 随机串
        String nonceStr = StringUtil.getRandomString(11);
        //创建 MD5
        String signType = "MD5";

        //创建hashmap(用户获得签名)
        SortedMap<String, String> paraMap = new TreeMap<String, String>();
        //设置(小程序ID)(这块一定要是大写)
        paraMap.put("appId", wxProperties.getAppId());
        //设置(时间戳)
        paraMap.put("timeStamp", timeStamp);
        //设置(随机串)
        paraMap.put("nonceStr", nonceStr);
        //设置(数据包)
        paraMap.put("package", "prepay_id="+prepayId);
        //设置(签名方式)
        paraMap.put("signType", signType);


        //调用逻辑传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        String stringA = StringUtil.formatUrlMap(paraMap, false, false);
        //第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。(签名)
        String sign = MD5Util.md5Password(stringA+"&key="+wxProperties.getKey()).toUpperCase();

        if(StringUtil.isNotEmpty(sign)){
            //返回签名信息
            result.put("sign", sign);
            //返回随机串(这个随机串是新创建的)
            result.put("nonceStr", nonceStr);
            //返回时间戳
            result.put("timeStamp", timeStamp);
            //返回数据包
            result.put("package", "prepay_id="+prepayId);
            LOGGER.info("微信 支付接口生成签名 设置返回值: {}", result);
        }
        LOGGER.info("微信 支付接口生成签名 方法结束");
    }

//    public DataResponse createOrder(@CurrentUser(isFull = true) User user, HttpServletRequest request, HttpServletResponse response) {
//        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();
//        //接受参数(openid)
//        String openid = request.getParameter("openid");
//        String mode = request.getParameter("mode");
//        String currentOpenId = cacheUtil.get(CacheKeys.WX_CURRENT_OPENID + "_" + mode + "_" + openid, String.class);
//        if (!openid.equals(currentOpenId)) {
//            return DataResponse.fail(user.getUserName() + "当前用户与登录用户不匹配");
//        }
//
//        //接受参数(金额)
//        String amount = request.getParameter("amount");
//        //用户ID
//        Long customerId = Long.parseLong(request.getParameter("customerId"));
//        //支付来源
//        String payName = request.getParameter("payName");
//        //支付备注
//        String remark = request.getParameter("remark");
//        Long orderId = Long.parseLong(request.getParameter("orderId"));
//        //接口调用总金额单位为分换算一下(测试金额改成1,单位为分则是0.01,根据自己业务场景判断是转换成float类型还是int类型)
//        BigDecimal bigAmount = new BigDecimal("0.02").multiply(new BigDecimal("100"));
//        int amountFen = bigAmount.intValue();
//        //设置随机字符串
//        String nonceStr = StringUtil.getRandomString(11);
//        //设置商户订单号
//        String outTradeNo =  StringUtil.getRandomString(11);
//
//
//        wxPayUnifiedOrderRequest.setNotifyUrl(NOTIFY_URL);
//        wxPayUnifiedOrderRequest.setTradeType("JSAPI");
//        wxPayUnifiedOrderRequest.setBody("先写测试商品");
//        //wxPayUnifiedOrderRequest.setDetail();
//        wxPayUnifiedOrderRequest.setOutTradeNo(outTradeNo);
//        // wxPayUnifiedOrderRequest.setFeeType();
//        wxPayUnifiedOrderRequest.setTotalFee(amountFen);
//        wxPayUnifiedOrderRequest.setSpbillCreateIp(IPUtil.getIpAddr(request, response));
////        wxPayUnifiedOrderRequest.setTimeStart();
////        wxPayUnifiedOrderRequest.setTimeExpire();
////        wxPayUnifiedOrderRequest.setGoodsTag();
////        wxPayUnifiedOrderRequest.setProductId();
////        wxPayUnifiedOrderRequest.setLimitPay();
//        wxPayUnifiedOrderRequest.setOpenid(openid);
////        wxPayUnifiedOrderRequest.setSubOpenid();
////        wxPayUnifiedOrderRequest.setReceipt();
////        wxPayUnifiedOrderRequest.setSceneInfo();
////        wxPayUnifiedOrderRequest.setFingerprint();
////        wxPayUnifiedOrderRequest.setProfitSharing();
////        wxPayUnifiedOrderRequest.setWorkWxSign();
//        wxPayUnifiedOrderRequest.setAppid(wxProperties.getAppId());
//        wxPayUnifiedOrderRequest.setMchId(wxProperties.getMchId());
//        wxPayUnifiedOrderRequest.setNonceStr(nonceStr);
////        wxPayUnifiedOrderRequest.setSubAppId();
////        wxPayUnifiedOrderRequest.setSubMchId();
////        wxPayUnifiedOrderRequest.setSign();
//        wxPayUnifiedOrderRequest.setSignType("MD5");
//
//
////        try {
////            WxPayUnifiedOrderResult wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
////        } catch (WxPayException e) {
////            e.printStackTrace();
////        }
//        return DataResponse.success();
//    }

    @ResponseBody
    @RequestMapping(value = "sign", method = RequestMethod.POST)
    public DataResponse sign(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("微信 支付接口生成签名 方法开始");
        //实例化返回对象
        Map<String, Object> result = new HashMap<>();

        //获得参数(微信统一下单接口生成的prepay_id )
        String prepayId = request.getParameter("prepayId");
        //创建 时间戳
        String timeStamp = Long.valueOf(System.currentTimeMillis()).toString();
        //创建 随机串
        String nonceStr = StringUtil.getRandomString(11);
        //创建 MD5
        String signType = "MD5";

        //创建hashmap(用户获得签名)
        SortedMap<String, String> paraMap = new TreeMap<String, String>();
        //设置(小程序ID)(这块一定要是大写)
        paraMap.put("appId", wxProperties.getAppId());
        //设置(时间戳)
        paraMap.put("timeStamp", timeStamp);
        //设置(随机串)
        paraMap.put("nonceStr", nonceStr);
        //设置(数据包)
        paraMap.put("package", "prepay_id="+prepayId);
        //设置(签名方式)
        paraMap.put("signType", signType);


        //调用逻辑传入参数按照字段名的 ASCII 码从小到大排序（字典序）
        String stringA = StringUtil.formatUrlMap(paraMap, false, false);
        //第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。(签名)
        String sign = MD5Util.md5Password(stringA+"&key="+wxProperties.getKey()).toUpperCase();

        if(StringUtil.isNotEmpty(sign)){
            //返回签名信息
            result.put("sign", sign);
            //返回随机串(这个随机串是新创建的)
            result.put("nonceStr", nonceStr);
            //返回时间戳
            result.put("timeStamp", timeStamp);
            //返回数据包
            result.put("package", "prepay_id="+prepayId);

            LOGGER.info("微信 支付接口生成签名 设置返回值");
        }
        LOGGER.info("微信 支付接口生成签名 方法结束");
        return DataResponse.success(result);
    }

    @RequestMapping(value = "/payCallback")
    public void payCallback(HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("微信回调接口方法 start");
        LOGGER.info("微信回调接口 操作逻辑 start");
        String inputLine = "";
        String notityXml = "";
        try {
            while((inputLine = request.getReader().readLine()) != null){
                notityXml += inputLine;
            }
            //关闭流
            request.getReader().close();
            LOGGER.info("微信回调内容信息："+notityXml);
            //解析成Map
            Map<String,String> map =  XmlUtil.doXMLParse(notityXml);
            //判断 支付是否成功
            if("SUCCESS".equals(map.get("result_code"))){
                LOGGER.info("微信回调返回是否支付成功：是");
                //获得 返回的商户订单号
                String outTradeNo = map.get("out_trade_no");
                LOGGER.info("微信回调返回商户订单号："+outTradeNo);
                QueryWrapper<Transaction> wrapper = new QueryWrapper<>();
                wrapper.eq("TRANSACTION_NO", outTradeNo);
                wrapper.eq("IS_DELETE", 0);
                //访问DB
                Transaction payInfo = transactionService.getOne(wrapper);
                LOGGER.info("微信回调 根据订单号查询订单状态："+payInfo.getStatus());
                if(OrderTradeDict.START.getCode().equals(payInfo.getStatus())){
                    //修改支付状态
                    payInfo.setStatus(OrderTradeDict.COMPLETE.getCode());
                    //更新Bean
                    boolean sqlRow = transactionService.updateById(payInfo);
                    //判断 是否更新成功
                    if(sqlRow){
                        LOGGER.info("微信回调  订单号："+outTradeNo +",修改状态成功");
                        //封装 返回值
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("<xml>");
                        buffer.append("<return_code>SUCCESS</return_code>");
                        buffer.append("<return_msg>OK</return_msg>");
                        buffer.append("</xml>");

                        //给微信服务器返回 成功标示 否则会一直询问 咱们服务器 是否回调成功
                        PrintWriter writer = response.getWriter();
                        //返回
                        writer.print(buffer.toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/updateStatus",method = RequestMethod.POST)
    @ResponseBody
    public DataResponse updateObj(@RequestBody Map<String, Object> params){
        String transactionId = MapUtil.getMapValue(params, "transactionId");
        String type = MapUtil.getString(params, "type");
        QueryWrapper<Transaction> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("IS_DELETE", 0);
        entityWrapper.eq("ID", transactionId);
        Transaction transaction = transactionService.getOne(entityWrapper);
        if(transaction != null){
            transaction.setStatus("SW1202");
            transactionService.updateById(transaction);
            // 更新订单状态
            if("order".equals(type)){
                orderService.updateOrder(params, transaction);
            }
        }
        return DataResponse.success();
    }

}
