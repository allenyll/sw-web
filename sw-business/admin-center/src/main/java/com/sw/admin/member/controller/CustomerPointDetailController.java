package com.sw.admin.member.controller;


import com.sw.client.controller.BaseController;
import com.sw.common.entity.customer.CustomerPointDetail;
import com.sw.admin.member.service.impl.CustomerPointDetailServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yu.leilei
 * @since 2019-01-09
 */
@RestController
@RequestMapping("customerPointDetail")
public class CustomerPointDetailController extends BaseController<CustomerPointDetailServiceImpl, CustomerPointDetail> {

}
