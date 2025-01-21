Ce projet implémente un pipeline de filtrage collaboratif distribué utilisant Hadoop. Il inclut trois jobs MapReduce qui prennent en entrée un fichier de relations utilisateur et produisent des recommandations basées sur des relations communes.

Structure du Projet
.
├── data/                           # Dossier contenant les données d'entrée
│   └── relationships/data.txt      # Fichier de relations utilisateur
├── deploy/                         # Fichiers nécessaires pour construire l'image Docker
│   ├── Dockerfile                  # Dockerfile pour configurer Hadoop et HDFS
│   ├── core-site.xml               # Configuration HDFS
│   ├── entrypoint.sh               # Script de démarrage du conteneur
│   └── ...                         # Autres fichiers de configuration
├── jars/                           # Dossier où sont copiés les JARs générés
│   ├── hadoop-tp3-collaborativeFiltering-job1-1.0.jar
│   ├── hadoop-tp3-collaborativeFiltering-job2-1.0.jar
│   └── hadoop-tp3-collaborativeFiltering-job1-1.0.jar
├── p-collaborative-filtering-job-1 # Code source et configuration du Job 1
├── p-collaborative-filtering-job-2 # Code source et configuration du Job 2
├── p-collaborative-filtering-job-3 # Code source et configuration du Job 3
└── pom.xml                         # Configuration Maven à la racine
Prérequis
Avant de commencer, assurez-vous d'avoir les outils suivants installés sur votre machine locale :

Docker : pour exécuter le conteneur Hadoop dans Docker.
Java (JDK 8) : nécessaire pour compiler les jobs Hadoop.
Maven : pour construire les projets Java.
Étapes pour Exécuter le Projet
1. Cloner le Répertoire
Clonez ce dépôt sur votre machine :

git clone <votre-lien-du-repo>
cd hadoop-tp3
2. Compiler les Jobs Hadoop
Accédez aux sous-projets et générez les JARs pour chaque job :

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
3. Copier les JARs dans le Dossier /jars
Une fois les jobs compilés, copiez les fichiers .jar générés dans le dossier jars :
4. Construire l'Image Docker
Accédez au dossier deploy et construisez l'image Docker :

cd deploy
docker build -t hadoop-tp3-img .
cd ..
5. Lancer le Conteneur Docker
Exécutez un conteneur basé sur l'image Docker que vous venez de créer :

docker run --rm -d \
  -p 8088:8088 -p 9870:9870 -p 9864:9864 \
  -v "$(pwd)/jars:/jars" \
  -v "$(pwd)/data:/data" \
  --name hadoop-tp3-cont hadoop-tp3-img
6. Accéder au Conteneur Docker
Entrez dans le conteneur Docker en tant qu'utilisateur Hadoop (epfuser) :

docker exec -it hadoop-tp3-cont su - epfuser
Étapes dans le Conteneur Docker
1. Créer les Répertoires HDFS
Créez les répertoires nécessaires dans le système HDFS :

hdfs dfs -mkdir -p /data/relationships
hdfs dfs -mkdir -p /data/output
hdfs dfs -mkdir -p /jars
2. Charger les Fichiers dans HDFS
Ajoutez le fichier d'entrée et les JARs dans HDFS :

hdfs dfs -put /data/relationships/data.txt /data/relationships/
hdfs dfs -put /jars/tpfinal-mourchid_moutuidine_job*.jar /jars
3. Exécuter les Jobs Hadoop
Exécutez les trois jobs Hadoop en séquence :

# Job 1
hadoop jar /jars/hadoop-tp3-collaborativeFiltering-job1-1.0.jar /data/relationships/data.txt /data/output/job1

# Job 2
hadoop jar /jars/hadoop-tp3-collaborativeFiltering-job2-1.0.jar /data/output/job1 /data/output/job2

# Job 3
hadoop jar /jars/hadoop-tp3-collaborativeFiltering-job3-1.0.jar /data/output/job2 /data/output/job3
4. Télécharger les Résultats Finaux
Téléchargez les résultats finaux sur votre machine locale depuis HDFS :

hdfs dfs -get /data/output/job3/part-r-00000 /data/output/final_recommendations.txt
Accès au Résultat
Les résultats finaux se trouvent dans le fichier en local (dans le projet) :

data/output/final_recommendations.txt
Points Importants
Ports Exposés :
8088 : Interface web YARN.
9870 : Interface web HDFS.
9864 : Port du DataNode HDFS.
Chemin des données :
Les données d'entrée doivent être placées dans data/relationships/data.txt.
Résultat attendu (dans final_recommendations.txt) :
alice   david:3
bob     eve:2, frank:2
charlie eve:3
david   alice:3, frank:2
eve     charlie:3, bob:2
frank   bob:2, david:2
