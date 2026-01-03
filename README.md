# Keycloak-Selector

This is a simple Keycloak selector to build config-driven flow selection pages.
It is based on the Auth Note and conditional flows.

method-selection-authenticator is used for showing the tiles with options.

auth-note-equals-condition is used to execute the selected flow.


Tested with Keycloak 26.0.7.

## Demo

Start docker compose
```
docker compose up
```

Demo setup can be loaded from the config folder.
```
python3 setup.py
```

https://github.com/user-attachments/assets/494f458d-4c5d-4813-b648-2edcca82e285



## Example JSON config

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
