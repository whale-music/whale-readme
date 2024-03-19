package org.web.webdav.auth;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.RealmBase;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.AccountService;
import org.springframework.stereotype.Component;
import org.web.webdav.common.enums.WebdavMethod;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Component
public class WebdavRealm extends RealmBase {
    private static final List<String> DEFAULT_ROLES = List.of("webdav");
    /**
     * 删除资源 Options、Head、Trace、Get、PropFind、PropFind、Mkcol、Put、Post、Copy、Move、Delete。
     */
    private static final List<WebdavMethod> DELETES_METHODS = Arrays.asList(WebdavMethod.GET,
            WebdavMethod.HEAD,
            WebdavMethod.TRACE,
            WebdavMethod.OPTIONS,
            WebdavMethod.PROPFIND,
            WebdavMethod.PROPPATCH,
            WebdavMethod.MKCOL,
            WebdavMethod.PUT,
            WebdavMethod.POST,
            WebdavMethod.COPY,
            WebdavMethod.MOVE,
            WebdavMethod.DELETE,
            WebdavMethod.LOCK,
            WebdavMethod.UNLOCK);
    /**
     * 修改资源 Options、Head、Trace、Get、PropFind、PropFind、Mkcol、Put、Post、Copy、Move。
     */
    private static final List<WebdavMethod> UPDATE_METHODS = Arrays.asList(WebdavMethod.GET,
            WebdavMethod.HEAD,
            WebdavMethod.TRACE,
            WebdavMethod.OPTIONS,
            WebdavMethod.PROPFIND,
            WebdavMethod.PROPPATCH,
            WebdavMethod.MKCOL,
            WebdavMethod.PUT,
            WebdavMethod.POST,
            WebdavMethod.COPY,
            WebdavMethod.MOVE,
            WebdavMethod.LOCK,
            WebdavMethod.UNLOCK);
    /**
     * 创建资源 Options、Head、Trace、Get、PropFind、PropFind、Mkcol、Lock、UnLock、Put、Post。
     */
    private static final List<WebdavMethod> UPLOAD_METHODS = Arrays.asList(WebdavMethod.GET,
            WebdavMethod.HEAD,
            WebdavMethod.TRACE,
            WebdavMethod.OPTIONS,
            WebdavMethod.PROPFIND,
            WebdavMethod.PROPPATCH,
            WebdavMethod.MKCOL,
            WebdavMethod.PUT,
            WebdavMethod.POST,
            WebdavMethod.LOCK,
            WebdavMethod.UNLOCK);
    /**
     * 浏览检索资源 Options、Head、Trace、Get、PropFind、PropFind。
     */
    private static final List<WebdavMethod> LIST_METHODS = Arrays.asList(WebdavMethod.GET,
            WebdavMethod.HEAD,
            WebdavMethod.TRACE,
            WebdavMethod.OPTIONS,
            WebdavMethod.PROPFIND,
            WebdavMethod.PROPPATCH);
    private final AccountService accountService;
    
    
    public WebdavRealm(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @Override
    protected String getPassword(String username) {
        return null;
    }
    
    @Override
    protected Principal getPrincipal(String username) {
        return new GenericPrincipal(username, DEFAULT_ROLES);
    }
    
    @Override
    public Principal authenticate(String username, String password) {
        try {
            SysUserPojo account = accountService.getUserOrSubAccount(username);
            return getPrincipal(account.getUsername());
        } catch (BaseException e) {
            if (ResultCode.USER_NOT_EXIST.getCode().equals(e.getCode())) {
                return null;
            }
            throw e;
        }
    }
    
    /**
     * 只允许查询数据，不支持修改，删除, 上传数据
     *
     * @param request     Request we are processing
     * @param response    Response we are creating
     * @param constraints Security constraint we are enforcing
     * @param context     The Context to which client of this class is attached.
     * @return
     */
    @Override
    public boolean hasResourcePermission(Request request, Response response, SecurityConstraint[] constraints, Context context) {
        WebdavMethod method = WebdavMethod.getMethod(request.getMethod());
        return LIST_METHODS.contains(method);
    }
}
