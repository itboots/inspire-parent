/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.context;

import java.util.Date;
import java.util.Enumeration;

/**
 * 此类由具体应用中实现，其中泛型为可取得当前session关联信息的对象。
 *
 * @author greg
 */
public interface SessionManager<T> {

    /**
     * 获得当前session的用户标识
     *
     * @return
     */
    public String getIdentity(T object);

    /**
     * @return 当前登录用户的凭据
     */
    public String getCredential(T object);

    /**
     * 设置当前session的用户标识
     *
     * @return
     */
    public void setIdentity(T object, String identity);

    /**
     * @return 当前登录用户的凭据
     */
    public void setCredential(T object, String credential);

    /**
     * 移除当前session的用户标识
     *
     * @return
     */
    public void removeIdentity(T object);

    /**
     * @return 移除登录用户的凭据
     */
    public void removeCredential(T object);

    /**
     * 返回session创建的时间, 1970年1月1日到当前时间的描述
     *
     * @return session创建的时间
     */
    public Date getCreationTime(T object);

    /**
     * @return session标识
     */

    public String getSessionId(T object);

    /**
     * @return 最后一次访问时间
     */
    public Date getLastAccessedTime(T object);

    /**
     * 设置session的失效的秒数
     *
     * @param interval session失效的秒数
     */

    public void setMaxInactiveInterval(T object, int interval);

    /**
     * @return 目前设置的session失效的秒数。
     * @see #setMaxInactiveInterval
     */

    public int getMaxInactiveInterval(T object);

    /**
     * @param name 对象名称
     * @return 按名字返回session中存储的数据
     */

    public Object getAttribute(T object, String name);

    /**
     * @return 返回所有存储的数据的名称
     */
    public Enumeration<?> getAttributeNames(T object);

    /**
     * 通过一个名称绑定一个对象到当前session
     *
     * @param name  绑定对象的名称
     * @param value 要绑定的对象
     */

    public void setAttribute(T object, String name, Object value);

    /**
     * 从当前移除一个已绑定的对象
     */

    public void removeAttribute(T object, String name);

    /**
     * 使当前session失效，同时移除所有对象绑定
     */
    public void invalidate(T object);

}
