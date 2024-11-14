# Documentation

## Swagger
La génération de documentation swagger est automatique au démarrage de l'application. Les class annotées @RestController sont automatiquement mappées et leurs attributs sont mis de base dans swagger.

### URL
Les documents swagger sont trouvable à l'URL :
- http://localhost:8080/documentation/swagger.html
- http://localhost:8080/documentation/swagger/JSON
## Javadoc
Pour modifier la javadoc il faut d'abord : 
- Générer la javadoc via maven ``mvn javadoc:javadoc``
- Copier le contenu de ``target/site/apidocs`` vers ``resources/static/apidocs``
- Relancer l'application
### URL
Vous pouvez trouver la documentation à l'url 
- http://localhost:8080/documentation/javadoc
- http://localhost:8080