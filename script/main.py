import requests


def authenticate():
    url = "http://localhost:8080/api/housewarming-party/authenticate"
    data = {"username": "marcos@gmail.com", "password": "password"}

    response = requests.post(url, json=data)
    if response.status_code == 200:
        print("Autenticação bem-sucedida!")
        return response.headers["Authorization"]
    else:
        raise Exception("Erro ao autenticar")


def get_item_base_64(key: str):
    with open("./expanded_images_base64.txt", "r") as file:
        while file.readline().find(key) < 0:
            pass
        return file.readline()


data_map = {
    "electric-stove-expanded-background": {
        "name": "Fogão Elétrico",
        "price": 480.40,
        "description": "O fogão elétrico é uma alternativa moderna aos fogões a gás, oferecendo aquecimento uniforme e controle preciso de temperatura. Ideal para cozinhas contemporâneas, ele elimina a necessidade de botijões de gás e reduz o risco de vazamentos.",
    },
    "coffee-machine-expanded-background": {
        "name": "Máquina de Café",
        "price": 195.44,
        "description": "A máquina de café permite preparar diversas bebidas à base de café de forma rápida e prática. Com diferentes configurações, é possível ajustar a intensidade e o volume, atendendo aos mais variados paladares.",
    },
    "fan-expanded-background": {
        "name": "Ventilador",
        "price": 150.00,
        "description": "O ventilador é um aparelho essencial para manter o ambiente fresco e arejado. Disponível em modelos de mesa, coluna ou teto, ele proporciona alívio imediato em dias quentes, circulando o ar de maneira eficiente.",
    },
    "blender-expanded-background": {
        "name": "Liquidificador",
        "price": 100.00,
        "description": "O liquidificador é um utensílio indispensável na cozinha, utilizado para preparar sucos, vitaminas, sopas e diversas receitas. Com diferentes velocidades e lâminas afiadas, ele tritura e mistura ingredientes com facilidade.",
    },
    "mixer-expanded-background": {
        "name": "Batedeira",
        "price": 120.00,
        "description": "A batedeira facilita o preparo de massas, bolos e outras receitas que exigem mistura homogênea. Com opções de velocidade e diferentes batedores, ela garante consistência e leveza nas preparações culinárias.",
    },
    "washing-machine-expanded-background": {
        "name": "Máquina de Lavar Roupas",
        "price": 1500.00,
        "description": "A máquina de lavar roupas automatiza o processo de lavagem, economizando tempo e esforço. Com programas específicos para diferentes tipos de tecidos e níveis de sujeira, ela assegura roupas limpas e bem cuidadas.",
    },
    "microwave-expanded-background": {
        "name": "Micro-ondas",
        "price": 400.00,
        "description": "O micro-ondas é um eletrodoméstico versátil que aquece, descongela e até cozinha alimentos em questão de minutos. Essencial para quem busca praticidade no dia a dia, ele oferece diversas funções e ajustes de potência.",
    },
    "rice-cooker-expanded-background": {
        "name": "Panela Elétrica de Arroz",
        "price": 150.00,
        "description": "A panela elétrica de arroz cozinha o arroz de forma uniforme e no ponto certo, sem a necessidade de supervisão constante. Alguns modelos também permitem preparar legumes no vapor, ampliando sua utilidade na cozinha.",
    },
    "iron-expanded-background": {
        "name": "Ferro de Passar",
        "price": 80.00,
        "description": "O ferro de passar é fundamental para manter as roupas sem amassados e com boa aparência. Com opções a vapor ou seco, ele facilita o processo de passar roupas, garantindo resultados profissionais em casa.",
    },
    "vacuum-cleaner-expanded-background": {
        "name": "Aspirador de Pó",
        "price": 250.00,
        "description": "O aspirador de pó é um aliado na limpeza doméstica, removendo poeira e detritos de pisos, carpetes e estofados. Com diferentes acessórios e potências, ele torna a higienização mais eficiente e menos trabalhosa.",
    },
}

url = "http://localhost:8080/api/housewarming-party/product"
headers = {"Content-Type": "application/json"}
headers["Authorization"] = authenticate()
data = []


for key, item in data_map.items():
    item.update({"base64": get_item_base_64(key)})
    data.append(item)


print(data)

try:
    response = requests.post(url, headers=headers, json=data[0])
    if response.status_code == 200:
        print("Produtos enviados com sucesso!")
        print("Resposta:", response.json())
    else:
        print(f"Erro: {response.status_code}")
        print("Mensagem:", response.text)
except requests.exceptions.RequestException as e:
    print("Erro ao enviar a requisição:", e)
