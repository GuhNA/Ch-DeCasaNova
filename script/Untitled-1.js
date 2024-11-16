const login = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/digital-pec/authenticate', {
                username: email.value,
                password: password.value
            })

            const token = response.headers['authorization']
            if (token) {
                localStorage.setItem('authToken', token)

                const decoded = jwtDecode(token)
                localStorage.setItem('name', decoded.name)

                username.value = decoded.name

                router.push({ name: 'Home' })
            } else {
                console.error('Authorization token not found in headers')
            }
        } catch (error) {
            console.error('Login failed:', error)
        }
    }


    
        // Configurar o corpo da requisição
        const requestData = {
            email: email,
            password: password
        };

        axios.post('http://localhost/login', requestData)
            .then(response => {
                document.getElementById('message').textContent = 'Login bem-sucedido!';
                document.getElementById('message').style.color = 'green';
            })
            .catch(error => {
                document.getElementById('message').textContent = 'Erro ao fazer login. Verifique as credenciais.';
                document.getElementById('message').style.color = 'red';
                console.error(error);
            });