import requests

url = "http://localhost:8080/api/housewarming-party/user"
headers = {"Content-Type": "application/json"}
data = {
    "name": "Marcos",
    "email": "marcos@gmail.com",
    "password": "password",
    "pixKey": "1999932504",
}

try:
    response = requests.post(url, headers=headers, json=data)
    if response.status_code == 200:
        print("Usuário criado com sucesso!")
        print("Resposta:", response.json())
    else:
        print(f"Erro ao criar usuário: {response.status_code}")
        print("Detalhes:", response.text)
except requests.exceptions.RequestException as e:
    print("Erro ao conectar ao servidor:", e)
