Projet : Pipeline de Filtrage Collaboratif Distribué avec Hadoop
Ce projet met en œuvre un pipeline de filtrage collaboratif distribué, construit à l’aide de Hadoop. Il repose sur trois jobs MapReduce successifs qui analysent un fichier de relations utilisateur et génèrent des recommandations basées sur des connexions partagées.

Prérequis
Pour exécuter ce projet, assurez-vous que les outils suivants sont installés sur votre machine :

Docker : pour exécuter Hadoop dans un conteneur.
Java (JDK 8) : nécessaire pour compiler les projets Java.
Maven : utilisé pour la gestion des dépendances et la compilation.
Étapes pour Lancer le Projet
1. Cloner le Dépôt
Récupérez les fichiers du projet avec la commande suivante :

git clone <votre-lien-du-repo>  
cd hadoop-tp3  
2. Compiler les Jobs Hadoop
Compilez les trois projets Java MapReduce pour générer les fichiers JAR nécessaires :

# Compiler le Job 1  
cd p-collaborative-filtering-job-1  
mvn clean install  
cd ..  

# Compiler le Job 2  
cd p-collaborative-filtering-job-2  
mvn clean install  
cd ..  

# Compiler le Job 3  
cd p-collaborative-filtering-job-3  
mvn clean install  
cd ..  
3. Déplacer les JARs dans le Répertoire jars/
Une fois compilés, copiez les fichiers JAR dans le dossier jars/.

4. Construire l’Image Docker
Placez-vous dans le dossier deploy/ et générez l’image Docker :


cd deploy  
docker build -t hadoop-tp3-img .  
cd ..  
5. Démarrer le Conteneur Docker
Lancez un conteneur en utilisant l’image créée :


docker run --rm -d \  
  -p 8088:8088 -p 9870:9870 -p 9864:9864 \  
  -v "$(pwd)/jars:/jars" \  
  -v "$(pwd)/data:/data" \  
  --name hadoop-tp3-cont hadoop-tp3-img  
6. Entrer dans le Conteneur
Pour interagir avec Hadoop, accédez au conteneur en tant qu’utilisateur epfuser :


docker exec -it hadoop-tp3-cont su - epfuser  
Étapes à Réaliser dans le Conteneur
1. Créer les Répertoires HDFS
Créez les répertoires nécessaires dans Hadoop Distributed File System (HDFS) :

bash
Copier
Modifier
hdfs dfs -mkdir -p /data/relationships  
hdfs dfs -mkdir -p /data/output  
hdfs dfs -mkdir -p /jars  
2. Charger les Fichiers dans HDFS
Transférez les fichiers d’entrée et les JARs dans HDFS :

hdfs dfs -put /data/relationships/data.txt /data/relationships/  
hdfs dfs -put /jars/*.jar /jars  
3. Lancer les Jobs Hadoop
Exécutez les trois jobs MapReduce dans l’ordre suivant :

# Job 1  
hadoop jar /jars/hadoop-tp3-collaborativeFiltering-job1-1.0.jar /data/relationships/data.txt /data/output/job1  

# Job 2  
hadoop jar /jars/hadoop-tp3-collaborativeFiltering-job2-1.0.jar /data/output/job1 /data/output/job2  

# Job 3  
hadoop jar /jars/hadoop-tp3-collaborativeFiltering-job3-1.0.jar /data/output/job2 /data/output/job3  
4. Télécharger les Résultats
Récupérez le fichier de résultats sur votre machine locale :

hdfs dfs -get /data/output/job3/part-r-00000 /data/output/final_recommendations.txt  
Résultat Attendu
Le fichier final final_recommendations.txt contient les recommandations sous ce format :


alice   david:3  
bob     eve:2, frank:2  
charlie eve:3  
david   alice:3, frank:2  
eve     charlie:3, bob:2  
frank   bob:2, david:2  
Ports Exposés
8088 : Interface YARN.
9870 : Interface HDFS.
9864 : Port du DataNode HDFS.
Répertoire des Données
Les données initiales doivent être placées dans data/relationships/data.txt. Les résultats finaux se trouvent dans data/output/final_recommendations.txt.

