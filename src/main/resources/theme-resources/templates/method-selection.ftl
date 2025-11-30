<#import "template.ftl" as layout>

<@layout.registrationLayout displayInfo=(realm.registrationAllowed && !registrationDisabled??); section>
    <#if section = "header">
        ${msg("loginAccountTitle")}
    <#elseif section = "form">
        <div class="kc-form-group">
            <h2>${msg("keycloakSelectorTitle")}</h2>
            <form id="kc-method-selection-form" action="${url.loginAction}" method="post">

                <#-- Display global errors if any -->
                <#if message?? && message != "">
                    <div class="kc-feedback-text">${message}</div>
                </#if>

                <div class="${config.selectionGroupClass!}">
                    <#list config.selections as sel>
                        <label class="${sel.tileClass!}">
                            <input type="radio" name="method" value="${sel.value!}"
                                <#if sel.disabled?? && sel.disabled>
                                    disabled="disabled"
                                </#if>
                            >
                            <div class="tile-content">
                                <i class="${sel.iconClass!}"></i><br/>
                                ${sel.displayTitle!}
                            </div>
                        </label>
                    </#list>
                </div>

            </form>
        </div>

        <script>
            // Automatically submit form when a tile is selected
            document.querySelectorAll('#kc-method-selection-form input[type="radio"]').forEach(function(radio) {
                radio.addEventListener('change', function() {
                    this.form.submit();
                });
            });
        </script>
    </#if>
</@layout.registrationLayout>
