# keycloak-selector


## JSON config

```json
{
  "selectionGroupClass": "auth-method-group-tiles",
  "selections": [
    {
      "tileClass": "auth-method-tile",
      "iconClass": "fas fa-envelope icon",
      "value": "email_otp",
      "displayTitle": "Email OTP",
      "disabled": false
    },
    {
      "tileClass": "auth-method-tile auth-method-tile-disabled",
      "iconClass": "fas fa-sms icon",
      "value": "sms_otp",
      "displayTitle": "SMS OTP",
      "disabled": true
    },
    {
      "tileClass": "auth-method-tile auth-method-tile-disabled",
      "iconClass": "fas fa-key icon",
      "value": "webauthn",
      "displayTitle": "Passkey",
      "disabled": true
    }
  ]
}
```