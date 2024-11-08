
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
    let qrCodeProduto = document.getElementById("qrCode");

    //Verifica se todos os campos estão preenchidos
    if (valorProduto === "" || nomeProduto === "" || !imagemProduto.files.length || !qrCodeProduto.files.length) {
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

    let qrCodeCell = linha.insertCell(3);
    passarImagem(qrCodeCell, qrCodeProduto.files[0]);

    let acaoCell = linha.insertCell(4);
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

    document.getElementById('popup').style.display = 'none';
}

function attPresente(linha)
{
    let imagemProduto= document.getElementById("imagemProduto");
    let nomeProduto= document.getElementById("produto").value;
    let valorProduto= document.getElementById("valorProduto").value;
    let qrCodeProduto = document.getElementById("qrCode");

    //Verifica se todos os campos estão preenchidos
    if (valorProduto === "" || nomeProduto === "" || !imagemProduto.files.length || !qrCodeProduto.files.length) {
        alert("Por favor, preencha todos os campos e selecione uma foto.");
        return;
    }
    
    passarImagem(linha.cells[0],imagemProduto.files[0]);
    linha.cells[1].textContent = nomeProduto;
    linha.cells[2].textContent = valorProduto;
    passarImagem(linha.cells[3],qrCodeProduto.files[0]);
    
    document.getElementById("produto").value = "";
    document.getElementById("valorProduto").value = "";
    document.getElementById("imagemProduto").value = "";
    document.getElementById("qrCode").value = "";

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
    }
    else
    {
        botao.textContent = "Editar";
        botao.onclick = function() 
        {
            attPresente(linha);
        }
    }
    document.getElementById('popup').style.display = 'flex';
}

function fecharPopup()
{
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
