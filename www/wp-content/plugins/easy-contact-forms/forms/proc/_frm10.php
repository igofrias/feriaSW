<div class='ufo-customform-fieldform'><div id='ufo-customform-settings-showlabel-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-showlabel-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-showlabel");'>&nbsp;</span><input type='hidden' id='ShowLabelHint' value='<?php echo EasyContactFormsT::get('CF_Hint_ShowLabel');?>' class='ufo-id-link'><div><input type='checkbox' id='ShowLabel' value='<?php echo isset($ShowLabel) && $ShowLabel == 'on' ? 'on' : 'off';?>' <?php echo isset($ShowLabel) && $ShowLabel == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='ShowLabel' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_ShowLabel');?><span id='ShowLabelHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-showlabel' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='Label'><?php echo EasyContactFormsT::get('CF_Label');?><span id='LabelHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='LabelHint' value='<?php echo EasyContactFormsT::get('CF_Hint_Label');?>' class='ufo-id-link'><div style='clear:left'></div><div style='position:relative;padding-right:100px'><input type='string' id='Label' value='<?php echo $Label;?>' class='ufo-formvalue textinput ufo-text' style='width:100%'><select id='LabelPosition' class='ufo-formvalue inputselect ufo-select' style='right:0;position:absolute;top:0;width:99px'><option value='top-align-left' <?php echo $LabelPosition == 'top-align-left' ? ' selected' : '';?>>top-align-left</option><option value='top-align-right' <?php echo $LabelPosition == 'top-align-right' ? ' selected' : '';?>>top-align-right</option><option value='left-align-left' <?php echo $LabelPosition == 'left-align-left' ? ' selected' : '';?>>left-align-left</option><option value='left-align-right' <?php echo $LabelPosition == 'left-align-right' ? ' selected' : '';?>>left-align-right</option><option value='right-align-left' <?php echo $LabelPosition == 'right-align-left' ? ' selected' : '';?>>right-align-left</option><option value='right-align-right' <?php echo $LabelPosition == 'right-align-right' ? ' selected' : '';?>>right-align-right</option><option value='bottom-align-left' <?php echo $LabelPosition == 'bottom-align-left' ? ' selected' : '';?>>bottom-align-left</option><option value='bottom-align-right' <?php echo $LabelPosition == 'bottom-align-right' ? ' selected' : '';?>>bottom-align-right</option></select></div><input type='hidden' value='var c = {};c.id = "Label";c.events = {};c.events.blur = [];c.required={};c.required.msg=AppMan.resources.ThisFieldIsRequired;c.events.blur.push("required");c.invClass = "ufo-fields-invalid-field";AppMan.addValidation(c);' class='ufo-eval'><div id='Label-invalid' class='ufo-fields-invalid-value ufo-id-link' style='display:none'></div></div><div id='ufo-customform-settings-showlabel-advanced-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-showlabel-advanced-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-showlabel-advanced");'>&nbsp;</span><span class='ufo-customform-fieldform-fieldset-legend-label'><?php echo EasyContactFormsT::get('CF_Advanced');?></span></div><div id='ufo-customform-settings-showlabel-advanced' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='LabelCSSClass'><?php echo EasyContactFormsT::get('CF_LabelCSSClass');?><span id='LabelCSSClassHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='LabelCSSClassHint' value='<?php echo EasyContactFormsT::get('CF_Hint_LabelCSSClass');?>' class='ufo-id-link'><input type='string' id='LabelCSSClass' value='<?php echo isset($LabelCSSClass) ? $LabelCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='LabelCSSStyle'><?php echo EasyContactFormsT::get('CF_LabelCSSStyle');?><span id='LabelCSSStyleHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='LabelCSSStyleHint' value='<?php echo EasyContactFormsT::get('CF_Hint_LabelCSSStyle');?>' class='ufo-id-link'><div><textarea id='LabelCSSStyle' class='ufo-formvalue textinput ufo-textarea' style='width:95%'><?php echo $LabelCSSStyle;?></textarea></div></div></div></div></div></div><div id='ufo-customform-settings-showdescription-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-showdescription-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-showdescription");'>&nbsp;</span><input type='hidden' id='ShowDescriptionHint' value='<?php echo EasyContactFormsT::get('CF_Hint_ShowDescription');?>' class='ufo-id-link'><div><input type='checkbox' id='ShowDescription' value='<?php echo isset($ShowDescription) && $ShowDescription == 'on' ? 'on' : 'off';?>' <?php echo isset($ShowDescription) && $ShowDescription == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='ShowDescription' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_ShowDescription');?><span id='ShowDescriptionHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-showdescription' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='Description'><?php echo EasyContactFormsT::get('CF_Description');?><span id='DescriptionHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='DescriptionHint' value='<?php echo EasyContactFormsT::get('CF_Hint_Description');?>' class='ufo-id-link'><div><textarea id='Description' class='ufo-formvalue textinput ufo-textarea' style='width:95%'><?php echo $Description;?></textarea></div></div><div><label for='DescriptionPosition'><?php echo EasyContactFormsT::get('CF_DescriptionPosition');?></label><select id='DescriptionPosition' class='ufo-formvalue inputselect ufo-select' style='width:100%'><option value='top' <?php echo $DescriptionPosition == 'top' ? ' selected' : '';?>>top</option><option value='right' <?php echo $DescriptionPosition == 'right' ? ' selected' : '';?>>right</option><option value='bottom' <?php echo $DescriptionPosition == 'bottom' ? ' selected' : '';?>>bottom</option></select></div><div id='ufo-customform-settings-showdescription-advanced-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-showdescription-advanced-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-showdescription-advanced");'>&nbsp;</span><span class='ufo-customform-fieldform-fieldset-legend-label'><?php echo EasyContactFormsT::get('CF_Advanced');?></span></div><div id='ufo-customform-settings-showdescription-advanced' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='DescriptionCSSClass'><?php echo EasyContactFormsT::get('CF_DescriptionCSSClass');?><span id='DescriptionCSSClassHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='DescriptionCSSClassHint' value='<?php echo EasyContactFormsT::get('CF_Hint_DescriptionCSSClass');?>' class='ufo-id-link'><input type='string' id='DescriptionCSSClass' value='<?php echo isset($DescriptionCSSClass) ? $DescriptionCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='DescriptionCSSStyle'><?php echo EasyContactFormsT::get('CF_DescriptionCSSStyle');?><span id='DescriptionCSSStyleHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='DescriptionCSSStyleHint' value='<?php echo EasyContactFormsT::get('CF_Hint_DescriptionCSSStyle');?>' class='ufo-id-link'><div><textarea id='DescriptionCSSStyle' class='ufo-formvalue textinput ufo-textarea' style='width:95%'><?php echo $DescriptionCSSStyle;?></textarea></div></div></div></div></div></div><div id='ufo-customform-settings-setdefaultvalue-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-setdefaultvalue-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-setdefaultvalue");'>&nbsp;</span><input type='hidden' id='SetDefaultValueHint' value='<?php echo EasyContactFormsT::get('CF_Hint_SetDefaultValue');?>' class='ufo-id-link'><div><input type='checkbox' id='SetDefaultValue' value='<?php echo isset($SetDefaultValue) && $SetDefaultValue == 'on' ? 'on' : 'off';?>' <?php echo isset($SetDefaultValue) && $SetDefaultValue == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='SetDefaultValue' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_SetDefaultValue');?><span id='SetDefaultValueHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-setdefaultvalue' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='DefaultValue'><?php echo EasyContactFormsT::get('CF_DefaultValue');?></label><input type='string' id='DefaultValue' value='<?php echo isset($DefaultValue) ? $DefaultValue : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='IsBlankValue'><?php echo EasyContactFormsT::get('CF_IsBlankValue');?><span id='IsBlankValueHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='IsBlankValueHint' value='<?php echo EasyContactFormsT::get('CF_Hint_IsBlankValue');?>' class='ufo-id-link'><input type='checkbox' id='IsBlankValue' value='<?php echo isset($IsBlankValue) && $IsBlankValue == 'on' ? 'on' : 'off';?>' <?php echo isset($IsBlankValue) && $IsBlankValue == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'></div><div id='ufo-customform-settings-setdefaultvalue-advanced-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-setdefaultvalue-advanced-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-setdefaultvalue-advanced");'>&nbsp;</span><span class='ufo-customform-fieldform-fieldset-legend-label'><?php echo EasyContactFormsT::get('CF_Advanced');?></span></div><div id='ufo-customform-settings-setdefaultvalue-advanced' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='DefaultValueCSSClass'><?php echo EasyContactFormsT::get('CF_DefaultValueCSSClass');?><span id='DefaultValueCSSClassHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='DefaultValueCSSClassHint' value='<?php echo EasyContactFormsT::get('CF_Hint_DefaultValueCSSClass');?>' class='ufo-id-link'><input type='string' id='DefaultValueCSSClass' value='<?php echo isset($DefaultValueCSSClass) ? $DefaultValueCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div></div></div></div></div><div id='ufo-customform-settings-required-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-required-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-required");'>&nbsp;</span><input type='hidden' id='RequiredHint' value='<?php echo EasyContactFormsT::get('CF_Hint_Required');?>' class='ufo-id-link'><div><input type='checkbox' id='Required' value='<?php echo isset($Required) && $Required == 'on' ? 'on' : 'off';?>' <?php echo isset($Required) && $Required == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='Required' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_Required');?><span id='RequiredHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-required' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='RequiredMessage'><?php echo EasyContactFormsT::get('CF_RequiredMessage');?><span id='RequiredMessageHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='RequiredMessageHint' value='<?php echo EasyContactFormsT::get('CF_Hint_RequiredMessage');?>' class='ufo-id-link'><div style='position:relative;padding-right:100px'><input type='string' id='RequiredMessage' value='<?php echo $RequiredMessage;?>' class='ufo-formvalue textinput ufo-text' style='width:100%'><select id='RequiredMessagePosition' class='ufo-formvalue inputselect ufo-select' style='right:0;position:absolute;top:0;width:99px'><option value='top' <?php echo $RequiredMessagePosition == 'top' ? ' selected' : '';?>>top</option><option value='right' <?php echo $RequiredMessagePosition == 'right' ? ' selected' : '';?>>right</option><option value='bottom' <?php echo $RequiredMessagePosition == 'bottom' ? ' selected' : '';?>>bottom</option></select></div><div style='clear:left'></div></div><div id='ufo-customform-settings-required-setrequiredsuffix-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-required-setrequiredsuffix-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-required-setrequiredsuffix");'>&nbsp;</span><input type='hidden' id='SetRequiredSuffixHint' value='<?php echo EasyContactFormsT::get('CF_Hint_SetRequiredSuffix');?>' class='ufo-id-link'><div><input type='checkbox' id='SetRequiredSuffix' value='<?php echo isset($SetRequiredSuffix) && $SetRequiredSuffix == 'on' ? 'on' : 'off';?>' <?php echo isset($SetRequiredSuffix) && $SetRequiredSuffix == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='SetRequiredSuffix' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_SetRequiredSuffix');?><span id='SetRequiredSuffixHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-required-setrequiredsuffix' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='RequiredSuffix'><?php echo EasyContactFormsT::get('CF_RequiredSuffix');?></label><input type='string' id='RequiredSuffix' value='<?php echo isset($RequiredSuffix) ? $RequiredSuffix : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div id='ufo-customform-settings-required-setrequiredsuffix-advanced-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-required-setrequiredsuffix-advanced-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-required-setrequiredsuffix-advanced");'>&nbsp;</span><span class='ufo-customform-fieldform-fieldset-legend-label'><?php echo EasyContactFormsT::get('CF_Advanced');?></span></div><div id='ufo-customform-settings-required-setrequiredsuffix-advanced' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='RequiredSuffixCSSClass'><?php echo EasyContactFormsT::get('CF_RequiredSuffixCSSClass');?></label><input type='string' id='RequiredSuffixCSSClass' value='<?php echo isset($RequiredSuffixCSSClass) ? $RequiredSuffixCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='RequiredSuffixCSSStyle'><?php echo EasyContactFormsT::get('CF_RequiredSuffixCSSStyle');?></label><div><textarea id='RequiredSuffixCSSStyle' class='ufo-formvalue textinput ufo-textarea' style='width:95%'><?php echo $RequiredSuffixCSSStyle;?></textarea></div></div></div></div></div></div><div id='ufo-customform-settings-required-advanced-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-required-advanced-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-required-advanced");'>&nbsp;</span><span class='ufo-customform-fieldform-fieldset-legend-label'><?php echo EasyContactFormsT::get('CF_Advanced');?></span></div><div id='ufo-customform-settings-required-advanced' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='AbsolutePosition'><?php echo EasyContactFormsT::get('CF_AbsolutePosition');?><span id='AbsolutePositionHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='AbsolutePositionHint' value='<?php echo EasyContactFormsT::get('CF_Hint_AbsolutePosition');?>' class='ufo-id-link'><input type='checkbox' id='AbsolutePosition' value='<?php echo isset($AbsolutePosition) && $AbsolutePosition == 'on' ? 'on' : 'off';?>' <?php echo isset($AbsolutePosition) && $AbsolutePosition == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'></div><div><label for='InvalidCSSClass'><?php echo EasyContactFormsT::get('CF_InvalidCSSClass');?><span id='InvalidCSSClassHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='InvalidCSSClassHint' value='<?php echo EasyContactFormsT::get('CF_Hint_InvalidCSSClass');?>' class='ufo-id-link'><input type='string' id='InvalidCSSClass' value='<?php echo isset($InvalidCSSClass) ? $InvalidCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='RequiredMessageCSSClass'><?php echo EasyContactFormsT::get('CF_RequiredMessageCSSClass');?></label><input type='string' id='RequiredMessageCSSClass' value='<?php echo isset($RequiredMessageCSSClass) ? $RequiredMessageCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='RequiredMessageCSSStyle'><?php echo EasyContactFormsT::get('CF_RequiredMessageCSSStyle');?></label><div><textarea id='RequiredMessageCSSStyle' class='ufo-formvalue textinput ufo-textarea' style='width:95%'><?php echo $RequiredMessageCSSStyle;?></textarea></div></div></div></div></div></div><div id='ufo-customform-settings-validate-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-validate-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-validate");'>&nbsp;</span><input type='hidden' id='ValidateHint' value='<?php echo EasyContactFormsT::get('CF_Hint_Validate');?>' class='ufo-id-link'><div><input type='checkbox' id='Validate' value='<?php echo isset($Validate) && $Validate == 'on' ? 'on' : 'off';?>' <?php echo isset($Validate) && $Validate == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='Validate' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_Validate');?><span id='ValidateHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-validate' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='MinLength'><?php echo EasyContactFormsT::get('CF_MinLength');?></label><input type='string' id='MinLength' value='<?php echo isset($MinLength) ? $MinLength : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'><input type='hidden' value='var c = {};c.id = "MinLength";c.events = {};c.events.blur = [];c.integer={};c.integer.msg=AppMan.resources.ThisIsAnIntegerField;c.events.blur.push("integer");c.invClass = "ufo-fields-invalid-field";AppMan.addValidation(c);' class='ufo-eval'><div id='MinLength-invalid' class='ufo-fields-invalid-value ufo-id-link' style='display:none'></div></div><div><label for='MaxLength'><?php echo EasyContactFormsT::get('CF_MaxLength');?></label><input type='string' id='MaxLength' value='<?php echo isset($MaxLength) ? $MaxLength : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'><input type='hidden' value='var c = {};c.id = "MaxLength";c.events = {};c.events.blur = [];c.integer={};c.integer.msg=AppMan.resources.ThisIsAnIntegerField;c.events.blur.push("integer");c.invClass = "ufo-fields-invalid-field";AppMan.addValidation(c);' class='ufo-eval'><div id='MaxLength-invalid' class='ufo-fields-invalid-value ufo-id-link' style='display:none'></div></div><div id='ufo-customform-settings-validate-setvalidmessage-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-validate-setvalidmessage-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-validate-setvalidmessage");'>&nbsp;</span><input type='hidden' id='SetValidMessageHint' value='<?php echo EasyContactFormsT::get('CF_Hint_SetValidMessage');?>' class='ufo-id-link'><div><input type='checkbox' id='SetValidMessage' value='<?php echo isset($SetValidMessage) && $SetValidMessage == 'on' ? 'on' : 'off';?>' <?php echo isset($SetValidMessage) && $SetValidMessage == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='SetValidMessage' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_SetValidMessage');?><span id='SetValidMessageHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-validate-setvalidmessage' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='ValidMessageAbsolutePosition'><?php echo EasyContactFormsT::get('CF_ValidMessageAbsolutePosition');?><span id='ValidMessageAbsolutePositionHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='ValidMessageAbsolutePositionHint' value='<?php echo EasyContactFormsT::get('CF_Hint_ValidMessageAbsolutePosition');?>' class='ufo-id-link'><input type='checkbox' id='ValidMessageAbsolutePosition' value='<?php echo isset($ValidMessageAbsolutePosition) && $ValidMessageAbsolutePosition == 'on' ? 'on' : 'off';?>' <?php echo isset($ValidMessageAbsolutePosition) && $ValidMessageAbsolutePosition == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'></div><div id='ufo-customform-settings-validate-setvalidmessage-advanced-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-validate-setvalidmessage-advanced-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-validate-setvalidmessage-advanced");'>&nbsp;</span><span class='ufo-customform-fieldform-fieldset-legend-label'><?php echo EasyContactFormsT::get('CF_Advanced');?></span></div><div id='ufo-customform-settings-validate-setvalidmessage-advanced' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='ValidMessage'><?php echo EasyContactFormsT::get('CF_ValidMessage');?><span id='ValidMessageHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='ValidMessageHint' value='<?php echo EasyContactFormsT::get('CF_Hint_ValidMessage');?>' class='ufo-id-link'><div style='position:relative;padding-right:100px'><input type='string' id='ValidMessage' value='<?php echo $ValidMessage;?>' class='ufo-formvalue textinput ufo-text' style='width:100%'><select id='ValidMessagePosition' class='ufo-formvalue inputselect ufo-select' style='right:0;position:absolute;top:0;width:99px'><option value='top' <?php echo $ValidMessagePosition == 'top' ? ' selected' : '';?>>top</option><option value='right' <?php echo $ValidMessagePosition == 'right' ? ' selected' : '';?>>right</option><option value='bottom' <?php echo $ValidMessagePosition == 'bottom' ? ' selected' : '';?>>bottom</option></select></div><div style='clear:left'></div></div><div><label for='ValidCSSClass'><?php echo EasyContactFormsT::get('CF_ValidCSSClass');?></label><input type='string' id='ValidCSSClass' value='<?php echo isset($ValidCSSClass) ? $ValidCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='ValidCSSStyle'><?php echo EasyContactFormsT::get('CF_ValidCSSStyle');?></label><div><textarea id='ValidCSSStyle' class='ufo-formvalue textinput ufo-textarea' style='width:95%'><?php echo $ValidCSSStyle;?></textarea></div></div></div></div></div></div></div></div><div id='ufo-customform-settings-setstyle-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-setstyle-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-setstyle");'>&nbsp;</span><input type='hidden' id='SetStyleHint' value='<?php echo EasyContactFormsT::get('CF_Hint_SetStyle');?>' class='ufo-id-link'><div><input type='checkbox' id='SetStyle' value='<?php echo isset($SetStyle) && $SetStyle == 'on' ? 'on' : 'off';?>' <?php echo isset($SetStyle) && $SetStyle == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='SetStyle' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_SetStyle');?><span id='SetStyleHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-setstyle' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='id' style='width:auto;margin-left:0;font-family:Arial;display:inline;margin:4px;font-size:12px;line-height:16px;padding:0'><?php echo EasyContactFormsT::get('CF_id');?></label><span id='id' class='ufo-formvalue ufo-customform-fieldform-fieldset-span'><?php echo isset($id) ? $id : '';?></span></div><div><label for='CSSClass'><?php echo EasyContactFormsT::get('CF_CSSClass');?><span id='CSSClassHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='CSSClassHint' value='<?php echo EasyContactFormsT::get('CF_Hint_CSSClass');?>' class='ufo-id-link'><input type='string' id='CSSClass' value='<?php echo isset($CSSClass) ? $CSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div><div><label for='CSSStyle'><?php echo EasyContactFormsT::get('CF_CSSStyle');?><span id='CSSStyleHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='CSSStyleHint' value='<?php echo EasyContactFormsT::get('CF_Hint_CSSStyle');?>' class='ufo-id-link'><div><textarea id='CSSStyle' class='ufo-formvalue textinput ufo-textarea' style='width:95%'><?php echo $CSSStyle;?></textarea></div></div><div id='ufo-customform-settings-setstyle-advanced-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-setstyle-advanced-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-setstyle-advanced");'>&nbsp;</span><span class='ufo-customform-fieldform-fieldset-legend-label'><?php echo EasyContactFormsT::get('CF_Advanced');?></span></div><div id='ufo-customform-settings-setstyle-advanced' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='RowCSSClass'><?php echo EasyContactFormsT::get('CF_RowCSSClass');?><span id='RowCSSClassHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label><input type='hidden' id='RowCSSClassHint' value='<?php echo EasyContactFormsT::get('CF_Hint_RowCSSClass');?>' class='ufo-id-link'><input type='string' id='RowCSSClass' value='<?php echo isset($RowCSSClass) ? $RowCSSClass : '';?>' class='ufo-formvalue textinput ufo-text' style='width:100%'></div></div></div></div></div><div id='ufo-customform-settings-setsize-fieldset' class='ufo-customform-fieldform-fieldset ufo-id-link'><div class='ufo-customform-fieldform-fieldset-legend'><span id='ufo-customform-settings-setsize-expander' class='ufo-customform-fieldform-fieldset-expander ufo-id-link' onclick='ufoCf.expanderClick(this.id, "ufo-customform-settings-setsize");'>&nbsp;</span><input type='hidden' id='SetSizeHint' value='<?php echo EasyContactFormsT::get('CF_Hint_SetSize');?>' class='ufo-id-link'><div><input type='checkbox' id='SetSize' value='<?php echo isset($SetSize) && $SetSize == 'on' ? 'on' : 'off';?>' <?php echo isset($SetSize) && $SetSize == 'on' ? 'checked' : '';?> class='ufo-formvalue ufo-customform-fieldform-fieldset-cb' onchange='this.value=(this.checked)?"on":"off";'><label for='SetSize' style='width:auto;font-family:Arial;clear:none;display:block;margin:0;float:none;font-size:12px;padding:0;line-height:16px'><?php echo EasyContactFormsT::get('CF_SetSize');?><span id='SetSizeHin' class='ufo-settingsform-label-hint ufo-label-hint ufo-id-link'>[<a>?</a>]</span></label></div></div><div id='ufo-customform-settings-setsize' class='ufo-customform-fieldform-fieldset-hidden ufo-id-link'><div><label for='Width'><?php echo EasyContactFormsT::get('CF_Width');?></label><div style='clear:left'></div><div style='position:relative;padding-right:100px'><input type='string' id='Width' value='<?php echo $Width;?>' class='ufo-formvalue textinput ufo-text' style='width:100%'><select id='WidthUnit' class='ufo-formvalue inputselect ufo-select' style='right:0;position:absolute;top:0;width:99px'><option value='px' <?php echo $WidthUnit == 'px' ? ' selected' : '';?>>px</option><option value='%' <?php echo $WidthUnit == '%' ? ' selected' : '';?>>%</option><option value='em' <?php echo $WidthUnit == 'em' ? ' selected' : '';?>>em</option></select></div><input type='hidden' value='var c = {};c.id = "Width";c.events = {};c.events.blur = [];c.integer={};c.integer.msg=AppMan.resources.ThisIsAnIntegerField;c.events.blur.push("integer");c.required={};c.required.msg=AppMan.resources.ThisFieldIsRequired;c.events.blur.push("required");c.invClass = "ufo-fields-invalid-field";AppMan.addValidation(c);' class='ufo-eval'><div id='Width-invalid' class='ufo-fields-invalid-value ufo-id-link' style='display:none'></div></div><div><label for='Height'><?php echo EasyContactFormsT::get('CF_Height');?></label><div style='clear:left'></div><div style='position:relative;padding-right:100px'><input type='string' id='Height' value='<?php echo $Height;?>' class='ufo-formvalue textinput ufo-text' style='width:100%'><select id='HeightUnit' class='ufo-formvalue inputselect ufo-select' style='right:0;position:absolute;top:0;width:99px'><option value='px' <?php echo $HeightUnit == 'px' ? ' selected' : '';?>>px</option><option value='%' <?php echo $HeightUnit == '%' ? ' selected' : '';?>>%</option><option value='em' <?php echo $HeightUnit == 'em' ? ' selected' : '';?>>em</option></select></div><input type='hidden' value='var c = {};c.id = "Height";c.events = {};c.events.blur = [];c.integer={};c.integer.msg=AppMan.resources.ThisIsAnIntegerField;c.events.blur.push("integer");c.required={};c.required.msg=AppMan.resources.ThisFieldIsRequired;c.events.blur.push("required");c.invClass = "ufo-fields-invalid-field";AppMan.addValidation(c);' class='ufo-eval'><div id='Height-invalid' class='ufo-fields-invalid-value ufo-id-link' style='display:none'></div></div></div></div></div>