# TALENTOS DIGITAIS - CAIXAVERSO - BACK-END JAVA

Feito por:

Cherze Carvalho Freitas


## Descrição

Protótipo de uma API REST em JAVA 25, utilizando o framework Quarkus, que permite simulações de investimentos e financiamentos.

Persistência em banco de dados H2.

## Rodando os testes de unidade e integração

Executar no terminal "./mvnw clean verify" para o pacote java ser criado para execução em produção, serem
executados os testes de unidade e integração e para validar a cobertura dos testes com o plugin JaCoCo.

## Cobertura dos teste com JaCoCo

Arquivo HTML na pasta "/target/jacoco-report/index.html" ou
no link <http://localhost:63342/cverso-imersao2/target/jacoco-report/index.html>

## Rodando a aplicação

Ambiente de produção, no terminal executar "java -jar target/quarkus-app/quarkus-run.jar"

Ambiente de desenvolvimento, no terminal executar "quarkus dev" ou "./mvnw quarkus:dev"

## Documentação SWAGGER/OpenAPI 

No link <http://localhost:8080/q/swagger-ui/#/> intencionalmente disponível também em produção. (quarkus.swagger-ui.always-include=always)

Exemplo JSON para teste via POSTMAN:

{

"valorInicial": 1000.00,

"taxaJurosMensal": 1.5,

"prazoMeses": 12

}

Response:

{

"id": 1,

"valorInicial": 1000.00,

"taxaJurosMensal": 1.5,

"prazoMeses": 12,

"valorTotalFinal": 1195.63,

"valorTotalJuros": 195.63,

"parcelas": [

{

"mes": 1,

"saldoInicial": 1000.00,

"juros": 15.00,

"saldoFinal": 1015.00

},

{

"mes": 2,

"saldoInicial": 1015.00,

"juros": 15.23,

"saldoFinal": 1030.23

},

{

"mes": 3,

"saldoInicial": 1030.23,

"juros": 15.45,

"saldoFinal": 1045.68

},

{

"mes": 4,

"saldoInicial": 1045.68,

"juros": 15.69,

"saldoFinal": 1061.37

},

{

"mes": 5,

"saldoInicial": 1061.37,

"juros": 15.92,

"saldoFinal": 1077.29

},

{

"mes": 6,

"saldoInicial": 1077.29,

"juros": 16.16,

"saldoFinal": 1093.45

},

{

"mes": 7,

"saldoInicial": 1093.45,

"juros": 16.40,

"saldoFinal": 1109.85

},

{

"mes": 8,

"saldoInicial": 1109.85,

"juros": 16.65,

"saldoFinal": 1126.50

},

{

"mes": 9,

"saldoInicial": 1126.50,

"juros": 16.90,

"saldoFinal": 1143.40

},

{

"mes": 10,

"saldoInicial": 1143.40,

"juros": 17.15,

"saldoFinal": 1160.55

},

{

"mes": 11,

"saldoInicial": 1160.55,

"juros": 17.41,

"saldoFinal": 1177.96

},

{

"mes": 12,

"saldoInicial": 1177.96,

"juros": 17.67,

"saldoFinal": 1195.63

}

]

}