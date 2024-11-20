document.addEventListener("DOMContentLoaded", () => {
    fetchAvailableProducts();
});

async function getLoggedUserId() {
    try {
        const response = await apiClient.get("/user/logged-user");
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar usuário logado:", error);
        alert("Erro ao obter o usuário logado. Tente novamente mais tarde.");
    }
}

async function fetchAvailableProducts() {
    try {
        const loggedUserId = await getLoggedUserId();
        const response = await apiClient.get("/product?status=AVAILABLE&userId=" + loggedUserId);
        const products = response.data;
        populateProductTable(products);
    } catch (error) {
        console.error("Erro ao buscar produtos:", error);
        alert("Erro ao carregar a lista de produtos. Tente novamente mais tarde.");
    }
}

async function fetchUnavailableProducts() {
    try {
        const loggedUserId = await getLoggedUserId();
        const response = await apiClient.get(`/product?status=UNAVAILABLE&userId=${loggedUserId}`);
        const products = response.data;
        populateProductTable(products); // Reutiliza a função existente para preencher a tabela
    } catch (error) {
        console.error("Erro ao buscar presentes recebidos:", error);
        alert("Erro ao carregar os presentes recebidos. Tente novamente mais tarde.");
    }
}

function fetchProductDetails(productId) {
    apiClient.get(`/product/${productId}`)
        .then(response => {
            const product = response.data;

            // Preencher os campos do formulário com os dados do produto
            document.getElementById("produto").value = product.name;
            document.getElementById("valorProduto").value = `R$ ${product.price.toFixed(2).replace('.', ',')}`;
            document.getElementById("descricaoProduto").value = product.description;

            // Carregar a imagem do produto
            const imgElement = document.getElementById("imagemProduto");
            imgElement.src = `data:image/png;base64,${product.image.base64}`;

            // Salvar o ID do produto no botão de edição
            document.getElementById("metamorfo").dataset.productId = product.id;
            
            // Aqui você pode carregar a lista de imagens pre-cadastradas para selecionar, se necessário.
            loadPreloadedImages();
        })
        .catch(error => {
            console.error("Erro ao obter detalhes do produto:", error);
            alert("Erro ao carregar os detalhes do produto.");
        });
}

function populateProductTable(products) {
    const listaPresente = document.getElementById("listaPresentes");
    listaPresente.innerHTML = ""; // Limpa a tabela antes de preenchê-la novamente

    products.forEach(product => {
        const row = listaPresente.insertRow();

        // Coluna da imagem
        const imgCell = row.insertCell(0);
        const img = document.createElement("img");
        img.src = `data:image/png;base64,${product.image.base64}`;
        img.alt = product.name;
        img.width = 120;
        img.height = 120;
        imgCell.appendChild(img);

        // Coluna do nome
        const nameCell = row.insertCell(1);
        nameCell.innerHTML = `${product.name}<br>${product.description.substring(0,40)}...`;
        // Coluna do preço
        const priceCell = row.insertCell(2);
        priceCell.textContent = `R$ ${formatarMoeda(product.price.toFixed(2).replace('.', ',').toString())}`;
        // Coluna de ações (botões)
        const actionCell = row.insertCell(3);
        actionCell.classList.add("action-buttons");

        // Botão Editar
        const editButton = document.createElement("button");
        editButton.textContent = "Editar";
        editButton.className = "button is-info is-small";
        editButton.onclick = () => abrirPopup(editButton, row); // Reutiliza a lógica existente
        actionCell.appendChild(editButton);

        // Botão Remover
        const removeButton = document.createElement("button");
        removeButton.textContent = "Remover";
        removeButton.className = "button is-danger is-small";
        removeButton.onclick = () => removeProduct(product.id, row); // Chama a função para remover
        actionCell.appendChild(removeButton);
    });
}

