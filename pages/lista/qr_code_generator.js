function gerarQRCode() {
    apiClient.get("/user/logged-user")
        .then(response => {
            const userId = response.data

            // Get the current URL
            const currentUrl = window.location.href;
            const folderAbove = currentUrl.substring(0, currentUrl.lastIndexOf('/'))

            // Define the new relative path and query string
            const newPath = "lista_publica/lista.html";
            const queryString = `?lista=${userId}`;

            // Construct the full URL
            const baseUrl = folderAbove .substring(0, folderAbove.lastIndexOf("/") + 1); // Get the base URL
            const newUrl = baseUrl + newPath + queryString;

            // Cria um div temporário para gerar o QR Code
            const tempDiv = document.createElement("div");

            // Gera o QR Code
            new QRCode(tempDiv, {
                text: newUrl,
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
        })
        .catch(error => {
            console.error("Erro ao buscar ID de usuário", error);
            alert("Erro ao buscar ID de usuário. Tente novamente mais tarde.");
        });
}

// Chamada inicial para gerar o QR Code (opcional)
gerarQRCode();

function copiarUrlAtual() {
    apiClient.get("/user/logged-user")
        .then(response => {
            const userId = response.data

            // Get the current URL
            const currentUrl = window.location.href;
            const folderAbove = currentUrl.substring(0, currentUrl.lastIndexOf('/'))

            // Define the new relative path and query string
            const newPath = "lista_publica/lista.html";
            const queryString = `?lista=${userId}`;

            // Construct the full URL
            const baseUrl = folderAbove .substring(0, folderAbove.lastIndexOf("/") + 1); // Get the base URL
            const newUrl = baseUrl + newPath + queryString;

            const tempInput = document.createElement("input");
            tempInput.value = newUrl ;

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
    })
    .catch(error => {
        console.error("Erro ao buscar ID de usuário", error);
        alert("Erro ao buscar ID de usuário. Tente novamente mais tarde.");
    });
}
