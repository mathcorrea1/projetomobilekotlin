# 🛒 MyCart

## 📱 Descrição
O **MyCart** é um aplicativo Android nativo desenvolvido em **Kotlin** com o objetivo de facilitar a criação e o gerenciamento de **listas de compras** de forma prática e intuitiva.  
O app permite que o usuário cadastre-se, faça login, crie listas personalizadas, adicione itens com informações detalhadas (quantidade, unidade e categoria), e marque os produtos já comprados.  

O projeto foi desenvolvido como parte da disciplina **Programação Mobile I**, com foco na aplicação dos conceitos de **ViewBinding**, **Material Design**, **RecyclerView** e gerenciamento de dados em memória.

---

## ⚙️ Funcionalidades Principais

### 🔐 RF001 - Login / Logout
- Tela de login com campos de **e-mail** e **senha**, além do **logotipo da aplicação**.  
- Validação de formato de e-mail e campos obrigatórios.  
- Autenticação simulada (dados mocados).  
- Função de **logout** redirecionando para a tela inicial.

### 👤 RF002 - Cadastro de Usuário
- Permite o cadastro de novos usuários.  
- Campos: **Nome, E-mail, Senha e Confirmação de Senha**.  
- Valida preenchimento completo e correspondência de senhas.  

### 🧾 RF003 - Gestão de Listas de Compras
- Criação, edição e exclusão de listas de compras.  
- Cada lista possui **título** e **imagem opcional**.  
- Exibição das listas em **RecyclerView**, ordenadas alfabeticamente.  
- Remoção de listas exclui automaticamente os itens associados.

### 🛍️ RF004 - Gestão de Itens da Lista
- Criação, edição e exclusão de **itens dentro das listas de compras**.  
- Cada item contém **nome, quantidade, unidade e categoria**.  
- Itens agrupados por categoria e ordenados alfabeticamente.  
- Possibilidade de marcar itens como **comprados**, sendo listados separadamente.

### 🔎 RF005 - Buscas
- Sistema de busca eficiente para **filtrar listas e itens** por nome.  

---

## 🧩 Tecnologias Utilizadas

| Tecnologia | Descrição |
|-------------|------------|
| **Kotlin** | Linguagem principal para desenvolvimento Android. |
| **Android Studio** | IDE utilizada para o desenvolvimento e testes. |
| **ViewBinding** | Acesso seguro aos elementos de interface. |
| **RecyclerView** | Exibição dinâmica e performática das listas. |
| **Material Design** | Padrão de design moderno e responsivo. |
| **Intent e Activity Navigation** | Gerenciamento de navegação entre telas. |

---

## 🧑‍💼 Desenvolvedores

* Vitor Mussi Dalpino	
* Matheus Da Silva Correa	
* Lucas Queiroz

## 📚 Contexto Acadêmico

Projeto desenvolvido para a disciplina Programação Mobile I, com o objetivo de aplicar conceitos de desenvolvimento Android nativo, manipulação de dados em memória e boas práticas de usabilidade segundo as diretrizes do Material Design.


