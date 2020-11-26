package com.sw.admin.pay.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sw.admin.order.service.impl.OrderServiceImpl;
import com.sw.admin.pay.utils.HttpUtils;
import com.sw.cache.util.CacheUtil;
import com.sw.client.annotion.CurrentUser;
import com.sw.common.constants.CacheKeys;
import com.sw.common.constants.dict.OrderStatusDict;
import com.sw.common.constants.dict.OrderTradeDict;
import com.sw.common.constants.dict.PayTypeDict;
import com.sw.common.entity.order.Order;
import com.sw.common.entity.pay.Transaction;
import com.sw.common.entity.system.User;
import com.sw.common.util.*;
import com.sw.admin.pay.properties.WxProperties;
import com.sw.admin.pay.service.impl.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
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
    CacheUtil cacheUtil;

    @ResponseBody
    @RequestMapping(value = "createUnifiedOrder", method = RequestMethod.POST)
    public DataResponse createUnifiedOrder(@CurrentUser(isFull = true) User user, HttpServletRequest request, HttpServletResponse response) {
        //设置最终返回对象
        Map<String, Object> result = new HashMap<>();
        //接受参数(openid)
        String openid = request.getParameter("openid");
        String currentOpenid = cacheUtil.get(CacheKeys.WX_CURRENT_OPENID + "_" + openid);
        if (!openid.equals(currentOpenid)) {
            return DataResponse.fail("当前用户与登录用户不匹配");
        }

        //接受参数(金额)
        String amount = request.getParameter("amount");
        //用户ID
        Long customerId = Long.parseLong(request.getParameter("customerId"));
        //支付来源
        String payName = request.getParameter("payName");
        //支付备注
        String remark = request.getParameter("remark");
        Long orderId = Long.parseLong(request.getParameter("orderId"));
        //接口调用总金额单位为分换算一下(测试金额改成1,单位为分则是0.01,根据自己业务场景判断是转换成float类型还是int类型)
        BigDecimal bigAmount = new BigDecimal("0.01").multiply(new BigDecimal("100"));
        int amountFen = bigAmount.intValue();
        //创建hashmap(用户获得签名)
        SortedMap<String, String> paraMap = new TreeMap<>();
        //设置body变量 (支付成功显示在微信支付 商品详情中)
        String body = "test";
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
            Map<String,String> map = XmlUtil.doXMLParse(HttpUtils.getRemotePortData(wxProperties.getOrderUrl(), new String(xmlData.getBytes(), "ISO8859-1")));
            //应该创建 支付表数据
            if(map!=null){
//                EntityWrapper<Customer> customerEntityWrapper = new EntityWrapper<>();
//                customerEntityWrapper.eq("IS_DELETE", 0);
//                customerEntityWrapper.eq("openid", openid);
//                Customer customer = customerService.selectOne(customerEntityWrapper);
//                if(customer == null){
//                    // TODO新建用户
//                }
                // 查看是否有支付记录
                QueryWrapper<Transaction> transactionEntityWrapper = new QueryWrapper<>();
                transactionEntityWrapper.eq("CUSTOMER_ID", customerId);
                transactionEntityWrapper.eq("IS_DELETE", 0);
                List<Transaction> payInfoList = transactionService.list(transactionEntityWrapper);
                //如果等于空 则证明是第一次支付
                if(CollectionUtils.isEmpty(payInfoList)){
                    // 第一次支付
                }
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
                transaction.setStatus("SW1201");
                transaction.setPayChannel(PayTypeDict.WECHAT.getCode());
                transaction.setRemark(remark);

                transaction.setIsDelete(0);
                transaction.setAddTime(DateUtil.getCurrentDateTime());
                transaction.setAddUser(customerId);
                transaction.setUpdateTime(DateUtil.getCurrentDateTime());
                transaction.setUpdateUser(customerId);
                //插入Dao
                boolean sqlRow = transactionService.save(transaction);
                //判断
                if(sqlRow){
                    LOGGER.info("微信 统一下单 接口调用成功 并且新增支付信息成功");
                    QueryWrapper<Transaction> entityWrapper = new QueryWrapper<>();
                    entityWrapper.eq("IS_DELETE", 0);
                    entityWrapper.eq("TRANSACTION_NO", outTradeNo);
                    entityWrapper.eq("CUSTOMER_ID", customerId);
                    Transaction _transaction = transactionService.getOne(entityWrapper);
                    if(_transaction != null){
                        result.put("transaction_id", _transaction.getId());
                    }
                    result.put("prepayId", map.get("prepay_id"));
                    result.put("outTradeNo", paraMap.get("out_trade_no"));
                    return DataResponse.success(result);
                }
            }
            //将 数据包ID 返回

            System.out.println(map);
        } catch (UnsupportedEncodingException e) {
            LOGGER.info("微信 统一下单 异常："+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            LOGGER.info("微信 统一下单 异常："+e.getMessage());
            e.printStackTrace();
        }
        LOGGER.info("微信 统一下单 失败");
        return DataResponse.success();
    }

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
                updateOrder(params, transaction);
            }
        }
        return DataResponse.success();
    }


    private void updateOrder(Map<String, Object> params, Transaction transaction) {
        Long orderId = MapUtil.getLong(params, "orderId");
        Order order = orderService.getById(orderId);
        // TODO 订单不存在， 支付渠道改造
        if(order == null){
            return;
        }
        LOGGER.info("交易支付订单Order: "+order);
        order.setPayAmount(transaction.getAmount());
        // TODO 支付渠道改造
        order.setPayChannel(transaction.getPayChannel());
        order.setPayTime(transaction.getTransactionTime());
        order.setOrderStatus(OrderStatusDict.PAY.getCode());
        order.setTradeNo(transaction.getTransactionNo());
        orderService.updateById(order);
    }

}
