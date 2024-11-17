document.addEventListener("DOMContentLoaded", () => {
    fetchAvailableProducts();
});

function fetchAvailableProducts() {
    const url = "http://localhost:8080/api/housewarming-party/product?status=AVAILABLE&userId=1";
    axios.get(url)
        .then(response => {
            const products = response.data;
            populateProductTable(products);
        })
        .catch(error => {
            console.error("Erro ao buscar produtos:", error);
            alert("Erro ao carregar a lista de produtos. Tente novamente mais tarde.");
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
        nameCell.textContent = product.name;

        // Coluna do preço
        const priceCell = row.insertCell(2);
        priceCell.textContent = `R$ ${product.price.toFixed(2).replace('.', ',')}`;

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
        removeButton.onclick = () => listaPresente.deleteRow(row.rowIndex - 1); // Remove a linha da tabela
        actionCell.appendChild(removeButton);
    });
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

function addPresente()
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
    editButton.onclick = () => abrirPopup(editButton, linha)
    acaoCell.appendChild(editButton);

    removeButton = document.createElement("button");
    removeButton.textContent = "Remover";
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

function abrirPopup(button, linha) {
    botao = document.getElementById("metamorfo")
    if(button.id === "add")
    {
        botao.textContent = "Adicionar";
        botao.onclick = function()
        {
            addPresente();
        }
        document.getElementsByClassName("popup-content")[0].style.display = "block";
    }
    else if(button.id === "QRCODE")
    {
        document.getElementsByClassName("popup-content")[1].style.display = "block";
    }
    else
    {
        botao.textContent = "Editar";
        botao.onclick = function() 
        {
            attPresente(linha);
        }
        document.getElementsByClassName("popup-content")[0].style.display = "block";
    }
    document.getElementById('popup').style.display = 'flex';
}

function fecharPopup(index)
{
    document.getElementsByClassName("popup-content")[index].style.display ="none";
    document.getElementById('popup').style.display = 'none';
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
