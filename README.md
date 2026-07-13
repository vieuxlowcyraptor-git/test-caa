Spec:
- Créer une application Web qui permet de lire et de stocker les messages déposés sur une file IBM MQ Series dans une base de données relationnelle,
- D’exposer des API REST pour la consultation des messages via une IHM

Environnement technique :
- Projet Maven : 3.9.16
- Java : 26
- Spring Boot : 4.1.0
- Angular : 22
- Base de données relationnelle : H2

Choix techniques :
- Backend lit les messages par 10 toutes les 30s avec @Scheduled et les enregistres dans une base local H2.
- Utilisation de spring JPA pour l'écriture et la lecture des données en base.
- Pas de connaisance IBM MQ Series ni de moyen de tester correctement -> mock des réponses au service back,

Installation :
- testapp/mvn clean install

Démarrage :
- SpringBoot App: fr.alexanj.testapp.App
- Angular : npm start
