document.addEventListener("DOMContentLoaded", () => {
    fetchAvailableProducts();
});

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/housewarming-party/',
  headers: {
    'Content-Type': 'application/json'
  }
})

function fetchAvailableProducts() {
    const path_params = new URLSearchParams(window.location.search);
    if (!path_params.get('lista')){
        window.location.href = '../../index.html';
    }
    apiClient.get(`/product?status=AVAILABLE&userId=${path_params.get('lista')}`)
        .then(response => {
            const products = response.data;
            populateProductTable(products);
        })
        .catch(error => {
            console.error("Erro ao buscar produtos:", error);
            alert("Erro ao carregar a lista de produtos. Tente novamente mais tarde.");
        });
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
        nameCell.innerHTML = `<strong class="product-title">${product.name}</strong><br>${product.description.substring(0, 40)}...`;

        // Coluna do preço
        const priceCell = row.insertCell(2);
        priceCell.textContent = `R$ ${product.price.toFixed(2).replace('.', ',')}`;

        // Coluna de ações (botões)
        const actionCell = row.insertCell(3);
        actionCell.className = "align-content-center"
        const viewQrCodeButton = document.createElement("button");
        viewQrCodeButton.textContent = "Ver QR Code";
        viewQrCodeButton.className = "button is-primary is-small";
        viewQrCodeButton.onclick = () => openProductModal(product.id);
        actionCell.appendChild(viewQrCodeButton);
    });
}

function openProductModal(productId) {
    const modal = document.getElementById("productModal");
    const qrCodeImage = document.getElementById("modalQrCode");
    const presentarButton = document.getElementById("presentarButton");

    // Obter o QR Code do produto
    apiClient.get(`/product/${productId}`)
        .then(response => {
            const product = response.data;
            qrCodeImage.src = `data:image/png;base64,${product.qrCodeImage}`;
            modal.classList.add("is-active");

            // Configurar ação do botão "Presentear"
            presentarButton.onclick = () => donateProduct(productId);
        })
        .catch(error => {
            console.error("Erro ao carregar o QR Code:", error);
            alert("Não foi possível carregar o QR Code. Tente novamente mais tarde.");
        });

    // Configurar ação do botão "Cancelar"
    const cancelButton = document.getElementById("cancelButton");
    cancelButton.onclick = () => modal.classList.remove("is-active");

    // Fechar ao clicar no 'X' ou fora da modal
    modal.querySelector(".modal-close").onclick = () => modal.classList.remove("is-active");
    modal.querySelector(".modal-background").onclick = () => modal.classList.remove("is-active");
}

function donateProduct(productId) {
    apiClient.put(`/product/${productId}/donate`)
        .then(() => {
            alert("Produto presenteado com sucesso!");
            document.getElementById("productModal").classList.remove("is-active");
            fetchAvailableProducts(); 
        })
        .catch(error => {
            console.error("Erro ao presentear o produto:", error);
            alert("Erro ao processar o presente. Tente novamente mais tarde.");
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
    const valorProduto = parseFloat(document.getElementById("valorProduto").value.replace("R$", "").replace(",", "."));
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
                row.cells[2].textContent = `R$ ${valorProduto.toFixed(2).replace('.', ',')}`;
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

const listaPresente = document.getElementById("listaPresentes"); //tbody

function passarImagem(cell, imagem)
{
    const reader = new FileReader();
    reader.onload = function(e) {
        cell.innerHTML="";
        let img = document.createElement("img");
        img.src = e.target.result;
        img.width = 120;
        img.height = 120;
        cell.appendChild(img);
    }
    reader.readAsDataURL(imagem);
}

//Formatando moeda
function formatarMoeda(input) {
    let valor = input.value;

    //Remove tudo que não é dígito
    valor = valor.replace(/\D/g, "");

    //Divide os centavos
    valor = (valor / 100).toFixed(2) + "";
    
    //Adiciona separador de milhar
    valor = valor.replace(".", ",");
    valor = valor.replace(/\B(?=(\d{3})+(?!\d))/g, ".");
    
    //Atualiza o valor formatado com símbolo da moeda
    input.value = "R$ " + valor;
}
