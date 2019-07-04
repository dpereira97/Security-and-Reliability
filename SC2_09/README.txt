Grupo 09
José Águas 47804
Simão Ferreira 47827
Diogo Pereira 47888

***Este projeto foi desenvolvido no ambiente dos laboratórios do DI - Linux
Xubuntu 14.04 e não foi testado noutros ambientes, como por exemplo
Windows*** 
A pasta do projeto SC2_09 tem de estar na diretoria da home do
utilizador da máquina. 
Para correr a nossa aplicação é necessário correr
primeiro o ManUsers com uma password que irá definir a password do
administrador para o servidor e ManUsers. 
Para registar utilizadores na aplicação é necessário escolher a operação “registar”
e seguir os passos indicados, para mudar a password é escolher a operação “mudarPass”,
para apagar um utilizador é necessário escolher “apagar”, no final é necessário
sair do ManUsers com a operação “sair” pois esta submete todas as operações
anteriores. 
É necessário ter-se registado pelo menos um utilizador, depois é
correr o PhotoShareServer dando a password do administrador, isto vai fazer
com que o servidor inicie e fique à espera que os clientes se conectem. 
O PhotoShareServer deve ser corrido no porto 23232 (parâmetro execução). 
Depois deste processo executado pode-se começar a ligar clientes no IP da máquina do
servidor no porto 23232 onde os parâmetros de execução estão especificados no
output. 
Assumimos que o cliente e o servidor já têm as keystores criadas e
prontas a utilizar, nós utilizámos os seguintes comandos para o fazer: $
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -keystore
servidor/keystore/myServer.keyStore com a password: toupas404 com o seguinte
certificado: CN=App PhotoShareServer, OU=DI, O=FCUL, L=LX, ST=LX, C=PT,
exportamos o certificado com o seguinte comando: keytool -exportcert -alias
server -file certServer.cer -keystore servidor/keystore/myServer.keyStore e
utilizamos a password definida na keystore do servidor. 
Importamos o certificado do servidor para a keystore do cliente com este comando: keytool
-importcert -alias server -file certServer.cer -keystore myClient.keyStore
com a password: toupas404. 
Iremos explicar como resolver este problema no relatório.