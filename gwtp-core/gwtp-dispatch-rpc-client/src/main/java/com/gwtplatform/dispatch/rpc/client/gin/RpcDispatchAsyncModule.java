/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.dispatch.rpc.client.gin;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.inject.Provides;
import com.gwtplatform.dispatch.client.DefaultSecurityCookieAccessor;
import com.gwtplatform.dispatch.client.gin.AbstractDispatchAsyncModule;
import com.gwtplatform.dispatch.rpc.client.DefaultRpcDispatchCallFactory;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchAsync;
import com.gwtplatform.dispatch.rpc.client.RpcDispatchCallFactory;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.dispatch.rpc.shared.DispatchService;
import com.gwtplatform.dispatch.rpc.shared.DispatchServiceAsync;

/**
 * An implementation of {@link AbstractDispatchAsyncModule} that uses Remote Procedure Calls (RPC).
 * <p/>
 * This gin module provides provides access to the {@link DispatchAsync} singleton, which is used to make calls to the
 * server over RPC.
 * <p/>
 * If you want to prevent XSRF attack (you use secured {@link com.gwtplatform.dispatch.rpc.shared.Action}s) the empty
 * {@link DefaultSecurityCookieAccessor} could leave your application vulnerable to XSRF attacks.
 *
 * @see <a href="http://www.gwtproject.org/articles/security_for_gwt_applications.html#xsrf">This document</a>.
 */
public class RpcDispatchAsyncModule extends AbstractDispatchAsyncModule {
    /**
     * A {@link RpcDispatchAsyncModule} builder.
     */
    public static class Builder extends AbstractDispatchAsyncModule.Builder {
        @Override
        public RpcDispatchAsyncModule build() {
            return new RpcDispatchAsyncModule(this);
        }
    }

    public RpcDispatchAsyncModule() {
        this(new Builder());
    }

    protected RpcDispatchAsyncModule(Builder builder) {
        super(builder);
    }

    @Override
    protected void configureDispatch() {
        bind(RpcDispatchCallFactory.class).to(DefaultRpcDispatchCallFactory.class).in(Singleton.class);

        bind(DispatchAsync.class).to(RpcDispatchAsync.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    DispatchServiceAsync provideDispatchServiceAsync() {
        return GWT.create(DispatchService.class);
    }
}
