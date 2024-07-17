import requests
import base64
import json

name = "DrRaiker"

response = requests.get(f"https://api.mojang.com/users/profiles/minecraft/{name}")
if response.status_code == 200:
    id = response.json()['id']

response = requests.get(f"https://sessionserver.mojang.com/session/minecraft/profile/{id}")
if response.status_code == 200:
    value = response.json()['properties'][0]['value']

    based = base64.b64decode(value.encode("ascii")).decode("ascii")

    print(json.loads(based)['textures']['SKIN']['url'])
