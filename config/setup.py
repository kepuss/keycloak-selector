#!/usr/bin/env python3
import json
import os
import requests

# --- Config ---
KC_HOST = os.getenv("KC_HOST", "http://localhost:8080").rstrip("/")
KC_REALM = os.getenv("KC_REALM", "master")
KC_USER = os.getenv("KC_USER", "admin")
KC_PASS = os.getenv("KC_PASS", "admin")
KC_VERIFY_SSL = False  # True if using valid HTTPS

FLOW_FILE = "flow.json"
CONFIGS_FILE = "authenticator-config.json"
EXECUTIONS_FILE = "authentication-executions.json"
CLIENT_FILE = "client.json"


def get_token():
    url = f"{KC_HOST}/realms/{KC_REALM}/protocol/openid-connect/token"
    data = {
        "client_id": "admin-cli",
        "username": KC_USER,
        "password": KC_PASS,
        "grant_type": "password"
    }
    r = requests.post(url, data=data, verify=KC_VERIFY_SSL)
    r.raise_for_status()
    return r.json()["access_token"]


def load_json(filename):
    with open(filename, "r") as f:
        return json.load(f)


def post(path, token, body):
    url = f"{KC_HOST}{path}"
    r = requests.post(url, json=body, headers={"Authorization": f"Bearer {token}"}, verify=KC_VERIFY_SSL)
    r.raise_for_status()
    return r

def put(path, token, body):
    url = f"{KC_HOST}{path}"
    r = requests.put(url, json=body, headers={"Authorization": f"Bearer {token}"}, verify=KC_VERIFY_SSL)
    r.raise_for_status()
    return r

def main():
    token = get_token()
    headers = {"Authorization": f"Bearer {token}"}

    # --- Flow ---
    flow = load_json(FLOW_FILE)
    flow_alias = flow["alias"]
    print(f"[+] Creating flow '{flow_alias}'")
    flow_resp = post(f"/admin/realms/{KC_REALM}/authentication/flows", token, flow)
    flow_id = flow["id"]

    # --- Authenticator Configs ---
    # configs_wrapper = load_json(CONFIGS_FILE)
    # configs_list = configs_wrapper.get("configs") if "configs" in configs_wrapper else configs_wrapper
    # config_alias_to_id = {}
    # for c in configs_list:
    #     print(f"[+] Creating authenticator config '{c['alias']}'")
    #     r = post(f"/admin/realms/{KC_REALM}/authentication/config", token, c)
    #     loc = r.headers.get("Location", "")
    #     config_id = loc.rstrip("/").split("/")[-1]
    #     config_alias_to_id[c['alias']] = config_id

    # --- Executions ---
    executions = load_json(EXECUTIONS_FILE)
    for e in executions:
        provider = e["provider"]
        print(f"[+] Creating execution for provider '{provider}'")
        # 1) POST execution
        r = post(f"/admin/realms/{KC_REALM}/authentication/flows/{flow_alias}/executions/execution",
                 token, {"provider": provider})
        # determine execution ID
        execs = requests.get(f"{KC_HOST}/admin/realms/{KC_REALM}/authentication/flows/{flow_alias}/executions",
                             headers=headers, verify=KC_VERIFY_SSL).json()
        exec_id = next(x["id"] for x in execs if x["providerId"] == provider)
        print(f"  Execution ID: {exec_id}")

        # 2) POST update requirement/priority
        update_body = {
            "id": exec_id,
            "requirement": e.get("requirement", "REQUIRED"),
            "priority": e.get("priority", 0)

        }
        put(f"/admin/realms/{KC_REALM}/authentication/flows/{flow_alias}/executions", token, update_body)

        # 3) POST attach authenticator config if provided
        configs_wrapper = load_json(CONFIGS_FILE)
        configs_list = configs_wrapper.get("configs") if "configs" in configs_wrapper else configs_wrapper
        config_alias_to_id = {}
        for c in configs_list:
            print(f"[+] Creating authenticator config '{c['alias']}'")
            r = post(f"/admin/realms/{KC_REALM}/authentication/executions/{exec_id}/config", token, c)

    # --- Client ---
    client = load_json(CLIENT_FILE)
    print(f"[+] Creating client '{client['clientId']}'")
    client["authenticationFlowBindingOverrides"]["browser"] = flow_id
    post(f"/admin/realms/{KC_REALM}/clients", token, client)

    print("[✓] All done.")


if __name__ == "__main__":
    main()
