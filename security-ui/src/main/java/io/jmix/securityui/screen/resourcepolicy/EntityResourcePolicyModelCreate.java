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

package io.jmix.securityui.screen.resourcepolicy;

import com.google.common.base.Strings;
import io.jmix.core.Metadata;
import io.jmix.security.model.ResourcePolicyType;
import io.jmix.securityui.model.DefaultResourcePolicyGroupResolver;
import io.jmix.securityui.model.ResourcePolicyModel;
import io.jmix.ui.component.*;
import io.jmix.ui.screen.MessageBundle;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UiController("sec_EntityResourcePolicyModel.create")
@UiDescriptor("entity-resource-policy-model-create.xml")
public class EntityResourcePolicyModelCreate extends MultipleResourcePolicyModelCreateScreen {

    @Autowired
    private ComboBox<String> entityField;

    @Autowired
    private CheckBox createCheckBox;

    @Autowired
    private CheckBox readCheckBox;

    @Autowired
    private CheckBox updateCheckBox;

    @Autowired
    private CheckBox deleteCheckBox;

    @Autowired
    private TextField<String> policyGroupField;

    @Autowired
    private ResourcePolicyEditorUtils resourcePolicyEditorUtils;

    @Autowired
    private DefaultResourcePolicyGroupResolver resourcePolicyGroupResolver;

    @Autowired
    private Metadata metadata;

    @Autowired
    private MessageBundle messageBundle;

    @Subscribe
    public void onInit(InitEvent event) {
        entityField.setOptionsMap(resourcePolicyEditorUtils.getEntityOptionsMap());
    }

    @Subscribe("entityField")
    public void onEntityFieldValueChange(HasValue.ValueChangeEvent<String> event) {
        String entityName = event.getValue();
        policyGroupField.setValue(resourcePolicyGroupResolver.resolvePolicyGroup(ResourcePolicyType.ENTITY, entityName));
    }

    private Set<String> getPolicyActions() {
        Set<String> actions = new HashSet<>();
        if (createCheckBox.isChecked())
            actions.add("create");
        if (readCheckBox.isChecked())
            actions.add("read");
        if (updateCheckBox.isChecked())
            actions.add("update");
        if (deleteCheckBox.isChecked())
            actions.add("delete");
        return actions;
    }

    @Subscribe("allActionsCheckBox")
    public void onAllActionsCheckBoxValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        Boolean allIsChecked = Boolean.TRUE.equals(event.getValue());
        createCheckBox.setEnabled(!allIsChecked);
        readCheckBox.setEnabled(!allIsChecked);
        updateCheckBox.setEnabled(!allIsChecked);
        deleteCheckBox.setEnabled(!allIsChecked);

        createCheckBox.setValue(allIsChecked);
        readCheckBox.setValue(allIsChecked);
        updateCheckBox.setValue(allIsChecked);
        deleteCheckBox.setValue(allIsChecked);
    }

    @Override
    protected ValidationErrors validateScreen() {
        ValidationErrors validationErrors = new ValidationErrors();
        if (Strings.isNullOrEmpty(entityField.getValue())) {
            validationErrors.add(entityField, messageBundle.getMessage("EntityResourcePolicyModelCreate.selectEntity"));
        }
        if (getPolicyActions().isEmpty()) {
            validationErrors.add(entityField, messageBundle.getMessage("EntityResourcePolicyModelCreate.selectActions"));
        }
        return validationErrors;
    }

    @Override
    public List<ResourcePolicyModel> getResourcePolicies() {
        List<ResourcePolicyModel> policies = new ArrayList<>();
        String entityName = entityField.getValue();
        for (String action : getPolicyActions()) {
            ResourcePolicyModel policy = metadata.create(ResourcePolicyModel.class);
            policy.setType(ResourcePolicyType.ENTITY);
            policy.setResource(entityName);
            policy.setPolicyGroup(policyGroupField.getValue());
            policy.setAction(action);
            policies.add(policy);
        }
        return policies;
    }
}