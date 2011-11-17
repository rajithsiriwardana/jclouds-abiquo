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

package org.jclouds.abiquo.features;

import java.util.concurrent.TimeUnit;

import org.jclouds.abiquo.domain.config.options.LicenseOptions;
import org.jclouds.abiquo.reference.annotations.EnterpriseEdition;
import org.jclouds.concurrent.Timeout;

import com.abiquo.server.core.config.LicenseDto;
import com.abiquo.server.core.config.LicensesDto;
import com.abiquo.server.core.enterprise.PrivilegeDto;
import com.abiquo.server.core.enterprise.PrivilegesDto;

/**
 * Provides synchronous access to Abiquo Admin API.
 * 
 * @see http://community.abiquo.com/display/ABI18/API+Reference
 * @see ConfigAsyncClient
 * @author Ignasi Barrera
 * @author Francesc Montserrat
 */
@Timeout(duration = 30, timeUnit = TimeUnit.SECONDS)
public interface ConfigClient
{
    /*********************** License ***********************/

    /**
     * List all licenses.
     * 
     * @return The list of licenses.
     */
    @EnterpriseEdition
    LicensesDto listLicenses();

    /**
     * List all active/inactive licenses.
     * 
     * @param options Optional query params.
     * @return The list of licenses.
     */
    @EnterpriseEdition
    LicensesDto listLicenses(LicenseOptions options);

    /**
     * Add a new license.
     * 
     * @param license The license to add.
     * @return The added license.
     */
    @EnterpriseEdition
    LicenseDto addLicense(LicenseDto license);

    /**
     * Removes an existing license.
     * 
     * @param license The license to delete.
     */
    @EnterpriseEdition
    void removeLicense(LicenseDto license);

    /*********************** Privilege ***********************/

    /**
     * List all privileges in the system.
     * 
     * @return The list of privileges.
     */
    PrivilegesDto listPrivileges();

    /**
     * Get the given privilege.
     * 
     * @param privilegeId The id of the privilege.
     * @return The privilege or <code>null</code> if it does not exist.
     */
    PrivilegeDto getPrivilege(Integer privilegeId);

}