Spec:
Créer une application Web qui permet de lire et de stocker les messages déposés sur une file IBM MQ Series dans une base de données relationnelle
D’exposer des API REST pour la consultation des messages via une IHM

Environnement technique :
- Langage : Java 21+
- Framework Spring Boot 4+
- Framework Angular 22
- Base de données relationnelle : aucune préférence
- Projet Maven
- Documentation pour tester l’application

Installation :
testapp/mvn clean install

Choix technique :
Pas de connaisance IBM MQ Series ni de moyen de tester correctement -> mock des réponses au service back
Backend lit les messages par 10 toutes les 30s avec @Scheduled et les enregistres dans une base local H2.
Utilisation de spring JPA pour l'écriture et la lecture des données en base.
