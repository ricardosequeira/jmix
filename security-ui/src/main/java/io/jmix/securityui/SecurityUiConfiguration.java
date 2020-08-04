/*
 * Copyright 2020 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.securityui;

import io.jmix.core.CoreConfiguration;
import io.jmix.core.annotation.JmixModule;
import io.jmix.core.impl.scanning.AnnotationScanMetadataReaderFactory;
import io.jmix.security.SecurityConfiguration;
import io.jmix.ui.UiConfiguration;
import io.jmix.ui.sys.UiControllersConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.util.Collections;

@Configuration
@JmixModule(dependsOn = {CoreConfiguration.class, SecurityConfiguration.class, UiConfiguration.class})
@PropertySource(name = "io.jmix.securityui", value = "classpath:/io/jmix/securityui/module.properties")
@Import(StandardSecurityUiConfiguration.class)
public class SecurityUiConfiguration {

    @Bean("sec_SecurityUiControllers")
    public UiControllersConfiguration screens(ApplicationContext applicationContext,
                                              AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        UiControllersConfiguration uiControllers
                = new UiControllersConfiguration(applicationContext, metadataReaderFactory);
        uiControllers.setBasePackages(Collections.singletonList("io.jmix.securityui"));
        return uiControllers;
    }
}

