function gerarQRCode() {
    // Texto ou link que será convertido em QR Code
    const texto = window.location.href; 

    // Cria um div temporário para gerar o QR Code
    const tempDiv = document.createElement("div");

    // Gera o QR Code
    new QRCode(tempDiv, {
        text: texto,
        width: 128, // Largura
        height: 128, // Altura
        colorDark: "#000000", // Cor do QR Code
        colorLight: "#ffffff", // Cor de fundo
        correctLevel: QRCode.CorrectLevel.H // Nível de correção
    });

    // Recupera a imagem gerada
    setTimeout(() => {
        const qrCodeImg = tempDiv.querySelector("img");
        if (qrCodeImg) {
            // Seleciona o elemento <img id="QrCodeImg">
            const imgElement = document.getElementById("QrCodeImg");
            // Define o src da imagem gerada no <img>
            imgElement.src = qrCodeImg.src;
        }
    }, 100); // Timeout para garantir que a imagem é gerada
}

// Chamada inicial para gerar o QR Code (opcional)
gerarQRCode();

function copiarUrlAtual() {
    // Obtém a URL atual
    const urlAtual = window.location.href;

    // Cria um elemento de texto temporário
    const tempInput = document.createElement("input");
    tempInput.value = urlAtual;

    // Adiciona ao documento para copiar o texto
    document.body.appendChild(tempInput);
    tempInput.select();
    tempInput.setSelectionRange(0, 99999); // Compatibilidade com dispositivos móveis

    // Executa o comando de cópia
    document.execCommand("copy");

    // Remove o elemento temporário
    document.body.removeChild(tempInput);

    // Opcional: Exibe uma mensagem de confirmação
    alert("URL copiada para a área de transferência!");
}