function loadPreloadedImages() {
    const selectElement = document.getElementById("imagemPreCadastrada");
    apiClient.get("/image")
        .then(response => {
            const images = response.data;
            selectElement.innerHTML = ""; // Limpar opções anteriores

            // Adicionar as opções de imagem pre-cadastradas
            images.forEach(image => {
                const option = document.createElement("option");
                option.value = image.id;
                option.textContent = `Imagem ${image.id}`;
                selectElement.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Erro ao carregar imagens pre-cadastradas:", error);
        });
}

function attPresente(row) {
    const produtoId = document.getElementById("metamorfo").dataset.productId;
    const nomeProduto = document.getElementById("produto").value;
    const valorProduto = parseFloat(document.getElementById("valorProduto").value.replace("R$", "").replaceAll(".","").replace(",","."));
    const descricaoProduto = document.getElementById("descricaoProduto").value;
    const imagemPreCadastrada = document.getElementById("imagemPreCadastrada").value;
    const imagemProduto = document.getElementById("imagemProduto").files[0];

    let base64Image = null;

    if (imagemProduto) {
        // Se o usuário fez upload de uma nova imagem
        const reader = new FileReader();
        reader.onload = function(e) {
            base64Image = e.target.result.split(',')[1]; // Pega somente a parte base64 da imagem
            sendEditRequest();
        };
        reader.readAsDataURL(imagemProduto);
    } else {
        // Se o usuário selecionou uma imagem pre-cadastrada
        base64Image = null;
        sendEditRequest();
    }

    function sendEditRequest() {
        const payload = {
            name: nomeProduto,
            price: valorProduto,
            description: descricaoProduto,
            image: { id: imagemPreCadastrada || null },
            base64Image: base64Image
        };

        apiClient.put(`/product/${produtoId}`, payload)
            .then(response => {
                alert("Produto atualizado com sucesso!");
                // Atualizar a linha na tabela
                row.cells[1].textContent = nomeProduto;
                //row.cells[2].textContent = `R$ ${valorProduto.toFixed(2).replace('.', ',')}`;
                row.cells[2].textContent = formatarMoeda(valorProduto.toString());
                row.cells[3].textContent = descricaoProduto;

                if (base64Image) {
                    row.cells[0].innerHTML = `<img src="data:image/png;base64,${base64Image}" width="120" height="120">`;
                }
                fecharPopup();
            })
            .catch(error => {
                console.error("Erro ao atualizar o produto:", error);
                alert("Erro ao atualizar o produto.");
            });
    }
}

function removeProduct(productId, row) {
    if (confirm("Tem certeza de que deseja remover este produto?")) {
        apiClient.delete(`/product/${productId}`)
            .then(() => {
                alert("Produto removido com sucesso!");
                row.remove(); // Remove a linha da tabela após sucesso na remoção
            })
            .catch(error => {
                console.error("Erro ao remover o produto:", error);
                alert("Não foi possível remover o produto. Tente novamente.");
            });
    }
}


const listaPresente = document.getElementById("listaPresentes"); //tbody


/*function addPresente()
{

    let imagemProduto= document.getElementById("imagemProduto");
    let nomeProduto= document.getElementById("produto").value;
    let valorProduto= document.getElementById("valorProduto").value;

    //Verifica se todos os campos estão preenchidos
    if (valorProduto === "" || nomeProduto === "" || !imagemProduto.files.length) {
        alert("Por favor, preencha todos os campos e selecione uma foto.");
        return;
    }

    let linha = listaPresente.insertRow();
    let imgCell = linha.insertCell(0);
    passarImagem(imgCell,imagemProduto.files[0]);

    let nome = linha.insertCell(1);
    nome.textContent = nomeProduto;

    let valor = linha.insertCell(2);
    valor.textContent = valorProduto;

    let acaoCell = linha.insertCell(3);
    //acaoCell.classList.add("backgroundOff")

    let editButton = document.createElement("button");
    editButton.textContent = "Editar";
    editButton.className = "button is-info is-small";
    editButton.onclick = () => abrirPopup(editButton, linha)
    acaoCell.appendChild(editButton);

    removeButton = document.createElement("button");
    removeButton.textContent = "Remover";
    removeButton.className = "button is-danger is-small";
    removeButton.onclick = () => listaPresente.removeChild(linha);
    acaoCell.appendChild(removeButton);

    document.getElementById("produto").value = "";
    document.getElementById("valorProduto").value = "";
    document.getElementById("imagemProduto").value = "";
    document.getElementById("qrCode").value = "";

    document.getElementsByClassName("popup-content")[0].style.display = "none";
    document.getElementById('popup').style.display = 'none';
}

function attPresente(linha)
{
    let imagemProduto= document.getElementById("imagemProduto");
    let nomeProduto= document.getElementById("produto").value;
    let valorProduto= document.getElementById("valorProduto").value;

    //Verifica se todos os campos estão preenchidos
    if (valorProduto === "" || nomeProduto === "" || !imagemProduto.files.length) {
        alert("Por favor, preencha todos os campos e selecione uma foto.");
        return;
    }
    
    passarImagem(linha.cells[0],imagemProduto.files[0]);
    linha.cells[1].textContent = nomeProduto;
    linha.cells[2].textContent = valorProduto;
    
    document.getElementById("produto").value = "";
    document.getElementById("valorProduto").value = "";
    document.getElementById("imagemProduto").value = "";

    document.getElementsByClassName("popup-content")[0].style.display ="none";
    document.getElementById('popup').style.display = 'none';
}*/

async function addPresente()
{
    const imagemProduto= document.getElementById("imagemProduto").files[0];
    const nomeProduto= document.getElementById("produto").value;
    const valorProduto= document.getElementById("valorProduto").value;
    const descricaoProduto = document.getElementById("descricaoProduto").value;
    const imagemPreCadastrada = document.getElementById("imagemPreCadastrada").value;

    const valor = parseFloat(valorProduto.slice(3).replaceAll(".","").replace(",","."));
    let base64 = null;
    //Verifica se todos os campos estão preenchidos
    if (valorProduto === "" || nomeProduto === "" || descricaoProduto === "") {
        alert("Por favor, preencha todos os campos");
        return;
    }

    if (!imagemPreCadastrada && imagemProduto) {
        try {
            base64 = await lerImagemBase64(imagemProduto);
        } catch (error) {
            console.error("Erro ao processar a imagem:", error);
            return;
        }
    }

function lerImagemBase64(imagemProduto) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = function(e) {
            resolve(e.target.result.split(',')[1]); // Retorna apenas a parte Base64
        };
        reader.onerror = function(err) {
            reject(err);
        };
        reader.readAsDataURL(imagemProduto);
    });
}

    const payload = 
    {
        name: nomeProduto,
        price: valor,
        description: descricaoProduto,
        image: imagemPreCadastrada && imagemPreCadastrada != ""  ? {id: imagemPreCadastrada} : null,
        base64Image: base64
    };

    apiClient.post("/product",payload);
    fetchAvailableProducts();


    document.getElementById("produto").value = "";
    document.getElementById("valorProduto").value = "";
    document.getElementById("imagemProduto").value = "";

    document.getElementsByClassName("popup-content")[0].style.display ="none";
    document.getElementById('popup').style.display = 'none';
}

