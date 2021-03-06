/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jclouds.abiquo.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.jclouds.abiquo.domain.options.QueryOptions;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.utils.ModifyRequest;
import org.jclouds.rest.Binder;

/**
 * Appends the parameter value to the end of the request URI.
 * 
 * @author Francesc Montserrat
 * @author Ignasi Barrera
 */
// This class cannot be singleton. uriBuilder is not thread-save!
public class AppendOptionsToPath implements Binder
{
    /** The configured URI builder. */
    private UriBuilder uriBuilder;

    @Inject
    public AppendOptionsToPath(final UriBuilder uriBuilder)
    {
        this.uriBuilder = uriBuilder;
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(final R request, final Object input)
    {
        checkArgument(checkNotNull(input, "input") instanceof QueryOptions,
            "this binder is only valid for QueryOptions objects");
        QueryOptions options = (QueryOptions) input;

        return ModifyRequest.addQueryParams(request, options.getOptions(), uriBuilder);
    }
}
