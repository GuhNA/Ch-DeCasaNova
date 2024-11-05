
const listaPresente = document.getElementById("listaPresentes"); //table

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
    const reader = new FileReader();
    reader.onload = function(e) {
        let linha = listaPresente.insertRow();

        let img = linha.insertCell(0);
        let imagem = document.createElement("img");
        imagem.src = e.target.result;
        img.appendChild(imagem);

        let nome = linha.insertCell(1);
        nome.innerHTML = nomeProduto;

        let valor = linha.insertCell(2);
        valor.innerHTML = valorProduto;
        linha.innerHTML +=
        `<td class="backgroundOff">
            <button onclick="attPresente()">Editar</button>
            <button onclick="removePresente()">Remover</button>
        </td>`
        /*let edit = linha.insertCell(3);
        edit.innerHTML = 
        `<button onclick="attPresente()">Editar</button>
        <button onclick="removePresente()">Remover</button>`*/
        //Limpar dados
        document.getElementById("produto").value = "";
        document.getElementById("valorProduto").value = "";
        document.getElementById("imagemProduto").value = "";

    }


    reader.readAsDataURL(imagemProduto.files[0]);

    document.getElementById('popup').style.display = 'none';



}

function fecharPopup()
{
    document.getElementById('popup').style.display = 'none';
}

function removePresente(button)
{

}
function abrirPopup() {

    document.getElementById('popup').style.display = 'flex';
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