function addQrCode()
{
    
    let imagem = document.getElementById("qrCode");

    if (!imagem.files.length) {
        alert("Por favor, preencha todos os campos e selecione uma foto.");
        return;
    }
    const reader = new FileReader();
    reader.onload = function(e) {
        let img = document.getElementById("QrCodeImg");
        img.src = e.target.result;
        img.width = 120;
        img.height = 120;
    }
    reader.readAsDataURL(imagem.files[0]);

    document.getElementById("QRCODE").textContent = "Editar QR Code";
    document.getElementById("qrCode").value = "";

    document.getElementsByClassName("popup-content")[1].style.display = "none";
    document.getElementById('popup').style.display = 'none';

}

function abrirPopup(button, row = null) {
    const botao = document.getElementById("metamorfo");

    // Caso seja o botão de adicionar um novo produto
    if (button.id === "add") {
        botao.textContent = "Adicionar";
        botao.onclick = function () {
            addPresente();
        };
        document.getElementsByClassName("popup-content")[0].style.display = "block";
    } 
    // Caso seja para editar um produto existente
    else if (row) {
        console.log(row)
        const produtoId = row.cells[0].dataset.productId; // Certifique-se de que 'produtoId' esteja definido na célula.

        if (!produtoId) {
            console.error("ID do produto não encontrado.");
        } else {
            // Preencher o formulário com os dados do produto
            botao.textContent = "Editar";
            botao.onclick = function () {
                attPresente(row);
            };

            // Obter os detalhes do produto
            fetchProductDetails(produtoId);
        }

        document.getElementsByClassName("popup-content")[0].style.display = "block";
    }
    // Caso seja para adicionar QR Code
    else if (button.id === "QRCODE") {
        document.getElementsByClassName("popup-content")[1].style.display = "block";
    }

    document.getElementById('popup').style.display = 'flex';
}

function fecharPopup(index)
{
    document.getElementById("produto").value = "";
    document.getElementById("valorProduto").value = "";
    document.getElementById("imagemProduto").value = "";
    document.getElementById("descricaoProduto").value = "";
    document.getElementsByClassName("popup-content")[index].style.display ="none";
    document.getElementById('popup').style.display = 'none';
}

//Formatando moeda
function formatarMoeda(input) {
    let valor;
    if(input.value === undefined) valor = input;
    else valor = input.value;

    //Remove tudo que não é dígito
    valor = valor.replace(/\D/g, "");

    //Divide os centavos
    valor = (valor / 100).toFixed(2) + "";
    
    //Adiciona separador de milhar
    valor = valor.replace(".", ",");
    valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    
    //Atualiza o valor formatado com símbolo da moeda
    input.value = "R$ " + valor;

    if(input.value === undefined) return valor;
}
