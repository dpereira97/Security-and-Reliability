Grupo 09
Jos� �guas 47804
Sim�o Ferreira 47827
Diogo Pereira 47888

***Este projeto foi desenvolvido no ambiente dos laborat�rios do DI - Linux
Xubuntu 14.04 e n�o foi testado noutros ambientes, como por exemplo
Windows*** 
A pasta do projeto SC2_09 tem de estar na diretoria da home do
utilizador da m�quina. 
Para correr a nossa aplica��o � necess�rio correr
primeiro o ManUsers com uma password que ir� definir a password do
administrador para o servidor e ManUsers. 
Para registar utilizadores na aplica��o � necess�rio escolher a opera��o �registar�
e seguir os passos indicados, para mudar a password � escolher a opera��o �mudarPass�,
para apagar um utilizador � necess�rio escolher �apagar�, no final � necess�rio
sair do ManUsers com a opera��o �sair� pois esta submete todas as opera��es
anteriores. 
� necess�rio ter-se registado pelo menos um utilizador, depois �
correr o PhotoShareServer dando a password do administrador, isto vai fazer
com que o servidor inicie e fique � espera que os clientes se conectem. 
O PhotoShareServer deve ser corrido no porto 23232 (par�metro execu��o). 
Depois deste processo executado pode-se come�ar a ligar clientes no IP da m�quina do
servidor no porto 23232 onde os par�metros de execu��o est�o especificados no
output. 
Assumimos que o cliente e o servidor j� t�m as keystores criadas e
prontas a utilizar, n�s utiliz�mos os seguintes comandos para o fazer: $
keytool -genkeypair -alias server -keyalg RSA -keysize 2048 -keystore
servidor/keystore/myServer.keyStore com a password: toupas404 com o seguinte
certificado: CN=App PhotoShareServer, OU=DI, O=FCUL, L=LX, ST=LX, C=PT,
exportamos o certificado com o seguinte comando: keytool -exportcert -alias
server -file certServer.cer -keystore servidor/keystore/myServer.keyStore e
utilizamos a password definida na keystore do servidor. 
Importamos o certificado do servidor para a keystore do cliente com este comando: keytool
-importcert -alias server -file certServer.cer -keystore myClient.keyStore
com a password: toupas404. 
Iremos explicar como resolver este problema no relat�rio.