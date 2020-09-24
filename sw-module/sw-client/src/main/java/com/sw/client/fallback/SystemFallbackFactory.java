package com.sw.client.fallback;

import com.sw.client.feign.SystemFeignClient;
import com.sw.common.entity.system.Dict;
import com.sw.common.entity.system.Log;
import com.sw.common.entity.system.SysUserRole;
import com.sw.common.entity.system.User;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description:  system-center 降级策略
 * @Author:       allenyll
 * @Date:         2020/5/4 8:30 下午
 * @Version:      1.0
 */
@Component
public class SystemFallbackFactory implements FallbackFactory<SystemFeignClient> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemFallbackFactory.class);

    @Override
    public SystemFeignClient create(Throwable throwable) {
        return new SystemFeignClient() {

            @Override
            public void save(User userToAdd) {
                LOGGER.error("FEIGN调用：调用system-center服务的save方法失败");
            }

            @Override
            public User selectOne(Map<String, Object> params) {
                LOGGER.error("FEIGN调用：调用system-center服务的selectOne方法失败");
                return null;
            }

            @Override
            public SysUserRole selectOneSysUserRole(Map<String, String> params) {
                LOGGER.error("FEIGN调用：调用system-center服务的selectOneSysUserRole方法失败");
                return null;
            }

            @Override
            public List<Map<String, Object>> getUserRoleMenuList(Map<String, String> param) {
                LOGGER.error("FEIGN调用：调用system-center服务的getUserRoleMenuList方法失败");
                return null;
            }

            @Override
            public void saveLog(Log sysLog) {
                LOGGER.error("FEIGN调用：调用system-center服务日志接口的保存日志saveLog方法失败");
            }

            @Override
            public User selectById(Long userId) {
                LOGGER.error("FEIGN调用：根据ID获取用户失败");
                return null;
            }

            @Override
            public Dict getDictByCode(String orderStatus) {
                LOGGER.error("FEIGN调用：获取字典失败");
                return null;
            }

            @Override
            public User selectUserByName(String userName) {
                LOGGER.error("FEIGN调用：根据用户名查询用户失败");
                return null;
            }

        };
    }
}
