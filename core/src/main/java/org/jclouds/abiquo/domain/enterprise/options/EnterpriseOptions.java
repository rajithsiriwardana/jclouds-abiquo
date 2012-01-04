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

package org.jclouds.abiquo.domain.enterprise.options;

import org.jclouds.abiquo.domain.options.search.FilterOptions;

/**
 * Available options to query enterprises.
 * 
 * @author Francesc Montserrat
 */
public class EnterpriseOptions extends FilterOptions
{
    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        EnterpriseOptions options = new EnterpriseOptions();
        options.map.putAll(map);
        return options;
    }

    @Override
    public String toString()
    {
        return this.map.toString();
    }

    public static class Builder extends FilterOptionsBuilder<Builder>
    {
        private String idPricingTemplate;

        private Boolean included;

        private String filter;

        private Integer page;

        private Integer results;

        private Boolean network;

        public Builder pricingTemplate(final String idPricingTemplate)
        {
            this.idPricingTemplate = idPricingTemplate;
            return this;
        }

        public Builder included(final boolean included)
        {
            this.included = included;
            return this;
        }

        public Builder filter(final String filter)
        {
            this.filter = filter;
            return this;
        }

        public Builder network(final boolean network)
        {
            this.network = network;
            return this;
        }

        public Builder page(final int page)
        {
            this.page = page;
            return this;
        }

        public Builder results(final int results)
        {
            this.results = results;
            return this;
        }

        @Override
        public EnterpriseOptions build()
        {
            EnterpriseOptions options = new EnterpriseOptions();

            if (idPricingTemplate != null)
            {
                options.map.put("idPricingTemplate", String.valueOf(idPricingTemplate));
            }

            if (included != null)
            {
                options.map.put("included", String.valueOf(included));
            }

            if (filter != null)
            {
                options.map.put("filter", String.valueOf(filter));
            }

            if (page != null)
            {
                options.map.put("page", String.valueOf(page));
            }

            if (results != null)
            {
                options.map.put("numResults", String.valueOf(results));
            }

            if (network != null)
            {
                options.map.put("network", String.valueOf(network));
            }

            // Add FilterOptions options
            options.map.putAll(super.build().getOptions());

            return options;
        }
    }
}
